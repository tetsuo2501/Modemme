package info.leonardofontana.modemme.model;

/**
 * Created by tetsu on 23/10/2015.
 */
public class ProjectionContract {
    public static final String[] PROJECTION = new String[]{
            FeedContract.Entry._ID,
            FeedContract.Entry.COLUMN_NAME_TITLE,
            FeedContract.Entry.COLUMN_DESCRIPTION,
            //FeedContract.Entry.COLUMN_CONTENT,
            FeedContract.Entry.COLUMN_MEDIA_LINK,
            FeedContract.Entry.COLUMN_NAME_LINK,
            FeedContract.Entry.COLUMN_NAME_PUBLISHED,
            FeedContract.Entry.COLUMN_NUMBERS_COMMENT
    };
    public static final int PROJ_ID = 0;
    public static final int PROJ_TITLE = 1;
    public static final int PROJ_DESCRIPTION = 2;
    //public static final int PROJ_CONTENT = 3;
    public static final int PROJ_MEDIA = 3;
    public static final int PROJ_LINK = 4;
    public static final int PROJ_DATE = 5;
    public static final int PROJ_NUM_COM = 6;
}
