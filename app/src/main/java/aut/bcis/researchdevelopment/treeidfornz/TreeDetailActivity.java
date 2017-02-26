package aut.bcis.researchdevelopment.treeidfornz;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import aut.bcis.researchdevelopment.database.DBInitialization;

public class TreeDetailActivity extends AppCompatActivity {
    private TextView txtTreeName;
    private String treeCommonName;
    private ImageButton btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tree_detail);
        addControls();
        addEvents();
    }

    private void addControls() {
        txtTreeName = (TextView) findViewById(R.id.txtTreeName);
        btnAdd = (ImageButton) findViewById(R.id.btnAdd);
        Intent intent = getIntent();
        treeCommonName = intent.getStringExtra("Tree");
        txtTreeName.setText(treeCommonName);
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

}
