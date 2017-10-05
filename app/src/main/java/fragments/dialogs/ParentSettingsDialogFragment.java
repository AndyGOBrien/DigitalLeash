package fragments.dialogs;

import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.llamalabb.digitalleash.R;


public class ParentSettingsDialogFragment extends DialogFragment {



    private OnFragmentInteractionListener mListener;
    private View mView;
    private Button mChangeParentButton;
    private ViewGroup mContainer;
    private LayoutInflater mInflater;

    public ParentSettingsDialogFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ParentSettingsDialogFragment newInstance(String title) {
        ParentSettingsDialogFragment fragment = new ParentSettingsDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mView = inflater.inflate(R.layout.fragment_parent_settings_dialog, container, false);
        mChangeParentButton = (Button) mView.findViewById(R.id.switch_parent);

        setListeners();

        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onResume() {
        // Store access variables for window and blank point
        Window window = getDialog().getWindow();
        Point size = new Point();
        // Store dimensions of the screen in `size`
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        // Set the width of the dialog proportional to 75% of the screen width
        window.setLayout((int) (size.x * 0.75), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        // Call super onResume after sizing
        super.onResume();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void setListeners(){
        mChangeParentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }
}
