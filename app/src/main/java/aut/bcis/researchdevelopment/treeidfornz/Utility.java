package aut.bcis.researchdevelopment.treeidfornz;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import aut.bcis.researchdevelopment.model.ListHeader;
import aut.bcis.researchdevelopment.model.Tree;

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
        }
        catch (Exception ex) {
            Log.i("DATABASE", "Problem updating picture", ex);
            picturePath = "";
        }
        // Updates the database entry for the report to point to the picture
        Cursor cursor = MainActivity.database.rawQuery("UPDATE Tree SET PicturePath = '" + picturePath + "' WHERE ID = " + reportId, null);
        cursor.moveToFirst();
        cursor.close();
    }

    public static void generateAlphabeticalHeaders(ArrayList<Object> treeList) {
        ArrayList<Integer> positionList = new ArrayList<>();
        ArrayList<String> valueList = new ArrayList<>();
        for(int i = 0; i < treeList.size(); i++) {
            Tree tree = (Tree) treeList.get(i);
            if(i == 0) {
                positionList.add(i);
                valueList.add(tree.getCommonName().substring(0, 1)); //get the first letter
            }
            else {
                Tree previousTree = (Tree) treeList.get(i-1);
                if(!tree.getCommonName().substring(0, 1).equalsIgnoreCase(previousTree.getCommonName().substring(0, 1))) {
                    positionList.add(i);
                    valueList.add(tree.getCommonName().substring(0, 1));
                }
            }
        }
        for(int i = 0; i < positionList.size(); i++) {
            treeList.add(positionList.get(i) + i, new ListHeader(valueList.get(i))); //important plus i here as the array shifts the object position
        }
    }

    public static void generateGenusHeaders(ArrayList<Object> treeList) {
        ArrayList<Integer> positionList = new ArrayList<>();
        ArrayList<String> valueList = new ArrayList<>();
        for(int i = 0; i < treeList.size(); i++) {
            Tree tree = (Tree) treeList.get(i);
            if(i == 0) {
                positionList.add(i);
                valueList.add(tree.getGenus());
            }
            else {
                Tree previousTree = (Tree) treeList.get(i-1);
                if(!tree.getGenus().equalsIgnoreCase(previousTree.getGenus())) {
                    positionList.add(i);
                    valueList.add(tree.getGenus());
                }
            }
        }
        for(int i = 0; i < positionList.size(); i++) {
            treeList.add(positionList.get(i) + i, new ListHeader(valueList.get(i))); //important plus i here as the array shifts the object position
        }
    }

    public static void generateFamilyHeaders(ArrayList<Object> treeList) {
        ArrayList<Integer> positionList = new ArrayList<>();
        ArrayList<String> valueList = new ArrayList<>();
        for(int i = 0; i < treeList.size(); i++) {
            Tree tree = (Tree) treeList.get(i);
            if(i == 0) {
                positionList.add(i);
                valueList.add(tree.getFamily().trim());
            }
            else {
                Tree previousTree = (Tree) treeList.get(i-1);
                if(!tree.getFamily().trim().equalsIgnoreCase(previousTree.getFamily().trim())) {
                    positionList.add(i);
                    valueList.add(tree.getFamily().trim());
                }
            }
        }
        for(int i = 0; i < positionList.size(); i++) {
            treeList.add(positionList.get(i) + i, new ListHeader(valueList.get(i))); //important plus i here as the array shifts the object position
        }
    }

    public static String countTreeTraits(String trait, String value) {
        Cursor cursor = MainActivity.database.rawQuery("SELECT Count(*) FROM Tree WHERE " + trait + " = '" + value + "'", null);
        int count = 0;
        while(cursor.moveToNext()) {
            count = cursor.getInt(cursor.getColumnIndex("Count(*)"));
        }
        cursor.close();
        return String.valueOf(count);
    }
    public static String countTreeTraitsGivenMargin(String margin, String trait, String value) { //to be updated.
        Cursor cursor = MainActivity.database.rawQuery("SELECT Count(*) FROM Tree WHERE MARGIN = '" + margin + "' AND " + trait + " = '" + value + "'", null);
        int count = 0;
        while(cursor.moveToNext()) {
            count = cursor.getInt(cursor.getColumnIndex("Count(*)"));
        }
        cursor.close();
        return String.valueOf(count);
    }

    public static String countTreeTraitsGivenArrangement(String arrangement, String trait, String value) { //to be updated.
        Cursor cursor = MainActivity.database.rawQuery("SELECT Count(*) FROM Tree WHERE ARRANGEMENT = '" + arrangement + "' AND " + trait + " = '" + value + "'", null);
        int count = 0;
        while(cursor.moveToNext()) {
            count = cursor.getInt(cursor.getColumnIndex("Count(*)"));
        }
        cursor.close();
        return String.valueOf(count);
    }
    public static void sortTypeSwitch(String sortType, ArrayList<Object> treeList) {
        switch(sortType) {
            case "CommonName":
                Utility.generateAlphabeticalHeaders(treeList);
                break;
            case "Family":
                Utility.generateFamilyHeaders(treeList);
                break;
            case "Genus":
                Utility.generateGenusHeaders(treeList);
                break;
        }
    }


    public static void archievedUpdatePicture(Context mContext) {
        updateReportPicture(1, R.drawable.tree_tikouka, mContext);
        updateReportPicture(2, R.drawable.tree_nikau, mContext);
        updateReportPicture(3, R.drawable.tree_puriri, mContext);
        updateReportPicture(4, R.drawable.tree_mapou, mContext);
        updateReportPicture(5, R.drawable.tree_sevenfinger,mContext);
        updateReportPicture(6, R.drawable.tree_fivefinger, mContext);
        updateReportPicture(7, R.drawable.tree_kawakawa, mContext);
        updateReportPicture(8, R.drawable.tree_northernrata, mContext);
        updateReportPicture(9, R.drawable.tree_southernrata, mContext);
        updateReportPicture(10, R.drawable.tree_pohutukawa, mContext);
        updateReportPicture(11, R.drawable.tree_kamahi, mContext);
        updateReportPicture(12, R.drawable.tree_pukatea, mContext);
        updateReportPicture(13, R.drawable.tree_wineberry, mContext);
        updateReportPicture(14, R.drawable.tree_pigeonwood, mContext);
        updateReportPicture(15, R.drawable.tree_kohekohe, mContext);
        updateReportPicture(16, R.drawable.tree_kaikomako, mContext);
        updateReportPicture(17, R.drawable.tree_houherelacebark, mContext);
        updateReportPicture(18, R.drawable.tree_horoekalancewood, mContext);
        updateReportPicture(19, R.drawable.tree_honeysuckle, mContext);
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
        updateReportPicture(32, R.drawable.tree_kahikatea, mContext);
        updateReportPicture(33, R.drawable.tree_rimu, mContext);
        updateReportPicture(34, R.drawable.tree_blackbeech, mContext);
        updateReportPicture(35, R.drawable.tree_moutainbeech, mContext);
        updateReportPicture(36, R.drawable.tree_totara, mContext);
        updateReportPicture(37, R.drawable.tree_kapuka, mContext);
        updateReportPicture(38, R.drawable.tree_karaka, mContext);
        updateReportPicture(39, R.drawable.tree_rangiora, mContext);
        updateReportPicture(40, R.drawable.tree_taraire, mContext);
        updateReportPicture(41, R.drawable.tree_kauri, mContext);
        updateReportPicture(42, R.drawable.tree_karo, mContext);
        updateReportPicture(43, R.drawable.tree_treefuschia, mContext);
        updateReportPicture(44, R.drawable.tree_tawa, mContext);
        updateReportPicture(45, R.drawable.tree_akeake, mContext);
        updateReportPicture(46, R.drawable.tree_tarata, mContext);
        updateReportPicture(47, R.drawable.tree_kohuhu, mContext);
        updateReportPicture(48, R.drawable.tree_kanuka, mContext);
        updateReportPicture(49, R.drawable.tree_manuka, mContext);
    }
}
