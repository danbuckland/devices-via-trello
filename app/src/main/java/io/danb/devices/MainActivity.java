package io.danb.devices;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.stetho.Stetho;

import java.util.ArrayList;

import io.danb.devices.api.ServiceGenerator;
import io.danb.devices.api.TrelloApi;
import io.danb.devices.model.TrelloCard;
import io.danb.devices.model.TrelloList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public TrelloApi trelloApi;
    public ProgressBar progressBar;
    public Button getListsBtn;
    public Button getCardsBtn;
    public ArrayList<TrelloList> trelloLists;
    public ArrayList<TrelloCard> trelloCards;
    ProjectAdapter projectAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Stetho.initializeWithDefaults(this);
        trelloApi = ServiceGenerator.createService(TrelloApi.class);

        progressBar = (ProgressBar) findViewById(R.id.main_progress_bar);
        progressBar.setVisibility(View.INVISIBLE);
        getListsBtn = (Button) findViewById(R.id.main_btn_get_lists);
        getCardsBtn = (Button) findViewById(R.id.main_btn_get_cards);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.main_recycler_projects);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter
        projectAdapter = new ProjectAdapter(this);
        mRecyclerView.setAdapter(projectAdapter);

        getListsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLists();
            }
        });

        getCardsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCards();
            }
        });
    }

    private void getLists() {
        // Show progress indicator while working
        progressBar.setVisibility(View.VISIBLE);

        String cards = "open";
        String card_fields = "labels,name,id,desc";
        String fields = "card,name,id";
        final Call<ArrayList<TrelloList>> call = trelloApi.getLists(TrelloApi.BOARD_ID,
                TrelloApi.APP_KEY, TrelloApi.AUTH_TOKEN, cards, card_fields, fields);

        // Make the request
        call.enqueue(new Callback<ArrayList<TrelloList>>() {

            @Override
            public void onResponse(Call<ArrayList<TrelloList>> arrayListCall, Response<ArrayList<TrelloList>> response) {
                trelloLists = response.body();

                // Get list names from response and add each to a single string
                if (trelloLists != null) {
                    String trelloListsString = "";
                    for (TrelloList listItem : trelloLists) {
                        trelloListsString = trelloListsString + listItem.getName() + "\n";
                    }

                    // Update data in custom view adapter
                    projectAdapter.updateData(trelloLists);
                }

                // Hide progress indicator when done
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<ArrayList<TrelloList>> arrayListCall, Throwable t) {
                // Log error here since request failed
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("Retrofit", t.getMessage());
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void getCards() {
        // Show progress indicator while working
        progressBar.setVisibility(View.VISIBLE);

        String cards = "open";
        String card_fields = "labels,name,id,desc";
        String fields = "card,name,id";
        final Call<ArrayList<TrelloList>> call = trelloApi.getLists(TrelloApi.BOARD_ID,
                TrelloApi.APP_KEY, TrelloApi.AUTH_TOKEN, cards, card_fields, fields);

        // Make the request
        call.enqueue(new Callback<ArrayList<TrelloList>>() {

            @Override
            public void onResponse(Call<ArrayList<TrelloList>> arrayListCall, Response<ArrayList<TrelloList>> response) {
                trelloLists = response.body();

                // Get list names from response and add each to a single string
                if (trelloLists != null) {

                    String trelloCardsString = "";

                    for (TrelloList listItem : trelloLists) {
                        trelloCards = listItem.getTrelloCards();
                        for (TrelloCard trelloCard : trelloCards) {
                            trelloCardsString = trelloCardsString + trelloCard.getName() + "\n";
                        }
                    }

                    // Update text view
                    // responseTxt.setText(trelloCardsString);
                }

                // Hide progress indicator when done
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<ArrayList<TrelloList>> arrayListCall, Throwable t) {
                // Log error here since request failed
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("Retrofit", t.getMessage());
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}
