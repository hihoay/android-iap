package taymay.iap.frameworks.utils;

import android.content.Context;
import android.net.ConnectivityManager;

import java.io.File;



public class MyConnection {


    public static boolean isOnline(Context mContext) {
        try {
            ConnectivityManager cm =
                    (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo().isConnectedOrConnecting();
        } catch (Exception e) {
            return false;
        }
    }









    public interface OnDone {
        void onDone(File file);
    }
}
