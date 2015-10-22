package info.leonardofontana.modemme.model;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by tetsu on 22/10/2015.
 */
public class FeedContract {

    public static final String CONTENT_AUTHORITY ="info.leonardofontana.modemme";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    private static final String PATH_ENTRIES = "entries";
    public static class Entry implements BaseColumns {
        /**
         * MIME type for lists of entries.
         */
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.modemme.entries";
        /**
         * MIME type for individual entries.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.modemme.entry";

        /**
         * Fully qualified URI for "entry" resources.
         */
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_ENTRIES).build();

        /**
         * Table name where records are stored for "entry" resources.
         */
        public static final String TABLE_NAME = "entry";
        /**
         * Atom ID. (Note: Not to be confused with the database primary key, which is _ID.
         */
        public static final String COLUMN_NAME_ENTRY_ID = "entry_id";
        /**
         * Article title
         */
        public static final String COLUMN_NAME_TITLE = "title";
        /**
         * Article hyperlink. Corresponds to the rel="alternate" link in the
         * Atom spec.
         */
        public static final String COLUMN_NAME_LINK = "link";
        /**
         * Date article was published.
         */
        public static final String COLUMN_NAME_PUBLISHED = "published";

        /**
         * enclosure: media with entry
         */
        public static final String COLUMN_MEDIA_LINK = "enclosure_link";

        /**
         * Description of article
         */
        public static final String COLUMN_DESCRIPTION = "description";

        /**
         * Content of the article
         */
        public static final String COLUMN_CONTENT = "content";

        /**
         * Numbers of comments
         */

        public static final String COLUMN_NUMBERS_COMMENT ="numero_commenti";

        /**
         * Link to RSS feed of comments
         */
        public final static String COLUMN_COMMENT ="commenti";

        /**
         * Autore del post
         */
        public final static String COLUMN_AUTHOR ="autore";
    }
}
