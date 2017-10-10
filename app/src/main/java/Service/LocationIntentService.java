package Service;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
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

import static java.lang.Double.parseDouble;
import static java.lang.System.in;
import static java.security.AccessController.getContext;


/**
 * Created by andy on 10/10/17.
 */

public class LocationIntentService extends IntentService {


    public static final String ACTION_RESP = "com.llamalabb.digitalleash.intent.action.MESSAGE_PROCESSED";

    public static final String CHILD_OR_PARENT = "type";
    public static final String IN_BOUND = "InBound";

    private String mType;

    private SharedPreferences mSettings;
    private MyLocationManager mMyLocationManager;

    private HttpURLConnection mHttpCon;
    private String mConStatus;
    private OutputStreamWriter mOutputStreamWriter;
    private String mParentLatitude;
    private String mParentLongitude;
    private String mChildLatitude;
    private String mChildLongitude;
    private String mRadius;
    private Boolean mIsParent;


    public LocationIntentService() {
        super("LocationIntentService");
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        mSettings = getApplicationContext().getSharedPreferences("MySettingsFile", MODE_PRIVATE);
        mMyLocationManager = MyLocationManager.getInstance(this);
        if(mSettings.getInt(getString(R.string.is_parent), 0) == 1){
            mIsParent = true;
        }
        else
            mIsParent = false;

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        mType = intent.getStringExtra(CHILD_OR_PARENT);

        while(true){
            SystemClock.sleep(10000);
            if(mSettings.getInt(getString(R.string.specify_parent_location), 0) == 1){
                openConnection();
                patchJSON(mSettings.getString(getString(R.string.specified_latitude), ""),
                        mSettings.getString(getString(R.string.specified_longitude), ""),
                        mSettings.getString(getString(R.string.radius),""));
            }
            else{
                openConnection();
                patchJSON(mMyLocationManager.getLatitude() + "",
                        mMyLocationManager.getLongitude() + "",
                        mSettings.getString(getString(R.string.radius),""));
            }

            processJsonInfo(getJSONString());

            Intent broadcastIntent = new Intent();

            broadcastIntent.setAction(ACTION_RESP);

            broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);

            if(isInRadius())
                broadcastIntent.putExtra(IN_BOUND, true);
            else
                broadcastIntent.putExtra(IN_BOUND, false);

            sendBroadcast(broadcastIntent);
        }

        //mHttpCon.disconnect();
    }

    private void openConnection(){
        String urlStr = "https://turntotech.firebaseio.com/digitalleash/"+mSettings.getString(getString(R.string.username), "")+".json";

        try {
            URL url = new URL(urlStr);
            mHttpCon = (HttpURLConnection) url.openConnection();
            mHttpCon.setRequestMethod("PATCH");
            mHttpCon.setDoOutput(true);
            mHttpCon.setRequestProperty("Content-Type", "application/json");
            mHttpCon.setRequestProperty("Accept", "application/json");
            mOutputStreamWriter = new OutputStreamWriter(mHttpCon.getOutputStream());

        }catch( Exception e) {e.printStackTrace();}
    }


    private void patchJSON(String lat, String lon, String radius){

        JSONObject jsonObj= new JSONObject();

        String latJsonKey = mType + "latitude";
        String lonJsonKey = mType + "longitude";

        try{
            jsonObj.put(latJsonKey, lat);
            jsonObj.put(lonJsonKey, lon);
            if(mIsParent)
                jsonObj.put("radius", radius);

        }catch (JSONException e){e.printStackTrace();}
        finally {
            String json = jsonObj.toString();

            try {
                mOutputStreamWriter.write(json);
                mOutputStreamWriter.flush();
                mOutputStreamWriter.close();

                mConStatus = String.valueOf(mHttpCon.getResponseCode());
                Log.e("ConnectionStatus", String.valueOf(mHttpCon.getResponseCode()));
                Log.e("ConnectionStatus", String.valueOf(mHttpCon.getResponseMessage()));

            }catch( Exception e) {
                e.printStackTrace();
            }
        }

    }

    private String getJSONString(){
        StringBuilder result = new StringBuilder();
        String urlStr = "https://turntotech.firebaseio.com/digitalleash/"+mSettings.getString(getString(R.string.username), "")+".json";
        try {
            URL url = new URL(urlStr);
            mHttpCon = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(mHttpCon.getInputStream());

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
        }catch(Exception e){e.printStackTrace();}
        return result.toString();
    }

    private void processJsonInfo(String jsonString) {

        if(jsonString == null){
            Toast.makeText(this, "User does not exist", Toast.LENGTH_SHORT).show();
        }
        else{
            try{

                JSONObject info = new JSONObject(jsonString);

                mParentLatitude = info.getString("latitude");
                mParentLongitude = info.getString("longitude");
                mChildLatitude = info.getString("child_latitude");
                mChildLongitude = info.getString("child_longitude");
                mRadius = info.getString("radius");

            }catch(Exception e){e.printStackTrace();}
        }
    }

    private boolean isInRadius(){
        Location parentLoc = new Location("parentLoc");
        Location childLoc = new Location("childLoc");

        parentLoc.setLatitude(parseDouble(mParentLatitude));
        parentLoc.setLongitude(parseDouble(mParentLongitude));

        childLoc.setLatitude(parseDouble(mChildLatitude));
        childLoc.setLongitude(parseDouble(mChildLongitude));

        double radius = Double.parseDouble(mRadius)*1609.34;

        float distance = parentLoc.distanceTo(childLoc);

        if (distance > radius){
            Log.d("LocationService", "Out of Bounds");
            return false;
        }

        Log.d("LocationService", "In Bounds");
        return true;
    }

}
