package com.example.autopum;

import android.Manifest;
import android.app.Notification;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 0;
    public static String RESTURL = "http://lukan.sytes.net:1880/";

    Button buttonStart;
    EditText editPojazd;
    public  EditText editKierowca;
    EditText editCel;
    TextView textIdTrasy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
            // MY_PERMISSIONS_REQUEST_LOCATION is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }

        buttonStart =  findViewById(R.id.buttonStart);
        editKierowca = findViewById(R.id.editKierowca);
        editPojazd =   findViewById(R.id.editPojazd);
        editCel =      findViewById(R.id.editCel);
        textIdTrasy =  findViewById(R.id.textIdTrasy);
       // if (savedInstanceState != null) id_trasy = savedInstanceState.getString("id_trasy");


        if(id_trasy != null) textIdTrasy.setText("Trasa nr: " + id_trasy);
        if(kierowca != null) editKierowca.setText(kierowca);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("id_trasy", "kokkokokokokoook");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        Toast.makeText(this,"Restore", Toast.LENGTH_LONG).show();

      //  textIdTrasy.setText("Trasa nr: " + savedInstanceState.getString("id_trasy"));
    }

    public static String IP="ip";
    public static String id_trasy;
    public static String kierowca;
    public JSONObject odp;
   // public Intent intentGps;

    public void onClickStart(View v) throws InterruptedException {

        if(id_trasy == null) {
            final JSONObject jsonObject1 = new JSONObject();
            try {
                kierowca = editKierowca.getText().toString();
                jsonObject1.put("pojazd", editPojazd.getText().toString());
                jsonObject1.put("kierowca", editKierowca.getText().toString());
                jsonObject1.put("cel", editCel.getText().toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            //id_trasy = null;


            new Thread(new Runnable() {
                @Override
                public void run() {
                    odp = SendJSON.getResponse(RESTURL + "nowatrasa", jsonObject1);

                    try {
                        id_trasy = odp.getString("id");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }).start();

            // czeka na odpowiedż -------------------------------------------------------  może zmienić
            while (id_trasy == null) {

                Thread.sleep(100);
            }

            textIdTrasy.setText("Trasa nr: " + id_trasy);

            //
            intentGps = new Intent(MainActivity.this, ServiceGps.class);

            intentGps.putExtra(IP, id_trasy);
            intentGps.putExtra("name", editCel.getText().toString() + id_trasy);
            startService(intentGps);

            Toast.makeText(this, "Start", Toast.LENGTH_LONG).show();

            buttonStart.setEnabled(false);
        }
        else {
            Toast.makeText(this, "Trasa już wystartowana", Toast.LENGTH_LONG).show();
        }

    }

    public static Intent intentGps;

    public void onClickStop(View v){
        //stopService(intentGps);
        //stopService(new Intent(MainActivity.this, ServiceGps.class));
        Toast.makeText(this,"Stop", Toast.LENGTH_LONG).show();
        this.finish();
        //buttonStart.setEnabled(true);
    }


}
