package com.example.myapplication;

import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

public class FirebaseConfig {

    // ↓↓↓ מעתיקים את הערכים החסרים מ-Firebase ↓↓↓
    public static final String API_KEY = "AIzaSyCS-ZR5UnvWxA06lq8Q4jbWg4KQU7PVA0A";
    public static final String APPLICATION_ID = "1:6157881239:android:87937849cbed1d345bbed2";
    public static final String DATABASE_URL = "https://bizcard-c855d-default-rtdb.firebaseio.com/";
    // ↑↑↑ בלי רווחים, ובתוך המרכאות ↑↑↑

    // האם מילאו את שלושת הערכים?
    public static boolean isConfigured() {
        return !API_KEY.isEmpty() && !APPLICATION_ID.isEmpty() && !DATABASE_URL.isEmpty();
    }

    // מחבר את האפליקציה ל-Firebase. מספיק פעם אחת: בפעם השנייה יוצאים מיד
    public static void ensureInitialized(Context context) {
        if (!FirebaseApp.getApps(context).isEmpty()) {
            return;                                     // כבר מחובר
        }
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setApiKey(API_KEY)
                .setApplicationId(APPLICATION_ID)
                .setDatabaseUrl(DATABASE_URL)
                .build();
        FirebaseApp.initializeApp(context.getApplicationContext(), options);
    }
}