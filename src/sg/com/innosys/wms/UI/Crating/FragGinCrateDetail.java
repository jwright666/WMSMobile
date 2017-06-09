package sg.com.innosys.wms.UI.Crating;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import sg.com.innosys.wms.BLL.Common.WhApp;
import sg.com.innosys.wms.BLL.Crating.GinCrate;
import sg.com.innosys.wms.R;
import sg.com.innosys.wms.UI.Common.ListViewItems;

public class FragGinCrateDetail extends Fragment{
	private GINCrateDetailsAdapter adpt;
	private View vDetailFragmentView;
	private ListView lvList;
	private ActCrateMain actCrate;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);
		actCrate = (ActCrateMain) getActivity();

		RefreshAdapter();	                
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
		vDetailFragmentView = inflater.inflate(R.layout.frag_cratedetail, container, false);
		lvList = (ListView)vDetailFragmentView.findViewById(R.id.lvvCrateDetailList);
		
		return vDetailFragmentView;
	 }
	 
	private OnItemClickListener itemClicked = new OnItemClickListener(){
		public void onItemClick(AdapterView<?> adapter, View view,
				int position, long arg3) {		
			actCrate.selectedGinCrate = actCrate.whApp.getGinHeader().ginCrates.get(position);						
			adpt.notifyDataSetChanged();
			try{					
				//start crate product activity
				Intent intent = new Intent(getActivity(), ActCrateProduct.class);
				intent.putExtra(ActCrateMain.CRATEMODE, WhApp.EDITMODE);
				if(actCrate.selectedGinCrate != null)
					intent.putExtra(ActCrateMain.SELECTEDCRATE, actCrate.selectedGinCrate);
				startActivity(intent);	
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}				
	};		 
	 
	 
	@Override
	public void onDestroy(){
		super.onDestroy();
	 }	 

	@Override
	public void onPause() {
		super.onPause();
	}
    public void onResume(){
    	super.onResume(); 
		actCrate = (ActCrateMain) getActivity();
		RefreshAdapter();	         
    }  
    
    public void clearAdapter(){
    	adpt = new GINCrateDetailsAdapter(getActivity(), R.layout.row_cratedetail, new ArrayList<GinCrate>());
		lvList.setAdapter(adpt);	
    }
	public void RefreshAdapter()
	 {
		if(actCrate.whApp.getGinHeader() != null){
			if(actCrate.whApp.getGinHeader().getIsConfirmedGin()){
					lvList.setOnItemClickListener(itemClicked);
			}
			adpt = new GINCrateDetailsAdapter(getActivity(), R.layout.row_cratedetail, actCrate.whApp.getGinHeader().ginCrates);			
		}
		lvList.setAdapter(adpt);
	 }
	
	public class GINCrateDetailsAdapter extends ArrayAdapter<GinCrate> {
		 ArrayList<GinCrate> mGinCrateList;

	    	public GINCrateDetailsAdapter(Context context, int textViewResourceId,
	    			ArrayList<GinCrate> ginCrateList) {
	             super(context, textViewResourceId, ginCrateList);
	             mGinCrateList = ginCrateList;
	 
	         }
	    	
			public View getView(int position, View convertView, ViewGroup parent) {
	              View v = convertView;
	              if (v == null) {

	                  LayoutInflater vi = (LayoutInflater)actCrate.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	                  v = vi.inflate(R.layout.row_cratedetail, null);
	                  
	                  
	              }
	              GinCrate item = mGinCrateList.get(position);
	              
	              ///*
	              if (item != null) {
	            	  ListViewItems crateNoView = (ListViewItems) v.findViewById(R.id.row_crateNo);
	            	  ListViewItems crateDescView = (ListViewItems) v.findViewById(R.id.row_crateDesc);
	            	  ListViewItems crateMarkingView = (ListViewItems) v.findViewById(R.id.row_crateMarking);
	            	  
	                  if(crateNoView != null){
	                	  crateNoView.setText(item.getCrateNo());
	                  }
	                  if(crateDescView != null){
	                	  crateDescView.setText(item.getCrateDesc());
	                  }
	                  if(crateMarkingView != null){
	                	  crateMarkingView.setText(item.getCrateMarking());
	                  }	                 
	              }
	              //*/
	              return v;
	          }

			@Override
			public int getCount() {
				return super.getCount();
			} 
			@Override
			public GinCrate getItem(int position){
				return mGinCrateList.get(position);
			}

			 
	    }

}
