package aut.bcis.researchdevelopment.treeidfornz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;

import aut.bcis.researchdevelopment.adapter.TreeAdapter;
import aut.bcis.researchdevelopment.database.DBContract;
import aut.bcis.researchdevelopment.database.DBInitialization;
import aut.bcis.researchdevelopment.model.Tree;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    //-------------------------Constants-----------------------------------------------------
    public static final int START_CHAR_POSITION_TO_DELETE = 24;
    public static final int END_CHAR_POSITION_TO_DELETE = 28;
    
    //-------------------------Main Activity-------------------------------------------------
    private TabHost tabHost;
    private SearchView searchView;
    public static SQLiteDatabase database = null;
    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    private ProgressDialog loadDatabaseDialog;
    //-------------------------List/Search Function------------------------------------------
    private ListView lvTreeList;
    private TreeAdapter treeAdapter;
    private ArrayList<Object> treeList;
    private CheckBox chkFamily, chkGenus;
    private ImageButton btnFavourite;
    public static boolean favouriteSelected = false;
    public static TextView txtAnnounce;
    //--------------------------Wizard Function-----------------------------------------------
    private CheckBox chkToothed, chkSmooth, chkAlternating, chkOpposite, chkHandshaped;;
    private Button btnFinalise;
    private String dynamicQuery = "SELECT * FROM " + DBContract.TABLE_TREE + " WHERE";
    private TextView txtFirstHeader, txtSecondHeader;
    private ArrayList<String> firstHeaderKeys = new ArrayList(), secondHeaderKeys = new ArrayList();
    private TextView txtToothedCount, txtSmoothCount, txtHandShapedCount, txtOppositeCount, txtAlternatingCount;
    //--------------------------Tree Map Function-----------------------------------------------
    protected static GoogleMap mMap;
    private ProgressDialog loadGoogleMapDialog;
    private boolean mapLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DBInitialization.getDatabaseFromAssets(MainActivity.this);
        database = openOrCreateDatabase(DBInitialization.DATABASE_NAME, MODE_PRIVATE, null);
        loadPictureIntoDatabase();
        addControls();
        addEvents();
    }

    private void loadPictureIntoDatabase() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getBoolean("firstTime", false)) {
            // <---- run your one time code here
            loadDatabaseDialog = new ProgressDialog(MainActivity.this);
            loadDatabaseDialog.setTitle("Please be patient");
            loadDatabaseDialog.setMessage("Setting up the database...");
            loadDatabaseDialog.setCanceledOnTouchOutside(false);
            loadDatabaseDialog.show();
            Utility.archivedUpdatePicture(MainActivity.this);
            loadDatabaseDialog.dismiss();
            // mark first time has runned.
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.commit();
        }
    }

    private void addControls() {
        //----------------------tabhost initiation--------------------------------
        tabHost = (TabHost)findViewById(R.id.tabHost);
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
        tabHost.addTab(tab1);
        tabHost.addTab(tab2);
        tabHost.addTab(tab3);
        tabHost.addTab(tab4);
        //-----------------------list view initiation------------------------------
        lvTreeList = (ListView) findViewById(R.id.lvTreeList);
        treeList = new ArrayList<>();
        treeAdapter = new TreeAdapter(MainActivity.this, treeList);
        lvTreeList.setAdapter(treeAdapter);
        //-----------------------other views initiation----------------------------
        btnFavourite = (ImageButton) findViewById(R.id.btnFavourite);
        chkFamily = (CheckBox) findViewById(R.id.chkFamily);
        chkGenus = (CheckBox) findViewById(R.id.chkGenus);
        txtAnnounce = (TextView)findViewById(R.id.txtAnnounce);
        //-----------------------wizard initiation----------------------------
        btnFinalise = (Button) findViewById(R.id.btnFinalise);
        txtFirstHeader = (TextView) findViewById(R.id.txtFirstHeader);
        txtSecondHeader = (TextView) findViewById(R.id.txtSecondHeader);
        chkSmooth = (CheckBox) findViewById(R.id.chkSmooth);
        chkToothed = (CheckBox) findViewById(R.id.chkToothed);
        chkAlternating = (CheckBox) findViewById(R.id.chkAlternating);
        chkHandshaped = (CheckBox) findViewById(R.id.chkHandshaped);
        chkOpposite = (CheckBox) findViewById(R.id.chkOpposite);
//        txtToothedCount = (TextView) findViewById(R.id.txtToothedCount);
//        txtSmoothCount = (TextView)findViewById(R.id.txtSmoothCount);
//        txtHandShapedCount = (TextView) findViewById(R.id.txtHandShapedCount);
//        txtAlternatingCount = (TextView) findViewById(R.id.txtAlternatingCount);
//        txtOppositeCount = (TextView) findViewById(R.id.txtOppositeCount);
        //---------------------------------------------------------------------------------
//        txtSmoothCount.setText(Utility.countTreeTraits("Margin", "smooth"));
//        txtToothedCount.setText(Utility.countTreeTraits("Margin", "toothed"));
//        txtHandShapedCount.setText(Utility.countTreeTraits("Arrangement", "hand-shaped"));
//        txtOppositeCount.setText(Utility.countTreeTraits("Arrangement", "opposite"));
//        txtAlternatingCount.setText(Utility.countTreeTraits("Arrangement", "alternating"));
        //-----------------------tree map initiation----------------------------
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }
    private void addEvents() {
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                tabHost.clearFocus();
                if(tabId.equalsIgnoreCase("t1")) {
                    searchView.setVisibility(View.INVISIBLE);
                }
                if(tabId.equalsIgnoreCase("t2")) {
                    searchView.setVisibility(View.INVISIBLE);
                    dynamicQuery = "SELECT * FROM " + DBContract.TABLE_TREE + " WHERE";
                }
                if(tabId.equalsIgnoreCase("t3")) {
                    searchView.setVisibility(View.VISIBLE);
                    if(favouriteSelected)
                        displayFavouriteTreeBasedOnCheckedBox();
                    else
                        displayTreeBasedOnCheckedBox();
                }
                if(tabId.equalsIgnoreCase("t4")) {
                    searchView.setVisibility(View.INVISIBLE);
                    if(mapLoaded == false) {
                        loadGoogleMapDialog = new ProgressDialog(MainActivity.this);
                        loadGoogleMapDialog.setTitle("Announcement");
                        loadGoogleMapDialog.setMessage("Loading map...");
                        loadGoogleMapDialog.setCanceledOnTouchOutside(false);
                        loadGoogleMapDialog.show();
                    }
                }
            }
        });
        //---------------------------list configuration-----------------------------
        chkGenus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked && chkFamily.isChecked()) { //only 1 button can be selected at once
                    chkFamily.setChecked(false);
                    displayTreesBasedOnState(DBContract.COLUMN_GENUS);
                }
                else if(!isChecked && !chkFamily.isChecked()) {
                    displayTreesBasedOnState(DBContract.COLUMN_COMMON_NAME);
                }
                else
                    displayTreesBasedOnState(DBContract.COLUMN_GENUS);
            }
        });
        chkFamily.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked && chkGenus.isChecked()) { //only 1 button can be selected at once
                    chkGenus.setChecked(false);
                    displayTreesBasedOnState(DBContract.COLUMN_FAMILY);
                }
                else if(!isChecked && !chkGenus.isChecked()) {
                    displayTreesBasedOnState(DBContract.COLUMN_COMMON_NAME);
                }
                else
                    displayTreesBasedOnState(DBContract.COLUMN_FAMILY);
            }
        });
        btnFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtAnnounce.setVisibility(View.INVISIBLE);
                if(favouriteSelected == false) {
                    displayFavouriteTreeBasedOnCheckedBox();
                    favouriteSelected = true;
                    btnFavourite.setBackgroundColor(Color.GRAY);
                }
                else {
                    displayTreeBasedOnCheckedBox();
                    favouriteSelected = false;
                    btnFavourite.setBackgroundColor(Color.WHITE);
                }
            }
        });
        lvTreeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) { /*position cannot be used here as array list is fixed and not changed
                                                                                               according to search filter */
                Intent intent = new Intent(MainActivity.this, TreeDetailActivity.class);
                TextView txtCommonName = (TextView) view.findViewById(R.id.txtCommonName);
                String treeName = txtCommonName.getText().toString();
                intent.putExtra("Tree", treeName);
                startActivity(intent);
            }
        });
        //---------------------------wizard configuration-----------------------------
        chkSmooth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    firstHeaderKeys.add("smooth");
                }
                else {
                    firstHeaderKeys.remove("smooth");
                }
                updateCorrespondingHeader(1);
            }
        });
        chkToothed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    firstHeaderKeys.add("toothed");
                }
                else {
                    firstHeaderKeys.remove("toothed");
                }
                updateCorrespondingHeader(1);
            }
        });
        chkHandshaped.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    secondHeaderKeys.add("hand-shaped");
                }
                else {
                    secondHeaderKeys.remove("hand-shaped");
                }
                updateCorrespondingHeader(2);
            }
        });
        chkAlternating.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    secondHeaderKeys.add("alternating");
                }
                else {
                    secondHeaderKeys.remove("alternating");
                }
                updateCorrespondingHeader(2);
            }
        });
        chkOpposite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    secondHeaderKeys.add("opposite");
                }
                else {
                    secondHeaderKeys.remove("opposite");
                }
                updateCorrespondingHeader(2);
            }
        });
        btnFinalise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(generateDynamicQuery() == true) {
                    Intent intent = new Intent(MainActivity.this, WizardResultActivity.class);
                    intent.putExtra("Query", dynamicQuery);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(MainActivity.this, "Please specify tree traits", Toast.LENGTH_LONG).show();
                }
            }
        });
        //---------------------------tree map configuration-----------------------------


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_find, menu);
        MenuItem menuSearch = menu.findItem(R.id.menuSearch);
        searchView = (SearchView) menuSearch.getActionView();
        searchView.setVisibility(View.INVISIBLE);
        searchView.setQueryHint("Common/Latin name");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                treeAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void displayAllTreesSortedBy(String sortType) {
        treeList.clear();
        treeAdapter = new TreeAdapter(MainActivity.this, treeList); //this line fixes search bug occurred when changes the button
        lvTreeList.setAdapter(treeAdapter);
        database = openOrCreateDatabase(DBInitialization.DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor = database.rawQuery("SELECT * FROM " + DBContract.TABLE_TREE + " ORDER BY " + sortType, null);
        while(cursor.moveToNext()) {
            int Id = cursor.getInt(cursor.getColumnIndex(DBContract.COLUMN_ID));
            String commonName = cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_COMMON_NAME));
            String latinName = cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_LATIN_NAME));
            String family = cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_FAMILY));
            String genus = cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_GENUS));
            int isLiked = cursor.getInt(cursor.getColumnIndex(DBContract.COLUMN_LIKED));
            String picturePath = cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_PICTURE_PATH));
            treeList.add(new Tree(Id, commonName, latinName, family, genus, picturePath, isLiked));
        }
        cursor.close();
        Utility.sortTypeSwitch(sortType, treeList);
        treeAdapter.notifyDataSetChanged();
    }

    private void displayAllFavouriteTreesSortedBy(String sortType) {
        treeList.clear();
        treeAdapter = new TreeAdapter(MainActivity.this, treeList); //this line fixes search bug occurred when changes the button
        lvTreeList.setAdapter(treeAdapter);
        database = openOrCreateDatabase(DBInitialization.DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor = database.rawQuery("SELECT * FROM " + DBContract.TABLE_TREE + " WHERE " + DBContract.COLUMN_LIKED + " = 1 ORDER BY " + DBContract.COLUMN_COMMON_NAME, null);
        while(cursor.moveToNext()) {
            int Id = cursor.getInt(cursor.getColumnIndex(DBContract.COLUMN_ID));
            String commonName = cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_COMMON_NAME));
            String latinName = cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_LATIN_NAME));
            String family = cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_FAMILY));
            String genus = cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_GENUS));
            int isLiked = cursor.getInt(cursor.getColumnIndex(DBContract.COLUMN_LIKED));
            String picturePath = cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_PICTURE_PATH));
            treeList.add(new Tree(Id, commonName, latinName, family, genus, picturePath, isLiked));
        }
        cursor.close();
        if(treeList.size() == 0) { //no favourite tree yet
            txtAnnounce.setVisibility(View.VISIBLE);
            txtAnnounce.setText("Please add more species into the favourite list.");
        }
        Utility.sortTypeSwitch(sortType, treeList);
        treeAdapter.notifyDataSetChanged();
    }

    private void displayTreesBasedOnState(String sortType) {
        if(favouriteSelected == false) {
            displayAllTreesSortedBy(sortType);
        }
        else
            displayFavouriteTreeBasedOnCheckedBox();
    }
    private void displayFavouriteTreeBasedOnCheckedBox() {
        if(!chkFamily.isChecked() && !chkGenus.isChecked()) {
            displayAllFavouriteTreesSortedBy(DBContract.COLUMN_COMMON_NAME);
        }
        else if(chkFamily.isChecked()) {
            displayAllFavouriteTreesSortedBy(DBContract.COLUMN_FAMILY);
        }
        else if(chkGenus.isChecked()) {
            displayAllFavouriteTreesSortedBy(DBContract.COLUMN_GENUS);
        }
    }
    private void displayTreeBasedOnCheckedBox() {
        if(!chkFamily.isChecked() && !chkGenus.isChecked()) {
            displayAllTreesSortedBy(DBContract.COLUMN_COMMON_NAME);
        }
        else if(chkFamily.isChecked()) {
            displayAllTreesSortedBy(DBContract.COLUMN_FAMILY);
        }
        else if(chkGenus.isChecked()) {
            displayAllTreesSortedBy(DBContract.COLUMN_GENUS);
        }
    }
    private void updateCorrespondingHeader(int headerNo) {
        ArrayList<String> keys = new ArrayList<>();
        TextView headerText = null;
        switch(headerNo) {
            case 1:
                keys = firstHeaderKeys;
                headerText = txtFirstHeader;
                break;
            case 2:
                keys = secondHeaderKeys;
                headerText = txtSecondHeader;
                break;
        }
        if(keys.isEmpty()) {
            switch(headerNo) {
                case 1:
                    headerText.setText(DBContract.COLUMN_MARGIN);
                    break;
                case 2:
                    headerText.setText(DBContract.COLUMN_ARRANGEMENT);
                    break;
            }
        }
        else {
            headerText.setText("");
            for (int i = 0; i < keys.size(); i++) {
                if(i == keys.size() - 1)
                    headerText.append(keys.get(i));
                else
                    headerText.append(keys.get(i) + ", ");
            }
        }
    }
    private boolean generateDynamicQuery() {
        if(!firstHeaderKeys.isEmpty()) {
            dynamicQuery += " AND " + DBContract.COLUMN_MARGIN + " = ";
            for(int i = 0; i < firstHeaderKeys.size(); i++) {
                if(i != firstHeaderKeys.size() - 1)
                    dynamicQuery += "'" + firstHeaderKeys.get(i) + "'" + " OR ";
                else
                    dynamicQuery += "'" + firstHeaderKeys.get(i) + "'";
            }
        }
        if(!secondHeaderKeys.isEmpty()) {
            dynamicQuery += " AND " + DBContract.COLUMN_ARRANGEMENT + " = ";
            for(int i = 0; i < secondHeaderKeys.size(); i++) {
                if(i != secondHeaderKeys.size() - 1)
                    dynamicQuery += "'" + secondHeaderKeys.get(i) + "'" + " OR ";
                else
                    dynamicQuery += "'" + secondHeaderKeys.get(i) + "'";
            }
        }
        if(dynamicQuery.length() > START_CHAR_POSITION_TO_DELETE) { // > 25 to make sure there is button selected.
            Toast.makeText(MainActivity.this, dynamicQuery.replaceFirst(dynamicQuery.substring(START_CHAR_POSITION_TO_DELETE, END_CHAR_POSITION_TO_DELETE),""), Toast.LENGTH_LONG).show();
            dynamicQuery = dynamicQuery.replaceFirst(dynamicQuery.substring(START_CHAR_POSITION_TO_DELETE, END_CHAR_POSITION_TO_DELETE), "").concat(" ORDER BY " + DBContract.COLUMN_COMMON_NAME);
            return true;
        }
        else { //no button selected will give a random query to avoid crash cause of substring.
            return false; //a random query which will give no result which is what we want since no button is selected.
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                loadGoogleMapDialog.dismiss();
                mapLoaded = true;
            }
        });
        // Add a marker in Sydney and move the camera
        LatLng newZealand = new LatLng(-41.270020, 173.891755);
//        mMap.addMarker(new MarkerOptions().position(newZealand).title("Marker in New Zealand"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newZealand, 5));
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Intent intent = new Intent(MainActivity.this, MarkerInformationActivity.class);
                intent.putExtra("Bundle", (Bundle) marker.getTag());
                startActivity(intent);
                return false;
            }
        });
    }


//    private void toggleButtonStateGivenMargin() { //to be updated.
//        if(Integer.parseInt(txtToothedCount.getText().toString()) == 0 || Integer.parseInt(txtSmoothCount.getText().toString()) == 0) {
//            if(Integer.parseInt(txtAlternatingCount.getText().toString()) == 0
//                    || Integer.parseInt(txtHandShapedCount.getText().toString()) == 0
//                    || Integer.parseInt(txtOppositeCount.getText().toString()) == 0)
//                btnFinalise.setEnabled(false);
//            else
//                btnFinalise.setEnabled(true);
//        }
//        else
//            btnFinalise.setEnabled(true);
//    }
//
//    private void toggleButtonStateGivenArrangement() { //to be updated.
//        if(Integer.parseInt(txtHandShapedCount.getText().toString()) == 0 || Integer.parseInt(txtSmoothCount.getText().toString()) == 0
//                || Integer.parseInt(txtAlternatingCount.getText().toString()) == 0) {
//            if(radToothed.isChecked() && Integer.parseInt(txtToothedCount.getText().toString()) == 0
//                    || radSmooth.isChecked() && Integer.parseInt(txtSmoothCount.getText().toString()) == 0)
//                btnFinalise.setEnabled(false);
//            else
//                btnFinalise.setEnabled(true);
//        }
//        else
//            btnFinalise.setEnabled(true);
//    }

    @Override
    protected void onResume() { //reset the dynamic query when back to the activity
        dynamicQuery = "SELECT * FROM " + DBContract.TABLE_TREE + " WHERE";
        super.onResume();
    }
}
