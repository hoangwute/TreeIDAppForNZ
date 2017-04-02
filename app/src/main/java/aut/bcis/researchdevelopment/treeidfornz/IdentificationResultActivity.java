package aut.bcis.researchdevelopment.treeidfornz;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import aut.bcis.researchdevelopment.adapter.TreeAdapter;
import aut.bcis.researchdevelopment.database.DBContract;
import aut.bcis.researchdevelopment.database.DBInitialization;
import aut.bcis.researchdevelopment.model.Tree;

public class IdentificationResultActivity extends AppCompatActivity {

    private ListView lvIdentifiedTreeList;
    private TreeAdapter identifiedTreeAdapter;
    private ArrayList<Object> identifiedTreeList;
    private String receivedDynamicQuery;
    private CheckBox chkIdentifiedFamily, chkIdentifiedGenus;
    private ImageButton btnIdentifiedFavourite;
    public static boolean identifiedFavouriteSelected = false; //class variable used in TreeAdapter.
    public static TextView txtIdentifiedAnnounce;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wizard_result);
        addControls();
        addEvents();
    }

    private void addControls() {
        identifiedFavouriteSelected = false;
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        TextView txtBarTitle = (TextView) myToolbar.findViewById(R.id.toolbar_title);
        txtBarTitle.setText("Identification Result");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        lvIdentifiedTreeList = (ListView) findViewById(R.id.lvIdentifiedTreeList);
        identifiedTreeList = new ArrayList<>();
        identifiedTreeAdapter = new TreeAdapter(IdentificationResultActivity.this, identifiedTreeList);
        lvIdentifiedTreeList.setAdapter(identifiedTreeAdapter);
        Intent intent = getIntent();
        receivedDynamicQuery = intent.getStringExtra("Query");
        btnIdentifiedFavourite = (ImageButton) findViewById(R.id.btnIdentifiedFavourite);
        chkIdentifiedFamily = (CheckBox) findViewById(R.id.chkIdentifiedFamily);
        chkIdentifiedGenus = (CheckBox) findViewById(R.id.chkIdentifiedGenus);
        txtIdentifiedAnnounce = (TextView) findViewById(R.id.txtIdentifiedAnnounce);
        if (identifiedFavouriteSelected)
            displayAllIdentifiedFavouriteTreeBasedOnCheckedBox();
        else
            displayAllIdentifiedTreeBasedOnCheckedBox();
    }

    private void addEvents() {
        chkIdentifiedGenus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && chkIdentifiedFamily.isChecked()) { //only 1 button can be selected at once
                    chkIdentifiedFamily.setChecked(false);
                    displayTreesBasedOnState(DBContract.COLUMN_GENUS);
                } else if (!isChecked && !chkIdentifiedFamily.isChecked()) {
                    displayTreesBasedOnState(DBContract.COLUMN_COMMON_NAME);
                } else
                    displayTreesBasedOnState(DBContract.COLUMN_GENUS);
            }
        });
        chkIdentifiedFamily.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && chkIdentifiedGenus.isChecked()) { //only 1 button can be selected at once
                    chkIdentifiedGenus.setChecked(false);
                    displayTreesBasedOnState(DBContract.COLUMN_FAMILY);
                } else if (!isChecked && !chkIdentifiedGenus.isChecked()) {
                    displayTreesBasedOnState(DBContract.COLUMN_COMMON_NAME);
                } else
                    displayTreesBasedOnState(DBContract.COLUMN_FAMILY);
            }
        });
        btnIdentifiedFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtIdentifiedAnnounce.setVisibility(View.INVISIBLE);
                if (identifiedFavouriteSelected == false) {
                    displayAllIdentifiedFavouriteTreeBasedOnCheckedBox();
                    identifiedFavouriteSelected = true;
                    btnIdentifiedFavourite.setBackgroundColor(Color.GRAY);
                } else {
                    displayAllIdentifiedTreeBasedOnCheckedBox();
                    identifiedFavouriteSelected = false;
                    btnIdentifiedFavourite.setBackgroundColor(Color.WHITE);
                }
            }
        });
        lvIdentifiedTreeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) { /*position cannot be used here as array list is fixed and not changed
                                                                                               according to search filter */
                Intent intent = new Intent(IdentificationResultActivity.this, TreeDetailActivity.class);
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
                identifiedTreeAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menuHome) {
            Intent intent = new Intent(IdentificationResultActivity.this, MainActivity.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.menuList) {
            Intent intent = new Intent(IdentificationResultActivity.this, ListActivity.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.menuIdentification) {
            Intent intent = new Intent(IdentificationResultActivity.this, IdentificationActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void displayAllIdentifiedTreesSortedBy(String sortType) {
        identifiedTreeList.clear();
        identifiedTreeAdapter = new TreeAdapter(IdentificationResultActivity.this, identifiedTreeList); //this line fixes search bug occurred when changes the button
        lvIdentifiedTreeList.setAdapter(identifiedTreeAdapter);
        MainActivity.database = openOrCreateDatabase(DBInitialization.DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor = MainActivity.database.rawQuery(receivedDynamicQuery + " ORDER BY " + sortType, null);
        while (cursor.moveToNext()) {
            int Id = cursor.getInt(cursor.getColumnIndex(DBContract.COLUMN_ID));
            String commonName = cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_COMMON_NAME));
            String latinName = cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_LATIN_NAME));
            String family = cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_FAMILY));
            String genus = cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_GENUS));
            int isLiked = cursor.getInt(cursor.getColumnIndex(DBContract.COLUMN_LIKED));
            String picturePath = cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_PICTURE_PATH));
            identifiedTreeList.add(new Tree(Id, commonName, latinName, family, genus, picturePath, isLiked));
        }
        cursor.close();
        Utility.sortTypeSwitch(sortType, identifiedTreeList);
        identifiedTreeAdapter.notifyDataSetChanged();
    }

    private void displayAllIdentifiedFavouriteTreesSortedBy(String sortType) {
        identifiedTreeList.clear();
        identifiedTreeAdapter = new TreeAdapter(IdentificationResultActivity.this, identifiedTreeList); //this line fixes search bug occurred when changes the button
        lvIdentifiedTreeList.setAdapter(identifiedTreeAdapter);
        MainActivity.database = openOrCreateDatabase(DBInitialization.DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor = MainActivity.database.rawQuery(receivedDynamicQuery + " AND " + DBContract.COLUMN_LIKED + " = 1 ORDER BY " + DBContract.COLUMN_COMMON_NAME, null);
        Toast.makeText(IdentificationResultActivity.this, receivedDynamicQuery + " AND " + DBContract.COLUMN_LIKED + " = 1 ORDER BY " + DBContract.COLUMN_COMMON_NAME, Toast.LENGTH_LONG).show();
        while (cursor.moveToNext()) {
            int Id = cursor.getInt(cursor.getColumnIndex(DBContract.COLUMN_ID));
            String commonName = cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_COMMON_NAME));
            String latinName = cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_LATIN_NAME));
            String family = cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_FAMILY));
            String genus = cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_GENUS));
            int isLiked = cursor.getInt(cursor.getColumnIndex(DBContract.COLUMN_LIKED));
            String picturePath = cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_PICTURE_PATH));
            identifiedTreeList.add(new Tree(Id, commonName, latinName, family, genus, picturePath, isLiked));
        }
        cursor.close();
        if (identifiedTreeList.size() == 0) { //no favourite tree yet
            txtIdentifiedAnnounce.setVisibility(View.VISIBLE);
            txtIdentifiedAnnounce.setText("There is no identified tree which is already in your favourite list.");
        }
        Utility.sortTypeSwitch(sortType, identifiedTreeList);
        identifiedTreeAdapter.notifyDataSetChanged();
    }

    private void displayTreesBasedOnState(String sortType) {
        if (identifiedFavouriteSelected == false) {
            displayAllIdentifiedTreesSortedBy(sortType);
        } else
            displayAllIdentifiedFavouriteTreeBasedOnCheckedBox();
    }

    private void displayAllIdentifiedFavouriteTreeBasedOnCheckedBox() {
        if (!chkIdentifiedFamily.isChecked() && !chkIdentifiedGenus.isChecked()) {
            displayAllIdentifiedFavouriteTreesSortedBy(DBContract.COLUMN_COMMON_NAME);
        } else if (chkIdentifiedFamily.isChecked()) {
            displayAllIdentifiedFavouriteTreesSortedBy(DBContract.COLUMN_FAMILY);
        } else if (chkIdentifiedGenus.isChecked()) {
            displayAllIdentifiedFavouriteTreesSortedBy(DBContract.COLUMN_GENUS);
        }
    }

    private void displayAllIdentifiedTreeBasedOnCheckedBox() {
        if (!chkIdentifiedFamily.isChecked() && !chkIdentifiedGenus.isChecked()) {
            displayAllIdentifiedTreesSortedBy(DBContract.COLUMN_COMMON_NAME);
        } else if (chkIdentifiedFamily.isChecked()) {
            displayAllIdentifiedTreesSortedBy(DBContract.COLUMN_FAMILY);
        } else if (chkIdentifiedGenus.isChecked()) {
            displayAllIdentifiedTreesSortedBy(DBContract.COLUMN_GENUS);
        }
    }

    private void displayAllClassifiedTrees() {
        identifiedTreeList.clear();
        identifiedTreeAdapter = new TreeAdapter(IdentificationResultActivity.this, identifiedTreeList); //this line fixes search bug occurred when changes the button
        lvIdentifiedTreeList.setAdapter(identifiedTreeAdapter);
        MainActivity.database = openOrCreateDatabase(DBInitialization.DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor = MainActivity.database.rawQuery(receivedDynamicQuery, null); //cursor based on the dynamic query statement
        while(cursor.moveToNext()) {
            int Id = cursor.getInt(cursor.getColumnIndex("ID"));
            String commonName = cursor.getString(cursor.getColumnIndex("CommonName"));
            String latinName = cursor.getString(cursor.getColumnIndex("LatinName"));
            String family = cursor.getString(cursor.getColumnIndex("Family"));
            String genus = cursor.getString(cursor.getColumnIndex("Genus"));
            int isLiked = cursor.getInt(cursor.getColumnIndex("Liked"));
            String picturePath = cursor.getString(cursor.getColumnIndex("PicturePath"));
            identifiedTreeList.add(new Tree(Id, commonName, latinName, family, genus, picturePath, isLiked));
        }
        cursor.close();
        identifiedTreeAdapter.notifyDataSetChanged();
    }
}
