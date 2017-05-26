package aut.bcis.researchdevelopment.treeidfornz;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
    private CheckBox chkIdentifiedFamily, chkIdentifiedStructuralClass, chkIdentifiedExpandSort, chkIdentifiedFavourite;
    public static boolean identifiedFavouriteSelected = false; //class variable used in TreeAdapter.
    private RadioGroup groupIdentifiedRad;
    private RadioButton radIdentifiedEnglishName, radIdentifiedMaoriName, radIdentifiedLatinName;
    private SearchView listIdentifiedSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identification_result);
        addControls();
        addEvents();
    }

    private void addControls() {
        identifiedFavouriteSelected = false;
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        TextView txtBarTitle = (TextView) myToolbar.findViewById(R.id.toolbar_title);
        txtBarTitle.setText("Identification Result");
        txtBarTitle.setTextColor(Color.WHITE);
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(),R.drawable.icon_menuu); // change tool bar icon
        myToolbar.setOverflowIcon(drawable);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        listIdentifiedSearch = (SearchView) findViewById(R.id.listIdentifiedSearch);
        lvIdentifiedTreeList = (ListView) findViewById(R.id.lvIdentifiedTreeList);
        identifiedTreeList = new ArrayList<>();
        identifiedTreeAdapter = new TreeAdapter(IdentificationResultActivity.this, identifiedTreeList);
        lvIdentifiedTreeList.setAdapter(identifiedTreeAdapter);
        Intent intent = getIntent();
        receivedDynamicQuery = intent.getStringExtra("Query");
        chkIdentifiedFavourite = (CheckBox) findViewById(R.id.chkIdentifiedFavourite);
        chkIdentifiedFamily = (CheckBox) findViewById(R.id.chkIdentifiedFamily);
        chkIdentifiedStructuralClass = (CheckBox) findViewById(R.id.chkIdentifiedStructuralClass);
        chkIdentifiedExpandSort = (CheckBox) findViewById(R.id.chkIdentifiedExpandSort);
        radIdentifiedEnglishName = (RadioButton) findViewById(R.id.radIdentifiedEnglishName);
        radIdentifiedLatinName = (RadioButton) findViewById(R.id.radIdentifiedLatinName);
        radIdentifiedMaoriName = (RadioButton) findViewById(R.id.radIdentifiedMaoriName);
        if (identifiedFavouriteSelected)
            displayAllIdentifiedFavouriteTreeBasedOnCheckedBox();
        else
            displayAllIdentifiedTreeBasedOnCheckedBox();
        groupIdentifiedRad = (RadioGroup) findViewById(R.id.groupIdentifiedRad);
        groupIdentifiedRad.bringToFront();
    }

    private void addEvents() {
        listIdentifiedSearch.setQueryHint("Search by keyword...");
        listIdentifiedSearch.onActionViewCollapsed();
        listIdentifiedSearch.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
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
        listIdentifiedSearch.setIconified(false);
        listIdentifiedSearch.clearFocus();
        LinearLayout linearLayout1 = (LinearLayout) listIdentifiedSearch.getChildAt(0);
        LinearLayout linearLayout2 = (LinearLayout) linearLayout1.getChildAt(2);
        final LinearLayout linearLayout3 = (LinearLayout) linearLayout2.getChildAt(1);
        linearLayout3.post(new Runnable() {
            @Override
            public void run() {
                ViewGroup.LayoutParams params = linearLayout3.getLayoutParams();
                params.height = linearLayout3.getHeight() + (linearLayout3.getHeight()/3);
                linearLayout3.setLayoutParams(params);
            }
        });
        AutoCompleteTextView autoComplete = (AutoCompleteTextView) linearLayout3.getChildAt(0);
        autoComplete.setTextSize(15);
        chkIdentifiedStructuralClass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && chkIdentifiedFamily.isChecked()) { //only 1 button can be selected at once
                    chkIdentifiedFamily.setChecked(false);
                    chkIdentifiedStructuralClass.setTextColor(Color.BLACK);
                    chkIdentifiedFamily.setTextColor(Color.WHITE);
                    displayTreesBasedOnState(DBContract.COLUMN_STRUCTURAL_CLASS);
                } else if (!isChecked && !chkIdentifiedFamily.isChecked()) {
                    chkIdentifiedStructuralClass.setTextColor(Color.WHITE);
                    chkIdentifiedFamily.setTextColor(Color.WHITE);
                    displayIdentifiedTreeBasedOnStateAndRadButton();
                } else {
                    chkIdentifiedStructuralClass.setTextColor(Color.BLACK);
                    chkIdentifiedFamily.setTextColor(Color.WHITE);
                    displayTreesBasedOnState(DBContract.COLUMN_STRUCTURAL_CLASS);
                }
            }
        });
        chkIdentifiedFamily.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && chkIdentifiedStructuralClass.isChecked()) { //only 1 button can be selected at once
                    chkIdentifiedStructuralClass.setChecked(false);
                    chkIdentifiedFamily.setTextColor(Color.BLACK);
                    chkIdentifiedStructuralClass.setTextColor(Color.WHITE);
                    displayTreesBasedOnState(DBContract.COLUMN_FAMILY);
                } else if (!isChecked && !chkIdentifiedStructuralClass.isChecked()) {
                    chkIdentifiedFamily.setTextColor(Color.WHITE);
                    chkIdentifiedStructuralClass.setTextColor(Color.WHITE);
                    displayIdentifiedTreeBasedOnStateAndRadButton();
                } else {
                    displayTreesBasedOnState(DBContract.COLUMN_FAMILY);
                    chkIdentifiedFamily.setTextColor(Color.BLACK);
                    chkIdentifiedStructuralClass.setTextColor(Color.WHITE);
                }
            }
        });
        chkIdentifiedExpandSort.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    Animation animation = AnimationUtils.loadAnimation(IdentificationResultActivity.this, R.anim.openradiogroupeffect);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            groupIdentifiedRad.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    groupIdentifiedRad.startAnimation(animation);
                }
                else {
                    Animation animation = AnimationUtils.loadAnimation(IdentificationResultActivity.this, R.anim.closeradiogroupeffect);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            groupIdentifiedRad.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    groupIdentifiedRad.startAnimation(animation);
                }
            }
        });
        chkIdentifiedFavourite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    displayAllIdentifiedFavouriteTreeBasedOnCheckedBox();
                    identifiedFavouriteSelected = true;
                    chkIdentifiedFavourite.setTextColor(Color.BLACK);
                } else {
                    displayAllIdentifiedTreeBasedOnCheckedBox();
                    identifiedFavouriteSelected = false;
                    chkIdentifiedFavourite.setTextColor(Color.WHITE);
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
        radIdentifiedEnglishName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    radIdentifiedEnglishName.setTextColor(Color.BLACK);
                    displayAccordingToRadButton(DBContract.COLUMN_COMMON_NAME);
                }
                else {
                    radIdentifiedEnglishName.setTextColor(Color.WHITE);
                }
            }
        });
        radIdentifiedMaoriName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    radIdentifiedMaoriName.setTextColor(Color.BLACK);
                    displayAccordingToRadButton(DBContract.COLUMN_MAORI_NAME);
                }
                else {
                    radIdentifiedMaoriName.setTextColor(Color.WHITE);
                }
            }
        });
        radIdentifiedLatinName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    radIdentifiedLatinName.setTextColor(Color.BLACK);
                    displayAccordingToRadButton(DBContract.COLUMN_LATIN_NAME);
                }
                else {
                    radIdentifiedLatinName.setTextColor(Color.WHITE);
                }
            }
        });
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
            String maoriName = cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_MAORI_NAME));
            String latinName = cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_LATIN_NAME));
            String family = cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_FAMILY));
            String structuralClass = cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_STRUCTURAL_CLASS));
            String mainPicture = cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_MAIN_PICTURE));
            int isLiked = cursor.getInt(cursor.getColumnIndex(DBContract.COLUMN_LIKED));
            identifiedTreeList.add(new Tree(Id, commonName, maoriName, latinName, family, structuralClass, mainPicture, isLiked));
        }
        if(identifiedTreeList.isEmpty()) {
            Toast.makeText(IdentificationResultActivity.this, "There is no tree which matches your search", Toast.LENGTH_LONG).show();
            finish();
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
        while (cursor.moveToNext()) {
            int Id = cursor.getInt(cursor.getColumnIndex(DBContract.COLUMN_ID));
            String commonName = cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_COMMON_NAME));
            String maoriName = cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_MAORI_NAME));
            String latinName = cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_LATIN_NAME));
            String family = cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_FAMILY));
            String structuralClass = cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_STRUCTURAL_CLASS));
            String mainPicture = cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_MAIN_PICTURE));
            int isLiked = cursor.getInt(cursor.getColumnIndex(DBContract.COLUMN_LIKED));
            identifiedTreeList.add(new Tree(Id, commonName, maoriName, latinName, family, structuralClass, mainPicture, isLiked));
        }
        cursor.close();
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
        if (!chkIdentifiedFamily.isChecked() && !chkIdentifiedStructuralClass.isChecked()) {
            if(radIdentifiedEnglishName.isChecked())
                displayAllIdentifiedFavouriteTreesSortedBy(DBContract.COLUMN_COMMON_NAME);
            else if(radIdentifiedLatinName.isChecked())
                displayAllIdentifiedFavouriteTreesSortedBy(DBContract.COLUMN_LATIN_NAME);
            else if(radIdentifiedMaoriName.isChecked())
                displayAllIdentifiedFavouriteTreesSortedBy(DBContract.COLUMN_MAORI_NAME);
        } else if (chkIdentifiedFamily.isChecked()) {
            displayAllIdentifiedFavouriteTreesSortedBy(DBContract.COLUMN_FAMILY);
        } else if (chkIdentifiedStructuralClass.isChecked()) {
            displayAllIdentifiedFavouriteTreesSortedBy(DBContract.COLUMN_STRUCTURAL_CLASS);
        }
    }

    private void displayAllIdentifiedTreeBasedOnCheckedBox() {
        if (!chkIdentifiedFamily.isChecked() && !chkIdentifiedStructuralClass.isChecked()) {
            if(radIdentifiedEnglishName.isChecked())
                displayAllIdentifiedTreesSortedBy(DBContract.COLUMN_COMMON_NAME);
            else if(radIdentifiedLatinName.isChecked())
                displayAllIdentifiedTreesSortedBy(DBContract.COLUMN_LATIN_NAME);
            else if(radIdentifiedMaoriName.isChecked())
                displayAllIdentifiedTreesSortedBy(DBContract.COLUMN_MAORI_NAME);
        } else if (chkIdentifiedFamily.isChecked()) {
            displayAllIdentifiedTreesSortedBy(DBContract.COLUMN_FAMILY);
        } else if (chkIdentifiedStructuralClass.isChecked()) {
            displayAllIdentifiedTreesSortedBy(DBContract.COLUMN_STRUCTURAL_CLASS);
        }
    }
    private void displayIdentifiedTreeBasedOnStateAndRadButton() {
        if(radIdentifiedEnglishName.isChecked())
            displayTreesBasedOnState(DBContract.COLUMN_COMMON_NAME);
        else if(radIdentifiedLatinName.isChecked())
            displayTreesBasedOnState(DBContract.COLUMN_LATIN_NAME);
        else if(radIdentifiedMaoriName.isChecked())
            displayTreesBasedOnState(DBContract.COLUMN_MAORI_NAME);
    }
    private void displayAccordingToRadButton(String nameSetting) {
        if (!chkIdentifiedFamily.isChecked() && !chkIdentifiedStructuralClass.isChecked()) {
            if(identifiedFavouriteSelected) {
                displayAllIdentifiedFavouriteTreesSortedBy(nameSetting);
            }
            else {
                displayAllIdentifiedTreesSortedBy(nameSetting);
            }
        }
    }

    @Override
    protected void onResume() {
        listIdentifiedSearch.clearFocus();
        super.onResume();
    }

}
