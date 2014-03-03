package firecamp.android_client;

import org.json.JSONArray;
import org.json.JSONException;

import android.os.AsyncTask;
import android.util.Log;

import com.facebook.Request;
import com.facebook.Request.GraphUserCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;

import firecamp.android_client.Database.QueryCompletedListener;

public class Application extends android.app.Application {

    private static String TAG = "firecamp.android_client.Application";

    private boolean loggedIn = false;
    private int userId = 0;

    public int getUserId() {
        return userId;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    private void onLoggedIn() {
        Log.i(TAG, "Logged in as FireCamp user #" + userId + ".");
    }

    private void onLoggedOut() {
        Log.i(TAG, "Logged out of FireCamp.");
    }

    void onFacebookSessionStateChange(final Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {
            new AsyncTask<String, Void, String>() {

                private void createUser(final String facebookId) {
                    Database.query("INSERT INTO `users` (`facebook_id`) VALUES ('" + facebookId + "');",
                            new QueryCompletedListener() {

                                @Override
                                public void onQueryCompleted(int status, String message, JSONArray data) {
                                    logInOrCreateUser(facebookId);
                                }

                            });
                }

                @Override
                protected String doInBackground(String... params) {
                    final StringBuilder stringBuilder = new StringBuilder();
                    Request request = Request.newMeRequest(session, new GraphUserCallback() {

                        @Override
                        public void onCompleted(GraphUser user, Response response) {
                            stringBuilder.append(user.getId());
                        }

                    });
                    request.executeAndWait();
                    return stringBuilder.toString();
                }

                private void logInOrCreateUser(final String facebookId) {
                    Log.w(TAG, "trying to log in...");
                    Database.query("SELECT * FROM `users` WHERE `facebook_id` = '" + facebookId + "';",
                            new QueryCompletedListener() {

                                @Override
                                public void onQueryCompleted(int status, String message, JSONArray data) {
                                    if (data != null && data.length() == 1) {
                                        try {
                                            userId = data.getJSONObject(0).getInt("user_id");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        loggedIn = true;
                                        onLoggedIn();
                                    } else {
                                        createUser(facebookId);
                                    }
                                }

                            });
                }

                @Override
                protected void onPostExecute(String result) {
                    logInOrCreateUser(result);
                }

            }.execute();
        } else {
            loggedIn = false;
            userId = 0;
            onLoggedOut();
        }
    }
}
