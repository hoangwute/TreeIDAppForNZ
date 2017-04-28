package aut.bcis.researchdevelopment.treeidfornz;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import aut.bcis.researchdevelopment.database.DBContract;
import aut.bcis.researchdevelopment.model.ListHeader;
import aut.bcis.researchdevelopment.model.Sighting;
import aut.bcis.researchdevelopment.model.Tree;
import aut.bcis.researchdevelopment.model.TreeMarker;

import static aut.bcis.researchdevelopment.treeidfornz.MainActivity.database;

/**
 * Created by VS9 X64Bit on 28/08/2016.
 */
public class Utility {

    public static void updateReportPicture(long reportId, int picture, Context mContext) {
        Bitmap bitmapPicture = BitmapFactory.decodeResource(mContext.getResources(), picture);
        // Saves the new picture to the internal storage with the unique identifier of the report as
        // the name. That way, there will never be two report pictures with the same name.
        String picturePath = "";
        File internalStorage = mContext.getDir("ReportPictures", Context.MODE_PRIVATE);
        File reportFilePath = new File(internalStorage, reportId + ".jpg");
        picturePath = reportFilePath.toString();

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(reportFilePath);
            bitmapPicture.compress(Bitmap.CompressFormat.PNG, 100 /*quality*/, fos);
            fos.close();
            Toast.makeText(mContext, "Update " + picturePath + " successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            Log.i("DATABASE", "Problem updating picture", ex);
            picturePath = "";
        }
        // Updates the database entry for the report to point to the picture
        Cursor cursor = database.rawQuery("UPDATE Tree SET " + DBContract.COLUMN_MAIN_PICTURE + " = '" + picturePath + "' WHERE ID = " + reportId, null);
        cursor.moveToFirst();
        cursor.close();
    }

    public static void insertSightingPicture(long reportId, Bitmap bitmapPicture, Context mContext) {
        // Saves the new picture to the internal storage with the unique identifier of the report as
        // the name. That way, there will never be two report pictures with the same name.
        String picturePath = "";
        File internalStorage = mContext.getDir("SightingPictures", Context.MODE_PRIVATE);
        File reportFilePath = new File(internalStorage, reportId + ".jpg");
        picturePath = reportFilePath.toString();

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(reportFilePath);
            bitmapPicture.compress(Bitmap.CompressFormat.PNG, 100 /*quality*/, fos);
            fos.close();
            Toast.makeText(mContext, "Update " + picturePath + " successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            Log.i("DATABASE", "Problem updating picture", ex);
            picturePath = "";
        }
        // Updates the database entry for the report to point to the picture
        Cursor cursor = database.rawQuery("UPDATE " + DBContract.TABLE_SIGHTING + " SET " + DBContract.COLUMN_SIGHTING_PICTURE + " = '" + picturePath + "' WHERE ID = " + reportId, null);
        cursor.moveToFirst();
        cursor.close();
    }

    public static void deleteSightingPicture(long reportId, Context mContext) {
        File internalStorage = mContext.getDir("SightingPictures", Context.MODE_PRIVATE);
        File reportFilePath = new File(internalStorage, reportId + ".jpg");
        Toast.makeText(mContext, reportFilePath.toString(), Toast.LENGTH_SHORT).show();
        reportFilePath.delete();
        // Delete the database entry for the report to point to the picture
        Cursor cursor = database.rawQuery("DELETE FROM " + DBContract.TABLE_SIGHTING + " WHERE " + DBContract.COLUMN_SIGHTING_ID + " = " + reportId, null);
        cursor.moveToFirst();
        cursor.close();
    }

    public static void generateAlphabeticalHeaders(ArrayList<Object> treeList) {
        ArrayList<Integer> positionList = new ArrayList<>();
        ArrayList<String> valueList = new ArrayList<>();
        for (int i = 0; i < treeList.size(); i++) {
            Tree tree = (Tree) treeList.get(i);
            if (i == 0) {
                positionList.add(i);
                valueList.add(tree.getCommonName().substring(0, 1)); //get the first letter
            } else {
                Tree previousTree = (Tree) treeList.get(i - 1);
                if (!tree.getCommonName().substring(0, 1).equalsIgnoreCase(previousTree.getCommonName().substring(0, 1))) {
                    positionList.add(i);
                    valueList.add(tree.getCommonName().substring(0, 1));
                }
            }
        }
        for (int i = 0; i < positionList.size(); i++) {
            treeList.add(positionList.get(i) + i, new ListHeader(valueList.get(i))); //important plus i here as the array shifts the object position
        }
    }

    public static void generateAlphabeticalHeadersLatinName(ArrayList<Object> treeList) {
        ArrayList<Integer> positionList = new ArrayList<>();
        ArrayList<String> valueList = new ArrayList<>();
        for (int i = 0; i < treeList.size(); i++) {
            Tree tree = (Tree) treeList.get(i);
            if (i == 0) {
                positionList.add(i);
                valueList.add(tree.getLatinName().substring(0, 1)); //get the first letter
            } else {
                Tree previousTree = (Tree) treeList.get(i - 1);
                if (!tree.getLatinName().substring(0, 1).equalsIgnoreCase(previousTree.getLatinName().substring(0, 1))) {
                    positionList.add(i);
                    valueList.add(tree.getLatinName().substring(0, 1));
                }
            }
        }
        for (int i = 0; i < positionList.size(); i++) {
            treeList.add(positionList.get(i) + i, new ListHeader(valueList.get(i))); //important plus i here as the array shifts the object position
        }
    }

    public static void generateAlphabeticalHeadersMaoriName(ArrayList<Object> treeList) {
        ArrayList<Integer> positionList = new ArrayList<>();
        ArrayList<String> valueList = new ArrayList<>();
        for (int i = 0; i < treeList.size(); i++) {
            Tree tree = (Tree) treeList.get(i);
            if (i == 0) {
                positionList.add(i);
                valueList.add(tree.getMaoriName().substring(0, 1)); //get the first letter
            } else {
                Tree previousTree = (Tree) treeList.get(i - 1);
                if (!tree.getMaoriName().substring(0, 1).equalsIgnoreCase(previousTree.getMaoriName().substring(0, 1))) {
                    positionList.add(i);
                    valueList.add(tree.getMaoriName().substring(0, 1));
                }
            }
        }
        for (int i = 0; i < positionList.size(); i++) {
            treeList.add(positionList.get(i) + i, new ListHeader(valueList.get(i))); //important plus i here as the array shifts the object position
        }
    }

    public static void generateSightingHeaders(ArrayList<Object> sightingList) {
        ArrayList<Integer> positionList = new ArrayList<>();
        ArrayList<String> valueList = new ArrayList<>();
        for (int i = 0; i < sightingList.size(); i++) {
            Sighting sighting = (Sighting) sightingList.get(i);
            if (i == 0) {
                positionList.add(i);
                valueList.add(sighting.getDate());
            } else {
                Sighting previousSighting = (Sighting) sightingList.get(i - 1);
                if (!sighting.getDate().equalsIgnoreCase(previousSighting.getDate())) {
                    positionList.add(i);
                    valueList.add(sighting.getDate());
                }
            }
        }
        for (int i = 0; i < positionList.size(); i++) {
            sightingList.add(positionList.get(i) + i, new ListHeader(valueList.get(i))); //important plus i here as the array shifts the object position
        }
    }

    public static void generateGenusHeaders(ArrayList<Object> treeList) {
        ArrayList<Integer> positionList = new ArrayList<>();
        ArrayList<String> valueList = new ArrayList<>();
        for (int i = 0; i < treeList.size(); i++) {
            Tree tree = (Tree) treeList.get(i);
            if (i == 0) {
                positionList.add(i);
                valueList.add(tree.getStructuralClass());
            } else {
                Tree previousTree = (Tree) treeList.get(i - 1);
                if (!tree.getStructuralClass().equalsIgnoreCase(previousTree.getStructuralClass())) {
                    positionList.add(i);
                    valueList.add(tree.getStructuralClass());
                }
            }
        }
        for (int i = 0; i < positionList.size(); i++) {
            treeList.add(positionList.get(i) + i, new ListHeader(valueList.get(i))); //important plus i here as the array shifts the object position
        }
    }

    public static void generateFamilyHeaders(ArrayList<Object> treeList) {
        ArrayList<Integer> positionList = new ArrayList<>();
        ArrayList<String> valueList = new ArrayList<>();
        for (int i = 0; i < treeList.size(); i++) {
            Tree tree = (Tree) treeList.get(i);
            if (i == 0) {
                positionList.add(i);
                valueList.add(tree.getFamily().trim());
            } else {
                Tree previousTree = (Tree) treeList.get(i - 1);
                if (!tree.getFamily().trim().equalsIgnoreCase(previousTree.getFamily().trim())) {
                    positionList.add(i);
                    valueList.add(tree.getFamily().trim());
                }
            }
        }
        for (int i = 0; i < positionList.size(); i++) {
            treeList.add(positionList.get(i) + i, new ListHeader(valueList.get(i))); //important plus i here as the array shifts the object position
        }
    }


//    public static int countSightedTreeType(String commonName) {
//        Cursor cursor = database.rawQuery("SELECT COUNT(*) FROM " + DBContract.TABLE_MARKER + " WHERE " + DBContract.COLUMN_MARKER_COMMON_NAME + " = '" + commonName + "'", null);
//        int count = 0;
//        while (cursor.moveToNext()) {
//            count = cursor.getInt(cursor.getColumnIndex("COUNT(*)"));
//        }
//        cursor.close();
//        return count;
//    }

    public static int countSightings(String treeName) {
        Cursor cursor = database.rawQuery("SELECT COUNT(*) FROM " + DBContract.TABLE_SIGHTING + " WHERE CommonName = \"" + treeName + "\"", null);
        int count = 0;
        while (cursor.moveToNext()) {
            count = cursor.getInt(cursor.getColumnIndex("COUNT(*)"));
        }
        cursor.close();
        return count;
    }

    public static void insertNewMarker(TreeMarker marker) {
        Cursor cursor = null;
        if (!marker.getNote().isEmpty()) {
            cursor = database.rawQuery("INSERT INTO MARKER(CommonName, LatinName, Location, Note, ImagePath" +
                    ", Latitude, Longitude) VALUES ('" + marker.getCommonName() + "', '" + marker.getLatinName() + "', '" +
                    marker.getLocation() + "', '" + marker.getNote() + "', '" + marker.getImagePath() + "', " + marker.getLatitude()
                    + ", " + marker.getLongitude() + ")", null);
        } else {
            cursor = database.rawQuery("INSERT INTO MARKER(CommonName, LatinName, Location, Note, ImagePath" +
                    ", Latitude, Longitude) VALUES ('" + marker.getCommonName() + "', '" + marker.getLatinName() + "', '" +
                    marker.getLocation() + "', '', '" + marker.getImagePath() + "', " + marker.getLatitude()
                    + ", " + marker.getLongitude() + ")", null);
        }
        cursor.moveToFirst();
        cursor.close();
    }

//    public static boolean foundInsertedFilter(String filterCommonName) {
//        Cursor cursor = database.rawQuery("SELECT CommonName FROM MARKER", null);
//        while (cursor.moveToNext()) {
//            String commonName = cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_MARKER_COMMON_NAME));
//            if (commonName.equals(filterCommonName)) {
//                return true;
//            }
//        }
//        cursor.close();
//        return false;
//    }

    public static void insertFilterEntry(TreeMarker marker) {
        Cursor cursor = database.rawQuery("INSERT INTO FilterEntry(CommonName, LatinName, Filtered) VALUES ('" + marker.getCommonName() + "', '" + marker.getLatinName() + "', 1)", null);
        cursor.moveToFirst();
        cursor.close();
    }

    public static int getLastInsertRowID() {
        Cursor cursor = database.rawQuery("SELECT last_insert_rowid()", null);
        int id = 0;
        while (cursor.moveToNext()) {
            id = cursor.getInt(cursor.getColumnIndex("last_insert_rowid()"));
        }
        cursor.close();
        return id;
    }

    public static String getSystemDate() {
        Calendar cal = Calendar.getInstance();
        Date fullDate = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy");
        return sdf.format(fullDate);
    }

    public static String getSightingDate() {
        Calendar cal = Calendar.getInstance();
        Date fullDate = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy");
        return sdf.format(fullDate);
    }

    public static void sortTypeSwitch(String sortType, ArrayList<Object> treeList) {
        switch (sortType) {
            case DBContract.COLUMN_COMMON_NAME:
                Utility.generateAlphabeticalHeaders(treeList);
                break;
            case DBContract.COLUMN_LATIN_NAME:
                Utility.generateAlphabeticalHeadersLatinName(treeList);
                break;
            case DBContract.COLUMN_MAORI_NAME:
                Utility.generateAlphabeticalHeadersMaoriName(treeList);
                break;
            case DBContract.COLUMN_FAMILY:
                Utility.generateFamilyHeaders(treeList);
                break;
            case DBContract.COLUMN_STRUCTURAL_CLASS:
                Utility.generateGenusHeaders(treeList);
                break;
        }
    }

    public static int checkFilterStatus(String markerCommonName) {
        Cursor cursor = database.rawQuery("SELECT Filtered FROM FilterEntry WHERE CommonName = '" + markerCommonName + "'", null);
        int filteredStatus = 0;
        while (cursor.moveToNext()) {
             filteredStatus = cursor.getInt(cursor.getColumnIndex("Filtered"));
        }
        cursor.close();
        return filteredStatus;
    }
    public static String findTreeAttributeValueGivenName(String commonName, String treeAttribute) {
        String value = null;
        Cursor cursor = database.rawQuery("SELECT " + treeAttribute + " FROM " + DBContract.TABLE_TREE + " WHERE " + DBContract.COLUMN_COMMON_NAME + " = \"" + commonName + "\"", null);
        while (cursor.moveToNext()) {
             value = cursor.getString(cursor.getColumnIndex(treeAttribute));
        }
        cursor.close();
        return value;
    }

    public static String findSightingInfoGivenID(Integer id, String sightingInfo) {
        String value = null;
        Cursor cursor = database.rawQuery("SELECT " + sightingInfo + " FROM " + DBContract.TABLE_SIGHTING + " WHERE " + DBContract.COLUMN_SIGHTING_ID + " = " + id, null);
        while (cursor.moveToNext()) {
            value = cursor.getString(cursor.getColumnIndex(sightingInfo));
        }
        cursor.close();
        return value;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager)  context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }



    public static void archivedUpdatePicture(Context mContext) {
        updateReportPicture(1, R.drawable.tree_tikouka, mContext);
        updateReportPicture(2, R.drawable.tree_nikau, mContext);
        updateReportPicture(3, R.drawable.tree_puriri, mContext);
        updateReportPicture(4, R.drawable.tree_sevenfinger, mContext);
        updateReportPicture(5, R.drawable.tree_fivefinger, mContext);
        updateReportPicture(6, R.drawable.tree_kawakawa, mContext);
        updateReportPicture(7, R.drawable.tree_northernrata, mContext);
        updateReportPicture(8, R.drawable.tree_southernrata, mContext);
        updateReportPicture(9, R.drawable.tree_pohutukawa, mContext);
        updateReportPicture(10, R.drawable.tree_kamahi, mContext);
        updateReportPicture(11, R.drawable.tree_pukatea, mContext);
        updateReportPicture(12, R.drawable.tree_wineberry, mContext);
        updateReportPicture(13, R.drawable.tree_pigeonwood, mContext);
        updateReportPicture(14, R.drawable.tree_kohekohe, mContext);
        updateReportPicture(15, R.drawable.tree_houherelacebark, mContext);
        updateReportPicture(16, R.drawable.tree_horoekalancewood, mContext);
        updateReportPicture(17, R.drawable.tree_horoekalancewood, mContext);
        updateReportPicture(18, R.drawable.tree_honeysuckle, mContext);
        updateReportPicture(19, R.drawable.tree_mahoe, mContext);
        updateReportPicture(20, R.drawable.tree_mahoe, mContext);
        updateReportPicture(21, R.drawable.tree_ngaio, mContext);
        updateReportPicture(22, R.drawable.tree_hinau, mContext);
        updateReportPicture(23, R.drawable.tree_titoki, mContext);
        updateReportPicture(24, R.drawable.tree_tanekaha, mContext);
        updateReportPicture(25, R.drawable.tree_hardbeech, mContext);
        updateReportPicture(26, R.drawable.tree_redbeech, mContext);
        updateReportPicture(27, R.drawable.tree_silverbeech, mContext);
        updateReportPicture(28, R.drawable.tree_marbleleaf, mContext);
        updateReportPicture(29, R.drawable.tree_kowhai, mContext);
        updateReportPicture(30, R.drawable.tree_matai, mContext);
        updateReportPicture(31, R.drawable.tree_miro, mContext);
        updateReportPicture(32, R.drawable.tree_rimu, mContext);
        updateReportPicture(33, R.drawable.tree_blackbeech, mContext);
        updateReportPicture(34, R.drawable.tree_moutainbeech, mContext);
        updateReportPicture(35, R.drawable.tree_totara, mContext);
        updateReportPicture(36, R.drawable.tree_kapuka, mContext);
        updateReportPicture(37, R.drawable.tree_pukatea, mContext); //false
        updateReportPicture(38, R.drawable.tree_karaka, mContext);
        updateReportPicture(39, R.drawable.tree_rangiora, mContext);
        updateReportPicture(40, R.drawable.tree_taraire, mContext);
        updateReportPicture(41, R.drawable.tree_kauri, mContext);
        updateReportPicture(42, R.drawable.tree_karo, mContext);
        updateReportPicture(43, R.drawable.tree_treefuschia, mContext);
//        updateReportPicture(44, R.drawable.tree_tawa, mContext);
//        updateReportPicture(45, R.drawable.tree_akeake, mContext);
//        updateReportPicture(46, R.drawable.tree_tarata, mContext);
//        updateReportPicture(47, R.drawable.tree_kohuhu, mContext);
//        updateReportPicture(48, R.drawable.tree_kanuka, mContext);
//        updateReportPicture(49, R.drawable.tree_manuka, mContext);
    }
}
