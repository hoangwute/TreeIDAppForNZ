package aut.bcis.researchdevelopment.treeidfornz;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.ArrayList;

import aut.bcis.researchdevelopment.adapter.TreeAdapter;
import aut.bcis.researchdevelopment.database.Database;
import aut.bcis.researchdevelopment.model.Tree;

public class MainActivity extends AppCompatActivity {
    //-------------------------Main Activity-------------------------------------------------
    private TabHost tabHost;
    private SearchView searchView;
    public static SQLiteDatabase database = null;
    //-------------------------List/Search Function------------------------------------------
    private ListView lvTreeList;
    private TreeAdapter treeAdapter;
    private ArrayList<Object> treeList;
    private CheckBox chkFamily, chkGenus;
    private ImageButton btnFavourite;
    public static boolean favouriteSelected = false;
    public static TextView txtAnnounce;
    //--------------------------Wizard Function-----------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Database.getDatabaseFromAssets(MainActivity.this);
        addControls();
        addEvents();
        displayAllTreesSortedBy("CommonName");
    }

    private void addControls() {
        //----------------------tabhost initiation--------------------------------
        tabHost = (TabHost)findViewById(R.id.tabHost);
        tabHost.setup();
        TabHost.TabSpec tab1 = tabHost.newTabSpec("t1");
        tab1.setIndicator("",getResources().getDrawable(R.drawable.icon_like));
        tab1.setContent(R.id.tab1);
        TabHost.TabSpec tab2 = tabHost.newTabSpec("t2");
        tab2.setIndicator("bb");
        tab2.setContent(R.id.tab2);
        TabHost.TabSpec tab3 = tabHost.newTabSpec("t3");
        tab3.setIndicator("cc");
        tab3.setContent(R.id.tab3);
        TabHost.TabSpec tab4 = tabHost.newTabSpec("t4");
        tab4.setIndicator("dd");
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
    }

    private void displayAllTreesSortedBy(String sortType) {
        treeList.clear();
        treeAdapter = new TreeAdapter(MainActivity.this, treeList); //this line fixes search bug occurred when changes the button
        lvTreeList.setAdapter(treeAdapter);
        database = openOrCreateDatabase(Database.DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor = database.rawQuery("SELECT * FROM Tree ORDER BY " + sortType, null);
        while(cursor.moveToNext()) {
            int Id = cursor.getInt(cursor.getColumnIndex("ID"));
            String commonName = cursor.getString(cursor.getColumnIndex("CommonName"));
            String latinName = cursor.getString(cursor.getColumnIndex("LatinName"));
            String family = cursor.getString(cursor.getColumnIndex("Family"));
            String genus = cursor.getString(cursor.getColumnIndex("Genus"));
            int isLiked = cursor.getInt(cursor.getColumnIndex("Liked"));
            String picturePath = cursor.getString(cursor.getColumnIndex("PicturePath"));
            treeList.add(new Tree(Id, commonName, latinName, family, genus, picturePath, isLiked));
        }
        cursor.close();
        switch(sortType) {
            case "CommonName":
                Utility.generateAlphabeticalHeaders(treeList);
                break;
            case "Family":
                Utility.generateFamilyHeaders(treeList);
                break;
            case "Genus":
                Utility.generateGenusHeaders(treeList);
                break;
        }
        treeAdapter.notifyDataSetChanged();
    }

    private void displayAllFavouriteTreesSortedBy(String sortType) {
        treeList.clear();
        treeAdapter = new TreeAdapter(MainActivity.this, treeList); //this line fixes search bug occurred when changes the button
        lvTreeList.setAdapter(treeAdapter);
        database = openOrCreateDatabase(Database.DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor = database.rawQuery("SELECT * FROM Tree WHERE Liked = 1 ORDER BY CommonName", null);
        while(cursor.moveToNext()) {
            int Id = cursor.getInt(cursor.getColumnIndex("ID"));
            String commonName = cursor.getString(cursor.getColumnIndex("CommonName"));
            String latinName = cursor.getString(cursor.getColumnIndex("LatinName"));
            String family = cursor.getString(cursor.getColumnIndex("Family"));
            String genus = cursor.getString(cursor.getColumnIndex("Genus"));
            int isLiked = cursor.getInt(cursor.getColumnIndex("Liked"));
            String picturePath = cursor.getString(cursor.getColumnIndex("PicturePath"));
            treeList.add(new Tree(Id, commonName, latinName, family, genus, picturePath, isLiked));
        }
        cursor.close();
        if(treeList.size() == 0) {
            txtAnnounce.setVisibility(View.VISIBLE);
            txtAnnounce.setText("Please add more species into the favourite list.");
        }
        switch(sortType) {
            case "CommonName":
                Utility.generateAlphabeticalHeaders(treeList);
                break;
            case "Family":
                Utility.generateFamilyHeaders(treeList);
                break;
            case "Genus":
                Utility.generateGenusHeaders(treeList);
                break;
        }
        treeAdapter.notifyDataSetChanged();
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
                }
            }
        });
        chkGenus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked && chkFamily.isChecked()) { //only 1 button can be selected at once
                    chkFamily.setChecked(false);
                    displayTreesBasedOnState("Genus");
                }
                else if(!isChecked && !chkFamily.isChecked()) {
                    displayTreesBasedOnState("CommonName");
                }
                else
                    displayTreesBasedOnState("Genus");
            }
        });
        chkFamily.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked && chkGenus.isChecked()) { //only 1 button can be selected at once
                    chkGenus.setChecked(false);
                    displayTreesBasedOnState("Family");
                }
                else if(!isChecked && !chkGenus.isChecked()) {
                    displayTreesBasedOnState("CommonName");
                }
                else
                    displayTreesBasedOnState("Family");
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
            displayAllFavouriteTreesSortedBy("CommonName");
        }
        else if(chkFamily.isChecked()) {
            displayAllFavouriteTreesSortedBy("Family");
        }
        else if(chkGenus.isChecked()) {
            displayAllFavouriteTreesSortedBy("Genus");
        }
    }
    private void displayTreeBasedOnCheckedBox() {
        if(!chkFamily.isChecked() && !chkGenus.isChecked()) {
            displayAllTreesSortedBy("CommonName");
        }
        else if(chkFamily.isChecked()) {
            displayAllTreesSortedBy("Family");
        }
        else if(chkGenus.isChecked()) {
            displayAllTreesSortedBy("Genus");
        }
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
}
