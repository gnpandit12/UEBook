package com.r.uebook.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.SparseBooleanArray;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.r.uebook.R;
import com.r.uebook.data.database.entity.MessageDataDb;
import com.r.uebook.data.database.entity.RecentChatDb;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.schedulers.Timed;
import timber.log.Timber;

/**
 * Created by vivek panchal on 09-09-2020.
 * https://vivekpanchal.dev
 **/
public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder> {

    public SparseBooleanArray selectedItems = new SparseBooleanArray();
    private List<MessageDataDb> list;
    private Context context;
    private String mOwnNumber;
    private ChatListAdapterListener listener;
//    private ChatListAdapter.OnItemClickListener listener;
//    private ChatListAdapter.OnItemSingleClickListener singleClickListener;
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    public ChatListAdapter(Context context, List<MessageDataDb> messageList) {
        this.context = context;
        this.list = messageList;
    }

    public void setOwnNumber(String ownNumber) {
        mOwnNumber = ownNumber;
    }

    public List<MessageDataDb> getList() {
        return list;
    }

    public void setList(List<MessageDataDb> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public MessageDataDb getMessage(int position) {
        return list.get(position);
    }

    public void removeItem(int position) {
        try {

            list.remove(position);

            notifyItemRemoved(position);

        } catch (Exception e) {
            Timber.d("unable to delete the item");
        }

    }

    @NonNull
    @Override
    public ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_LEFT) {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);
            return new ChatListViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);
            return new ChatListViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListViewHolder holder, int position) {
        if (list.get(position) != null) {
            holder.bindTo(list.get(position), holder);
        }

        holder.itemView.setOnClickListener(view -> {
            if (listener != null)
                listener.onItemClick(position, view);
        });

        holder.itemView.setOnLongClickListener(view -> {
            if (listener != null)
                listener.onItemLongClick(position, view);
            return true;
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).getSender().equals(mOwnNumber)) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }

    }

    public void setListener(ChatListAdapterListener listener) {
        this.listener = listener;
    }

    public void toggleBackgroundColor(int position, View view, int clickType) {

        if (clickType == 0){
            list.get(position).setSelected(false);
        }else if (clickType == 1){
            list.get(position).setSelected(true);
        }

        if (list.get(position).isSelected()){
            view.setBackgroundColor(Color.parseColor("#76ff03"));
        }else {
            view.setBackgroundColor(Color.parseColor("#EDEDED"));
        }
    }

    public static class ChatListViewHolder extends RecyclerView.ViewHolder {
        private TextView textMessage;

        public ChatListViewHolder(@NonNull View itemView) {
            super(itemView);
            textMessage = itemView.findViewById(R.id.tv_text_message);

        }


        public void bindTo(MessageDataDb messageDataDb, ChatListViewHolder holder) {
            String message = messageDataDb.getMsg();
            textMessage.setText(message);

            messageDataDb.setSelected(!messageDataDb.isSelected());
            if (messageDataDb.isSelected()) {
                holder.itemView.setBackgroundColor(Color.parseColor("#EDEDED"));
            } else {
                holder.itemView.setBackgroundColor(Color.parseColor("#76ff03"));
                holder.itemView.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
            }
        }
    }

    public interface ChatListAdapterListener {
        void onItemClick(int position, View view);
        void onItemLongClick(int position, View view);
    }

}
