package aut.bcis.researchdevelopment.treeidfornz;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TabHost;

import java.util.ArrayList;

import aut.bcis.researchdevelopment.adapter.TreeAdapter;
import aut.bcis.researchdevelopment.database.Database;
import aut.bcis.researchdevelopment.model.Tree;

public class MainActivity extends AppCompatActivity {
    private TabHost tabHost;
    private SearchView searchView;
    private ListView lvTreeList;
    private TreeAdapter treeAdapter;
    private ArrayList<Tree> treeList;
    public static SQLiteDatabase database = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Database.getDatabaseFromAssets(MainActivity.this);
        addControls();
        addEvents();
        handleDisplayAllTrees();
    }

    private void addControls() {
        tabHost = (TabHost)findViewById(R.id.tabHost);
        tabHost.setup();
        TabHost.TabSpec tab1 = tabHost.newTabSpec("t1");
        tab1.setIndicator("",getResources().getDrawable(R.drawable.like));
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

        lvTreeList = (ListView) findViewById(R.id.lvTreeList);
        treeList = new ArrayList<>();
        treeAdapter = new TreeAdapter(MainActivity.this, R.layout.tree, treeList);
        lvTreeList.setAdapter(treeAdapter);
    }

    private void handleDisplayAllTrees() {
        lvTreeList.setAdapter(treeAdapter);
        treeList.clear();
        database = openOrCreateDatabase(Database.DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor = database.rawQuery("SELECT * FROM Tree ORDER BY CommonName", null);
        while(cursor.moveToNext()) {
            int ID = cursor.getInt(cursor.getColumnIndex("ID"));
            String commonName = cursor.getString(cursor.getColumnIndex("CommonName"));
            String latinName = cursor.getString(cursor.getColumnIndex("LatinName"));
            String family = cursor.getString(cursor.getColumnIndex("Family"));
            String genus = cursor.getString(cursor.getColumnIndex("Genus"));
            int isLiked = cursor.getInt(cursor.getColumnIndex("Liked"));
            String picturePath = cursor.getString(cursor.getColumnIndex("PicturePath"));
            treeList.add(new Tree(commonName, latinName, family, genus, picturePath, isLiked));
        }
        cursor.close();
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
                }
                if(tabId.equalsIgnoreCase("t4")) {
                    searchView.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_find, menu);
        MenuItem menuSearch = menu.findItem(R.id.menuSearch);
        searchView = (SearchView) menuSearch.getActionView();
        searchView.setVisibility(View.INVISIBLE);
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
