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


    public EditText editTextLitry;
    public TextView editTextCena;
    public EditText editTextLicznik;
    public String kierowca;
    public String pojazd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tankowanie);

        editTextLitry = findViewById(R.id.editTextLitry);
        editTextCena = findViewById(R.id.editTextCena);
        editTextLicznik = findViewById(R.id.editTextLicznik);
    }

    public void onClickSend(View v){


        final JSONObject jsonObject = new JSONObject();
        try {
            kierowca = MainActivity.kierowca;
            pojazd = MainActivity.pojazd;
            jsonObject.put("pojazd", pojazd);
            jsonObject.put("kierowca", kierowca);
            jsonObject.put("litry", editTextLitry.getText().toString());
            jsonObject.put("cena", editTextCena.getText().toString());
            jsonObject.put("licznik", editTextLicznik.getText().toString());

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
