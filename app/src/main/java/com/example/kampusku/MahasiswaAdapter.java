package com.example.kampusku;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.*;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MahasiswaAdapter extends RecyclerView.Adapter<MahasiswaAdapter.ViewHolder> {

    Context context;
    ArrayList<Mahasiswa> list;
    DBHelper dbHelper;

    public MahasiswaAdapter(Context context, ArrayList<Mahasiswa> list) {
        this.context = context;
        this.list = list;
        dbHelper = new DBHelper(context);
    }

    @NonNull
    @Override
    public MahasiswaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_mahasiswa, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MahasiswaAdapter.ViewHolder holder, int position) {
        Mahasiswa mhs = list.get(position);

        holder.tvNomor.setText(String.valueOf(position + 1));
        holder.tvNama.setText(mhs.getNama());
        holder.tvNpm.setText("NPM: " + mhs.getNpm());

        // Klik item → Detail (mengirim semua data termasuk foto_profil)
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailMahasiswaActivity.class);
            intent.putExtra("id", mhs.getId());
            intent.putExtra("npm", mhs.getNpm());
            intent.putExtra("nama", mhs.getNama());
            intent.putExtra("jurusan", mhs.getJurusan());
            intent.putExtra("tgl_lahir", mhs.getTglLahir());
            intent.putExtra("gender", mhs.getGender());
            intent.putExtra("alamat", mhs.getAlamat());
            intent.putExtra("angkatan", mhs.getAngkatan());
            intent.putExtra("fakultas", mhs.getFakultas());
            intent.putExtra("email", mhs.getEmail());
            intent.putExtra("foto_profil", mhs.getFotoProfil());
            context.startActivity(intent);
        });

        // Edit
        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditMahasiswaActivity.class);
            intent.putExtra("id", mhs.getId());
            context.startActivity(intent);
        });

        // Hapus dengan konfirmasi
        holder.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Hapus Data")
                    .setMessage("Yakin ingin menghapus data ini?")
                    .setPositiveButton("Ya", (dialog, which) -> {
                        int adapterPosition = holder.getAdapterPosition();
                        if (dbHelper.deleteMahasiswa(mhs.getId())) {
                            list.remove(adapterPosition);
                            notifyItemRemoved(adapterPosition);
                            notifyItemRangeChanged(adapterPosition, list.size());
                            Toast.makeText(context, "Data dihapus", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Gagal menghapus data", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Batal", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNomor, tvNama, tvNpm, btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNomor = itemView.findViewById(R.id.tvNomor);
            tvNama = itemView.findViewById(R.id.tvNama);
            tvNpm = itemView.findViewById(R.id.tvNpm);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
