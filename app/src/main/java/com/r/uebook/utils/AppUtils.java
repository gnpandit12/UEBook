package com.r.uebook.utils;

import com.google.gson.Gson;
import com.r.uebook.data.remote.model.UserProfile;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import timber.log.Timber;

import static com.r.uebook.utils.ApplicationConstants.LOGGED_IN;
import static com.r.uebook.utils.ApplicationConstants.USER_DETAILS;

public class AppUtils {


    public static void setLogin(UserProfile userProfile) {
        try {
            if (userProfile != null) {
                Gson gson = new Gson();
                String userDetails = gson.toJson(userProfile);
                Prefs.setPreferences(USER_DETAILS, userDetails);
                Prefs.setBoolean(LOGGED_IN, true);
            }
        } catch (Exception e) {
            Timber.d("exception in saving details");
        }
    }

    public static UserProfile getUserDetails() {
        try {
            Gson gson = new Gson();
            String userDetails = Prefs.getPreferences(USER_DETAILS);
            return gson.fromJson(userDetails, UserProfile.class);
        } catch (Exception e) {
            Timber.d("exception in fetching user details");
            return null;
        }
    }

    //check login
    public static boolean isLoggedIn() {
        return Prefs.getBoolean(LOGGED_IN, false);
    }

    //logout
    public static void loggedOut() {
        Prefs.clearPreferences();
    }


    public static void LogOutUser() {

    }

    public static String getCurrentTimeStamp(Date date) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("hh.mm aa", Locale.US);
            return dateFormat.format(date).toString();

        } catch (Exception e) {
            Timber.d("error in fetching current time");
            return null;
        }

    }

}
