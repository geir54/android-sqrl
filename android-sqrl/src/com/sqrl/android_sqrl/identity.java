package com.sqrl.android_sqrl;

// Should hold everything related to the identity

public class identity {
	private byte[] Master_Key = "a secret psudo random number".getBytes();
	
	public identity() {
		
	}
	
	public byte[] getMasterKey() {
		return Master_Key;
	} 

	private void decryptMasterKey(String password)
	{
		// TODO: Scrypt to get key
		// Decrypt master key (AES)
	}


	public void LoadFromFile() {
		// Load form imported file
	}
	
}
