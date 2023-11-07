package tn743.ufrrj.j17listadisciplinascc;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DisciplinasAdapter extends RecyclerView.Adapter<DisciplinasAdapter.ViewHolder> {
    private List<Disciplina> mDisciplinas;
    private Context mContext;
    public DisciplinasAdapter(List<Disciplina> disciplinas, Context c) {

        mDisciplinas = disciplinas;
        mContext = c;
    }
    @NonNull
    @Override
    /*
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_disciplina, parent, false);
        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView, mContext);
    }
*/
    public DisciplinasAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_disciplina, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//    public void onBindViewHolder(@NonNull DisciplinasAdapter.ViewHolder holder, int position) {
        Disciplina disciplina = mDisciplinas.get(position);
        TextView textView = holder.mTextCodigo;
        textView.setText(disciplina.getCodigo());
        textView = holder.mTextNome;
        textView.setText(disciplina.getNome());
    }

    @Override
    public int getItemCount() {
        return mDisciplinas.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

        public TextView mTextCodigo;
        public TextView mTextNome;
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            itemView.setOnClickListener(this);
            mTextCodigo = itemView.findViewById(R.id.codigo);
            mTextNome =  itemView.findViewById(R.id.nome);
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
    }//public class ViewHolder extends RecyclerView.ViewHolder {
}//public class DisciplinasAdapter extends RecyclerView.Adapter<DisciplinasAdapter.ViewHolder> {
