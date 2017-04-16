package aut.bcis.researchdevelopment.database;

/**
 * Created by admin on 25-Feb-17.
 */

public class DBContract {
    //------------------------------------- Tree table ------------------------------------
    public static final String TABLE_TREE = "Tree";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_COMMON_NAME = "CommonName";
    public static final String COLUMN_LATIN_NAME = "LatinName";
    public static final String COLUMN_MAORI_NAME = "MaoriName";
    public static final String COLUMN_FAMILY = "Family";
    public static final String COLUMN_GENUS = "Genus";
    public static final String COLUMN_LIKED = "Liked";
    public static final String COLUMN_PICTURE_PATH = "PicturePath";
    public static final String COLUMN_ARRANGEMENT = "Arrangement";
    public static final String COLUMN_MARGIN = "Margin";
    //------------------------------------- TreeMarker table ------------------------------------
    public static final String TABLE_MARKER = "Marker";
    public static final String COLUMN_MARKER_ID = "ID";
    public static final String COLUMN_MARKER_COMMON_NAME = "CommonName";
    public static final String COLUMN_MARKER_LATIN_NAME = "LatinName";
    public static final String COLUMN_MARKER_LOCATION = "Location";
    public static final String COLUMN_MARKER_NOTE = "Note";
    public static final String COLUMN_MARKER_IMAGE = "ImagePath";
    public static final String COLUMN_MARKER_LATITUDE = "Latitude";
    public static final String COLUMN_MARKER_LONGITUDE = "Longitude";
    public static final String COLUMN_MARKER_FILTERED = "Filtered";


}
