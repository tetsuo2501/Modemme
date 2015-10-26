package info.leonardofontana.modemme.model;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    //Questa non l'ho capita: dovrebbe tornare il viewholder di una data posizione
    //Inoltre è già implementato nel genitore
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
        //todo: impostare onclick con apertura del contenuto della card
        //viewHolder.mTextView.setText(myListItem.getName());
        viewHolder.immagine.setImageUrl(cardItem.immagine, VolleyRequestQueue.getInstance(context).getImageLoader());
        viewHolder.titolo.setText(cardItem.titolo);
        Log.d(TAG, "Titolo da CardViewHolder: " + cardItem.titolo);
        viewHolder.descrizione.setText(cardItem.descrizione);
        viewHolder.commenti.setText(String.valueOf(cardItem.num_commenti));
        viewHolder.view.setOnClickListener(new ClickListener(cardItem.titolo,cardItem.contenuto,cardItem.link));
    }
    class ClickListener implements View.OnClickListener{
        private String link;
        private String content;
        private String titolo;

        public ClickListener(String titolo,  String content, String link) {
            this.link = link;
            this.content = content;
            this.titolo = titolo;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, PostActivity.class);
            intent.putExtra(IntentPostContract.contenuto, content);
            intent.putExtra(IntentPostContract.link, link);
            intent.putExtra(IntentPostContract.titolo, titolo);
            context.startActivity(intent);
        }
    }
}

