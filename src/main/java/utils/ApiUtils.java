package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class ApiUtils {

	/**
	 * make a request to an api and return the response string
	 * @param urlString the api request 
	 * @return the response string
	 * @throws IOException
	 */
	public static String makeGETRequest(String urlString) throws IOException {
	      URL url = new URL(urlString);
	      HttpURLConnection newsCon = (HttpURLConnection) url.openConnection();
	      newsCon.setRequestMethod("GET");
	      if (newsCon.getResponseCode() != 200) {
	    	  return null;
	      }
	      // get json
	      try (BufferedReader in = new BufferedReader(
	          new InputStreamReader(newsCon.getInputStream(), "UTF-8"))) {
	        String inputLine;
	        StringBuffer response = new StringBuffer();
	        while ((inputLine = in.readLine()) != null) {
	          response.append(inputLine);
	        }
	       return response.toString();
	      }
	}
	
	
}
