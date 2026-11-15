package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;

public class MainActivity extends AppCompatActivity {

    private String key, name, job, phone, site;
    public Button btnCreate;
    public EditText etName, etJob, etPhone, etSite;

    // null = כרטיס חדש. אם יש כאן מפתח = עורכים כרטיס קיים
    private String editKey = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = findViewById(R.id.etName);
        etJob = findViewById(R.id.etJob);
        etPhone = findViewById(R.id.etPhone);
        etSite = findViewById(R.id.etSite);
        btnCreate = findViewById(R.id.btnCreate);

        // מזהים מצב עריכה
        editKey = getIntent().getStringExtra("key");
        if (editKey != null) {
            etName.setText(getIntent().getStringExtra("name"));
            etJob.setText(getIntent().getStringExtra("job"));
            etPhone.setText(getIntent().getStringExtra("phone"));
            etSite.setText(getIntent().getStringExtra("site"));

            btnCreate.setText("שמור שינויים");
            setTitle("עריכת כרטיס");
        }

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCard();
            }
        });
    }

    private void saveCard() {
        String name  = etName.getText().toString().trim();
        String job   = etJob.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String site  = etSite.getText().toString().trim();

        if (name.isEmpty()) {
            Toast.makeText(this, "חובה למלא שם", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!FirebaseConfig.isConfigured()) {
            Toast.makeText(this, "חסרים ערכים ב-FirebaseConfig.java", Toast.LENGTH_LONG).show();
            return;
        }

        final Card card = new Card(name, job, phone, site);
        DatabaseReference ref = CardsDb.cards(this);

        if (editKey == null) {
            // ===== הוספה (Create) =====
            String key = ref.push().getKey();
            card.setKey(key);

            ref.child(key).setValue(card)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(MainActivity.this, "נשמר בענן", Toast.LENGTH_SHORT).show();
                            clearForm();
                            openCard(card);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "השמירה נכשלה: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

        } else {
            // ===== עדכון (Update) =====
            card.setKey(editKey);
            ref.child(editKey).setValue(card)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(MainActivity.this, "הכרטיס עודכן", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "העדכון נכשל: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

    private void openCard(Card c) {
        Intent intent = new Intent(this, CardActivity.class);
        intent.putExtra("key", c.getKey());
        intent.putExtra("name", c.getName());
        intent.putExtra("job", c.getJob());
        intent.putExtra("phone", c.getPhone());
        intent.putExtra("site", c.getSite());
        startActivity(intent);
    }

    private void clearForm() {
        etName.setText("");
        etJob.setText("");
        etPhone.setText("");
        etSite.setText("");
        etName.requestFocus();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            etName.setText("דוגמה");
            etJob.setText("תפקיד");
            etPhone.setText("050-0000000");
            etSite.setText("example.com");
            return true;
        }

        if (item.getItemId() == R.id.action_reset) {
            clearForm();
            return true;
        }

        if (item.getItemId() == R.id.action_list) {
            startActivity(new Intent(this, CardsListActivity.class));
            return true;
        }

        if (item.getItemId() == R.id.action_about) {
            startActivity(new Intent(this, AboutActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}