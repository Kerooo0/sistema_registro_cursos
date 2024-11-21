package com.example.sistemagestion;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MateriaInscritaAdapter extends RecyclerView.Adapter<MateriaInscritaAdapter.MateriaInscritaViewHolder> {

    private List<Materia> materias;
    private OnDesuscribirClickListener desuscribirClickListener;

    public MateriaInscritaAdapter(List<Materia> materias, OnDesuscribirClickListener desuscribirClickListener) {
        this.materias = materias;
        this.desuscribirClickListener = desuscribirClickListener;
    }

    @NonNull
    @Override
    public MateriaInscritaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.materia_inscrita, parent, false);
        return new MateriaInscritaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MateriaInscritaViewHolder holder, int position) {
        Materia materia = materias.get(position);
        holder.nombreTextView.setText(materia.getNombre());
        holder.descripcionTextView.setText(materia.getDescripcion());
        holder.desuscribirButton.setOnClickListener(v -> desuscribirClickListener.onDesuscribirClick(materia.getId()));
    }

    @Override
    public int getItemCount() {
        return materias.size();
    }

    public static class MateriaInscritaViewHolder extends RecyclerView.ViewHolder {
        TextView nombreTextView;
        TextView descripcionTextView;
        Button desuscribirButton;

        public MateriaInscritaViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.materia_inscrita_nombre);
            descripcionTextView = itemView.findViewById(R.id.materia_inscrita_descripcion);
            desuscribirButton = itemView.findViewById(R.id.desuscribir_button);
        }
    }

    public interface OnDesuscribirClickListener {
        void onDesuscribirClick(int materiaId);
    }
}