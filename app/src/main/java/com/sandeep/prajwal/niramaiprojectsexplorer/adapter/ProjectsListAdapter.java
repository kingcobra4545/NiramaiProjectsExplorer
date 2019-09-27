package com.sandeep.prajwal.niramaiprojectsexplorer.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sandeep.prajwal.niramaiprojectsexplorer.R;
import com.sandeep.prajwal.niramaiprojectsexplorer.model.ProjectData;

/**
 * Created by KingCobra on 27/09/19.
 */

public class ProjectsListAdapter extends RecyclerView.Adapter<ProjectsListAdapter.ViewHolder>  {

    ProjectData[] listdata;
    public ProjectsListAdapter(ProjectData[] listdata) {
        this.listdata = listdata;
    }
    @Override
    public ProjectsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ProjectsListAdapter.ViewHolder holder, int position) {
        holder.title_text_view.setText(listdata[position].getTitle());
        holder.shortDesc.setText(listdata[position].getShortDesc());
        holder.timeCreated.setText(listdata[position].getTimeCreated());
    }

    @Override
    public int getItemCount() {
        return listdata.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView title_text_view, shortDesc,timeCreated;
        public RelativeLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
            this.title_text_view = (TextView) itemView.findViewById(R.id.title_text_view);
            this.shortDesc = (TextView) itemView.findViewById(R.id.short_desc);
            this.timeCreated = (TextView) itemView.findViewById(R.id.created_time);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout);
        }
    }
}
