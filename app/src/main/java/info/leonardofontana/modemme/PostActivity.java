package info.leonardofontana.modemme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import info.leonardofontana.modemme.model.IntentPostContract;

/**
 * Created by tetsu on 26/10/2015.
 */
public class PostActivity extends AppCompatActivity {
    private WebView webview;
    public String titolo;
    public String link;
    public String contenuto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_post);
        setSupportActionBar(toolbar);
        webview = (WebView) findViewById(R.id.webView);
        Intent intent = getIntent();
        titolo = intent.getStringExtra(IntentPostContract.titolo);

        link = intent.getStringExtra(IntentPostContract.titolo);
        contenuto =intent.getStringExtra(IntentPostContract.contenuto);
        //todo: Completare metodo: gestione del link
        ((TextView)findViewById(R.id.titolo) ).setText(titolo);



        WebSettings webViewSettings = webview.getSettings();
        webViewSettings.setUseWideViewPort(true);
        webViewSettings.setLoadWithOverviewMode(true);
        webViewSettings.setMinimumFontSize(40);
        webViewSettings.setDefaultFixedFontSize(40);
        webViewSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        webViewSettings.setJavaScriptEnabled(true);
        webViewSettings.setSupportZoom(true);

        webview.loadDataWithBaseURL("http://modemme.com",contenuto,"text/html","UTF-8","");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle app bar item clicks here. The app bar
        // automatically handles clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();
        /*
        if (id == R.id) {
            return true;
        }
        */
        return super.onOptionsItemSelected(item);
    }

}
