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
	
	  private static final int DEFAULT_PORT = 4567;
	  private static final Gson GSON = new Gson();
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

		  //Spotify spot = new Spotify();
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
	  }
	  
	  private static class FrontHandler implements TemplateViewRoute {
		    @Override
		    public ModelAndView handle(Request req, Response res) {
		      Map<String, Object> variables = ImmutableMap.of("title",
		          "Stars: Query the database");
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
