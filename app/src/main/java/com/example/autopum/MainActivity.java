package com.example.autopum;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 0;

    Button buttonStart;
    EditText editPojazd;
    EditText editKierowca;
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

        buttonStart = (Button) findViewById(R.id.buttonStart);
        editKierowca = findViewById(R.id.editKierowca);
        editPojazd = findViewById(R.id.editPojazd);
        editCel = findViewById(R.id.editCel);
        textIdTrasy = findViewById(R.id.textIdTrasy);
    }

    public static String IP="ip";
    public String id_trasy;
    public JSONObject odp;

    public void onClickStart(View v) throws InterruptedException {

        final JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("pojazd", editPojazd.getText().toString());
            jsonObject1.put("kierowca", editKierowca.getText().toString());
            jsonObject1.put("cel", editCel.getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                odp = SendJSON.getResponse("http://lukan.sytes.net:1880/nowatrasa",jsonObject1);

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

        Intent intent1 = new Intent(this, ServiceGps.class);
        intent1.putExtra(IP, id_trasy);
        intent1.putExtra("name", editCel.getText().toString() + id_trasy);
        startService(intent1);


        //this.getApplication().startForegroundService(intent);

        //Intent intent = new Intent(this, IntentServiceGPS.class);
        //startService(intent);
        //startForegroundService(intent);


    }






}
