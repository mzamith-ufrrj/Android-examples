package tn743.ufrrj.j17listadisciplinascc;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView mTextCodigo;
    public TextView mTextNome;
    private Context mContext;
    public ViewHolder(@NonNull View itemView, Context c) {
        super(itemView);
        itemView.setOnClickListener(this);
        this.mTextCodigo = itemView.findViewById(R.id.codigo);
        this.mTextNome =  itemView.findViewById(R.id.nome);
        this.mContext = c;
    }

    @Override
    public void onClick(View v) {
        int i = getAdapterPosition();
        Log.d( "ViewHolder", "onClick " + Integer.toString(i) );
        Intent myintent = new Intent("disciplina-escolhida");
        // You can also include some extra data.
        String msg = Integer.toString(i);
        myintent.putExtra("message", msg);

        LocalBroadcastManager.getInstance(mContext).sendBroadcast(myintent);
    }
}
