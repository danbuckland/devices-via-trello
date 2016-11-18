package io.danb.devices.projectlist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import io.danb.devices.MainActivity;
import io.danb.devices.R;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {

    private ArrayList<Project> projectsList;
    private static Context mContext;

    public ProjectAdapter(Context context) {
        projectsList = new ArrayList<>();
        this.mContext = context;
    }

    @Override
    public int getItemCount() {
        return projectsList.size();
    }

    @Override
    public void onBindViewHolder(ProjectViewHolder projectViewHolder, int i) {
        Project project = projectsList.get(i);
        projectViewHolder.projectNameText.setText(project.getName());
        if (project.getNoOfiPhones() > 0) {
            projectViewHolder.noOfDevicesText.setText(project.getNoOfiPhones() + " iPhone(s)");
        }
        if (project.getNoOfiPads() > 0) {
            projectViewHolder.noOfDevicesText.setText(project.getNoOfiPhones() + " iPad(s)");
        }
        if (project.getNoOfAndroids() > 0) {
            projectViewHolder.noOfDevicesText.setText(project.getNoOfiPhones() + " Android(s)");
        }
        if (project.getNoOfOthers() > 0) {
            projectViewHolder.noOfDevicesText.setText(project.getNoOfiPhones() + " Other(s)");
        }
        projectViewHolder.currentItem = projectsList.get(i);
    }

    @Override
    public ProjectViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.list_project, viewGroup, false);

        return new ProjectViewHolder(itemView);
    }

    public void updateData(ArrayList<Project> projects) {
        // update the adapter's dataset
        projectsList = projects;
        notifyDataSetChanged();
    }

    public static class ProjectViewHolder extends RecyclerView.ViewHolder {
        public Project currentItem;
        protected TextView projectNameText;
        protected TextView noOfDevicesText;

        public ProjectViewHolder(View v) {
            super(v);

            projectNameText = (TextView) v.findViewById(R.id.list_project_name);
            noOfDevicesText = (TextView) v.findViewById(R.id.list_project_no_of_devices);

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
