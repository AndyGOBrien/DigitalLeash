package fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

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
    private String mJsonString;
    private TextView mTextView;
    
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
        mCardView = mCardPagerAdapter.getCardViewAt(CardValues.SIGN_UP_CARD);
        mTextView = (TextView) mCardView.findViewById(R.id.intro_text);

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

        setListeners();

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
                String UrlStr = "https://turntotech.firebaseio.com/digitalleash/"+mUserName.getText().toString()+".json";
                JSONObject jsonObj = new JSONObject();
                try{
                    jsonObj.put("latitude", mLatitude.getText().toString());
                    jsonObj.put("longitude", mLongitude.getText().toString());
                    jsonObj.put("radius", mRadius.getText().toString());
                }catch (JSONException e){e.printStackTrace();}
                finally {
                    String json = jsonObj.toString();
                    new GetSetData().execute(new String[] {UrlStr,json});
                }
            }
        });

    }

    public class GetSetData extends AsyncTask<String, String, String> {

        HttpURLConnection httpCon;
        String conStatus;

        @Override
        protected String doInBackground(String... params) {

            StringBuilder result = new StringBuilder();

            try {
                URL url = new URL(params[0]);
                httpCon = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(httpCon.getInputStream());

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

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

            }catch( Exception e) {
                e.printStackTrace();
            }
            finally {
                httpCon.disconnect();
            }

            return result.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            processJsonInfo(result);
        }

    }

    private void processJsonInfo(String jsonString) {
        mJsonString = jsonString;
        if(mJsonString == null){
            Toast.makeText(getContext(), "User does not exist", Toast.LENGTH_SHORT).show();
        }
        else{
            try{

                JSONObject info = new JSONObject(jsonString);

                String JsonUserName = info.getString("username");
                String JsonRadius = info.getString("radius");

                if(identityConfirmed(JsonUserName, info.getString("password"))){
                    setUserInformation(JsonUserName, JsonRadius);
                    hideViews();
                    mTextView.setText("Your account has been verified\n\nPlease Continue...");
                    mEditor.putInt("introComplete", 1);
                    mEditor.commit();
                }

            }catch(Exception e){e.printStackTrace();}
        }
    }
    
    private boolean identityConfirmed(String username, String password){
        
        if(password.equals(mPassword.getText().toString()) &&
                username.toLowerCase().equals(
                        mUserName.getText().toString().toLowerCase())){
            return true;
        }
        Toast.makeText(getContext(), "Username or Password Incorrect", Toast.LENGTH_SHORT).show();
        return false;
    }

    private void setUserInformation(String username, String radius){
        mEditor.putString("username", username);
        mEditor.putString("radius", radius);
        mEditor.commit();
    }

    private void hideViews(){
        mUserName.setVisibility(View.GONE);
        mPassword.setVisibility(View.GONE);
        mRadius.setVisibility(View.GONE);
        mSetInfoButton.setVisibility(View.GONE);

        mView.findViewById(R.id.textView).setVisibility(View.GONE);
        mView.findViewById(R.id.textView2).setVisibility(View.GONE);
        mView.findViewById(R.id.textView3).setVisibility(View.GONE);
    }
}
