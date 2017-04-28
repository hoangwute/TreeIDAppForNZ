package aut.bcis.researchdevelopment.treeidfornz;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.List;

import aut.bcis.researchdevelopment.adapter.TreeAdapter;
import aut.bcis.researchdevelopment.database.DBContract;
import aut.bcis.researchdevelopment.database.DBInitialization;
import aut.bcis.researchdevelopment.model.FilterEntry;
import aut.bcis.researchdevelopment.model.Tree;

public class MainActivity extends AppCompatActivity {

    public static SQLiteDatabase database = null;
    private ProgressDialog loadDatabaseDialog;
    private Button btnIdentifyATree;
    private Button btnAToZOfTree;
    private Button btnMyFavourites;
    private RelativeLayout favouriteLayout;
    private ImageButton btnFavourite;

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

    private void addEvents() {
        btnAToZOfTree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                startActivity(intent);
            }
        });
        btnIdentifyATree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, IdentificationActivity.class);
                startActivity(intent);
            }
        });
        btnMyFavourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                intent.putExtra("FromHomePage", "homepage");
                startActivity(intent);
            }
        });
        btnFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                intent.putExtra("FromHomePage", "homepage");
                startActivity(intent);
            }
        });
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
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        final int height = size.y;
        favouriteLayout = (RelativeLayout) findViewById(R.id.favouriteLayout);
        btnIdentifyATree = (Button) findViewById(R.id.btnIdentifyATree);
        btnAToZOfTree = (Button) findViewById(R.id.btnAToZOfTree);
        btnMyFavourites = (Button) findViewById(R.id.btnMyFavourites);
        btnFavourite = (ImageButton) findViewById(R.id.btnFavourite);
        btnIdentifyATree.post(new Runnable() {
            @Override
            public void run() { //the decisive factor for all other views dimension as this is the only group which has 4 views.
                RelativeLayout.LayoutParams groupLayoutParams = new RelativeLayout.LayoutParams(
                        btnIdentifyATree.getWidth(), btnIdentifyATree.getHeight());
                groupLayoutParams.setMargins(16, height/2-(height/10), 16, 0);
                btnIdentifyATree.setLayoutParams(groupLayoutParams);
            }
        });
        favouriteLayout.post(new Runnable() {
            @Override
            public void run() { //the decisive factor for all other views dimension as this is the only group which has 4 views.
                RelativeLayout.LayoutParams groupLayoutParams = new RelativeLayout.LayoutParams(
                        favouriteLayout.getWidth(), favouriteLayout.getHeight());
                groupLayoutParams.setMargins(btnFavourite.getWidth(), height-(height/7), 0, 0);
                favouriteLayout.setLayoutParams(groupLayoutParams);
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

}
