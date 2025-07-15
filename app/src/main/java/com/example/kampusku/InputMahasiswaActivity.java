package com.example.kampusku;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.util.Calendar;

public class InputMahasiswaActivity extends AppCompatActivity {

    EditText etNpm, etNama, etJurusan, etTanggalLahir, etAlamat, etAngkatan, etFakultas, etEmail;
    AutoCompleteTextView dropdownGender;
    Button btnSimpan;
    ImageView imgPreview;
    DBHelper dbHelper;

    String selectedFotoPath = "", selectedGender = "";
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_mahasiswa);

        // Inisialisasi komponen
        etNpm = findViewById(R.id.etNpm);
        etNama = findViewById(R.id.etNama);
        etJurusan = findViewById(R.id.etJurusan);
        etTanggalLahir = findViewById(R.id.etTanggalLahir);
        etAlamat = findViewById(R.id.etAlamat);
        etAngkatan = findViewById(R.id.etAngkatan);
        etFakultas = findViewById(R.id.etFakultas);
        etEmail = findViewById(R.id.etEmail);
        dropdownGender = findViewById(R.id.dropdownGender);
        btnSimpan = findViewById(R.id.btnSimpan);
        imgPreview = findViewById(R.id.imgPreview);

        dbHelper = new DBHelper(this);

        // Setup dropdown gender
        String[] genderItems = {"Laki-laki", "Perempuan"};
        ArrayAdapter<String> adapterGender = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, genderItems);
        dropdownGender.setAdapter(adapterGender);
        dropdownGender.setOnClickListener(v -> dropdownGender.showDropDown());
        dropdownGender.setOnItemClickListener((parent, view, position, id) -> {
            selectedGender = adapterGender.getItem(position);
        });

        // Tanggal lahir (DatePicker)
        etTanggalLahir.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            new DatePickerDialog(this, (view, y, m, d) -> {
                etTanggalLahir.setText(d + "/" + (m + 1) + "/" + y);
            }, year, month, day).show();
        });

        // Klik gambar → pilih gambar
        imgPreview.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        // Simpan data ke database
        btnSimpan.setOnClickListener(v -> {
            String npm = etNpm.getText().toString();
            String nama = etNama.getText().toString();
            String jurusan = etJurusan.getText().toString();
            String tgl = etTanggalLahir.getText().toString();
            String gender = dropdownGender.getText().toString();
            String alamat = etAlamat.getText().toString();
            String angkatan = etAngkatan.getText().toString();
            String fakultas = etFakultas.getText().toString();
            String email = etEmail.getText().toString();

            if (npm.isEmpty() || nama.isEmpty()) {
                Toast.makeText(this, "NPM dan Nama wajib diisi", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean berhasil = dbHelper.insertMahasiswa(npm, nama, jurusan, tgl, gender, alamat, angkatan, fakultas, email, selectedFotoPath);
            if (berhasil) {
                Toast.makeText(this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Menangani hasil pilih gambar (dengan ContentResolver agar aman di Android 7+)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            selectedFotoPath = uri.toString(); // simpan ke DB jika perlu

            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imgPreview.setImageBitmap(bitmap);
                if (inputStream != null) inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Gagal memuat gambar", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
