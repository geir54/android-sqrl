package com.sqrl.android_sqrl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import com.sqrl.utils.crypto;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

// Should hold everything related to the identity

public class identity {
	private byte[] MasterKey = new byte[32];// "a secret psudo random number".getBytes();
	private byte[] mixKey = new byte[32]; // The key that is XORed to make the master key
	private byte[] salt = new byte[8];
	private int iterations = 1000; // How many times to has the password default 1000
	
	public identity() {
		
	}
	
	// This creates salt and mixKey
	// Just using sha256 to make something for now
	public void createKeys() {
		mixKey = crypto.sha256(crypto.makeRandom(30));	
		salt = crypto.sha256(crypto.makeRandom(30));
		salt = crypto.subByte(salt, 0, 8);
			
		Log.i("id", Base64.encodeToString(mixKey, Base64.DEFAULT));
		Log.i("id", Base64.encodeToString(salt, Base64.DEFAULT));
		
	} 
	
	public byte[] getMasterKey() {
		return MasterKey;
	}
	
    private byte[] derivePassKey(String password) {
    	byte[] inn = crypto.ByteConcat(password.getBytes(), salt); // merge password and salt
    	byte[] passkey = inn;
    	
    	for(int i=0; i<=iterations; i++)
    	{
    		passkey = crypto.sha256(passkey);
    	}
    	
    	return passkey;
    }
	
	public void deriveMasterKey(String password)
	{	
		byte[] passkey = derivePassKey(password);
		MasterKey = crypto.xor(passkey, mixKey);		
	}
	
	public void changePassword(String password) {
		byte[] passkey = derivePassKey(password);
		mixKey = crypto.xor(passkey, MasterKey);
	}
		
	// Load from storage
	public void load(Context con) {		
		byte[] iter = new byte[4];
		try {
			FileInputStream fis = con.getApplicationContext().openFileInput("sqrl.dat");		
			fis.read(mixKey, 0, 32);
			fis.read(salt, 0, 8);		
			fis.read(iter, 0, 4); // read the bytes for the iterations int
			fis.close();
		}
		catch (Exception e) {			
		}			
		
		iterations = java.nio.ByteBuffer.wrap(iter).getInt(); // convert to int32
		
		Log.i("id", "mixkey " +Base64.encodeToString(mixKey, Base64.DEFAULT));
		Log.i("id", "Salt "+ Base64.encodeToString(salt, Base64.DEFAULT));
		Log.i("id", "iterations " +iterations);
	}
	
	// save to storage
	//TODO: allow for more than one user
	public void save(Context con) {
		try {
			FileOutputStream fos = con.getApplicationContext().openFileOutput("sqrl.dat", Context.MODE_PRIVATE);
			fos.write(mixKey);
			fos.write(salt);
			fos.write(ByteBuffer.allocate(4).putInt(iterations).array()); // convert the iterations int to four bytes
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
