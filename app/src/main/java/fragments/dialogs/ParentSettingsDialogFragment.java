package fragments.dialogs;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;


import com.llamalabb.digitalleash.R;


public class ParentSettingsDialogFragment extends DialogFragment{



    private View mView;
    private Button mChangeParentButton;
    private Switch mSpecifyLocationSwitch;
    private EditText mLatitudeEditText;
    private EditText mLongitudeEditText;
    private ParentSettingsDialogListener mActionListener;

    public ParentSettingsDialogFragment() {
        // Required empty public constructor
    }

    public interface ParentSettingsDialogListener{
        void onParentChangeButtonClicked(boolean bool);
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
        mSpecifyLocationSwitch = (Switch) mView.findViewById(R.id.specify_location_switch);
        mLatitudeEditText = (EditText) mView.findViewById(R.id.latitude_editText);
        mLongitudeEditText = (EditText) mView.findViewById(R.id.longitude_editText);

        setListeners();

        return mView;
    }


    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        mActionListener = (ParentSettingsDialogListener) context;

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

    private void setListeners(){

        mChangeParentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActionListener.onParentChangeButtonClicked(true);
                dismiss();
            }
        });

        mSpecifyLocationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mLatitudeEditText.setVisibility(View.VISIBLE);
                    mLongitudeEditText.setVisibility(View.VISIBLE);
                }
                else{
                    mLatitudeEditText.setVisibility(View.INVISIBLE);
                    mLongitudeEditText.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

}
