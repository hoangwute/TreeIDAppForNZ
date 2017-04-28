package aut.bcis.researchdevelopment.treeidfornz;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import aut.bcis.researchdevelopment.adapter.ImageSwipeAdapter;
import aut.bcis.researchdevelopment.database.DBContract;
import aut.bcis.researchdevelopment.database.DBInitialization;
import aut.bcis.researchdevelopment.model.CustomViewPager;


public class TreeDetailActivity extends AppCompatActivity {
    private TextView txtTreeCommonName, txtTreeLatinName, txtTreeMaoriName, txtTreeFamily, txtImageDescription,
            txtTreeGroup, txtTreeLeaves, txtTreeFlower, txtTreeFruit, txtTreeBark, txtDescription, txtDidYouKnow;
    private CheckBox chkTreeFavourite;
    private String treeCommonName;
    private ImageButton btnTreeFruitStatus, btnTreeFlowerStatus, btnTreePoisonStatus, btnTreeMedicalUse;
    private Button btnAddSighting, btnShowSighting;
    private ViewPager viewPager;
    private ImageSwipeAdapter imageSwipeAdapter;
    private ArrayList<Integer> imageList;
    private final String ATTRIBUTE_COLOR_CODE = "<font color=#4c4c4c>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tree_detail);
        addControls();
        fillTheTreeDetail();
        addEvents();
    }

    private void addControls() {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        TextView txtBarTitle = (TextView) myToolbar.findViewById(R.id.toolbar_title);
        txtBarTitle.setText("Species Details");
        txtBarTitle.setTextColor(Color.WHITE);
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(),R.drawable.icon_menuu); // change tool bar icon
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        myToolbar.setOverflowIcon(drawable);
        Intent intent = getIntent();
        treeCommonName = intent.getStringExtra("Tree");
        txtTreeCommonName = (TextView) findViewById(R.id.txtTreeCommonName);
        txtTreeLatinName = (TextView) findViewById(R.id.txtTreeLatinName);
        txtTreeMaoriName = (TextView) findViewById(R.id.txtTreeMaoriName);
        txtTreeFamily = (TextView) findViewById(R.id.txtTreeFamily);
        txtTreeGroup = (TextView) findViewById(R.id.txtTreeGroup);
        txtTreeLeaves = (TextView) findViewById(R.id.txtTreeLeaves);
        txtTreeFlower = (TextView) findViewById(R.id.txtTreeFlower);
        txtTreeFruit = (TextView) findViewById(R.id.txtTreeFruit);
        txtTreeBark = (TextView) findViewById(R.id.txtTreeBark);
        txtDescription = (TextView) findViewById(R.id.txtDescription);
        txtDidYouKnow = (TextView) findViewById(R.id.txtDidYouKnow);
        chkTreeFavourite = (CheckBox) findViewById(R.id.chkTreeFavourite);
        btnTreeFruitStatus = (ImageButton) findViewById(R.id.btnTreeFruitStatus);
        btnTreeFlowerStatus = (ImageButton) findViewById(R.id.btnTreeFlowerStatus);
        btnTreePoisonStatus = (ImageButton) findViewById(R.id.btnTreePoisonStatus);
        btnTreeMedicalUse = (ImageButton) findViewById(R.id.btnTreeMedicalUse);
        btnAddSighting = (Button) findViewById(R.id.btnAddSighting);
        btnShowSighting = (Button) findViewById(R.id.btnShowSighting);
        btnShowSighting.setText("MY SIGHTINGS (" + Utility.countSightings(treeCommonName) + ")");
        viewPager = (CustomViewPager) findViewById(R.id.viewpager);
        imageList = new ArrayList<>();
        imageList.add(R.drawable.tree_fivefinger); imageList.add(R.drawable.tree_blackbeech); imageList.add(R.drawable.tree_fivefinger); imageList.add(R.drawable.tree_honeysuckle);
        imageSwipeAdapter = new ImageSwipeAdapter(this, imageList);
        viewPager.setAdapter(imageSwipeAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabDots);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void addEvents() {
        btnAddSighting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TreeDetailActivity.this, SaveSightedTreeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("CommonName", treeCommonName);
                bundle.putString("LatinName", Utility.findTreeAttributeValueGivenName(treeCommonName, DBContract.COLUMN_LATIN_NAME));
                bundle.putString("MaoriName", Utility.findTreeAttributeValueGivenName(treeCommonName, DBContract.COLUMN_MAORI_NAME));
                intent.putExtra("Bundle", bundle);
                startActivity(intent);
            }
        });
        btnShowSighting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TreeDetailActivity.this, MySightingActivity.class);
                intent.putExtra("CommonName", treeCommonName);
                startActivity(intent);
            }
        });
        btnTreeMedicalUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TreeDetailActivity.this);
                View dialogView = getLayoutInflater().inflate(R.layout.medicinal_dialog, null);
                ImageButton btnCloseDialog = (ImageButton) dialogView.findViewById(R.id.btnCloseDialog);
                TextView txtAlertDescription = (TextView) dialogView.findViewById(R.id.txtAlertDescription);
                builder.setView(dialogView);
                final AlertDialog diag = builder.create();
                diag.setCanceledOnTouchOutside(false);
                diag.show();
                btnCloseDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        diag.cancel();
                    }
                });
                txtAlertDescription.setText(Utility.findTreeAttributeValueGivenName(treeCommonName, DBContract.COLUMN_MEDICAL_USE));
            }
        });
    }

    private void fillTheTreeDetail() {
        txtTreeCommonName.append(Html.fromHtml(ATTRIBUTE_COLOR_CODE + treeCommonName + "</font>"));
        txtTreeMaoriName.append(Html.fromHtml(ATTRIBUTE_COLOR_CODE + Utility.findTreeAttributeValueGivenName(treeCommonName, DBContract.COLUMN_MAORI_NAME) + "<font>"));
        txtTreeLatinName.append(Html.fromHtml(ATTRIBUTE_COLOR_CODE + Utility.findTreeAttributeValueGivenName(treeCommonName, DBContract.COLUMN_LATIN_NAME) + "<font>"));
        txtTreeFamily.append(Html.fromHtml(ATTRIBUTE_COLOR_CODE + Utility.findTreeAttributeValueGivenName(treeCommonName, DBContract.COLUMN_FAMILY) + "<font>"));
        txtTreeGroup.append(Html.fromHtml(ATTRIBUTE_COLOR_CODE + Utility.findTreeAttributeValueGivenName(treeCommonName, DBContract.COLUMN_STRUCTURAL_CLASS) + "<font>"));
        txtTreeLeaves.append(Html.fromHtml(ATTRIBUTE_COLOR_CODE + Utility.findTreeAttributeValueGivenName(treeCommonName, DBContract.COLUMN_LEAFLET_ARRANGEMENT) + "<font>"));
        txtTreeFlower.append(Html.fromHtml(ATTRIBUTE_COLOR_CODE + Utility.findTreeAttributeValueGivenName(treeCommonName, DBContract.COLUMN_FLOWER_COLOUR) + "<font>"));
        txtTreeFruit.append(Html.fromHtml(ATTRIBUTE_COLOR_CODE + Utility.findTreeAttributeValueGivenName(treeCommonName, DBContract.COLUMN_FRUIT_COLOUR) + ", <font>"));
//                + Html.fromHtml(ATTRIBUTE_COLOR_CODE + Utility.findTreeAttributeValueGivenName(treeCommonName, DBContract.COLUMN_CONE_TYPE) + "<font>")));
        txtTreeBark.append(Html.fromHtml(ATTRIBUTE_COLOR_CODE + Utility.findTreeAttributeValueGivenName(treeCommonName, DBContract.COLUMN_TRUNK_COLOUR) + ", <font>"
                 + Html.fromHtml(ATTRIBUTE_COLOR_CODE + Utility.findTreeAttributeValueGivenName(treeCommonName, DBContract.COLUMN_TRUNK_TEXTURE) + "<font>")));
        txtDescription.setText(Utility.findTreeAttributeValueGivenName(treeCommonName, DBContract.COLUMN_DESCRIPTION));
        txtDidYouKnow.setText(Utility.findTreeAttributeValueGivenName(treeCommonName, DBContract.COLUMN_DID_YOU_KNOW));
        txtImageDescription = (TextView) findViewById(R.id.txtImageDescription);
        txtImageDescription.setText(treeCommonName);
        if(Utility.findTreeAttributeValueGivenName(treeCommonName, DBContract.COLUMN_LIKED).equals("1"))
            chkTreeFavourite.setChecked(true);
        else
            chkTreeFavourite.setChecked(false);
        if(Utility.findTreeAttributeValueGivenName(treeCommonName, DBContract.COLUMN_POISONOUS).equals("1"))
            btnTreePoisonStatus.setVisibility(View.VISIBLE);
        if(isTreeFlowering())
            btnTreeFlowerStatus.setSelected(true);
        else
            btnTreeFlowerStatus.setSelected(false);
        if(isTreeFruiting())
            btnTreeFruitStatus.setSelected(true);
        else
            btnTreeFruitStatus.setSelected(false);
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
            Intent intent = new Intent(TreeDetailActivity.this, MainActivity.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.menuList) {
            Intent intent = new Intent(TreeDetailActivity.this, ListActivity.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.menuIdentification) {
            Intent intent = new Intent(TreeDetailActivity.this, IdentificationActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        btnShowSighting.setText("MY SIGHTINGS (" + Utility.countSightings(treeCommonName) + ")");
        super.onResume();
    }

    private String getSystemMonth() {
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH);
        return String.valueOf(month + 1);
    }

    private boolean isTreeFlowering() {
        ArrayList<String> monthArrayList = new ArrayList<>();
        String givenString = Utility.findTreeAttributeValueGivenName(treeCommonName, DBContract.COLUMN_FLOWERING);
        if(givenString != null) {
            String[] monthArray = givenString.split(",");
            for (int i = 0; i < monthArray.length; i++) {
                monthArrayList.add(monthArray[i]);
            }
            for (int i = 0; i < monthArrayList.size(); i++) {
                if (monthArrayList.get(i).equalsIgnoreCase(getSystemMonth())) {
                    return true;
                }

            }
            return false;
        }
        return false;
    }

    private boolean isTreeFruiting() {
        ArrayList<String> monthArrayList = new ArrayList<>();
        String givenString = Utility.findTreeAttributeValueGivenName(treeCommonName, DBContract.COLUMN_FRUITING);
        if(givenString != null) {
            String[] monthArray = givenString.split(",");
            for (int i = 0; i < monthArray.length; i++) {
                monthArrayList.add(monthArray[i]);
            }
            for (int i = 0; i < monthArrayList.size(); i++) {
                if (monthArrayList.get(i).equalsIgnoreCase(getSystemMonth())) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

}
