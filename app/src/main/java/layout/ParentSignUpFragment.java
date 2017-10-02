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

import com.llamalabb.digitalleash.CardPagerAdapter;
import com.llamalabb.digitalleash.CardValues;
import com.llamalabb.digitalleash.R;

import static android.content.Context.MODE_PRIVATE;

public class ParentSignUpFragment extends Fragment {

    private View mView;
    private Button mSetInfoButton;
    private EditText mUserName, mRadius, mLongitude, mLatitude;
    private SharedPreferences mSettings;
    private SharedPreferences.Editor mEditor;
    private int mIsPreviousUser;
    private CardPagerAdapter mCardPagerAdapter;
    private CardView mCardView;


    public ParentSignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCardPagerAdapter = CardPagerAdapter.getInstance();
        mCardView = mCardPagerAdapter.getCardViewAt(CardValues.SIGN_UP_CARD);
        mSettings = getContext().getSharedPreferences("MySettingsFile", MODE_PRIVATE);
        mEditor = mSettings.edit();
        mIsPreviousUser = mSettings.getInt("isPreviousUser", -1);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_parent_sign_up, container, false);
        mSetInfoButton = (Button) mView.findViewById(R.id.set_info_button);
        mUserName = (EditText) mView.findViewById(R.id.username_editText);
        mRadius = (EditText) mView.findViewById(R.id.radius_editText);
        mLongitude = (EditText) mView.findViewById(R.id.longitude_editText);
        mLatitude = (EditText) mView.findViewById(R.id.latitude_editText);

        if(mIsPreviousUser == 1)
            mSetInfoButton.setText("Set Info");
        else if(mIsPreviousUser == 0)
            mSetInfoButton.setText("Create");
        else
            mSetInfoButton.setVisibility(View.INVISIBLE);

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


}
