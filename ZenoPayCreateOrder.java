package com.example.myapp;

import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CreateOrderTask extends AsyncTask<Void, Void, String> {

    private static final String API_URL = "https://api.zeno.africa";
    private static final String BUYER_EMAIL = "YOUR_CUSTOMER_EMAIL";
    private static final String BUYER_NAME = "YOUR_CUSTOMER_NAME";
    private static final String BUYER_PHONE = "0752117588";
    private static final int AMOUNT = 10000;
    private static final String ACCOUNT_ID = "YOUR_ACCOUNT_ID";
    private static final String API_KEY = "YOUR_KEY";
    private static final String SECRET_KEY = "YOUR_SECRET_KEY";

    @Override
    protected String doInBackground(Void... voids) {
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(API_URL);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            String postData = "create_order=1&buyer_email=" + BUYER_EMAIL + "&buyer_name=" + BUYER_NAME +
                    "&buyer_phone=" + BUYER_PHONE + "&amount=" + AMOUNT + "&account_id=" + ACCOUNT_ID +
                    "&api_key=" + API_KEY + "&secret_key=" + SECRET_KEY;

            OutputStream os = urlConnection.getOutputStream();
            os.write(postData.getBytes());
            os.flush();
            os.close();

            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();

            return response.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    @Override
    protected void onPostExecute(String result) {
        // Handle the response
        System.out.println(result);
    }
}
