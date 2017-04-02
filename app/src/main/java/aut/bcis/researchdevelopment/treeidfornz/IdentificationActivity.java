package aut.bcis.researchdevelopment.treeidfornz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import aut.bcis.researchdevelopment.database.DBContract;

public class IdentificationActivity extends AppCompatActivity {
    public static final int START_CHAR_POSITION_TO_DELETE = 26;
    private CheckBox chkToothed, chkSmooth, chkAlternating, chkOpposite, chkHandshaped;
    private Button btnFinalise;
    public static String dynamicQuery = "SELECT * FROM " + DBContract.TABLE_TREE + " WHERE (";
    private TextView txtFirstHeader, txtSecondHeader;
    private ArrayList<String> firstHeaderKeys = new ArrayList(), secondHeaderKeys = new ArrayList();

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
        txtBarTitle.setText("Tree Identification");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        btnFinalise = (Button) findViewById(R.id.btnFinalise);
        txtFirstHeader = (TextView) findViewById(R.id.txtFirstHeader);
        txtSecondHeader = (TextView) findViewById(R.id.txtSecondHeader);
        chkSmooth = (CheckBox) findViewById(R.id.chkSmooth);
        chkToothed = (CheckBox) findViewById(R.id.chkToothed);
        chkAlternating = (CheckBox) findViewById(R.id.chkAlternating);
        chkHandshaped = (CheckBox) findViewById(R.id.chkHandshaped);
        chkOpposite = (CheckBox) findViewById(R.id.chkOpposite);

    }
    private void addEvents() {
        chkSmooth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    firstHeaderKeys.add("smooth");
                } else {
                    firstHeaderKeys.remove("smooth");
                }
                updateCorrespondingHeader(1);
            }
        });
        chkToothed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    firstHeaderKeys.add("toothed");
                } else {
                    firstHeaderKeys.remove("toothed");
                }
                updateCorrespondingHeader(1);
            }
        });
        chkHandshaped.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    secondHeaderKeys.add("hand-shaped");
                } else {
                    secondHeaderKeys.remove("hand-shaped");
                }
                updateCorrespondingHeader(2);
            }
        });
        chkAlternating.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    secondHeaderKeys.add("alternating");
                } else {
                    secondHeaderKeys.remove("alternating");
                }
                updateCorrespondingHeader(2);
            }
        });
        chkOpposite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    secondHeaderKeys.add("opposite");
                } else {
                    secondHeaderKeys.remove("opposite");
                }
                updateCorrespondingHeader(2);
            }
        });
        btnFinalise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (generateDynamicQuery() == true) {
                    Intent intent = new Intent(IdentificationActivity.this, IdentificationResultActivity.class);
                    intent.putExtra("Query", dynamicQuery);
                    startActivity(intent);
                } else {
                    Toast.makeText(IdentificationActivity.this, "Please specify tree traits", Toast.LENGTH_LONG).show();
                }
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

    private void updateCorrespondingHeader(int headerNo) {
        ArrayList<String> keys = new ArrayList<>();
        TextView headerText = null;
        switch (headerNo) {
            case 1:
                keys = firstHeaderKeys;
                headerText = txtFirstHeader;
                break;
            case 2:
                keys = secondHeaderKeys;
                headerText = txtSecondHeader;
                break;
        }
        if (keys.isEmpty()) {
            switch (headerNo) {
                case 1:
                    headerText.setText(DBContract.COLUMN_MARGIN);
                    break;
                case 2:
                    headerText.setText(DBContract.COLUMN_ARRANGEMENT);
                    break;
            }
        } else {
            headerText.setText("");
            for (int i = 0; i < keys.size(); i++) {
                if (i == keys.size() - 1)
                    headerText.append(keys.get(i));
                else
                    headerText.append(keys.get(i) + ", ");
            }
        }
    }

    private boolean generateDynamicQuery() {
        if (!firstHeaderKeys.isEmpty()) {
            dynamicQuery += " AND " + DBContract.COLUMN_MARGIN + " = ";
            for (int i = 0; i < firstHeaderKeys.size(); i++) {
                if (i != firstHeaderKeys.size() - 1)
                    dynamicQuery += DBContract.COLUMN_MARGIN + " = '" + firstHeaderKeys.get(i) + "'" + " OR ";
                else
                    dynamicQuery += "'" + firstHeaderKeys.get(i) + "'";
            }
        }
        if (!secondHeaderKeys.isEmpty()) {
            dynamicQuery += " AND " + DBContract.COLUMN_ARRANGEMENT + " = ";
            for (int i = 0; i < secondHeaderKeys.size(); i++) {
                if (i != secondHeaderKeys.size() - 1)
                    dynamicQuery += DBContract.COLUMN_ARRANGEMENT + " = '" + secondHeaderKeys.get(i) + "'" + " OR ";
                else
                    dynamicQuery += "'" + secondHeaderKeys.get(i) + "'";
            }
        }
        if (dynamicQuery.length() > START_CHAR_POSITION_TO_DELETE) { // > 26 to make sure there is criteria selected.
            dynamicQuery = dynamicQuery.replaceFirst(" AND ", "").concat(")");
            Toast.makeText(IdentificationActivity.this, dynamicQuery, Toast.LENGTH_LONG).show();
            System.out.println(dynamicQuery);
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
