package com.example.sistemagestion;

import android.content.Intent;
import android.database.Cursor;
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

public class LoginActivity extends AppCompatActivity {
    private UniversidadDatabaseHelper db;
    private Handler handler;

    EditText usernameText, passwordText;
    Button loginButton, registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        db = new UniversidadDatabaseHelper(this);
        handler = new Handler(Looper.getMainLooper());

        usernameText = findViewById(R.id.username);
        passwordText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        registerButton = findViewById(R.id.register);

        loginButton.setOnClickListener(view -> {
            String username = usernameText.getText().toString();
            String password = passwordText.getText().toString();

            new Thread(() -> {
                try (Cursor cursor = db.getStudent(username, password)) {
                    if (cursor.moveToFirst()) {
                        int estudianteId = cursor.getInt(cursor.getColumnIndexOrThrow(UniversidadDatabaseHelper.COLUMN_ID));
                        handler.post(() -> {
                            Toast.makeText(this, "Bienvenido " + username, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("estudianteId", estudianteId);
                            startActivity(intent);
                            finish();
                        });
                    } else {
                        handler.post(() -> Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show());
                    }
                } catch (Exception e) {
                    handler.post(() -> Toast.makeText(this, "Error al iniciar sesión", Toast.LENGTH_SHORT).show());
                }
            }).start();
        });

        registerButton.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}