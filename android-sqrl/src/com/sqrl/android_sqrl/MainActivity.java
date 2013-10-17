package com.sqrl.android_sqrl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;

import com.sqrl.crypto.ed25519;

import eu.livotov.zxscan.ZXScanHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class MainActivity extends Activity {
	private TextView textView1 = null;
	private EditText editText1 = null;
	private EditText editText2 = null;
	private authRequest authReq; // Contains all the info for the web page you are trying to authenticate with 
	private identity ident = new identity();
	
	private String pubKey = "";
	private String sign = "";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);     
        
        textView1 = (TextView) findViewById(R.id.textView1);
        editText1 = (EditText) findViewById(R.id.editText1);
        editText2 = (EditText) findViewById(R.id.editText2);  
        
        final Button confbutton = (Button) findViewById(R.id.confbutton);
        confbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	 editText1.setText("Please wait this will take time");  
            	 confbutton.setEnabled(false);
            	 new createSignature().execute(authReq.getURL());
            }  });
        
     //   ident.deleteIdentityFile(this.getApplicationContext()); // Uncomment to restart identity
        
        if (!ident.isIdentityCreated(this.getApplicationContext())) { // Check if an identity is created
        	// if not create it
        	Log.i("identity", "Create new identity");
        	ident.createMasterKey();
        	ident.save(this.getApplicationContext());
        }
        
        // Start QR activity
        ZXScanHelper.scan(this,12345);               
    }
    
    // Jumps here when QR is scanned
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data)
    {
        if (resultCode == RESULT_OK && requestCode == 12345)
        {
            String scannedCode = ZXScanHelper.getScannedCode(data);
            authReq = new authRequest(scannedCode);
            
            textView1.setText("Authenticate to " + authReq.getDomain());           
        }
    }
  
    private class createSignature extends AsyncTask<String, Void, String[]> {
        @Override
        protected String[] doInBackground(String... params) {        	 
             String URL = params[0];
         	 byte[] privateKey = CreatePrivateKey(authReq.getDomain(), ident.getMasterKey()); 
    		 byte[] publicKey = ed25519.publickey(privateKey);
    		 String publicKey_s = Base64.encodeToString(publicKey, Base64.DEFAULT); 
    
    		 byte[] signature = ed25519.signature(URL.getBytes(), privateKey, publicKey);
    		 String sign_s = Base64.encodeToString(signature, Base64.DEFAULT); 
              
          return new String[] {publicKey_s, sign_s};
        }

        @Override
        protected void onPostExecute(String[]result) {
        	pubKey = result[0];
        	sign = result[1];
        	editText1.setText(pubKey);        
        	editText2.setText(sign);    
     
        	web_post(authReq.getReturnURL(), authReq.getURL(), sign, pubKey);	
        }
      }
    
    // Create the private key from URL and secret key
    public static byte[] CreatePrivateKey(String domain, byte[] key) {    	
    	byte[] hmac=null;
    	try {    		
    		SecretKeySpec pKey = new SecretKeySpec(key, "HmacSHA256");
    		Mac mac = Mac.getInstance("HmacSHA256");
    		mac.init(pKey);
    		hmac = mac.doFinal(domain.getBytes());
    	} catch (Exception e) {    		
		}
    	
    	return hmac;
    }   
   
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    // Send signature and pubkey to server
    private void web_post(String URL, String message, String signature, String publicKey) {
    	HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost(URL);
	  
	    try {
	        // Add data to post
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        nameValuePairs.add(new BasicNameValuePair("message", message));
	        nameValuePairs.add(new BasicNameValuePair("signature", signature));
	        nameValuePairs.add(new BasicNameValuePair("publicKey", publicKey));
	    
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));	       	        
	        
	        HttpResponse response = httpclient.execute(httppost); // Execute HTTP Post Request
	    	
	        int status = response.getStatusLine().getStatusCode();
	        
	        Context context = getApplicationContext();
	        if (status == HttpStatus.SC_OK) {
	        	ByteArrayOutputStream ostream = new ByteArrayOutputStream();
	        	response.getEntity().writeTo(ostream);
	    
	        	String out = ostream.toString();
	        	Log.v("web", out);
	        	// See if the page returned "Verified"
	        	if (out.contains("Verified")) {
	        		Toast.makeText(context, "Verified", Toast.LENGTH_LONG).show(); // show the user	        		
	        	}
	        }  else {Log.v("web", "Connection not ok");}
	    } catch (ClientProtocolException e) {	
	    	Log.e("web", "error");
	    } catch (IOException e) {	  
	    	Log.e("web", "error");
	    }
    }
}
