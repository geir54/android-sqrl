package com.sqrl.android_sqrl;

// Copyright © 2013 geir54

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
		return URL;
	}
		
	 // get domain form URL
    public String getDomain() {    
    	String domain  = URL.substring(0,URL.indexOf("/"));    	
    	return domain;
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
