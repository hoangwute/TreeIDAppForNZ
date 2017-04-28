package aut.bcis.researchdevelopment.treeidfornz;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import aut.bcis.researchdevelopment.adapter.SightingAdapter;
import aut.bcis.researchdevelopment.database.DBContract;
import aut.bcis.researchdevelopment.database.DBInitialization;
import aut.bcis.researchdevelopment.model.Sighting;

public class MySightingActivity extends AppCompatActivity {

    public static ListView lvSightingList;
    private SightingAdapter sightingAdapter;
    public static ArrayList<Object> sightingList;
    private String sightingName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sighting);
        addControls();
        addEvents();
    }

    private void addControls() {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        TextView txtBarTitle = (TextView) myToolbar.findViewById(R.id.toolbar_title);
        txtBarTitle.setText("Sightings");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        txtBarTitle.setTextColor(Color.WHITE);
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(),R.drawable.icon_menuu); // change tool bar icon
        myToolbar.setOverflowIcon(drawable);
        //--------------------------------------------------------------------------------
        Intent intent = getIntent();
        sightingName = intent.getStringExtra("CommonName");
        lvSightingList = (ListView) findViewById(R.id.lvSightingList);
        sightingList = new ArrayList<>();
        sightingAdapter = new SightingAdapter(MySightingActivity.this, sightingList);
        lvSightingList.setAdapter(sightingAdapter);
        displayAllSightings();
    }

    private void addEvents() {
        lvSightingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MySightingActivity.this, SightingInfoActivity.class);
                Sighting sighting = (Sighting) sightingList.get(i);
                intent.putExtra("ID", sighting.getId());
                startActivity(intent);
            }
        });
    }

    private void displayAllSightings() {
        sightingList.clear();
        sightingAdapter = new SightingAdapter(MySightingActivity.this, sightingList); //this line fixes search bug occurred when changes the button
        lvSightingList.setAdapter(sightingAdapter);
        MainActivity.database = openOrCreateDatabase(DBInitialization.DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor = MainActivity.database.rawQuery("SELECT * FROM " + DBContract.TABLE_SIGHTING + " WHERE CommonName = '" + sightingName + "'"
                + " ORDER BY " + DBContract.COLUMN_TIME_STAMP, null);
        while (cursor.moveToNext()) {
            int Id = cursor.getInt(cursor.getColumnIndex(DBContract.COLUMN_SIGHTING_ID));
            String commonName = cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_SIGHTING_COMMON_NAME));
            String latinName = cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_SIGHTING_LATIN_NAME));
            String maoriName = cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_SIGHTING_MAORI_NAME));
            String mainPicture = cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_SIGHTING_PICTURE));
            String location = cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_SIGHTING_LOCATION));
            String date = cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_SIGHTING_DATE));
            sightingList.add(new Sighting(Id, commonName, maoriName, latinName, mainPicture, location, date));
        }
        cursor.close();
        Utility.generateSightingHeaders(sightingList);
        sightingAdapter.notifyDataSetChanged();
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
            Intent intent = new Intent(MySightingActivity.this, MainActivity.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.menuList) {
            Intent intent = new Intent(MySightingActivity.this, ListActivity.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.menuIdentification) {
            Intent intent = new Intent(MySightingActivity.this, IdentificationActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        displayAllSightings();
        super.onResume();
    }
}
