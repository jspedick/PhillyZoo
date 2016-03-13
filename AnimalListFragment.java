package com.example.phillyzoo;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;


public class AnimalListFragment extends ListFragment{
	
	List<RowItem> rowItems;
	
	@Override
	public View onCreateView (final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rowItems = new ArrayList<RowItem>();

		ParseQuery<ParseObject> querySpecies = ParseQuery.getQuery("Species");
		querySpecies.orderByAscending("speciesName");
		querySpecies.findInBackground(new FindCallback<ParseObject>() {
		    public void done(List<ParseObject> species, ParseException e) {
				final String[] myString = new String[species.size()];
				final int[] myInts = new int[species.size()];
				RowItem item;
		        if (e == null) {

		            for(int i=0; i < species.size(); i++){ 
		            	
		            	myString[i]=species.get(i).getString("speciesName");
		            	myInts[i] = species.get(i).getInt("speciesImage");
		            	item = new RowItem(myInts[i], myString[i]);
		            	rowItems.add(item);
		            }
		            CustomListViewAdapter adapter = new CustomListViewAdapter(inflater.getContext(),R.layout.single_animal, rowItems);
		            setListAdapter(adapter);
		            
		        } 
		    }
		});	
		
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		
		super.onListItemClick(l, v, position, id);
		final String speciesName = ((TextView) v.findViewById(R.id.tvName)).getText().toString();
		int speciesPicId = ((ImageView)v.findViewById(R.id.ivAnimal)).getId();
		
		ParseQuery<ParseObject> querySpecies = ParseQuery.getQuery("Species");
		querySpecies.whereEqualTo("speciesName", speciesName);
		querySpecies.getFirstInBackground(new GetCallback<ParseObject>() {
		    public void done(ParseObject species, ParseException e) {
		        if (e == null) {
                    	String myString = species.getString("speciesName");
		            	int myInt= species.getInt("speciesIcon"); 
		            	Fragment target = getTargetFragment();
		        		if(target instanceof FragmentB)
		        		{
		        			((FragmentB) target).receiveSpeciesName(myInt,myString);
		        		}
		        } 
		    }
		});
		
		MainActivity.fragmentManager.beginTransaction().hide(this).commit();
	}

	public class RowItem{
		private int speciesPicID;
		private String speciesName;
		
		public RowItem (int speciesPicID, String speciesName) {
			this.speciesPicID = speciesPicID;
			this.speciesName = speciesName;
		}
		public int getImageId() {return speciesPicID; }
		public void setImageId(int speciesPicID) {this.speciesPicID = speciesPicID;}
		public String getSpeciesName() {return speciesName; }
		public void setSpeciesName(String speciesName) {this.speciesName = speciesName;}
	}
	
	public class CustomListViewAdapter extends ArrayAdapter<RowItem> {
		Context context;
				
		
		public CustomListViewAdapter(Context context, int resourceID, List<RowItem> items) {
			super(context, resourceID, items);
			this.context = context;
		}
		
		private class ViewHolder {
			ImageView ivSpecies;
			TextView tvSpecies;
		}
		
		public View getView (int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			RowItem rowItem = getItem(position);
			
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.single_animal,	null);
				holder = new ViewHolder();
				holder.ivSpecies = (ImageView) convertView.findViewById(R.id.ivAnimal);
				holder.tvSpecies = (TextView) convertView.findViewById(R.id.tvName);
				convertView.setTag(holder);
			}
			else
				holder = (ViewHolder) convertView.getTag();
			
			holder.ivSpecies.setImageResource(rowItem.getImageId());
			holder.tvSpecies.setText(rowItem.getSpeciesName());
			
			return convertView;
		}	
	}
}

