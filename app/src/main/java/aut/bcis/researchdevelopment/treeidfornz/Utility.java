package aut.bcis.researchdevelopment.treeidfornz;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.style.LeadingMarginSpan;
import android.view.Display;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import aut.bcis.researchdevelopment.database.DBContract;
import aut.bcis.researchdevelopment.model.ListHeader;
import aut.bcis.researchdevelopment.model.Sighting;
import aut.bcis.researchdevelopment.model.Tree;

import static aut.bcis.researchdevelopment.treeidfornz.MainActivity.database;

/**
 * Created by VS9 X64Bit on 28/08/2016.
 */
public class Utility {

    public static void deleteSightingPicture(long reportId, Context mContext) {
        File internalStorage = mContext.getDir("SightingPictures", Context.MODE_PRIVATE);
        File reportFilePath = new File(internalStorage, reportId + ".jpg");
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


    public static int countSightings(String treeName) {
        Cursor cursor = database.rawQuery("SELECT COUNT(*) FROM " + DBContract.TABLE_SIGHTING + " WHERE CommonName = \"" + treeName + "\"", null);
        int count = 0;
        while (cursor.moveToNext()) {
            count = cursor.getInt(cursor.getColumnIndex("COUNT(*)"));
        }
        cursor.close();
        return count;
    }

    public static int countTreeFound(String countQuery) {
        Cursor cursor = database.rawQuery(countQuery, null);
        int count = 0;
        while (cursor.moveToNext()) {
            count = cursor.getInt(cursor.getColumnIndex("COUNT(*)"));
        }
        cursor.close();
        return count;
    }

    public static int countAllTrees() {
        Cursor cursor = database.rawQuery("SELECT COUNT(*) FROM " + DBContract.TABLE_TREE, null);
        int count = 0;
        while (cursor.moveToNext()) {
            count = cursor.getInt(cursor.getColumnIndex("COUNT(*)"));
        }
        cursor.close();
        return count;
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

    public static String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month-1];
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
    public static SpannableString createIndentedText(String text, int marginFirstLine, int marginNextLines) {
        SpannableString result=new SpannableString(text);
        result.setSpan(new LeadingMarginSpan.Standard(marginFirstLine, marginNextLines),0,text.length(),0);
        return result;
    }


    public static Bitmap decodeFile(File f){
        try {
            //decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f),null,o);
            //Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE=70;
            int width_tmp=o.outWidth, height_tmp=o.outHeight;
            int scale=1;
            while(true){
                if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
                    break;
                width_tmp/=2;
                height_tmp/=2;
                scale++;
            }

            //decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {

        }
        return null;
    }

    public static HashMap<Integer, Integer> mainImageMap() {
        HashMap<Integer, Integer> imageMap = new HashMap<Integer, Integer>();
        imageMap.put(1, R.drawable.tree_cordyline_australis);
        imageMap.put(2, R.drawable.tree_rhopalostylis_sapida2);
        imageMap.put(3, R.drawable.tree_vitex_lucens);
        imageMap.put(4, R.drawable.tree_schefflera_digitata);
        imageMap.put(5, R.drawable.tree_pseudopanax_arboreus);
        imageMap.put(6, R.drawable.tree_piper_excelsum_subsp_excelsum);
        imageMap.put(7, R.drawable.tree_metrosideros_robusta);
        imageMap.put(8, R.drawable.tree_metrosideros_umbulata);
        imageMap.put(9, R.drawable.tree_metrosideros_excelsa);
        imageMap.put(10, R.drawable.tree_weinmannia_racemosa);
        imageMap.put(11, R.drawable.tree_laurelia_novae_zelandiae);
        imageMap.put(12, R.drawable.tree_aristotelia_serrata);
        imageMap.put(13, R.drawable.tree_hedycara_arborea);
        imageMap.put(14, R.drawable.tree_dysoxylum_spectabile);
        imageMap.put(15, R.drawable.tree_hoheria_populnea);
        imageMap.put(16, R.drawable.tree_pseudopanax_crassifolius);
        imageMap.put(17, R.drawable.tree_pseudopanax_crassifolius_juvenile);
        imageMap.put(18, R.drawable.tree_knightia_excelsa);
        imageMap.put(19, R.drawable.tree_melicytus_ramiflorus);
        imageMap.put(20, R.drawable.tree_melicytus_macrophyllus);
        imageMap.put(21, R.drawable.tree_myoporum_laetum);
        imageMap.put(22, R.drawable.tree_elaeocarpus_dentatus);
        imageMap.put(23, R.drawable.tree_alectryon_excelsus);
        imageMap.put(24, R.drawable.tree_phyllocladus_trichomanoides);
        imageMap.put(25, R.drawable.tree_fuscospora_truncata);
        imageMap.put(26, R.drawable.tree_fuscospora_fusca);
        imageMap.put(27, R.drawable.tree_nothofagus_menziesii);
        imageMap.put(28, R.drawable.tree_carpodetus_serratus);
        imageMap.put(29, R.drawable.tree_sophora_microphylla);
        imageMap.put(30, R.drawable.tree_prumnopitys_taxifolia);
        imageMap.put(31, R.drawable.tree_prumnopitys_ferruginea);
        imageMap.put(32, R.drawable.tree_dacrydium_cupressinum);
        imageMap.put(33, R.drawable.tree_fuscospora_solandri);
        imageMap.put(34, R.drawable.tree_fuscospora_cliffortioides);
        imageMap.put(35, R.drawable.tree_podocarpus_totara);
        imageMap.put(36, R.drawable.tree_griselinia_littoralis);
        imageMap.put(37, R.drawable.tree_griselinia_lucida);
        imageMap.put(38, R.drawable.tree_corynocarpus_laevigatus);
        imageMap.put(39, R.drawable.tree_brachyglottis_repanda);
        imageMap.put(40, R.drawable.tree_beilschmiedia_tarairi);
        imageMap.put(41, R.drawable.tree_agathis_australis);
        imageMap.put(42, R.drawable.tree_pittosporum_crassifolium);
        imageMap.put(43, R.drawable.tree_fuchsia_excorticata);
        imageMap.put(44, R.drawable.tree_beilschmiedia_tawa);
        imageMap.put(45, R.drawable.tree_pittosporum_eugenioides);
        imageMap.put(46, R.drawable.tree_myrsine_australis);
        imageMap.put(47, R.drawable.tree_pittosporum_tenuifolium);
        imageMap.put(48, R.drawable.tree_kunzea_ericoides);
        imageMap.put(49, R.drawable.tree_leptospermum_scoparium);
        return imageMap;
    }

    public static HashMap<Integer, Integer> secondImageMap() {
        HashMap<Integer, Integer> imageMap = new HashMap<Integer, Integer>();
        imageMap.put(1, R.drawable.tree_cordyline_australis2);
        imageMap.put(2, R.drawable.tree_rhopalostylis_sapida);
        imageMap.put(3, R.drawable.tree_vitex_lucens2);
        imageMap.put(5, R.drawable.tree_pseudopanax_arboreus2);
        imageMap.put(6, R.drawable.tree_piper_excelsum_subsp_excelsum2);
        imageMap.put(8, R.drawable.tree_metrosideros_umbulata2);
        imageMap.put(9, R.drawable.tree_metrosideros_excelsa2);
        imageMap.put(10, R.drawable.tree_weinmannia_racemosa2);
        imageMap.put(11, R.drawable.tree_laurelia_novae_zelandiae2);
        imageMap.put(12, R.drawable.tree_aristotelia_serrata2);
        imageMap.put(13, R.drawable.tree_hedycara_arborea2);
        imageMap.put(14, R.drawable.tree_dysoxylum_spectabile2);
        imageMap.put(15, R.drawable.tree_hoheria_populnea2);
        imageMap.put(17, R.drawable.tree_pseudopanax_crassifolius_juvenile2);
        imageMap.put(18, R.drawable.tree_knightia_excelsa2);
        imageMap.put(19, R.drawable.tree_melicytus_ramiflorus2);
        imageMap.put(21, R.drawable.tree_myoporum_laetum2);
        imageMap.put(22, R.drawable.tree_elaeocarpus_dentatus2);
        imageMap.put(23, R.drawable.tree_alectryon_excelsus2);
        imageMap.put(24, R.drawable.tree_phyllocladus_trichomanoides2);
        imageMap.put(27, R.drawable.tree_nothofagus_menziezii2);
        imageMap.put(30, R.drawable.tree_prumnopitys_taxifolia2);
        imageMap.put(31, R.drawable.tree_prumnopitys_ferruginea2);
        imageMap.put(32, R.drawable.tree_dacrydium_cupressinum2);
        imageMap.put(33, R.drawable.tree_fuscospora_solandri2);
        imageMap.put(34, R.drawable.tree_fuscospora_cliffortioides2);
        imageMap.put(35, R.drawable.tree_podocarpus_totara2);
        imageMap.put(37, R.drawable.tree_griselinia_lucida2);
        imageMap.put(38, R.drawable.tree_corynocarpus_laevigatus2);
        imageMap.put(39, R.drawable.tree_brachyglottis_repanda2);
        imageMap.put(40, R.drawable.tree_beilschmiedia_tarairi2);
        imageMap.put(41, R.drawable.tree_agathis_australis2);
        imageMap.put(42, R.drawable.tree_pittosporum_crassifolium2);
        imageMap.put(43, R.drawable.tree_fuchsia_excorticata2);
        imageMap.put(44, R.drawable.tree_beilschmiedia_tawa2);
        imageMap.put(45, R.drawable.tree_pittosporum_eugenioides2);
        imageMap.put(46, R.drawable.tree_myrsine_australis2);
        imageMap.put(47, R.drawable.tree_pittosporum_tenuifolium2);
        imageMap.put(48, R.drawable.tree_kunzea_ericoides2);
        imageMap.put(49, R.drawable.tree_leptospermum_scoparium2);
        return imageMap;
    }

    public static HashMap<Integer, Integer> thirdImageMap() {
        HashMap<Integer, Integer> imageMap = new HashMap<Integer, Integer>();
        imageMap.put(2, R.drawable.tree_rhopalostylis_sapida3);
        imageMap.put(5, R.drawable.tree_pseudopanax_arboreus3);
        imageMap.put(8, R.drawable.tree_metrosideros_umbulata3);
        imageMap.put(9, R.drawable.tree_metrosideros_excelsa3);
        imageMap.put(14, R.drawable.tree_dysoxylum_spectabile3);
        imageMap.put(15, R.drawable.tree_hoheria_populnea3);
        imageMap.put(17, R.drawable.tree_pseudopanax_crassifolius_juvenile3);
        imageMap.put(18, R.drawable.tree_knightia_excelsa3);
        imageMap.put(19, R.drawable.tree_melicytus_ramiflorus3);
        imageMap.put(22, R.drawable.tree_elaeocarpus_dentatus3);
        imageMap.put(23, R.drawable.tree_alectryon_excelsus3);
        imageMap.put(24, R.drawable.tree_phyllocladus_trichomanoides3);
        imageMap.put(30, R.drawable.tree_prumnopitys_taxifolia3);
        imageMap.put(32, R.drawable.tree_dacrydium_cupressinum3);
        imageMap.put(34, R.drawable.tree_fuscospora_cliffortioides3);
        imageMap.put(35, R.drawable.tree_podocarpus_totara3);
        imageMap.put(37, R.drawable.tree_griselinia_lucida3);
        imageMap.put(38, R.drawable.tree_corynocarpus_laevigatus3);
        imageMap.put(39, R.drawable.tree_brachyglottis_repanda3);
        imageMap.put(41, R.drawable.tree_agathis_australis3);
        imageMap.put(42, R.drawable.tree_pittosporum_crassifolium3);
        imageMap.put(44, R.drawable.tree_beilschmiedia_tawa3);
        imageMap.put(46, R.drawable.tree_myrsine_australis3);
        imageMap.put(47, R.drawable.tree_pittosporum_tenuifolium3);
        imageMap.put(48, R.drawable.tree_kunzea_ericoides3);
        return imageMap;
    }
    public static HashMap<Integer, Integer> fourthImageMap() {
        HashMap<Integer, Integer> imageMap = new HashMap<Integer, Integer>();
        imageMap.put(2, R.drawable.tree_rhopalostylis_sapida4);
        imageMap.put(5, R.drawable.tree_pseudopanax_arboreus4);
        imageMap.put(15, R.drawable.tree_hoheria_populnea4);
        imageMap.put(18, R.drawable.tree_knightia_excelsa4);
        imageMap.put(23, R.drawable.tree_alectryon_excelsus4);
        imageMap.put(34, R.drawable.tree_fuscospora_cliffortioides4);
        imageMap.put(35, R.drawable.tree_podocarpus_totara4);
        imageMap.put(37, R.drawable.tree_griselinia_lucida4);
        imageMap.put(38, R.drawable.tree_corynocarpus_laevigatus4);
        imageMap.put(39, R.drawable.tree_brachyglottis_repanda4);
        imageMap.put(41, R.drawable.tree_agathis_australis4);
        imageMap.put(44, R.drawable.tree_beilschmiedia_tawa4);
        imageMap.put(46, R.drawable.tree_myrsine_australis4);
        imageMap.put(47, R.drawable.tree_pittosporum_tenuifolium4);
        imageMap.put(48, R.drawable.tree_kunzea_ericoides4);
        return imageMap;
    }
    public static HashMap<Integer, Integer> fifthImageMap() {
        HashMap<Integer, Integer> imageMap = new HashMap<Integer, Integer>();
        imageMap.put(2, R.drawable.tree_rhopalostylis_sapida5);
        imageMap.put(18, R.drawable.tree_knightia_excelsa5);
        imageMap.put(34, R.drawable.tree_fuscospora_cliffortioides5);
        imageMap.put(38, R.drawable.tree_corynocarpus_laevigatus5);
        imageMap.put(41, R.drawable.tree_agathis_australis5);
        imageMap.put(46, R.drawable.tree_myrsine_australis5);
        imageMap.put(48, R.drawable.tree_kunzea_ericoides5);
        return imageMap;
    }
    public static HashMap<Integer, Integer> sixthImageMap() {
        HashMap<Integer, Integer> imageMap = new HashMap<Integer, Integer>();
        imageMap.put(18, R.drawable.tree_knightia_excelsa6);
        imageMap.put(34, R.drawable.tree_fuscospora_cliffortioides6);
        imageMap.put(41, R.drawable.tree_agathis_australis6);
        return imageMap;
    }
    public static HashMap<Integer, Integer> seventhImageMap() {
        HashMap<Integer, Integer> imageMap = new HashMap<Integer, Integer>();
        imageMap.put(34, R.drawable.tree_fuscospora_cliffortioides7);
        return imageMap;
    }
}
