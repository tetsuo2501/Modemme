package info.leonardofontana.modemme;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import info.leonardofontana.modemme.model.FeedContract;
import info.leonardofontana.modemme.model.IntentPostContract;

/**
 * Created by tetsu on 26/10/2015.
 */
public class PostActivity extends AppCompatActivity {
    public static final String TAG="PostActivity";
    private WebView webview;
    public String titolo;
    public String link;
    public String contenuto;


    private enum PROJ{
        ENTRY(FeedContract.Entry.COLUMN_NAME_ENTRY_ID,0),
        CONTENT(FeedContract.Entry.COLUMN_CONTENT,1),
        TITLE(FeedContract.Entry.COLUMN_NAME_TITLE,2);
        private int col;
        private String name;
        PROJ(String columnContent, int i) {
            name = columnContent;
            col = i;
        }

        public int getCol() {
            return col;
        }

        public String getName() {
            return name;
        }
        public static String[] getPROJ(){
            return new String[]{ PROJ.ENTRY.getName(), PROJ.CONTENT.getName(), PROJ.TITLE.getName()};
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_post);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        webview = (WebView) findViewById(R.id.webView);
        Intent intent = getIntent();
        /* Intento esplicito
        titolo = intent.getStringExtra(IntentPostContract.titolo);

        link = intent.getStringExtra(IntentPostContract.link);
        */
        //Get data from implicit intent
        Uri data = intent.getData();
        link = data.toString();
        Log.d(TAG,"path da intent: " + link);
        //Log.d("Content: "+FeedContract.BASE_CONTENT_URI.+"/entries");
        //Effettuo la query
        Cursor mCursor = getContentResolver().query(
                Uri.parse("content://" + FeedContract.CONTENT_AUTHORITY+"/entries"),
                PROJ.getPROJ(), //Projection
                PROJ.ENTRY.getName() + " = ?",//Selection
                new String[]{link},//Selection args
                null //sort
        );
        if(mCursor.moveToFirst()) {
            contenuto = mCursor.getString(PROJ.CONTENT.getCol());
            titolo = mCursor.getString(PROJ.TITLE.getCol());
            Log.d(TAG, "Contenuto preso da db");
        }
        else{
            Log.e(TAG,"Non sono riuscito a prendere i dati dal db");
        }


        //todo: Completare metodo: gestione del link
        //((TextView)findViewById(R.id.titolo_post) ).setText(titolo);





        //Setto il nrowser interno
        WebSettings webViewSettings = webview.getSettings();
        webViewSettings.setUseWideViewPort(true);
        webViewSettings.setLoadWithOverviewMode(true);
        webViewSettings.setMinimumFontSize(40);
        webViewSettings.setDefaultFixedFontSize(40);
        webViewSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        webViewSettings.setJavaScriptEnabled(true);
        webViewSettings.setSupportZoom(true);

        webview.loadDataWithBaseURL("http://modemme.com",formattaPagina(contenuto),"text/html","UTF-8","");
        toolbar.setSubtitle(titolo);
    }

    private  String formattaPagina(String contenuto){
        StringBuffer res = new StringBuffer(getString(R.string.startHtml));
        res.append(contenuto);
        res.append(getString(R.string.endHtml));
        return res.toString();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle app bar item clicks here. The app bar
        // automatically handles clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();
/*
        if (id == R.id.) {
            return true;
        }
*/
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_post, menu);
        return true;
    }

    public void apriBrowser(MenuItem m) {
        Log.d(TAG,"Apro il browser");
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(link));
        startActivity(i);
    }

    public void condividi(MenuItem m){
        Log.d(TAG,"Condivido il link");
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.send_to)+ link);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.shareMessage)));
    }


}
