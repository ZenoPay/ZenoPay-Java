### ZenoPay Integration with Java Android Studio

#### Overview

This documentation provides a guide for integrating ZenoPay into your Java Android application using `HttpURLConnection` for making HTTP requests. ZenoPay is a payment gateway that allows you to create and manage orders through its API.

#### Prerequisites

1. **Android Studio**: Ensure you have Android Studio installed and set up for your project.
2. **Internet Permission**: Your `AndroidManifest.xml` file must include the INTERNET permission.

#### Step-by-Step Integration

##### 1. Setup Project

1. **Create a New Project**: Open Android Studio and create a new project with an appropriate name and settings.
2. **Add Permissions**: Open `AndroidManifest.xml` and add the following permission to enable network access:

    ```xml
    <uses-permission android:name="android.permission.INTERNET" />
    ```

##### 2. Create Network Tasks

1. **Create a New AsyncTask for Order Creation**: In your project, create a new Java class to handle the order creation network operation.

    ```java
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
                URL url = new URL(API_URL + "/create-order");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                String postData = "buyer_email=" + BUYER_EMAIL + "&buyer_name=" + BUYER_NAME +
                        "&buyer_phone=" + BUYER_PHONE + "&amount=" + AMOUNT +
                        "&account_id=" + ACCOUNT_ID + "&api_key=" + API_KEY +
                        "&secret_key=" + SECRET_KEY;

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
    ```

2. **Create a New AsyncTask for Checking Order Status**: Create another Java class to handle checking the order status.

    ```java
    import android.os.AsyncTask;
    import java.io.BufferedReader;
    import java.io.InputStreamReader;
    import java.io.OutputStream;
    import java.net.HttpURLConnection;
    import java.net.URL;

    public class CheckOrderStatusTask extends AsyncTask<String, Void, String> {

        private static final String API_URL = "https://api.zeno.africa";
        private static final String API_KEY = "YOUR_API_KEY";
        private static final String SECRET_KEY = "YOUR_SECRET_KEY";

        @Override
        protected String doInBackground(String... params) {
            String orderId = params[0];
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(API_URL + "/order-status");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                String postData = "check_status=1&order_id=" + orderId +
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
    ```

3. **Run the AsyncTasks**: To execute the `CreateOrderTask` and `CheckOrderStatusTask`, call them from your activity or fragment:

    ```java
    // Create a new order
    new CreateOrderTask().execute();

    // Check order status (replace "YOUR_ORDER_ID" with the actual order ID)
    new CheckOrderStatusTask().execute("YOUR_ORDER_ID");
    ```

##### 3. Handle API Response

- **Success Response**: The API response will be handled in the `onPostExecute` methods of both tasks. You can process the response data as needed, such as updating the UI or storing the information.

- **Error Handling**: In case of an error, it will be printed to the console. For production applications, consider logging errors to a file or displaying them to the user.

##### 4. Secure Sensitive Data

- **Avoid Hardcoding**: For security reasons, avoid hardcoding sensitive data (like API keys) directly in your source code. Use secure methods to store and retrieve these credentials, such as using Android's `SharedPreferences` with encryption or other secure storage solutions.

##### 5. Test Your Integration

- **Run Tests**: Ensure to thoroughly test your integration on different devices and scenarios to verify that the order creation and status checking processes work as expected and that error handling is robust.

This documentation should provide a comprehensive guide for integrating ZenoPay into your Android application. Make sure to replace placeholder values with your actual credentials and handle the responses appropriately in your application.