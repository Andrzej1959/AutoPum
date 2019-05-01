package com.example.autopum;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SendJSON {

    public static void send(String targetUrl, JSONObject jsonObject){
    URL url = null;
    HttpURLConnection urlConnection = null;
        try {
        url = new URL(targetUrl);
        //url = new URL("http://lukan.sytes.net:1880/mapa");
        urlConnection = (HttpURLConnection) url.openConnection();

        urlConnection.setDoOutput(true);
        urlConnection.setChunkedStreamingMode(0);
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("content-type","application/json");

        OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());

        out.write(jsonObject.toString().getBytes());
        out.close();

        InputStream in = new BufferedInputStream(urlConnection.getInputStream());

    } catch (
                IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    public static JSONObject getResponse(String targetUrl, JSONObject jsonObject){
        URL url = null;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL(targetUrl);
            //url = new URL("http://lukan.sytes.net:1880/mapa");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setChunkedStreamingMode(0);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("content-type","application/json");

            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());

            out.write(jsonObject.toString().getBytes());
            out.close();

           // InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(urlConnection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);

            String tmp="";
            while((tmp=reader.readLine())!=null)
                json.append(tmp).append("\n");
            reader.close();

            JSONObject data = new JSONObject(json.toString());
            return data;

        } catch (
                IOException | JSONException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }


    public static JSONArray getArray(String targetUrl){
        URL url = null;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL(targetUrl);
            //url = new URL("http://lukan.sytes.net:1880/mapa");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(false);
            urlConnection.setChunkedStreamingMode(0);
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("content-type","application/json");

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(urlConnection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);

            String tmp="";
            while((tmp=reader.readLine())!=null)
                json.append(tmp).append("\n");
            reader.close();

            JSONArray data = new JSONArray(json.toString());
            return data;
        } catch (
                IOException | JSONException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }

    public static JSONObject getJSON(String targetUrl){
        URL url = null;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL(targetUrl);
            //url = new URL("http://lukan.sytes.net:1880/mapa");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(false);
            urlConnection.setChunkedStreamingMode(0);
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("content-type","application/json");

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(urlConnection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);

            String tmp="";
            while((tmp=reader.readLine())!=null)
                json.append(tmp).append("\n");
            reader.close();

            JSONObject data = new JSONObject(json.toString());
            return data;

        } catch (
                IOException | JSONException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }

}
