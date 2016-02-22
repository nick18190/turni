package turni.app.it.turni.view_controller;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import turni.app.it.turni.R;

public class  WorkingActivity extends Activity {

    private static final String LAUNCH_ACTIVITY = "LAUNCH_WORKINGACTIVITY";
    private static final boolean DEBUG = true;
    private static final String TAG = "WORKING ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_working);
        String text = getIntent().getStringExtra(LAUNCH_ACTIVITY);
        postponeEnterTransition();
        // Add the fragment to the activity, pushing this transaction
        // on to the back stack.
        Fragment newFragment = WorkingFragment.newInstance(text);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.working_activity_fragment, newFragment);
        ft.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_working, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
