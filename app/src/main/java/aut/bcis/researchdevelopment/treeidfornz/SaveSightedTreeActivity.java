package aut.bcis.researchdevelopment.treeidfornz;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

import aut.bcis.researchdevelopment.database.DBContract;
import aut.bcis.researchdevelopment.database.DBInitialization;

import static aut.bcis.researchdevelopment.treeidfornz.MainActivity.database;

public class SaveSightedTreeActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    private TextView txtSavedTreeCommonName, txtSavedTreeLatinName, txtSaveSightingLatLngState, txtSightingLocation;
    private EditText editTxtSightingNote;
    private Button btnSaveSighting, btnAddSightingLocation, btnChangeSightingLocation;
    private String treeLocation,treeCommonName, treeMaoriName, treeLatinName;
    private Double treeLatitude = -41.270020, treeLongitude = 173.891755;
    private ImageView imgSightingPicture;
    private final int LOCATION_REQUEST_CODE = 2603;
    public static final int LOCATION_RESULT_CODE = 0106;
    private final int REQUEST_IMAGE_CAPTURE = 2;
    private final int PICK_IMAGE_REQUEST = 3;
    private final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 4;
    private boolean mLocationPermissionGranted;
    private GoogleApiClient mGoogleApiClient;
    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private final int MAX_ENTRIES = 5;
    private LatLng[] mLikelyPlaceLatLngs = new LatLng[MAX_ENTRIES];
    private Bitmap imageBitmap;
    private ProgressDialog imageDialog;
    private int width;
    private String mCurrentPhotoPath;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_sighted_tree);
        addControls();
        addEvents();
    }

    private void addControls() {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        TextView txtBarTitle = (TextView) myToolbar.findViewById(R.id.toolbar_title);
        txtBarTitle.setText("New Sighting");
        txtBarTitle.setTextColor(Color.WHITE);
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(),R.drawable.icon_menuu); // change tool bar icon
        myToolbar.setOverflowIcon(drawable);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        btnChangeSightingLocation = (Button) findViewById(R.id.btnChangeSightingLocation);
        btnAddSightingLocation = (Button) findViewById(R.id.btnAddSightingLocation);
        btnSaveSighting = (Button) findViewById(R.id.btnSaveSighting);
        txtSightingLocation = (TextView) findViewById(R.id.txtSightingLocation);
        txtSavedTreeCommonName = (TextView) findViewById(R.id.txtSavedTreeCommonName);
        txtSavedTreeLatinName = (TextView) findViewById(R.id.txtSavedTreeLatinName);
        txtSaveSightingLatLngState = (TextView) findViewById(R.id.txtSaveLatLngState);
        editTxtSightingNote = (EditText) findViewById(R.id.editTxtSightingNote);
        imgSightingPicture = (ImageView) findViewById(R.id.btnTakePicture);
        registerForContextMenu(imgSightingPicture);
        //-----------------------------------------------------------------------------------------------
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,
                        this /* OnConnectionFailedListener */)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        mGoogleApiClient.connect();
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("Bundle");
        treeCommonName = bundle.getString("CommonName");
        treeMaoriName = bundle.getString("MaoriName");
        treeLatinName = bundle.getString("LatinName");
        txtSavedTreeCommonName.setText(treeCommonName + "/" + treeMaoriName);
        txtSavedTreeLatinName.setText(treeLatinName);
        intent.getStringExtra("Tree Location");
        final LocationManager manager = (LocationManager) getSystemService(SaveSightedTreeActivity.LOCATION_SERVICE);
        if(Utility.isNetworkAvailable(SaveSightedTreeActivity.this) && manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getDeviceLocation();
            getCurrentLagLng();
        }
        imageDialog = new ProgressDialog(SaveSightedTreeActivity.this);
        imageDialog.setTitle("Please be patient");
        imageDialog.setMessage("Saving your image...");
        imageDialog.setCanceledOnTouchOutside(false);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
    }

    private void addEvents() {
        btnAddSightingLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Utility.isNetworkAvailable(SaveSightedTreeActivity.this)) {
                    Intent intent = new Intent(SaveSightedTreeActivity.this, AddSightingLocationActivity.class);
                    if (!txtSaveSightingLatLngState.getText().toString().isEmpty()) {
                        String[] latLongString = txtSaveSightingLatLngState.getText().toString().split(",");
                        treeLatitude = Double.parseDouble(latLongString[0].replace("lat/lng: (", ""));
                        treeLongitude = Double.parseDouble(latLongString[1].substring(0, latLongString[1].length() - 1));
                    }
                    intent.putExtra("CurrentLocationLatitude", treeLatitude);
                    intent.putExtra("CurrentLocationLongitude", treeLongitude);
                    startActivityForResult(intent, LOCATION_REQUEST_CODE);
                }
                else
                    createInternetAlertDialog();
            }
        });
        btnChangeSightingLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Utility.isNetworkAvailable(SaveSightedTreeActivity.this)) {
                    Intent intent = new Intent(SaveSightedTreeActivity.this, AddSightingLocationActivity.class);
                    intent.putExtra("CurrentLocationLatitude", treeLatitude);
                    intent.putExtra("CurrentLocationLongitude", treeLongitude);
                    startActivityForResult(intent, LOCATION_REQUEST_CODE);
                }
                else
                    createInternetAlertDialog();
            }
        });
        btnSaveSighting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String location = txtSightingLocation.getText().toString();
                if(txtSightingLocation.getVisibility() == View.INVISIBLE) {
                    location = "Location is not specified.";
                }
                String note = editTxtSightingNote.getEditableText().toString();
                String timeStamp = Utility.getSystemDate();
                String sightingDate = Utility.getSightingDate();
                MainActivity.database = openOrCreateDatabase(DBInitialization.DATABASE_NAME, MODE_PRIVATE, null);
                Cursor cursor = database.rawQuery("INSERT INTO " + DBContract.TABLE_SIGHTING + "(CommonName, LatinName, Date, Location, Note, MaoriName, TimeStamp, Latitude, Longitude) " +
                        "VALUES (\"" + treeCommonName + "\", '" + treeLatinName + "', '" + sightingDate+ "', '" + location + "', '" + note + "', '" + treeMaoriName + "', '"
                        + timeStamp + "', " + treeLatitude + ", " + treeLongitude + ")" , null);
                cursor.moveToFirst();
                cursor.close();
                if(imageBitmap != null) {
                    UpdateImageTask task = new UpdateImageTask(imageBitmap);
                    task.execute();
                }
                else {
                    Toast.makeText(SaveSightedTreeActivity.this, "New Sighting is successfully added.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) { //for camera function
            File f = new File(mCurrentPhotoPath);
            Uri contentUri = Uri.fromFile(f);
            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),contentUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Glide.with(SaveSightedTreeActivity.this).load(contentUri).override(width,333).centerCrop().into(imgSightingPicture);
        }
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                Glide.with(SaveSightedTreeActivity.this).load(uri).override(width,333).centerCrop().into(imgSightingPicture);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(requestCode == LOCATION_REQUEST_CODE && resultCode == LOCATION_RESULT_CODE) {
            treeLocation = data.getStringExtra("Tree Location");
            treeLatitude = data.getDoubleExtra("Tree Latitude", 0.0);
            treeLongitude = data.getDoubleExtra("Tree Longitude", 0.0);
            btnAddSightingLocation.setVisibility(View.INVISIBLE);
            btnChangeSightingLocation.setVisibility(View.VISIBLE);
            txtSightingLocation.setVisibility(View.VISIBLE);
            txtSightingLocation.setText(treeLocation);
        }
    }

    private void createInternetAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SaveSightedTreeActivity.this);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        })
                .setTitle("Phone is not connected to the internet")
                .setMessage("Please connect your phone to the internet. It is also recommended to turn on the GPS so the application" +
                        "can access your current location.");
        AlertDialog diag = builder.create();
        diag.show();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(SaveSightedTreeActivity.this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
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

    private void getCurrentLagLng() {
        if (mLocationPermissionGranted) {
            @SuppressWarnings("MissingPermission")
            PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi
                    .getCurrentPlace(mGoogleApiClient, null);
            result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
                @Override
                public void onResult(@NonNull PlaceLikelihoodBuffer likelyPlaces) {
                    for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                        mLikelyPlaceLatLngs[0] = placeLikelihood.getPlace().getLatLng();
                        break;
                    }
                    // Release the place likelihood buffer, to avoid memory leaks.
                    txtSaveSightingLatLngState.setText(mLikelyPlaceLatLngs[0].toString());
                    likelyPlaces.release();
                }
            });
        }
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menuHome) {
            Intent intent = new Intent(SaveSightedTreeActivity.this, MainActivity.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.menuList) {
            Intent intent = new Intent(SaveSightedTreeActivity.this, ListActivity.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.menuIdentification) {
            Intent intent = new Intent(SaveSightedTreeActivity.this, IdentificationActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private class UpdateImageTask extends AsyncTask<String, Void, String> {
        Bitmap imageBitmap;

        public UpdateImageTask(Bitmap imageBitmap) {
            this.imageBitmap = imageBitmap;
        }

        @Override
        protected String doInBackground(String... params) {
            File internalStorage = SaveSightedTreeActivity.this.getDir("SightingPictures", Context.MODE_PRIVATE);
            File reportFilePath = new File(internalStorage, Utility.getLastInsertRowID() + ".jpg");
            FileOutputStream fos = null;
            String picturePath = reportFilePath.toString();
            try {
                fos = new FileOutputStream(reportFilePath);
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 50 /*quality*/, fos);
                fos.close();
                Cursor cursor = database.rawQuery("UPDATE " + DBContract.TABLE_SIGHTING + " SET " + DBContract.COLUMN_SIGHTING_PICTURE + " = '" + picturePath + "' WHERE ID = " + Utility.getLastInsertRowID(), null);
                cursor.moveToFirst();
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            imageDialog.show();
        }

        @Override
        protected void onPostExecute(String picturePath) {
            imageDialog.dismiss();
            Toast.makeText(SaveSightedTreeActivity.this, "New Sighting is successfully added.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

}
