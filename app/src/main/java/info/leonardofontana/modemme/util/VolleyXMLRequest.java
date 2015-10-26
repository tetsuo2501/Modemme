package info.leonardofontana.modemme.util;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;


import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by tetsu on 22/10/2015.
 */
public class VolleyXMLRequest extends Request<Document> {
    private static final String TAG = "VolleyXMLRequest";
    private final Response.Listener<Document> listener;

    public VolleyXMLRequest(int method, String url, Listener<Document> listener,
                            Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener=listener;
    }



    @Override
    protected Response<Document> parseNetworkResponse(NetworkResponse response) {

        Document doc  = null;
        try {

            doc =  DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse(new ByteArrayInputStream(
                            (new String(
                                    response.data,
                                    HttpHeaderParser.parseCharset(response.headers))
                            ).getBytes()
                    ));
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "Errore durante la coversione della stringa",e);
        }catch (SAXException e) {
            Log.e(TAG,"Errore durante il parsing SAX",e);
        } catch (IOException e) {
            Log.e(TAG,"errore di IO",e);
        } catch (ParserConfigurationException e) {
            Log.e(TAG,"Errore nella configurazione del parser",e);
        }

        return Response.success(doc, HttpHeaderParser.parseCacheHeaders(response));
    }



    @Override
    protected void deliverResponse(Document response) {
       listener.onResponse(response);
    }
}
