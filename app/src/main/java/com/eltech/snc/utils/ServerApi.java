package com.eltech.snc.utils;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.eltech.snc.MainActivity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class ServerApi {
    private static volatile ServerApi instance;
    private static final String TAG = "ServerApi";
    private static final String CREATE_USER_URL = "/createUser";
    private static final String FIND_USER_URL = "/findUser";
    private static final String GET_UNLOCK_REGS_RESULT = "/getUnlockRegs";
    private static final String GET_STATISTICS = "/getStatistic";
    private static final String SAVE_IMAGE_RESULT = "/saveStencilStatistic";
    private static final String SAVE_MAZE_RESULT = "/saveMazeStatistic";
    private static final String SAVE_BALL_RESULT = "/saveRollingBallStatistic";
    private static final String SAVE_UNLOCK_RESULT = "/saveUnlock";
    private static final String SERVER_API_URL_PROPERTY_NAME = "server.api.url";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final String serverAddress;
    private final ThreadPoolExecutor executor = new ThreadPoolExecutor(0, 2,
                                                                       30, TimeUnit.SECONDS,
                                                                       new ArrayBlockingQueue<>(10));

    private Integer userId = -1;
    private String username;
    private boolean isNewUser = false;

    private ServerApi(final String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public static ServerApi getInstance() {
        if (instance == null) {
            synchronized (ServerApi.class) {
                if (instance == null) {
                    String serverAddress = MainActivity.properties.getProperty(SERVER_API_URL_PROPERTY_NAME);
                    instance = new ServerApi(serverAddress);
                }
            }
        }

        return instance;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public boolean isNewUser() {
        return isNewUser;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Integer createUser(final String userName, final Context context, final BiConsumer<Integer, String> onSuccess) {
        executor.submit(() -> {
            try {
                UserEntity userEntity = new UserEntity();
                userEntity.setName(userName);

                RequestFuture<JSONObject> future = RequestFuture.newFuture();
                RequestQueue requestQueue = Volley.newRequestQueue(context);
                JSONObject payload = new JSONObject(OBJECT_MAPPER.writeValueAsString(userEntity));
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, serverAddress + CREATE_USER_URL,
                                                                  payload, future, future);
                requestQueue.add(request);
                userId = future.get(10, TimeUnit.SECONDS).getInt("id");
                System.out.println("Created userId: " + userId);
                username = userName;
                isNewUser = true;
                onSuccess.accept(userId, username);
            } catch (JSONException | InterruptedException | ExecutionException | TimeoutException | JsonProcessingException e) {
                e.printStackTrace();
            }
        });
        return userId;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Integer getUserId(final String userName, final Context context, final BiConsumer<Integer, String> onSuccess) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.GET, serverAddress + FIND_USER_URL + "?name=" + userName,
                                                  response -> {
                                                      System.out.println("Got userId: " + response);
                                                      userId = Integer.valueOf(response);
                                                      username = userName;
                                                      onSuccess.accept(userId, username);
                                                  }, error -> System.out.println("Username " + userName + " is unknown or server error"));
        requestQueue.add(request);
        return userId;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void sendImageModuleResult(final double result, final Context context) throws JsonProcessingException, JSONException {
        if (userId == null) {
            System.err.println("Unknown user");
            return;
        }

        StencilEntity stencilEntity = new StencilEntity();
        stencilEntity.setUserId(userId);
        stencilEntity.setResult(result);
        stencilEntity.setDate(LocalDateTime.now().toString());

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject payload = new JSONObject(OBJECT_MAPPER.writeValueAsString(stencilEntity));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, serverAddress + SAVE_IMAGE_RESULT,
                                                          payload, response -> {
        }, null);

        requestQueue.add(request);
        Log.d(TAG, "Send ImageModule result. userId: " + userId + ", result: " + result);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void sendMazeModuleResult(final double result, final Context context) throws JsonProcessingException, JSONException {
        if (userId == null) {
            System.err.println("Unknown user");
            return;
        }

        MazeEntity mazeEntity = new MazeEntity();
        mazeEntity.setUserId(userId);
        mazeEntity.setResult(result);
        mazeEntity.setDate(LocalDateTime.now().toString());

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject payload = new JSONObject(OBJECT_MAPPER.writeValueAsString(mazeEntity));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, serverAddress + SAVE_MAZE_RESULT,
                                                          payload, response -> {
        }, null);

        requestQueue.add(request);
        Log.d(TAG, "Send MazeModule result. userId: " + userId + ", result: " + result);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void sendBallModuleResult(final double result, final Context context) throws JsonProcessingException, JSONException {
        if (userId == null) {
            System.err.println("Unknown user");
            return;
        }

        RollingBallEntity rollingBallEntity = new RollingBallEntity();
        rollingBallEntity.setUserId(userId);
        rollingBallEntity.setResult(result);
        rollingBallEntity.setDate(LocalDateTime.now().toString());

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject payload = new JSONObject(OBJECT_MAPPER.writeValueAsString(rollingBallEntity));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, serverAddress + SAVE_BALL_RESULT,
                                                          payload, response -> {
        }, null);

        requestQueue.add(request);
        Log.d(TAG, "Send BallModule result. userId: " + userId + ", result: " + result);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void sendUnlockResult(final List<Float> pointX, final List<Float> pointY, final Context context, final Consumer<Integer> onResponse)
            throws JsonProcessingException, JSONException {
        if (userId == null) {
            System.err.println("Unknown user");
            return;
        }

        UnlockListEntity unlockListEntity = new UnlockListEntity();
        unlockListEntity.setUserId(userId);
        unlockListEntity.setPointX(pointX);
        unlockListEntity.setPointY(pointY);
        unlockListEntity.setDate(LocalDateTime.now().toString());

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject payload = new JSONObject(OBJECT_MAPPER.writeValueAsString(unlockListEntity));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, serverAddress + SAVE_UNLOCK_RESULT,
                                                          payload, response -> {
            System.out.println(response);
            try {
                onResponse.accept(response.getInt("id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            System.out.println(error);
        });

        requestQueue.add(request);
        Log.d(TAG, "Send Unlock result. userId: " + userId + ", pointsX: " + pointX + ", pointsY: " + pointY);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getUnlockRegs(final Context context, final Consumer<Integer> onResponse) {
        if (userId == null) {
            System.err.println("Unknown user");
            return;
        }

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.GET, serverAddress + GET_UNLOCK_REGS_RESULT + "?userId=" + userId,
                                                  response -> {
                                                      System.out.println(response);
                                                      onResponse.accept(Integer.valueOf(response));
                                                  }, null);

        requestQueue.add(request);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getStatistics(final Context context, final Consumer<StatisticEntity> onResponse) {
        if (userId == null) {
            System.err.println("Unknown user");
            return;
        }

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.GET, serverAddress + GET_STATISTICS + "?id=" + userId,
                                                  response -> {
                                                      System.out.println(response);
                                                      try {
                                                          StatisticEntity statisticEntity = OBJECT_MAPPER.readValue(response, StatisticEntity.class);
                                                          onResponse.accept(statisticEntity);
                                                      } catch (JsonProcessingException e) {
                                                          e.printStackTrace();
                                                      }
                                                  }, null);

        requestQueue.add(request);
    }
}
