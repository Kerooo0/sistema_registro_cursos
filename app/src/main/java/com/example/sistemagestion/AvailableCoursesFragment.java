package com.example.sistemagestion;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AvailableCoursesFragment extends Fragment {

    private UniversidadDatabaseHelper db;
    private MateriaAdapter materiaAdapter;
    private List<Materia> materias;
    private Handler handler;
    private int estudianteId;

    public AvailableCoursesFragment(int estudianteId) {
        this.estudianteId = estudianteId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_available_courses, container, false);

        db = new UniversidadDatabaseHelper(getContext());
        handler = new Handler(Looper.getMainLooper());

        // Configuro el recyclkerviwa para materias
        RecyclerView recyclerView = view.findViewById(R.id.materias_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        materias = new ArrayList<>();
        materiaAdapter = new MateriaAdapter(materias, materiaId -> inscribirMateria(materiaId));
        recyclerView.setAdapter(materiaAdapter);

        // Muesto las materias disponibles
        mostrarMaterias();

        return view;
    }

    private void mostrarMaterias() {
        new Thread(() -> {
            Cursor cursor = db.getAllMaterias();
            handler.post(() -> {
                if (cursor.moveToFirst()) {
                    materias.clear();
                    do {
                        int id = cursor.getInt(cursor.getColumnIndexOrThrow(UniversidadDatabaseHelper.COLUMN_MATERIA_ID));
                        String nombre = cursor.getString(cursor.getColumnIndexOrThrow(UniversidadDatabaseHelper.COLUMN_MATERIA_NOMBRE));
                        String descripcion = cursor.getString(cursor.getColumnIndexOrThrow(UniversidadDatabaseHelper.COLUMN_MATERIA_DESCRIPCION));
                        materias.add(new Materia(id, nombre, descripcion));
                    } while (cursor.moveToNext());
                    materiaAdapter.notifyDataSetChanged();
                }
                cursor.close();
            });
        }).start();
    }

    private void inscribirMateria(int materiaId) {
        new Thread(() -> {
            if (db.isAlreadyInscribed(estudianteId, materiaId)) {
                handler.post(() -> Toast.makeText(getContext(), "Ya estás inscrito en esta materia", Toast.LENGTH_SHORT).show());
                return;
            }

            String fecha = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            db.inscribirMateria(estudianteId, materiaId, fecha);
            handler.post(() -> {
                Toast.makeText(getContext(), "Inscripción exitosa", Toast.LENGTH_SHORT).show();
            });
        }).start();
    }
}