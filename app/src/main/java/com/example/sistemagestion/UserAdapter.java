package com.example.sistemagestion;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> users;
    private OnUpdateClickListener updateClickListener;
    private OnDeleteClickListener deleteClickListener;

    public UserAdapter(List<User> users, OnUpdateClickListener updateClickListener, OnDeleteClickListener deleteClickListener) {
        this.users = users;
        this.updateClickListener = updateClickListener;
        this.deleteClickListener = deleteClickListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = users.get(position);
        holder.nombreTextView.setText(user.getNombre());
        holder.edadTextView.setText(String.valueOf(user.getEdad()));
        holder.updateButton.setOnClickListener(v -> updateClickListener.onUpdateClick(user.getId()));
        holder.deleteButton.setOnClickListener(v -> deleteClickListener.onDeleteClick(user.getId()));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView nombreTextView;
        TextView edadTextView;
        Button updateButton;
        Button deleteButton;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.user_nombre);
            edadTextView = itemView.findViewById(R.id.user_edad);
            updateButton = itemView.findViewById(R.id.update_button);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }

    public interface OnUpdateClickListener {
        void onUpdateClick(int userId);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(int userId);
    }
}