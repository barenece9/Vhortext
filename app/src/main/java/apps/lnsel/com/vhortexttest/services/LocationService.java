package apps.lnsel.com.vhortexttest.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import apps.lnsel.com.vhortexttest.presenters.LocationServicePresenter;
import apps.lnsel.com.vhortexttest.utils.ConstantUtil;
import apps.lnsel.com.vhortexttest.utils.SharedManagerUtil;
import apps.lnsel.com.vhortexttest.utils.UrlUtil;


/**
 * Created by apps2 on 6/1/2017.
 */
public class LocationService extends Service {
    SharedManagerUtil session;

    private LocationServicePresenter service;

    //final Handler handler = new Handler();
    private Handler handler = new Handler();
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        //Toast.makeText(this, " MyService Created ", Toast.LENGTH_LONG).show();

        // Session Manager
        session = new SharedManagerUtil(this);

        service = new LocationServicePresenter(this);


    }

    private Runnable timedTask = new Runnable(){

        @Override
        public void run() {
            // TODO Auto-generated method stub
            System.out.println("Location Service Running");
            locationUpdate();
            handler.postDelayed(timedTask, 1800000);
            //location Update in every (30*60*1000) seconds(i.e 30 min)
        }};



    @Override
    public void onStart(Intent intent, int startId) {
        handler.post(timedTask);
        System.out.println("Location Service Started");
       // Toast.makeText(this, " Location Service Started", Toast.LENGTH_LONG).show();
    }


    @Override
    public void onDestroy() {
        //handler.removeCallbacks(null);
        handler.removeCallbacks(timedTask);
        System.out.println("Location Service Stopped");
        Toast.makeText(this, "Location Stopped", Toast.LENGTH_LONG).show();
    }


    public void locationUpdate(){

        if(session.getIsDeviceActive() && isNetworkAvailable()){

            if(session.isLoggedIn() && session.getUserLocationPermission().equalsIgnoreCase("true")){

                GPSService mGPSService = new GPSService(this);
                mGPSService.getLocation();

                if (mGPSService.isLocationAvailable == false) {
                    System.out.println("Location not available, Open GPS");
                    //Toast.makeText(this, "Location not available, Open GPS", Toast.LENGTH_SHORT).show();
                } else {

                    SimpleDateFormat stf = new SimpleDateFormat("HH:mm");
                    String current_time = stf.format(new Date());

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String current_date = sdf.format(new Date());

                    // Getting location co-ordinates
                    double latitude = mGPSService.getLatitude();
                    double longitude = mGPSService.getLongitude();

                    String currentLat = Double.toString(latitude);
                    String currentLong = Double.toString(longitude);


                    ConstantUtil.latitude=currentLat;
                    ConstantUtil.longitude=currentLong;


                    if(currentLat.equals("0.0")){
                        System.out.println("Location Updated : "+currentLat+" / "+currentLong);
                        //Toast.makeText(this, "GPS not Responding, Check your GPS", Toast.LENGTH_LONG).show();
                    }else{
                        System.out.println("Location Updated : "+currentLat+" / "+currentLong);
                        service.LocationUpdateService(UrlUtil.LOCATION_UPDATE_URL,
                                UrlUtil.API_KEY,
                                session.getUserId(),
                                currentLat,
                                currentLong,
                                ConstantUtil.APP_VERSION,
                                ConstantUtil.APP_TYPE,
                                ConstantUtil.DEVICE_ID);
                    }


                }

            }

        }

    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
