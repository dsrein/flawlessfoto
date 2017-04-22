package apis;


import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.input.image.ClarifaiImage;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Concept;

public class Clarifai {
	
	private final String clientID = "Rbey6xoT0H7-xxnousmtRtzjnLbcuRP9lyibdVH4";
	private final String clientSecret = "sRrOxdZ0Nli1nqFZN2ey502n_K9ehlGDhezFPYpV";
	private final ClarifaiClient client;
	
	public Clarifai(){
		client = new ClarifaiBuilder(clientID, clientSecret).buildSync();
	}
	
	public List<ClarifaiOutput<Concept>> search(String url){
		return client.getDefaultModels().generalModel() 
		        .predict()
		        .withInputs(
		            ClarifaiInput.forImage(ClarifaiImage.of(url))
//		             ClarifaiInput.forImage(ClarifaiImage.of(new File("/home/user/image.jpeg"))))
		        ).executeSync().get();
	}

	private List<String> getAttributes(String url){
		List<ClarifaiOutput<Concept>> concepts = search(url);
		LinkedList<String> names = new LinkedList<String>();
		for(ClarifaiOutput<Concept> concept: concepts){
			for(Concept c: concept.data()){
				names.add(c.name());
			}
		}
		return names;
	}
	
	private class ConceptComparator implements Comparator<Concept> {

		@Override
		public int compare(Concept o1, Concept o2) {       
			if (o1.value() > o2.value()){
				return -1;
            }
			
			if (o1.value() < o2.value()){
				return 1;
			}
			
			return 0;
		}
		
	}
	
	private List<String> getNAttributes(String url, int n){
		List<ClarifaiOutput<Concept>> concepts = search(url);
		List<String> toRet = new LinkedList<String>();
		PriorityQueue<Concept> names = new PriorityQueue<Concept>(new ConceptComparator());
		for(ClarifaiOutput<Concept> concept: concepts){
			for(Concept c: concept.data()){
				names.add(c);
				
			}
		}
		while(!names.isEmpty() && n > 0){
			n--;
			toRet.add(names.poll().name());
		}
		return toRet;
	}
	
	
	public static void main(String[] args){
		Clarifai myClarifai = new Clarifai();
		// List<String> names = myClarifai.getAttributes("https://samples.clarifai.com/metro-north.jpg");
		List<String> names2 = myClarifai.getNAttributes("https://samples.clarifai.com/metro-north.jpg", 3);
		
		for(String name: names2){
			System.out.println(name);
		}
	}
}
