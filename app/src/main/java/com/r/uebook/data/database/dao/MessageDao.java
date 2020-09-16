package com.r.uebook.data.database.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.r.uebook.data.database.entity.MessageDataDb;
import com.r.uebook.data.database.entity.RecentChatDb;
import com.r.uebook.data.remote.model.Friend;

import java.util.List;

@Dao
public interface MessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMessage(MessageDataDb messageDataDb);

    @Query("SELECT * FROM MessageData WHERE (sender = :number AND receiver= :number2) " +
            "OR (sender = :number2 AND receiver= :number)")
    LiveData<List<MessageDataDb>> getCourseData(String number, String number2);


    //Delete one item by id
    @Query("DELETE FROM MessageData WHERE msgId = :itemId")
    void deleteByItemId(String itemId);


    //clear all data
    @Query("DELETE FROM messagedata")
    void clearData();

    @Query("DELETE FROM recentchats")
    void clearDataRecent();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRecentChat(RecentChatDb recentChatDb);


    @Query("SELECT * FROM recentchats Order by timeStamp desc")
    LiveData<List<RecentChatDb>> getLiveRecentChats();

    //Delete one item by last msg
    @Query("DELETE FROM RecentChats WHERE lastMsg = :latestMsg")
    void deleteByLatestMSG(String latestMsg);

    @Query("DELETE FROM RecentChats WHERE FriendDetail = :friend")
    void deleteRecentChat(Friend friend);

    @Delete
    void deleteRecentChatViewHolder(List<MessageDataDb> messageDataDbList);

}
