package io.danb.devices;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.stetho.Stetho;

import java.util.ArrayList;

import io.danb.devices.api.ServiceGenerator;
import io.danb.devices.api.TrelloApi;
import io.danb.devices.model.TrelloCard;
import io.danb.devices.model.TrelloList;
import io.danb.devices.projectlist.Project;
import io.danb.devices.projectlist.ProjectAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public TrelloApi trelloApi;
    public ProgressBar progressBar;
    public ArrayList<TrelloList> trelloLists;
    public ArrayList<TrelloCard> trelloCards;
    public ArrayList<Project> projects;
    ProjectAdapter projectAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Stetho.initializeWithDefaults(this);
        trelloApi = ServiceGenerator.createService(TrelloApi.class);

        progressBar = (ProgressBar) findViewById(R.id.main_progress_bar);
        progressBar.setVisibility(View.INVISIBLE);

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
        
        getProjects();

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.main_swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                refreshProjects();
            }
        });
    }

    private void getProjects() {
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
                    // convert each Trello list into a Project
                    projects = new ArrayList<Project>();
                    for (TrelloList listItem : trelloLists) {
                        projects.add(new Project(listItem));
                    }
                    // Update data in custom view adapter
                    projectAdapter.updateData(projects);
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

    private void refreshProjects() {
        // Load items
        getProjects();

        // Load complete
        onItemsLoadComplete();
    }

    private void onItemsLoadComplete() {
        mSwipeRefreshLayout.setRefreshing(false);
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
