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
import com.r.uebook.data.remote.model.Friend;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import timber.log.Timber;

public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.FriendViewHolder> implements Filterable {

    private List<Friend> friendList = new ArrayList<>();
    private List<Friend> filteredNameList = new ArrayList<>();
    private FriendsListAdapter.OnItemClickListener listener;


    public void setFriends(List<Friend> list) {
        this.friendList = list;
        filteredNameList.addAll(friendList);
        notifyDataSetChanged();
    }

    public Friend getItemAt(int position) {
        return friendList.get(position);
    }


    @NotNull
    @Override
    public FriendViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.contact_item_layout, viewGroup, false);
        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull FriendViewHolder friendViewHolder, int i) {
        if (getItemAt(i) != null) {
            Friend friend = getItemAt(i);
            friendViewHolder.bindsTo(friend);
        }
    }


    @Override
    public int getItemCount() {
        return friendList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Friend> filteredList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    Timber.d("char sequence is null");
                    filteredList.addAll(filteredNameList);
                } else {
                    Timber.d("received char sequence %s ", constraint);
                    for (Friend friend : filteredNameList) {
                        if (friend.getUserName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                            filteredList.add(friend);
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                friendList.clear();
                friendList.addAll((Collection<? extends Friend>) results.values);
                notifyDataSetChanged();

            }
        };
    }


    public class FriendViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout relativeLayout;
        TextView nameTVcontact;
        TextView bioTVcontact;
        ImageView prof_pic_contact;


        public FriendViewHolder(View itemView) {
            super(itemView);
            relativeLayout = itemView.findViewById(R.id.rv_rel_layout_contact);
            nameTVcontact = itemView.findViewById(R.id.name_tv_contact);
            bioTVcontact = itemView.findViewById(R.id.bio_tv);
            prof_pic_contact = itemView.findViewById(R.id.profile_pic_contact);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(position);
                }
            });
        }


        public void bindsTo(Friend friend) {
            nameTVcontact.setText(friend.getUserName());
            Picasso.get()
                    .load(friend.getProfilePic())
                    .placeholder(R.drawable.land)
                    .into(prof_pic_contact);
            bioTVcontact.setText("Hey There! Have a good day!");

        }
    }


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(FriendsListAdapter.OnItemClickListener listener) {
        this.listener = listener;

    }
}
