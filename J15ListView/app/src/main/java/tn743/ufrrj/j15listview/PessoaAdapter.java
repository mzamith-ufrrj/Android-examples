package tn743.ufrrj.j15listview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PessoaAdapter extends RecyclerView.Adapter<PessoaAdapter.PessoaViewHolder> {

    public static class PessoaViewHolder extends RecyclerView.ViewHolder {
        public TextView nomeView;
        public  TextView idadeView;
        public  ImageView fotoView;

        PessoaViewHolder(View itemView) {
            super(itemView);
            nomeView = itemView.findViewById(R.id.person_name);
            idadeView = itemView.findViewById(R.id.person_age);
            fotoView = (ImageView) itemView.findViewById(R.id.person_photo);
        }
    }//public static class PessoaViewHolder extends RecyclerView.ViewHolder {
    private ArrayList<Pessoa> pessoas;
    PessoaAdapter(ArrayList<Pessoa> pessoas){
        this.pessoas = pessoas;
    }
    @NonNull
    @Override
    public PessoaAdapter.PessoaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        PessoaViewHolder pvh = new PessoaViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull PessoaViewHolder holder, int position) {
        holder.nomeView.setText(pessoas.get(position).getNome());
        holder.idadeView.setText(pessoas.get(position).getIdade());
        holder.fotoView.setImageResource(pessoas.get(position).getFotoId());
    }

    @Override
    public int getItemCount() {
        return pessoas.size();
    }
}
