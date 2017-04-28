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
    public static final String COLUMN_STRUCTURAL_CLASS = "StructuralClass";
    public static final String COLUMN_LIKED = "Liked";
    public static final String COLUMN_MAIN_PICTURE = "MainPicture";
    public static final String COLUMN_LEAFLET_ARRANGEMENT = "LeafletArrangement";
    public static final String COLUMN_LEAF_MARGIN = "LeafMargin";
    public static final String COLUMN_LEAFLET_SHAPE = "LeafletShape";
    public static final String COLUMN_LEAF_TIP = "LeafTip";
    public static final String COLUMN_FLOWER_COLOUR = "FlowerColour";
    public static final String COLUMN_FRUIT_COLOUR = "FruitColour";
    public static final String COLUMN_CONE_TYPE = "ConeType  ";
    public static final String COLUMN_TRUNK_TEXTURE = "TrunkTexture";
    public static final String COLUMN_TRUNK_COLOUR = "TrunkColour";
    public static final String COLUMN_SYNONYMS = "Synonyms";
    public static final String COLUMN_LOCATION_MAP = "LocationMap";
    public static final String COLUMN_DISTRIBUTION = "Distribution";
    public static final String COLUMN_MEDICAL_USE = "MedicalUse";
    public static final String COLUMN_POISONOUS = "Poisonous";
    public static final String COLUMN_DID_YOU_KNOW = "DidYouKnow";
    public static final String COLUMN_DESCRIPTION = "Description";
    public static final String COLUMN_FLOWERING = "Flowering";
    public static final String COLUMN_FRUITING = "Fruiting";
    public static final String COLUMN_ETYMOLOGY = "Etymology";


    //------------------------------------- Sighting table ------------------------------------
    public static final String TABLE_SIGHTING = "Sighting";
    public static final String COLUMN_SIGHTING_ID = "ID";
    public static final String COLUMN_SIGHTING_COMMON_NAME = "CommonName";
    public static final String COLUMN_SIGHTING_MAORI_NAME = "LatinName";
    public static final String COLUMN_SIGHTING_LATIN_NAME = "LatinName";
    public static final String COLUMN_SIGHTING_LOCATION = "Location";
    public static final String COLUMN_SIGHTING_NOTE = "Note";
    public static final String COLUMN_SIGHTING_DATE = "Date";
    public static final String COLUMN_TIME_STAMP = "TimeStamp";
    public static final String COLUMN_SIGHTING_PICTURE = "Picture";
    public static final String COLUMN_SIGHTING_LONGITUDE = "Longitude";
    public static final String COLUMN_SIGHTING_LATITUDE = "Latitude";


}
