package com.r.uebook.data.remote.service;


import com.r.uebook.data.remote.model.LoginResponse;
import com.r.uebook.data.remote.model.Result;
import com.r.uebook.data.remote.model.ValidationResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {


    @GET("/loginUser")
    Call<LoginResponse> isValidUser(
            @Query("login_id") String login_id,
            @Query("password") String password
    );


    //The register call
    @GET("/registerUser")
    Call<LoginResponse> createUser(
            @Query("mail_id") String email,
            @Query("password") String password,
            @Query("contact_no") String contact,
            @Query("user_name") String userName,
            @Query("f_name") String firstName,
            @Query("l_name") String lastName);


    @GET("/refreshSystem")
    Call<LoginResponse> getRefreshedData(
            @Query("login_id") String login_id,
            @Query("password") String password
    );


    @GET("/chat")
    Call<ValidationResponse> sendMessage(
            @Query("sender") String senderMobile,
            @Query("rcvr") String receiverMobile,
            @Query("room") String room_sender_number,
            @Query("msg") String message,
            @Query("uid")String uid
    );



    @GET("/delmsg")
    Call<ValidationResponse> deleteMessage(
            @Query("sender") String senderMobile,
            @Query("rcvr") String receiverMobile,
            @Query("msgid") String msg_id,
            @Query("room") String room
    );


    //region validation responses -->>

    @GET("/userNameCheck")
    Call<ValidationResponse> checkUserName(
            @Query("userName") String userName);

    @GET("/userMailCheck")
    Call<ValidationResponse> checkEmail(
            @Query("userMail") String user_mail);

    @GET("/userMobilenoCheck")
    Call<ValidationResponse> checkMobileNumber(
            @Query("userContact_no") String mobile);

    //endregion


}
