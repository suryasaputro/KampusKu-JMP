package com.example.kampusku;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class DetailMahasiswaActivity extends AppCompatActivity {

    TextView tvNpm, tvNama, tvJurusan, tvTanggalLahir, tvGender, tvAlamat, tvAngkatan, tvFakultas, tvEmail;
    ImageView imgFotoProfil;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_mahasiswa);

        // Inisialisasi komponen
        tvNpm = findViewById(R.id.tvNpm);
        tvNama = findViewById(R.id.tvNama);
        tvJurusan = findViewById(R.id.tvJurusan);
        tvTanggalLahir = findViewById(R.id.tvTanggalLahir);
        tvGender = findViewById(R.id.tvGender);
        tvAlamat = findViewById(R.id.tvAlamat);
        tvAngkatan = findViewById(R.id.tvAngkatan);
        tvFakultas = findViewById(R.id.tvFakultas);
        tvEmail = findViewById(R.id.tvEmail);
        imgFotoProfil = findViewById(R.id.imgFotoProfil);

        dbHelper = new DBHelper(this);

        // Ambil ID dari intent
        int id = getIntent().getIntExtra("id", -1);
        if (id != -1) {
            Mahasiswa mhs = dbHelper.getMahasiswaById(id);
            if (mhs != null) {
                // Set data ke TextView
                tvNpm.setText(mhs.getNpm());
                tvNama.setText(mhs.getNama());
                tvJurusan.setText(mhs.getJurusan());
                tvTanggalLahir.setText(mhs.getTglLahir());
                tvGender.setText(mhs.getGender());
                tvAlamat.setText(mhs.getAlamat());
                tvAngkatan.setText(mhs.getAngkatan());
                tvFakultas.setText(mhs.getFakultas());
                tvEmail.setText(mhs.getEmail());

                String fotoPath = mhs.getFotoProfil();
                if (fotoPath != null && !fotoPath.isEmpty()) {
                    File imgFile = new File(fotoPath);
                    if (imgFile.exists()) {
                        Bitmap bitmap = BitmapFactory.decodeFile(fotoPath);
                        imgFotoProfil.setImageBitmap(bitmap);
                    } else {
                        imgFotoProfil.setImageResource(R.drawable.user); // fallback jika file tidak ada
                    }
                } else {
                    imgFotoProfil.setImageResource(R.drawable.user); // fallback default
                }

            } else {
                Toast.makeText(this, "Data tidak ditemukan", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "ID tidak ditemukan", Toast.LENGTH_SHORT).show();
        }
    }
}
