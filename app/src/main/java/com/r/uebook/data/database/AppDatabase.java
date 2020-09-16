package com.r.uebook.data.database;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.r.uebook.data.database.converter.FriendConverter;
import com.r.uebook.data.database.converter.TimeConverters;
import com.r.uebook.data.database.dao.MessageDao;
import com.r.uebook.data.database.dao.StaredMessageDao;
import com.r.uebook.data.database.entity.MessageDataDb;
import com.r.uebook.data.database.entity.RecentChatDb;
import com.r.uebook.data.database.entity.StaredMessage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Database(entities = {MessageDataDb.class, RecentChatDb.class, StaredMessage.class}, version = 5, exportSchema = false)
@TypeConverters({TimeConverters.class, FriendConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract MessageDao messageDao();
    public abstract StaredMessageDao staredMessageDao();

    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "UeBook")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
