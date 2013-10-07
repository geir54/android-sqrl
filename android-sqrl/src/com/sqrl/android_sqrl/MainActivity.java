package com.sqrl.android_sqrl;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.sqrl.crypto.ed25519;

import eu.livotov.zxscan.ZXScanHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Base64;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private static byte[] Master_Key = "a secret psudo random number".getBytes();
	private TextView textView1 = null;
	private EditText editText1 = null;
	private EditText editText2 = null;
	private String URL = "";
	
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
            	 new createSignature().execute(URL);
            }  });
        
        ZXScanHelper.scan(this,12345);            
    }
    
    // Jumps here when QR is scanned
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data)
    {
        if (resultCode == RESULT_OK && requestCode == 12345)
        {
            String scannedCode = ZXScanHelper.getScannedCode(data);
            
            URL = removeHTTP(scannedCode);
            String domain = getDomain(URL);
            
            textView1.setText("Authenticate to " + domain);
        }
    }
  
    private class createSignature extends AsyncTask<String, Void, String[]> {
        @Override
        protected String[] doInBackground(String... params) {        	 
             String URL = params[0];
         	 byte[] privateKey = CreatePrivateKey(getDomain(URL), Master_Key); 
    		 byte[] publicKey = ed25519.publickey(privateKey);
    		 String publicKey_s = Base64.encodeToString(publicKey, Base64.DEFAULT); 
    
    		 byte[] signature = ed25519.signature(URL.getBytes(), privateKey, publicKey);
    		 String sign_s = Base64.encodeToString(signature, Base64.DEFAULT); 
              
          return new String[] {publicKey_s, sign_s};
        }

        @Override
        protected void onPostExecute(String[]result) {
        	editText1.setText(result[0]);        
        	editText2.setText(result[1]);        
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

    // remove the HTTP:// part from the URL
    private String removeHTTP(String URL) {
    	if (URL.contains("://")) {
    		URL = URL.substring(URL.indexOf("://")+3);
    	}    	
    	return URL;
    }
    
    // get domain form URL
    private String getDomain(String URL) {    
    	URL = URL.substring(0,URL.indexOf("/?"));    	
    	return URL;
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
