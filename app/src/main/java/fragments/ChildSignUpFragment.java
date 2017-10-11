package fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.llamalabb.digitalleash.CardPagerAdapter;
import com.llamalabb.digitalleash.CardValues;
import com.llamalabb.digitalleash.MyLocationManager;
import com.llamalabb.digitalleash.R;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.Context.MODE_PRIVATE;

public class ChildSignUpFragment extends Fragment {

    private EditText mParentUserName;
    private TextView mLatitude;
    private TextView mLongitude;
    private View mView;
    private CardPagerAdapter mCardPagerAdapter;
    private CardView mCardView;
    private SharedPreferences mSettings;
    private SharedPreferences.Editor mEditor;
    private MyLocationManager mMyLocationManager;
    private TextView mTextView;
    private Button mSetParentButton;
    private ChildSignInListener mActionListener;


    public ChildSignUpFragment() {
        // Required empty public constructor
    }

    public interface ChildSignInListener{
        void childSignUpSuccess();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCardPagerAdapter = CardPagerAdapter.getInstance();
        mCardView = mCardPagerAdapter.getCardViewAt(CardValues.SIGN_UP_CARD);
        mSettings = getContext().getSharedPreferences("MySettingsFile", MODE_PRIVATE);
        mEditor = mSettings.edit();
        mMyLocationManager = MyLocationManager.getInstance(getContext());
        mCardView = mCardPagerAdapter.getCardViewAt(CardValues.SIGN_UP_CARD);
        mTextView = (TextView) mCardView.findViewById(R.id.intro_text);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_child_sign_up, container, false);
        mParentUserName = (EditText) mView.findViewById(R.id.parent_name_editText);
        mLatitude = (TextView) mView.findViewById(R.id.latitude_text);
        mLongitude = (TextView) mView.findViewById(R.id.longitude_text);
        mSetParentButton = (Button) mView.findViewById(R.id.set_parent_button);

        mLatitude.setText(""+mMyLocationManager.getLatitude());
        mLongitude.setText(""+mMyLocationManager.getLongitude());

        setListeners();


        return mView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActionListener = (ChildSignInListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void setListeners(){
        mSetParentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urlStr = "https://turntotech.firebaseio.com/digitalleash/" + mParentUserName.getText().toString() + ".json";
                JSONObject jsonObject = new JSONObject();

                try{
                    jsonObject.put("child_latitude", mLatitude.getText().toString());
                    jsonObject.put("child_longitude", mLongitude.getText().toString());

                    mEditor.putString(getString(R.string.username),
                            mParentUserName.getText().toString())
                            .commit();

                }catch(Exception e){e.printStackTrace();}
                finally {
                    String string = jsonObject.toString();
                    new SetData().execute(new String[] {urlStr, string});
                }
            }
        });
    }

    public class SetData extends AsyncTask<String, String, String>{

        HttpURLConnection httpCon;
        String conStatus;

        @Override
        protected String doInBackground(String... params) {

            try {
                URL url = new URL(params[0]);

                httpCon = (HttpURLConnection) url.openConnection();
                httpCon.setRequestMethod("PATCH");
                httpCon.setDoOutput(true);

                httpCon.setRequestProperty("Content-Type", "application/json");
                httpCon.setRequestProperty("Accept", "application/json");

                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpCon.getOutputStream());
                outputStreamWriter.write(params[1]);
                outputStreamWriter.flush();
                outputStreamWriter.close();

                conStatus = String.valueOf(httpCon.getResponseCode());
                Log.e("ConnectionStatus", String.valueOf(httpCon.getResponseCode()));
                Log.e("ConnectionStatus", String.valueOf(httpCon.getResponseMessage()));


            }catch(Exception e){e.printStackTrace();}
            finally {
                httpCon.disconnect();
            }

            return conStatus;
        }

        @Override
        protected void onPostExecute(String string){
            if(Integer.parseInt(conStatus) >= 200 && Integer.parseInt(conStatus) < 300){
                hideViews();
                mTextView.setText("The parent has been set\n\n Please Continue...");
                mEditor.putInt(getString(R.string.intro_complete), 1);
                mEditor.commit();
                mActionListener.childSignUpSuccess();
            }
            else{
                Toast.makeText(getContext(), "ERROR: " + conStatus, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void hideViews(){
        mView.setVisibility(View.GONE);
    }
}
