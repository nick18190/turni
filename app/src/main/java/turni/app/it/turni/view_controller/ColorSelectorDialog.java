package turni.app.it.turni.view_controller;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;

import turni.app.it.turni.R;

public class ColorSelectorDialog extends ActionBarActivity {

    public static final int COLOR_1 = 1;
    public static final int COLOR_2 = 2;
    public static final int COLOR_3 = 3;
    public static final int COLOR_4 = 4;
    public static final int COLOR_5 = 5;
    public static final int COLOR_6 = 6;
    public static final int COLOR_7 = 7;
    public static final int COLOR_8 = 8;
    public static final int COLOR_9 = 9;
    public static final int COLOR_10 = 10;
    public static final int COLOR_11 = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_selector_dialog);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_color_selector_dialog, menu);
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
    public static class PlaceholderFragment extends Fragment implements View.OnClickListener {

        private Button mColorView1;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_color_selector_dialog, container, false);
            getActivity().getWindow().setEnterTransition(TransitionInflater.from(getActivity()).inflateTransition(R.transition.enter_ma_da));
            mColorView1 = (Button) rootView.findViewById(R.id.row_color_1);

            mColorView1.setTag(COLOR_1);

            mColorView1.setOnClickListener(this);

            return rootView;
        }

        @Override
        public void onClick(View view) {
            ((Button) view).setCompoundDrawables(getActivity().getResources().getDrawable((int) view.getTag()),null,null,null);

            }
        }
    }

    public static int getColorDrawable(int colorString) {
        switch (colorString) {
            case COLOR_1:
                return R.drawable.blu;
            case COLOR_2:
                return R.drawable.blu;
            case COLOR_3:
                return R.drawable.blu;
            case COLOR_4:
                return R.drawable.blu;
            case COLOR_5:
                return R.drawable.blu;
            case COLOR_6:
                return R.drawable.blu;
            case COLOR_7:
                return R.drawable.blu;
            case COLOR_8:
                return R.drawable.blu;
            case COLOR_9:
                return R.drawable.blu;
            case COLOR_10:
                return R.drawable.blu;
            case COLOR_11:
                return R.drawable.blu;
        }
        return 0;
    }


}
