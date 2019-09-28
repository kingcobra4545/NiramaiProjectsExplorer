package com.sandeep.prajwal.niramaiprojectsexplorer;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.gson.Gson;
import com.sandeep.prajwal.niramaiprojectsexplorer.databinding.ActivityProjectDetailsBinding;
import com.sandeep.prajwal.niramaiprojectsexplorer.model.ProjectData;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ProjectDetailsActivity extends AppCompatActivity {
    ActivityProjectDetailsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_project_details);
        String json = PreferencesDB.getString(this, Utils.CURRENT_PROJECT_DETAILS);
        Gson gson = new Gson();
        ProjectData user= gson.fromJson(json, ProjectData.class);
        Toolbar toolbar = (Toolbar) binding.toolbar;
        toolbar.setTitle(user.getTitle()+" here");
        setSupportActionBar(toolbar);

        toolbar.setTitle(user.getTitle());
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

        binding.contentMain.shortDescField.setText(user.getShortDesc());
        binding.contentMain.longDescField.setText(user.getLongDesc());

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        binding.contentMain.createdTimeDate.setText(format.format(new Date(user.getTimeCreated())));


    }

}
