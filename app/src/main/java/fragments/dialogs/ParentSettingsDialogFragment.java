package fragments.dialogs;

import android.content.Context;
import android.content.SharedPreferences;
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

import static android.content.Context.MODE_PRIVATE;


public class ParentSettingsDialogFragment extends DialogFragment{



    private View mView;
    private Button mChangeParentButton, mSaveButton;
    private Switch mSpecifyLocationSwitch;
    private EditText mLatitudeEditText, mLongitudeEditText, mRadiusEditText;
    private ParentSettingsDialogListener mActionListener;
    private SharedPreferences mSettings;
    private SharedPreferences.Editor mEditor;
    private boolean locationSettingChanged;



    public ParentSettingsDialogFragment() {
        // Required empty public constructor
    }

    public interface ParentSettingsDialogListener{
        void onParentChangeButtonClicked();
        void locationSettingChanged();
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
    public void onCreate(Bundle b){
        super.onCreate(b);

        mSettings = getContext().getSharedPreferences("MySettingsFile", MODE_PRIVATE);
        mEditor = mSettings.edit();
        locationSettingChanged = false;

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
        mRadiusEditText = (EditText) mView.findViewById(R.id.radius_editText);
        mSaveButton = (Button) mView.findViewById(R.id.save_button);

        mRadiusEditText.setText(mSettings.getString(getString(R.string.radius),""));

        setSpecifyLocationSwitchChecked();
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
                mActionListener.onParentChangeButtonClicked();
                dismiss();
            }
        });

        mSpecifyLocationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    setEditTextVisible();
                }
                else{
                    setEditTextGone();
                }
            }
        });

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String editedRadius = mRadiusEditText.getText().toString();
                String editedLatitude = mLatitudeEditText.getText().toString();
                String editedLongitude = mLongitudeEditText.getText().toString();
                String cachedRadius = mSettings.getString(getString(R.string.radius),"");
                String cachedLatitude = mSettings.getString(getString(R.string.specified_latitude), "");
                String cachedLongitude = mSettings.getString(getString(R.string.specified_longitude), "");
                int specifyParentLocation = mSettings.getInt(getString(R.string.specify_parent_location), 0);


                if(mSpecifyLocationSwitch.isChecked()){
                    if(specifyParentLocation == 0){
                        locationSettingChanged = true;
                        mEditor.putInt(getString(R.string.specify_parent_location), 1);
                        mEditor.commit();
                    }
                    if(!cachedLatitude.equals(editedLatitude) ||
                            !cachedLongitude.equals(editedLongitude)){
                        locationSettingChanged = true;
                        mEditor.putString(getString(R.string.specified_latitude), editedLatitude);
                        mEditor.putString(getString(R.string.specified_longitude), editedLongitude);
                        mEditor.commit();
                    }
                }
                else{
                    if(specifyParentLocation == 1){
                        locationSettingChanged = true;
                    }
                    mEditor.putInt(getString(R.string.specify_parent_location), 0);
                    mEditor.commit();
                }


                if(!cachedRadius.equals(editedRadius)){
                    mEditor.putString(getString(R.string.radius), editedRadius);
                    mEditor.commit();
                    locationSettingChanged = true;
                }


                if(locationSettingChanged)
                    mActionListener.locationSettingChanged();


                dismiss();
            }
        });
    }

    private void setSpecifyLocationSwitchChecked(){

        if(mSettings.getInt(getString(R.string.specify_parent_location), 0) == 1){
            mSpecifyLocationSwitch.setChecked(true);
            mLatitudeEditText.setVisibility(View.VISIBLE);
            mLongitudeEditText.setVisibility(View.VISIBLE);
            mLatitudeEditText.setText(
                    mSettings.getString(getString(R.string.specified_latitude), "0.0"));
            mLongitudeEditText.setText(
                    mSettings.getString(getString(R.string.specified_longitude), "0.0"));

        }
        else{

        }

    }

    private void setEditTextGone(){
        mLatitudeEditText.setVisibility(View.GONE);
        mLongitudeEditText.setVisibility(View.GONE);
    }

    private void setEditTextVisible(){
        mLatitudeEditText.setVisibility(View.VISIBLE);
        mLongitudeEditText.setVisibility(View.VISIBLE);
    }

}
