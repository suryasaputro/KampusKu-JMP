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

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Calendar;

public class InputMahasiswaActivity extends AppCompatActivity {

    EditText etNpm, etNama, etJurusan, etTanggalLahir, etAlamat, etAngkatan, etFakultas, etEmail;
    AutoCompleteTextView dropdownGender;
    Button btnSimpan;
    ImageView imgPreview;

    DBHelper dbHelper;

    private static final int PICK_IMAGE_REQUEST = 1;
    String selectedFotoPath = "";
    boolean isImageSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_mahasiswa);

        // Inisialisasi view
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

        // Date picker
        etTanggalLahir.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            new DatePickerDialog(this, (view, y, m, d) -> {
                etTanggalLahir.setText(d + "/" + (m + 1) + "/" + y);
            }, year, month, day).show();
        });

        // Dropdown gender
        String[] genderItems = {"Laki-laki", "Perempuan"};
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, genderItems);
        dropdownGender.setAdapter(genderAdapter);
        dropdownGender.setOnClickListener(v -> dropdownGender.showDropDown());

        // Pilih gambar
        imgPreview.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        // Simpan data
        btnSimpan.setOnClickListener(v -> {
            String npm = etNpm.getText().toString();
            String nama = etNama.getText().toString();
            String jurusan = etJurusan.getText().toString();
            String tglLahir = etTanggalLahir.getText().toString();
            String gender = dropdownGender.getText().toString();
            String alamat = etAlamat.getText().toString();
            String angkatan = etAngkatan.getText().toString();
            String fakultas = etFakultas.getText().toString();
            String email = etEmail.getText().toString();

            if (npm.isEmpty() || nama.isEmpty()) {
                Toast.makeText(this, "NPM dan Nama wajib diisi", Toast.LENGTH_SHORT).show();
                return;
            }

            // Simpan data ke database
            boolean berhasil = dbHelper.insertMahasiswa(npm, nama, jurusan, tglLahir, gender, alamat, angkatan, fakultas, email, selectedFotoPath);
            if (berhasil) {
                Toast.makeText(this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Simpan gambar ke internal storage
    private String saveImageToInternalStorage(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();

            String filename = "foto_" + System.currentTimeMillis() + ".png";
            File file = new File(getFilesDir(), filename);
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();

            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            selectedFotoPath = saveImageToInternalStorage(uri);
            isImageSelected = true;

            if (!selectedFotoPath.isEmpty()) {
                File imgFile = new File(selectedFotoPath);
                if (imgFile.exists()) {
                    imgPreview.setImageBitmap(BitmapFactory.decodeFile(selectedFotoPath));
                }
            }
        }
    }
}
