package com.example.sistemagestion;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RegisterActivity extends AppCompatActivity {
    private UniversidadDatabaseHelper db;
    EditText usernameText, edadText, passwordText;
    Button registerButton;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        db = new UniversidadDatabaseHelper(this);
        handler = new Handler(Looper.getMainLooper());

        usernameText = findViewById(R.id.username1);
        edadText = findViewById(R.id.edad);
        passwordText = findViewById(R.id.password1);
        registerButton = findViewById(R.id.register1);

        registerButton.setOnClickListener(view -> {
            String username = usernameText.getText().toString().trim();
            String edadStr = edadText.getText().toString().trim();
            String password = passwordText.getText().toString().trim();


            if (username.isEmpty() || edadStr.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            int edad;
            try {
                edad = Integer.parseInt(edadStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "La edad debe ser un numero valido", Toast.LENGTH_SHORT).show();
                return;
            }


            if (edad < 17 || edad > 70) {
                Toast.makeText(this, "Ingrese su edad correctamente (17-70)", Toast.LENGTH_SHORT).show();
                return;
            }


            new Thread(() -> {
                if (db.studentExists(username)) {
                    handler.post(() -> Toast.makeText(this, "El nombre de usuario ya existe", Toast.LENGTH_SHORT).show());
                    return;
                }


                try {
                    db.addStudent(username, edad, password);
                    handler.post(() -> {
                        Toast.makeText(this, "Estudiante registrado", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    });
                } catch (Exception e) {
                    handler.post(() -> Toast.makeText(this, "Error al registrar estudiante", Toast.LENGTH_SHORT).show());
                }
            }).start();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}