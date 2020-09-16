package com.r.uebook.ui.fragments.friends;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.r.uebook.data.Repository.Repository;
import com.r.uebook.data.remote.model.LoginResponse;

public class FriendsViewModel extends ViewModel {

    Repository repository;
    private MutableLiveData<LoginResponse> friendsList;

    private MutableLiveData<String> errorResponse;

    public FriendsViewModel() {
        repository = new Repository();
    }

    public MutableLiveData<LoginResponse> getFriendsList(String username, String password) {
        return repository.getRefreshResponse(username, password);
    }

    public MutableLiveData<String> getErrorResponse() {
        return repository.getErrorResponse();
    }

    public void clearDatabase(){
        repository.clearDatabase();
    }

}