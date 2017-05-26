package aut.bcis.researchdevelopment.database;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by VS9 X64Bit on 26/08/2016.
 */
public class DBInitialization {
    public static final String DATABASE_NAME = "TreeID.sqlite";
    private static final String DB_PATH_SUFFIX = "/databases/";

    public static void getDatabaseFromAssets(Activity context) {
        File dbFile = context.getDatabasePath(DATABASE_NAME);
        if (!dbFile.exists()) {
            try {
                CopyDataBaseFromAsset(context);
            }
            catch(Exception e) {
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }
    public static void CopyDataBaseFromAsset(Activity context) {
        try {
            InputStream myInput = context.getAssets().open(DATABASE_NAME);
            String outFileName = getDatabasePath(context);
            File f = new File(context.getApplicationInfo().dataDir + DB_PATH_SUFFIX);
            if(!f.exists()) {
                f.mkdir();
            }
            OutputStream myOutput = new FileOutputStream(outFileName);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            myOutput.flush();
            myOutput.close();
            myInput.close();
        }
        catch(Exception e) {
            Log.e("COPYING ERROR", e.toString());
        }
    }
    public static String getDatabasePath(Activity context) {
        return context.getApplicationInfo().dataDir + DB_PATH_SUFFIX + DATABASE_NAME;
    }
}

