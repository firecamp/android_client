package firecamp.android_client;

import android.content.Intent;
import android.os.Bundle;

import com.facebook.Session;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;

abstract class FragmentActivity extends android.support.v4.app.FragmentActivity {

    private UiLifecycleHelper uiLifecycleHelper;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiLifecycleHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        if (uiLifecycleHelper == null) {
            uiLifecycleHelper = new UiLifecycleHelper(this, new StatusCallback() {
                @Override
                public void call(Session session, SessionState state, Exception exception) {
                    Application application = (Application) getApplication();
                    application.onFacebookSessionStateChange(session, state, exception);
                }
            });
        }
        uiLifecycleHelper.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        uiLifecycleHelper.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        uiLifecycleHelper.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        uiLifecycleHelper.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiLifecycleHelper.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        super.onStop();
        uiLifecycleHelper.onStop();
    }

}
