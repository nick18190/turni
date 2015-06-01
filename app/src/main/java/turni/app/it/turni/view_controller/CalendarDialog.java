package turni.app.it.turni.view_controller;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import model.Util;
import turni.app.it.turni.R;

public class CalendarDialog extends Activity {

    private static final boolean DEBUG = true;
    private static final String TAG = "CALENDAR DIALOG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_calendar_dialog);

        PlaceholderFragment newFragment = new PlaceholderFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.dialog_container, newFragment);
        ft.commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calendar_dialog, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends android.app.Fragment implements View.OnClickListener {

        private static final String BACKGROUND = "background";
        private static final String CALENDAR_ROW = "calendar row";
        /**
         * Activity result intent Key
         */
        private static final String RESULT_CALENDAR = "result calendar";
        /**
         * Activity resul code Ok
         */
        private static final int CODE_OK = 1;
        /**
         * Activity resul code not Ok
         */
        private static final int CODE_NOT_OK = 0;
        /**
         * Activity result intent key
         */
        private static final String RESULT_ACCOUNT = "result account";
        private ArrayList<String> mAccountsList;
        private LinearLayout mLinearLayout;
        private RelativeLayout mBackgroundView;
        private ArrayList<String> mCalendarList;

        public PlaceholderFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_calendar_dialog, container, false);
            mLinearLayout = (LinearLayout) rootView.findViewById(R.id.calendar_dialog_llayout);
            mBackgroundView = (RelativeLayout) rootView.findViewById(R.id.background_view);

            mBackgroundView.setTag(BACKGROUND);

            mBackgroundView.setOnClickListener(this);
            getActivity().getWindow().setEnterTransition(TransitionInflater.from(getActivity()).inflateTransition(R.transition.enter_ma_da));

            int eventIndex = 0;
            mAccountsList = Util.getCalendarAccounts(getActivity());
            for (int i = 0; i < mAccountsList.size(); i++) {
                if (DEBUG)
                    Log.d(TAG, "accounts " + mAccountsList.get(i));
                TextView accountLine = (TextView) inflater.inflate(R.layout.dialog_account_text, null);
                String accountName = mAccountsList.get(i);
                accountLine.setText(accountName);
                mLinearLayout.addView(accountLine);
                eventIndex = mLinearLayout.getChildCount();
                mCalendarList = Util.getCalendarNames(getActivity(), accountName);
                for (int j = 0; j < mCalendarList.size(); j++) {
                    Button calendarRow = (Button) inflater.inflate(R.layout.dialog_calendar_text, null);
                    calendarRow.setOnClickListener(this);
                    calendarRow.setTag(CALENDAR_ROW);
                    calendarRow.setId(i);
                    String calendarName = mCalendarList.get(j);
                    if (!calendarName.equalsIgnoreCase(accountName)) {
                        calendarRow.setText(mCalendarList.get(j));
                        mLinearLayout.addView(calendarRow);
                    } else {
                        calendarRow.setText("Eventi");
                        mLinearLayout.addView(calendarRow, eventIndex);
                    }
                }
            }
            return rootView;
        }

        @Override
        public void onClick(View v) {
            String tag = (String) v.getTag();

            if (BACKGROUND.equalsIgnoreCase(tag))
                getActivity().finishAfterTransition();

            if(CALENDAR_ROW.equalsIgnoreCase(tag)){
                Intent resultIntent=new Intent();
                resultIntent.putExtra(RESULT_CALENDAR,((Button) v).getText());
                resultIntent.putExtra(RESULT_ACCOUNT,mAccountsList.get(v.getId()));
                getActivity().setResult(CODE_OK, resultIntent);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getActivity().finishAfterTransition();
                    }
                },250);

            }
        }
    }
}
