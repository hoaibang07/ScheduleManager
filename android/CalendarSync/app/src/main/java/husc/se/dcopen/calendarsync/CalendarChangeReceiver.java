package husc.se.dcopen.calendarsync;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;

import java.net.URI;

public class CalendarChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        android.net.Uri uri = intent.getData();
        String action = intent.getAction();

        Log.e("ON RECEIVER", "" + uri);
        Log.e("ON RECEIVER", "" + action);
    }
}