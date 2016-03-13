package com.example.phillyzoo;
import java.util.List;
import android.app.Application;
import android.util.Log;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;

public class ParseApplication extends Application {
	
	ParseObject speciesObject;
	ParseObject animalObject;
	String qSpeciesName;
	
	@Override
	public void onCreate() {
		super.onCreate();

		Parse.enableLocalDatastore(this);
		Parse.initialize(this, "Q5YCzo3XOj1wavhi35N835zjHl6LhFHyU9D3gaYn", "5BSj8OQlXI5VrForA5kWimtwVI0uK9ebGh1XOc9M");
		
		createPopulateSpecies();
		createPopulateAnimal();
		
	}
	
	private void createPopulateSpecies() {

		populateSpecies("Blue-eyed black Lemur","Tree-Top", R.drawable.lemur,R.drawable.lemur_icon);
		populateSpecies("Bolivian gray titi Monkey","Tree-Top", R.drawable.monkey,R.drawable.monkey_icon);
		populateSpecies("Red-Capped Mangabey", "Tree-Top", R.drawable.mangabey,R.drawable.mangabey_icon);
		populateSpecies("White-faced Saki","Tree-Top", R.drawable.saki,R.drawable.saki_icon);
		
		
		populateSpecies("Orangutan","Ape", R.drawable.orangutan,R.drawable.orangutan_icon);
		
		populateSpecies("Amur Tiger","Cat", R.drawable.amur_tiger,R.drawable.tiger_icon);
		populateSpecies("African Lion","Cat", R.drawable.african_lion,R.drawable.lion_icon);
		populateSpecies("Jaguar","Cat", R.drawable.jaguar,R.drawable.jaguar_icon);
		populateSpecies("Puma","Cat", R.drawable.puma, R.drawable.puma_icon);
		populateSpecies("Amur Leopard","Cat", R.drawable.amur_leopard, R.drawable.leopard_icon);
		populateSpecies("Snow Leopard","Cat", R.drawable.snowleopard,R.drawable.leopard_icon);
		
	}
	
	private void createPopulateAnimal(){
		populateAnimal("Stewart","Blue-eyed black Lemur");
		populateAnimal("Tua", "Orangutan");
		populateAnimal("Dimitri", "Amur Tiger");
		
	}

	public void populateSpecies(final String name, final String constraints, final int imageID, final int iconID) {
		
		//queries parse online database
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Species");
		query.whereEqualTo("speciesName", name);
		query.getFirstInBackground(new GetCallback<ParseObject>() {
		    public void done(ParseObject species, ParseException e) {
		        if (species == null) {
		    		speciesObject = new ParseObject("Species");
		    		speciesObject.put("speciesName", name);
		    		speciesObject.put("speciesTrail", constraints);
		    		speciesObject.put("speciesImage", imageID);
		    		speciesObject.put("speciesIcon",iconID);
		    		speciesObject.put("onMap", false);
		    		speciesObject.saveInBackground();    
		        } 
		    }
		});
		
		//queries local datastore
		ParseQuery<ParseObject> queryLocal = ParseQuery.getQuery("Species");
		queryLocal.whereEqualTo("speciesName", name);
		queryLocal.fromLocalDatastore();
		queryLocal.getFirstInBackground(new GetCallback<ParseObject>() {
		    public void done(ParseObject speciesLocal, ParseException e) {
		        if (speciesLocal == null) {
		    		speciesObject = new ParseObject("Species");
		    		speciesObject.put("speciesName", name);
		    		speciesObject.put("speciesTrail", constraints);
		    		speciesObject.put("speciesImage", imageID);
		    		speciesObject.put("speciesIcon",iconID);
		    		speciesObject.put("onMap", false);
		    		speciesObject.pinInBackground();  
		        } 
		    }
		});

	}
	
	public void populateAnimal(final String animalName, final String speciesName) {
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Animal");
		query.whereEqualTo("animalName", animalName);
		query.getFirstInBackground(new GetCallback<ParseObject>() {
		    public void done(ParseObject animal, ParseException e) {
		        if (animal == null) {
		    		
		        	animalObject = new ParseObject("Animal");
		    		animalObject.put("animalName", animalName);
		    		animalObject.saveInBackground();	
		    		
		    		ParseQuery<ParseObject> querySpecies= ParseQuery.getQuery("Species");
		    		querySpecies.whereEqualTo("speciesName", speciesName);
		    		querySpecies.getFirstInBackground(new GetCallback<ParseObject>() {
		    		    public void done(ParseObject objSpecies, ParseException e) {
		    		        if (e == null) {
		    		        	
		    		        	ParseRelation<ParseObject> relation = objSpecies.getRelation("animals");
		    		        	relation.add(animalObject);
		    		        	objSpecies.saveInBackground();
		    		        		            
		    		        } 
		    		    }
		    		});	
		        } 
		    }
		});	
	}
}
