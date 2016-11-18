package io.danb.devices.api;


import java.util.ArrayList;

import io.danb.devices.model.TrelloList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TrelloApi {

    public static final String BASE_URL = "https://api.trello.com/1/";
    public static final String APP_KEY = "91bd7382a964408ef824f8d588d6327f";
    public static final String AUTH_TOKEN = "";

    // Request method and URL specified in the annotation
    // Callback for the parsed response is the last parameter

    // Get lists and cards from Trello board with all information
    @GET("boards/{boardId}/lists")
    Call<ArrayList<TrelloList>> getLists(@Path("boardId") String boardId,
                                         @Query("key") String app_key,
                                         @Query("token") String auth_token,
                                         @Query("cards") String cards,
                                         @Query("card_fields") String card_fields,
                                         @Query("fields") String fields);

}
