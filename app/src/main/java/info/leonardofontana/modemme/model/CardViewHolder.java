package info.leonardofontana.modemme.model;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import info.leonardofontana.modemme.R;


/**
 * Questa classe implementa il modello della view: ha quindi le proprietà pubbliche
 * ed esse contengono le singole view della card
 */
public class CardViewHolder extends RecyclerView.ViewHolder {

    public NetworkImageView immagine;
    public TextView titolo;
    public TextView descrizione;
    public TextView commenti;
    public View view;

    public CardViewHolder(View view) {
        super(view);
        immagine = (NetworkImageView)view.findViewById(R.id.immagine);
        titolo = (TextView) view.findViewById(R.id.titolo);
        descrizione = (TextView) view.findViewById(R.id.descrizione);
        commenti = (TextView) view.findViewById(R.id.commenti);
        this.view = view;


    }
}
