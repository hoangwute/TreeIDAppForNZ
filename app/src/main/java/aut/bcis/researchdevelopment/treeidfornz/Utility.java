package aut.bcis.researchdevelopment.treeidfornz;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;

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

    public void archievedUpdatePicture(Context mContext) {
        updateReportPicture(1, R.drawable.tikouka, mContext);
        updateReportPicture(2, R.drawable.nikau, mContext);
//        updateReportPicture(3, R.drawable.puriri);
//        updateReportPicture(4, R.drawable.mapou);
//        updateReportPicture(5, R.drawable.sevenfinger);
//        updateReportPicture(6, R.drawable.fivefinger);
//        updateReportPicture(7, R.drawable.kawakawa);
//        updateReportPicture(8, R.drawable.northernrata);
//        updateReportPicture(9, R.drawable.southernrata);
//        updateReportPicture(10, R.drawable.pohutukawa);
//        updateReportPicture(11, R.drawable.kamahi);
//        updateReportPicture(12, R.drawable.pukatea);
//        updateReportPicture(13, R.drawable.wineberry);
//        updateReportPicture(14, R.drawable.pigeonwood);
//        updateReportPicture(15, R.drawable.kohekohe);
//        updateReportPicture(16, R.drawable.kaikomako);
//        updateReportPicture(17, R.drawable.houherelacebark);
//        updateReportPicture(18, R.drawable.horoekalancewood);
//        updateReportPicture(19, R.drawable.honeysuckle);
//        updateReportPicture(20, R.drawable.mahoe);
//        updateReportPicture(21, R.drawable.ngaio);
//        updateReportPicture(22, R.drawable.hinau);
//        updateReportPicture(23, R.drawable.titoki);
//        updateReportPicture(24, R.drawable.tanekaha);
//        updateReportPicture(25, R.drawable.hardbeech);
//        updateReportPicture(26, R.drawable.redbeech);
//        updateReportPicture(27, R.drawable.silverbeech);
//        updateReportPicture(28, R.drawable.marbleleaf);
//        updateReportPicture(29, R.drawable.kowhai);
//        updateReportPicture(30, R.drawable.matai);
//        updateReportPicture(31, R.drawable.miro);
//        updateReportPicture(32, R.drawable.whitepine);
//        updateReportPicture(33, R.drawable.rimu);
//        updateReportPicture(34, R.drawable.blackbeech);
//        updateReportPicture(35, R.drawable.moutainbeech);
//        updateReportPicture(36, R.drawable.totara);
//        updateReportPicture(37, R.drawable.kapuka);
//        updateReportPicture(38, R.drawable.karaka);
//        updateReportPicture(39, R.drawable.rangiora);
//        updateReportPicture(40, R.drawable.taraire);
//        updateReportPicture(41, R.drawable.kauri);
//        updateReportPicture(42, R.drawable.karo);
//        updateReportPicture(43, R.drawable.treefuschia);
//        updateReportPicture(44, R.drawable.tawa);
//        updateReportPicture(45, R.drawable.akeake);
//        updateReportPicture(46, R.drawable.lemonwood);
//        updateReportPicture(47, R.drawable.kohuhu);
//        updateReportPicture(48, R.drawable.kanuka);
//        updateReportPicture(49, R.drawable.manuka);
    }
}
