package com.sandeep.prajwal.niramaiprojectsexplorer;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sandeep.prajwal.niramaiprojectsexplorer.databinding.ActivityProjectDetailsBinding;
import com.sandeep.prajwal.niramaiprojectsexplorer.model.ProjectData;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ProjectDetailsActivity extends AppCompatActivity {
    ActivityProjectDetailsBinding binding;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details);
        context = this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_project_details);
        String json = PreferencesDB.getString(this, Utils.CURRENT_PROJECT_DETAILS);
        final Gson gson = new Gson();
        final ProjectData user = gson.fromJson(json, ProjectData.class);
        Toolbar toolbar = (Toolbar) binding.toolbar;
        toolbar.setTitle(user.getTitle()+" here");
        setSupportActionBar(toolbar);

        toolbar.setTitle(user.getTitle());
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

        binding.contentMain.shortDescField.setText(user.getShortDesc());
        binding.contentMain.longDescField.setText(user.getLongDesc());

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        binding.contentMain.createdTimeDate.setText(format.format(new Date(user.getTimeCreated())));

        binding.contentMain.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newShortDesc = binding.contentMain.shortDescField.getText().toString();
                String newLongDesc = binding.contentMain.longDescField.getText().toString();
                Long newTime = System.currentTimeMillis();
                Gson gson1 = new Gson();
                Type listType = new TypeToken<List<ProjectData>>(){}.getType();
                List<ProjectData> mDataList = gson1.fromJson(PreferencesDB.getString(context, Utils.FULL_DATA), listType);

                for (ProjectData unitData:
                     mDataList) {
                    if(unitData.getTitle().equals(user.getTitle())) {
                        unitData.setShortDesc(newShortDesc);
                        unitData.setLongDesc(newLongDesc);
                        unitData.setTimeCreated(newTime);
                    }
                }
                PreferencesDB.putString(context, Utils.FULL_DATA, gson.toJson(mDataList));
                finish();
            }
        });
    }

}
