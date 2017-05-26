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
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import aut.bcis.researchdevelopment.database.DBContract;

public class IdentificationActivity extends AppCompatActivity {
    public static final int START_CHAR_POSITION_TO_DELETE = 26;
    public static final int START_CHAR_COUNT_POSITION_TO_DELETE = 34;
    private LinearLayout radGroupLeafEdge, radGroupLeafArrangement, radGroupLeafShape, radGroupLeafTip, radGroupFlowerColour, radGroupFruitColour, radGroupTrunkTexture;
    private CheckBox radLeafArrangementNotsure, radLeafShapeNotsure, radLeafTipNotsure;
    private CheckBox radOpposite, radAlternate, radWhorled;
    private CheckBox radToothed, radMarginSmooth, radLeafEdgeNotsure;
    private CheckBox radLinear, radOblongOvateElliptic;
    private CheckBox radAcute, radObtuse;
    private CheckBox radFlowerWhite, radFlowerRed, radFlowerGreen, radFlowerPurple, radFlowerPink, radFlowerOrange, radFlowerYellow, radFlowerBlack, radFlowerNA;
    private CheckBox radFruitPurple, radFruitBlack, radFruitYellow, radFruitRed, radFruitOrange, radFruitBrown, radFruitWhite, radFruitGreen, radFruitPink, radFruitNA;
    private CheckBox radRough, radSmooth, radTrunkTextureNotsure;
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
        radGroupLeafEdge = (LinearLayout) findViewById(R.id.radGroupLeafEdge);
        radGroupLeafArrangement = (LinearLayout) findViewById(R.id.radGroupLeafArrangement);
        radGroupLeafShape = (LinearLayout) findViewById(R.id.radGroupLeafShape);
        radGroupLeafTip = (LinearLayout) findViewById(R.id.radGroupLeafTip);
        radGroupFlowerColour = (LinearLayout) findViewById(R.id.radGroupFlowerColour);
        radGroupFruitColour = (LinearLayout) findViewById(R.id.radGroupFruitColour);
        radGroupTrunkTexture = (LinearLayout) findViewById(R.id.radGroupTrunkTexture);
        radMarginSmooth = (CheckBox) findViewById(R.id.radEntire);
        radToothed = (CheckBox) findViewById(R.id.radToothed);
        radAlternate = (CheckBox) findViewById(R.id.radAlternate);
        radOpposite = (CheckBox) findViewById(R.id.radOpposite);
        radWhorled = (CheckBox) findViewById(R.id.radWhorled);
        radLeafEdgeNotsure = (CheckBox) findViewById(R.id.radNotSureLeafEdge);
        radLinear = (CheckBox) findViewById(R.id.radLinear);
        radOblongOvateElliptic = (CheckBox) findViewById(R.id.radOblongOvateElliptic);
        radLeafShapeNotsure = (CheckBox) findViewById(R.id.radNotSureLeafShape);
        radLeafArrangementNotsure = (CheckBox) findViewById(R.id.radNotSureLeafArrangement);
        radAcute = (CheckBox) findViewById(R.id.radAcute);
        radObtuse = (CheckBox) findViewById(R.id.radObtuse);
        radLeafTipNotsure = (CheckBox) findViewById(R.id.radNotSureLeafTip);
        radFlowerRed = (CheckBox) findViewById(R.id.radFlowerRed);
        radFruitRed = (CheckBox) findViewById(R.id.radFruitRed);
        radFlowerWhite = (CheckBox) findViewById(R.id.radFlowerWhite);
        radFruitWhite = (CheckBox) findViewById(R.id.radFruitWhite);
        radFlowerGreen = (CheckBox) findViewById(R.id.radFlowerGreen);
        radFruitGreen = (CheckBox) findViewById(R.id.radFruitGreen);
        radFlowerBlack = (CheckBox) findViewById(R.id.radFlowerBlack);
        radFruitBlack = (CheckBox) findViewById(R.id.radFruitBlack);
        radFlowerPink = (CheckBox) findViewById(R.id.radFlowerPink);
        radFruitPink = (CheckBox) findViewById(R.id.radFruitPink);
        radFlowerPurple = (CheckBox) findViewById(R.id.radFlowerPurple);
        radFruitPurple = (CheckBox) findViewById(R.id.radFruitPurple);
        radFlowerYellow = (CheckBox) findViewById(R.id.radFlowerYellow);
        radFruitYellow = (CheckBox) findViewById(R.id.radFruitYellow);
        radFlowerOrange = (CheckBox) findViewById(R.id.radFlowerOrange);
        radFruitOrange = (CheckBox) findViewById(R.id.radFruitOrange);
        radFruitBrown = (CheckBox) findViewById(R.id.radFruitBrown);
        radFlowerNA = (CheckBox) findViewById(R.id.radFlowerNa);
        radFruitNA = (CheckBox) findViewById(R.id.radFruitNa);
        radRough = (CheckBox) findViewById(R.id.radRough);
        radSmooth = (CheckBox) findViewById(R.id.radSmooth);
        radTrunkTextureNotsure = (CheckBox) findViewById(R.id.radNotSureTrunkTexture);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setText("SUBMIT [" + Utility.countAllTrees() + "]");
        submitLayout = (RelativeLayout) findViewById(R.id.submitLayout);
        submitLayout.bringToFront();

        radGroupLeafArrangement.post(new Runnable() {
            @Override
            public void run() { //the decisive factor for all other views dimension as this is the only group which has 4 views.
                LinearLayout.LayoutParams groupLayoutParams = new LinearLayout.LayoutParams(
                        (radGroupLeafArrangement.getWidth() * 3 / 4) + 16 , radAlternate.getWidth());
                groupLayoutParams.setMargins(0,0,0,10);
                LinearLayout.LayoutParams arrangementLayoutParams = new LinearLayout.LayoutParams(
                        radGroupLeafArrangement.getWidth(), radAlternate.getWidth());
                arrangementLayoutParams.setMargins(0,0,0,10); //pixel
                LinearLayout.LayoutParams lastLayoutParams = new LinearLayout.LayoutParams(
                        (radGroupLeafArrangement.getWidth() * 3 / 4) + 16, radAlternate.getWidth());
                lastLayoutParams.setMargins(0,0,16,btnSubmit.getHeight() + 30); //pixel
                LinearLayout.LayoutParams leftLayoutParams = new LinearLayout.LayoutParams(
                        radAlternate.getWidth(), radAlternate.getWidth());
                leftLayoutParams.setMargins(20,0,0,0);
                LinearLayout.LayoutParams middleLayoutParams = new LinearLayout.LayoutParams(
                        radAlternate.getWidth(), radAlternate.getWidth());
                middleLayoutParams.setMargins(10,0,0,0);
                radGroupLeafArrangement.setLayoutParams(arrangementLayoutParams);
                radGroupLeafEdge.setLayoutParams(groupLayoutParams);
                radGroupLeafTip.setLayoutParams(groupLayoutParams);
                radGroupLeafShape.setLayoutParams(groupLayoutParams);
                radGroupTrunkTexture.setLayoutParams(lastLayoutParams);
                Toast.makeText(IdentificationActivity.this, radLeafArrangementNotsure.getWidth() + "", Toast.LENGTH_SHORT).show();
                Toast.makeText(IdentificationActivity.this, radAlternate.getWidth() + "", Toast.LENGTH_SHORT).show();
                Toast.makeText(IdentificationActivity.this, radOpposite.getWidth() + "", Toast.LENGTH_SHORT).show();
                Toast.makeText(IdentificationActivity.this, radWhorled.getWidth() + "", Toast.LENGTH_SHORT).show();


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
                else {
                    Intent intent = new Intent(IdentificationActivity.this, IdentificationResultActivity.class);
                    intent.putExtra("Query", "SELECT * FROM " + DBContract.TABLE_TREE);
                    startActivity(intent);
                }
            }
        });

        radToothed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(radMarginSmooth.isChecked() || radLeafEdgeNotsure.isChecked()) {
                    radMarginSmooth.setChecked(false);
                    radLeafEdgeNotsure.setChecked(false);
                }
                updateCountNumber();
            }
        });
        radAlternate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(radOpposite.isChecked() || radWhorled.isChecked() || radLeafArrangementNotsure.isChecked()) {
                    radOpposite.setChecked(false);
                    radWhorled.setChecked(false);
                    radLeafArrangementNotsure.setChecked(false);
                }
                updateCountNumber();
            }
        });
        radSmooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(radRough.isChecked() || radTrunkTextureNotsure.isChecked()) {
                    radRough.setChecked(false);
                    radTrunkTextureNotsure.setChecked(false);
                }
                updateCountNumber();
            }
        });
        radOpposite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(radAlternate.isChecked() || radWhorled.isChecked() || radLeafArrangementNotsure.isChecked()) {
                    radAlternate.setChecked(false);
                    radWhorled.setChecked(false);
                    radLeafArrangementNotsure.setChecked(false);
                }
                updateCountNumber();
            }
        });
        radWhorled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(radAlternate.isChecked() || radOpposite.isChecked() || radLeafArrangementNotsure.isChecked()) {
                    radAlternate.setChecked(false);
                    radOpposite.setChecked(false);
                    radLeafArrangementNotsure.setChecked(false);
                }
                updateCountNumber();
            }
        });
        radLeafEdgeNotsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(radMarginSmooth.isChecked() || radToothed.isChecked()) {
                    radMarginSmooth.setChecked(false);
                    radToothed.setChecked(false);
                }
                updateCountNumber();
            }
        });
        radLeafArrangementNotsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(radAlternate.isChecked() || radOpposite.isChecked() || radWhorled.isChecked()) {
                    radAlternate.setChecked(false);
                    radOpposite.setChecked(false);
                    radWhorled.setChecked(false);
                }
                updateCountNumber();
            }
        });
        radLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(radOblongOvateElliptic.isChecked() || radLeafShapeNotsure.isChecked()) {
                    radOblongOvateElliptic.setChecked(false);
                    radLeafShapeNotsure.setChecked(false);
                }
                updateCountNumber();
            }
        });
        radOblongOvateElliptic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(radLinear.isChecked() || radLeafShapeNotsure.isChecked()) {
                    radLinear.setChecked(false);
                    radLeafShapeNotsure.setChecked(false);
                }
                updateCountNumber();
            }
        });
        radLeafShapeNotsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(radLinear.isChecked() || radOblongOvateElliptic.isChecked()) {
                    radLinear.setChecked(false);
                    radOblongOvateElliptic.setChecked(false);
                }
                updateCountNumber();
            }
        });
        radAcute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(radObtuse.isChecked() || radLeafTipNotsure.isChecked()) {
                    radObtuse.setChecked(false);
                    radLeafTipNotsure.setChecked(false);
                }
                updateCountNumber();
            }
        });
        radObtuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(radAcute.isChecked() || radLeafTipNotsure.isChecked()) {
                    radAcute.setChecked(false);
                    radLeafTipNotsure.setChecked(false);
                }
                updateCountNumber();
            }
        });
        radLeafTipNotsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(radAcute.isChecked() || radObtuse.isChecked()) {
                    radAcute.setChecked(false);
                    radObtuse.setChecked(false);
                }
                updateCountNumber();
            }
        });
        radMarginSmooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(radToothed.isChecked() || radLeafEdgeNotsure.isChecked()) {
                    radToothed.setChecked(false);
                    radLeafEdgeNotsure.setChecked(false);
                }
                updateCountNumber();
            }
        });
        radRough.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(radSmooth.isChecked() || radTrunkTextureNotsure.isChecked()) {
                    radSmooth.setChecked(false);
                    radTrunkTextureNotsure.setChecked(false);
                }
                updateCountNumber();
            }
        });
        radTrunkTextureNotsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(radSmooth.isChecked() || radRough.isChecked()) {
                    radSmooth.setChecked(false);
                    radRough.setChecked(false);
                }
                updateCountNumber();
            }
        });
        radFlowerWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCountNumber();
            }
        });
        radFlowerYellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCountNumber();
            }
        });
        radFlowerOrange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCountNumber();
            }
        });
        radFlowerPink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCountNumber();
            }
        });
        radFlowerRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCountNumber();
            }
        });
        radFlowerGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCountNumber();
            }
        });
        radFlowerPurple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCountNumber();
            }
        });
        radFlowerBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCountNumber();
            }
        });
        radFlowerNA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCountNumber();
            }
        });
        radFruitWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCountNumber();
            }
        });
        radFruitYellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCountNumber();
            }
        });
        radFruitOrange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCountNumber();
            }
        });
        radFruitPink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCountNumber();
            }
        });
        radFruitRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCountNumber();
            }
        });
        radFruitGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCountNumber();
            }
        });
        radFruitPurple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCountNumber();
            }
        });
        radFruitBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCountNumber();
            }
        });
        radFruitBrown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCountNumber();
            }
        });
        radFruitNA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCountNumber();
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
        if(radMarginSmooth.isChecked())
            dynamicQuery += " AND " + DBContract.COLUMN_LEAF_MARGIN + " LIKE '%Smooth%'";
        if(radLinear.isChecked())
            dynamicQuery += " AND " + DBContract.COLUMN_GROUP_LEAF_SHAPE + " LIKE '%Linear%'";
        if(radOblongOvateElliptic.isChecked())
            dynamicQuery += " AND (" + DBContract.COLUMN_GROUP_LEAF_SHAPE + " LIKE '%Elliptic%')";
        if(radAcute.isChecked())
            dynamicQuery += " AND " + DBContract.COLUMN_LEAF_TIP + " LIKE '%Acute%'";
        if(radObtuse.isChecked())
            dynamicQuery += " AND " + DBContract.COLUMN_LEAF_TIP + " LIKE '%Obtuse%'";
        if(radRough.isChecked())
            dynamicQuery += " AND " + DBContract.COLUMN_TRUNK_TEXTURE + " LIKE '%Rough%'";
        if(radSmooth.isChecked())
            dynamicQuery += " AND " + DBContract.COLUMN_TRUNK_TEXTURE + " LIKE '%Smooth%'";
        if(radFlowerWhite.isChecked() || radFlowerYellow.isChecked() || radFlowerOrange.isChecked() || radFlowerPink.isChecked() || radFlowerRed.isChecked()
                || radFlowerGreen.isChecked() || radFlowerPurple.isChecked() || radFlowerBlack.isChecked() || radFlowerNA.isChecked())
            dynamicQuery += handleCountFlowerColour();
        if(radFruitWhite.isChecked()
                || radFruitYellow.isChecked() || radFruitOrange.isChecked() || radFruitPink.isChecked() || radFruitRed.isChecked() || radFruitGreen.isChecked()
                || radFruitPurple.isChecked() || radFruitBlack.isChecked() || radFruitBrown.isChecked() || radFruitNA.isChecked())
            dynamicQuery += handleCountFruitColour();
        if (dynamicQuery.length() > START_CHAR_POSITION_TO_DELETE) { // > 26 to make sure there is a criteria selected.
            dynamicQuery = dynamicQuery.replaceFirst(" AND ", "").concat(")");
//            Toast.makeText(IdentificationActivity.this, dynamicQuery, Toast.LENGTH_LONG).show();
            return true;
        } else { //no button selected will give a random query to avoid crash cause of substring.
            return false; //a random query which will give no result which is what we want since no button is selected.
        }
    }

    private String generateDynamicCountQuery() {
        String dynamicCountQuery = "SELECT COUNT(*) FROM " + DBContract.TABLE_TREE + " WHERE (";
        if(radAlternate.isChecked())
            dynamicCountQuery += " AND " + DBContract.COLUMN_LEAFLET_ARRANGEMENT + " LIKE '%Alternate%'";
        if(radOpposite.isChecked())
            dynamicCountQuery += " AND " + DBContract.COLUMN_LEAFLET_ARRANGEMENT + " LIKE '%Opposite%'";
        if(radWhorled.isChecked())
            dynamicCountQuery += " AND " + DBContract.COLUMN_LEAFLET_ARRANGEMENT + " LIKE '%Whorled%'";
        if(radToothed.isChecked())
            dynamicCountQuery += " AND " + DBContract.COLUMN_LEAF_MARGIN + " LIKE '%Toothed%'";
        if(radMarginSmooth.isChecked())
            dynamicCountQuery += " AND " + DBContract.COLUMN_LEAF_MARGIN + " LIKE '%Smooth%'";
        if(radLinear.isChecked())
            dynamicCountQuery += " AND " + DBContract.COLUMN_GROUP_LEAF_SHAPE + " LIKE '%Linear%'";
        if(radOblongOvateElliptic.isChecked())
            dynamicCountQuery += " AND (" + DBContract.COLUMN_GROUP_LEAF_SHAPE + " LIKE '%Elliptic%')";
        if(radAcute.isChecked())
            dynamicCountQuery += " AND " + DBContract.COLUMN_LEAF_TIP + " LIKE '%Acute%'";
        if(radObtuse.isChecked())
            dynamicCountQuery += " AND " + DBContract.COLUMN_LEAF_TIP + " LIKE '%Obtuse%'";
        if(radRough.isChecked())
            dynamicCountQuery += " AND " + DBContract.COLUMN_TRUNK_TEXTURE + " LIKE '%Rough%'";
        if(radSmooth.isChecked())
            dynamicCountQuery += " AND " + DBContract.COLUMN_TRUNK_TEXTURE + " LIKE '%Smooth%'";
        if(radFlowerWhite.isChecked() || radFlowerYellow.isChecked() || radFlowerOrange.isChecked() || radFlowerPink.isChecked() || radFlowerRed.isChecked()
                || radFlowerGreen.isChecked() || radFlowerPurple.isChecked() || radFlowerBlack.isChecked() || radFlowerNA.isChecked())
            dynamicCountQuery += handleCountFlowerColour();
        if(radFruitWhite.isChecked()
                || radFruitYellow.isChecked() || radFruitOrange.isChecked() || radFruitPink.isChecked() || radFruitRed.isChecked() || radFruitGreen.isChecked()
                || radFruitPurple.isChecked() || radFruitBlack.isChecked() || radFruitBrown.isChecked() || radFruitNA.isChecked())
            dynamicCountQuery += handleCountFruitColour();
        dynamicCountQuery = dynamicCountQuery.replaceFirst(" AND ", "").concat(")");
        return dynamicCountQuery;
    }

    private void updateCountNumber() {
        if(generateDynamicCountQuery().length() > START_CHAR_COUNT_POSITION_TO_DELETE) {
            btnSubmit.setText(Utility.countTreeFound(generateDynamicCountQuery()) + " matches");
        }
        else
            btnSubmit.setText(Utility.countAllTrees() + " matches");
    }

    private String handleCountFlowerColour() {
        String flowerQuery = "";
        if(radMarginSmooth.isChecked() || radToothed.isChecked() || radAlternate.isChecked() || radOpposite.isChecked() || radWhorled.isChecked() ||
                radLinear.isChecked() || radOblongOvateElliptic.isChecked() || radAcute.isChecked() || radObtuse.isChecked() || radFruitWhite.isChecked()
                || radFruitYellow.isChecked() || radFruitOrange.isChecked() || radFruitPink.isChecked() || radFruitRed.isChecked() || radFruitGreen.isChecked()
                || radFruitPurple.isChecked() || radFruitBlack.isChecked() || radFruitBrown.isChecked() || radFruitNA.isChecked() || radSmooth.isChecked() || radRough.isChecked())
            flowerQuery = " AND (";
        else
            flowerQuery = "(";
        if(radFlowerWhite.isChecked()) {
            flowerQuery += " OR " + DBContract.COLUMN_FLOWER_COLOUR + " LIKE '%White%'";
        }
        if(radFlowerYellow.isChecked()) {
            flowerQuery += " OR " + DBContract.COLUMN_FLOWER_COLOUR + " LIKE '%Yellow%'";
        }
        if(radFlowerOrange.isChecked()) {
            flowerQuery += " OR " + DBContract.COLUMN_FLOWER_COLOUR + " LIKE '%Orange%'";
        }
        if(radFlowerPink.isChecked()) {
            flowerQuery += " OR " + DBContract.COLUMN_FLOWER_COLOUR + " LIKE '%Pink%'";
        }
        if(radFlowerRed.isChecked()) {
            flowerQuery += " OR " + DBContract.COLUMN_FLOWER_COLOUR + " LIKE '%Red%'";
        }
        if(radFlowerGreen.isChecked()) {
            flowerQuery += " OR " + DBContract.COLUMN_FLOWER_COLOUR + " LIKE '%Green%'";
        }
        if(radFlowerPurple.isChecked()) {
            flowerQuery += " OR " + DBContract.COLUMN_FLOWER_COLOUR + " LIKE '%Purple%'";
        }
        if(radFlowerBlack.isChecked()) {
            flowerQuery += " OR " + DBContract.COLUMN_FLOWER_COLOUR + " LIKE '%Black%'";
        }
        if(radFlowerNA.isChecked()) {
            flowerQuery += " OR " + DBContract.COLUMN_FLOWER_COLOUR + " LIKE '%N/A%'";
        }
        flowerQuery = flowerQuery.replaceFirst(" OR " , "").concat(")");
        return flowerQuery;
    }

    private String handleCountFruitColour() {
        String fruitQuery = "";
        if(radMarginSmooth.isChecked() || radToothed.isChecked() || radAlternate.isChecked() || radOpposite.isChecked() || radWhorled.isChecked() ||
                radLinear.isChecked() || radOblongOvateElliptic.isChecked() || radAcute.isChecked() || radObtuse.isChecked() || radFlowerWhite.isChecked()
                || radFlowerYellow.isChecked() || radFlowerOrange.isChecked() || radFlowerPink.isChecked() || radFlowerRed.isChecked() || radFlowerGreen.isChecked()
                || radFlowerPurple.isChecked() || radFlowerBlack.isChecked() || radFlowerNA.isChecked() || radSmooth.isChecked() || radRough.isChecked())
            fruitQuery = " AND (";
        else
            fruitQuery = "(";
        if(radFruitWhite.isChecked()) {
            fruitQuery += " OR " + DBContract.COLUMN_FRUIT_COLOUR + " LIKE '%White%'";
        }
        if(radFruitYellow.isChecked()) {
            fruitQuery += " OR " + DBContract.COLUMN_FRUIT_COLOUR + " LIKE '%Yellow%'";
        }
        if(radFruitOrange.isChecked()) {
            fruitQuery += " OR " + DBContract.COLUMN_FRUIT_COLOUR + " LIKE '%Orange%'";
        }
        if(radFruitPink.isChecked()) {
            fruitQuery += " OR " + DBContract.COLUMN_FRUIT_COLOUR + " LIKE '%Pink%'";
        }
        if(radFruitRed.isChecked()) {
            fruitQuery += " OR " + DBContract.COLUMN_FRUIT_COLOUR + " LIKE '%Red%'";
        }
        if(radFruitGreen.isChecked()) {
            fruitQuery += " OR " + DBContract.COLUMN_FRUIT_COLOUR + " LIKE '%Green%'";
        }
        if(radFruitPurple.isChecked()) {
            fruitQuery += " OR " + DBContract.COLUMN_FRUIT_COLOUR + " LIKE '%Purple%'";
        }
        if(radFruitBlack.isChecked()) {
            fruitQuery += " OR " + DBContract.COLUMN_FRUIT_COLOUR + " LIKE '%Black%'";
        }
        if(radFruitBrown.isChecked()) {
            fruitQuery += " OR " + DBContract.COLUMN_FRUIT_COLOUR + " LIKE '%Brown%'";
        }
        if(radFruitNA.isChecked()) {
            fruitQuery += " OR " + DBContract.COLUMN_FRUIT_COLOUR + " LIKE '%N/A%'";
        }
        fruitQuery = fruitQuery.replaceFirst(" OR " , "").concat(")");
        return fruitQuery;
    }

    @Override
    protected void onResume() {
        dynamicQuery = "SELECT * FROM " + DBContract.TABLE_TREE + " WHERE ("; //reset the dynamic query when back to the activity
        super.onResume();
    }
}
