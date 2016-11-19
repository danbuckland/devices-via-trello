package io.danb.devices;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.stetho.Stetho;

import java.util.ArrayList;

import io.danb.devices.api.ServiceGenerator;
import io.danb.devices.api.TrelloApi;
import io.danb.devices.model.TrelloList;
import io.danb.devices.projectlist.Project;
import io.danb.devices.projectlist.ProjectAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProjectsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProjectsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProjectsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    ProjectAdapter projectAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    public TrelloApi trelloApi;
    public ArrayList<TrelloList> trelloLists;
    public ArrayList<Project> projects;
    public Context context;

    private OnProjectInteractionListener mListener;

    public ProjectsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProjectsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProjectsFragment newInstance(String param1, String param2) {
        ProjectsFragment fragment = new ProjectsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_projects, container, false);

        Stetho.initializeWithDefaults(getActivity());
        trelloApi = ServiceGenerator.createService(TrelloApi.class);

        RecyclerView mRecyclerView = (RecyclerView) rootView.findViewById(R.id.main_recycler_projects);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // specify an adapter
        projectAdapter = new ProjectAdapter(getActivity());
        mRecyclerView.setAdapter(projectAdapter);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        getProjects();

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.main_swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                refreshProjects();
            }
        });
        return rootView;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onProjectSelected(int position) {
        if (mListener != null) {
            mListener.onProjectInteraction(position);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnProjectInteractionListener) {
            mListener = (OnProjectInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnProjectInteractionListener {
        // TODO: Update argument type and name
        void onProjectInteraction(int position);
    }

    private void getProjects() {

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

            }

            @Override
            public void onFailure(Call<ArrayList<TrelloList>> arrayListCall, Throwable t) {
                // Log error here since request failed
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("Retrofit", t.getMessage());
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
}
