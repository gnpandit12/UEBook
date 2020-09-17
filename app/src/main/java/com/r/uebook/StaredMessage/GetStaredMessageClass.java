package com.r.uebook.StaredMessage;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.r.uebook.data.database.DatabaseClient;
import com.r.uebook.data.database.entity.StaredMessage;

import java.util.List;

import timber.log.Timber;

public class GetStaredMessageClass extends AsyncTask<Void, Void, List<StaredMessage>> {

    private Context mContext;
    public static final String TAG = "get_stared_msg_class";

    public GetStaredMessageClass(Context context){
        this.mContext = context;
        Log.d(TAG, "GetStaredMessageClass: ");
    }


    @Override
    protected List<StaredMessage> doInBackground(Void... voids) {
        Log.d(TAG, "doInBackground: ");
        return DatabaseClient
                .getInstance(mContext)
                .getAppDatabase()
                .staredMessageDao()
                .getAllStaredMessages();
    }

    @Override
    protected void onPostExecute(List<StaredMessage> staredMessageList) {
        super.onPostExecute(staredMessageList);
        Log.d(TAG, "onPostExecute: ");
        StaredMessage staredMessage = staredMessageList.get(0);
        String messageID = staredMessage.getMessageID();
        String message = staredMessage.getMessage();
        Log.d(TAG, "MessageID: "+messageID);
        Log.d(TAG, "Message: "+message);

    }
}
