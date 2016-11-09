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
        drawAllViews();
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

    private void drawAllViews() {
        String [] words = receivedDynamicQuery.split(" ");
        for(int i = 0; i < words.length; i++) {
            drawViewsBasedOnName(words[i], "'toothed'");
            drawViewsBasedOnName(words[i], "'smooth'");
            drawViewsBasedOnName(words[i], "'alternating'");
            drawViewsBasedOnName(words[i], "'hand-shaped'");
            drawViewsBasedOnName(words[i], "'opposite'");
            if(words[i].equalsIgnoreCase("and")) {
                ImageView arrowImg = new ImageView(WizardResultActivity.this);
                arrowImg.setBackgroundResource(R.drawable.wizard_arrow);
                LinearLayout.LayoutParams arrowLayoutParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                arrowLayoutParams.gravity = Gravity.CENTER;
                arrowImg.setLayoutParams(arrowLayoutParams);
                layoutKey.addView(arrowImg);
            }
            if(words[i].equalsIgnoreCase("or")) {
                ImageView arrowImg = new ImageView(WizardResultActivity.this);
                arrowImg.setBackgroundResource(R.drawable.rsz_1forwardslash);
                LinearLayout.LayoutParams arrowLayoutParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                arrowLayoutParams.gravity = Gravity.CENTER;
                arrowImg.setLayoutParams(arrowLayoutParams);
                layoutKey.addView(arrowImg);
            }
            drawViewsBasedOnName(words[i], "'opposite'");
            drawViewsBasedOnName(words[i], "'alternating'");
            drawViewsBasedOnName(words[i], "'opposite'");
            drawViewsBasedOnName(words[i], "'alternating'");

        }
    }

    public void drawViewsBasedOnName(String word, String keyName) {
        if(word.equalsIgnoreCase(keyName)) {
            ImageView keyImg = new ImageView(WizardResultActivity.this);
            keyImg.setBackground(getDrawable(word));
            layoutKey.addView(keyImg, params);
        }
    }

    private Drawable getDrawable(String key) {
        Drawable draw = null;
        switch(key) {
            case "'toothed'":
                draw = getResources().getDrawable(R.drawable.rsz_wizard_toothed);
                break;
            case "'smooth'":
                draw = getResources().getDrawable(R.drawable.rsz_1wizard_smooth);
                break;
            case "'hand-shaped'":
                draw = getResources().getDrawable(R.drawable.rsz_wizard_handshaped);
                break;
            case "'opposite'":
                draw = getResources().getDrawable(R.drawable.rsz_wizard_opposite);
                break;
            case "'alternating'":
                draw = getResources().getDrawable(R.drawable.rsz_wizard_alternating);
                break;
            case "and":
                draw = getResources().getDrawable(R.drawable.wizard_arrow);
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
