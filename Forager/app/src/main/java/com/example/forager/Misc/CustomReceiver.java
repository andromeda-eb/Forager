package com.example.forager.Misc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.forager.Activities.SettingsActivity;

public class CustomReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String broadcastMessage = "Unknown intent action";

        String intentAction = intent.getAction();

        if( intentAction.equals(Intent.ACTION_AIRPLANE_MODE_CHANGED) )
            broadcastMessage = "System Broadcast Message: Airplane Mode has been changed";

        else if( intentAction.equals(SettingsActivity.CUSTOM_ACTION_BROADCAST_THEME_CHANGED) )
            broadcastMessage = "Custom Broadcast Message: Theme has been changed";

        Toast.makeText(context, broadcastMessage, Toast.LENGTH_SHORT).show();
    }
}
