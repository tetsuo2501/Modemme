package info.leonardofontana.modemme.model;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by tetsu on 22/10/2015.
 */
public class DBHelper extends SQLiteOpenHelper {

    public final static int DB_VERSION = 1;
    public static final String DB_NAME ="feed.db";
    public static final String TYPE_TEXT = " TEXT";
    public static final String INT_TYPE = " INTEGER";
    public static final String COMMA = ",";

    public static final String DB_CREATE =
            "CREATE TABLE "+FeedContract.Entry.TABLE_NAME + " ("+
                    //ID
                    FeedContract.Entry._ID + " INTEGER PRIMARY KEY," +
                    //ID del content Provider
                    FeedContract.Entry.COLUMN_NAME_ENTRY_ID + TYPE_TEXT + COMMA +
                    //Titolo
                    FeedContract.Entry.COLUMN_NAME_TITLE + TYPE_TEXT + COMMA +
                    //Descrizione
                    FeedContract.Entry.COLUMN_DESCRIPTION + TYPE_TEXT + COMMA +
                    //Link
                    FeedContract.Entry.COLUMN_NAME_LINK + TYPE_TEXT +COMMA +
                    //Link immagine
                    FeedContract.Entry.COLUMN_MEDIA_LINK + TYPE_TEXT+COMMA+
                    //Numero Commenti
                    FeedContract.Entry.COLUMN_NUMBERS_COMMENT + INT_TYPE +COMMA+
                    //Link Commenti
                    FeedContract.Entry.COLUMN_COMMENT + TYPE_TEXT +COMMA+
                    //Contenuto
                    FeedContract.Entry.COLUMN_CONTENT + TYPE_TEXT + COMMA +
                    //Data Pubblicazione
                    FeedContract.Entry.COLUMN_NAME_PUBLISHED + INT_TYPE +
                    //Autore del Post
                    FeedContract.Entry.COLUMN_AUTHOR + TYPE_TEXT +
                    ")";


    /**
     * Crea il database
     * @param db Ritorna il Database generato
     */
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_CREATE);
    }

    /**
     * In caso di aggiornamento DB effettua la conversione tra i db
     * @param db Database
     * @param oldVersion versione vecchio Database
     * @param newVersion versione nuovo database
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }
    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

}
