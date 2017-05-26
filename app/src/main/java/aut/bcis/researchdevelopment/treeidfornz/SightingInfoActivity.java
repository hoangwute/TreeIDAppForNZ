package aut.bcis.researchdevelopment.treeidfornz;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import aut.bcis.researchdevelopment.database.DBContract;

import static aut.bcis.researchdevelopment.treeidfornz.MainActivity.database;

public class SightingInfoActivity extends AppCompatActivity {
    private TextView txtSightingInfoName, txtSightingInfoLatinName, txtSightingInfoDate, txtSightingInfoLocation;
    private ImageView imgSightingInfoPicture;
    private Button btnChangeSightingInfoLocation, btnEditSightingInfo;
    private ImageButton btnShareSighting;
    private EditText editTxtSightingInfoNote;
    private Integer sightingID;
    private final int LOCATION_REQUEST_CODE = 2603;
    private String treeLocation;
    private double treeLatitude, treeLongitude;
    public static final int LOCATION_RESULT_CODE = 0106;
    private final int REQUEST_IMAGE_CAPTURE = 2;
    private final int PICK_IMAGE_REQUEST = 3;
    private Bitmap imageBitmap;
    private ProgressDialog dialog;
    private int width;
    private String mCurrentPhotoPath;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sighting_info);
        addControls();
        addEvents();
    }

    private void addControls() {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        TextView txtBarTitle = (TextView) myToolbar.findViewById(R.id.toolbar_title);
        txtBarTitle.setText("Sightings");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        txtBarTitle.setTextColor(Color.WHITE);
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(),R.drawable.icon_menuu); // change tool bar icon
        myToolbar.setOverflowIcon(drawable);
        //------------------------------------------------------------------------------------------------------------
        Intent intent = getIntent();
        sightingID = intent.getIntExtra("ID", 0);
        treeLocation = Utility.findSightingInfoGivenID(sightingID, DBContract.COLUMN_SIGHTING_LOCATION);
        treeLongitude = Double.parseDouble(Utility.findSightingInfoGivenID(sightingID, DBContract.COLUMN_SIGHTING_LONGITUDE));
        treeLatitude = Double.parseDouble(Utility.findSightingInfoGivenID(sightingID, DBContract.COLUMN_SIGHTING_LATITUDE));
        txtSightingInfoName = (TextView) findViewById(R.id.txtSightingInfoName);
        txtSightingInfoLatinName = (TextView) findViewById(R.id.txtSightingInfoLatinName);
        txtSightingInfoDate = (TextView) findViewById(R.id.txtSightingInfoDate);
        txtSightingInfoLocation = (TextView) findViewById(R.id.txtSightingInfoLocation);
        imgSightingInfoPicture = (ImageView) findViewById(R.id.imgSightingInfoPicture);
        btnChangeSightingInfoLocation = (Button) findViewById(R.id.btnChangeSightingInfoLocation);
        btnShareSighting = (ImageButton) findViewById(R.id.btnShareSighting);
        btnEditSightingInfo = (Button) findViewById(R.id.btnEditSightingInfo);
        editTxtSightingInfoNote = (EditText) findViewById(R.id.editTxtSightingInfoNote);
        fillSightingInfo();
        dialog = new ProgressDialog(SightingInfoActivity.this);
        dialog.setTitle("Please be patient.");
        dialog.setMessage("Saving your image...");
        dialog.setCanceledOnTouchOutside(false);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
    }

    private void addEvents() {
        btnChangeSightingInfoLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Utility.isNetworkAvailable(SightingInfoActivity.this)) {
                    Intent intent = new Intent(SightingInfoActivity.this, AddSightingLocationActivity.class);
                    intent.putExtra("CurrentLocationLatitude", treeLatitude);
                    intent.putExtra("CurrentLocationLongitude", treeLongitude);
                    startActivityForResult(intent, LOCATION_REQUEST_CODE);
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SightingInfoActivity.this);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                            .setTitle("Phone is not connected to the internet")
                            .setMessage("Please connect your phone to the internet.");
                    AlertDialog diag = builder.create();
                    diag.show();
                }

            }
        });
        btnEditSightingInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor cursor = database.rawQuery("UPDATE " + DBContract.TABLE_SIGHTING + " SET " + DBContract.COLUMN_SIGHTING_LOCATION + " = '" + treeLocation
                        + "', " + DBContract.COLUMN_SIGHTING_NOTE + " = '" + editTxtSightingInfoNote.getText().toString() + "' WHERE ID = " + sightingID, null);
                cursor.moveToFirst();
                cursor.close();
                if(imageBitmap!= null) {
                    UpdateImageTask task = new UpdateImageTask(imageBitmap);
                    task.execute();
                }
                else {
                    Toast.makeText(SightingInfoActivity.this, "Your sighting has been successfully updated.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnShareSighting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Just sighted a new tree today!");
                startActivity(shareIntent);
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
            Glide.with(SightingInfoActivity.this).load(contentUri).override(width,333).centerCrop().into(imgSightingInfoPicture);
        }
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();
            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                Glide.with(SightingInfoActivity.this).load(uri).override(width,333).centerCrop().into(imgSightingInfoPicture);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(requestCode == LOCATION_REQUEST_CODE && resultCode == LOCATION_RESULT_CODE) {
            treeLocation = data.getStringExtra("Tree Location");
            treeLatitude = data.getDoubleExtra("Tree Latitude", 0.0);
            treeLongitude = data.getDoubleExtra("Tree Longitude", 0.0);
            txtSightingInfoLocation.setText(treeLocation);
        }
    }

    private void fillSightingInfo() {
        txtSightingInfoName.setText(Utility.findSightingInfoGivenID(sightingID, DBContract.COLUMN_SIGHTING_COMMON_NAME) + "/"
            + Utility.findSightingInfoGivenID(sightingID, DBContract.COLUMN_SIGHTING_MAORI_NAME));
        txtSightingInfoLatinName.setText(Utility.findSightingInfoGivenID(sightingID, DBContract.COLUMN_SIGHTING_LATIN_NAME));
        txtSightingInfoDate.setText(Utility.findSightingInfoGivenID(sightingID, DBContract.COLUMN_SIGHTING_DATE));
        txtSightingInfoLocation.setText(treeLocation);
        editTxtSightingInfoNote.setText(Utility.findSightingInfoGivenID(sightingID, DBContract.COLUMN_SIGHTING_NOTE));
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        final int width = size.x;
        if(Utility.findSightingInfoGivenID(sightingID, DBContract.COLUMN_SIGHTING_PICTURE) != null)
            Picasso.with(SightingInfoActivity.this).load(new File(Utility.findSightingInfoGivenID(sightingID, DBContract.COLUMN_SIGHTING_PICTURE))).centerCrop().resize(width, 333).into(imgSightingInfoPicture); //load picture using Picasso library
        else
            registerForContextMenu(imgSightingInfoPicture);
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
            Intent intent = new Intent(SightingInfoActivity.this, MainActivity.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.menuList) {
            Intent intent = new Intent(SightingInfoActivity.this, ListActivity.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.menuIdentification) {
            Intent intent = new Intent(SightingInfoActivity.this, IdentificationActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
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
                Uri photoURI = FileProvider.getUriForFile(SightingInfoActivity.this,
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
    private class UpdateImageTask extends AsyncTask<String, Void, String> {
        Bitmap imageBitmap;

        public UpdateImageTask(Bitmap imageBitmap) {
            this.imageBitmap = imageBitmap;
        }

        @Override
        protected String doInBackground(String... params) {
            File internalStorage = SightingInfoActivity.this.getDir("SightingPictures", Context.MODE_PRIVATE);
            File reportFilePath = new File(internalStorage, sightingID + ".jpg");
            FileOutputStream fos = null;
            String picturePath = reportFilePath.toString();
            try {
                fos = new FileOutputStream(reportFilePath);
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 50 /*quality*/, fos);
                fos.close();
                Cursor cursor = database.rawQuery("UPDATE " + DBContract.TABLE_SIGHTING + " SET " + DBContract.COLUMN_SIGHTING_PICTURE + " = '" + picturePath + "' WHERE ID = " + sightingID, null);
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
            dialog.show();
        }

        @Override
        protected void onPostExecute(String picturePath) {
            dialog.dismiss();
            Toast.makeText(SightingInfoActivity.this, "Your sighting has been successfully updated.", Toast.LENGTH_SHORT).show();
        }
    }
}
