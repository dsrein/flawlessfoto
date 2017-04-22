package main;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.Map;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import spark.ExceptionHandler;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import freemarker.template.Configuration;
import apis.Spotify;

public class Main {
	
	  private static final int DEFAULT_PORT = 4569;
	  private static final Gson GSON = new Gson();
	  private Spotify spot;
	/**
	   * The initial method called when execution begins .
	   *
	   * @param args
	   *          An array of command line arguments
	   * @throws IOException
	   *           if cannot read in input
	   */
	  public static void main(String[] args) throws IOException {
		  new Main().run();
	  }
	  
	  
	  private void run() throws IOException{

		 spot = new Spotify("lala");
		 // spot.getTracksFromKeyword("little bird");
		    // Parse command line arguments
		  runSparkServer();
		   
		  }


	  private static FreeMarkerEngine createEngine() {
	    Configuration config = new Configuration();
	    File templates = new File("src/main/resources/spark/template/freemarker");
	    try {
	      config.setDirectoryForTemplateLoading(templates);
	    } catch (IOException ioe) {
	      System.out.printf("ERROR: Unable use %s for template loading.%n",
	          templates);
	      System.exit(1);
	    }
	    return new FreeMarkerEngine(config);
	  }

	  private void runSparkServer() {
	    Spark.port(DEFAULT_PORT);
	    Spark.externalStaticFileLocation("src/main/resources/static");
	    Spark.exception(Exception.class, new ExceptionPrinter());
	    FreeMarkerEngine freeMarker = createEngine();
	    // Setup Spark Routes
	    Spark.get("/", new FrontHandler(), freeMarker);
	    Spark.post("/redirect", new LoginHandler());
	  }
	  
	  private class LoginHandler implements Route {
		    @Override
		    public Object handle(Request arg0, Response arg1) throws Exception {
		      System.out.println("login");
		      String url = spot.getAuthrizeUrl();
		      System.out.println(url.length());
		      spot.authorize();
		   //   spot.setUser();
		      return GSON.toJson(ImmutableMap.of("url", url));
		    }
	}
	  
	  private static class FrontHandler implements TemplateViewRoute {
		    @Override
		    public ModelAndView handle(Request req, Response res) {
		      Map<String, Object> variables = ImmutableMap.of("title",
		          "Life of the Party!");
		      return new ModelAndView(variables, "home.ftl");
		    }
		  }
	  
	  private static class ExceptionPrinter implements ExceptionHandler {
		    @Override
		    public void handle(Exception e, Request req, Response res) {
		      res.status(500);
		      StringWriter stacktrace = new StringWriter();
		      try (PrintWriter pw = new PrintWriter(stacktrace)) {
		        pw.println("<pre>");
		        e.printStackTrace(pw);
		        pw.println("</pre>");
		      }
		      res.body(stacktrace.toString());
		    }
		  }
}
