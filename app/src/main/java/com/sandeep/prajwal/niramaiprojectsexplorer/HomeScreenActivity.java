package com.sandeep.prajwal.niramaiprojectsexplorer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.sandeep.prajwal.niramaiprojectsexplorer.adapter.ProjectsListAdapter;
import com.sandeep.prajwal.niramaiprojectsexplorer.databinding.ActivityHomeScreenBinding;
import com.sandeep.prajwal.niramaiprojectsexplorer.model.ProjectData;

public class HomeScreenActivity extends AppCompatActivity {
    ActivityHomeScreenBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ProjectData[] myListData = new ProjectData[] {
                new ProjectData("Project1","Short Description1",
                        "Long Description1", "6.30pm "),
                new ProjectData("Project2","Short Description2",
                        "Long Description2", "9.30pm ")
        };

        RecyclerView recyclerView = (RecyclerView) binding.content.list;
        ProjectsListAdapter adapter = new ProjectsListAdapter(myListData);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager ll = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(ll);
        recyclerView.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                ll.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);





    }

}
