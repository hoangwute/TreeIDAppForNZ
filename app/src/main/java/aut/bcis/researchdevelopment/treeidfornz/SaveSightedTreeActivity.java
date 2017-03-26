package aut.bcis.researchdevelopment.treeidfornz;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import aut.bcis.researchdevelopment.database.DBContract;
import aut.bcis.researchdevelopment.database.DBInitialization;
import aut.bcis.researchdevelopment.model.TreeMarker;

import static aut.bcis.researchdevelopment.treeidfornz.MainActivity.database;
import static aut.bcis.researchdevelopment.treeidfornz.MainActivity.txtSightedTreeCount;

public class SaveSightedTreeActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    private TextView txtSavedTreeCommonName, txtSavedTreeLatinName, txtShowLocation;
    private EditText txtSavedTreeNote;
    private Button btnSaveTree;
    private ImageView imgTakePicture;
    private final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private Place place;
    private final int REQUEST_IMAGE_CAPTURE = 2;
    private final int PICK_IMAGE_REQUEST = 3;
    private final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 4;
    private boolean mLocationPermissionGranted;
    private RadioButton radChoosePlace, radCurrentPlace;
    private GoogleApiClient mGoogleApiClient;
    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;
    private final int MAX_ENTRIES = 5;
    private String[] mLikelyPlaceNames = new String[MAX_ENTRIES];
    private String[] mLikelyPlaceAddresses = new String[MAX_ENTRIES];
    private String[] mLikelyPlaceAttributions = new String[MAX_ENTRIES];
    private LatLng[] mLikelyPlaceLatLngs = new LatLng[MAX_ENTRIES];
    private LatLng spottedTreeLocation;
    private Bitmap imageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_sighted_tree);
        addControls();
        addEvents();
    }

    private void addControls() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,
                        this /* OnConnectionFailedListener */)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        mGoogleApiClient.connect();
        txtSavedTreeCommonName = (TextView) findViewById(R.id.txtSavedTreeCommonName);
        txtSavedTreeLatinName = (TextView) findViewById(R.id.txtSavedTreeLatinName);
        txtSavedTreeNote = (EditText) findViewById(R.id.txtSavedTreeNote);
        txtShowLocation = (TextView) findViewById(R.id.txtShowLocation);
        imgTakePicture = (ImageView) findViewById(R.id.btnTakePicture);
        btnSaveTree = (Button) findViewById(R.id.btnSaveTree);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("Bundle");
        txtSavedTreeCommonName.setText(bundle.getString("CommonName"));
        txtSavedTreeLatinName.setText(bundle.getString("LatinName"));
        radChoosePlace = (RadioButton) findViewById(R.id.radChoosePlace);
        radCurrentPlace = (RadioButton) findViewById(R.id.radCurrentPlace);
    }

    private void addEvents() {
        btnSaveTree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!txtShowLocation.getText().toString().equals("Location")) {
                    Toast.makeText(SaveSightedTreeActivity.this, txtSavedTreeCommonName.getText().toString() + " has been successfully added to your own tree map.", Toast.LENGTH_LONG).show();
                    String commonName = txtSavedTreeCommonName.getText().toString();
                    String latinName = txtSavedTreeLatinName.getText().toString();
                    String image = Utility.getTakenPicturePath(imageBitmap, SaveSightedTreeActivity.this);
                    double latitude = spottedTreeLocation.latitude;
                    double longitude = spottedTreeLocation.longitude;
                    String place = txtShowLocation.getText().toString();
                    String note = txtSavedTreeNote.getEditableText().toString();
                    TreeMarker marker = new TreeMarker(commonName, latinName, place, note, image, latitude, longitude);
                    if(Utility.foundInsertedFilter(marker.getCommonName()) == false) {
                        Utility.insertFilterEntry(marker);
                    }
                    Utility.insertNewMarker(marker);
                    txtSightedTreeCount.setText(String.valueOf(Utility.countAllSightedTree()));
                }
                else
                    Toast.makeText(SaveSightedTreeActivity.this, "Please specify the tree location", Toast.LENGTH_LONG).show();

            }
        });
        registerForContextMenu(imgTakePicture);
        radChoosePlace.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!radCurrentPlace.isChecked() && radChoosePlace.isChecked()) {
                    openLocationFinderActivity();
                    radChoosePlace.setChecked(false);
                }
                else if(radChoosePlace.isChecked() && radCurrentPlace.isChecked()) {
                    openLocationFinderActivity();
                    radChoosePlace.setChecked(false);
                }
            }
        });
        radCurrentPlace.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!radChoosePlace.isChecked() && radCurrentPlace.isChecked()) {
                    getDeviceLocation();
                    showCurrentPlace();
                    radCurrentPlace.setChecked(false);
                }
                else if(radChoosePlace.isChecked() && radCurrentPlace.isChecked()) {
                    getDeviceLocation();
                    showCurrentPlace();
                    radCurrentPlace.setChecked(false);
                }
            }
        });
    }

    private void openLocationFinderActivity() {
        try {
            AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder()
                    .setTypeFilter(Place.TYPE_COUNTRY)
                    .setCountry("NZ") //restrict place search to New Zealand
                    .build();
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .setFilter(autocompleteFilter)
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
                spottedTreeLocation = place.getLatLng();
                txtShowLocation.setText("Location: " + place.getAddress());
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
            imageBitmap = (Bitmap) extras.get("data");
            imgTakePicture.setImageBitmap(imageBitmap);
            imgTakePicture.setScaleType(ImageView.ScaleType.FIT_XY);
        }
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imgTakePicture.setImageBitmap(imageBitmap);
                imgTakePicture.setScaleType(ImageView.ScaleType.FIT_XY);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_picture, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.openCamera) {
            dispatchTakePictureIntent();
            Toast.makeText(SaveSightedTreeActivity.this, "Opening Camera", Toast.LENGTH_LONG).show();
        }
        else if(item.getItemId() == R.id.browseLibrary) {
            Intent intent = new Intent();
            intent.setType("image/*"); // Show only images, no videos or anything else
            intent.setAction(Intent.ACTION_GET_CONTENT); // Always show the chooser (if there are multiple options available)
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        }
        return super.onContextItemSelected(item);
    }

    private void getDeviceLocation() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
         /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        if (mLocationPermissionGranted) {
            mLastKnownLocation = LocationServices.FusedLocationApi
                    .getLastLocation(mGoogleApiClient);
        }
    }
//
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
    }

    private void showCurrentPlace() {

        if (mLocationPermissionGranted) {
            // Get the likely places - that is, the businesses and other points of interest that
            // are the best match for the device's current location.
            @SuppressWarnings("MissingPermission")
            PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi
                    .getCurrentPlace(mGoogleApiClient, null);
            result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
                @Override
                public void onResult(@NonNull PlaceLikelihoodBuffer likelyPlaces) {
                    int i = 0;
                    mLikelyPlaceNames = new String[MAX_ENTRIES];
                    mLikelyPlaceAddresses = new String[MAX_ENTRIES];
                    mLikelyPlaceAttributions = new String[MAX_ENTRIES];
                    mLikelyPlaceLatLngs = new LatLng[MAX_ENTRIES];
                    for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                        // Build a list of likely places to show the user. Max 5.
                        mLikelyPlaceNames[i] = (String) placeLikelihood.getPlace().getName();
                        mLikelyPlaceAddresses[i] = (String) placeLikelihood.getPlace().getAddress();
                        mLikelyPlaceAttributions[i] = (String) placeLikelihood.getPlace()
                                .getAttributions();
                        mLikelyPlaceLatLngs[i] = placeLikelihood.getPlace().getLatLng();
                        i++;
                        if (i > (MAX_ENTRIES - 1)) {
                            break;
                        }
                    }
                    // Release the place likelihood buffer, to avoid memory leaks.
                    likelyPlaces.release();

                    // Show a dialog offering the user the list of likely places, and add a
                    // marker at the selected place.
                    openPlacesDialog();
                }
            });
        }
    }
    private void openPlacesDialog() {
        // Ask the user to choose the place where they are now.
        DialogInterface.OnClickListener listener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // The "which" argument contains the position of the selected item.
                        spottedTreeLocation = mLikelyPlaceLatLngs[which];
                        String markerSnippet = mLikelyPlaceAddresses[which];
                        txtShowLocation.setText("Location: " + markerSnippet);
                        if (mLikelyPlaceAttributions[which] != null) {
                            markerSnippet = markerSnippet + "\n" + mLikelyPlaceAttributions[which];
                        };
                    }
                };

        // Display the dialog.
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Pick a place")
                .setItems(mLikelyPlaceNames, listener)
                .show();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
