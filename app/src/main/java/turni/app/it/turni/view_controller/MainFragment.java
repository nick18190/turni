package turni.app.it.turni.view_controller;

import android.app.ActivityOptions;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;

import model.Util;
import turni.app.it.turni.R;

public class MainFragment extends Fragment implements View.OnClickListener {
    private static final boolean DEBUG = true;
    private static final String TAG = "MAINFRAGMENT";
    private static final String TURN_TEXT = "LAUNCH_WORKINGACTIVITY";
    private static final String TAG_FOWARD_BUTTON = "foward button";
    private static final String TAG_ACCOUNT_BUTTON = "calendar button";
    private static final int DIALOG_ACTIVITY_RESULT_CODE = 1;
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
    private static final String SP_CALENDAR_USED = "calendar used";
    private static final String SP_ACCOUNT_USED = "account used";
    private View mView;
    private FloatingActionButton mFowardButton;
    private EditText mEditText;
    private String mText;
    private TextView mTextView;
    private Button mAccountButton;
    private SharedPreferences mSharedPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharedPref = getActivity().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_main, null, false);
        Toolbar toolbar = (Toolbar) mView.findViewById(R.id.my_awesome_toolbar);
        ((ActionBarActivity) getActivity()).setSupportActionBar(toolbar);
        mFowardButton = (FloatingActionButton) mView.findViewById(R.id.foward_button);
        mEditText = (EditText) mView.findViewById(R.id.edit_text);
        mAccountButton = (Button) mView.findViewById(R.id.account_button);

        mFowardButton.setTag(TAG_FOWARD_BUTTON);
        mAccountButton.setTag(TAG_ACCOUNT_BUTTON);

        mFowardButton.setOnClickListener(this);
        mAccountButton.setOnClickListener(this);

        String calendarName, accountName = null;
        calendarName = mSharedPref.getString(SP_CALENDAR_USED, null);
        accountName = mSharedPref.getString(SP_ACCOUNT_USED, null);
        //If there is a saved calendar and this calendar still exists, set the calendar name into the button.
        if (calendarName != null && accountName != null && Util.getCalendarID(getActivity(), calendarName, accountName) >= 0)
            mAccountButton.setText(calendarName + "  (" + accountName + ")");

        String text = "2015-03-31 XL90355Bonuzzi N.LD1-VR107.00-14.12\n" +
                "2015-04-01 XL90355Bonuzzi N.LN1-VR221.15-04.27\n";
        //         "2015-04-07 XL90355Bonuzzi N.RECRecupero";
//        String text="";
        mEditText.setText(text);

        return mView;
    }


    @Override
    public void onClick(View v) {
        String tag = (String) v.getTag();
        if (TAG_FOWARD_BUTTON.equals(tag)) {
            String text = mEditText.getText().toString();
            Intent intent = new Intent(getActivity(), WorkingActivity.class);
            intent.putExtra(TURN_TEXT, text);
            getActivity().getWindow().setExitTransition(null);
            getActivity().getWindow().setEnterTransition(TransitionInflater.from(getActivity()).inflateTransition(R.transition.enter_ma_dwa));
            startActivity(intent,
                    ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
        }
        if (TAG_ACCOUNT_BUTTON.equals(tag)) {
            Intent intent = new Intent(getActivity(), CalendarDialog.class);
            v.setTransitionName("snapshot");
            getActivity().getWindow().setExitTransition(null);
            getActivity().getWindow().setReenterTransition(null);
            getActivity().getWindow().setSharedElementEnterTransition(TransitionInflater.from(getActivity())
                    .inflateTransition(R.transition.circular_reveal_shared_transition));
            startActivityForResult(intent, DIALOG_ACTIVITY_RESULT_CODE,
                    ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
            mAccountButton.animate().alpha(0).setDuration(250);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DIALOG_ACTIVITY_RESULT_CODE && resultCode == CODE_OK) {
            final Intent intent = data;
            String calendarName = data.getStringExtra(RESULT_CALENDAR);
            String accountName = data.getStringExtra(RESULT_ACCOUNT);
            // calendar_name  (account_name)
            mAccountButton.setText(calendarName + "  (" + accountName + ")");
            mAccountButton.animate().alpha(1f).setDuration(250);
            SharedPreferences.Editor edit = mSharedPref.edit();
            edit.putString(SP_CALENDAR_USED, calendarName);
            edit.putString(SP_ACCOUNT_USED, accountName);
            edit.commit();
        } else
            mAccountButton.animate().alpha(1f).setDuration(250);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
