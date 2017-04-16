package aut.bcis.researchdevelopment.treeidfornz;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.Marker;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Random;

import aut.bcis.researchdevelopment.database.DBContract;
import aut.bcis.researchdevelopment.database.DBInitialization;
import aut.bcis.researchdevelopment.model.ListHeader;
import aut.bcis.researchdevelopment.model.Tree;
import aut.bcis.researchdevelopment.model.TreeMarker;

import static aut.bcis.researchdevelopment.treeidfornz.MainActivity.database;
import static aut.bcis.researchdevelopment.treeidfornz.R.id.chkGenus;

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
        Cursor cursor = database.rawQuery("UPDATE Tree SET " + DBContract.COLUMN_PICTURE_PATH + " = '" + picturePath + "' WHERE ID = " + reportId, null);
        cursor.moveToFirst();
        cursor.close();
    }

    public static String getTakenPicturePath(Bitmap bitmapPicture, Context mContext) {
        // Saves the new picture to the internal storage with the unique identifier of the report as
        // the name. That way, there will never be two report pictures with the same name.
        Random rand = new Random();
        int randomNum = rand.nextInt((10000000 - 1) + 1) + 1;
        String picturePath = "";
        File internalStorage = mContext.getDir("TakenPictures", Context.MODE_PRIVATE);
        File reportFilePath = new File(internalStorage, randomNum + ".jpg");
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
        return picturePath;
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

    public static void generateGenusHeaders(ArrayList<Object> treeList) {
        ArrayList<Integer> positionList = new ArrayList<>();
        ArrayList<String> valueList = new ArrayList<>();
        for (int i = 0; i < treeList.size(); i++) {
            Tree tree = (Tree) treeList.get(i);
            if (i == 0) {
                positionList.add(i);
                valueList.add(tree.getGenus());
            } else {
                Tree previousTree = (Tree) treeList.get(i - 1);
                if (!tree.getGenus().equalsIgnoreCase(previousTree.getGenus())) {
                    positionList.add(i);
                    valueList.add(tree.getGenus());
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


    public static int countSightedTreeType(String commonName) {
        Cursor cursor = database.rawQuery("SELECT COUNT(*) FROM " + DBContract.TABLE_MARKER + " WHERE " + DBContract.COLUMN_MARKER_COMMON_NAME + " = '" + commonName + "'", null);
        int count = 0;
        while (cursor.moveToNext()) {
            count = cursor.getInt(cursor.getColumnIndex("COUNT(*)"));
        }
        cursor.close();
        return count;
    }

    public static int countAllSightedTree() {
        Cursor cursor = database.rawQuery("SELECT COUNT(*) FROM " + DBContract.TABLE_MARKER, null);
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

    public static boolean foundInsertedFilter(String filterCommonName) {
        Cursor cursor = database.rawQuery("SELECT CommonName FROM MARKER", null);
        while (cursor.moveToNext()) {
            String commonName = cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_MARKER_COMMON_NAME));
            if (commonName.equals(filterCommonName)) {
                return true;
            }
        }
        cursor.close();
        return false;
    }

    public static void insertFilterEntry(TreeMarker marker) {
        Cursor cursor = database.rawQuery("INSERT INTO FilterEntry(CommonName, LatinName, Filtered) VALUES ('" + marker.getCommonName() + "', '" + marker.getLatinName() + "', 1)", null);
        cursor.moveToFirst();
        cursor.close();
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
            case DBContract.COLUMN_GENUS:
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
        Cursor cursor = database.rawQuery("SELECT " + treeAttribute + " FROM " + DBContract.TABLE_TREE + " WHERE " + DBContract.COLUMN_COMMON_NAME + " = '" + commonName + "'", null);
        while (cursor.moveToNext()) {
             value = cursor.getString(cursor.getColumnIndex(treeAttribute));
        }
        cursor.close();
        return value;
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
