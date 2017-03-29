package infotech.jain.app.school.schoolapp.service;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONException;
import org.json.JSONObject;

import infotech.jain.app.school.schoolapp.bean.UserFirebaseToken;
import infotech.jain.app.school.schoolapp.bean.Web_API_Config;
import infotech.jain.app.school.schoolapp.network.CheckInternetConnection;
import infotech.jain.app.school.schoolapp.sqlite_db.FirebaseTokenStorage;

/**
 * Created by admin on 04/08/16.
 */
public class MyFirebaseInstantIDService extends FirebaseInstanceIdService
{


    private static final String TAG = "MyFBInstantIDSerice";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh()
    {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // TODO: Implement this method to send any registration to your app's servers.
        //Checking Internet Connection
        CheckInternetConnection checkInternetConnection = new CheckInternetConnection();
        Log.e(TAG, "Outside Check Internet Connection");
        if(checkInternetConnection.checkingInternetConnection(getApplicationContext())){
            Log.e(TAG, "Inside Check Internet Connection");
            sendRegistrationToServer(refreshedToken);
        }

    }
    // [END refresh_token]

    private void sendRegistrationToServer(String token) {
        //Create JSONObjectRequest for Volley

        String url = Web_API_Config.send_token_fcm_notification;

        if(token == "" || token == null){
            token = "MyCustomToken";
        }

        //Getting Wifi Address
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo wInfo          = wifiManager.getConnectionInfo();
        String macAddress       = wInfo.getMacAddress();


        url = url + "?User_Id=0"+"&Token_Id="+token+"&Mac_Id="+macAddress;

        Log.e(TAG, url);
        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // the response is already constructed as a JSONObject!


                        Log.e(TAG, response.toString());

                        try {
                            String token = response.getString("Token_Id");
                            int success  = response.getInt("Status");

                            UserFirebaseToken userFirebaseToken = new UserFirebaseToken();
                            userFirebaseToken.setToken(token);
                            userFirebaseToken.setSuccess(success);

                            FirebaseTokenStorage firebaseTokenStorage = new FirebaseTokenStorage(getApplicationContext());
                            firebaseTokenStorage.addToken(userFirebaseToken);

                            Log.e(TAG, String.valueOf(success));

                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                        }





                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        Volley.newRequestQueue(this).add(jsonRequest);
    }

}
