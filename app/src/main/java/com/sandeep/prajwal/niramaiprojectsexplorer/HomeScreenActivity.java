package com.sandeep.prajwal.niramaiprojectsexplorer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sandeep.prajwal.niramaiprojectsexplorer.adapter.ItemAdapter;
import com.sandeep.prajwal.niramaiprojectsexplorer.adapter.ProjectsListAdapter;
import com.sandeep.prajwal.niramaiprojectsexplorer.databinding.ActivityHomeScreenBinding;
import com.sandeep.prajwal.niramaiprojectsexplorer.model.ProjectData;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomeScreenActivity extends AppCompatActivity implements ItemAdapter.ItemListener,
        ProjectsListAdapter.OnItemClickListener {
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
    ProjectsListAdapter.OnItemClickListener  onItemlistener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_home_screen);
        context = this;
        listOfSelectedFilterItems = new ArrayList<>();
        onItemlistener = this;
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
        mockData();

        Gson gson = new Gson();

        if(PreferencesDB.getString(this, Utils.FULL_DATA).equals(""))
            PreferencesDB.putString(this, Utils.FULL_DATA, gson.toJson(mDataList));

        if(!PreferencesDB.getString(context, Utils.FULL_DATA).equals("")){
            mDataList.clear();
            Type listType = new TypeToken<List<ProjectData>>(){}.getType();
            mDataList = gson.fromJson(PreferencesDB.getString(context, Utils.FULL_DATA), listType);
        }


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
        adapter = new ProjectsListAdapter(mDataList, onItemlistener);
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
        binding.contentMain.searchView.clearFocus();

        final List<ProjectData> filteredList = new ArrayList<>();
        binding.applyFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filteredList.clear();
                Log.i(Utils.PRAJWAL, "listOfSelectedFilterItems.size()-->  " + listOfSelectedFilterItems.size());
                if(listOfSelectedFilterItems.size()==0) {
                    adapter = new ProjectsListAdapter(mDataList, onItemlistener);
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
                adapter = new ProjectsListAdapter(filteredList, onItemlistener);
                recyclerView.setAdapter(adapter);
                behaviorFilter.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        binding.clearFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter = new ProjectsListAdapter(mDataList, onItemlistener);
                recyclerView.setAdapter(adapter);
                mAdapterFilter = new ItemAdapter(context, listOfFilterItems, listener, Utils.FILTER);
                recyclerViewFilter.setAdapter(mAdapterFilter);
                behaviorFilter.setState(BottomSheetBehavior.STATE_COLLAPSED);
                listOfSelectedFilterItems.clear();
            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();
        if(!PreferencesDB.getString(context, Utils.FULL_DATA).equals("")){
            mDataList.clear();
            Gson gson = new Gson();
            Type listType = new TypeToken<List<ProjectData>>(){}.getType();
            mDataList = gson.fromJson(PreferencesDB.getString(context, Utils.FULL_DATA), listType);
            adapter = new ProjectsListAdapter(mDataList, onItemlistener);
            recyclerView.setAdapter(adapter);
        }
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

    @Override
    public void onItemClick(ProjectData item) {
        Intent newIntent = new Intent(HomeScreenActivity.this, ProjectDetailsActivity.class);
        Gson gson = new Gson();
        String json = gson.toJson(item);
        PreferencesDB.putString(context,Utils.CURRENT_PROJECT_DETAILS, json);
        context.startActivity(newIntent);
    }
    public static void hideKeyboard(Activity activity, View view) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.

        //If no view currently has focus, create a new one, just so we can grab a window token from it

        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    private void mockData() {
        mDataList.add(new ProjectData("Millimeter Wave Mobile Communications for 5G Cellular: It Will Work!","2016 IST-Africa Week Conference",
                "Helper class hosting utilities used to make information from split apks available to the rest of the app after the split is installed." +
                        "Helper class hosting utilities used to make information from split apks available to the rest of the app after the split is installed" +
                        "Helper class hosting utilities used to make information from split apks available to the rest of the app after the split is installed" +
                        "Helper class hosting utilities used to make information from split apks available to the rest of the app after the split is installed\n\n\n" +
                        "Helper class hosting utilities used to make information from split apks available to the rest of the app after the split is installed" +
                        "Helper class hosting utilities used to make information from split apks available to the rest of the app after the split is installed" +
                        "Helper class hosting utilities used to make information from split apks available to the rest of the app after the split is installed\n\n" +
                        "Helper class hosting utilities used to make information from split apks available to the rest of the app after the split is installed" +
                        "Helper class hosting utilities used to make information from split apks available to the rest of the app after the split is installed\n\n" +
                        "Helper class hosting utilities used to make information from split apks available to the rest of the app after the split is installed" +
                        "", 1569617330000l, "Cisco"));
        mDataList.add(new ProjectData("Internet of Things for Smart Cities","2016 1st International Workshop on Science of Smart City ",
                "The best answer, as ha1ogen says, is to make a custom drawable. Start with the 9-patch that is used for normal EditText fields. Modify it to strip out the underbar and other graphics you don't want. With this, your modified EditText will have the same margins and overall appearance as normal EditText fields. If you simply set the background to null, it will lose the margins." +
                        " Helper class hosting utilities used to make information from split apks available to the rest of the app after the split is installed." +
                        "Helper class hosting utilities used to make information from split apks available to the rest of the app after the split is installed.\n\n" +
                        "Helper class hosting utilities used to make information from split apks available to the rest of the app after the split is installed.", 1552677130000l, "NIRAMAI"));
        mDataList.add(new ProjectData("CTRL: Leaking Data from Via Keyboard LEDs","Xueping Liang ; Sachin Shetty ; Deepak Tosh",
                "A class to enable automated accessibility checks in Espresso tests. These checks will run as a global ViewAssertion, and cover a variety of accessibility issues (see LATEST to see which checks are run).", 1569677430000l, "ACCENTURE"));
        mDataList.add(new ProjectData("Original Symbol Phase Rotated Secure Transmission","2018 International Conference on Communication, Computing and Internet of Things (IC3IoT)",
                "SplitCompat enables access to splits installed through SplitInstallManager before application restart.\n" +
                        "SplitCompat enables access to splits installed through SplitInstallManager before application restart.\n" +
                        "SplitCompat enables access to splits installed through SplitInstallManager before application restart.\n" +
                        "SplitCompat enables access to splits installed through SplitInstallManager before application restart.\n" +
                        "SplitCompat enables access to splits installed through SplitInstallManager before application restart.\n" +
                        "SplitCompat enables access to splits installed through SplitInstallManager before application restart.\n" +
                        "SplitCompat enables access to splits installed through SplitInstallManager before application restart.\n" +
                        "\n", 1503677130000l, "SAP"));
        mDataList.add(new ProjectData("Internet of Things: A Survey on Enabling Technologies, Protocols, and Applications","Bengt Ahlgren ; Markus Hidell ; Edith C.-H. Ngai",
                "A convenience Application that prevents an incorrectly sideloaded app from crashing because of missing splits. There are the following possibilities of using this class:\n" +
                        "A convenience Application that prevents an incorrectly sideloaded app from crashing because of missing splits. There are the following possibilities of using this class:\n" +
                        "A convenience Application that prevents an incorrectly sideloaded app from crashing because of missing splits. There are the following possibilities of using this class:" +
                        "A convenience Application that prevents an incorrectly sideloaded app from crashing because of missing splits. There are the following possibilities of using this class:" +
                        "A convenience Application that prevents an incorrectly sideloaded app from crashing because of missing splits. There are the following possibilities of using this class:" +
                        "A convenience Application that prevents an incorrectly sideloaded app from crashing because of missing splits. There are the following possibilities of using this class:" +
                        "A convenience Application that prevents an incorrectly sideloaded app from crashing because of missing splits. There are the following possibilities of using this class:" +
                        "A convenience Application that prevents an incorrectly sideloaded app from crashing because of missing splits. There are the following possibilities of using this class:" +
                        "\n" +
                        "Declare this class as the application in your AndroidManifest.xml.\n" +
                        "Have your Application extend this class. Optionally override onCreateCustom() to define custom onCreate behavior.", 1521673430000l, "SAP"));
        mDataList.add(new ProjectData("Application of Big Data and Machine Learning in Smart Grid, and Associated Security Concerns: A Review","Year: 2016 | Volume: 20, Issue: 6 | Magazine Article | Publisher: IEEE",
                "This paper conducts a comprehensive study on the application of big data and machine learning in the electrical power grid introduced through the emergence of the next-generation power system-the smart grid (SG). Connectivity lies at the core of this new grid infrastructure, which is provided by the Internet of Things (IoT). This connectivity, and constant communication required in this system, also introduced a massive data volume that demands techniques far superior to conventional methods for proper analysis and decision-making. The IoT-integrated SG system can provide efficient load forecasting and data acquisition technique along with cost-effectiveness. Big data analysis and machine learning techniques are essential to reaping these benefits. In the complex connected system of SG, cyber security becomes a critical issue; IoT devices and their data turning into major targets of attacks. Such security concerns and their solutions are also included in this paper. Key information obtained through literature review is tabulated in the corresponding sections to provide a clear synopsis; and the findings of this rigorous review are listed to give a concise picture of this area of study and promising future fields of academic and industrial research, with current limitations with viable solutions along with their effectiveness.", 1569677130000l, "IBM"));
        mDataList.add(new ProjectData("Blockchains and Smart Contracts for the Internet of Things","2018 IEEE International Smart Cities Conference (ISC2)",
                "When you are done with this object, don't forget to call endConnection() to ensure proper cleanup. This object holds a binding to the in-app billing service and the manager to handle broadcast events, which will leak unless you dispose it correctly. If you created the object inside the onCreate(Bundle) method, then the recommended place to dispose is the onDestroy() method." +
                        "When you are done with this object, don't forget to call endConnection() to ensure proper cleanup. This object holds a binding to the in-app billing service and the manager to handle broadcast events, which will leak unless you dispose it correctly. If you created the object inside the onCreate(Bundle) method, then the recommended place to dispose is the onDestroy() method.\n\n" +
                        " When you are done with this object, don't forget to call endConnection() to ensure proper cleanup. This object holds a binding to the in-app billing service and the manager to handle broadcast events, which will leak unless you dispose it correctly. If you created the object inside the onCreate(Bundle) method, then the recommended place to dispose is the onDestroy() method.\n" +
                        "\n" +
                        "To get library logs inside Android logcat, set corresponding logging level. E.g.: adb shell setprop log.tag.BillingClient VERBOSE", 1529677430000l, "ACCENTURE"));

        mDataList.add(new ProjectData("CTRL-ALT-LED: Leaking Data from Air-Gapped Computers Via Keyboard LEDs","2017 14th International Computer Conference on Wavelet Active Media Technology and Information Processing (ICCWAMTIP)",
                "A class to enable automated accessibility checks in Espresso tests. These checks will run as a global ViewAssertion, and cover a variety of accessibility issues (see LATEST to see which checks are run).", 1545377430000l, "JIO INFOCOMM"));
        mDataList.add(new ProjectData("Decentralizing Privacy: Using Blockchain to Protect Personal Data","2018 IEEE International Smart Cities Conference (ISC2)",
                "A class to enable automated accessibility checks in Espresso tests. These checks will run as a global ViewAssertion, and cover a variety of accessibility issues (see LATEST to see which checks are run).", 1545677430000l, "ACCENTURE"));
        mDataList.add(new ProjectData("A Survey of Data Mining and Machine Learning Methods for Cyber Security Intrusion Detection","2016 IEEE 3rd World Forum on Internet of Things (WF-IoT)",
                "A class to enable automated accessibility checks in Espresso tests. These checks will run as a global ViewAssertion, and cover a variety of accessibility issues (see LATEST to see which checks are run).", 1512477430000l, "RELIANCE"));
        mDataList.add(new ProjectData("A Survey on 5G Networks for the Internet of Things: Communication Technologies and Challenges","K E Skouby ; P Lynggaard",
                "After instantiating, you must perform setup in order to start using the object. To perform setup, call the startConnection(BillingClientStateListener) method and provide a listener; that listener will be notified when setup is complete, after which (and not before) you may start calling other methods. After setup is complete, you will typically want to request an inventory of owned items and subscriptions. See queryPurchases(String) and querySkuDetailsAsync(SkuDetailsParams, SkuDetailsResponseListener)." +
                        "After instantiating, you must perform setup in order to start using the object. To perform setup, call the startConnection(BillingClientStateListener) method and provide a listener; that listener will be notified when setup is complete, after which (and not before) you may start calling other methods. After setup is complete, you will typically want to request an inventory of owned items and subscriptions. See queryPurchases(String) and querySkuDetailsAsync(SkuDetailsParams, SkuDetailsResponseListener)." +
                        "After instantiating, you must perform setup in order to start using the object. To perform setup, call the startConnection(BillingClientStateListener) method and provide a listener; that listener will be notified when setup is complete, after which (and not before) you may start calling other methods. After setup is complete, you will typically want to request an inventory of owned items and subscriptions. See queryPurchases(String) and querySkuDetailsAsync(SkuDetailsParams, SkuDetailsResponseListener).\n\n" +
                        "After instantiating, you must perform setup in order to start using the object. To perform setup, call the startConnection(BillingClientStateListener) method and provide a listener; that listener will be notified when setup is complete, after which (and not before) you may start calling other methods. After setup is complete, you will typically want to request an inventory of owned items and subscriptions. See queryPurchases(String) and querySkuDetailsAsync(SkuDetailsParams, SkuDetailsResponseListener)." +
                        "After instantiating, you must perform setup in order to start using the object. To perform setup, call the startConnection(BillingClientStateListener) method and provide a listener; that listener will be notified when setup is complete, after which (and not before) you may start calling other methods. After setup is complete, you will typically want to request an inventory of owned items and subscriptions. See queryPurchases(String) and querySkuDetailsAsync(SkuDetailsParams, SkuDetailsResponseListener)." +
                        "After instantiating, you must perform setup in order to start using the object. To perform setup, call the startConnection(BillingClientStateListener) method and provide a listener; that listener will be notified when setup is complete, after which (and not before) you may start calling other methods. After setup is complete, you will typically want to request an inventory of owned items and subscriptions. See queryPurchases(String) and querySkuDetailsAsync(SkuDetailsParams, SkuDetailsResponseListener)." +
                        "", 1534577430000l, "ACCENTURE"));
        mDataList.add(new ProjectData("Security for 5G Mobile Wireless Networks","2019 IEEE 5th World Forum on Internet of Things (WF-IoT)",
                "The best answer, as ha1ogen says, is to make a custom drawable. Start with the 9-patch that is used for normal EditText fields. Modify it to strip out the underbar and other graphics you don't want. With this, your modified EditText will have the same margins and overall appearance as normal EditText fields. If you simply set the background to null, it will lose the margins.\n" +
                        "Voluminous amounts of data have been produced, since the past decade as the miniaturization of Internet of things (IoT) devices increases. However, such data are not useful without analytic power. Numerous big data, IoT, and analytics solutions have enabled people to obtain valuable insight into large data generated by IoT devices. However, these solutions are still in their infancy, and the domain lacks a comprehensive survey. This paper investigates the state-of-the-art research efforts directed toward big IoT data analytics. The relationship between big data analytics and IoT is explained. Moreover, this paper adds value by proposing a new architecture for big IoT data analytics. Furthermore, big IoT data analytic types, methods, and technologies for big data mining are discussed. Numerous notable use cases are also presented. Several opportunities brought by data analytics in IoT paradigm are then discussed. Finally, open research challenges, such as privacy, big data mining, visualization, and integration, are presented as future research directions.", 1569677430000l, "ACCENTURE"));
        mDataList.add(new ProjectData("Big IoT Data Analytics: Architecture, Opportunities, and Open Research Challenges","Modify it to strip out the underbar | Magazine Article | Publisher: IEEE",
                "The advanced features of 5G mobile wireless network systems yield new security requirements and challenges. This paper presents a comprehensive study on the security of 5G wireless network systems compared with the traditional cellular networks. The paper starts with a review on 5G wireless networks particularities as well as on the new requirements and motivations of 5G wireless security. The potential attacks and security services are summarized with the consideration of new service requirements and new use cases in 5G wireless networks. The recent development and the existing schemes for the 5G wireless security are presented based on the corresponding security services, including authentication, availability, data confidentiality, key management, and privacy. This paper further discusses the new security features involving different technologies applied to 5G, such as heterogeneous networks, device-to-device communications, massive multiple-input multiple-output, software-defined networks, and Internet of Things. Motivated by these security research and development activities, we propose a new 5G wireless security architecture, based on which the analysis of identity management and flexible authentication is provided. As a case study, we explore a handover procedure as well as a signaling load scheme to show the advantages of the proposed security architecture. The challenges and future directions of 5G wireless security are finally summarized.", 1569677430000l, "ACCENTURE"));
        mDataList.add(new ProjectData("CTRL-HALT-LCD: Leaking Info from Hair-Gapped Machines Via Keyboard LCDs","2018 IEEE International Smart Cities Conference (ISC2)",
                "A class to enable automated accessibility checks in Espresso tests. These checks will run as a global ViewAssertion, and cover a variety of accessibility issues (see LATEST to see which checks are run).", 1538577430000l, "ELEMENTORA"));
        mDataList.add(new ProjectData("A Survey of 5G Network: Architecture and Emerging Technologies","Year: 2018 | Conference Paper | Publisher: IEEE",
                "Main interface for communication between the library and user application code.\n" +
                        "\n" +
                        "It provides convenience methods for in-app billing. You can create one instance of this class for your application and use it to process in-app billing operations. It provides synchronous (blocking) and asynchronous (non-blocking) methods for many common in-app billing operations.\n" +
                        "\n" +
                        "All methods are supposed to be called from the Ui thread and all the asynchronous callbacks will be returned on the Ui thread as well." +
                        "All methods are supposed to be called from the Ui thread and all the asynchronous callbacks will be returned on the Ui thread as well" +
                        "All methods are supposed to be called from the Ui thread and all the asynchronous callbacks will be returned on the Ui thread as well\n\n" +
                        "All methods are supposed to be called from the Ui thread and all the asynchronous callbacks will be returned on the Ui thread as well" +
                        "All methods are supposed to be called from the Ui thread and all the asynchronous callbacks will be returned on the Ui thread as well" +
                        "All methods are supposed to be called from the Ui thread and all the asynchronous callbacks will be returned on the Ui thread as well", 1519677430000l, "NIRAMAI"));
        mDataList.add(new ProjectData("SegNet: A Deep Convolutional Encoder-Decoder Architecture for Image Segmentation","2018 IEEE International Conference on Smart Internet of Things (SmartIoT)",
                "After instantiating, you must perform setup in order to start using the object. To perform setup, call the startConnection(BillingClientStateListener) method and provide a listener; that listener will be notified when setup is complete, after which (and not before) you may start calling other methods. After setup is complete, you will typically want to request an inventory of owned items and subscriptions. See queryPurchases(String) and querySkuDetailsAsync(SkuDetailsParams, SkuDetailsResponseListener)." +
                        "After instantiating, you must perform setup in order to start using the object. To perform setup, call the startConnection(BillingClientStateListener) method and provide a listener; that listener will be notified when setup is complete, after which (and not before) you may start calling other methods. After setup is complete, you will typically want to request an inventory of owned items and subscriptions. See queryPurchases(String) and querySkuDetailsAsync(SkuDetailsParams, SkuDetailsResponseListener)." +
                        "After instantiating, you must perform setup in order to start using the object. To perform setup, call the startConnection(BillingClientStateListener) method and provide a listener; that listener will be notified when setup is complete, after which (and not before) you may start calling other methods. After setup is complete, you will typically want to request an inventory of owned items and subscriptions. See queryPurchases(String) and querySkuDetailsAsync(SkuDetailsParams, SkuDetailsResponseListener).\n\n" +
                        "After instantiating, you must perform setup in order to start using the object. To perform setup, call the startConnection(BillingClientStateListener) method and provide a listener; that listener will be notified when setup is complete, after which (and not before) you may start calling other methods. After setup is complete, you will typically want to request an inventory of owned items and subscriptions. See queryPurchases(String) and querySkuDetailsAsync(SkuDetailsParams, SkuDetailsResponseListener)." +
                        "After instantiating, you must perform setup in order to start using the object. To perform setup, call the startConnection(BillingClientStateListener) method and provide a listener; that listener will be notified when setup is complete, after which (and not before) you may start calling other methods. After setup is complete, you will typically want to request an inventory of owned items and subscriptions. See queryPurchases(String) and querySkuDetailsAsync(SkuDetailsParams, SkuDetailsResponseListener)." +
                        "After instantiating, you must perform setup in order to start using the object. To perform setup, call the startConnection(BillingClientStateListener) method and provide a listener; that listener will be notified when setup is complete, after which (and not before) you may start calling other methods. After setup is complete, you will typically want to request an inventory of owned items and subscriptions. See queryPurchases(String) and querySkuDetailsAsync(SkuDetailsParams, SkuDetailsResponseListener)." +
                        "", 1564577430000l, "NIRAMAI"));
    }
}
