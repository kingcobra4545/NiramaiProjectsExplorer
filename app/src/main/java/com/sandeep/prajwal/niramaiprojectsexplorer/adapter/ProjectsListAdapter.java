package com.sandeep.prajwal.niramaiprojectsexplorer.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sandeep.prajwal.niramaiprojectsexplorer.R;
import com.sandeep.prajwal.niramaiprojectsexplorer.Utils;
import com.sandeep.prajwal.niramaiprojectsexplorer.databinding.ActivityHomeScreenBinding;
import com.sandeep.prajwal.niramaiprojectsexplorer.model.ProjectData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by KingCobra on 27/09/19.
 */

public class ProjectsListAdapter extends RecyclerView.Adapter<ProjectsListAdapter.ViewHolder> implements Filterable {

    List<ProjectData> listdata;
    ValueFilter valueFilter;
    List<ProjectData> mStringFilterList;
    ActivityHomeScreenBinding binding;

    public ProjectsListAdapter(List<ProjectData> listdata) {
        this.listdata = listdata;
        this.mStringFilterList = listdata;
    }
    View listItem;
    @Override
    public ProjectsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.activity_home_screen, (RecyclerView)parent, false);
        listItem = layoutInflater.inflate(R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ProjectsListAdapter.ViewHolder holder, int position) {
        holder.title_text_view.setText(listdata.get(position).getTitle());
        holder.companyName.setText(listdata.get(position).getCompanyName());

        holder.shortDesc.setText(listdata.get(position).getShortDesc());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        holder.timeCreated.setText(format.format(new Date(listdata.get(position).getTimeCreated())));
        for (ProjectData data:
             listdata) {
            Log.i(Utils.PRAJWAL, " Data received by adapter - > " + data.getTitle());
        }
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                List<ProjectData> filterList = new ArrayList<>();
                for (int i = 0; i < mStringFilterList.size(); i++) {
                    if ((mStringFilterList.get(i).getTitle().toUpperCase()).contains(constraint.toString().toUpperCase())) {
                        filterList.add(mStringFilterList.get(i));
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = mStringFilterList.size();
                results.values = mStringFilterList;
            }
            return results;

        }

        @Override
        @SuppressWarnings("unchecked")
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {

            listdata = (List<ProjectData>) results.values;
            notifyDataSetChanged();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView title_text_view, shortDesc,timeCreated, companyName;
        private RelativeLayout relativeLayout;
        private ViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
            this.title_text_view = (TextView) itemView.findViewById(R.id.title_text_view);
            this.companyName = (TextView) itemView.findViewById(R.id.company_name);
            this.shortDesc = (TextView) itemView.findViewById(R.id.short_desc);
            this.timeCreated = (TextView) itemView.findViewById(R.id.created_time);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout);
        }
    }
}
