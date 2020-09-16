package com.r.uebook.ui.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.r.uebook.R;
import com.r.uebook.data.database.entity.RecentChatDb;
import com.r.uebook.utils.AppUtils;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import timber.log.Timber;

public class HomeChatListAdapter extends RecyclerView.Adapter<HomeChatListAdapter.RecentChatHolder> implements Filterable {

    private List<RecentChatDb> chatList = new ArrayList<>();
    private List<RecentChatDb> filteredChatList = new ArrayList<>();
//    private HomeChatListAdapter.OnItemClickListener listener;
    private HomeChatAdapterListener homeChatAdapterListener;


    public void setChats(List<RecentChatDb> list) {
        this.chatList = list;
        filteredChatList.addAll(chatList);
        notifyDataSetChanged();
    }

    public RecentChatDb getItemAt(int position) {
        return chatList.get(position);
    }

    @NotNull
    @Override
    public HomeChatListAdapter.RecentChatHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chat_item_layout, viewGroup, false);
        return new RecentChatHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull HomeChatListAdapter.RecentChatHolder recentChatHolder, int position) {
        if (getItemAt(position) != null) {
            recentChatHolder.no_of_msgs.setVisibility(View.GONE);
            recentChatHolder.bindsTo(recentChatHolder, position);
        }
    }


    @Override
    public int getItemCount() {
        return chatList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<RecentChatDb> filteredList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    Timber.d("char sequence is null");
                    filteredList.addAll(filteredChatList);
                } else {

                    Timber.d("received char sequence %s ", constraint);
                    for (RecentChatDb chat : filteredChatList) {
                        if (chat.getFriend().getUserName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                            filteredList.add(chat);
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                chatList.clear();
                chatList.addAll((Collection<? extends RecentChatDb>) results.values);
                notifyDataSetChanged();

            }
        };
    }

    public void setHomeChatAdapterListener(HomeChatAdapterListener homeChatAdapterListener) {
        this.homeChatAdapterListener = homeChatAdapterListener;
    }


    public class RecentChatHolder extends RecyclerView.ViewHolder {

        RelativeLayout relativeLayout;
        TextView nameTV;
        TextView dateTV;
        TextView latestmsgTV;
        ImageView prof_pic;
        TextView no_of_msgs;

        public RecentChatHolder(View itemView) {
            super(itemView);
            relativeLayout = itemView.findViewById(R.id.rv_rel_layout);
            nameTV = itemView.findViewById(R.id.name_tv);
            dateTV = itemView.findViewById(R.id.date);
            latestmsgTV = itemView.findViewById(R.id.latest_msg_tv);
            prof_pic = itemView.findViewById(R.id.profile_pic);
            no_of_msgs = itemView.findViewById(R.id.no_of_msgs);

//            itemView.setOnClickListener(v -> {
//                int position = getAdapterPosition();
//                if (listener != null && position != RecyclerView.NO_POSITION) {
//                    listener.onItemClick(position);
//                }
//            });
        }

        public void bindsTo( HomeChatListAdapter.RecentChatHolder recentChatHolder, int position) {
            RecentChatDb chat = chatList.get(position);
            nameTV.setText(chat.getFriend().getUserName());
            if (AppUtils.getCurrentTimeStamp(chat.getTimestamp()) != null) {
                String date = AppUtils.getCurrentTimeStamp(chat.getTimestamp());
                dateTV.setText(date);
            }

            latestmsgTV.setText(chat.getLatest_msg());
            Picasso.get()
                    .load(chat.getFriend().getProfilePic())
                    .placeholder(R.drawable.land)
                    .into(prof_pic);

            recentChatHolder.itemView.setOnClickListener(view -> {
                Timber.d("SingleClick!");
                homeChatAdapterListener.onSingleClick(position, chat);
            });

            recentChatHolder.itemView.setOnLongClickListener(view -> {
                Timber.d("LongClick!");
                homeChatAdapterListener.onLongClick(position, chat);
                return true;
            });

        }
    }


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

//    public void setOnItemClickListener(HomeChatListAdapter.OnItemClickListener listener) {
//        this.listener = listener;
//    }

    public interface HomeChatAdapterListener {
        void onSingleClick(int position, RecentChatDb chatDb);
        void onLongClick(int position, RecentChatDb chatDb);
    }

}
