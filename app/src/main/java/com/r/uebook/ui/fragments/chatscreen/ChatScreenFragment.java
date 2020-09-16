package com.r.uebook.ui.fragments.chatscreen;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.r.uebook.BaseApplication;
import com.r.uebook.R;
import com.r.uebook.data.Repository.Repository;
import com.r.uebook.data.database.DatabaseClient;
import com.r.uebook.data.database.entity.MessageDataDb;
import com.r.uebook.data.database.entity.StaredMessage;
import com.r.uebook.data.remote.model.Friend;
import com.r.uebook.data.remote.model.UserProfile;
import com.r.uebook.data.remote.model.socket.DeleteEventResponse;
import com.r.uebook.data.remote.model.socket.SocketMessageResponse;
import com.r.uebook.data.remote.model.socket.SocketModel;
import com.r.uebook.ui.adapters.ChatListAdapter;
import com.r.uebook.utils.AppExecutors;
import com.r.uebook.utils.AppUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import timber.log.Timber;

public class ChatScreenFragment extends Fragment {

    private NavController navController;
    private Repository repository;
    private ChatScreenViewModel mViewModel;
    private ChatListAdapter adapter;
    private android.view.ActionMode actionMode;
    private int itemPosition = -1;
    private Socket mSocket;
    Gson gson;
    private String mCurrentUserNum;
    MessageDataDb msgToBeDeleted;
    List<MessageDataDb> chatList = new ArrayList<>();
    private ImageButton btn_back;
    private TextView profile_text;
    private CircleImageView profile;
    private EditText edit_message;
    private FloatingActionButton btn_send_message;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private Friend mFriend;


    public static ChatScreenFragment newInstance() {
        return new ChatScreenFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // This callback will only be called when MyFragment is at least Started.
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.chat_screen_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            initViews(view);

            ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
            ChatScreenFragmentArgs fragmentArgs = ChatScreenFragmentArgs.fromBundle(getArguments());
            Timber.d("received arguments: %s", fragmentArgs.getFriend());
            mFriend = fragmentArgs.getFriend();
            Timber.d("received friend number : %s", mFriend.getUserContactNo());
            profile_text.setText(mFriend.getUserName());
            Picasso.get().load(mFriend.getProfilePic()).into(profile);
            profile.setOnClickListener(v ->
                    navController.navigate(ChatScreenFragmentDirections.actionChatScreenFragmentToProfileFragment(mFriend)));

            profile_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navController.navigate(ChatScreenFragmentDirections.actionChatScreenFragmentToProfileFragment(mFriend));
                }
            });


            UserProfile userProfile = AppUtils.getUserDetails();

            if (userProfile != null) {
                mCurrentUserNum = userProfile.getUserContactNo();
                adapter.setOwnNumber(userProfile.getUserContactNo());
                //region Let's connect to our Chat room! :D
                mSocket = BaseApplication.getInstance().getSocket();
                mSocket.on(Socket.EVENT_CONNECT, onConnect)
                        .on("update", onUpdateEvent)
                        .on("endChatMsg", onEndChatMsgEvent)
                        .on("readMsgStatus", readMsgStatusEvent)
                        .on("deleteMessage", deleteMessageEvent)
                        .on(Socket.EVENT_DISCONNECT, onDisConnect);
                mSocket.connect();
                //endregion


            } else {
                Timber.d("Try logging again");
            }

            adapter.setListener(new ChatListAdapter.ChatListAdapterListener() {
                @Override
                public void onItemClick(int position, View view) {
                    if (actionMode != null) {
                        actionMode.finish();
                    }
                    adapter.toggleBackgroundColor(position, view, 0);
                }

                @Override
                public void onItemLongClick(int position, View view) {
                    enableActionMode(position);
                    adapter.toggleBackgroundColor(position, view, 1);
                }
            });
        }
    }

    private void enableActionMode(int position) {
        if (actionMode == null){
            //region actionMode Callback
            actionMode = requireActivity().startActionMode(new android.view.ActionMode.Callback() {
                @Override
                public boolean onCreateActionMode(android.view.ActionMode actionMode, Menu menu) {
                    MenuInflater inflater = actionMode.getMenuInflater();
                    inflater.inflate(R.menu.context_menu, menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(android.view.ActionMode actionMode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(android.view.ActionMode actionMode, MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.star_message:
                            msgToBeDeleted = adapter.getMessage(position);
                            starMessage(msgToBeDeleted.getMsg());
                            return true;
                        case R.id.copy:
                            //logic for copy the message
                            msgToBeDeleted = adapter.getMessage(position);
                            setClipboard(requireActivity(), msgToBeDeleted.getMsg());
                            actionMode.finish();
                            return true;
                        case R.id.delete:
                            //delete the item
                            msgToBeDeleted = adapter.getMessage(position);
                            mViewModel.deleteMessage(
                                    msgToBeDeleted.getSender(),
                                    msgToBeDeleted.getReceiver(),
                                    msgToBeDeleted.getMsgId(),
                                    msgToBeDeleted.getRoom()).observe(getViewLifecycleOwner(),
                                    response -> {
                                        if (response != null) {
                                            if (response.getResp().equals("true")) {
                                                Timber.d("message delete api success");
//                                        adapter.removeItem(itemPosition);
                                                mViewModel.deleteRecentChatFromDB(msgToBeDeleted.getMsg());
                                                actionMode.finish();
                                            } else {
                                                Timber.d("message sent error");
                                                Toast.makeText(BaseApplication.getInstance(), "Network Error Try again", Toast.LENGTH_SHORT).show();
                                            }

                                        } else {
                                            Toast.makeText(BaseApplication.getInstance(), "Error Deleting", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            actionMode.finish();
                        case R.id.forward:
                            //forward the message copied

                        default:
                            return false;
                    }
                }

                @Override
                public void onDestroyActionMode(android.view.ActionMode mode) {
//                    adapter.selectedItems.clear();
//                    List<MessageDataDb> messageDataDbs = adapter.getList();
//                    for (MessageDataDb email : messageDataDbs) {
//                        if (email.isSelected())
//                            email.setSelected(false);
//                    }
//
//                    adapter.notifyDataSetChanged();
                    actionMode = null;
                }
            });

            }
        }


        private void starMessage(String message){

        class StarMessageAsyncTask extends AsyncTask<Void, Void, Void>{

            @Override
            protected Void doInBackground(Void... voids) {
                StaredMessage staredMessage = new StaredMessage();
                staredMessage.setMessage(message);
                DatabaseClient.getInstance(getActivity().getApplicationContext()).getAppDatabase()
                        .staredMessageDao()
                        .insertStartedMessage(staredMessage);
                return null;
            }


            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                try {
                    if (actionMode != null){
                        getActivity().runOnUiThread(() -> Toast.makeText(getActivity().getApplicationContext(), "Message Stared!", Toast.LENGTH_SHORT).show());
                        actionMode.finish();
                    }
                }catch (Exception e){
                    Timber.d("Exception: %s", e.toString());
                }
            }
        }
        StarMessageAsyncTask starMessageAsyncTask = new StarMessageAsyncTask();
        starMessageAsyncTask.execute();
        }

    private void initViews(View view) {
        gson = new Gson();
        navController = Navigation.findNavController(view);
        repository = new Repository();
        toolbar = view.findViewById(R.id.toolbar_chat_screen);
        profile_text = view.findViewById(R.id.tv_username);
        btn_back = view.findViewById(R.id.btn_back);
        profile = view.findViewById(R.id.image_profile);
        edit_message = view.findViewById(R.id.ed_message);
        btn_send_message = view.findViewById(R.id.btn_send);
        recyclerView = view.findViewById(R.id.rv_chat_screen);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new ChatListAdapter(getContext(), chatList);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ChatScreenViewModel.class);

        if (mFriend != null && mCurrentUserNum != null) {

            btn_send_message.setOnClickListener(v -> {
                String message = edit_message.getText().toString();
                if (TextUtils.isEmpty(message)) {
                    Timber.d("empty msg");
//                    Toast.makeText(getContext(), "P", Toast.LENGTH_SHORT).show();
                } else {
                    sendMessage(message);

                }
            });
            mViewModel.getErrorResponse().observe(getViewLifecycleOwner(),
                    s -> Timber.d("error occurred %s", s));

            mViewModel.getMessageHistory(mCurrentUserNum, mFriend.getUserContactNo())
                    .observe(getViewLifecycleOwner(), messageDataDbs -> {
                        if (messageDataDbs != null) {
                            Timber.d("list received size %s", messageDataDbs.size());
                            adapter.setList(messageDataDbs);
                            AppExecutors.getInstance().mainThread().execute(() -> {
                                adapter.notifyItemInserted(messageDataDbs.size());
                                recyclerView.scrollToPosition(messageDataDbs.size() - 1); //move focus on last message

                            });
                        }

                    });


            btn_back.setOnClickListener(v -> {
                if (adapter.getList()!=null&&adapter.getList().size()!=0){
                    mViewModel.AddLastMessage(adapter.getList().get(adapter.getList().size() - 1), mFriend);
                }
                navController.popBackStack();
            });

        }

    }

    OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
        @Override
        public void handleOnBackPressed() {
            // Handle the back button event
            if (adapter.getList()!=null&&adapter.getList().size()!=0){
                mViewModel.AddLastMessage(adapter.getList().get(adapter.getList().size() - 1), mFriend);
            }
            navController.popBackStack();

        }
    };


    //region Send // Add item to recyclerview and delete methods are here
    private void sendMessage(String message) {
        String uuid = String.valueOf(UUID.randomUUID());
        mViewModel.sendMessage(mCurrentUserNum, mFriend.getUserContactNo(), message, uuid)
                .observe(getViewLifecycleOwner(), validationResponse -> {
                    try {
                        if (validationResponse.getResp() != null) {
                            if (validationResponse.getResp().equals("true")) {
                                MessageDataDb messageResponse =
                                        new MessageDataDb(uuid,
                                                message,
                                                mCurrentUserNum,
                                                mFriend.getUserContactNo(),
                                                mCurrentUserNum,
                                                "true");
                                addItemToRecyclerView(messageResponse);
                                Timber.d("message sent successfully");
                                edit_message.setText("");
                            } else {
                                Timber.d("message sent error");
                                Toast.makeText(getActivity(), "Network Error Try again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (Exception e) {
                        Timber.d("Exception occurred %s", e.getLocalizedMessage());
                    }

                });
    }

    private void addItemToRecyclerView(MessageDataDb messageResponse) {
        mViewModel.addData(messageResponse);
    }

    private void deleteItem() {
        AppExecutors.getInstance().mainThread().execute(() -> {
            adapter.removeItem(itemPosition);
        });

    }
    //endregion

    //region event listeners

    Emitter.Listener onUpdateEvent = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {

            Timber.d("onUpdateEvent -> %s", args);
        }
    };

    Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Timber.d("connection established socket id -> %s", mSocket.id());
            SocketModel socketModel = new SocketModel(mCurrentUserNum, mSocket.id());
            String json = gson.toJson(socketModel);
            mSocket.emit("globalListener", json);
        }
    };


    Emitter.Listener onDisConnect = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Timber.d("disconnect event -> %s", args);
        }
    };


    Emitter.Listener onEndChatMsgEvent = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            if (args != null) {
                String response = args[0].toString();
                Timber.d("onEndChatMsgEvent received  %s", response);
                SocketMessageResponse messageResponse = gson.fromJson(response, SocketMessageResponse.class);
                if (!messageResponse.getSender().equals(mCurrentUserNum)) {
                    if (messageResponse.getSender().equals(mFriend.getUserContactNo())) {
                        Timber.d("sender the friend you are inside so add the message inside recyclerview");
                        MessageDataDb msg =
                                new MessageDataDb(messageResponse.getMsgId(),
                                        messageResponse.getMsg(),
                                        messageResponse.getRoom(),
                                        messageResponse.getReceiver(),
                                        messageResponse.getSender(),
                                        messageResponse.getResp());
                        addItemToRecyclerView(msg);
                    }
                }
            }
        }
    };


    Emitter.Listener readMsgStatusEvent = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            if (args != null) {
                String response = args[0].toString();
                Timber.d("readMsgStatusEvent received  %s", response);
                MessageDataDb messageResponse = gson.fromJson(response, MessageDataDb.class);
//                if (!messageResponse.getSender().equals(mCurrentUserNum)) {
//                    if (messageResponse.getSender().equals(mFriend.getUserContactNo())) {
//                        addItemToRecyclerView(messageResponse);
//                    }
//                }


            }
        }
    };


    Emitter.Listener deleteMessageEvent = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            if (args != null) {
                String response = args[0].toString();
                Timber.d("deleteMessageEvent received  %s", response);
                try {
                    DeleteEventResponse eventResponse = gson.fromJson(response, DeleteEventResponse.class);
                    Timber.d("deleteMessageEvent json  %s", eventResponse);
                    if (eventResponse.getSender().equals(mCurrentUserNum)) {
                        if (msgToBeDeleted.getMsgId().equals(eventResponse.getMsgid())) {
                            Timber.d("message needs to be deleted as both id matches of mine");
                            deleteItem();
                            mViewModel.deleteFromDb(eventResponse.getMsgid());
                        }
                    } else {
                        if (eventResponse.getSender().equals(mFriend.getUserContactNo())) {
                            //delete logic for sender msg needs to be tweaked
                            for (int i = 0; i < adapter.getList().size(); i++) {
                                if (eventResponse.getMsgid().equals(adapter.getList().get(i).getMsgId())) {
                                    Timber.d("message needs to be deleted as both id matches of sender");
                                    itemPosition = i;
                                    deleteItem();
                                    mViewModel.deleteFromDb(eventResponse.getMsgid());
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    Timber.d("exception occurred ..!!!  %s", e.getLocalizedMessage());
                }
            }
        }
    };


    //endregion




    private void setClipboard(Context context, String text) {
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
        }

    }


    //endregion
}