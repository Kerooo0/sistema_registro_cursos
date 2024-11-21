package com.example.sistemagestion;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UpdateUserActivity extends AppCompatActivity {

    private UniversidadDatabaseHelper db;
    private Handler handler;
    private int userId;
    private EditText inputNombre, inputEdad, inputPassword;
    private Button updateButton, deleteButton, regresarButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        db = new UniversidadDatabaseHelper(this);
        handler = new Handler(Looper.getMainLooper());

        userId = getIntent().getIntExtra("userId", -1);

        inputNombre = findViewById(R.id.input_nombre);
        inputEdad = findViewById(R.id.input_edad);
        inputPassword = findViewById(R.id.input_password);
        updateButton = findViewById(R.id.update_button);
        deleteButton = findViewById(R.id.delete_button);
        regresarButton = findViewById(R.id.regresar_button);

        cargarDatosUsuario();

        updateButton.setOnClickListener(v -> actualizarUsuario());
        deleteButton.setOnClickListener(v -> eliminarUsuario());
        regresarButton.setOnClickListener(v -> finish());
    }

    private void cargarDatosUsuario() {
        new Thread(() -> {
            Cursor cursor = db.getUserById(userId);
            if (cursor.moveToFirst()) {
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow(UniversidadDatabaseHelper.COLUMN_NAME));
                int edad = cursor.getInt(cursor.getColumnIndexOrThrow(UniversidadDatabaseHelper.COLUMN_AGE));
                String password = cursor.getString(cursor.getColumnIndexOrThrow(UniversidadDatabaseHelper.COLUMN_PASSWORD));

                handler.post(() -> {
                    inputNombre.setText(nombre);
                    inputEdad.setText(String.valueOf(edad));
                    inputPassword.setText(password);
                });
            }
            cursor.close();
        }).start();
    }

    private void actualizarUsuario() {
        String nombre = inputNombre.getText().toString();
        String edadStr = inputEdad.getText().toString();
        String password = inputPassword.getText().toString();

        if (nombre.isEmpty() || edadStr.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        int edad;
        try {
            edad = Integer.parseInt(edadStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "La edad debe ser un número válido", Toast.LENGTH_SHORT).show();
            return;
        }

        if (edad < 17 || edad > 70) {
            Toast.makeText(this, "Ingrese una edad válida (17-70)", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            db.updateStudent(userId, nombre, edad, password);
            handler.post(() -> {
                Toast.makeText(this, "Usuario actualizado, por favor vuelva a iniciar sesion", Toast.LENGTH_SHORT).show();
                finish();
            });
        }).start();
    }

    private void eliminarUsuario() {
        new Thread(() -> {
            db.deleteStudent(userId);
            handler.post(() -> {
                Toast.makeText(this, "Usuario eliminado", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UpdateUserActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            });
        }).start();
    }
}