package com.example.kampusku;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.*;
import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "kampusku.db";
    private static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Buat tabel user (login)
        db.execSQL("CREATE TABLE user(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT, password TEXT)");

        // Buat tabel mahasiswa
        db.execSQL("CREATE TABLE mahasiswa(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "npm TEXT, nama TEXT, jurusan TEXT, " +
                "tgl_lahir TEXT, gender TEXT, alamat TEXT, " +
                "angkatan TEXT, fakultas TEXT, email TEXT, " +
                "foto_profil TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Hapus tabel lama jika upgrade
        db.execSQL("DROP TABLE IF EXISTS user");
        db.execSQL("DROP TABLE IF EXISTS mahasiswa");
        onCreate(db);
    }

    // ========================
    // USER LOGIN
    // ========================
    public boolean insertUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues val = new ContentValues();
        val.put("username", username);
        val.put("password", password);
        long res = db.insert("user", null, val);
        return res != -1;
    }

    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM user WHERE username=? AND password=?",
                new String[]{username, password});
        boolean result = cursor.getCount() > 0;
        cursor.close();
        return result;
    }

    // ========================
    // MAHASISWA
    // ========================

    public boolean insertMahasiswa(String npm, String nama, String jurusan, String tglLahir, String gender,
                                   String alamat, String angkatan, String fakultas, String email, String fotoProfil) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues val = new ContentValues();
        val.put("npm", npm);
        val.put("nama", nama);
        val.put("jurusan", jurusan);
        val.put("tgl_lahir", tglLahir);
        val.put("gender", gender);
        val.put("alamat", alamat);
        val.put("angkatan", angkatan);
        val.put("fakultas", fakultas);
        val.put("email", email);
        val.put("foto_profil", fotoProfil);
        long res = db.insert("mahasiswa", null, val);
        return res != -1;
    }

    public ArrayList<Mahasiswa> getAllMahasiswa() {
        ArrayList<Mahasiswa> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM mahasiswa", null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String npm = cursor.getString(cursor.getColumnIndexOrThrow("npm"));
                String nama = cursor.getString(cursor.getColumnIndexOrThrow("nama"));
                String jurusan = cursor.getString(cursor.getColumnIndexOrThrow("jurusan"));
                String tglLahir = cursor.getString(cursor.getColumnIndexOrThrow("tgl_lahir"));
                String gender = cursor.getString(cursor.getColumnIndexOrThrow("gender"));
                String alamat = cursor.getString(cursor.getColumnIndexOrThrow("alamat"));
                String angkatan = cursor.getString(cursor.getColumnIndexOrThrow("angkatan"));
                String fakultas = cursor.getString(cursor.getColumnIndexOrThrow("fakultas"));
                String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
                String foto = cursor.getString(cursor.getColumnIndexOrThrow("foto_profil"));

                list.add(new Mahasiswa(id, npm, nama, jurusan, tglLahir, gender, alamat, angkatan, fakultas, email, foto));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public Mahasiswa getMahasiswaById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM mahasiswa WHERE id=?", new String[]{String.valueOf(id)});
        if (cursor.moveToFirst()) {
            Mahasiswa mhs = new Mahasiswa(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("npm")),
                    cursor.getString(cursor.getColumnIndexOrThrow("nama")),
                    cursor.getString(cursor.getColumnIndexOrThrow("jurusan")),
                    cursor.getString(cursor.getColumnIndexOrThrow("tgl_lahir")),
                    cursor.getString(cursor.getColumnIndexOrThrow("gender")),
                    cursor.getString(cursor.getColumnIndexOrThrow("alamat")),
                    cursor.getString(cursor.getColumnIndexOrThrow("angkatan")),
                    cursor.getString(cursor.getColumnIndexOrThrow("fakultas")),
                    cursor.getString(cursor.getColumnIndexOrThrow("email")),
                    cursor.getString(cursor.getColumnIndexOrThrow("foto_profil"))
            );
            cursor.close();
            return mhs;
        }
        cursor.close();
        return null;
    }

    public boolean updateMahasiswa(int id, String npm, String nama, String jurusan, String tglLahir, String gender,
                                   String alamat, String angkatan, String fakultas, String email, String fotoProfil) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues val = new ContentValues();
        val.put("npm", npm);
        val.put("nama", nama);
        val.put("jurusan", jurusan);
        val.put("tgl_lahir", tglLahir);
        val.put("gender", gender);
        val.put("alamat", alamat);
        val.put("angkatan", angkatan);
        val.put("fakultas", fakultas);
        val.put("email", email);
        val.put("foto_profil", fotoProfil); // ← simpan path gambar
        int res = db.update("mahasiswa", val, "id=?", new String[]{String.valueOf(id)});
        return res > 0;
    }

    public boolean deleteMahasiswa(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int res = db.delete("mahasiswa", "id=?", new String[]{String.valueOf(id)});
        return res > 0;
    }
}
