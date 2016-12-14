package aut.bcis.researchdevelopment.treeidfornz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
import android.widget.TextView;

public class TreeDetailActivity extends AppCompatActivity {
    private TextView txtTreeName;
    private String treeName;
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
        treeName = intent.getStringExtra("Tree");
        txtTreeName.setText(treeName);
    }

    private void addEvents() {

    }

}
