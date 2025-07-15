package com.example.kampusku;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {

    Button btnData, btnInfo, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        btnData = findViewById(R.id.btnData);
        btnInfo = findViewById(R.id.btnInfo);
        btnLogout = findViewById(R.id.btnLogout);

        btnData.setOnClickListener(v -> {
            startActivity(new Intent(DashboardActivity.this, ListMahasiswaActivity.class));
        });

        btnInfo.setOnClickListener(v -> {
            startActivity(new Intent(DashboardActivity.this, InformasiActivity.class));
        });

        btnLogout.setOnClickListener(v -> {
            // Hapus session
            SharedPreferences preferences = getSharedPreferences("user_session", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.apply();

            // Arahkan ke login
            Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Hindari kembali ke dashboard
            startActivity(intent);
            finish();
        });
    }
}
