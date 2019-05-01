package com.example.autopum;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class TankowanieActivity extends AppCompatActivity {

    public TextView textViewData;


    public EditText editTextLitry;
    public TextView editTextCena;
    public EditText editTextLicznik;
    public String kierowca;
    public String pojazd;
    public  JSONObject tankowanie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tankowanie);

        textViewData = findViewById(R.id.textViewData);

        editTextLitry = findViewById(R.id.editTextLitry);
        editTextCena = findViewById(R.id.editTextCena);
        editTextLicznik = findViewById(R.id.editTextLicznik);



        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    tankowanie = SendJSON.getJSON( MainActivity.RESTURL + "tankowanie?pojazd=" + MainActivity.pojazd);
                }catch (Exception e) {
                }
            }
        });

        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        try {
            if(tankowanie.has("pojazd"))
            textViewData.setText(tankowanie.getString("data") + "\nVolume: " + tankowanie.getString("litry") + " l\nOdometer: "
            + tankowanie.getString("licznik") + " km");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onClickClose(View v){
        finish();
    }

    public void onClickSend(View v){


        final JSONObject jsonObject = new JSONObject();
        try {
            kierowca = MainActivity.kierowca;
            pojazd = MainActivity.pojazd;
            jsonObject.put("pojazd", pojazd);
            jsonObject.put("kierowca", kierowca);
            jsonObject.put("litry", "0" + editTextLitry.getText().toString());
            jsonObject.put("cena", "0" + editTextCena.getText().toString());
            jsonObject.put("licznik", "0" + editTextLicznik.getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                SendJSON.send(MainActivity.RESTURL + "tankowanie",jsonObject);
            }
        }).start();

        finish();
    }
}
