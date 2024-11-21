package com.example.sistemagestion;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    private int estudianteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Obtengo el id del estudiante
        estudianteId = getIntent().getIntExtra("estudianteId", -1);


        Button verMateriasInscritasButton = findViewById(R.id.ver_materias_inscritas_button);
        verMateriasInscritasButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MateriasInscritasActivity.class);
            intent.putExtra("estudianteId", estudianteId);
            startActivity(intent);
        });


        Button salirButton = findViewById(R.id.salir_button);
        salirButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        // Agrego lso fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        UserProfileFragment userProfileFragment = new UserProfileFragment(estudianteId);
        AvailableCoursesFragment availableCoursesFragment = new AvailableCoursesFragment(estudianteId);

        fragmentTransaction.replace(R.id.fragment_container_user_profile, userProfileFragment);
        fragmentTransaction.replace(R.id.fragment_container_available_courses, availableCoursesFragment);

        fragmentTransaction.commit();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}