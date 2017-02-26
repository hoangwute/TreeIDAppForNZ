package aut.bcis.researchdevelopment.treeidfornz;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.squareup.picasso.Picasso;

import java.io.File;

public class SaveSightedTreeActivity extends AppCompatActivity {
    private TextView txtSavedTreeCommonName, txtSavedTreeLatinName;
    private EditText txtSavedTreeNote;
    private Button btnSavedTreeLocation, btnSaveTree, btnCloseTree;
    private ImageView imgTakePicture;
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private Place place;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_sighted_tree);
        addControls();
        addEvents();
    }

    private void addControls() {
        txtSavedTreeCommonName = (TextView) findViewById(R.id.txtSavedTreeCommonName);
        txtSavedTreeLatinName = (TextView) findViewById(R.id.txtSavedTreeLatinName);
        txtSavedTreeNote = (EditText) findViewById(R.id.txtSavedTreeNote);
        btnSavedTreeLocation = (Button) findViewById(R.id.btnSavedTreeLocation);
        imgTakePicture = (ImageView) findViewById(R.id.btnTakePicture);
        btnSaveTree = (Button) findViewById(R.id.btnSaveTree);
        btnCloseTree = (Button) findViewById(R.id.btnCloseTree);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("Bundle");
        txtSavedTreeCommonName.setText(bundle.getString("CommonName"));
        txtSavedTreeLatinName.setText(bundle.getString("LatinName"));
    }

    private void addEvents() {
        btnSavedTreeLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAutoCompleteActivity();
            }
        });
        btnSaveTree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LatLng spottedTreeLocation = place.getLatLng();
                Marker newMarker = MainActivity.mMap.addMarker(new MarkerOptions().position(spottedTreeLocation));
                Bundle bundle = new Bundle();
                bundle.putString("Tree name", txtSavedTreeCommonName.getText().toString());
                bundle.putString("Tree place", place.getName().toString());
                newMarker.setTag(bundle);
                Log.i(MainActivity.LOG_TAG, "Place: " + place.getName());
                Toast.makeText(SaveSightedTreeActivity.this, txtSavedTreeCommonName.getText().toString() + " has been successfully added.", Toast.LENGTH_LONG).show();
            }
        });
        btnCloseTree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        imgTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
                Toast.makeText(SaveSightedTreeActivity.this, "Opening Camera", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void openAutoCompleteActivity() {
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            // Indicates that Google Play Services is either not installed or not up to date. Prompt
            // the user to correct the issue.
            GoogleApiAvailability.getInstance().getErrorDialog(this, e.getConnectionStatusCode(),
                    0 /* requestCode */).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            // Indicates that Google Play Services is not available and the problem is not easily
            // resolvable.
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);

            Log.e(MainActivity.LOG_TAG, message);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) { //for getting place function
            if (resultCode == RESULT_OK) {
                place = PlaceAutocomplete.getPlace(this, data);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i(MainActivity.LOG_TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) { //for camera function
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            Picasso.with(SaveSightedTreeActivity.this).load(new File(tree.getFirstPicture())).centerCrop().resize(120, 105).into(holder.imgFirstPicture); //load picture using Picasso library
//            Picasso.with(SaveSightedTreeActivity.this).load(imageBitmap.getGenerationId())
            imgTakePicture.setImageBitmap(imageBitmap);
            imgTakePicture.setScaleType(ImageView.ScaleType.FIT_XY);
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

}
