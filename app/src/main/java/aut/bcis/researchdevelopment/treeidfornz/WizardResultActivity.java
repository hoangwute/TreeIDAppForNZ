package aut.bcis.researchdevelopment.treeidfornz;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import aut.bcis.researchdevelopment.adapter.TreeAdapter;
import aut.bcis.researchdevelopment.database.Database;
import aut.bcis.researchdevelopment.model.Tree;

public class WizardResultActivity extends AppCompatActivity {

    private ListView lvClassifiedTreeList;
    private TreeAdapter classifiedTreeAdapter;
    private ArrayList<Object> classifiedTreeList;
    private String receivedDynamicQuery;
    private ViewGroup.LayoutParams params;
    private LinearLayout layoutKey;
    private TextView txtClassifiedResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wizard_result);
        addControls();
        displayChosenKeys();
        displayAllClassifiedTrees();
    }

    private void addControls() {
        lvClassifiedTreeList = (ListView) findViewById(R.id.lvClassifiedTreeList);
        classifiedTreeList = new ArrayList<>();
        classifiedTreeAdapter = new TreeAdapter(WizardResultActivity.this, classifiedTreeList);
        lvClassifiedTreeList.setAdapter(classifiedTreeAdapter);
        layoutKey = (LinearLayout) findViewById(R.id.layoutKey);
        params = new ViewGroup.LayoutParams(60,60);
        txtClassifiedResults = (TextView) findViewById(R.id.txtClassifiedResults);
        Intent intent = getIntent();
        receivedDynamicQuery = intent.getStringExtra("Query");
    }

    private void displayChosenKeys() {
        ArrayList<String> keyList = analyzeReceivedDynamicQuery();
        for(int i = 0; i < keyList.size(); i++) {
            ImageView keyImg = new ImageView(WizardResultActivity.this);
            keyImg.setBackground(getDrawable(keyList.get(i)));
            layoutKey.addView(keyImg, params);
            if(i != keyList.size() - 1) {
                ImageView arrowImg = new ImageView(WizardResultActivity.this);
                arrowImg.setBackgroundResource(R.drawable.wizard_arrow);
                LinearLayout.LayoutParams arrowLayoutParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                arrowLayoutParams.gravity = Gravity.CENTER;
                arrowImg.setLayoutParams(arrowLayoutParams);
                layoutKey.addView(arrowImg);
            }
        }
    }

    private ArrayList<String> analyzeReceivedDynamicQuery() {
        ArrayList<String> chosenKeys = new ArrayList<>();
        if(receivedDynamicQuery.contains("toothed")) {
            chosenKeys.add("toothed");
        }
        if(receivedDynamicQuery.contains("smooth")) {
            chosenKeys.add("smooth");
        }
        if(receivedDynamicQuery.contains("hand-shaped")) {
            chosenKeys.add("hand-shaped");
        }
        if(receivedDynamicQuery.contains("alternating")) {
            chosenKeys.add("alternating");
        }
        if(receivedDynamicQuery.contains("opposite")) {
            chosenKeys.add("opposite");
        }
        return chosenKeys;
    }

    private Drawable getDrawable(String key) {
        Drawable draw = null;
        switch(key) {
            case "toothed":
                draw = getResources().getDrawable(R.drawable.wizard_toothed);
                break;
            case "smooth":
                draw = getResources().getDrawable(R.drawable.wizard_smooth);
                break;
            case "hand-shaped":
                draw = getResources().getDrawable(R.drawable.wizard_handshaped);
                break;
            case "opposite":
                draw = getResources().getDrawable(R.drawable.wizard_opposite);
                break;
            case "alternating":
                draw = getResources().getDrawable(R.drawable.wizard_alternating);
                break;
        }
        return draw;
    }

    private void displayAllClassifiedTrees() {
        classifiedTreeList.clear();
        classifiedTreeAdapter = new TreeAdapter(WizardResultActivity.this, classifiedTreeList); //this line fixes search bug occurred when changes the button
        lvClassifiedTreeList.setAdapter(classifiedTreeAdapter);
        MainActivity.database = openOrCreateDatabase(Database.DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor = MainActivity.database.rawQuery(receivedDynamicQuery, null); //cursor based on the dynamic query statement
        while(cursor.moveToNext()) {
            int Id = cursor.getInt(cursor.getColumnIndex("ID"));
            String commonName = cursor.getString(cursor.getColumnIndex("CommonName"));
            String latinName = cursor.getString(cursor.getColumnIndex("LatinName"));
            String family = cursor.getString(cursor.getColumnIndex("Family"));
            String genus = cursor.getString(cursor.getColumnIndex("Genus"));
            int isLiked = cursor.getInt(cursor.getColumnIndex("Liked"));
            String picturePath = cursor.getString(cursor.getColumnIndex("PicturePath"));
            classifiedTreeList.add(new Tree(Id, commonName, latinName, family, genus, picturePath, isLiked));
        }
        cursor.close();
        classifiedTreeAdapter.notifyDataSetChanged();
    }
}
