package aut.bcis.researchdevelopment.treeidfornz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AddSightingLocationActivity extends AppCompatActivity implements OnMapReadyCallback {
    private SearchView treeLocationDisplay;
    private GoogleMap mMap;
    private ProgressDialog mapLoadingProgressDialog;
    private Geocoder geoCoder;
    private String treeAddress;
    private LatLng currentTreeLocation;
    private Button btnSaveLocation;
    private Intent intent;
    private Double treeLatitude = 0.0;
    private Double treeLongitude = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sighting_location);
        addControls();
        addEvents();
    }

    private void addControls()  {
        //-------------------------------------------Basic Layout Initiation-----------------------------------------------------------
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        TextView txtBarTitle = (TextView) myToolbar.findViewById(R.id.toolbar_title);
        txtBarTitle.setText("Sighting Location");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        txtBarTitle.setTextColor(Color.WHITE);
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.icon_menuu); // change tool bar icon
        myToolbar.setOverflowIcon(drawable);
        btnSaveLocation = (Button) findViewById(R.id.btnSaveLocation);
        treeLocationDisplay = (SearchView) findViewById(R.id.treeDisplay);
        treeLocationDisplay.setQueryHint("Tap on the map to specify tree location.");
        treeLocationDisplay.setIconified(false);
        treeLocationDisplay.clearFocus();
        LinearLayout linearLayout1 = (LinearLayout) treeLocationDisplay.getChildAt(0);
        LinearLayout linearLayout2 = (LinearLayout) linearLayout1.getChildAt(2);
        final LinearLayout linearLayout3 = (LinearLayout) linearLayout2.getChildAt(1);
        linearLayout3.post(new Runnable() {
            @Override
            public void run() {
                ViewGroup.LayoutParams params = linearLayout3.getLayoutParams();
                params.height = linearLayout3.getHeight() + (linearLayout3.getHeight()/3);
                linearLayout3.setLayoutParams(params);
            }
        });
        AutoCompleteTextView autoComplete = (AutoCompleteTextView) linearLayout3.getChildAt(0);
        autoComplete.setTextSize(15);
        //-------------------------------------------Load Google Map-----------------------------------------------------------
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mapLoadingProgressDialog = new ProgressDialog(AddSightingLocationActivity.this);
        mapLoadingProgressDialog.setTitle("Please wait");
        mapLoadingProgressDialog.setMessage("Google Map is loading...");
        mapLoadingProgressDialog.setCanceledOnTouchOutside(false);
        mapLoadingProgressDialog.show();
        geoCoder = new Geocoder(this, Locale.getDefault());
        //--------------------------------------------GPS For Current Tree Location----------------------------------------------------------
        intent = getIntent();
        treeLatitude = intent.getDoubleExtra("CurrentLocationLatitude", 0);
        treeLongitude = intent.getDoubleExtra("CurrentLocationLongitude", 0);
        currentTreeLocation = new LatLng(treeLatitude, treeLongitude);
        List<Address> addresses = null;
        try {
            addresses = geoCoder.getFromLocation(currentTreeLocation.latitude, currentTreeLocation.longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses != null) {
            Address address = (addresses.get(0));
            StringBuilder strReturnedAddress = new StringBuilder("");
            for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                if (i != address.getMaxAddressLineIndex() - 1)
                    strReturnedAddress.append(address.getAddressLine(i)).append(", ");
                else
                    strReturnedAddress.append(address.getAddressLine(i));
            }
            treeAddress = strReturnedAddress.toString();
            treeLocationDisplay.setQuery(treeAddress, false);
        }
    }

    private void addEvents() {
        treeLocationDisplay.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(s.isEmpty()) { //clear all the marker if search box is empty
                    treeLocationDisplay.setIconified(false);
                    treeLocationDisplay.clearFocus();
                    mMap.clear();
                }
                return false;
            }
        });

        btnSaveLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(treeLocationDisplay.getQuery().toString().isEmpty()) { //cannot continue if search box is empty
                    Toast.makeText(AddSightingLocationActivity.this, "Please specify the tree location", Toast.LENGTH_SHORT).show();
                }
                else {
                    intent.putExtra("Tree Location", treeLocationDisplay.getQuery().toString());
                    intent.putExtra("Tree Latitude", treeLatitude);
                    intent.putExtra("Tree Longitude", treeLongitude);
                    setResult(SaveSightedTreeActivity.LOCATION_RESULT_CODE, intent);
                    finish();
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
        if (item.getItemId() == R.id.menuHome) {
            Intent intent = new Intent(AddSightingLocationActivity.this, MainActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.menuList) {
            Intent intent = new Intent(AddSightingLocationActivity.this, ListActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.menuIdentification) {
            Intent intent = new Intent(AddSightingLocationActivity.this, IdentificationActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mapLoadingProgressDialog.dismiss();
            }
        });
        if(currentTreeLocation.latitude != -41.270020)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentTreeLocation, 12));
        else
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentTreeLocation, 5));
        mMap.addMarker(new MarkerOptions().position(currentTreeLocation).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_tree)));
        if(Utility.isNetworkAvailable(AddSightingLocationActivity.this)) {
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    mMap.clear(); //clear the old marker and add a new one
                    mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_tree)));
                    try {
                        treeLatitude = latLng.latitude;
                        treeLongitude = latLng.longitude;
                        List<Address> addresses = geoCoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                        if (addresses.size() != 0) { //avoid user clicks at the place where no location is detected by api.
                            Address address = (addresses.get(0));
                            StringBuilder strReturnedAddress = new StringBuilder("");
                            for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                                if (i != address.getMaxAddressLineIndex() - 1)
                                    strReturnedAddress.append(address.getAddressLine(i)).append(", ");
                                else
                                    strReturnedAddress.append(address.getAddressLine(i));
                            }
                            treeAddress = strReturnedAddress.toString();
                            treeLocationDisplay.setQuery(treeAddress, false);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

    }

    @Override
    protected void onResume() {
        treeLocationDisplay.clearFocus();
        super.onResume();
    }

}
