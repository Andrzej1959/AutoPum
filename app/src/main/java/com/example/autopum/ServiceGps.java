package com.example.autopum;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServiceGps extends Service {
    public ServiceGps() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }



    public LocationManager locationManager;
    public LocationProvider locationProvider;

    String ip;
    String name;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        ip =  intent.getStringExtra(MainActivity.IP);
        name = intent.getStringExtra("name");

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {

       // locationManager.removeUpdates(locationListener);
       // locationManager = null;

        super.onDestroy();
    }

    @Override
    public void onCreate() {

        super.onCreate();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationProvider = locationManager.getProvider(LocationManager.GPS_PROVIDER);
        //locationProvider = locationManager.getProvider(LocationManager.NETWORK_PROVIDER);

        if (locationProvider != null) {
            try {
                this.locationManager.requestLocationUpdates(locationProvider.getName(), 10000, 25,
                        this.locationListener);
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }




        // Notyfikacje

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        String channel; // =  createChannel();

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            channel = createChannel();
        } else {
            channel = "kanalik";
        }

        Notification notification = null;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notification = new Notification.Builder(this, channel)
                    .setContentTitle("kookokookok")
                    .setContentText("jijijijijijiiji")
                    // .setSmallIcon(R.drawable.ikona)
                    .setContentIntent(pendingIntent)
                    .setTicker("okokokokokokko")
                    .build();

            startForeground(5555, notification);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private synchronized String createChannel() {
        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        String name = "snap map fake location ";
        int importance = NotificationManager.IMPORTANCE_LOW;

        NotificationChannel mChannel = new NotificationChannel("snap map channel", name, importance);

        mChannel.enableLights(true);
        mChannel.setLightColor(Color.BLUE);
        if (mNotificationManager != null) {
            mNotificationManager.createNotificationChannel(mChannel);
        } else {
            stopSelf();
        }
        return "snap map channel";
    }

    Location lastLocation;

    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {


            final double lat = location.getLatitude();
            final double lon = location.getLongitude();
            final long time = location.getTime();
            final  double speed = location.getSpeed();
            final  double bearing = location.getBearing();
            final  double accuracy = location.getAccuracy();
            //DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSSXXX");
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            Date date = new Date(location.getTime());
            final String formatted = format.format(date);



            if(lastLocation == null) lastLocation = location;

            final  double dystans = location.distanceTo(lastLocation);
            lastLocation = location;


            final JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("name", ip);
                jsonObject.put("id_trasy", ip);
                jsonObject.put("lat", lat);
                jsonObject.put("lon", lon);
                jsonObject.put("speed", speed*3.6);
                jsonObject.put("bearing", bearing);
                jsonObject.put("accuracy", accuracy);
                jsonObject.put("timeGPS", formatted);
                jsonObject.put("time", time);
                jsonObject.put("dystans", dystans);

               // jsonObject.put("icon", "fa-truck");
               // jsonObject.put("iconColor", "DarkGreen");
               // jsonObject.put("color", "DarkGreen");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    SendJSON.send(MainActivity.RESTURL + "mapa",jsonObject);
                }
            }).start();

        }



        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };
}
