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
    public void onCreate() {

        super.onCreate();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationProvider = locationManager.getProvider(LocationManager.GPS_PROVIDER);

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

        Notification notification =
                new Notification.Builder(this, channel)
                        .setContentTitle("kookokookok")
                        .setContentText("jijijijijijiiji")
                        // .setSmallIcon(R.drawable.ikona)
                        .setContentIntent(pendingIntent)
                        .setTicker("okokokokokokko")
                        .build();

        startForeground(5555, notification);

    }

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



    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {


            final double lat = location.getLatitude();
            final double lon = location.getLongitude();
            final double time = location.getTime();
            final  double speed = location.getSpeed();

            DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSSXXX");
            Date date = new Date(location.getTime());
            final String formatted = format.format(date);


            final JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("name", ip);
                jsonObject.put("id_trasy", ip);
                jsonObject.put("lat", lat);
                jsonObject.put("lon", lon);
                jsonObject.put("speed", speed);
                jsonObject.put("timeFormat", formatted);
                jsonObject.put("time", time);
               // jsonObject.put("icon", "fa-truck");
               // jsonObject.put("iconColor", "DarkGreen");
               // jsonObject.put("color", "DarkGreen");

            } catch (JSONException e) {
                e.printStackTrace();
            }

                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                    SendJSON.send("http://lukan.sytes.net:1880/mapa",jsonObject);

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
