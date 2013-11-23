package com.sqrl.android_sqrl;

import android.util.Log;

// Copyright � 2013 geir54

//Contains all the info for the web page you are trying to authenticate with 
public class authRequest {	
	private boolean isHTTPS; // Is it using SSL or not
	private String URL; // contains everything except scheme
	
	
	public authRequest(String URL) {
		this.URL = removeScheme(URL);
	}
	
	public boolean isHTTPS() {
		return isHTTPS;
	}
	
	// The part thet should be signed
	public String getURL() {
		return URL.replace("|", "/");
	}
	
	public String getReturnURL() 
	{
		String retURL = URL.substring(0, URL.indexOf("?"));
		retURL = retURL.replace("|", "/");
		if (isHTTPS) retURL =  "https://" + retURL; else
			retURL =  "http://" + retURL;
		Log.v("web", retURL);
		return retURL;
	}
		
	 // get domain form URL
    public String getDomain() {    
    	if(URL.indexOf("|") == -1)
    		return URL.substring(0,URL.indexOf("/"));    	
    	else return URL.substring(0, URL.indexOf("|"));
    }
	
	  // remove the sqrl:// part from the URL and set isHTTPS
    private String removeScheme(String URL) {
    	if (URL.substring(0, 1).compareTo("s") == 0) 
    	{
    		URL = URL.substring(7);
    		isHTTPS = true; 
    	} 
    	else  
    	{
    		URL = URL.substring(6);
    		isHTTPS = false; 
    	}    	
    	return URL;
    }

}
