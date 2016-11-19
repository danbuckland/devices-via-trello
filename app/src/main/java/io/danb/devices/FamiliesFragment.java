package io.danb.devices;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.stetho.Stetho;

import java.util.ArrayList;

import io.danb.devices.api.ServiceGenerator;
import io.danb.devices.api.TrelloApi;
import io.danb.devices.familylist.Family;
import io.danb.devices.familylist.FamilyAdapter;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FamiliesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FamiliesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FamiliesFragment extends Fragment {
    FamilyAdapter familyAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    public TrelloApi trelloApi;
    public ArrayList<Family> families;
    public Context context;

    private OnFamilyInteractionListener mListener;

    public FamiliesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FamiliesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FamiliesFragment newInstance(String param1, String param2) {
        FamiliesFragment fragment = new FamiliesFragment();
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
        familyAdapter = new FamilyAdapter(getActivity());
        mRecyclerView.setAdapter(familyAdapter);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        getFamilies();

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.projects_swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                refreshFamilies();
            }
        });
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onFamilySelected(int position) {
        if (mListener != null) {
            mListener.onFamilyInteraction(position);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFamilyInteractionListener) {
            mListener = (OnFamilyInteractionListener) context;
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
    public interface OnFamilyInteractionListener {
        // TODO: Update argument type and name
        void onFamilyInteraction(int position);
    }

    private void getFamilies() {
        // TODO: Add a getFamilies method that doesn't require an additional network call
    }

    private void refreshFamilies() {
        // TODO: Do a netwrok refresh here
        getFamilies();

        // Load complete
        onItemsLoadComplete();
    }

    private void onItemsLoadComplete() {
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
