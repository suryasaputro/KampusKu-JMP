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

import java.io.*;
import java.util.Calendar;

public class EditMahasiswaActivity extends AppCompatActivity {

    EditText etNpm, etNama, etJurusan, etTanggalLahir, etAlamat, etAngkatan, etFakultas, etEmail;
    AutoCompleteTextView dropdownGender;
    Button btnEdit;
    ImageView imgPreview;
    DBHelper dbHelper;

    int mahasiswaId = -1;
    String selectedFotoPath = "";
    boolean isImageChanged = false;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_mahasiswa);

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
        btnEdit = findViewById(R.id.btnEdit);
        imgPreview = findViewById(R.id.imgPreview);

        dbHelper = new DBHelper(this);

        // Tampilkan data yang sudah ada
        mahasiswaId = getIntent().getIntExtra("id", -1);
        if (mahasiswaId != -1) {
            Mahasiswa mhs = dbHelper.getMahasiswaById(mahasiswaId);
            if (mhs != null) {
                etNpm.setText(mhs.getNpm());
                etNama.setText(mhs.getNama());
                etJurusan.setText(mhs.getJurusan());
                etTanggalLahir.setText(mhs.getTglLahir());
                etAlamat.setText(mhs.getAlamat());
                etAngkatan.setText(mhs.getAngkatan());
                etFakultas.setText(mhs.getFakultas());
                etEmail.setText(mhs.getEmail());
                dropdownGender.setText(mhs.getGender(), false);
                selectedFotoPath = mhs.getFotoProfil();

                // Tampilkan gambar jika ada
                if (selectedFotoPath != null && !selectedFotoPath.isEmpty()) {
                    File imgFile = new File(selectedFotoPath);
                    if (imgFile.exists()) {
                        Bitmap bitmap = BitmapFactory.decodeFile(selectedFotoPath);
                        imgPreview.setImageBitmap(bitmap);
                    }
                }
            }
        }

        // Gender dropdown
        String[] genderItems = {"Laki-laki", "Perempuan"};
        ArrayAdapter<String> adapterGender = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, genderItems);
        dropdownGender.setAdapter(adapterGender);
        dropdownGender.setOnClickListener(v -> dropdownGender.showDropDown());

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

        // Pilih gambar
        imgPreview.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        // Tombol edit
        btnEdit.setText("Perbarui");
        btnEdit.setOnClickListener(v -> {
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

            boolean berhasil = dbHelper.updateMahasiswa(mahasiswaId, npm, nama, jurusan, tgl, gender, alamat, angkatan, fakultas, email, selectedFotoPath);
            if (berhasil) {
                Toast.makeText(this, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Gagal memperbarui data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Simpan gambar ke internal storage
    private String saveImageToInternalStorage(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            if (inputStream != null) inputStream.close();

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
            isImageChanged = true;

            File imgFile = new File(selectedFotoPath);
            if (imgFile.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(selectedFotoPath);
                imgPreview.setImageBitmap(bitmap);
            }
        }
    }
}
