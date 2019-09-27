package com.sandeep.prajwal.niramaiprojectsexplorer;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;

import com.sandeep.prajwal.niramaiprojectsexplorer.adapter.ProjectsListAdapter;
import com.sandeep.prajwal.niramaiprojectsexplorer.databinding.ActivityHomeScreenBinding;
import com.sandeep.prajwal.niramaiprojectsexplorer.model.ProjectData;

import java.util.ArrayList;
import java.util.List;

public class HomeScreenActivity extends AppCompatActivity {
    ActivityHomeScreenBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_home_screen);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home_screen);

        /*ProjectData[] myListData = new ProjectData[] {
                new ProjectData("Project1","Short Description1",
                        "Long Description1", "6.30pm "),
                new ProjectData("Project2","Short Description2",
                        "Long Description2", "9.30pm ")
        };*/
        List<ProjectData> mDataList = new ArrayList<>();
        mDataList.add(new ProjectData("Project1","Short Description1",
                "Long Description1", "6.30pm "));
        mDataList.add(new ProjectData("Project2","Short Description2",
                "Long Description2", "9.30pm "));


        RecyclerView recyclerView = (RecyclerView) binding.contentMain.list;
        final ProjectsListAdapter adapter = new ProjectsListAdapter(mDataList);
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
    }



}
