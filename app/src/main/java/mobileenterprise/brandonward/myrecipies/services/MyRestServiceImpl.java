package mobileenterprise.brandonward.myrecipies.services;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import mobileenterprise.brandonward.myrecipies.domain.Recipe;

/**
 * Created by BrandonWard on 4/9/2015.
 */
public class MyRestServiceImpl implements IRestSvc{
    private static final String TAG = "MyRestServiceImpl";
    @Override
    public String retrieve() {
        Log.i(TAG, "doInBackground");

        StringBuffer result = new StringBuffer();

        HttpClient client = new DefaultHttpClient();
        String url = "http://10.0.2.2:8080/MobileEnterpriseWebService/webresources/service.dummy";
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Accept", "application/json");
        try {
            HttpResponse response = client.execute(httpGet);
            InputStream inputStream = response.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String s;
            while((s=reader.readLine()) !=null){
                result.append(s);
            }
        } catch(Exception e){
            Log.e(TAG,"EXCEPTION" + e.getMessage());
        }
        Log.i(TAG, "Returned String: "+ result.toString());
        return result.toString();
    }

    @Override
    public String retrieve(int id) {
        return null;
    }

    @Override
    public String create(Recipe recipe) {
        String result = "";
        Log.i(TAG,"Entering Create");
        HttpClient client = new DefaultHttpClient();
        String url = "http://10.0.2.2:8080/MobileEnterpriseWebService/webresources/service.dummy";
        HttpPost httpPost = new HttpPost(url);
        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("recipeName","Chocolate");
            jsonObject.put("ingredients","cocoa powder");
            jsonObject.put("instructions", "cook it good");
            jsonObject.put("cookTime",5);
            jsonObject.put("servings",1);
            StringEntity entity = new StringEntity(jsonObject.toString());
            entity.setContentType("Application/json");
            httpPost.setEntity(entity);
            HttpResponse response = client.execute(httpPost);

        } catch(Exception e){

        }
        Log.i(TAG,"Exiting create");
        return result;
    }

    @Override
    public String update(Recipe recipe) {
        return null;
    }

    @Override
    public String delete(int id) {
        return null;
    }
}
