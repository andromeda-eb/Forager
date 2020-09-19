package com.example.forager.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.forager.BuildConfig;
import com.example.forager.R;

public class SettingsActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    private Switch nightMode;
    private int nightModeState;
    public static final String CUSTOM_ACTION_BROADCAST_THEME_CHANGED = BuildConfig.APPLICATION_ID + ".CUSTOM_ACTION_BROADCAST_THEME_CHANGED";
    public static final String SETTINGS_KEY = "SETTINGS_REPLY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toast.makeText(getApplicationContext(), getIntent().getStringExtra(MainActivity.GREETING), Toast.LENGTH_SHORT).show();

        nightMode = findViewById(R.id.nightMode);

        nightModeState = AppCompatDelegate.getDefaultNightMode();

        if(nightModeState == AppCompatDelegate.MODE_NIGHT_YES)
            nightMode.setChecked(true);

        if(nightMode != null)
            nightMode.setOnCheckedChangeListener(this);

        LocalBroadcastManager.getInstance(this).registerReceiver(MainActivity.receiver, new IntentFilter(CUSTOM_ACTION_BROADCAST_THEME_CHANGED));

        Intent reply_intent = new Intent();

        reply_intent.putExtra(SETTINGS_KEY, getString(R.string.settings_reply));

        setResult(RESULT_OK, reply_intent);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        // Custom Broadcast
        Intent customBroadcastIntent = new Intent(CUSTOM_ACTION_BROADCAST_THEME_CHANGED);

        // If switch is on or it isn't night mode
        if(isChecked || nightModeState == AppCompatDelegate.MODE_NIGHT_NO)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        LocalBroadcastManager.getInstance(this).sendBroadcast(customBroadcastIntent);
    }
}
