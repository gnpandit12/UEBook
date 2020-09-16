package com.r.uebook.data.database.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.r.uebook.data.database.entity.StaredMessage;

import java.util.List;

@Dao
public interface StaredMessageDao {

    @Insert
    void insertStartedMessage(StaredMessage staredMessage);

    @Query("SELECT * FROM startedMessages")
    List<StaredMessage> getAllStaredMessages();

}
