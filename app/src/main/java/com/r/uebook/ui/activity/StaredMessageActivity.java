package com.r.uebook.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;

import com.r.uebook.R;
import com.r.uebook.data.database.DatabaseClient;
import com.r.uebook.data.database.entity.StaredMessage;
import com.r.uebook.ui.adapters.StarMessageListAdapter;

import java.util.List;

public class StaredMessageActivity extends AppCompatActivity {

    private RecyclerView starMessageRecyclerView;
    private StarMessageListAdapter starMessageListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stared_message);

        starMessageRecyclerView = findViewById(R.id.star_message_recycler_view);
        starMessageRecyclerView.setHasFixedSize(true);


        getStaredMessages();
    }

    private void getStaredMessages() {
        class GetStaredMessages extends AsyncTask<Void, Void, List<StaredMessage>>{


            @Override
            protected List<StaredMessage> doInBackground(Void... voids) {
                return DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .staredMessageDao()
                        .getAllStaredMessages();
            }

            @Override
            protected void onPostExecute(List<StaredMessage> staredMessageList) {
                super.onPostExecute(staredMessageList);
                starMessageListAdapter = new StarMessageListAdapter(getApplicationContext(), staredMessageList);
                starMessageRecyclerView.setAdapter(starMessageListAdapter);
                starMessageListAdapter.notifyDataSetChanged();
            }
        }
        GetStaredMessages getStaredMessages = new GetStaredMessages();
        getStaredMessages.execute();
    }
}