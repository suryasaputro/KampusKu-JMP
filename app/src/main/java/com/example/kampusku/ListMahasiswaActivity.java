package com.example.kampusku;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListMahasiswaActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MahasiswaAdapter adapter;
    DBHelper dbHelper;
    ArrayList<Mahasiswa> listMahasiswa;
    Button btnTambah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_mahasiswa);

        recyclerView = findViewById(R.id.recyclerView);
        btnTambah = findViewById(R.id.btnTambah);
        dbHelper = new DBHelper(this);

        listMahasiswa = dbHelper.getAllMahasiswa();
        adapter = new MahasiswaAdapter(this, listMahasiswa);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter); // ← gunakan adapter yang sudah dibuat di atas

        btnTambah.setOnClickListener(v ->
                startActivity(new Intent(this, InputMahasiswaActivity.class))
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        listMahasiswa.clear(); // Kosongkan list lama
        listMahasiswa.addAll(dbHelper.getAllMahasiswa()); // Isi ulang
        adapter.notifyDataSetChanged(); // Beri tahu adapter
    }
}
