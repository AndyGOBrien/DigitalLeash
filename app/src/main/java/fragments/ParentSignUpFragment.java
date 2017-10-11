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

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.Context.MODE_PRIVATE;

public class ParentSignUpFragment extends Fragment {

    private View mView;
    private Button mSetInfoButton;
    private EditText mUserName, mRadius, mPassword, mConfirmPW;
    private TextView mLongitude, mLatitude;
    private SharedPreferences mSettings;
    private SharedPreferences.Editor mEditor;
    private CardPagerAdapter mCardPagerAdapter;
    private CardView mCardView;
    private TextView mTextView;
    private MyLocationManager mMyLocationManager;
    private ParentSignUpListener mActionListener;




    public ParentSignUpFragment() {
        // Required empty public constructor
    }

    public interface ParentSignUpListener{
        void parentSignUpSuccess();
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

        mView = inflater.inflate(R.layout.fragment_parent_sign_up, container, false);
        mSetInfoButton = (Button) mView.findViewById(R.id.set_info_button);
        mUserName = (EditText) mView.findViewById(R.id.username_editText);
        mRadius = (EditText) mView.findViewById(R.id.radius_editText);
        mPassword = (EditText) mView.findViewById(R.id.password_editText);
        mConfirmPW = (EditText) mView.findViewById(R.id.confirm_editText);
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
        mActionListener = (ParentSignUpListener) context;

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void setListeners(){
        mSetInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mPassword.getText().toString().equals(mConfirmPW.getText().toString())
                        && mUserName.getText().toString().length() >= 3
                        && mPassword.getText().toString().length() >= 6
                        && !mRadius.getText().toString().isEmpty()) {

                    String UrlStr = "https://turntotech.firebaseio.com/digitalleash/"+mUserName.getText().toString()+".json";
                    JSONObject jsonObject= new JSONObject();
                    try {
                        jsonObject.put("username", mUserName.getText().toString().toLowerCase());
                        jsonObject.put("password", mPassword.getText().toString());
                        jsonObject.put("latitude", mLatitude.getText().toString());
                        jsonObject.put("longitude", mLongitude.getText().toString());
                        jsonObject.put("radius", mRadius.getText().toString());
                        jsonObject.put("child_latitude", "Not Set");
                        jsonObject.put("child_longitude", "Not Set");
                        mEditor.putString(getString(R.string.username), mUserName.getText().toString());
                        mEditor.putString(getString(R.string.radius), mRadius.getText().toString());
                        mEditor.commit();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }finally {
                        String json = jsonObject.toString();
                        new SendData().execute(new String[]{UrlStr,json});
                    }

                }

                else if(mUserName.getText().toString().length() < 3)
                    Toast.makeText(getContext(), "The User Name must be at least 3 characters", Toast.LENGTH_SHORT).show();
                else if(mPassword.getText().toString().length() < 6)
                    Toast.makeText(getContext(), "The password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                else if(!mPassword.getText().toString().equals(mConfirmPW.getText().toString()))
                    Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void hideViews(){
        mUserName.setVisibility(View.GONE);
        mPassword.setVisibility(View.GONE);
        mConfirmPW.setVisibility(View.GONE);
        mRadius.setVisibility(View.GONE);
        mSetInfoButton.setVisibility(View.GONE);

        mView.findViewById(R.id.textView).setVisibility(View.GONE);
        mView.findViewById(R.id.textView2).setVisibility(View.GONE);
        mView.findViewById(R.id.textView3).setVisibility(View.GONE);
        mView.findViewById(R.id.textView4).setVisibility(View.GONE);
    }


    public class SendData extends AsyncTask<String, String, String>{

        HttpURLConnection httpCon;
        String conStatus;

        @Override
        protected String doInBackground(String... params){
            try {
                URL url = new URL(params[0]);
                httpCon = (HttpURLConnection) url.openConnection();

                httpCon.setRequestMethod("PUT");
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

            }catch(Exception e){
                e.printStackTrace();
            }finally {
                httpCon.disconnect();
            }
            return conStatus;
        }

        @Override
        protected void onPostExecute(String result){

            if(Integer.parseInt(conStatus) >= 200 && Integer.parseInt(conStatus) < 300){
                hideViews();
                mTextView.setText("Your account has been created...\n\n Please Continue...");
                mEditor.putInt("introComplete", 1);
                mEditor.commit();
                mActionListener.parentSignUpSuccess();
            }
            else{
                Toast.makeText(getContext(), "ERROR: " + conStatus, Toast.LENGTH_SHORT).show();
            }
        }

    }

}
