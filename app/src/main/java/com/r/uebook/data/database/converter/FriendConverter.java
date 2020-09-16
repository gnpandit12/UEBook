package com.r.uebook.data.database.converter;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.r.uebook.data.remote.model.Friend;
import com.r.uebook.data.remote.model.UserProfile;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by vivek panchal on 11-09-2020.
 * https://vivekpanchal.dev
 **/
public class FriendConverter {

    @TypeConverter
    public Friend StringToFriend(String jsonString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, Friend.class);
    }

    @TypeConverter
    public String FriendToString(Friend genreList) {
        Gson gson = new Gson();
        return gson.toJson(genreList);
    }
}
