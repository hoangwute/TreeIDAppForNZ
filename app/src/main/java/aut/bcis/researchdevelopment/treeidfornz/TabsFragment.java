package aut.bcis.researchdevelopment.treeidfornz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import aut.bcis.researchdevelopment.adapter.FilterAdapter;
import aut.bcis.researchdevelopment.database.DBContract;
import aut.bcis.researchdevelopment.database.DBInitialization;

import static aut.bcis.researchdevelopment.treeidfornz.MainActivity.database;
import static aut.bcis.researchdevelopment.treeidfornz.MainActivity.filterEntriesList;
import static aut.bcis.researchdevelopment.treeidfornz.MainActivity.filterEntryAdapter;
import static aut.bcis.researchdevelopment.treeidfornz.MainActivity.generateFilterEntryList;
import static aut.bcis.researchdevelopment.treeidfornz.MainActivity.markerList;
import static aut.bcis.researchdevelopment.treeidfornz.MainActivity.spinnerCollection;


public class TabsFragment extends Fragment implements OnMapReadyCallback {

    public static TabHost tabHost;
    public static int currentTab = 0;
    public static boolean backed = false;
    private ViewStub stub2, stub3, stub4;
    public TabsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_tabs, container, false);
        tabHost = (TabHost) rootView.findViewById(R.id.tabHost);
        tabHost.setup();
        TabHost.TabSpec tab1 = tabHost.newTabSpec("t1");
        tab1.setIndicator("Home");
        tab1.setContent(R.id.tab1);
        TabHost.TabSpec tab2 = tabHost.newTabSpec("t2");
        tab2.setIndicator("Wizard");
        tab2.setContent(R.id.tab2);
        TabHost.TabSpec tab3 = tabHost.newTabSpec("t3");
        tab3.setIndicator("List");
        tab3.setContent(R.id.tab3);
        TabHost.TabSpec tab4 = tabHost.newTabSpec("t4");
        tab4.setIndicator("Map");
        tab4.setContent(R.id.tab4);
        //-----------------------------------------------------------------------------
        //inflate view to each view stub
        stub2 = (ViewStub) rootView.findViewById(R.id.tab2_stub);
        stub3 = (ViewStub) rootView.findViewById(R.id.tab3_stub);
        stub4 = (ViewStub) rootView.findViewById(R.id.tab4_stub);
        if(getActivity().getLocalClassName().equals("MainActivity")) {
            stub2.setLayoutResource(R.layout.wizard_layout);
            stub3.setLayoutResource(R.layout.list_search_layout);
            stub4.setLayoutResource(R.layout.treemap_layout);
        }
        else { //empty tab layout if current activity is not main activity
            stub2.setLayoutResource(R.layout.empty_layout);
            stub3.setLayoutResource(R.layout.empty_layout);
            stub4.setLayoutResource(R.layout.empty_layout);
        }
        stub2.inflate();
        stub3.inflate();
        stub4.inflate();
        tabHost.addTab(tab1);
        tabHost.addTab(tab2);
        tabHost.addTab(tab3);
        tabHost.addTab(tab4);
        tabHost.setCurrentTab(currentTab - 1);
        addTabEvents();
        //-------------------------- google map loading ------------------------------------------------------------
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if(getActivity().getLocalClassName().equals("MainActivity")) //other activities does not need to load the map, avoid crashing the app
            mapFragment.getMapAsync(this);
//        markerList = new ArrayList<Marker>();
        Toast.makeText(getActivity(), "ON CREATE FRAGMENT", Toast.LENGTH_SHORT).show();
        return rootView;
    }

    private void addTabEvents() {
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if(!getActivity().getLocalClassName().equals("MainActivity")) {
                    backed = true;
                    getActivity().finish();
                }
                tabHost.clearFocus();
                if(tabId.equalsIgnoreCase("t1")) {
                    MainActivity.searchView.setVisibility(View.INVISIBLE);
                    currentTab = 1;
                }
                if(tabId.equalsIgnoreCase("t2")) {
                    MainActivity.searchView.setVisibility(View.INVISIBLE);
                    MainActivity.dynamicQuery = "SELECT * FROM " + DBContract.TABLE_TREE + " WHERE";
                    currentTab = 2;
                }
                if(tabId.equalsIgnoreCase("t3")) {
                    MainActivity.searchView.setVisibility(View.VISIBLE);
                    currentTab = 3;
                }
                if(tabId.equalsIgnoreCase("t4")) {
                    Toast.makeText(getActivity(), "JUMP HERE", Toast.LENGTH_SHORT).show();
//                    markerList = new ArrayList<Marker>();
                    currentTab = 4;
//                    MainActivity.searchView.setVisibility(View.INVISIBLE);
                    if(getActivity().getLocalClassName().equals("MainActivity")) {
//                        markerList.clear();
                        displayMarkers();
//                        for(int i = 0; i < markerList.size(); i++) {
//                            Bundle bundle = (Bundle) markerList.get(i).getTag();
//                            String commonName = bundle.getString("Tree common name");
//                            System.out.println("Marker list names: " + commonName);
//                        }
                        generateFilterEntryList(getActivity());
                    }
                    if(MainActivity.mapLoaded == false) {
                        MainActivity.loadGoogleMapDialog = new ProgressDialog(getActivity());
                        MainActivity.loadGoogleMapDialog.setTitle("Announcement");
                        MainActivity.loadGoogleMapDialog.setMessage("Loading map...");
                        MainActivity.loadGoogleMapDialog.setCanceledOnTouchOutside(false);
                        MainActivity.loadGoogleMapDialog.show();
                    }
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MainActivity.mMap = googleMap;
        MainActivity.mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                MainActivity.loadGoogleMapDialog.dismiss();
                MainActivity.mapLoaded = true;
            }
        });
        LatLng newZealand = new LatLng(-41.270020, 173.891755);
        displayMarkers();
//        mMap.addMarker(new MarkerOptions().position(newZealand).title("TreeMarker in New Zealand"));
        MainActivity.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newZealand, 5));
        MainActivity.mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Intent intent = new Intent(getActivity(), MarkerInformationActivity.class);
                intent.putExtra("Bundle", (Bundle) marker.getTag());
                startActivity(intent);
                return false;
            }
        });
    }
    public void displayMarkers() {
        database = getActivity().openOrCreateDatabase(DBInitialization.DATABASE_NAME, getActivity().MODE_PRIVATE, null);
        Cursor cursor = database.rawQuery("SELECT * FROM " + DBContract.TABLE_MARKER, null);
        while(cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(DBContract.COLUMN_MARKER_ID));
            String commonName = cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_MARKER_COMMON_NAME));
            String latinName = cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_MARKER_LATIN_NAME));
            String location = cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_MARKER_LOCATION));
            String image = cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_MARKER_IMAGE));
            String note = cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_MARKER_NOTE));
            double latitude = cursor.getDouble(cursor.getColumnIndex(DBContract.COLUMN_MARKER_LATITUDE));
            double longitude = cursor.getDouble(cursor.getColumnIndex(DBContract.COLUMN_MARKER_LONGITUDE));
            Marker newMarker = MainActivity.mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_tree)));
            Bundle bundle = new Bundle();
            bundle.putString("Tree common name", commonName);
            bundle.putString("Tree place", location);
            bundle.putString("Tree latin name", latinName);
            bundle.putString("Tree note", note);
            bundle.putString("Tree image", image);
            bundle.putInt("Tree id", id);
            newMarker.setTag(bundle);
            markerList.add(newMarker);
//            Toast.makeText(getActivity(), "Marker list size: " + String.valueOf(markerList.size()), Toast.LENGTH_SHORT).show();
            if(Utility.checkFilterStatus(commonName) == 0) {
                newMarker.setVisible(false);
            }
            else if(Utility.checkFilterStatus(commonName) == 1) {
                newMarker.setVisible(true);
            }
        }
        cursor.close();
    }

}

