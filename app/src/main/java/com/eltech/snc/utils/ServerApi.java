package com.eltech.snc.utils;

import android.content.Context;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public final class ServerApi {
    private static volatile ServerApi instance;
    private static final String CREATE_USER_URL = "/createUser";
    private static final String SAVE_IMAGE_RESULT = "/saveStencilStatistic";
    private static final String SAVE_MAZE_RESULT = "/saveMazeStatistic";
    private static final String SAVE_BALL_RESULT = "/saveRollingBallStatistic";

    private ServerApi() {}

    public static ServerApi getInstance() {
        if (instance == null) {
            synchronized (ServerApi.class) {
                if (instance == null) {
                    instance = new ServerApi();
                }
            }
        }

        return instance;
    }

    public Integer createUser(final String userName, final Context context) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        throw new UnsupportedOperationException();
    }

    public Integer getUserId(final String userName) {
        throw new UnsupportedOperationException();
    }

    public void sendImageModuleResult(final float result, final Context context) {
        throw new UnsupportedOperationException();
    }

    public void sendMazeModuleResult(final float result, final Context context) {
        throw new UnsupportedOperationException();
    }

    public void sendBallModuleResult(final float result, final Context context) {
        throw new UnsupportedOperationException();
    }
}
