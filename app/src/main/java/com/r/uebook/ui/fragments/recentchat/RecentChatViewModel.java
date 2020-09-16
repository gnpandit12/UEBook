package com.r.uebook.ui.fragments.recentchat;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.r.uebook.data.Repository.Repository;
import com.r.uebook.data.database.entity.RecentChatDb;
import com.r.uebook.data.remote.model.Friend;
import com.r.uebook.data.remote.model.LoginResponse;

import java.util.List;

public class RecentChatViewModel extends ViewModel {
    Repository repository;

    public RecentChatViewModel() {
        repository = new Repository();
    }

    public MutableLiveData<LoginResponse> getFriendsList(String username, String password) {
        return repository.getRefreshResponse(username, password);
    }

    public LiveData<List<RecentChatDb>> getLiveRecentChats() {
        return repository.getRecentChat();
    }

    public MutableLiveData<String> getErrorResponse() {
        return repository.getErrorResponse();
    }


    public void clearDatabase(){
        repository.clearDatabase();
    }
    public void deleteRecentChatViewHolder(Friend friend){
        repository.deleteRecentChatViewHolder(friend);
    }
}