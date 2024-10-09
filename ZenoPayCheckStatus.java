import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class OrderStatusCheckTask extends AsyncTask<Void, Void, String> {

    private static final String TAG = "OrderStatusCheckTask";
    private static final String ENDPOINT_URL = "https://api.zeno.africa/order-status";
    private String orderId = "66c4bb9c9abb1";
    private String apiKey = "YOUR_API_KEY";
    private String secretKey = "YOUR_SECRET_KEY";

    @Override
    protected String doInBackground(Void... voids) {
        try {
            // Create the URL object
            URL url = new URL(ENDPOINT_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            // Create the POST data
            String postData = "check_status=1&order_id=" + orderId + "&api_key=" + apiKey + "&secret_key=" + secretKey;

            // Send the request
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(postData);
            wr.flush();
            wr.close();

            // Check the response code
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                
                // Return the JSON response
                return response.toString();
            } else {
                return "{\"status\":\"error\",\"message\":\"Request failed with response code: " + responseCode + "\"}";
            }

        } catch (Exception e) {
            Log.e(TAG, "Request error: ", e);
            return "{\"status\":\"error\",\"message\":\"Request error: " + e.getMessage() + "\"}";
        }
    }

    @Override
    protected void onPostExecute(String result) {
        try {
            // Parse the JSON response
            JSONObject jsonResponse = new JSONObject(result);
            if (jsonResponse.getString("status").equals("success")) {
                // Handle success
                String orderId = jsonResponse.getString("order_id");
                String message = jsonResponse.getString("message");
                String paymentStatus = jsonResponse.optString("payment_status");
                
                // Use the result as needed
                Log.d(TAG, "Success: " + orderId + ", " + message + ", Payment Status: " + paymentStatus);
            } else {
                // Handle error
                String message = jsonResponse.getString("message");
                Log.e(TAG, "Error: " + message);
            }
        } catch (Exception e) {
            Log.e(TAG, "JSON parsing error: ", e);
        }
    }
}
