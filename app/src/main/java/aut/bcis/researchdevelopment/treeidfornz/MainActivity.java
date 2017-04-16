package aut.bcis.researchdevelopment.treeidfornz;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;

import aut.bcis.researchdevelopment.adapter.TreeAdapter;
import aut.bcis.researchdevelopment.database.DBContract;
import aut.bcis.researchdevelopment.database.DBInitialization;
import aut.bcis.researchdevelopment.model.FilterEntry;
import aut.bcis.researchdevelopment.model.Tree;

public class MainActivity extends AppCompatActivity {
    //-------------------------Constants-----------------------------------------------------
    public static final int START_CHAR_POSITION_TO_DELETE = 24;
    public static final int END_CHAR_POSITION_TO_DELETE = 28;

    //-------------------------Main Activity-------------------------------------------------
//    private TabHost tabHost;
    public static SearchView searchView;
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
    private CheckBox chkToothed, chkSmooth, chkAlternating, chkOpposite, chkHandshaped;
    private Button btnFinalise;
    public static String dynamicQuery = "SELECT * FROM " + DBContract.TABLE_TREE + " WHERE";
    private TextView txtFirstHeader, txtSecondHeader;
    private ArrayList<String> firstHeaderKeys = new ArrayList(), secondHeaderKeys = new ArrayList();
    private TextView txtToothedCount, txtSmoothCount, txtHandShapedCount, txtOppositeCount, txtAlternatingCount;
    //--------------------------Tree Map Function-----------------------------------------------
    public static GoogleMap mMap;
    public static ProgressDialog loadGoogleMapDialog;
    public static boolean mapLoaded = false;
    public static Spinner spinnerCollection;
    public static ArrayAdapter<FilterEntry> filterEntryAdapter;
    public static ArrayList<FilterEntry> filterEntriesList;
    public static TextView txtSightedTreeCount;
    public static ArrayList<Marker> markerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DBInitialization.getDatabaseFromAssets(MainActivity.this);
        database = openOrCreateDatabase(DBInitialization.DATABASE_NAME, MODE_PRIVATE, null);
        loadPictureIntoDatabase();
        addControls();
//        addEvents();
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
            // mark that first time has already run.
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.commit();
        }
    }

    private void addControls() {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        TextView txtBarTitle = (TextView) myToolbar.findViewById(R.id.toolbar_title);
        txtBarTitle.setText("Home");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //-----------------------list view initiation------------------------------
//        lvTreeList = (ListView) findViewById(R.id.lvTreeList);
//        treeList = new ArrayList<>();
//        treeAdapter = new TreeAdapter(MainActivity.this, treeList);
//        lvTreeList.setAdapter(treeAdapter);
        //-----------------------other views initiation----------------------------
//        btnFavourite = (ImageButton) findViewById(R.id.btnFavourite);
//        chkFamily = (CheckBox) findViewById(R.id.chkFamily);
//        chkGenus = (CheckBox) findViewById(R.id.chkGenus);
//        txtAnnounce = (TextView) findViewById(R.id.txtAnnounce);
//        //-----------------------wizard initiation----------------------------
//        btnFinalise = (Button) findViewById(R.id.btnFinalise);
//        txtFirstHeader = (TextView) findViewById(R.id.txtFirstHeader);
//        txtSecondHeader = (TextView) findViewById(R.id.txtSecondHeader);
//        chkSmooth = (CheckBox) findViewById(R.id.chkSmooth);
//        chkToothed = (CheckBox) findViewById(R.id.chkToothed);
//        chkAlternating = (CheckBox) findViewById(R.id.chkAlternating);
//        chkHandshaped = (CheckBox) findViewById(R.id.chkHandshaped);
//        chkOpposite = (CheckBox) findViewById(R.id.chkOpposite);
//        if (favouriteSelected)
//            displayFavouriteTreeBasedOnCheckedBox();
//        else
//            displayTreeBasedOnCheckedBox();
        //-----------------------tree map initiation----------------------------
//        database = openOrCreateDatabase(DBInitialization.DATABASE_NAME, MODE_PRIVATE, null);
//        markerList = new ArrayList<>();
//        txtSightedTreeCount = (TextView) findViewById(R.id.txtSightedTreeCount);
//        txtSightedTreeCount.setText(String.valueOf(Utility.countAllSightedTree()));
//        spinnerCollection = (Spinner) findViewById(R.id.spinner_collection);
//        filterEntriesList = new ArrayList<>();
//        filterEntryAdapter = new FilterAdapter(MainActivity.this, R.layout.filter_layout, R.id.txtFilterCommonName, filterEntriesList);
//        spinnerCollection.setAdapter(filterEntryAdapter);
//        getAllActiveMarkers(MainActivity.this);

    }

    private void addEvents() {
        //---------------------------list configuration-----------------------------
        chkGenus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && chkFamily.isChecked()) { //only 1 button can be selected at once
                    chkFamily.setChecked(false);
                    displayTreesBasedOnState(DBContract.COLUMN_GENUS);
                } else if (!isChecked && !chkFamily.isChecked()) {
                    displayTreesBasedOnState(DBContract.COLUMN_COMMON_NAME);
                } else
                    displayTreesBasedOnState(DBContract.COLUMN_GENUS);
            }
        });
        chkFamily.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && chkGenus.isChecked()) { //only 1 button can be selected at once
                    chkGenus.setChecked(false);
                    displayTreesBasedOnState(DBContract.COLUMN_FAMILY);
                } else if (!isChecked && !chkGenus.isChecked()) {
                    displayTreesBasedOnState(DBContract.COLUMN_COMMON_NAME);
                } else
                    displayTreesBasedOnState(DBContract.COLUMN_FAMILY);
            }
        });
        btnFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtAnnounce.setVisibility(View.INVISIBLE);
                if (favouriteSelected == false) {
                    displayFavouriteTreeBasedOnCheckedBox();
                    favouriteSelected = true;
                    btnFavourite.setBackgroundColor(Color.GRAY);
                } else {
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
//                backed = true;
                startActivity(intent);
            }
        });
        //---------------------------wizard configuration-----------------------------
        chkSmooth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    firstHeaderKeys.add("smooth");
                } else {
                    firstHeaderKeys.remove("smooth");
                }
                updateCorrespondingHeader(1);
            }
        });
        chkToothed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    firstHeaderKeys.add("toothed");
                } else {
                    firstHeaderKeys.remove("toothed");
                }
                updateCorrespondingHeader(1);
            }
        });
        chkHandshaped.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    secondHeaderKeys.add("hand-shaped");
                } else {
                    secondHeaderKeys.remove("hand-shaped");
                }
                updateCorrespondingHeader(2);
            }
        });
        chkAlternating.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    secondHeaderKeys.add("alternating");
                } else {
                    secondHeaderKeys.remove("alternating");
                }
                updateCorrespondingHeader(2);
            }
        });
        chkOpposite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    secondHeaderKeys.add("opposite");
                } else {
                    secondHeaderKeys.remove("opposite");
                }
                updateCorrespondingHeader(2);
            }
        });
        btnFinalise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (generateDynamicQuery() == true) {
                    Intent intent = new Intent(MainActivity.this, IdentificationResultActivity.class);
                    intent.putExtra("Query", dynamicQuery);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Please specify tree traits", Toast.LENGTH_LONG).show();
                }
            }
        });
        //---------------------------tree map configuration-----------------------------


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menuHome) {
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.menuList) {
            Intent intent = new Intent(MainActivity.this, ListActivity.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.menuIdentification) {
            Intent intent = new Intent(MainActivity.this, IdentificationActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void displayAllTreesSortedBy(String sortType) {
        treeList.clear();
        treeAdapter = new TreeAdapter(MainActivity.this, treeList); //this line fixes search bug occurred when changes the button
        lvTreeList.setAdapter(treeAdapter);
        database = openOrCreateDatabase(DBInitialization.DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor = database.rawQuery("SELECT * FROM " + DBContract.TABLE_TREE + " ORDER BY " + sortType, null);
        while (cursor.moveToNext()) {
            int Id = cursor.getInt(cursor.getColumnIndex(DBContract.COLUMN_ID));
            String commonName = cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_COMMON_NAME));
            String maoriName = cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_MAORI_NAME));
            String latinName = cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_LATIN_NAME));
            String family = cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_FAMILY));
            String genus = cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_GENUS));
            int isLiked = cursor.getInt(cursor.getColumnIndex(DBContract.COLUMN_LIKED));
            String picturePath = cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_PICTURE_PATH));
            treeList.add(new Tree(Id, commonName, maoriName, latinName, family, genus, picturePath, isLiked));
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
        while (cursor.moveToNext()) {
            int Id = cursor.getInt(cursor.getColumnIndex(DBContract.COLUMN_ID));
            String commonName = cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_COMMON_NAME));
            String maoriName = cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_MAORI_NAME));
            String latinName = cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_LATIN_NAME));
            String family = cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_FAMILY));
            String genus = cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_GENUS));
            int isLiked = cursor.getInt(cursor.getColumnIndex(DBContract.COLUMN_LIKED));
            String picturePath = cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_PICTURE_PATH));
            treeList.add(new Tree(Id, commonName, maoriName, latinName, family, genus, picturePath, isLiked));
        }
        cursor.close();
        if (treeList.size() == 0) { //no favourite tree yet
            txtAnnounce.setVisibility(View.VISIBLE);
            txtAnnounce.setText("Please add more species into the favourite list.");
        }
        Utility.sortTypeSwitch(sortType, treeList);
        treeAdapter.notifyDataSetChanged();
    }

    private void displayTreesBasedOnState(String sortType) {
        if (favouriteSelected == false) {
            displayAllTreesSortedBy(sortType);
        } else
            displayFavouriteTreeBasedOnCheckedBox();
    }

    private void displayFavouriteTreeBasedOnCheckedBox() {
        if (!chkFamily.isChecked() && !chkGenus.isChecked()) {
            displayAllFavouriteTreesSortedBy(DBContract.COLUMN_COMMON_NAME);
        } else if (chkFamily.isChecked()) {
            displayAllFavouriteTreesSortedBy(DBContract.COLUMN_FAMILY);
        } else if (chkGenus.isChecked()) {
            displayAllFavouriteTreesSortedBy(DBContract.COLUMN_GENUS);
        }
    }

    private void displayTreeBasedOnCheckedBox() {
        if (!chkFamily.isChecked() && !chkGenus.isChecked()) {
            displayAllTreesSortedBy(DBContract.COLUMN_COMMON_NAME);
        } else if (chkFamily.isChecked()) {
            displayAllTreesSortedBy(DBContract.COLUMN_FAMILY);
        } else if (chkGenus.isChecked()) {
            displayAllTreesSortedBy(DBContract.COLUMN_GENUS);
        }
    }

    private void updateCorrespondingHeader(int headerNo) {
        ArrayList<String> keys = new ArrayList<>();
        TextView headerText = null;
        switch (headerNo) {
            case 1:
                keys = firstHeaderKeys;
                headerText = txtFirstHeader;
                break;
            case 2:
                keys = secondHeaderKeys;
                headerText = txtSecondHeader;
                break;
        }
        if (keys.isEmpty()) {
            switch (headerNo) {
                case 1:
                    headerText.setText(DBContract.COLUMN_MARGIN);
                    break;
                case 2:
                    headerText.setText(DBContract.COLUMN_ARRANGEMENT);
                    break;
            }
        } else {
            headerText.setText("");
            for (int i = 0; i < keys.size(); i++) {
                if (i == keys.size() - 1)
                    headerText.append(keys.get(i));
                else
                    headerText.append(keys.get(i) + ", ");
            }
        }
    }

    private boolean generateDynamicQuery() {
        if (!firstHeaderKeys.isEmpty()) {
            dynamicQuery += " AND " + DBContract.COLUMN_MARGIN + " = ";
            for (int i = 0; i < firstHeaderKeys.size(); i++) {
                if (i != firstHeaderKeys.size() - 1)
                    dynamicQuery += "'" + firstHeaderKeys.get(i) + "'" + " OR ";
                else
                    dynamicQuery += "'" + firstHeaderKeys.get(i) + "'";
            }
        }
        if (!secondHeaderKeys.isEmpty()) {
            dynamicQuery += " AND " + DBContract.COLUMN_ARRANGEMENT + " = ";
            for (int i = 0; i < secondHeaderKeys.size(); i++) {
                if (i != secondHeaderKeys.size() - 1)
                    dynamicQuery += "'" + secondHeaderKeys.get(i) + "'" + " OR ";
                else
                    dynamicQuery += "'" + secondHeaderKeys.get(i) + "'";
            }
        }
        if (dynamicQuery.length() > START_CHAR_POSITION_TO_DELETE) { // > 25 to make sure there is button selected.
            Toast.makeText(MainActivity.this, dynamicQuery.replaceFirst(dynamicQuery.substring(START_CHAR_POSITION_TO_DELETE, END_CHAR_POSITION_TO_DELETE), ""), Toast.LENGTH_LONG).show();
            dynamicQuery = dynamicQuery.replaceFirst(dynamicQuery.substring(START_CHAR_POSITION_TO_DELETE, END_CHAR_POSITION_TO_DELETE), "").concat(" ORDER BY " + DBContract.COLUMN_COMMON_NAME);
            return true;
        } else { //no button selected will give a random query to avoid crash cause of substring.
            return false; //a random query which will give no result which is what we want since no button is selected.
        }
    }

    public static void generateFilterEntryList(Activity context) {
        filterEntriesList.clear();
        Cursor cursor = database.rawQuery("SELECT * FROM FilterEntry ORDER BY " + DBContract.COLUMN_MARKER_COMMON_NAME, null);
        while (cursor.moveToNext()) {
            String commonName = cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_MARKER_COMMON_NAME));
            String latinName = cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_MARKER_LATIN_NAME));
            int isFiltered = cursor.getInt(cursor.getColumnIndex(DBContract.COLUMN_MARKER_FILTERED));
            int id = cursor.getInt(cursor.getColumnIndex(DBContract.COLUMN_MARKER_ID));
            FilterEntry filterEntry = new FilterEntry(commonName, latinName, Utility.countSightedTreeType(commonName), isFiltered, id);
            filterEntriesList.add(filterEntry);
        }
        cursor.close();
        filterEntryAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
//        database = openOrCreateDatabase(DBInitialization.DATABASE_NAME, MODE_PRIVATE, null);
//        Toast.makeText(MainActivity.this, "RESUMED", Toast.LENGTH_SHORT).show();
//        dynamicQuery = "SELECT * FROM " + DBContract.TABLE_TREE + " WHERE"; //reset the dynamic query when back to the activity
//        tabHost = (TabHost) findViewById(R.id.tabHost);
//        if (backed == true) {
//            switch (TabsFragment.currentTab) {
//                case 1:
//                    TabsFragment.tabHost.setCurrentTab(0);
//                    backed = false;
//                    break;
//                case 2:
//                    TabsFragment.tabHost.setCurrentTab(1);
//                    backed = false;
//                    break;
//                case 3:
//                    TabsFragment.tabHost.setCurrentTab(2);
//                    backed = false;
//                    break;
//                case 4:
//                    TabsFragment.tabHost.setCurrentTab(3);
//                    backed = false;
//                    break;
//            }
//        }
        super.onResume();
    }


}
