package com.example.phillyzoo;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class FragmentB extends Fragment {

	public FragmentB() {
		// Required empty public constructor
	}

	private static View view;
	private static GoogleMap mMap;
	private static LatLng curPosition;
	static int diffInMin;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
		
		Bundle bundle=getArguments(); 
		String[] myStrings;
		if(bundle!= null)
			myStrings=bundle.getStringArray("speciesName"); 
        
	    if (container == null) {
	        return null;
	    }
	    
	    view = (RelativeLayout) inflater.inflate(R.layout.fragment_b, container, false);
	    
	    ImageButton refreshButton = (ImageButton)view.findViewById(R.id.refreshButton);
	    refreshButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				refresh();
			}
		});

	    setUpMapIfNeeded(); // For setting up the MapFragment

	    return view;
	}
	
	public static void refresh()
	{
		setUpMap(false);
	}

	public static void setUpMapIfNeeded() {
	    // Do a null check to confirm that we have not already instantiated the map.
	    if (mMap == null) {
	        // Try to obtain the map from the SupportMapFragment.
	        mMap = ((SupportMapFragment) MainActivity.fragmentManager.
	        		findFragmentById(R.id.location_map)).getMap();
	        // Check if we were successful in obtaining the map.
	        if (mMap != null)
	            setUpMap(true);
	    }
	}

	private static void setUpMap(boolean firstTime) {
		
		ParseQuery<ParseObject> querySpecies = ParseQuery.getQuery("Species");
		querySpecies.whereEqualTo("onMap",true);
		List<ParseObject> animalList = null;
		mMap.clear();
		try {
			animalList = querySpecies.find();
			
			String speciesName;
			int speciesIcon;
			LatLng animalPosition = null;
			SimpleDateFormat localDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			for(int i=0; i < animalList.size(); i++){ 
            	
            	speciesName=animalList.get(i).getString("speciesName");
            	speciesIcon = animalList.get(i).getInt("speciesIcon");
            	animalPosition = new LatLng(animalList.get(i).getDouble("animalLat"),animalList.get(i).getDouble("animalLong"));
            	
            	SimpleDateFormat dfDate  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         	    Date created = null;
         	    Date now = null;
         	    Calendar cal = Calendar.getInstance();
         	    String date = dfDate.format(animalList.get(i).getUpdatedAt());
         	    try {
						created = (Date) new java.sql.Date(dfDate.parse(dfDate.format(animalList.get(i).getUpdatedAt())).getTime());
						now = (Date)  new java.sql.Date(dfDate.parse(dfDate.format(cal.getTime())).getTime());
					} catch (java.text.ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

         	    diffInMin = (int) ((now.getTime() - created.getTime())/ (1000 * 60));
            	
            	mMap.addMarker(new MarkerOptions()
        		.position(animalPosition)
        		.icon(BitmapDescriptorFactory.fromResource(speciesIcon))
        		.title(speciesName + " : last seen " + diffInMin +" minutes ago")
        		.draggable(true));
            	
            }
		} catch (ParseException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		if(firstTime)
			mMap.setOnMapClickListener(new OnMapClickListener() {
				@Override
				public void onMapClick(LatLng position) {
					curPosition= position;
					
			        Polygon polygon = mMap.addPolygon(new PolygonOptions()
	        		.add(new LatLng(39.974933,-75.195537), new LatLng(39.974448,-75.194968), 
	        				new LatLng(39.972869,-75.194421), new LatLng(39.971628,-75.193938), 
	        				new LatLng(39.970238,-75.193369), new LatLng(39.969095,-75.192897), 
	        				new LatLng(39.968405,-75.192640), new LatLng(39.968931,-75.194807), 
	        				new LatLng(39.969112,-75.195462), new LatLng(39.969474,-75.196288), 
	        				new LatLng(39.969942,-75.196985), new LatLng(39.970723,-75.197661), 
	        				new LatLng(39.971192,-75.197875), new LatLng(39.971579,-75.197972), 
	        				new LatLng(39.972393,-75.197972), new LatLng(39.973009,-75.197725), 
	        				new LatLng(39.973593,-75.197253), new LatLng(39.974234,-75.196491), 
	        				new LatLng(39.974900,-75.195569), new LatLng(39.974933,-75.195537))
	        				.strokeColor(Color.RED));
			        List<LatLng> zooPoints = polygon.getPoints();
	
			        if (pointInPolygon(zooPoints, position))
					{
						Fragment newFragment = new AnimalListFragment();
						//FragmentB hostFragment= (MainActivity.fragmentManager.findFragmentById(R.id.location_map));
						List<Fragment> hostFragment = MainActivity.fragmentManager.getFragments();
						newFragment.setTargetFragment(hostFragment.get(1), 0);
						MainActivity.fragmentManager.beginTransaction().replace(R.id.location_map, newFragment).addToBackStack(null).commit();
					}
	
				}
			});
		
		LatLng phillyZoo = new LatLng(39.972403,-75.195826);
		GroundOverlayOptions phillyZooMap = new GroundOverlayOptions()
		.image(BitmapDescriptorFactory.fromResource(R.drawable.newmap))
		.position(phillyZoo,495f,735f);

		mMap.addGroundOverlay(phillyZooMap);
		// Enabling MyLocation Layer of Google Map
		mMap.setMyLocationEnabled(true);				
				
        CameraPosition cameraPosition = new CameraPosition.Builder()
        .target(phillyZoo)
        .zoom(16)
        .bearing(0)
        .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));	
        
        

        
        
	}
	
	public static boolean pointInPolygon (List<LatLng> poly, LatLng point) {
		int i, j, numSides;
		numSides = poly.size() - 1;
		j = numSides - 1;
		boolean oddNodes = false;
		double polyY_i, polyY_j, polyX_i, polyX_j, x, y;
		x = point.latitude;
		y = point.longitude;
		
		for (i = 0; i<numSides; i++) {
			polyY_i = poly.get(i).longitude;
			polyY_j = poly.get(j).longitude;
			polyX_i = poly.get(i).latitude;
			polyX_j = poly.get(j).latitude;

			if (polyY_i < y && polyY_j >= y
					|| polyY_j < y && polyY_i >= y) 
				if (polyX_i + (y - polyY_i)/(polyY_j - polyY_i)*(polyX_j - polyX_i) < x)
					oddNodes = !oddNodes;
			j = i;		
			
		}		
		return oddNodes;
	}
	
	
	
	public void receiveSpeciesName(int speciesImage, String speciesName){

		final double lat = curPosition.latitude;
        final double lon = curPosition.longitude;
        Log.d("receiveSpeciesName", "in receiveSpeciesName");
        
        ParseQuery<ParseObject> queryRelation = ParseQuery.getQuery("Species");
        queryRelation.whereEqualTo("speciesName", speciesName);
        queryRelation.getFirstInBackground(new GetCallback<ParseObject>(){
            public void done(ParseObject relationList, ParseException e){
                Log.d("done", "before for loop");
                if(relationList == null){
                    Log.d("null", "it's null hurrrhhh");
                }
                    String animalName = relationList.getString("speciesName");
                    Log.d("animalName", animalName);
                    relationList.put("animalLat", lat);
                    relationList.put("animalLong", lon);
                    relationList.put("onMap", true);
                    try {
						relationList.save();
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
            }
        });
		mMap.addMarker(new MarkerOptions()
		.position(curPosition)
		.icon(BitmapDescriptorFactory.fromResource(speciesImage))
		.title(speciesName + " last seen seconds ago")
		.draggable(true));
        //setUpMap(false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
	    // TODO Auto-generated method stub
	    if (mMap != null)
	        setUpMap(true);

	    if (mMap == null) {
	        // Try to obtain the map from the SupportMapFragment.
	        mMap = ((SupportMapFragment) MainActivity.fragmentManager
	                .findFragmentById(R.id.location_map)).getMap();
	        // Check if we were successful in obtaining the map.
	        if (mMap != null)
	            setUpMap(true);
	    }
	}

	@Override
	public void onDestroyView() {
	    super.onDestroyView();
	    if (mMap != null) {
	        MainActivity.fragmentManager.beginTransaction()
	            .remove(MainActivity.fragmentManager.findFragmentById(R.id.location_map)).commit();
	        mMap = null;
	    }
	}
}
