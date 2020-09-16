package com.r.uebook.data.Repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.r.uebook.BaseApplication;
import com.r.uebook.data.database.AppDatabase;
import com.r.uebook.data.database.dao.MessageDao;
import com.r.uebook.data.database.entity.MessageDataDb;
import com.r.uebook.data.database.entity.RecentChatDb;
import com.r.uebook.data.database.entity.StaredMessage;
import com.r.uebook.data.remote.client.RetrofitClient;
import com.r.uebook.data.remote.model.Friend;
import com.r.uebook.data.remote.model.LoginResponse;
import com.r.uebook.data.remote.model.ValidationResponse;
import com.r.uebook.data.remote.service.ApiService;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by vivek panchal on 07-09-2020.
 * https://vivekpanchal.dev
 **/
public class Repository {

    private ApiService apiService;
    private RetrofitClient retrofitClient;
    private MessageDao dao;
    final MutableLiveData<String> errorResponse = new MutableLiveData<>();
    public MutableLiveData<List<MessageDataDb>> listMutableLiveData = new MutableLiveData<>();

    public Repository() {
        apiService = RetrofitClient.getClient().create(ApiService.class);
        dao = AppDatabase.getDatabase(BaseApplication.getInstance()).messageDao();
    }


    //region db

    public void addMessageData(MessageDataDb response) {
        AppDatabase.databaseWriteExecutor.execute(() -> dao.insertMessage(new MessageDataDb(response.getMsgId(),
                response.getMsg(), response.getRoom(), response.getReceiver(), response.getSender(), response.getResp())));

    }


    public void deleteMessage(String messageID) {
        Timber.d("messageID: %s", messageID);
        AppDatabase.databaseWriteExecutor.execute(() -> dao.deleteByItemId(messageID));

    }

    public void deleteRecentChat(String latestMsg) {
        Timber.d("messageID: %s", latestMsg);
        AppDatabase.databaseWriteExecutor.execute(() -> dao.deleteByLatestMSG(latestMsg));
    }

    public void deleteRecentChatViewHolder(Friend friend) {
        AppDatabase.databaseWriteExecutor.execute(() -> dao.deleteRecentChat(friend));
    }


    public LiveData<List<MessageDataDb>> getMessageHistory(String sender, String recvier) {
        return dao.getCourseData(sender, recvier);
    }

    public LiveData<List<RecentChatDb>> getRecentChat() {
        return dao.getLiveRecentChats();
    }


    public void clearDatabase() {
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.clearData();
                dao.clearDataRecent();
            }
        });
    }


    //endregion

    public MutableLiveData<LoginResponse> getRefreshResponse(String user_id, String password) {
        final MutableLiveData<LoginResponse> mutableLiveData = new MutableLiveData<>();

        apiService.getRefreshedData(user_id, password).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NotNull Call<LoginResponse> call, @NotNull Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    if (response.body().getResp() != null && !response.body().getResp().isEmpty()) {

                        switch (response.body().getResp()) {
                            case "true":
                                LoginResponse loginResponse = response.body();
                                Timber.d("LoginResponse: %s", loginResponse.toString());
                                mutableLiveData.setValue(loginResponse);

                                break;
                            case "false":
                                errorResponse.setValue("Try again ");
                                break;
                            case "error":
                                errorResponse.setValue("Network error ");

                                break;
                        }

                    }
                }

            }

            @Override
            public void onFailure(@NotNull Call<LoginResponse> call, @NotNull Throwable t) {
                Timber.d("error occurred %s", t.getLocalizedMessage());
                mutableLiveData.setValue(null);
                errorResponse.setValue("Network error");
            }
        });

        return mutableLiveData;
    }


    public MutableLiveData<ValidationResponse> sendMessage(String senderMob, String receiverMob, String message, String uid) {
        final MutableLiveData<ValidationResponse> mutableLiveData = new MutableLiveData<>();

        apiService.sendMessage(senderMob, receiverMob, receiverMob, message, uid).enqueue(new Callback<ValidationResponse>() {
            @Override
            public void onResponse(@NotNull Call<ValidationResponse> call, @NotNull Response<ValidationResponse> response) {

                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getResp() != null && !response.body().getResp().isEmpty()) {

                        switch (response.body().getResp()) {
                            case "true":
                                mutableLiveData.setValue(response.body());
                                break;
                            case "false":
                                errorResponse.setValue("Try again ");
                                break;
                            case "error":
                                errorResponse.setValue("Network error ");
                                break;
                        }

                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<ValidationResponse> call, @NotNull Throwable t) {
                Timber.d("error occurred %s", t.getLocalizedMessage());
                mutableLiveData.setValue(null);
                errorResponse.setValue("Network error");
            }
        });

        return mutableLiveData;
    }

    public MutableLiveData<ValidationResponse> deleteMessage(String senderMob, String receiverMob, String msg_id, String room) {
        final MutableLiveData<ValidationResponse> mutableLiveData = new MutableLiveData<>();
        apiService.
                deleteMessage(senderMob, receiverMob, msg_id, room)
                .enqueue(new Callback<ValidationResponse>() {
                    @Override
                    public void onResponse(@NotNull Call<ValidationResponse> call, @NotNull Response<ValidationResponse> response) {

                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getResp() != null && !response.body().getResp().isEmpty()) {

                                switch (response.body().getResp()) {
                                    case "true":
                                        mutableLiveData.setValue(response.body());
                                        break;
                                    case "false":
                                        errorResponse.setValue("Try again ");
                                        break;
                                    case "error":
                                        errorResponse.setValue("Network error ");
                                        break;
                                }

                            }
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ValidationResponse> call, @NotNull Throwable t) {
                        Timber.d("error occurred %s", t.getLocalizedMessage());
                        mutableLiveData.setValue(null);
                        errorResponse.setValue("Network error");
                    }
                });

        return mutableLiveData;
    }


    public MutableLiveData<String> getErrorResponse() {
        return errorResponse;
    }


    public void addLastMessageReceived(MessageDataDb messageDataDb, Friend friend) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            RecentChatDb recentChatDb = new RecentChatDb(friend.getUserContactNo(), friend, messageDataDb.getMsg(),
                    messageDataDb.getTimestamp(), 0);
            dao.insertRecentChat(recentChatDb);
        });
    }




}
