package com.projectstalker.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.io.InputStream;
import java.io.OutputStream;

public class Utils {
    public static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (; ; ) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
        }
    }

    public static boolean isNetworkConnected(Context ctx) {
        ConnectivityManager connectivityManager = (ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected() ||
               connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();
    }

    public static void toastAndDebugLog(Context ctx, String tag, String message) {
        Log.d(tag, message);
        Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
    }

    public static String getDeviceId(Context ctx) {
        return ((TelephonyManager)ctx.getSystemService(Context.TELEPHONY_SERVICE)).getSimSerialNumber();
        /*return Settings.Secure.getString(
                ctx.getContentResolver(),
                Settings.Secure.ANDROID_ID);*/
    }
}
