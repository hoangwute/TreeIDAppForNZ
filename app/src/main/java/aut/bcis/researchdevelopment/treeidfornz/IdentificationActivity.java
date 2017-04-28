package aut.bcis.researchdevelopment.treeidfornz;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import aut.bcis.researchdevelopment.database.DBContract;

public class IdentificationActivity extends AppCompatActivity {
    public static final int START_CHAR_POSITION_TO_DELETE = 26;
    private RadioGroup radGroupLeafEdge, radGroupLeafArrangement, radGroupLeafShape, radGroupLeafTip, radGroupFlowerColour, radGroupFruitColour, radGroupTrunkTexture;
    private RadioButton radLeafEdgeNotsure, radLeafArrangmentNotsure, radLeafShapeNotsure, radLeafTipNotsure;
    private RadioButton radOpposite, radAlternate, radWhorled;
    private RadioButton radToothed, radEntire;
    private RadioButton radLinear, radOblongOvateElliptic;
    private RadioButton radAcute, radObtuse;
    private RadioButton radFlowerWhite, radFlowerRed, radFlowerGreen, radFlowerPurple, radFlowerPink, radFlowerOrange, radFlowerYellow, radFlowerBlack;
    private RadioButton radFruitPurple, radFruitBlack, radFruitYellow, radFruitRed, radFruitOrange, radFruitBrown, radFruitWhite, radFruitGreen, radFruitPink;
    private RadioButton radRough, radSmooth, radTrunkTextureNotsure;
    private Button btnSubmit;
    private RelativeLayout submitLayout;
    public static String dynamicQuery = "SELECT * FROM " + DBContract.TABLE_TREE + " WHERE (";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identification);
        addControls();
        addEvents();
    }
    private void addControls() {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        TextView txtBarTitle = (TextView) myToolbar.findViewById(R.id.toolbar_title);
        txtBarTitle.setText("Identify a tree");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        txtBarTitle.setTextColor(Color.WHITE);
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(),R.drawable.icon_menuu); // change tool bar icon
        myToolbar.setOverflowIcon(drawable);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //-------------------------------------------------------------------
        radGroupLeafEdge = (RadioGroup) findViewById(R.id.radGroupLeafEdge);
        radGroupLeafArrangement = (RadioGroup) findViewById(R.id.radGroupLeafArrangement);
        radGroupLeafShape = (RadioGroup) findViewById(R.id.radGroupLeafShape);
        radGroupLeafTip = (RadioGroup) findViewById(R.id.radGroupLeafTip);
        radGroupFlowerColour = (RadioGroup) findViewById(R.id.radGroupFlowerColour);
        radGroupFruitColour = (RadioGroup) findViewById(R.id.radGroupFruitColour);
        radGroupTrunkTexture = (RadioGroup) findViewById(R.id.radGroupTrunkTexture);
        radEntire = (RadioButton) findViewById(R.id.radEntire);
        radToothed = (RadioButton) findViewById(R.id.radToothed);
        radAlternate = (RadioButton) findViewById(R.id.radAlternate);
        radOpposite = (RadioButton) findViewById(R.id.radOpposite);
        radWhorled = (RadioButton) findViewById(R.id.radWhorled);
        radLeafEdgeNotsure = (RadioButton) findViewById(R.id.radNotSureLeafEdge);
        radLinear = (RadioButton) findViewById(R.id.radLinear);
        radOblongOvateElliptic = (RadioButton) findViewById(R.id.radOblongOvateElliptic);
        radLeafShapeNotsure = (RadioButton) findViewById(R.id.radNotSureLeafShape);
        radAcute = (RadioButton) findViewById(R.id.radAcute);
        radObtuse = (RadioButton) findViewById(R.id.radObtuse);
        radLeafTipNotsure = (RadioButton) findViewById(R.id.radNotSureLeafTip);
        radFlowerRed = (RadioButton) findViewById(R.id.radFlowerRed);
        radFruitRed = (RadioButton) findViewById(R.id.radFruitRed);
        radFlowerWhite = (RadioButton) findViewById(R.id.radFlowerWhite);
        radFruitWhite = (RadioButton) findViewById(R.id.radFruitWhite);
        radFlowerGreen = (RadioButton) findViewById(R.id.radFlowerGreen);
        radFruitGreen = (RadioButton) findViewById(R.id.radFruitGreen);
        radFlowerBlack = (RadioButton) findViewById(R.id.radFlowerBlack);
        radFruitBlack = (RadioButton) findViewById(R.id.radFruitBlack);
        radFlowerPink = (RadioButton) findViewById(R.id.radFlowerPink);
        radFruitPink = (RadioButton) findViewById(R.id.radFruitPink);
        radFlowerPurple = (RadioButton) findViewById(R.id.radFlowerPurple);
        radFruitPurple = (RadioButton) findViewById(R.id.radFruitPurple);
        radFlowerYellow = (RadioButton) findViewById(R.id.radFlowerYellow);
        radFruitYellow = (RadioButton) findViewById(R.id.radFruitYellow);
        radFlowerOrange = (RadioButton) findViewById(R.id.radFlowerOrange);
        radFruitOrange = (RadioButton) findViewById(R.id.radFruitOrange);
        radFruitBrown = (RadioButton) findViewById(R.id.radFruitBrown);
        radRough = (RadioButton) findViewById(R.id.radRough);
        radSmooth = (RadioButton) findViewById(R.id.radSmooth);
        radTrunkTextureNotsure = (RadioButton) findViewById(R.id.radNotSureTrunkTexture);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        submitLayout = (RelativeLayout) findViewById(R.id.submitLayout);
        submitLayout.bringToFront();

        radGroupLeafArrangement.post(new Runnable() {
            @Override
            public void run() { //the decisive factor for all other views dimension as this is the only group which has 4 views.
                LinearLayout.LayoutParams groupLayoutParams = new LinearLayout.LayoutParams(
                        radGroupLeafArrangement.getWidth(), radAlternate.getWidth());
                groupLayoutParams.setMargins(0,0,0,10);
                LinearLayout.LayoutParams arrangementLayoutParams = new LinearLayout.LayoutParams(
                        radGroupLeafArrangement.getWidth(), radAlternate.getWidth());
                arrangementLayoutParams.setMargins(0,0,16,10); //pixel
                LinearLayout.LayoutParams lastLayoutParams = new LinearLayout.LayoutParams(
                        radGroupLeafArrangement.getWidth(), radAlternate.getWidth());
                lastLayoutParams.setMargins(0,0,16,80); //pixel
                LinearLayout.LayoutParams leftLayoutParams = new LinearLayout.LayoutParams(
                        radAlternate.getWidth(), radAlternate.getWidth());
                leftLayoutParams.setMargins(16,0,0,0);
                LinearLayout.LayoutParams middleLayoutParams = new LinearLayout.LayoutParams(
                        radAlternate.getWidth(), radAlternate.getWidth());
                middleLayoutParams.setMargins(10,0,0,0);
                radGroupLeafArrangement.setLayoutParams(arrangementLayoutParams);
                radGroupLeafEdge.setLayoutParams(groupLayoutParams);
                radGroupLeafTip.setLayoutParams(groupLayoutParams);
                radGroupTrunkTexture.setLayoutParams(lastLayoutParams);
                radEntire.setLayoutParams(leftLayoutParams);
                radToothed.setLayoutParams(middleLayoutParams);
                radLeafEdgeNotsure.setLayoutParams(middleLayoutParams);
                radAlternate.setLayoutParams(leftLayoutParams);
                radOpposite.setLayoutParams(middleLayoutParams);
                radWhorled.setLayoutParams(middleLayoutParams);
                radGroupLeafShape.setLayoutParams(groupLayoutParams);
                radLinear.setLayoutParams(leftLayoutParams);
                radOblongOvateElliptic.setLayoutParams(middleLayoutParams);
                radLeafShapeNotsure.setLayoutParams(middleLayoutParams);
                radAcute.setLayoutParams(leftLayoutParams);
                radObtuse.setLayoutParams(middleLayoutParams);
                radLeafTipNotsure.setLayoutParams(middleLayoutParams);
                radSmooth.setLayoutParams(leftLayoutParams);
                radRough.setLayoutParams(middleLayoutParams);
                radTrunkTextureNotsure.setLayoutParams(middleLayoutParams);
            }
        });
        radGroupFlowerColour.post(new Runnable() {
            @Override
            public void run() { //the decisive factor for all other views dimension as this is the only group which has 4 views.
                LinearLayout.LayoutParams groupLayoutParams = new LinearLayout.LayoutParams(
                        radGroupFlowerColour.getWidth(), radFlowerRed.getWidth());
                radGroupFlowerColour.setLayoutParams(groupLayoutParams);
            }
        });
        radGroupFruitColour.post(new Runnable() {
            @Override
            public void run() { //the decisive factor for all other views dimension as this is the only group which has 4 views.
                LinearLayout.LayoutParams groupLayoutParams = new LinearLayout.LayoutParams(
                        radGroupFruitColour.getWidth(), radFruitRed.getWidth());
                radGroupFruitColour.setLayoutParams(groupLayoutParams);
            }
        });
    }
    private void addEvents() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(generateDynamicQuery() == true) {
                    Intent intent = new Intent(IdentificationActivity.this, IdentificationResultActivity.class);
                    intent.putExtra("Query", dynamicQuery);
                    startActivity(intent);
                }
                else
                    Toast.makeText(IdentificationActivity.this, "Please specify tree traits", Toast.LENGTH_SHORT).show();
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
            Intent intent = new Intent(IdentificationActivity.this, MainActivity.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.menuList) {
            Intent intent = new Intent(IdentificationActivity.this, ListActivity.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.menuIdentification) {
            Intent intent = new Intent(IdentificationActivity.this, IdentificationActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    private boolean generateDynamicQuery() {
        if(radAlternate.isChecked())
            dynamicQuery += " AND " + DBContract.COLUMN_LEAFLET_ARRANGEMENT + " LIKE '%Alternate%'";
        if(radOpposite.isChecked())
            dynamicQuery += " AND " + DBContract.COLUMN_LEAFLET_ARRANGEMENT + " LIKE '%Opposite%'";
        if(radWhorled.isChecked())
            dynamicQuery += " AND " + DBContract.COLUMN_LEAFLET_ARRANGEMENT + " LIKE '%Whorled%'";
        if(radToothed.isChecked())
            dynamicQuery += " AND " + DBContract.COLUMN_LEAF_MARGIN + " LIKE '%Toothed%'";
        if(radEntire.isChecked())
            dynamicQuery += " AND " + DBContract.COLUMN_LEAF_MARGIN + " LIKE '%Entire%'";
        if(radLinear.isChecked())
            dynamicQuery += " AND " + DBContract.COLUMN_LEAFLET_SHAPE + " LIKE '%Linear%'";
        if(radOblongOvateElliptic.isChecked())
            dynamicQuery += " AND (" + DBContract.COLUMN_LEAFLET_SHAPE + " LIKE '%Oblong%' OR LeafletShape LIKE '%Elliptic%' OR LeafletShape LIKE '%Ovate%')";
        if(radAcute.isChecked())
            dynamicQuery += " AND " + DBContract.COLUMN_LEAF_TIP + " LIKE '%Acute%'";
        if(radObtuse.isChecked())
            dynamicQuery += " AND " + DBContract.COLUMN_LEAF_TIP + " LIKE '%Obtuse%'";
        if(radFlowerWhite.isChecked())
            dynamicQuery += " AND " + DBContract.COLUMN_FLOWER_COLOUR + " LIKE '%White%'";
        if(radFlowerPink.isChecked())
            dynamicQuery += " AND " + DBContract.COLUMN_FLOWER_COLOUR + " LIKE '%Pink%'";
        if(radFlowerPurple.isChecked())
            dynamicQuery += " AND " + DBContract.COLUMN_FLOWER_COLOUR + " LIKE '%Purple%'";
        if(radFlowerRed.isChecked())
            dynamicQuery += " AND " + DBContract.COLUMN_FLOWER_COLOUR + " LIKE '%Red%'";
        if(radFlowerGreen.isChecked())
            dynamicQuery += " AND " + DBContract.COLUMN_FLOWER_COLOUR + " LIKE '%Green%'";
        if(radFlowerBlack.isChecked())
            dynamicQuery += " AND " + DBContract.COLUMN_FLOWER_COLOUR + " LIKE '%Black%'";
        if(radFlowerYellow.isChecked())
            dynamicQuery += " AND " + DBContract.COLUMN_FLOWER_COLOUR + " LIKE '%Yellow%'";
        if(radFlowerOrange.isChecked())
            dynamicQuery += " AND " + DBContract.COLUMN_FLOWER_COLOUR + " LIKE '%Orange%'";
        if(radFruitWhite.isChecked())
            dynamicQuery += " AND " + DBContract.COLUMN_FRUIT_COLOUR + " LIKE '%White%'";
        if(radFruitPink.isChecked())
            dynamicQuery += " AND " + DBContract.COLUMN_FRUIT_COLOUR + " LIKE '%Pink%'";
        if(radFruitPurple.isChecked())
            dynamicQuery += " AND " + DBContract.COLUMN_FRUIT_COLOUR + " LIKE '%Purple%'";
        if(radFruitRed.isChecked())
            dynamicQuery += " AND " + DBContract.COLUMN_FRUIT_COLOUR + " LIKE '%Red%'";
        if(radFruitGreen.isChecked())
            dynamicQuery += " AND " + DBContract.COLUMN_FRUIT_COLOUR + " LIKE '%Green%'";
        if(radFruitBlack.isChecked())
            dynamicQuery += " AND " + DBContract.COLUMN_FRUIT_COLOUR + " LIKE '%Black%'";
        if(radFruitYellow.isChecked())
            dynamicQuery += " AND " + DBContract.COLUMN_FRUIT_COLOUR + " LIKE '%Yellow%'";
        if(radFruitOrange.isChecked())
            dynamicQuery += " AND " + DBContract.COLUMN_FRUIT_COLOUR + " LIKE '%Orange%'";
        if(radFruitBrown.isChecked())
            dynamicQuery += " AND " + DBContract.COLUMN_FRUIT_COLOUR + " LIKE '%Brown%'";
        if(radRough.isChecked())
            dynamicQuery += " AND " + DBContract.COLUMN_TRUNK_TEXTURE + " LIKE '%White%'";
        if(radSmooth.isChecked())
            dynamicQuery += " AND " + DBContract.COLUMN_TRUNK_TEXTURE + " LIKE '%Smooth%'";

        if (dynamicQuery.length() > START_CHAR_POSITION_TO_DELETE) { // > 26 to make sure there is criteria selected.
            dynamicQuery = dynamicQuery.replaceFirst(" AND ", "").concat(")");
            Toast.makeText(IdentificationActivity.this, dynamicQuery, Toast.LENGTH_LONG).show();
            return true;
        } else { //no button selected will give a random query to avoid crash cause of substring.
            return false; //a random query which will give no result which is what we want since no button is selected.
        }

    }


    @Override
    protected void onResume() {
        dynamicQuery = "SELECT * FROM " + DBContract.TABLE_TREE + " WHERE ("; //reset the dynamic query when back to the activity
        super.onResume();
    }
}
