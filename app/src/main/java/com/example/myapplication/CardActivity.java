package com.example.myapplication;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class CardActivity extends AppCompatActivity {

    private String key, name, job, phone, site;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        TextView tvName = findViewById(R.id.tvName);
        TextView tvJob = findViewById(R.id.tvJob);
        TextView tvPhone = findViewById(R.id.tvPhone);
        TextView tvSite = findViewById(R.id.tvSite);

        key   = getIntent().getStringExtra("key");
        name  = getIntent().getStringExtra("name");
        job   = getIntent().getStringExtra("job");
        phone = getIntent().getStringExtra("phone");
        site  = getIntent().getStringExtra("site");

        tvName.setText(name);
        tvJob.setText(job);
        tvPhone.setText(phone);
        tvSite.setText(site);

        findViewById(R.id.btnDial).setOnClickListener(v -> {
            try {
                Intent dial = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                startActivity(dial);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "אין אפליקציה מתאימה לחיוג", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btnWeb).setOnClickListener(v -> {
            try {
                String finalSite = site;
                if (finalSite != null && !finalSite.startsWith("http://") && !finalSite.startsWith("https://")) {
                    finalSite = "https://" + finalSite;
                }
                Intent web = new Intent(Intent.ACTION_VIEW, Uri.parse(finalSite));
                startActivity(web);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "אין דפדפן מותקן", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btnShare).setOnClickListener(v -> {
            try {
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, "כרטיס ביקור:\n" + name + "\n" + job + "\n" + phone + "\n" + site);
                startActivity(Intent.createChooser(share, "שתף כרטיס עם…"));
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "אין אפליקציית שיתוף", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.card_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_edit) {
            Intent edit = new Intent(this, MainActivity.class);
            edit.putExtra("key", key);
            edit.putExtra("name", name);
            edit.putExtra("job", job);
            edit.putExtra("phone", phone);
            edit.putExtra("site", site);

            startActivity(edit);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}