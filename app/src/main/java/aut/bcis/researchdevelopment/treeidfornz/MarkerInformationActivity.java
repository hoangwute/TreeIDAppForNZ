package aut.bcis.researchdevelopment.treeidfornz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MarkerInformationActivity extends AppCompatActivity {
    private TextView txtMarkerName;
    private TextView txtMarkerPlace;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_information);
        addControls();
        addEvents();
    }

    private void addControls() {
        txtMarkerName = (TextView) findViewById(R.id.txtMarkerName);
        txtMarkerPlace = (TextView) findViewById(R.id.txtMarkerPlace);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("Bundle");
        txtMarkerName.setText(bundle.getString("Tree name"));
        txtMarkerPlace.setText(bundle.getString("Tree place"));
    }

    private void addEvents() {

    }
}
