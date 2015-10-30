package info.leonardofontana.modemme.model;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.analytics.HitBuilders;

import info.leonardofontana.modemme.ModemmeApplication;
import info.leonardofontana.modemme.PostActivity;
import info.leonardofontana.modemme.R;
import info.leonardofontana.modemme.util.VolleyRequestQueue;

/**
 * Created by tetsu on 23/10/2015.
 */
public class CardListAdapter extends CursorRecyclerViewAdapter<CardViewHolder> {
    Context context;
    private static final String TAG = "CardListAdapter";
    public CardListAdapter(Context context,Cursor cursor){
        super(context,cursor);
        this.context = context;
    }


    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_layout, parent, false);
        CardViewHolder h = new CardViewHolder(itemView);

        return h;
    }

    @Override
    public void onBindViewHolder(CardViewHolder viewHolder, Cursor cursor) {
        CardItem cardItem = new CardItem(cursor);
        viewHolder.immagine.setImageUrl(cardItem.immagine, VolleyRequestQueue.getInstance(context).getImageLoader());
        viewHolder.titolo.setText(cardItem.titolo);
        Log.d(TAG, "Titolo da CardViewHolder: " + cardItem.titolo);
        viewHolder.descrizione.setText(cardItem.descrizione);
        viewHolder.commenti.setText(String.valueOf(cardItem.num_commenti));
        viewHolder.view.setOnClickListener(new ClickListener(cardItem.link));
    }
    class ClickListener implements View.OnClickListener{
        private String link;


        public ClickListener(String link) {
            this.link = link;

        }

        @Override
        public void onClick(View v) {
            /* Intento esplicito
            Intent intent = new Intent(context, PostActivity.class);
            intent.putExtra(IntentPostContract.contenuto, content);
            Log.d(TAG,"Invio contentuoto: "+ content);
            intent.putExtra(IntentPostContract.link, link);
            intent.putExtra(IntentPostContract.titolo, titolo);
            context.startActivity(intent);
            */
            //Intento inplicito
            Intent i = new Intent(context, PostActivity.class);
            i.setData(Uri.parse(link));
            context.startActivity(i);
            ((ModemmeApplication)((AppCompatActivity) context)
                    .getApplication())
                    .getDefaultTracker().send(
                        (new HitBuilders.EventBuilder(
                                "clickMain",
                                "Apre pagina"
                        ))
                                .setLabel(link)
                    .build()

            );
        }
    }
}

