package com.example.autopum;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DaneActivity extends AppCompatActivity {

    public ListView listview;
    public JSONObject linia;
    public JSONArray tablica;
    //public final String RESTURL = "http://lukan.sytes.net:1880/";
    public static String lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dane);

        listview = findViewById(R.id.lista);
        lista = getIntent().getStringExtra("lista").toString();

        new Thread(new Runnable() {
            @Override
            public void run() {
                tablica = SendJSON.getArray( MainActivity.RESTURL + lista);
            }
        }).start();

        while (tablica == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        final ArrayList<String> list = new ArrayList<String>();

        //final ArrayList<JSONObject> jasonList = new ArrayList<JSONObject>((Collection<? extends JSONObject>) tablica);

        for (int i = 0; i < tablica.length(); ++i) {
            try {
                linia = tablica.getJSONObject(i);
                list.add(linia.getString("opis"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                JSONObject pozycja;
                String linia = null;
                String klucz = null;
                try {
                    pozycja = tablica.getJSONObject(position);
                    linia = pozycja.getString("klucz") + " -> ";
                    linia += pozycja.getString("opis");
                    klucz = pozycja.getString("klucz");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Toast.makeText(getApplicationContext(),lista + "  " + linia, Toast.LENGTH_LONG).show();

                if(lista.equals("listakierowcow")) MainActivity.kierowca = klucz;
                if(lista.equals("listapojazdow")) MainActivity.pojazd = klucz;

                finish();
            }
        });
    }
}
