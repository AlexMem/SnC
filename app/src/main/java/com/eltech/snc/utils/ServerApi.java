package com.eltech.snc.utils;

import android.content.Context;
import android.graphics.Path;
import android.os.Build;
import androidx.annotation.RequiresApi;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.eltech.snc.server.jpa.entity.MazeEntity;
import com.eltech.snc.server.jpa.entity.RollingBallEntity;
import com.eltech.snc.server.jpa.entity.StencilEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.std.JsonValueSerializer;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public final class ServerApi {
    private static volatile ServerApi instance;
    private static final String CREATE_USER_URL = "/createUser";
    private static final String FIND_USER_URL = "/findUser";
    private static final String SAVE_IMAGE_RESULT = "/saveStencilStatistic";
    private static final String SAVE_MAZE_RESULT = "/saveMazeStatistic";
    private static final String SAVE_BALL_RESULT = "/saveRollingBallStatistic";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final String serverAddress = "";

    private Integer userId;

    private ServerApi() {
    }

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

    public Integer createUser(final String userName, final Context context) throws JSONException, InterruptedException, ExecutionException, TimeoutException {
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject payload = new JSONObject().accumulate("userName", userName);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, serverAddress + CREATE_USER_URL,
                                                          payload, future, future);
        requestQueue.add(request);
        Integer id = (Integer) future.get(30, TimeUnit.SECONDS).get("id");
        userId = id;
        return id;
    }

    public Integer getUserId(final String userName, final Context context) throws InterruptedException, ExecutionException, TimeoutException {
        RequestFuture<String> future = RequestFuture.newFuture();
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.GET, serverAddress + FIND_USER_URL + "?" + userName,
                                                  future, future);
        requestQueue.add(request);
        Integer id = Integer.valueOf(future.get(30, TimeUnit.SECONDS));
        userId = id;
        return id;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void sendImageModuleResult(final double result, final Context context) throws JsonProcessingException, JSONException {
        if (userId == null) throw new RuntimeException("Unknown user");

        StencilEntity stencilEntity = new StencilEntity();
        stencilEntity.setUserId(userId);
        stencilEntity.setResult(result);
        stencilEntity.setDate(LocalDateTime.now());

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject payload = new JSONObject(OBJECT_MAPPER.writeValueAsString(stencilEntity));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, serverAddress + SAVE_IMAGE_RESULT,
                                                          payload, response -> {}, null);

        requestQueue.add(request);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void sendMazeModuleResult(final double result, final Context context) throws JsonProcessingException, JSONException {
        if (userId == null) throw new RuntimeException("Unknown user");

        MazeEntity mazeEntity = new MazeEntity();
        mazeEntity.setUserId(userId);
        mazeEntity.setResult(result);
        mazeEntity.setDate(LocalDateTime.now());

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject payload = new JSONObject(OBJECT_MAPPER.writeValueAsString(mazeEntity));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, serverAddress + SAVE_MAZE_RESULT,
                                                          payload, response -> {}, null);

        requestQueue.add(request);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void sendBallModuleResult(final double result, final Context context) throws JsonProcessingException, JSONException {
        if (userId == null) throw new RuntimeException("Unknown user");

        RollingBallEntity rollingBallEntity = new RollingBallEntity();
        rollingBallEntity.setUserId(userId);
        rollingBallEntity.setResult(result);
        rollingBallEntity.setDate(LocalDateTime.now());

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject payload = new JSONObject(OBJECT_MAPPER.writeValueAsString(rollingBallEntity));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, serverAddress + SAVE_BALL_RESULT,
                                                          payload, response -> {}, null);

        requestQueue.add(request);
    }
}
