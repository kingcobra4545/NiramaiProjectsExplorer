package com.sandeep.prajwal.niramaiprojectsexplorer.model;

/**
 * Created by KingCobra on 27/09/19.
 */

public class ProjectData  {



    String shortDesc;
    String longDesc;
    String title;
    Long timeCreated;

    public ProjectData(String title, String shortDesc, String longDesc,  Long timeCreated, String companyName){
        this.shortDesc = shortDesc;
        this.longDesc = longDesc;
        this.title = title;
        this.timeCreated = timeCreated;
        this.companyName = companyName;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    int position;

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getLongDesc() {
        return longDesc;
    }

    public void setLongDesc(String longDesc) {
        this.longDesc = longDesc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(Long timeCreated) {
        this.timeCreated = timeCreated;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    String companyName;
}
