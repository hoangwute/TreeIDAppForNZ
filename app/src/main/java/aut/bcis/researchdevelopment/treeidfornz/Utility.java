package aut.bcis.researchdevelopment.treeidfornz;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import aut.bcis.researchdevelopment.model.ListHeader;

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

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromPath(String path,
                                                     int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    public static void generateAlphabeticalHeaders(ArrayList<Object> treeList, int cursorCounter) {
        switch(cursorCounter) {
            case 0:
                treeList.add(new ListHeader("A"));
                break;
            case 1:
                treeList.add(new ListHeader("H"));
                break;
            case 4:
                treeList.add(new ListHeader("K"));
                break;
            case 17:
                treeList.add(new ListHeader("M"));
                break;
            case 23:
                treeList.add(new ListHeader("N"));
                break;
            case 26:
                treeList.add(new ListHeader("P"));
                break;
            case 32:
                treeList.add(new ListHeader("R"));
                break;
            case 35:
                treeList.add(new ListHeader("S"));
                break;
            case 36:
                treeList.add(new ListHeader("T"));
                break;
            case 48:
                treeList.add(new ListHeader("W"));
                break;
        }
    }

    public static void generateFamilyHeaders(ArrayList<Object> treeList, int cursorCounter) {
        switch(cursorCounter) {
            case 0:
                treeList.add(new ListHeader("Araliaceae"));
                break;
            case 3:
                treeList.add(new ListHeader("Araucariaceae"));
                break;
            case 4:
                treeList.add(new ListHeader("Arecaceae"));
                break;
            case 5:
                treeList.add(new ListHeader("Asteraceae"));
                break;
            case 6:
                treeList.add(new ListHeader("Atherospermataceae"));
                break;
            case 7:
                treeList.add(new ListHeader("Carpodetaceae"));
                break;
            case 8:
                treeList.add(new ListHeader("Corynocarpaceae"));
                break;
            case 9:
                treeList.add(new ListHeader("Cunoniaceae"));
                break;
            case 10:
                treeList.add(new ListHeader("Elaeocarpaceae"));
                break;
            case 12:
                treeList.add(new ListHeader("Fabaceae"));
                break;
            case 13:
                treeList.add(new ListHeader("Griseliniaceae"));
                break;
            case 14:
                treeList.add(new ListHeader("Lamiaceae"));
                break;
            case 15:
                treeList.add(new ListHeader("Lauraceae"));
                break;
            case 17:
                treeList.add(new ListHeader("Laxmanniaceae"));
                break;
            case 18:
                treeList.add(new ListHeader("Malvaceae"));
                break;
            case 19:
                treeList.add(new ListHeader("Meliaceae"));
                break;
            case 20:
                treeList.add(new ListHeader("Monimiaceae"));
                break;
            case 21:
                treeList.add(new ListHeader("Myrsinaceae"));
                break;
            case 22:
                treeList.add(new ListHeader("Myrtaceae"));
                break;
            case 27:
                treeList.add(new ListHeader("Nothofagaceae"));
                break;
            case 32:
                treeList.add(new ListHeader("Onagraceae"));
                break;
            case 33:
                treeList.add(new ListHeader("Pennantiaceae"));
                break;
            case 34:
                treeList.add(new ListHeader("Piperaceae"));
                break;
            case 35:
                treeList.add(new ListHeader("Pittosporaceae"));
                break;
            case 38:
                treeList.add(new ListHeader("Podocarpaceae"));
                break;
            case 43:
                treeList.add(new ListHeader("Proteaceae"));
                break;
            case 44:
                treeList.add(new ListHeader("Prumnopityaceae"));
                break;
            case 45:
                treeList.add(new ListHeader("Sapindaceae"));
                break;
            case 47:
                treeList.add(new ListHeader("Scrophulariaceae"));
                break;
            case 48:
                treeList.add(new ListHeader("Violaceae"));
                break;

        }
    }

    public static void generateGenusHeaders(ArrayList<Object> treeList, int cursorCounter) {
        switch(cursorCounter) {
            case 0:
                treeList.add(new ListHeader("Agathis"));
                break;
            case 1:
                treeList.add(new ListHeader("Alectryon"));
                break;
            case 2:
                treeList.add(new ListHeader("Aristotelia"));
                break;
            case 3:
                treeList.add(new ListHeader("Beilschmiedia"));
                break;
            case 5:
                treeList.add(new ListHeader("Brachyglottis"));
                break;
            case 6:
                treeList.add(new ListHeader("Carpodetus"));
                break;
            case 7:
                treeList.add(new ListHeader("Cordyline"));
                break;
            case 8:
                treeList.add(new ListHeader("Corynocarpus"));
                break;
            case 9:
                treeList.add(new ListHeader("Dacrycarpus"));
                break;
            case 10:
                treeList.add(new ListHeader("Dacrydium"));
                break;
            case 11:
                treeList.add(new ListHeader("Dodonaea"));
                break;
            case 12:
                treeList.add(new ListHeader("Dysoxylum"));
                break;
            case 13:
                treeList.add(new ListHeader("Elaeocarpus"));
                break;
            case 14:
                treeList.add(new ListHeader("Fuchsia"));
                break;
            case 15:
                treeList.add(new ListHeader("Fuscospora"));
                break;
            case 18:
                treeList.add(new ListHeader("Griselinia"));
                break;
            case 19:
                treeList.add(new ListHeader("Hedycarya"));
                break;
            case 20:
                treeList.add(new ListHeader("Hoheria"));
                break;
            case 21:
                treeList.add(new ListHeader("Knightia"));
                break;
            case 22:
                treeList.add(new ListHeader("Kunzea"));
                break;
            case 23:
                treeList.add(new ListHeader("Laurelia"));
                break;
            case 24:
                treeList.add(new ListHeader("Leptospermum"));
                break;
            case 25:
                treeList.add(new ListHeader("Lophozonia"));
                break;
            case 26:
                treeList.add(new ListHeader("Macropiper"));
                break;
            case 27:
                treeList.add(new ListHeader("Melicytus"));
                break;
            case 28:
                treeList.add(new ListHeader("Metrosideros"));
                break;
            case 31:
                treeList.add(new ListHeader("Myoporum"));
                break;
            case 32:
                treeList.add(new ListHeader("Myrsine"));
                break;
            case 33:
                treeList.add(new ListHeader("Nothofagus"));
                break;
            case 34:
                treeList.add(new ListHeader("Pennantia"));
                break;
            case 35:
                treeList.add(new ListHeader("Phyllocladus"));
                break;
            case 36:
                treeList.add(new ListHeader("Pittosporum"));
                break;
            case 39:
                treeList.add(new ListHeader("Podocarpus"));
                break;
            case 40:
                treeList.add(new ListHeader("Prumnopitys"));
                break;
            case 42:
                treeList.add(new ListHeader("Pseudopanax"));
                break;
            case 44:
                treeList.add(new ListHeader("Rhopalostylis"));
                break;
            case 45:
                treeList.add(new ListHeader("Schefflera"));
                break;
            case 46:
                treeList.add(new ListHeader("Sophora"));
                break;
            case 47:
                treeList.add(new ListHeader("Vitex"));
                break;
            case 48:
                treeList.add(new ListHeader("Weinmannia"));
                break;

        }
    }

    public void archievedUpdatePicture(Context mContext) {
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
