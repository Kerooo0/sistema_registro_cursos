package com.example.sistemagestion;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MateriaAdapter extends RecyclerView.Adapter<MateriaAdapter.MateriaViewHolder> {

    private List<Materia> materias;
    private OnInscribirClickListener inscribirClickListener;

    public MateriaAdapter(List<Materia> materias, OnInscribirClickListener inscribirClickListener) {
        this.materias = materias;
        this.inscribirClickListener = inscribirClickListener;
    }

    @NonNull
    @Override
    public MateriaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_materia, parent, false);
        return new MateriaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MateriaViewHolder holder, int position) {
        Materia materia = materias.get(position);
        holder.nombreTextView.setText(materia.getNombre());
        holder.descripcionTextView.setText(materia.getDescripcion());
        holder.inscribirButton.setOnClickListener(v -> inscribirClickListener.onInscribirClick(materia.getId()));
    }

    @Override
    public int getItemCount() {
        return materias.size();
    }

    public static class MateriaViewHolder extends RecyclerView.ViewHolder {
        TextView nombreTextView;
        TextView descripcionTextView;
        Button inscribirButton;

        public MateriaViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.materia_nombre);
            descripcionTextView = itemView.findViewById(R.id.materia_descripcion);
            inscribirButton = itemView.findViewById(R.id.inscribir_button);
        }
    }

    public interface OnInscribirClickListener {
        void onInscribirClick(int materiaId);
    }
}