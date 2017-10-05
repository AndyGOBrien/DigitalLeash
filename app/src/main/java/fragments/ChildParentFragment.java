package fragments;

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

public class ChildParentFragment extends Fragment {

    private Button mChildButton, mParentButton;
    private View mView;
    private SharedPreferences mSettings;
    private SharedPreferences.Editor mEditor;
    private CardPagerAdapter mCardPagerAdapter;
    private CardView mCardView;

    public ChildParentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCardPagerAdapter = CardPagerAdapter.getInstance();
        mCardView = mCardPagerAdapter.getCardViewAt(CardValues.CHILD_PARENT_CARD);
        mSettings = getContext().getSharedPreferences("MySettingsFile", MODE_PRIVATE);
        mEditor = mSettings.edit();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mView = inflater.inflate(R.layout.fragment_child_parent, container, false);
        mChildButton = (Button) mView.findViewById(R.id.child_button);
        mParentButton = (Button) mView.findViewById(R.id.parent_button);
        setListeners();

        return mView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void setListeners(){
        mChildButton.setOnClickListener(new View.OnClickListener() {
            TextView textView;

            @Override
            public void onClick(View v) {
                mEditor.putInt("isParent", 0);
                mEditor.commit();

                textView = (TextView) mCardView.findViewById(R.id.intro_text);
                textView.setText("This device has been set as a Child Device\n\nPlease continue...");

                setButtonsInvisible();
            }
        });

        mParentButton.setOnClickListener(new View.OnClickListener() {
            TextView textView;

            @Override
            public void onClick(View v) {
                mEditor.putInt("isParent", 1);
                mEditor.commit();

                textView = (TextView) mCardView.findViewById(R.id.intro_text);
                textView.setText("This device has been set as a Parent Device\n\nPlease continue...");

                setButtonsInvisible();
            }
        });
    }

    private void setButtonsInvisible(){
        mChildButton.setClickable(false);
        mParentButton.setClickable(false);
        mChildButton.setVisibility(View.INVISIBLE);
        mParentButton.setVisibility(View.INVISIBLE);
    }

}
