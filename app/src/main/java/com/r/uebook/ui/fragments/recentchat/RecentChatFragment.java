package com.r.uebook.ui.fragments.recentchat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.r.uebook.BaseApplication;
import com.r.uebook.R;
import com.r.uebook.data.database.entity.MessageDataDb;
import com.r.uebook.data.database.entity.RecentChatDb;
import com.r.uebook.data.database.entity.StaredMessage;
import com.r.uebook.data.remote.model.Friend;
import com.r.uebook.data.remote.model.UserProfile;
import com.r.uebook.ui.activity.StaredMessageActivity;
import com.r.uebook.ui.adapters.HomeChatListAdapter;
import com.r.uebook.ui.fragments.friends.FriendsViewModel;
import com.r.uebook.ui.login_register.LoginActivity;
import com.r.uebook.utils.AppUtils;
import com.r.uebook.utils.ApplicationConstants;
import com.r.uebook.utils.Prefs;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import timber.log.Timber;

public class RecentChatFragment extends Fragment {

    private HomeChatListAdapter adapter;
    private RecyclerView recyclerView;
    private Context context;
    private TextView top_text;
    private List<RecentChatDb> mChatList = new ArrayList<>();
    private List<Friend> friendList = new ArrayList<>();
    private UserProfile userProfile;
    private NavController navController;
    private ConstraintLayout no_data_available;
    private RecentChatViewModel mViewModel;
    private android.view.ActionMode actionMode;
    private List<MessageDataDb> messageDataDbList = new ArrayList<>();

    public static RecentChatFragment newInstance() {
        return new RecentChatFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.recent_chat_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userProfile = AppUtils.getUserDetails();
        if (userProfile != null) {
            Timber.d("user_profile = %s", Objects.requireNonNull(userProfile).toString());
            initViews(view);


        } else {
            Timber.d("user profile is empty");
            Toast.makeText(getActivity(), "Server Error Kindly Login again", Toast.LENGTH_SHORT).show();
        }


    }

    private void initViews(View view) {
        navController = Navigation.findNavController(view);
        recyclerView = view.findViewById(R.id.rv_recent_chat);
        recyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new HomeChatListAdapter();
        recyclerView.setAdapter(adapter);
        top_text = view.findViewById(R.id.top_tv);
        no_data_available = view.findViewById(R.id.no_data_tc);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(RecentChatViewModel.class);
        String username = Prefs.getPreferences(ApplicationConstants.USERNAME);
        String password = Prefs.getPreferences(ApplicationConstants.PASSWORD);
        if (username != null && password != null) {
            mViewModel.getLiveRecentChats().observe(getViewLifecycleOwner(), recentChatDbs -> {
                if (recentChatDbs != null) {
                    mChatList = recentChatDbs;
                    no_data_available.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    adapter.setChats(recentChatDbs);
                } else {
                    no_data_available.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }

            });

//            adapter.setOnItemClickListener(position -> {
//
//            });

            adapter.setHomeChatAdapterListener(new HomeChatListAdapter.HomeChatAdapterListener() {
                @Override
                public void onSingleClick(int position, RecentChatDb chatDb) {
                    RecentChatDb item = adapter.getItemAt(position);
                    navController.navigate(RecentChatFragmentDirections.actionChatFragmentToChatScreenFragment(item.getFriend()));
                }

                @Override
                public void onLongClick(int position, RecentChatDb chatDb) {
                    enableActionMode(position, chatDb);
                }
            });
        }
    }

    private void enableActionMode(int position, RecentChatDb recentChatDb) {
        if (actionMode == null){
            //region actionMode Callback
            actionMode = requireActivity().startActionMode(new android.view.ActionMode.Callback() {
                @Override
                public boolean onCreateActionMode(android.view.ActionMode actionMode, Menu menu) {
                    MenuInflater inflater = actionMode.getMenuInflater();
                    inflater.inflate(R.menu.recent_chat_menu, menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(android.view.ActionMode actionMode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(android.view.ActionMode actionMode, MenuItem menuItem) {
                    if (menuItem.getItemId() == R.id.delete) {

                        mViewModel.deleteRecentChatViewHolder(recentChatDb.getFriend());
                        mChatList.remove(position);
                        adapter.notifyDataSetChanged();
                        actionMode.finish();
                    }
                    return false;
                }

                @Override
                public void onDestroyActionMode(android.view.ActionMode mode) {
                    actionMode = null;
                }
            });
        }
    }


    //region for Search query and overflow menu

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                searchView.clearFocus();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                Timber.d("onMenuItemClick: search clicked");
                return true;
            case R.id.stared_msg:
                startActivity(new Intent(getContext(), StaredMessageActivity.class));
                return true;
            case R.id.edit_profile:
                Timber.d("send user to profile detail");
                navController.navigate(RecentChatFragmentDirections.actionChatFragmentToCurrentUserProfileFragment(userProfile));
                return true;

            case R.id.log_out:
                Timber.d("log out clicked");
                mViewModel.clearDatabase();
                AppUtils.loggedOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                requireActivity().finish();
            default:
                return super.onOptionsItemSelected(item);

        }

    }

    //endregion
}