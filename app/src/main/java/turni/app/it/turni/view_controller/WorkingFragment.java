package turni.app.it.turni.view_controller;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import turni.app.it.turni.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * <p/>
 * Use the {@link WorkingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorkingFragment extends Fragment {
    private static final boolean DEBUG = true;
    private static final String TAG = "WORKING FRAGMENT";
    private static final String CREATE_EVENTS = "create events";
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    private String mText;
    private View mView;
    private boolean isActivityCalled = false;


    public WorkingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment WorkingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WorkingFragment newInstance(String param1) {
        WorkingFragment fragment = new WorkingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Get the text to elaborate from the activity
        if (getArguments() != null) {
            mText = getArguments().getString(ARG_PARAM1);
            if (mText != null)
                isActivityCalled = true;
        }
        getActivity().getWindow().setEnterTransition(TransitionInflater.from(getActivity()).inflateTransition(R.transition.enter_ma_dwa));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_working, container, false);
        mView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (isActivityCalled) {
                    Intent service = new Intent(getActivity(), WorkingService.class);
                    service.setAction(CREATE_EVENTS);
                    service.putExtra(CREATE_EVENTS, mText);
                    getActivity().startService(service);
                    //Making sure this event won't be called again
                    isActivityCalled = false;
                    mText = null;
                    getActivity().startPostponedEnterTransition();
                }
            }
        });

        return mView;
    }


}