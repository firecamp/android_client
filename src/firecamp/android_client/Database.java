package firecamp.android_client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

class Database {

    public static interface QueryCompletedListener {
        void onQueryCompleted(int status, String message, JSONArray data);
    }

    private static final class QueryTask extends AsyncTask<String, Void, String> {

        private final String query;
        private final QueryCompletedListener listener;

        public QueryTask(String query, QueryCompletedListener listener) {
            this.query = query;
            this.listener = listener;
        }

        @Override
        protected String doInBackground(String... params) {
            HttpClient client = new DefaultHttpClient();
            HttpPost request = new HttpPost("http://192.168.0.2/query.php");
            String responseString = null;
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("query", query));
                request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(request);
                InputStream stream = response.getEntity().getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                responseString = stringBuilder.toString();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            int status = 0;
            String message = null;
            JSONArray data = null;

            JSONObject response;
            try {
                response = new JSONObject(result);
                status = response.getInt("status");
                message = response.getString("message");
            } catch (JSONException e) {
                e.printStackTrace();
                return;
            }
            try {
                data = response.getJSONArray("data");
            } catch (JSONException e) {
                // Don't print anything. It's okay for data to be null.
                //e.printStackTrace();
            }
            if (listener != null) {
                listener.onQueryCompleted(status, message, data);
            }
        }

    }

    public static void query(String query, QueryCompletedListener listener) {
        Log.i("Database", "Executing query: " + query);
        new QueryTask(query, listener).execute();
    }

}
