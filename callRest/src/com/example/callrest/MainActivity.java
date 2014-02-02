package com.example.callrest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.Principal;

import org.apache.http.HttpMessage;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Base64;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private TextView textViewSampleId,textViewSampleName;
	private Button button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		textViewSampleId=(TextView)findViewById(R.id.textViewSampleId);
		textViewSampleName=(TextView)findViewById(R.id.textViewSampleName);
		button=(Button)findViewById(R.id.button1);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// call AsynTask to perform network operation on separate thread
			/*new HttpAsyncTask().execute(
		        		"http://10.0.2.2:8080/booklet-ws/services/sample/listJson");
		      */  		
				
			new HttpAsyncTask().execute(
		        		"http://10.0.2.2:8080/booklet-ws/services/sample/addSample");
			}
		});
	}
	
	public static String GET(String url){
        InputStream inputStream = null;
        String result = "";
        try {
 
            // create HttpClient
            DefaultHttpClient httpclient = new DefaultHttpClient();
            
            // make GET request to the given URL
            HttpGet get=new HttpGet(url);
            get.setHeader("Accept", "application/json");
            get.setHeader("Content-Type", "application/json");
            
            //credentials
            String credentials = "gokman" + ":" + "kocaman";  
            String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);  
            get.addHeader("Authorization", "Basic " + base64EncodedCredentials);
            
 
            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(get);
 
            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();
 
            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";
 
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
 
        return result;
    }
	
	public static String POST(String url){
        InputStream inputStream = null;
        String result = "";
        try {
 
            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();
            
            
            // make GET request to the given URL
            HttpPost post=new HttpPost(url);
            post.setHeader("Accept", "application/json");
            post.setHeader("Content-Type", "application/json");
            
            //credentials
            String credentials = "gokman" + ":" + "kocaman";  
            String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);  
            post.addHeader("Authorization", "Basic " + base64EncodedCredentials);
            
            JSONObject jsonum=new JSONObject();
            jsonum.put("sampleName", "rarababacaca");
            
            
            StringEntity sampleEntity=new StringEntity(jsonum.toString());
            post.setEntity(sampleEntity);
            
            HttpResponse httpResponse = httpclient.execute(post);
 
            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();
 
            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";
 
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
 
        return result;
    }
	
	private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;
 
        inputStream.close();
        return result;
 
    }
	
	private class HttpAsyncTask extends AsyncTask<String, Void, String> {
		
        @Override
        protected String doInBackground(String... urls) {
 
            //return GET(urls[0]);
        	return POST(urls[0]);
        }
        
        @Override
        protected void onPostExecute(String result) {
        	//postExecuteForGet(result);
        	postExecuteForPost(result);		
       }
    }
	
	public void postExecuteForGet(String result){
		Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
        
        //convert to json
        JSONArray jsonArray;
        JSONObject jsonObject=null;
		try {
			jsonArray=new JSONArray(result);
			for(int i=0;i<jsonArray.length();i++){
			    jsonObject=(JSONObject)jsonArray.get(i);
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
			try {
				textViewSampleId.setText(jsonObject.getString("sampleId"));
				textViewSampleName.setText(jsonObject.getString("sampleName"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	public void postExecuteForPost(String result){
		Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
        
        //convert to json
        JSONObject jsonObject=null;
		try {
			
			    jsonObject=new JSONObject(result);
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
			try {
				textViewSampleId.setText(jsonObject.getString("sampleId"));
				textViewSampleName.setText(jsonObject.getString("sampleName"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}