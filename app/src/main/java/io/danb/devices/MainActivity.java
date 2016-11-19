package io.danb.devices;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.stetho.Stetho;

import java.util.ArrayList;

import io.danb.devices.api.ServiceGenerator;
import io.danb.devices.api.TrelloApi;
import io.danb.devices.familylist.Family;
import io.danb.devices.model.TrelloCard;
import io.danb.devices.model.TrelloLabel;
import io.danb.devices.model.TrelloList;
import io.danb.devices.projectlist.Project;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements ProjectsFragment.OnProjectInteractionListener, FamiliesFragment.OnFamilyInteractionListener {

    private ViewPager mViewPager;
    public static TrelloApi trelloApi;
    public static ArrayList<TrelloList> trelloLists;
    public static ArrayList<Project> projects;
    public static ArrayList<Family> families;
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Stetho.initializeWithDefaults(this);
        trelloApi = ServiceGenerator.createService(TrelloApi.class);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        mViewPager = (ViewPager) findViewById(R.id.main_container);
        PagerAdapter pagerAdapter =
                new PagerAdapter(getSupportFragmentManager(), MainActivity.this);
        mViewPager.setAdapter(pagerAdapter);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.main_tabs);
        tabLayout.setupWithViewPager(mViewPager);

        getDevicesData();

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    class PagerAdapter extends FragmentPagerAdapter {

        String tabTitles[] = new String[] { "Projects", "Families" };
        Context context;

        public PagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return new FamiliesFragment();
                case 1:
                    return new ProjectsFragment();
            }

            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            switch (position) {
                case 0:
                    return "Devices";
                case 1:
                    return "Projects";
            }
            return null;
        }

    }

    public void onProjectInteraction(int position) {
        // The user selected a project from the ProjectsFragment
    }

    public void onFamilyInteraction(int position) {
        // The user selected a project from the FamiliesFragment
    }

    public static void getDevicesData() {

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

                int availableiPhones = 0;
                int availableiPads = 0;
                int availableAndroids = 0;
                int availableOthers = 0;

                int assignediPhones = 0;
                int assignediPads = 0;
                int assignedAndroids = 0;
                int assignedOthers = 0;

                // Get list names from response and add each to a single string
                if (trelloLists != null) {
                    // convert each Trello list into a Project
                    projects = new ArrayList<Project>();
                    families = new ArrayList<Family>();
                    ArrayList<TrelloCard> trelloCards = new ArrayList<TrelloCard>();
                    for (TrelloList listItem : trelloLists) {
                        // Add each list to the array of projects
                        projects.add(new Project(listItem));
                        if (listItem.getName().equals("Available Devices")) {
                            // Iterate through each card in the Available list and tally totals by type
                            trelloCards = listItem.getTrelloCards();
                            for (TrelloCard trelloCard : trelloCards) {
                                ArrayList<TrelloLabel> trelloLabels = trelloCard.getLabels();
                                if (trelloLabels.size() > 0) {
                                    for (TrelloLabel trelloLabel : trelloLabels) {
                                        switch (trelloLabel.getName()) {
                                            case "iPhone": availableiPhones++;
                                                break;
                                            case "iPad": availableiPads++;
                                                break;
                                            case "Android": availableAndroids++;
                                                break;
                                            case "Other": availableOthers++;
                                        }
                                    }
                                }
                            }
                        } else {
                            // Iterate through each card in all others lists and tally totals by type
                            trelloCards = listItem.getTrelloCards();
                            for (TrelloCard trelloCard : trelloCards) {
                                ArrayList<TrelloLabel> trelloLabels = trelloCard.getLabels();
                                if (trelloLabels.size() > 0) {
                                    for (TrelloLabel trelloLabel : trelloLabels) {
                                        switch (trelloLabel.getName()) {
                                            case "iPhone": assignediPhones++;
                                                break;
                                            case "iPad": assignediPads++;
                                                break;
                                            case "Android": assignedAndroids++;
                                                break;
                                            case "Other": assignedOthers++;
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Create a Family for each device platform and add it to the families array
                    families.add(new Family("iPhones", availableiPhones, assignediPhones));
                    families.add(new Family("iPads", availableiPads, assignediPads));
                    families.add(new Family("Androids", availableAndroids, assignedAndroids));
                    families.add(new Family("Others", availableOthers, assignedOthers));

                    // Update data in custom view adapters
                    ProjectsFragment.projectAdapter.updateData(projects);
                    FamiliesFragment.familyAdapter.updateData(families);
                }

            }

            @Override
            public void onFailure(Call<ArrayList<TrelloList>> arrayListCall, Throwable t) {
                // Log error here since request failed
                Toast.makeText(context.getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("Retrofit", t.getMessage());
            }
        });
    }
}
