package turni.app.it.turni.view_controller;

import android.app.ActivityOptions;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
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

public class  MainFragment extends Fragment implements View.OnClickListener {


    private static final boolean DEBUG = true;
    private static final String TAG = "MAINFRAGMENT";
    private static final String TURN_TEXT = "LAUNCH_WORKINGACTIVITY";
    private static final String TAG_FOWARD_BUTTON = "foward button";
    private static final String TAG_ACCOUNT_BUTTON = "calendar button";
    private static final int CALENDAR_DIALOG_ACTIVITY_RESULT_CODE = 1;
    private static final int COLOR_DIALOG_ACTIVITY_RESULT_CODE = 2;
    private static final String RESULT_COLOR_SELECTED = "result color selected";
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
    private static final String TAG_VERONA_COLOR_BUTTON = "tag color button";
    private static final String TAG_BASSONA_COLOR_BUTTON = "tag bassona color button";
    private static final String COLOR_SELECTOR_BUNDLE = "color selector bundle";
    private static final String BASSONA_COLOR_DEFAULT = "bassona color default";
    private static final String VERONA_COLOR_DEFAULT = "result color selected";

    private View mView;
    private FloatingActionButton mFowardButton;
    private EditText mEditText;
    private String mText;
    private TextView mTextView;
    private Button mAccountButton;
    private SharedPreferences mSharedPref;
    private Button mVeronaColorButton;
    private Button mBassonaColorButton;
    private boolean openVerona=false;

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
        mVeronaColorButton = (Button) mView.findViewById(R.id.verona_color_button);
        mBassonaColorButton = (Button) mView.findViewById(R.id.bassona_color_button);

        mFowardButton.setTag(TAG_FOWARD_BUTTON);
        mAccountButton.setTag(TAG_ACCOUNT_BUTTON);
        mVeronaColorButton.setTag((TAG_VERONA_COLOR_BUTTON));
        mBassonaColorButton.setTag((TAG_BASSONA_COLOR_BUTTON));

        int drawableColor=ColorSelectorDialog.getColorDrawable(mSharedPref.getInt(VERONA_COLOR_DEFAULT, 1));
        Drawable d = getResources().getDrawable(drawableColor);
        mVeronaColorButton.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);
        drawableColor=ColorSelectorDialog.getColorDrawable(mSharedPref.getInt(BASSONA_COLOR_DEFAULT, 1));
        d = getResources().getDrawable(drawableColor);
        mBassonaColorButton.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);


        mFowardButton.setOnClickListener(this);
        mAccountButton.setOnClickListener(this);
        mVeronaColorButton.setOnClickListener(this);
        mBassonaColorButton.setOnClickListener(this);

        String calendarName, accountName = null;
        calendarName = mSharedPref.getString(SP_CALENDAR_USED, null);
        accountName = mSharedPref.getString(SP_ACCOUNT_USED, null);
        //If there is a saved calendar and this calendar still exists, set the calendar name into the button.
        if (calendarName != null && accountName != null && Util.getCalendarID(getActivity(), calendarName, accountName) >= 0)
            mAccountButton.setText(calendarName + "  (" + accountName + ")");

        String text = "2015-11-23 XL90355Bonuzzi N.LD1-VR107.00-14.12\n" +
                "2015-11-24 XL90355Bonuzzi N.LD1-VR207.00-14.12";
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
            getActivity().getWindow().setEnterTransition(TransitionInflater.from(getActivity()).inflateTransition(R.transition.enter_ma_da));
            //         getActivity().getWindow().setSharedElementEnterTransition(TransitionInflater.from(getActivity())
            //               .inflateTransition(R.transition.circular_reveal_shared_transition));
            startActivityForResult(intent, CALENDAR_DIALOG_ACTIVITY_RESULT_CODE,
                    ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
            mAccountButton.animate().alpha(0).setDuration(250);
        }
        if (TAG_VERONA_COLOR_BUTTON.equals(tag) || TAG_BASSONA_COLOR_BUTTON.equals(tag)) {
            Intent intent = new Intent(getActivity(), ColorSelectorDialog.class);
            if (TAG_VERONA_COLOR_BUTTON.equals(tag)) {
                intent.putExtra(COLOR_SELECTOR_BUNDLE, TAG_VERONA_COLOR_BUTTON);
                openVerona = true;
            }
            if (TAG_BASSONA_COLOR_BUTTON.equals(tag)) {
                intent.putExtra(COLOR_SELECTOR_BUNDLE, TAG_BASSONA_COLOR_BUTTON);
                openVerona = false;
            }
            v.setTransitionName("snapshot");
            getActivity().getWindow().setExitTransition(null);
            getActivity().getWindow().setEnterTransition(TransitionInflater.from(getActivity()).inflateTransition(R.transition.enter_ma_da));
            //         getActivity().getWindow().setSharedElementEnterTransition(TransitionInflater.from(getActivity())
            //               .inflateTransition(R.transition.circular_reveal_shared_transition));
            startActivityForResult(intent, COLOR_DIALOG_ACTIVITY_RESULT_CODE,
                    ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
            if (TAG_VERONA_COLOR_BUTTON.equals(tag))
                mVeronaColorButton.animate().alpha(0).setDuration(250);
            if (TAG_BASSONA_COLOR_BUTTON.equals(tag))
                mBassonaColorButton.animate().alpha(0).setDuration(250);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        int req = requestCode;
        switch (requestCode) {
            case (CALENDAR_DIALOG_ACTIVITY_RESULT_CODE):
                if (resultCode == CODE_OK) {
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

                break;
            case (COLOR_DIALOG_ACTIVITY_RESULT_CODE):
                if (resultCode == CODE_OK) {
                    int colorSelected = 0;
                    if (TAG_VERONA_COLOR_BUTTON.equals(data.getStringExtra(COLOR_SELECTOR_BUNDLE))) {
                        colorSelected = mSharedPref.getInt(VERONA_COLOR_DEFAULT, 0);
                        int colorDrawable = ColorSelectorDialog.getColorDrawable(colorSelected);
                        Drawable d = getActivity().getResources().getDrawable(colorDrawable);
                        mVeronaColorButton.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);
                        mVeronaColorButton.animate().alpha(1f).setDuration(250);
                    } else {
                        colorSelected = mSharedPref.getInt(BASSONA_COLOR_DEFAULT, 0);
                        int colorDrawable = ColorSelectorDialog.getColorDrawable(colorSelected);
                        Drawable d = getActivity().getResources().getDrawable(colorDrawable);
                        mBassonaColorButton.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);
                        mBassonaColorButton.animate().alpha(1f).setDuration(250);
                    }
                    break;
                }
                else {
                    mVeronaColorButton.animate().alpha(1f).setDuration(250);
                    mBassonaColorButton.animate().alpha(1f).setDuration(250);
                }
                super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
