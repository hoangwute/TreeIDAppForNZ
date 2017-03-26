package aut.bcis.researchdevelopment.treeidfornz;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;

import java.io.File;

import aut.bcis.researchdevelopment.model.FilterEntry;

import static aut.bcis.researchdevelopment.treeidfornz.MainActivity.filterEntriesList;
import static aut.bcis.researchdevelopment.treeidfornz.MainActivity.markerList;


public class MarkerInformationActivity extends AppCompatActivity {
    private TextView txtMarkerName, txtMarkerLatinName, txtMarkerNote, txtMarkerLocation;
    private ImageView imgMarkerPicture;
    private Button btnDelete;
    private int receivedID;
    private String receivedCommonName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_information);
        addControls();
        addEvents();
    }

    private void addControls()  {
        txtMarkerName = (TextView) findViewById(R.id.txtMarkerName);
        txtMarkerLatinName = (TextView) findViewById(R.id.txtMarkerLatinName);
        txtMarkerNote = (TextView) findViewById(R.id.txtMarkerNote);
        txtMarkerLocation = (TextView) findViewById(R.id.txtMarkerLocation);
        imgMarkerPicture = (ImageView) findViewById(R.id.imgMarkerPicture);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("Bundle");
        txtMarkerName.setText(bundle.getString("Tree common name"));
        txtMarkerLatinName.setText(bundle.getString("Tree latin name"));
        txtMarkerLocation.setText(bundle.getString("Tree place"));
        txtMarkerNote.setText(bundle.getString("Tree note"));
        receivedID = bundle.getInt("Tree id");
        receivedCommonName = bundle.getString("Tree common name");
        Picasso.with(MarkerInformationActivity.this).load(new File(bundle.getString("Tree image"))).into(imgMarkerPicture); //load picture using Picasso library
        imgMarkerPicture.setScaleType(ImageView.ScaleType.FIT_XY);
    }

    public void addEvents() {
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(Marker m : markerList) {
                    Bundle bundle = (Bundle) m.getTag();
                    if(bundle != null) {
                        int id  = bundle.getInt("Tree id");
                        if (id == receivedID) {
                            m.remove();
                            Cursor newCursor = MainActivity.database.rawQuery("DELETE FROM Marker WHERE ID = " + id, null);
                            newCursor.moveToFirst();
                            newCursor.close();
                        }
                    }
                }
                if(Utility.foundInsertedFilter(receivedCommonName) == false) {
                    Cursor newCursor = MainActivity.database.rawQuery("DELETE FROM FilterEntry WHERE CommonName = '" + receivedCommonName + "'", null);
                    newCursor.moveToFirst();
                    newCursor.close();
                    for(FilterEntry f: filterEntriesList) {
                        if(f.getCommonName().equals(receivedCommonName))
                            filterEntriesList.remove(f);
                    }
                }
                MainActivity.filterEntryAdapter.notifyDataSetChanged();
                finish();
            }
        });
    }
}
