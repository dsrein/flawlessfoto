package apis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.common.escape.Escaper;
import com.google.common.net.UrlEscapers;
import com.google.common.util.concurrent.SettableFuture;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wrapper.spotify.Api;
import com.wrapper.spotify.methods.PlaylistCreationRequest;
import com.wrapper.spotify.methods.TrackSearchRequest;
import com.wrapper.spotify.methods.authentication.ClientCredentialsGrantRequest;
import com.wrapper.spotify.models.ClientCredentials;
import com.wrapper.spotify.models.Page;
import com.wrapper.spotify.models.Track;

import utils.ApiUtils;

public class Spotify {
 
  private final String ID = "43523841033c4694ba2f5ae7ec0f619c";
  private final String SECRET = "e7264805a5a54d3ca6e4a6f562911aaa";
  private String userId;
  private String name;
  
  Api api = Api.builder()
		  .clientId(ID)
		  .clientSecret(SECRET)
		  .redirectURI("http://localhost:8888/callback")
		  .build();
  
  /* Create a request object. */
  final ClientCredentialsGrantRequest request = api.clientCredentialsGrant().build();

  /* Use the request object to make the request, either asynchronously (getAsync) or synchronously (get) */
  final SettableFuture<ClientCredentials> responseFuture = request.getAsync();
  
  public Spotify(String userId, String name) {
	  this.userId = userId;
	  this.name = name;
  }
  
  private class PopComp implements Comparator<Track> {
		@Override
		public int compare(Track o1, Track o2) {
			int cmp = o1.getPopularity() < o2.getPopularity() ? +1 : o1.getPopularity() > o2.getPopularity() ? -1 : 0;
			return cmp;
		}
	  }

  
  public Track getTracksFromKeyword(String keyword) throws IOException {
	/*  Escaper escape = UrlEscapers.urlPathSegmentEscaper();
	  String word = escape.escape(keyword); */
	  final TrackSearchRequest request = api.searchTracks(keyword).build();	  
	  try {
	     final Page<Track> trackSearchResult = request.get();
	     List<Track> results = trackSearchResult.getItems();
	     Collections.sort(results, new PopComp());
	     if (results.size() == 0) {
	    	 return null;
	     }
	     return results.get(0);
	    // System.out.println("I got " + trackSearchResult.getTotal() + " results!");
	  } catch (Exception e) {
	     System.out.println("Something went wrong!" + e.getMessage());
	  }
	 return null;	  
  }
  
  public void makePlaylist() {
	  final PlaylistCreationRequest request = api.createPlaylist(userId, name)
			  .publicAccess(true)
			  .build();

			try {
			  final com.wrapper.spotify.models.Playlist playlist = request.get();
			  
			  System.out.println("You just created this playlist!");
			  System.out.println("Its title is " + playlist.getName());
			} catch (Exception e) {
			   System.out.println("Something went wrong!" + e.getMessage());
			}
  }
  
  
  
  

}
