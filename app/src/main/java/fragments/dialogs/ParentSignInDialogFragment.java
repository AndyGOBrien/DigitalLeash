package fragments.dialogs;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

/**
 * Created by andy on 10/5/17.
 */

public class ParentSignInDialogFragment extends DialogFragment {

    private View mView;
    private Button mSetInfoButton;
    private EditText mUserName, mRadius, mPassword;
    private TextView mLongitude, mLatitude;
    private SharedPreferences mSettings;
    private SharedPreferences.Editor mEditor;
    private MyLocationManager mMyLocationManager;
    private String mJsonString;

    public ParentSignInDialogFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ParentSignInDialogFragment newInstance(String title) {
        ParentSignInDialogFragment fragment = new ParentSignInDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mSettings = getContext().getSharedPreferences("MySettingsFile", MODE_PRIVATE);
        mEditor = mSettings.edit();
        mMyLocationManager = MyLocationManager.getInstance(getContext());

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
                    dismiss();
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

}
