package apps.lnsel.com.vhortexttest.utils;

import android.util.Log;

/**
 * Created by db on 12/11/2017.
 */

public class AppThreadUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        Log.e("TAG", "Received exception '" + ex.getMessage() + "' from thread " + thread.getName(), ex);
        //Toast.makeText(AppThreadUncaughtExceptionHandler.this,"",Toast.LENGTH_SHORT).show();
    }

}
