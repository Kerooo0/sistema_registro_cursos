package com.example.sistemagestion;

import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

public class UserProfileFragment extends Fragment implements UserAdapter.OnUpdateClickListener, UserAdapter.OnDeleteClickListener {

    private UniversidadDatabaseHelper db;
    private UserAdapter userAdapter;
    private List<User> users;
    private Handler handler;
    private int estudianteId;

    public UserProfileFragment(int estudianteId) {
        this.estudianteId = estudianteId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        db = new UniversidadDatabaseHelper(getContext());
        handler = new Handler(Looper.getMainLooper());

        // Configurar el RecyclerView para el perfil del usuario
        RecyclerView recyclerView = view.findViewById(R.id.perfil_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        users = new ArrayList<>();
        userAdapter = new UserAdapter(users, this, this);
        recyclerView.setAdapter(userAdapter);

        // Mostrar el perfil del usuario
        mostrarPerfilUsuario();

        return view;
    }

    private void mostrarPerfilUsuario() {
        new Thread(() -> {
            Cursor cursor = db.getUserById(estudianteId);
            handler.post(() -> {
                if (cursor.moveToFirst()) {
                    users.clear();
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(UniversidadDatabaseHelper.COLUMN_ID));
                    String nombre = cursor.getString(cursor.getColumnIndexOrThrow(UniversidadDatabaseHelper.COLUMN_NAME));
                    int edad = cursor.getInt(cursor.getColumnIndexOrThrow(UniversidadDatabaseHelper.COLUMN_AGE));
                    String password = cursor.getString(cursor.getColumnIndexOrThrow(UniversidadDatabaseHelper.COLUMN_PASSWORD));
                    users.add(new User(id, nombre, edad, password));
                    userAdapter.notifyDataSetChanged();
                }
                cursor.close();
            });
        }).start();
    }

    @Override
    public void onUpdateClick(int userId) {
        Intent intent = new Intent(getActivity(), UpdateUserActivity.class);
        intent.putExtra("userId", userId);
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(int userId) {
        new Thread(() -> {
            db.deleteStudent(userId);
            handler.post(() -> {
                Toast.makeText(getContext(), "Usuario eliminado", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            });
        }).start();
    }
}