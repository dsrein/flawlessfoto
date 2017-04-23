package apis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.SettableFuture;
import com.wrapper.spotify.Api;
import com.wrapper.spotify.exceptions.WebApiException;
import com.wrapper.spotify.methods.AddTrackToPlaylistRequest;
import com.wrapper.spotify.methods.ChangePlaylistDetailsRequest;
import com.wrapper.spotify.methods.GetMySavedTracksRequest;
import com.wrapper.spotify.methods.PlaylistCreationRequest;
import com.wrapper.spotify.methods.TrackSearchRequest;
import com.wrapper.spotify.methods.authentication.ClientCredentialsGrantRequest;
import com.wrapper.spotify.models.AuthorizationCodeCredentials;
import com.wrapper.spotify.models.ClientCredentials;
import com.wrapper.spotify.models.LibraryTrack;
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
  private String redirectURI = "http://localhost:4569";
  private Api api;
  private String userId;
  /* Set the necessary scopes that the application will need from the user */
  private final List<String> scopes = Arrays.asList("user-read-private", "user-read-email", "playlist-modify-public");
  /* Set a state. This is used to prevent cross site request forgeries. */
  private String state;
  /* Create a request object. */
  private final ClientCredentialsGrantRequest request;
  private String authorizeUrl;
  private String accessToken;
  
  
  public Spotify(String name) throws IOException {
	  
	  api = Api.builder()
			  .clientId(ID)
			  .clientSecret(SECRET)
			  .redirectURI(redirectURI)
			  .build();

	  request = api.clientCredentialsGrant().build();
	  try{
	  /* Use the request object to make the request, either asynchronously (getAsync) or synchronously (get) */
	  ClientCredentials responseFuture = request.get();
	  } catch (WebApiException e){
		  System.out.println("dude i dont know");
		  
	  }
	  this.name = name;
	  tracksToAdd = new ArrayList<>();
	  insertIndex = 0;
  }
  
  protected String generateRandString() {
      String SALTCHARS = "abcdefghijklmnopqrstuvwxyz1234567890";
      StringBuilder salt = new StringBuilder();
      Random rnd = new Random();
      while (salt.length() < 16) { // length of the random string.
          int index = (int) (rnd.nextFloat() * SALTCHARS.length());
          salt.append(SALTCHARS.charAt(index));
      }
      String saltStr = salt.toString();
      return saltStr;

  }
  
  private class PopComp implements Comparator<Track> {
		@Override
		public int compare(Track o1, Track o2) {
			int cmp = o1.getPopularity() < o2.getPopularity() ? +1 : o1.getPopularity() > o2.getPopularity() ? -1 : 0;
			return cmp;
		}
	  }
  
  public String getAuthrizeUrl() {
	  System.out.println("trying to authorize url");
	  state = generateRandString();
	  System.out.println(state);
	  authorizeUrl = api.createAuthorizeURL(scopes, state);
	  System.out.println("returning authorize URL "  + authorizeUrl);
	  return authorizeUrl;
  }
  
  public void setUser() throws IOException, WebApiException {
	  userId = api.getMe().build().get().getId();
	  System.out.println("user id : " + userId);
  }
  
  public String getPlaylist() {
	startAPlaylist();
  	return playlist.getUri();
  }

  public void authorize(String code) {
	  
	  /* Application details necessary to get an access token */
	  
	  System.out.println("CODE: " + code);

	  /* Make a token request. Asynchronous requests are made with the .getAsync method and synchronous requests
	   * are made with the .get method. This holds for all type of requests. */
	  final SettableFuture<AuthorizationCodeCredentials> authorizationCodeCredentialsFuture = api.authorizationCodeGrant(code).build().getAsync();

	  /* Add callbacks to handle success and failure */
	  Futures.addCallback(authorizationCodeCredentialsFuture, new FutureCallback<AuthorizationCodeCredentials>() {
	    @Override
	    public void onSuccess(AuthorizationCodeCredentials authorizationCodeCredentials) {
	      /* The tokens were retrieved successfully! */
	      accessToken = authorizationCodeCredentials.getAccessToken();
	      System.out.println("Successfully retrieved an access token! " + accessToken);
	      System.out.println("The access token expires in " + authorizationCodeCredentials.getExpiresIn() + " seconds");
	      System.out.println("Luckily, I can refresh it using this refresh token! " +     authorizationCodeCredentials.getRefreshToken());
	    
	      /* Set the access token and refresh token so that they are used whenever needed */
	      api.setAccessToken(accessToken);
	      api.setRefreshToken(authorizationCodeCredentials.getRefreshToken());
	    }
	    

	    @Override
	    public void onFailure(Throwable throwable) {
	      /* Let's say that the client id is invalid, or the code has been used more than once,
	       * the request will fail. Why it fails is written in the throwable's message. */
	    	System.out.println("failure authorizing");
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
	     if (authorizeUrl != null) {
	    	 tracksToAdd.add(results.get(0).getUri());
		     if (playlist == null) {
		    	 startAPlaylist();
		     }
		     addTracksToPlaylist();
	     }
	     return results.get(0);
	    // System.out.println("I got " + trackSearchResult.getTotal() + " results!");
	  } catch (Exception e) {
	     System.out.println("Something went wrong!" + e.getMessage());
	  }
	 return null;	  
  }
  
  public void startAPlaylist() {
	 // api = Api.builder().accessToken(accessToken).build();

	 // System.out.println(accessToken);
	  final PlaylistCreationRequest request1 = api.createPlaylist(userId, name)
			  .publicAccess(true)
			  .build();
	  	    System.out.println(request1.toString());
			try {
			  playlist = request1.get();
			  System.out.println("You just created this playlist!");
			  System.out.println("Its title is " + playlist.getName());
			} catch (Exception e) {
			   System.out.println("Something went wrong!" + e.getMessage());
			}
  }
  
public void changePlaylistName(String newName){
	final Api api = Api.builder().accessToken(accessToken).build();

	ChangePlaylistDetailsRequest request = api
	  .changePlaylistDetails(userId, playlist.getId())
	  .publicAccess(true)
	  .name(newName)
	  .build();

	try {
	  String response = request.get();
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
