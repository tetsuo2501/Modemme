package info.leonardofontana.modemme.model;

import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by tetsu on 22/10/2015.
 */
public class FeedParser {
    public static final String TAG ="FeedParser";
    public static final String TAG_TITLE = "title";
    public static final String TAG_LINK = "link";
    public static final String TAG_COMMENT ="wfw:commentRss";
    public static final String TAG_DATE ="pubDate";
    public static final String TAG_AUTH = "dc:creator";
    public static final String TAG_DESCRIPTION = "description";
    public static final String TAG_CONTENT = "content:encoded";
    public static final String TAG_NUM_COMMENTS ="slash:comments";
    public static final String TAG_ENCLOSURE = "enclosure";


    private static final String ENTRY = "entry";

    public List<Entry> parse(Document d){
        List<Entry> entries = new ArrayList<Entry>();
        d.getDocumentElement().normalize();
        NodeList nl = d.getElementsByTagName(ENTRY);
        if(nl == null || nl.getLength() < 1)
            return null;
        for( int i = 0; i < nl.getLength(); i++){
            entries.add(parseEntry(nl.item(i)));
        }
        return entries;
    }

    private Entry parseEntry(Node n){
        DateFormat formatoData = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
        Element e = (Element)n;
        long data = 0l;
        try {
            data = (formatoData.parse(
                    e.getElementsByTagName(TAG_DATE).item(0).getNodeValue()))
                    .getTime();
        } catch (ParseException e1) {
            Log.e(TAG, "Errore durante il parsing della data", e1);
        }

        //todo: implementare metodo

        return new Entry(
                e.getElementsByTagName(TAG_LINK).item(0).getNodeValue(),
                e.getElementsByTagName(TAG_TITLE).item(0).getNodeValue(),
                e.getElementsByTagName(TAG_DESCRIPTION).item(0).getNodeValue(),
                e.getElementsByTagName(TAG_AUTH).item(0).getNodeValue(),
                e.getElementsByTagName(TAG_LINK).item(0).getNodeValue(),
                e.getElementsByTagName(TAG_ENCLOSURE).item(0).getAttributes().item(0).getNodeValue(),
                e.getElementsByTagName(TAG_COMMENT).item(0).getNodeValue(),
                data,
                e.getElementsByTagName(TAG_CONTENT).item(0).getNodeValue(),
                Integer.getInteger( e.getElementsByTagName(TAG_NUM_COMMENTS).item(0).getNodeValue())

        );
    }

    public static class Entry {
        public final String id; //Entry ID
        public final String titolo;
        public final String descrizione;
        public final String autore;
        public final String link;
        public final String mediaLink;
        public final String linkCommenti;
        public final long data;
        public final String contenuto;
        public final int numeroCommenti;

        public Entry(String id, String titolo, String descrizione, String autore, String link, String mediaLink, String linkCommenti, long data, String contenuto, int numeroCommenti) {
            this.id = id;
            this.titolo = titolo;
            this.descrizione = descrizione;
            this.autore = autore;
            this.link = link;
            this.mediaLink = mediaLink;
            this.linkCommenti = linkCommenti;
            this.data = data;
            this.contenuto = contenuto;
            this.numeroCommenti = numeroCommenti;
        }


    }
}
