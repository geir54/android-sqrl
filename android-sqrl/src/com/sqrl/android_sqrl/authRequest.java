package com.sqrl.android_sqrl;

import java.net.URI;
import java.net.URISyntaxException;

import android.util.Log;

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
	
	/**
	 *  The part that should be signed.
	 *  Removing the port number and login information from
	 *  the url.
	 */
	public String getURL() {		
		try {
			URI u = new URI("http://"+URL.replaceAll("|", "/"));
			StringBuilder sb = new StringBuilder();
			sb.append(u.getHost());
			sb.append(u.getPath());
			sb.append("?");
			sb.append(u.getQuery());		
			
			return sb.toString();
		} catch (URISyntaxException e) {
			Log.e("web", e.getMessage(), e);
		}
		return URL;
	}
	
	/**
	 * Return url should be the full url so we get the 
	 * querystring to the server for processing. 
	 */
	public String getReturnURL() {
		String retURL = (isHTTPS ? "https://" :  "http://") + URL;
		Log.v("web", "ReturnURL: "+retURL);
		return retURL;
	}
		
	/**
	 * Get domain form URL. Use the URL without the port number and login information
	 * to fetch the domain from. The scheme says to take everything
	 * before pipe if available and turn in to domain. Otherwise
	 * return the part before the slash.
	 * 
	 * @return
	 */
    public String getDomain() {
    	String domain = null;
    	if(URL.indexOf("|") > 0) {
    		domain = URL.substring(0,URL.indexOf("|"));
    	} else {
    		domain = URL.substring(0,URL.indexOf("/"));
    	}
    	
    	try {
			URI u = new URI("http://"+domain);
			StringBuilder sb = new StringBuilder();
			sb.append(u.getHost());
			sb.append(u.getPath());		
	    	    	
			Log.v("web", "Domain: "+sb.toString());
			return sb.toString();
    	} catch (Exception e) {
    		Log.e("web", e.getMessage(), e);    		
    	}
    	return domain;
    }
	
	  // remove the sqrl:// part from the URL and set isHTTPS
    private String removeScheme(String URL) {
    	if (URL.substring(0, 1).compareTo("s") == 0) {
    		URL = URL.substring(7);
    		isHTTPS = true; 
    	} else {
    		URL = URL.substring(6);
    		isHTTPS = false;
    	}    	
    	return URL;
    }

}
