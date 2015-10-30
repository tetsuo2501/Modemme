package info.leonardofontana.modemme.model;

import android.database.Cursor;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;

import java.util.Date;

/**
 * Created by tetsu on 23/10/2015.
 */
public class CardItem {
    public String titolo;
    public Spanned descrizione;
    public String contenuto;
    public String immagine;
    public String link;
    public int num_commenti;
    //public Date data
    CardItem(Cursor c){
        //todo:implementare modello
        titolo = c.getString(ProjectionContract.PROJ_TITLE);
        descrizione = Html.fromHtml(c.getString(ProjectionContract.PROJ_DESCRIPTION));
        //contenuto = c.getString(ProjectionContract.PROJ_CONTENT);
        Log.d("CardItem","Valore contenuto: "+contenuto);
        link = c.getString(ProjectionContract.PROJ_LINK);
        immagine = c.getString(ProjectionContract.PROJ_MEDIA);
        num_commenti = c.getInt(ProjectionContract.PROJ_NUM_COM);
    }
}
