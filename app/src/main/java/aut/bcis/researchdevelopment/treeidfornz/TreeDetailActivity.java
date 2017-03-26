package aut.bcis.researchdevelopment.treeidfornz;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

import aut.bcis.researchdevelopment.adapter.ImageSwipeAdapter;
import aut.bcis.researchdevelopment.database.DBContract;
import aut.bcis.researchdevelopment.database.DBInitialization;

import static aut.bcis.researchdevelopment.treeidfornz.TabsFragment.backed;

public class TreeDetailActivity extends AppCompatActivity {
    private TextView txtTreeCommonName, txtTreeLatinName, txtTreeFamily;
    private String treeCommonName;
    private ImageButton btnAdd;
    private ViewPager viewPager;
    private ImageSwipeAdapter imageSwipeAdapter;
    private ArrayList<Integer> imageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tree_detail);
        addControls();
        addEvents();
    }

    private void addControls() {
        txtTreeCommonName = (TextView) findViewById(R.id.txtTreeCommonName);
        txtTreeLatinName = (TextView) findViewById(R.id.txtTreeLatinName);
        txtTreeFamily = (TextView) findViewById(R.id.txtTreeFamily);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        imageList = new ArrayList<>();
        imageList.add(R.drawable.tree_akeake); imageList.add(R.drawable.tree_blackbeech); imageList.add(R.drawable.tree_fivefinger); imageList.add(R.drawable.tree_honeysuckle);
        imageSwipeAdapter = new ImageSwipeAdapter(this, imageList);
        viewPager.setAdapter(imageSwipeAdapter);
        btnAdd = (ImageButton) findViewById(R.id.btnAdd);
        Intent intent = getIntent();
        treeCommonName = intent.getStringExtra("Tree");
        txtTreeCommonName.setText(treeCommonName);
        txtTreeLatinName.setText(Utility.findTreeAttributeValueGivenName(treeCommonName, DBContract.COLUMN_LATIN_NAME));
        txtTreeFamily.setText("Tree Family: " + Utility.findTreeAttributeValueGivenName(treeCommonName, DBContract.COLUMN_FAMILY));
    }

    private String getTreeAttributesBasedOnCommonName(String chosenAttribute) {
        String attribute = "";
        MainActivity.database = openOrCreateDatabase(DBInitialization.DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor = MainActivity.database.rawQuery("SELECT " + chosenAttribute + " FROM Tree Where CommonName = '" + treeCommonName + "'", null);
        while(cursor.moveToNext()) {
            attribute = cursor.getString(cursor.getColumnIndex(chosenAttribute));
        }
        cursor.close();
        return attribute;
    }

    private void addEvents() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TreeDetailActivity.this, SaveSightedTreeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("CommonName", treeCommonName);
                bundle.putString("LatinName", getTreeAttributesBasedOnCommonName("LatinName"));
                intent.putExtra("Bundle", bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        if(backed == true)
            finish();
        super.onResume();
    }

}
