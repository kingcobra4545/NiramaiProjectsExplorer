package com.sandeep.prajwal.niramaiprojectsexplorer;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.sandeep.prajwal.niramaiprojectsexplorer.adapter.ItemAdapter;
import com.sandeep.prajwal.niramaiprojectsexplorer.adapter.ProjectsListAdapter;
import com.sandeep.prajwal.niramaiprojectsexplorer.databinding.ActivityHomeScreenBinding;
import com.sandeep.prajwal.niramaiprojectsexplorer.model.ProjectData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomeScreenActivity extends AppCompatActivity implements ItemAdapter.ItemListener {
    ActivityHomeScreenBinding binding;
    BottomSheetBehavior behavior, behaviorFilter;
    RecyclerView recyclerViewSortItems;
    private ItemAdapter mAdapter, mAdapterFilter;
    ProjectsListAdapter adapter;
    List<ProjectData> mDataList;
    RecyclerView recyclerView, recyclerViewFilter;
    Context context;
    ItemAdapter.ItemListener listener;
    List<String> listOfSelectedFilterItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_home_screen);
        context = this;
        listOfSelectedFilterItems = new ArrayList<>();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home_screen);
        View bottomSheet = binding.bottomSheet;
        behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        Button sortButton = binding.contentMain.sortButton;
        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        View bottomSheetFilter = binding.bottomSheetFilter;
        behaviorFilter = BottomSheetBehavior.from(bottomSheetFilter);
        behaviorFilter.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        Button filterButton = binding.contentMain.filterButton;
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                behaviorFilter.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        recyclerViewSortItems = binding.recyclerViewSortItems;
        recyclerViewFilter = binding.recyclerViewFilterItems;
        recyclerViewSortItems.setHasFixedSize(true);
        recyclerViewSortItems.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewFilter.setHasFixedSize(true);
        recyclerViewFilter.setLayoutManager(new LinearLayoutManager(this));

        List<String> items = new ArrayList();
        items.add(Utils.A_Z);
        items.add(Utils.Z_A);
        /*items.add(Utils.low_high);
        items.add(Utils.high_low);*/
        mDataList = new ArrayList<>();
        mDataList.add(new ProjectData("bProject1","Short Description1",
                "Long Description1", 1569617330000l, "Cisco"));
        mDataList.add(new ProjectData("aProject2","Short Description2",
                "Long Description2", 1569677130000l, "SAP"));
        mDataList.add(new ProjectData("bProject1","Short Description1",
                "Long Description1", 1569677130000l, "SAP"));
        mDataList.add(new ProjectData("aaProject2","Short Description2",
                "Long Description2", 1569673430000l, "SAP"));
        mDataList.add(new ProjectData("baProject1","Short Description1",
                "Long Description1", 1569677130000l, "IBM"));
        mDataList.add(new ProjectData("bbaProject2","Short Description2",
                "Long Description2", 1569677430000l, "ACCENTURE"));
        final List<String> listOfFilterItems = new ArrayList<>();
        for (ProjectData unitData: mDataList) {
            if(!listOfFilterItems.contains(unitData.getCompanyName()))
                listOfFilterItems.add(unitData.getCompanyName());
        }



        mAdapter = new ItemAdapter(this, items, this, Utils.SORT);
        mAdapterFilter = new ItemAdapter(this, listOfFilterItems, this, Utils.FILTER);
        recyclerViewSortItems.setAdapter(mAdapter);
        recyclerViewFilter.setAdapter(mAdapterFilter);




        recyclerView = (RecyclerView) binding.contentMain.list;
        adapter = new ProjectsListAdapter(mDataList);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager ll = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(ll);
        recyclerView.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                ll.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        binding.contentMain.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                adapter.getFilter().filter(newText);

                return false;
            }
        });
        binding.contentMain.searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.contentMain.searchView.setIconified(false);
            }
        });
        final List<ProjectData> filteredList = new ArrayList<>();
        binding.applyFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filteredList.clear();
                Log.i(Utils.PRAJWAL, "listOfSelectedFilterItems.size()-->  " + listOfSelectedFilterItems.size());
                if(listOfSelectedFilterItems.size()==0) {
                    adapter = new ProjectsListAdapter(mDataList);
                    recyclerView.setAdapter(adapter);
                    behaviorFilter.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    return;
                }
                for (ProjectData unitData:
                     mDataList) {
                    Log.i(Utils.PRAJWAL, " List of Selected filter items --> " );
                    for (String s:
                         listOfSelectedFilterItems) {
                        Log.i(Utils.PRAJWAL, " " + s );
                    }
//                    if(PreferencesDB.getString(context,Utils.CURRENT_FILTER).contains(unitData.getCompanyName()) && !filteredList.contains(unitData))
                    if(listOfSelectedFilterItems.contains(unitData.getCompanyName()) && !filteredList.contains(unitData))
                        filteredList.add(unitData);
                }
                adapter = new ProjectsListAdapter(filteredList);
                recyclerView.setAdapter(adapter);
                behaviorFilter.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        binding.clearFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter = new ProjectsListAdapter(mDataList);
                recyclerView.setAdapter(adapter);
                mAdapterFilter = new ItemAdapter(context, listOfFilterItems, listener, Utils.FILTER);
                recyclerViewFilter.setAdapter(mAdapterFilter);
                behaviorFilter.setState(BottomSheetBehavior.STATE_COLLAPSED);
                listOfSelectedFilterItems.clear();
            }
        });
    }

    @Override
    public void onItemClick(String item) {
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        switch(item){
            case "A-Z":
                /*Collections.sort(mDataList, new Comparator<ProjectData>() {
                    @Override
                    public int compare(ProjectData lhs, ProjectData rhs) {
                        return lhs.getTitle().compareToIgnoreCase(rhs.getTitle());
                    }
                });*/
                Collections.sort(mDataList, new Comparator<ProjectData>() {

                    public int compare(ProjectData o1, ProjectData o2) {
                        Long xx1 = ((ProjectData) o1).getTimeCreated();
                        Long xx2 = ((ProjectData) o2).getTimeCreated();
                        return xx1.compareTo(xx2);
                    }});
                Collections.sort(mDataList, new Comparator<ProjectData>() {

                    public int compare(ProjectData o1, ProjectData o2) {
                        if(o1.getTimeCreated().equals(o2.getTimeCreated()) ){
                            String xx1 =  o1.getTitle();
                            String xx2 =  o2.getTitle();
                            return xx1.compareTo(xx2);
                        }
                        return 0;
                    }});

                break;
            case "Z-A":
                Collections.sort(mDataList, new Comparator<ProjectData>() {

                    public int compare(ProjectData o1, ProjectData o2) {
                        Long xx1 = ((ProjectData) o1).getTimeCreated();
                        Long xx2 = ((ProjectData) o2).getTimeCreated();
                        return -xx1.compareTo(xx2);
                    }});
                Collections.sort(mDataList, new Comparator<ProjectData>() {

                    public int compare(ProjectData o1, ProjectData o2) {
                        if(o1.getTimeCreated().equals(o2.getTimeCreated()) ){
                            String xx1 =  o1.getTitle();
                            String xx2 =  o2.getTitle();
                            return -xx1.compareTo(xx2);
                        }
                        return 0;
                    }});


                break;
            /*case "Cost -- Low to High":
                break;
            case "Cost -- High to Low":
                break;*/

        }
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onFilterItemClick(List<String> items) {
        listOfSelectedFilterItems = items;
    }

    @Override
    public void onBackPressed() {

        if(behaviorFilter.getState()==BottomSheetBehavior.STATE_EXPANDED) {
            behaviorFilter.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
        else super.onBackPressed();
    }
}
