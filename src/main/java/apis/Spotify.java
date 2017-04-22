package apis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.SettableFuture;
import com.wrapper.spotify.Api;
import com.wrapper.spotify.methods.AddTrackToPlaylistRequest;
import com.wrapper.spotify.methods.PlaylistCreationRequest;
import com.wrapper.spotify.methods.TrackSearchRequest;
import com.wrapper.spotify.methods.authentication.ClientCredentialsGrantRequest;
import com.wrapper.spotify.models.AuthorizationCodeCredentials;
import com.wrapper.spotify.models.ClientCredentials;
import com.wrapper.spotify.models.Page;
import com.wrapper.spotify.models.Playlist;
import com.wrapper.spotify.models.Track;


public class Spotify {
 
  private final String ID = "43523841033c4694ba2f5ae7ec0f619c";
  private final String SECRET = "e7264805a5a54d3ca6e4a6f562911aaa";
  private String name;
  private Playlist playlist;
  private List<String> tracksToAdd;
  private int insertIndex;
  private String redirectURI = "http://localhost:8888/callback";
  private Api api = Api.builder()
		  .clientId(ID)
		  .clientSecret(SECRET)
		  .redirectURI(redirectURI)
		  .build();
  private String userId;
  /* Set the necessary scopes that the application will need from the user */
  private final List<String> scopes = Arrays.asList("user-read-private", "user-read-email");
  /* Set a state. This is used to prevent cross site request forgeries. */
  private final String state = "someExpectedStateString";
  /* Create a request object. */
  private final ClientCredentialsGrantRequest request = api.clientCredentialsGrant().build();

  /* Use the request object to make the request, either asynchronously (getAsync) or synchronously (get) */
  private final SettableFuture<ClientCredentials> responseFuture = request.getAsync();
  private String authorizeUrl;
  
  
  public Spotify(String userId, String name) {
	  this.userId = userId;
	  this.name = name;
	  tracksToAdd = new ArrayList<>();
	  insertIndex = 0;
  }
  
  private class PopComp implements Comparator<Track> {
		@Override
		public int compare(Track o1, Track o2) {
			int cmp = o1.getPopularity() < o2.getPopularity() ? +1 : o1.getPopularity() > o2.getPopularity() ? -1 : 0;
			return cmp;
		}
	  }
  
  public String getAuthrizeUrl() {
	  return authorizeUrl;
  }

  public void authorize() {
	  authorizeUrl = api.createAuthorizeURL(scopes, state);
	  
	  /* Application details necessary to get an access token */
	  final String code = redirectURI;

	  /* Make a token request. Asynchronous requests are made with the .getAsync method and synchronous requests
	   * are made with the .get method. This holds for all type of requests. */
	  final SettableFuture<AuthorizationCodeCredentials> authorizationCodeCredentialsFuture = api.authorizationCodeGrant(code).build().getAsync();

	  /* Add callbacks to handle success and failure */
	  Futures.addCallback(authorizationCodeCredentialsFuture, new FutureCallback<AuthorizationCodeCredentials>() {
	    @Override
	    public void onSuccess(AuthorizationCodeCredentials authorizationCodeCredentials) {
	      /* The tokens were retrieved successfully! */
	      System.out.println("Successfully retrieved an access token! " + authorizationCodeCredentials.getAccessToken());
	      System.out.println("The access token expires in " + authorizationCodeCredentials.getExpiresIn() + " seconds");
	      System.out.println("Luckily, I can refresh it using this refresh token! " +     authorizationCodeCredentials.getRefreshToken());
	    
	      /* Set the access token and refresh token so that they are used whenever needed */
	      api.setAccessToken(authorizationCodeCredentials.getAccessToken());
	      api.setRefreshToken(authorizationCodeCredentials.getRefreshToken());
	    }

	    @Override
	    public void onFailure(Throwable throwable) {
	      /* Let's say that the client id is invalid, or the code has been used more than once,
	       * the request will fail. Why it fails is written in the throwable's message. */
	    	System.out.println(throwable.getMessage());
	    }}); 
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
	     tracksToAdd.add(results.get(0).getUri());
	     if (playlist == null) {
	    	 startAPlaylist();
	     }
	     addTracksToPlaylist();
	     return results.get(0);
	    // System.out.println("I got " + trackSearchResult.getTotal() + " results!");
	  } catch (Exception e) {
	     System.out.println("Something went wrong!" + e.getMessage());
	  }
	 return null;	  
  }
  
  public void startAPlaylist() {
	  final PlaylistCreationRequest request = api.createPlaylist(userId, name)
			  .publicAccess(true)
			  .build();
			try {
			  playlist = request.get();
			  System.out.println("You just created this playlist!");
			  System.out.println("Its title is " + playlist.getName());
			} catch (Exception e) {
			   System.out.println("Something went wrong!" + e.getMessage());
			}
  }
  
  public void addTracksToPlaylist() {
	  AddTrackToPlaylistRequest request = api.addTracksToPlaylist(userId, playlist.getId() , tracksToAdd)
			  .position(insertIndex)
			  .build();			  
			try {
			  request.get();
			  insertIndex += tracksToAdd.size();
			  tracksToAdd.clear();
			} catch (Exception e) {
			   System.out.println("Something went wrong!" + e.getMessage());
			}
  }
  
  
  
  

}
