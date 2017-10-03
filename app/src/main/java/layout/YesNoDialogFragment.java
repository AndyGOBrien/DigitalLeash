package layout;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.llamalabb.digitalleash.CardPagerAdapter;
import com.llamalabb.digitalleash.CardValues;
import com.llamalabb.digitalleash.R;

import static android.content.Context.MODE_PRIVATE;

public class YesNoDialogFragment extends Fragment {

    private Button mYesButton, mNoButton;
    private View mView;
    private SharedPreferences mSettings;
    private SharedPreferences.Editor mEditor;
    private CardPagerAdapter mCardPagerAdapter;
    private CardView mCardView;


    public YesNoDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCardPagerAdapter = CardPagerAdapter.getInstance();
        mCardView = mCardPagerAdapter.getCardViewAt(CardValues.YES_NO_CARD);
        mSettings = getContext().getSharedPreferences("MySettingsFile", MODE_PRIVATE);
        mEditor = mSettings.edit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_yes_no_dialog, container, false);
        mYesButton = (Button) mView.findViewById(R.id.yes_button);
        mNoButton = (Button) mView.findViewById(R.id.no_button);
        setListeners();

        return mView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void setListeners(){

        mYesButton.setOnClickListener(new View.OnClickListener() {
            TextView textView;

            @Override
            public void onClick(View v) {
                mEditor.putInt("isPreviousUser", 0);
                mEditor.commit();

                setButtonsInvisible();

                textView = (TextView) mCardView.findViewById(R.id.intro_text);

                textView.setText("You have been set as a new user. \n\nPlease continue...");
            }
        });

        mNoButton.setOnClickListener(new View.OnClickListener(){

            TextView textView;

            @Override
            public void onClick(View v){
                mEditor.putInt("isPreviousUser", 1);
                mEditor.commit();

                setButtonsInvisible();

                textView = (TextView) mCardView.findViewById(R.id.intro_text);

                textView.setText("You have been set as a previous user. \n\nPlease continue...");
            }

        });
    }

    private void setButtonsInvisible(){
        mYesButton.setClickable(false);
        mNoButton.setClickable(false);
        mYesButton.setVisibility(View.INVISIBLE);
        mNoButton.setVisibility(View.INVISIBLE);
    }

}
