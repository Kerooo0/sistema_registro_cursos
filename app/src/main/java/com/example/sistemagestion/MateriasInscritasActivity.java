package com.example.sistemagestion;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MateriasInscritasActivity extends AppCompatActivity implements MateriaInscritaAdapter.OnDesuscribirClickListener {

    private UniversidadDatabaseHelper db;
    private int estudianteId;
    private MateriaInscritaAdapter materiaInscritaAdapter;
    private List<Materia> materiasInscritas;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_materias_inscritas);

        db = new UniversidadDatabaseHelper(this);
        handler = new Handler(Looper.getMainLooper());

        // Obtengo el id del estudiante
        estudianteId = getIntent().getIntExtra("estudianteId", -1);

        RecyclerView recyclerViewInscritas = findViewById(R.id.materias_inscritas_recycler_view);
        recyclerViewInscritas.setLayoutManager(new LinearLayoutManager(this));
        materiasInscritas = new ArrayList<>();
        materiaInscritaAdapter = new MateriaInscritaAdapter(materiasInscritas, this);
        recyclerViewInscritas.setAdapter(materiaInscritaAdapter);

        mostrarMateriasInscritas();

        Button volverButton = findViewById(R.id.volver_button);
        volverButton.setOnClickListener(v -> {
            Intent intent = new Intent(MateriasInscritasActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        Button regresarButton = findViewById(R.id.regresar_button);
        regresarButton.setOnClickListener(v -> {
            finish();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void mostrarMateriasInscritas() {
        new Thread(() -> {
            Cursor cursor = db.getMateriasInscritas(estudianteId);
            handler.post(() -> {
                materiasInscritas.clear();
                if (cursor.moveToFirst()) {
                    do {
                        int id = cursor.getInt(cursor.getColumnIndexOrThrow(UniversidadDatabaseHelper.COLUMN_MATERIA_ID));
                        String nombre = cursor.getString(cursor.getColumnIndexOrThrow(UniversidadDatabaseHelper.COLUMN_MATERIA_NOMBRE));
                        String descripcion = cursor.getString(cursor.getColumnIndexOrThrow(UniversidadDatabaseHelper.COLUMN_MATERIA_DESCRIPCION));
                        materiasInscritas.add(new Materia(id, nombre, descripcion));
                    } while (cursor.moveToNext());
                } else {
                    Toast.makeText(this, "No estás inscrito en ninguna materia", Toast.LENGTH_SHORT).show();
                }
                cursor.close();
                materiaInscritaAdapter.notifyDataSetChanged();
            });
        }).start();
    }

    @Override
    public void onDesuscribirClick(int materiaId) {
        new Thread(() -> {
            db.desuscribirMateria(estudianteId, materiaId);
            handler.post(() -> {
                Toast.makeText(this, "Materia borrada", Toast.LENGTH_SHORT).show();
                mostrarMateriasInscritas();
            });
        }).start();
    }
}