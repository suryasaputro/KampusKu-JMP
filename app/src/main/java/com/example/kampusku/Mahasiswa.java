package com.example.kampusku;

public class Mahasiswa {
    private int id;
    private String npm, nama, jurusan, tglLahir, gender, alamat;
    private String angkatan, fakultas, email, fotoProfil;

    // Constructor lengkap
    public Mahasiswa(int id, String npm, String nama, String jurusan, String tglLahir, String gender,
                     String alamat, String angkatan, String fakultas, String email, String fotoProfil) {
        this.id = id;
        this.npm = npm;
        this.nama = nama;
        this.jurusan = jurusan;
        this.tglLahir = tglLahir;
        this.gender = gender;
        this.alamat = alamat;
        this.angkatan = angkatan;
        this.fakultas = fakultas;
        this.email = email;
        this.fotoProfil = fotoProfil;
    }

    // Getter
    public int getId() { return id; }
    public String getNpm() { return npm; }
    public String getNama() { return nama; }
    public String getJurusan() { return jurusan; }
    public String getTglLahir() { return tglLahir; }
    public String getGender() { return gender; }
    public String getAlamat() { return alamat; }
    public String getAngkatan() { return angkatan; }
    public String getFakultas() { return fakultas; }
    public String getEmail() { return email; }
    public String getFotoProfil() { return fotoProfil; }

    // Setter (opsional, hanya kalau ingin ubah data di luar konstruktor)
    public void setId(int id) { this.id = id; }
    public void setNpm(String npm) { this.npm = npm; }
    public void setNama(String nama) { this.nama = nama; }
    public void setJurusan(String jurusan) { this.jurusan = jurusan; }
    public void setTglLahir(String tglLahir) { this.tglLahir = tglLahir; }
    public void setGender(String gender) { this.gender = gender; }
    public void setAlamat(String alamat) { this.alamat = alamat; }
    public void setAngkatan(String angkatan) { this.angkatan = angkatan; }
    public void setFakultas(String fakultas) { this.fakultas = fakultas; }
    public void setEmail(String email) { this.email = email; }
    public void setFotoProfil(String fotoProfil) { this.fotoProfil = fotoProfil; }
}
