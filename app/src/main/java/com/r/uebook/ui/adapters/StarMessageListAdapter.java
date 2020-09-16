package com.r.uebook.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.r.uebook.R;
import com.r.uebook.data.database.entity.StaredMessage;
import com.r.uebook.ui.activity.StaredMessageActivity;

import java.util.List;

public class StarMessageListAdapter extends RecyclerView.Adapter<StarMessageListAdapter.StarMessageViewHolder> {

    private Context mContext;
    private List<StaredMessage> mStaredMessageList;

    public StarMessageListAdapter(Context context, List<StaredMessage> staredMessageList){
        this.mContext = context;
        this.mStaredMessageList = staredMessageList;
    }

    @NonNull
    @Override
    public StarMessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_item_right, parent, false);
        return new StarMessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StarMessageViewHolder holder, int position) {
        StaredMessage staredMessage = mStaredMessageList.get(position);
        holder.messageTextView.setText(staredMessage.getMessage());
    }

    @Override
    public int getItemCount() {
        return mStaredMessageList.size();
    }

    public static class StarMessageViewHolder extends RecyclerView.ViewHolder {

        TextView messageTextView;

        public StarMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.tv_text_message);
        }

    }


}
