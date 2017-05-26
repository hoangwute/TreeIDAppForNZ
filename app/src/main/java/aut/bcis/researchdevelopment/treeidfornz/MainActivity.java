package aut.bcis.researchdevelopment.treeidfornz;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import aut.bcis.researchdevelopment.database.DBContract;
import aut.bcis.researchdevelopment.database.DBInitialization;

public class MainActivity extends AppCompatActivity {

    public static SQLiteDatabase database = null;
    private Button btnIdentifyATree;
    private Button btnAToZOfTree;
    private Button btnMyFavourites;
    private Button btnAbout;
    private RelativeLayout favouriteLayout;
    private ImageButton btnFavourite;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DBInitialization.getDatabaseFromAssets(MainActivity.this);
        database = openOrCreateDatabase(DBInitialization.DATABASE_NAME, MODE_PRIVATE, null);
        addControls();
        addEvents();
        loadPictureIntoDatabase();
    }

    private void addEvents() {
        btnAToZOfTree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                startActivity(intent);
            }
        });
        btnIdentifyATree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, IdentificationActivity.class);
                startActivity(intent);
            }
        });
        btnMyFavourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                intent.putExtra("FromHomePage", "homepage");
                startActivity(intent);
            }
        });
        btnFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                intent.putExtra("FromHomePage", "homepage");
                startActivity(intent);
            }
        });
        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadPictureIntoDatabase() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getBoolean("firstTime", false)) {
            // <---- run your one time code here
            UpdateImageTask task = new UpdateImageTask();
            task.execute();
//            Utility.archivedUpdatePicture(MainActivity.this);
            // mark that first time has already run.
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.commit();
        }
    }

    private void addControls() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        final int height = size.y;
        favouriteLayout = (RelativeLayout) findViewById(R.id.favouriteLayout);
        btnIdentifyATree = (Button) findViewById(R.id.btnIdentifyATree);
        btnAToZOfTree = (Button) findViewById(R.id.btnAToZOfTree);
        btnMyFavourites = (Button) findViewById(R.id.btnMyFavourites);
        btnFavourite = (ImageButton) findViewById(R.id.btnFavourite);
        btnAbout = (Button) findViewById(R.id.btnAbout);
        btnIdentifyATree.post(new Runnable() {
            @Override
            public void run() {
                RelativeLayout.LayoutParams groupLayoutParams = new RelativeLayout.LayoutParams(
                        btnIdentifyATree.getWidth(), btnIdentifyATree.getHeight());
                groupLayoutParams.setMargins(16, height/2-(height/10), 16, 0);
                btnIdentifyATree.setLayoutParams(groupLayoutParams);
            }
        });
        favouriteLayout.post(new Runnable() {
            @Override
            public void run() {
                RelativeLayout.LayoutParams groupLayoutParams = new RelativeLayout.LayoutParams(
                        favouriteLayout.getWidth(), favouriteLayout.getHeight());
                groupLayoutParams.setMargins(btnFavourite.getWidth(), height-(height/7), 0, 0);
                favouriteLayout.setLayoutParams(groupLayoutParams);
            }
        });
        dialog = new ProgressDialog(MainActivity.this);
        dialog.setIndeterminate(false);
        dialog.setTitle("Please do not close the application.");
        dialog.setMessage("Updating necessary components...");
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setCanceledOnTouchOutside(false);
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
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.menuList) {
            Intent intent = new Intent(MainActivity.this, ListActivity.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.menuIdentification) {
            Intent intent = new Intent(MainActivity.this, IdentificationActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private class UpdateImageTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                File internalStorage = MainActivity.this.getDir("MainPictures", Context.MODE_PRIVATE);
                FileOutputStream fos = null;
                int progress = 1;
                ArrayList<HashMap<Integer, Integer>> mapList = new ArrayList<>();
                Collections.addAll(mapList, Utility.mainImageMap(), Utility.secondImageMap(), Utility.thirdImageMap(), Utility.fourthImageMap(), Utility.fifthImageMap(), Utility.sixthImageMap(), Utility.seventhImageMap());
                for(int i = 0; i < mapList.size(); i++) {
                    for(Map.Entry<Integer, Integer> entry : mapList.get(i).entrySet()) {
                        BitmapFactory.Options o = new BitmapFactory.Options();
                        o.inJustDecodeBounds = true;
                        BitmapFactory.decodeResource(MainActivity.this.getResources(), entry.getValue());
                        // The new size we want to scale to
                        final int REQUIRED_SIZE=70;
                        // Find the correct scale value. It should be the power of 2.
                        int scale = 1;
                        while(o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                                o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                            scale *= 2;
                        }
                        // Decode with inSampleSize
                        BitmapFactory.Options o2 = new BitmapFactory.Options();
                        o2.inSampleSize = scale;
                        Bitmap bitmapPicture = BitmapFactory.decodeResource(MainActivity.this.getResources(), entry.getValue());
                        File reportFilePath = new File(internalStorage, entry.getValue() + ".jpeg");
                        String picturePath = reportFilePath.toString();
                        fos = new FileOutputStream(reportFilePath);
                        bitmapPicture.compress(Bitmap.CompressFormat.JPEG, 50 /*quality*/, fos);
                        fos.close();
                        if(i == 0) {
                            Cursor cursor = database.rawQuery("UPDATE Tree SET " + DBContract.COLUMN_MAIN_PICTURE + " = '" + picturePath + "' WHERE ID = " + entry.getKey(), null);
                            cursor.moveToFirst();
                            cursor.close();
                        }
                        else {
                            Cursor cursor = database.rawQuery("INSERT INTO " + DBContract.TABLE_IMAGE + "(ID, PicturePath) VALUES (" + entry.getKey() + ", '" + picturePath + "')", null);
                            cursor.moveToFirst();
                            cursor.close();
                        }
                        publishProgress(progress);
                        progress++;
                    }
                }
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
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            dialog.setProgress(values[0]);
            dialog.setMax(Utility.mainImageMap().size() + Utility.secondImageMap().size() + Utility.thirdImageMap().size() + Utility.fourthImageMap().size()
                + Utility.fifthImageMap().size() + Utility.sixthImageMap().size() + Utility.seventhImageMap().size());
        }
    }

}
