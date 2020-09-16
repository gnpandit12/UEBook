package com.r.uebook.ui.fragments.chatscreen;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.r.uebook.data.Repository.Repository;
import com.r.uebook.data.database.entity.MessageDataDb;
import com.r.uebook.data.remote.model.Friend;
import com.r.uebook.data.remote.model.ValidationResponse;

import java.util.List;

public class ChatScreenViewModel extends ViewModel {

    Repository mRepository;

    public ChatScreenViewModel() {
        mRepository = new Repository();
    }


    public MutableLiveData<ValidationResponse> sendMessage(String senderMob, String receiverMob, String message, String uid) {
        return mRepository.sendMessage(senderMob, receiverMob, message, uid);
    }

    public void addData(MessageDataDb response) {
        mRepository.addMessageData(response);
    }

    public void AddLastMessage(MessageDataDb messageDataDb, Friend friend) {
        mRepository.addLastMessageReceived(messageDataDb, friend);
    }

    public LiveData<List<com.r.uebook.data.database.entity.MessageDataDb>> getMessageHistory(String sender, String recvier) {
        return mRepository.getMessageHistory(sender, recvier);
    }


    public void deleteFromDb(String messageID) {
        mRepository.deleteMessage(messageID);
    }

    public void deleteRecentChatFromDB(String latestMsg){
        mRepository.deleteRecentChat(latestMsg);
    }




    public MutableLiveData<String> getErrorResponse() {
        return mRepository.getErrorResponse();
    }

    public MutableLiveData<ValidationResponse> deleteMessage(String sender, String receiver, String msgId, String room) {
        return mRepository.deleteMessage(sender, receiver, msgId, room);

    }
}