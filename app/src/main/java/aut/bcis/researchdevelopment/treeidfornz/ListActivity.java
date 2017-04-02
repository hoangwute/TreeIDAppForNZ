package aut.bcis.researchdevelopment.treeidfornz;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import aut.bcis.researchdevelopment.adapter.TreeAdapter;
import aut.bcis.researchdevelopment.database.DBContract;
import aut.bcis.researchdevelopment.database.DBInitialization;
import aut.bcis.researchdevelopment.model.Tree;

public class ListActivity extends AppCompatActivity {
    private ListView lvTreeList;
    private TreeAdapter treeAdapter;
    private ArrayList<Object> treeList;
    private CheckBox chkFamily, chkGenus;
    private ImageButton btnFavourite;
    public static boolean favouriteSelected = false; //class variable used in TreeAdapter.
    public static TextView txtAnnounce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        addControls();
        addEvents();
    }

    private void addControls() {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        TextView txtBarTitle = (TextView) myToolbar.findViewById(R.id.toolbar_title);
        txtBarTitle.setText("A to Z of NZ Trees");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        lvTreeList = (ListView) findViewById(R.id.lvTreeList);
        treeList = new ArrayList<>();
        treeAdapter = new TreeAdapter(ListActivity.this, treeList);
        lvTreeList.setAdapter(treeAdapter);
        btnFavourite = (ImageButton) findViewById(R.id.btnFavourite);
        chkFamily = (CheckBox) findViewById(R.id.chkFamily);
        chkGenus = (CheckBox) findViewById(R.id.chkGenus);
        txtAnnounce = (TextView) findViewById(R.id.txtAnnounce);
        if (favouriteSelected) {
            displayFavouriteTreeBasedOnCheckedBox();
            btnFavourite.setBackgroundColor(Color.GRAY);
        }
        else
            displayTreeBasedOnCheckedBox();
    }
    private void addEvents() {
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
                Intent intent = new Intent(ListActivity.this, TreeDetailActivity.class);
                TextView txtCommonName = (TextView) view.findViewById(R.id.txtCommonName);
                String treeName = txtCommonName.getText().toString();
                intent.putExtra("Tree", treeName);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem menuSearch = menu.findItem(R.id.action_search);
        menuSearch.setVisible(true);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuSearch);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menuHome) {
            Intent intent = new Intent(ListActivity.this, MainActivity.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.menuList) {
            Intent intent = new Intent(ListActivity.this, ListActivity.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.menuIdentification) {
            Intent intent = new Intent(ListActivity.this, IdentificationActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    private void displayAllTreesSortedBy(String sortType) {
        treeList.clear();
        treeAdapter = new TreeAdapter(ListActivity.this, treeList); //this line fixes search bug occurred when changes the button
        lvTreeList.setAdapter(treeAdapter);
        MainActivity.database = openOrCreateDatabase(DBInitialization.DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor = MainActivity.database.rawQuery("SELECT * FROM " + DBContract.TABLE_TREE + " ORDER BY " + sortType, null);
        while (cursor.moveToNext()) {
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
        treeAdapter = new TreeAdapter(ListActivity.this, treeList); //this line fixes search bug occurred when changes the button
        lvTreeList.setAdapter(treeAdapter);
        MainActivity.database = openOrCreateDatabase(DBInitialization.DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor = MainActivity.database.rawQuery("SELECT * FROM " + DBContract.TABLE_TREE + " WHERE " + DBContract.COLUMN_LIKED + " = 1 ORDER BY " + DBContract.COLUMN_COMMON_NAME, null);
        while (cursor.moveToNext()) {
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
}
