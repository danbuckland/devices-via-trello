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


        familyViewHolder.availableDevices.setText("");
        familyViewHolder.assignedDevices.setText("");
        if (family.getAvailableDevices() > 0) {
            familyViewHolder.availableDevices.setText(family.getAvailableDevices() + " Available");
        }
        if (family.getAssignedDevices() > 0) {
            familyViewHolder.assignedDevices.setText(family.getAssignedDevices() + " Assigned");
        }
        familyViewHolder.currentItem = familiesList.get(i);
    }

    @Override
    public FamilyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.list_family, viewGroup, false);

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
        protected TextView availableDevices;
        protected TextView assignedDevices;

        public FamilyViewHolder(View v) {
            super(v);

            familyNameText = (TextView) v.findViewById(R.id.list_family_name);
            availableDevices = (TextView) v.findViewById(R.id.list_family_no_of_available);
            assignedDevices = (TextView) v.findViewById(R.id.list_family_no_of_assigned);

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
