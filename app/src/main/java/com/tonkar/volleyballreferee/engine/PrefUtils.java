package com.tonkar.volleyballreferee.engine;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.tonkar.volleyballreferee.engine.stored.JsonIOUtils;
import com.tonkar.volleyballreferee.engine.stored.api.ApiUserSummary;
import com.tonkar.volleyballreferee.engine.stored.api.ApiUserToken;
import com.tonkar.volleyballreferee.engine.stored.api.ApiUtils;

import java.util.Calendar;
import java.util.TimeZone;

public class PrefUtils {

    public static final  String PREF_KEEP_SCREEN_ON            = "pref_keep_screen_on";
    public static final  String PREF_INTERACTIVE_NOTIFICATIONS = "pref_interactive_notification";
    private static final String PREF_USER                      = "pref_user";
    private static final String PREF_USER_TOKEN                = "pref_user_token";
    private static final String PREF_USER_TOKEN_EXPIRY         = "pref_user_token_expiry";
    private static final String PREF_WEB_PREMUIM_TOKEN         = "pref_web_premium_token";
    private static final String PREF_USER_ID                   = "pref_user_identifier";
    private static final String PREF_USER_PSEUDO               = "pref_user_pseudo";

    public static void signIn(Context context, ApiUserToken userToken) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences
                .edit()
                .putString(PREF_USER, JsonIOUtils.GSON.toJson(userToken.getUser()))
                .putString(PREF_USER_TOKEN, userToken.getToken())
                .putLong(PREF_USER_TOKEN_EXPIRY, userToken.getTokenExpiry())
                .apply();
    }

    public static void storeUser(Context context, ApiUserSummary user) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences
                .edit()
                .putString(PREF_USER, JsonIOUtils.GSON.toJson(user))
                .apply();
    }

    public static ApiUserSummary getUser(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String userStr = sharedPreferences.getString(PREF_USER, "");
        if ("".equals(userStr)) {
            return ApiUserSummary.emptyUser();
        } else {
            return JsonIOUtils.GSON.fromJson(userStr, ApiUserSummary.class);
        }
    }

    public static void signOut(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences
                .edit()
                .remove(PREF_USER_TOKEN)
                .apply();
    }

    public static boolean isSignedIn(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return !"".equals(sharedPreferences.getString(PREF_USER, ""))
                && !"".equals(sharedPreferences.getString(PREF_USER_TOKEN, ""))
                && Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTime().getTime() < sharedPreferences.getLong(PREF_USER_TOKEN_EXPIRY, 0L);
    }

    public static ApiUserToken getUserToken(Context context) {
        ApiUserToken userToken;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String userStr = sharedPreferences.getString(PREF_USER, "");
        String tokenStr = sharedPreferences.getString(PREF_USER_TOKEN, "");
        long tokenExpiry = sharedPreferences.getLong(PREF_USER_TOKEN_EXPIRY, 0L);

        if (!"".equals(userStr) && !"".equals(tokenStr)) {
            userToken = new ApiUserToken(JsonIOUtils.GSON.fromJson(userStr, ApiUserSummary.class), tokenStr, tokenExpiry);
        } else {
            userToken = new ApiUserToken(ApiUserSummary.emptyUser(), "", 0L);
        }

        return userToken;
    }

    public static void purchaseWebPremium(Context context, String purchaseToken) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putString(PREF_WEB_PREMUIM_TOKEN, purchaseToken).apply();
    }

    public static boolean isWebPremiumPurchased(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return !"".equals(sharedPreferences.getString(PREF_WEB_PREMUIM_TOKEN, ""));
    }

    public static String getWebPremiumPurchaseToken(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(PREF_WEB_PREMUIM_TOKEN, "");
    }

    public static boolean oldUserExists(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return !"".equals(sharedPreferences.getString(PREF_USER_PSEUDO, "")) && !"".equals(sharedPreferences.getString(PREF_USER_ID, ""));
    }

    public static String getOldUserId(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(PREF_USER_ID, "");
    }

    public static String getOldUserPseudo(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(PREF_USER_PSEUDO, "");
    }

    public static boolean canSync(Context context) {
        return isWebPremiumPurchased(context) && ApiUtils.isConnectedToInternet(context) && isSignedIn(context);
    }

    public static boolean shouldSignIn(Context context) {
        return isWebPremiumPurchased(context) && ApiUtils.isConnectedToInternet(context) && !isSignedIn(context);
    }
}
