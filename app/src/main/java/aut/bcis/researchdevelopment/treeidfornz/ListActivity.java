package aut.bcis.researchdevelopment.treeidfornz;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import aut.bcis.researchdevelopment.adapter.TreeAdapter;
import aut.bcis.researchdevelopment.database.DBContract;
import aut.bcis.researchdevelopment.database.DBInitialization;
import aut.bcis.researchdevelopment.model.Tree;

public class ListActivity extends AppCompatActivity {
    private ListView lvTreeList;
    private TreeAdapter treeAdapter;
    private ArrayList<Object> treeList;
    private CheckBox chkFamily, chkStructuralClass, chkExpandSort, chkFavourite;
    public static boolean favouriteSelected = false; //class variable used in TreeAdapter.
    private RadioGroup groupRad;
    private RadioButton radEnglishName, radMaoriName, radLatinName;
    private SearchView listSearch;

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
        txtBarTitle.setText("A-Z of NZ trees");
        txtBarTitle.setTextColor(Color.WHITE);
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(),R.drawable.icon_menuu); // change tool bar icon
        myToolbar.setOverflowIcon(drawable);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        listSearch = (SearchView) findViewById(R.id.listSearch);
        lvTreeList = (ListView) findViewById(R.id.lvTreeList);
        treeList = new ArrayList<>();
        treeAdapter = new TreeAdapter(ListActivity.this, treeList);
        lvTreeList.setAdapter(treeAdapter);
        chkFamily = (CheckBox) findViewById(R.id.chkFamily);
        chkStructuralClass = (CheckBox) findViewById(R.id.chkStructuralClass);
        chkFavourite = (CheckBox) findViewById(R.id.chkFavourite);
        chkExpandSort = (CheckBox) findViewById(R.id.chkExpandSort);
        radEnglishName = (RadioButton) findViewById(R.id.radEnglishName);
        radLatinName = (RadioButton) findViewById(R.id.radLatinName);
        radMaoriName = (RadioButton) findViewById(R.id.radMaoriName);
        if (favouriteSelected) {
            displayFavouriteTreeBasedOnCheckedBox();
            chkFavourite.setChecked(true);
            chkFavourite.setTextColor(Color.BLACK);
        }
        else
            displayTreeBasedOnCheckedBox();
        groupRad = (RadioGroup) findViewById(R.id.groupRad);
//        groupRad.bringToFront();
        Intent intent = getIntent();
        String homePage = intent.getStringExtra("FromHomePage");
        if(homePage != null && homePage.equals("homepage")) {
            chkFavourite.post(new Runnable() {
                @Override
                public void run() { //the decisive factor for all other views dimension as this is the only group which has 4 views.
                    chkFavourite.setChecked(true);
                    listSearch.clearFocus();
                }
            });
        }
    }
    private void addEvents() {
        listSearch.setQueryHint("Search by keyword...");
        listSearch.onActionViewCollapsed();
        listSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        listSearch.setIconified(false);
        listSearch.clearFocus();
        LinearLayout linearLayout1 = (LinearLayout) listSearch.getChildAt(0);
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
        chkStructuralClass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && chkFamily.isChecked()) { //only 1 button can be selected at once
                    chkFamily.setChecked(false);
                    chkStructuralClass.setTextColor(Color.BLACK);
                    chkFamily.setTextColor(Color.WHITE);
                    displayTreesBasedOnState(DBContract.COLUMN_STRUCTURAL_CLASS);
                } else if (!isChecked && !chkFamily.isChecked()) {
                    chkStructuralClass.setTextColor(Color.WHITE);
                    chkFamily.setTextColor(Color.WHITE);
                    displayTreeBasedOnStateAndRadButton();
                } else {
                    chkStructuralClass.setTextColor(Color.BLACK);
                    chkFamily.setTextColor(Color.WHITE);
                    displayTreesBasedOnState(DBContract.COLUMN_STRUCTURAL_CLASS);
                }
            }
        });
        chkFamily.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && chkStructuralClass.isChecked()) { //only 1 button can be selected at once
                    chkStructuralClass.setChecked(false);
                    chkFamily.setTextColor(Color.BLACK);
                    chkStructuralClass.setTextColor(Color.WHITE);
                    displayTreesBasedOnState(DBContract.COLUMN_FAMILY);
                } else if (!isChecked && !chkStructuralClass.isChecked()) {
                    chkFamily.setTextColor(Color.WHITE);
                    chkStructuralClass.setTextColor(Color.WHITE);
                    displayTreeBasedOnStateAndRadButton();
                } else {
                    displayTreesBasedOnState(DBContract.COLUMN_FAMILY);
                    chkFamily.setTextColor(Color.BLACK);
                    chkStructuralClass.setTextColor(Color.WHITE);
                }
            }
        });
        chkExpandSort.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    Animation animation = AnimationUtils.loadAnimation(ListActivity.this, R.anim.openradiogroupeffect);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            groupRad.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    groupRad.startAnimation(animation);
                }
                else {
                    Animation animation = AnimationUtils.loadAnimation(ListActivity.this, R.anim.closeradiogroupeffect);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            groupRad.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    groupRad.startAnimation(animation);
                }
            }
        });
        chkFavourite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    displayFavouriteTreeBasedOnCheckedBox();
                    favouriteSelected = true;
                    chkFavourite.setTextColor(Color.BLACK);
                } else {
                    displayTreeBasedOnCheckedBox();
                    favouriteSelected = false;
                    chkFavourite.setTextColor(Color.WHITE);
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
        radEnglishName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    radEnglishName.setTextColor(Color.BLACK);
                    chkExpandSort.setButtonDrawable(R.drawable.icon_en);
                    displayAccordingToRadButton(DBContract.COLUMN_COMMON_NAME);
                }
                else {
                    radEnglishName.setTextColor(Color.WHITE);
                }
            }
        });
        radMaoriName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    radMaoriName.setTextColor(Color.BLACK);
                    chkExpandSort.setButtonDrawable(R.drawable.icon_ma);
                    displayAccordingToRadButton(DBContract.COLUMN_MAORI_NAME);
                }
                else {
                    radMaoriName.setTextColor(Color.WHITE);
                }
            }
        });
        radLatinName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    radLatinName.setTextColor(Color.BLACK);
                    chkExpandSort.setButtonDrawable(R.drawable.icon_la);
                    displayAccordingToRadButton(DBContract.COLUMN_LATIN_NAME);
                }
                else {
                    radLatinName.setTextColor(Color.WHITE);
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
        else if(item.getItemId() == R.id.menuFavourite) {
            Intent intent = new Intent(ListActivity.this, ListActivity.class);
            intent.putExtra("FromHomePage", "homepage");
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
            String maoriName = cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_MAORI_NAME));
            String latinName = cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_LATIN_NAME));
            String family = cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_FAMILY));
            String structuralClass = cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_STRUCTURAL_CLASS));
            String mainPicture = cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_MAIN_PICTURE));
            int isLiked = cursor.getInt(cursor.getColumnIndex(DBContract.COLUMN_LIKED));
            treeList.add(new Tree(Id, commonName, maoriName, latinName, family, structuralClass, mainPicture, isLiked));
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
            String maoriName = cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_MAORI_NAME));
            String latinName = cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_LATIN_NAME));
            String family = cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_FAMILY));
            String structuralClass = cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_STRUCTURAL_CLASS));
            String mainPicture = cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_MAIN_PICTURE));
            int isLiked = cursor.getInt(cursor.getColumnIndex(DBContract.COLUMN_LIKED));
            treeList.add(new Tree(Id, commonName, maoriName, latinName, family, structuralClass, mainPicture, isLiked));
        }
        cursor.close();
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
        if (!chkFamily.isChecked() && !chkStructuralClass.isChecked()) {
            if(radEnglishName.isChecked())
                displayAllFavouriteTreesSortedBy(DBContract.COLUMN_COMMON_NAME);
            else if(radLatinName.isChecked())
                displayAllFavouriteTreesSortedBy(DBContract.COLUMN_LATIN_NAME);
            else if(radMaoriName.isChecked())
                displayAllFavouriteTreesSortedBy(DBContract.COLUMN_MAORI_NAME);
        } else if (chkFamily.isChecked()) {
            displayAllFavouriteTreesSortedBy(DBContract.COLUMN_FAMILY);
        } else if (chkStructuralClass.isChecked()) {
            displayAllFavouriteTreesSortedBy(DBContract.COLUMN_STRUCTURAL_CLASS);
        }
    }

    private void displayTreeBasedOnCheckedBox() {
        if (!chkFamily.isChecked() && !chkStructuralClass.isChecked()) {
            if(radEnglishName.isChecked())
                displayAllTreesSortedBy(DBContract.COLUMN_COMMON_NAME);
            else if(radLatinName.isChecked())
                displayAllTreesSortedBy(DBContract.COLUMN_LATIN_NAME);
            else if(radMaoriName.isChecked())
                displayAllTreesSortedBy(DBContract.COLUMN_MAORI_NAME);
        } else if (chkFamily.isChecked()) {
            displayAllTreesSortedBy(DBContract.COLUMN_FAMILY);
        } else if (chkStructuralClass.isChecked()) {
            displayAllTreesSortedBy(DBContract.COLUMN_STRUCTURAL_CLASS);
        }
    }

    private void displayTreeBasedOnStateAndRadButton() {
        if(radEnglishName.isChecked())
            displayTreesBasedOnState(DBContract.COLUMN_COMMON_NAME);
        else if(radLatinName.isChecked())
            displayTreesBasedOnState(DBContract.COLUMN_LATIN_NAME);
        else if(radMaoriName.isChecked())
            displayTreesBasedOnState(DBContract.COLUMN_MAORI_NAME);
    }
    private void displayAccordingToRadButton(String nameSetting) {
        if (!chkFamily.isChecked() && !chkStructuralClass.isChecked()) {
            if(favouriteSelected) {
                displayAllFavouriteTreesSortedBy(nameSetting);
            }
            else {
                displayAllTreesSortedBy(nameSetting);
            }
        }
    }

    @Override
    protected void onResume() {
        if (favouriteSelected) {
            displayFavouriteTreeBasedOnCheckedBox();
        }
        else
            displayTreeBasedOnCheckedBox();
        listSearch.clearFocus();
        super.onResume();
    }
}
