package layout;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.llamalabb.digitalleash.CardPagerAdapter;
import com.llamalabb.digitalleash.CardValues;
import com.llamalabb.digitalleash.MyLocationManager;
import com.llamalabb.digitalleash.R;

import static android.content.Context.MODE_PRIVATE;

public class ParentSignInFragment extends Fragment {

    private View mView;
    private Button mSetInfoButton;
    private EditText mUserName, mRadius, mPassword;
    private TextView mLongitude, mLatitude;
    private SharedPreferences mSettings;
    private SharedPreferences.Editor mEditor;
    private CardPagerAdapter mCardPagerAdapter;
    private CardView mCardView;
    private MyLocationManager mMyLocationManager;


    public ParentSignInFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCardPagerAdapter = CardPagerAdapter.getInstance();
        mCardView = mCardPagerAdapter.getCardViewAt(CardValues.SIGN_UP_CARD);
        mSettings = getContext().getSharedPreferences("MySettingsFile", MODE_PRIVATE);
        mEditor = mSettings.edit();
        mMyLocationManager = MyLocationManager.getInstance(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_parent_sign_in, container, false);
        mSetInfoButton = (Button) mView.findViewById(R.id.set_info_button);
        mUserName = (EditText) mView.findViewById(R.id.username_editText);
        mRadius = (EditText) mView.findViewById(R.id.radius_editText);
        mPassword = (EditText) mView.findViewById(R.id.password_editText);
        mLongitude = (TextView) mView.findViewById(R.id.longitude_text);
        mLatitude = (TextView) mView.findViewById(R.id.latitude_text);

        mLongitude.setText("" + mMyLocationManager.getLongitude());
        mLatitude.setText("" + mMyLocationManager.getLatitude());

        return mView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void setListeners(){
        mSetInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
