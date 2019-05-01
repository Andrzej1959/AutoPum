package com.example.autopum;

import android.Manifest;
import android.app.AlertDialog;
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
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 0;
    public static String RESTURL = "http://lukan.sytes.net:1880/";
    //public static String RESTURL = "http://lukan.sytes.net:1880/";

    public Button buttonStart;
    public Button buttonKierowca;
    public EditText editPojazd;
    public EditText editKierowca;
    public EditText editCel;
    public EditText editTextLicznik;
    public TextView textIdTrasy;

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
        buttonKierowca =  findViewById(R.id.buttonKierowca);
        editKierowca = findViewById(R.id.editKierowca);
        editPojazd =   findViewById(R.id.editPojazd);
        editCel =      findViewById(R.id.editCel);
        editTextLicznik = findViewById(R.id.editTextLicznik);
        textIdTrasy =  findViewById(R.id.textIdTrasy);
    }

    public static String ID_TRASY ="ip";   //Identyfikator do wiadomości do usługi
    public static String id_trasy;
    public static String kierowca;
    public static String pojazd;
    public static String cel = "Cel";

    public JSONObject odp;

    @Override
    protected void onPostResume() {
        super.onPostResume();

        if(id_trasy != null) textIdTrasy.setText("Trasa nr: " + id_trasy);
        else textIdTrasy.setText("");

        if(kierowca != null) editKierowca.setText(kierowca);
        if(pojazd != null) editPojazd.setText(pojazd);

    }

    public void onClickStart(View v) throws InterruptedException {

        if(kierowca == null || pojazd == null){
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setTitle("Uwaga");
            dialogBuilder.setMessage("Najpierw wybierz kierowcę i pojazd");
            dialogBuilder.create().show();

        }else {

            if (id_trasy == null) {
                final JSONObject jsonObject1 = new JSONObject();
                try {
                    kierowca = editKierowca.getText().toString();
                    jsonObject1.put("pojazd", editPojazd.getText().toString());
                    jsonObject1.put("kierowca", editKierowca.getText().toString());

                    jsonObject1.put("licznik", "0" + editTextLicznik.getText().toString());
                    cel = editCel.getText().toString();
                    jsonObject1.put("cel", editCel.getText().toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        odp = SendJSON.getResponse(RESTURL + "nowatrasa", jsonObject1);

                        try {
                            id_trasy = odp.getString("id");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
                thread.join();

                textIdTrasy.setText("Trasa nr: " + id_trasy);

                if (intentGps == null) intentGps = new Intent(MainActivity.this, ServiceGps.class);

                intentGps.putExtra(ID_TRASY, id_trasy);
                intentGps.putExtra("name", editCel.getText().toString() + id_trasy);
                startService(intentGps);

                buttonStart.setEnabled(false);
            } else {
                Toast.makeText(this, "Trasa już wystartowana", Toast.LENGTH_LONG).show();
            }
        }
    }

    public static Intent intentGps;

    public void onClickStop(View v){
        if (intentGps != null) stopService(intentGps);
        buttonStart.setEnabled(true);
        id_trasy = null;
    }

    public void onClickKierowca(View v){
        Intent intent = new Intent(MainActivity.this, DaneActivity.class);
        intent.putExtra("lista", "listakierowcow");
        startActivity(intent);
    }

    public void onClickPojazd(View v){
        Intent intent = new Intent(MainActivity.this, DaneActivity.class);
        intent.putExtra("lista", "listapojazdow");
        startActivity(intent);
    }

    public void onClickRefueling(View v){

        if(kierowca == null || pojazd == null){

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setTitle("Uwaga");
            dialogBuilder.setMessage("Najpierw wybierz kierowcę i pojazd");
            dialogBuilder.create().show();

        }else {
            Intent intent = new Intent(MainActivity.this, TankowanieActivity.class);
            //intent.putExtra("lista", "listapojazdow");
            startActivity(intent);
        }
    }
}
