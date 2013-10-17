package com.sqrl.android_sqrl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import android.content.Context;
import android.util.Log;

// Should hold everything related to the identity

public class identity {
	private byte[] Master_Key = new byte[32];// "a secret psudo random number".getBytes();
	
	public identity() {
		
	}
	
	// Just using sha256 to make something for now
	public byte[] createMasterKey() {
		byte [] input = new byte[30];
		
		// A better random number generator should be used
		 Random randomGenerator = new Random();
		    for (int i = 0; i <= 29; ++i) {
		      int ran = randomGenerator.nextInt(255);
		      input[i] =(byte) ran;
		    }
		
		// calculate sha256
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
		
		digest.reset();
		
		Master_Key = digest.digest(input);	   
		
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		return Master_Key;
	} 
	
	public byte[] getMasterKey() {
		return Master_Key;
	}
	
	private void decryptMasterKey(String password)
	{
		// TODO: Scrypt to get key
		// Decrypt master key (AES)
	}
	
	private void encryptMasterKey(String password)
	{
		
	}
	
	// Load from storage
	public void load(Context con) {
		byte[] id = new byte[32];
		try {
			FileInputStream fis = con.getApplicationContext().openFileInput("sqrl.dat");		
			fis.read(id);
			fis.close();
		}
		catch (Exception e) {			
		}		
		Log.e("web", new String(id));
	}
	
	// save to storage
	//TODO: allow for more than one user
	public void save(Context con) {
		try {
			FileOutputStream fos = con.getApplicationContext().openFileOutput("sqrl.dat", Context.MODE_PRIVATE);
			fos.write(Master_Key);
			fos.close();
		}
		catch (Exception e) {
			
		}
	}
	
	// check if there is an Identity
	public boolean isIdentityCreated(Context con) {
		File file = con.getFileStreamPath("sqrl.dat");
		return file.exists();
	}
	
	// Deletes the identify file
	public boolean deleteIdentityFile(Context con) {
		File file = con.getFileStreamPath("sqrl.dat");
        return file.delete();
	}
	
	public void backuoToFile() {
		
	}

	public void restoreFromFile() {
		// Load form imported file
	}
	
}
