package com.r.uebook;

import android.app.Application;

//import com.amitshekhar.BuildConfig;
import com.r.uebook.utils.eventservice.EventListener;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import timber.log.Timber;

/**
 * Created by vivek panchal on 07-09-2020.
 * https://vivekpanchal.dev
 **/
public class BaseApplication extends Application {
    private static BaseApplication instance;
    private EventListener eventListener;

    private Socket mSocket;
    private static final String URL = "http://15.207.210.147";

    public static BaseApplication getInstance() {
        return instance;
    }

    public BaseApplication() {
        instance = this;
    }

    public Socket getSocket() {
        return mSocket;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            mSocket = IO.socket(URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    //region event listeners

    Emitter.Listener onUpdateEvent = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {

            Timber.d("onUpdateEvent -> %s", args);

        }
    };

    Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Timber.d("connection established socket id -> %s", mSocket.id());
//            SocketModel socketModel = new SocketModel(mCurrentUserNum, mSocket.id());
//            String json = gson.toJson(socketModel);
//            mSocket.emit("globalListener", json);
        }
    };


    Emitter.Listener onDisConnect = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Timber.d("disconnect event -> %s", args);

        }
    };


    Emitter.Listener onEndChatMsgEvent = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            if (args != null) {
                String response = args[0].toString();
                Timber.d("onEndChatMsgEvent received  %s", response);
//                SocketMessageResponse messageResponse = gson.fromJson(response, SocketMessageResponse.class);
//                if (!messageResponse.getSender().equals(mCurrentUserNum)) {
//                    if (messageResponse.getSender().equals(mFriend.getUserContactNo())) {
//                        addItemToRecyclerView(messageResponse);
//                    }
//                }
//            }
            }
        }
    };


    Emitter.Listener readMsgStatusEvent = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            if (args != null) {
                String response = args[0].toString();
                Timber.d("readMsgStatusEvent received  %s", response);
//                SocketMessageResponse messageResponse = gson.fromJson(response, SocketMessageResponse.class);
//                if (!messageResponse.getSender().equals(mCurrentUserNum)) {
//                    if (messageResponse.getSender().equals(mFriend.getUserContactNo())) {
//                        addItemToRecyclerView(messageResponse);
//                    }
//                }


            }
        }
    };


    Emitter.Listener deleteMessageEvent = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            if (args != null) {
                String response = args[0].toString();
                Timber.d("deleteMessageEvent received  %s", response);
//                try {
//                    DeleteEventResponse eventResponse = gson.fromJson(response, DeleteEventResponse.class);
//                    Timber.d("deleteMessageEvent json  %s", eventResponse);
//                    if (eventResponse.getSender().equals(mCurrentUserNum)) {
//                        if (msgToBeDeleted.getMsgId().equals(eventResponse.getMsgid())) {
//                            Timber.d("message needs to be deleted as both id matches of mine");
//                            deleteItem();
//                        }
//                    } else {
//                        if (eventResponse.getSender().equals(mFriend.getUserContactNo())) {
//                            //delete logic for sender msg needs to be tweaked
//                            for (int i = 0; i < adapter.getList().size(); i++) {
//                                if (eventResponse.getMsgid().equals(adapter.getList().get(i).getMsgId())) {
//                                    Timber.d("message needs to be deleted as both id matches of sender");
//                                    itemPosition = i;
//                                    deleteItem();
//                                }
//                            }
//                        }
//                    }
//                } catch (Exception e) {
//                    Timber.d("exception occurred ..!!!");
//                    Toast.makeText(getActivity(), "Error occurred", Toast.LENGTH_SHORT).show();
                }
            }
        };



    //endregion
}

