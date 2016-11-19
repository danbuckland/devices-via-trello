package io.danb.devices.familylist;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import io.danb.devices.MainActivity;
import io.danb.devices.R;

public class FamilyAdapter extends RecyclerView.Adapter<FamilyAdapter.FamilyViewHolder>{

    private ArrayList<Family> familiesList;
    private static Context mContext;

    public FamilyAdapter(Context context) {
        familiesList = new ArrayList<>();
        this.mContext = context;
    }

    @Override
    public int getItemCount() {
        return familiesList.size();
    }

    @Override
    public void onBindViewHolder(FamilyViewHolder familyViewHolder, int i) {
        Family family = familiesList.get(i);
        familyViewHolder.familyNameText.setText(family.getName());
        familyViewHolder.currentItem = familiesList.get(i);
    }

    @Override
    public FamilyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.list_project, viewGroup, false);

        return new FamilyViewHolder(itemView);
    }

    public void updateData(ArrayList<Family> families) {
        // update the adapter's dataset
        familiesList = families;
        notifyDataSetChanged();
    }

    public static class FamilyViewHolder extends RecyclerView.ViewHolder {
        public Family currentItem;
        protected TextView familyNameText;

        public FamilyViewHolder(View v) {
            super(v);

            familyNameText = (TextView) v.findViewById(R.id.list_project_name);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mContext instanceof MainActivity) {
                        // ((MainActivity)mContext).viewDevicesActivity(currentItem, v);
                    }
                }
            });
        }
    }
}
