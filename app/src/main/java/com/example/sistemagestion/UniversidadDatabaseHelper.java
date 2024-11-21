package com.example.sistemagestion;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UniversidadDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "universidad.db";
    private static final int DATABASE_VERSION = 21;

    public static final String TABLE_STUDENTS = "estudiantes";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "nombre";
    public static final String COLUMN_AGE = "edad";
    public static final String COLUMN_PASSWORD = "password";

    public static final String TABLE_MATERIAS = "materias";
    public static final String COLUMN_MATERIA_ID = "id";
    public static final String COLUMN_MATERIA_NOMBRE = "nombre";
    public static final String COLUMN_MATERIA_DESCRIPCION = "descripcion";

    public static final String TABLE_INSCRIPCIONES = "inscripciones";
    public static final String COLUMN_INSCRIPCION_ID = "id";
    public static final String COLUMN_INSCRIPCION_ESTUDIANTE_ID = "estudiante_id";
    public static final String COLUMN_INSCRIPCION_MATERIA_ID = "materia_id";
    public static final String COLUMN_INSCRIPCION_FECHA = "fecha_inscripcion";

    private static final String TABLE_CREATE_STUDENTS = "CREATE TABLE "
            + TABLE_STUDENTS + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_NAME + " TEXT, "
            + COLUMN_AGE + " INTEGER, "
            + COLUMN_PASSWORD + " VARCHAR(50))";

    private static final String TABLE_CREATE_MATERIAS = "CREATE TABLE "
            + TABLE_MATERIAS + " (" + COLUMN_MATERIA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_MATERIA_NOMBRE + " TEXT, "
            + COLUMN_MATERIA_DESCRIPCION + " TEXT)";

    private static final String TABLE_CREATE_INSCRIPCIONES = "CREATE TABLE "
            + TABLE_INSCRIPCIONES + " (" + COLUMN_INSCRIPCION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_INSCRIPCION_ESTUDIANTE_ID + " INTEGER, "
            + COLUMN_INSCRIPCION_MATERIA_ID + " INTEGER, "
            + COLUMN_INSCRIPCION_FECHA + " TEXT, "
            + "FOREIGN KEY(" + COLUMN_INSCRIPCION_ESTUDIANTE_ID + ") REFERENCES " + TABLE_STUDENTS + "(" + COLUMN_ID + "), "
            + "FOREIGN KEY(" + COLUMN_INSCRIPCION_MATERIA_ID + ") REFERENCES " + TABLE_MATERIAS + "(" + COLUMN_MATERIA_ID + "))";

    public UniversidadDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE_STUDENTS);
        db.execSQL(TABLE_CREATE_MATERIAS);
        db.execSQL(TABLE_CREATE_INSCRIPCIONES);
        insertInitialMaterias(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MATERIAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INSCRIPCIONES);
        onCreate(db);
    }

    private void insertInitialMaterias(SQLiteDatabase db) {
        addMateria(db, "Matemáticas", "Estudio de los números y las operaciones matemáticas");
        addMateria(db, "Filosofía", "Estudio de las ideas y pensamientos de los seres humanos.");
        addMateria(db, "Ciencias Éticas", "Estudio de la moral y la ética para entornos laborales");
        addMateria(db, "Derecho", "Estudio de las leyes y obligaciones de los ciudadanos.");
        addMateria(db, "Biología", "Estudio de las ciencias naturales y la vida de los seres vivos.");
    }

    private void addMateria(SQLiteDatabase db, String nombre, String descripcion) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_MATERIA_NOMBRE, nombre);
        values.put(COLUMN_MATERIA_DESCRIPCION, descripcion);
        db.insert(TABLE_MATERIAS, null, values);
    }

    // Metodos para gestionar estudiantes
    public Cursor getStudent(String nombre, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_ID, COLUMN_NAME, COLUMN_AGE, COLUMN_PASSWORD};
        String selection = COLUMN_NAME + " = ? AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {nombre, password};
        return db.query(TABLE_STUDENTS, columns, selection, selectionArgs, null, null, null);
    }

    public boolean studentExists(String nombre) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_ID};
        String selection = COLUMN_NAME + " = ?";
        String[] selectionArgs = {nombre};
        Cursor cursor = db.query(TABLE_STUDENTS, columns, selection, selectionArgs, null, null, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public void addStudent(String nombre, int edad, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, nombre);
        values.put(COLUMN_AGE, edad);
        values.put(COLUMN_PASSWORD, password);
        db.insert(TABLE_STUDENTS, null, values);
        db.close();
    }

    public int updateStudent(int id, String nombre, int edad, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, nombre);
        values.put(COLUMN_AGE, edad);
        values.put(COLUMN_PASSWORD, password);
        String selection = COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        return db.update(TABLE_STUDENTS, values, selection, selectionArgs);
    }

    public int deleteStudent(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        return db.delete(TABLE_STUDENTS, selection, selectionArgs);
    }

    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_STUDENTS, null, null, null, null, null, null);
    }

    public Cursor getUserById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        return db.query(TABLE_STUDENTS, null, selection, selectionArgs, null, null, null);
    }

    // Metodos para gestionar materias
    public Cursor getAllMaterias() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_MATERIAS, null, null, null, null, null, null);
    }

    // Metodos para gestionar inscripciones
    public void inscribirMateria(int estudianteId, int materiaId, String fecha) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_INSCRIPCION_ESTUDIANTE_ID, estudianteId);
        values.put(COLUMN_INSCRIPCION_MATERIA_ID, materiaId);
        values.put(COLUMN_INSCRIPCION_FECHA, fecha);
        db.insert(TABLE_INSCRIPCIONES, null, values);
        db.close();
    }

    public Cursor getInscripciones(int estudianteId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_INSCRIPCION_ESTUDIANTE_ID + " = ?";
        String[] selectionArgs = {String.valueOf(estudianteId)};
        return db.query(TABLE_INSCRIPCIONES, null, selection, selectionArgs, null, null, null);
    }

    public Cursor getMateriasInscritas(int estudianteId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT m." + COLUMN_MATERIA_ID + ", m." + COLUMN_MATERIA_NOMBRE + ", m." + COLUMN_MATERIA_DESCRIPCION +
                " FROM " + TABLE_MATERIAS + " m" +
                " INNER JOIN " + TABLE_INSCRIPCIONES + " i ON m." + COLUMN_MATERIA_ID + " = i." + COLUMN_INSCRIPCION_MATERIA_ID +
                " WHERE i." + COLUMN_INSCRIPCION_ESTUDIANTE_ID + " = ?";
        String[] selectionArgs = {String.valueOf(estudianteId)};
        return db.rawQuery(query, selectionArgs);
    }

    public boolean isAlreadyInscribed(int estudianteId, int materiaId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_INSCRIPCION_ESTUDIANTE_ID + " = ? AND " + COLUMN_INSCRIPCION_MATERIA_ID + " = ?";
        String[] selectionArgs = {String.valueOf(estudianteId), String.valueOf(materiaId)};
        Cursor cursor = db.query(TABLE_INSCRIPCIONES, null, selection, selectionArgs, null, null, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public void desuscribirMateria(int estudianteId, int materiaId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = COLUMN_INSCRIPCION_ESTUDIANTE_ID + " = ? AND " + COLUMN_INSCRIPCION_MATERIA_ID + " = ?";
        String[] selectionArgs = {String.valueOf(estudianteId), String.valueOf(materiaId)};
        db.delete(TABLE_INSCRIPCIONES, selection, selectionArgs);
    }
}