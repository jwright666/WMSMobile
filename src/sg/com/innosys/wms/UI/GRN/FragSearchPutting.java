package sg.com.innosys.wms.UI.GRN;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import sg.com.innosys.wms.BLL.Common.WhApp;
import sg.com.innosys.wms.BLL.GRN.GrnHeader;
import sg.com.innosys.wms.BLL.GRN.Pallet;
import sg.com.innosys.wms.R;
import sg.com.innosys.wms.UI.Common.ListViewItems;

public class FragSearchPutting extends Fragment{
	public static final String TAG = "fragSearchPutting"; 
	private View vFragSearchPutting;
	private Button btnAdd;
	private GrnPutSearchAdapter adpGrnPutSearchAdapter;
	private ListView lvList;
	private ActGrnPut actGrnPut;
	private ActionBar bar;
	private ArrayList<Pallet> palletList;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		//initialize here..
		actGrnPut = (ActGrnPut) getActivity();
		//actGrnPut.dbWhGrn.open();
		if(savedInstanceState != null){
			actGrnPut.LOCATION_MODE = savedInstanceState.getInt(ActGrnPut.LOCATIONMODE);
			actGrnPut.palletId =savedInstanceState.getString(actGrnPut.PALLETID);
			actGrnPut.isShowAllLocation = savedInstanceState.getBoolean(actGrnPut.ISSHOWALLLOCATION);
		}
		bar= actGrnPut.getActionBar();
		
		refreshAdapter();
		btnAdd = (Button) vFragSearchPutting.findViewById(R.id.btnAddSearchPut);
		btnAdd.setOnClickListener(btnClickListener);
		lvList.setOnItemClickListener(itemClick);
		if(!actGrnPut.isLocationSaved & actGrnPut.LOCATION_MODE != WhApp.DELETEMODE){
			bar.selectTab(bar.getTabAt(1));	
		}
    	else{
	    	refreshAdapter();
    	}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){
		
		vFragSearchPutting = inflater.inflate(R.layout.frag_searchputting, container, false);
		lvList = (ListView)vFragSearchPutting.findViewById(R.id.lvvSearchPutting);	
		btnAdd = (Button) vFragSearchPutting.findViewById(R.id.btnAddSearchPut);
		return vFragSearchPutting;
	}
   
    @SuppressWarnings("static-access")
	@Override
    public void onSaveInstanceState(Bundle outState){
    	super.onSaveInstanceState(outState);
    	actGrnPut = (ActGrnPut) getActivity();
    	outState.putInt(ActGrnPut.LOCATIONMODE, actGrnPut.LOCATION_MODE);
    	outState.putString(actGrnPut.PALLETID, actGrnPut.palletId);
    	outState.putBoolean(actGrnPut.ISSHOWALLLOCATION, actGrnPut.isShowAllLocation);
    	outState.putStringArrayList(actGrnPut.PALLETIDs, actGrnPut.palletIDList);    	
    }  	
	
    @Override
    public void onResume(){
    	super.onResume();
    	actGrnPut = (ActGrnPut) getActivity();
    	if(!actGrnPut.isLocationSaved & actGrnPut.LOCATION_MODE != WhApp.DELETEMODE){
			bar.selectTab(bar.getTabAt(1));	
		}
    	else{    		
	    	refreshAdapter();
    	}
    }
	@Override
    public void onPause(){
    	super.onPause(); 	
	}
	@Override
    public void onDestroy(){
    	super.onDestroy();
	}
	public OnClickListener btnClickListener = new OnClickListener() {
		public void onClick(View v) {					
			switch(v.getId()){
			case R.id.btnAddSearchPut:
				//do add			
				actGrnPut.LOCATION_MODE = WhApp.ADDMODE;
				actGrnPut.palletId = "";
				bar.selectTab(bar.getTabAt(1));				
				break;		
			}
	 	}
		};
		
	public OnItemClickListener itemClick = new OnItemClickListener(){

		public void onItemClick(AdapterView<?> adp, View view, int position,
				long arg3) {
			//grnPutsList = dbWhGrn.getAllGrnPut();
			Pallet selectedPallet = (Pallet) palletList.get(position);
			Toast.makeText(getActivity(), "Select Pallet is : \nPalletId - "
							+ selectedPallet.getPalletId()
							+ selectedPallet.getHasRack().toString(), 5).show();
			
			actGrnPut.palletId = selectedPallet.getPalletId().toUpperCase();
			//fillLocationFields(selectedPallet);
			//actGrnPut.grnHeader.objPallet = actGrnPut.grnHeader.hmPallet.get(selectedPallet.getPalletId().toUpperCase());
			actGrnPut.LOCATION_MODE = WhApp.EDITMODE;
			bar.selectTab(bar.getTabAt(1));	
		}		
	};
		
	public void refreshAdapter()
	 {
		palletList = new ArrayList<Pallet>();
		try{
			if(actGrnPut.isShowAllLocation){
				palletList = new ArrayList<Pallet>(actGrnPut.whApp.getGrnHeader().hmPallet.values());
			}
			else{
				HashMap<String, Pallet> tempHashMap = new HashMap<String, Pallet>();
				if(actGrnPut.palletIDList.size()>0){
					ArrayList<Pallet> tempPalletList = new ArrayList<Pallet>(actGrnPut.whApp.getGrnHeader().hmPallet.values());
					for(String palletID : actGrnPut.palletIDList){
						for(Pallet pallet : tempPalletList){
							if(pallet.getPalletId().equalsIgnoreCase(palletID))
								tempHashMap.put(pallet.getPalletId(), pallet);
						}
					}
				}
				palletList = new ArrayList<Pallet>(tempHashMap.values());
			}
			Comparator<Pallet> comparebyPalletId = GrnHeader.sortByPalletId;
			Collections.sort(palletList,comparebyPalletId);
			
			adpGrnPutSearchAdapter = new GrnPutSearchAdapter(getActivity(), R.layout.row_grnsearch_putting, palletList );
			lvList.setAdapter(adpGrnPutSearchAdapter); 		
			adpGrnPutSearchAdapter.notifyDataSetChanged();
		}
		catch(Exception e){}
		
	 }	
	public class GrnPutSearchAdapter extends ArrayAdapter<Pallet> {
		 ArrayList<Pallet> mPalletList;

	    	public GrnPutSearchAdapter(Context context, int textViewResourceId,
	    			ArrayList<Pallet> palletList) {
	             super(context, textViewResourceId, palletList);
	             mPalletList = palletList;
	 
	         }
	    	
			public View getView(int position, View convertView, ViewGroup parent) {
	              View v = convertView;
	              if (v == null) {

	                  LayoutInflater vi = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	                  v = vi.inflate(R.layout.row_grnsearch_putting, null);	                  
	              }
	              
	              Pallet item = mPalletList.get(position);
	              
	              if (item != null) {
	            	  ListViewItems palletIdView = (ListViewItems) v.findViewById(R.id.row_sp_PalletID);
	            	  ListViewItems zoneView = (ListViewItems) v.findViewById(R.id.row_sp_Zone);
	            	  ListViewItems rowView = (ListViewItems) v.findViewById(R.id.row_sp_Row);
	            	  ListViewItems columnView = (ListViewItems) v.findViewById(R.id.row_sp_Column);
	            	  ListViewItems tierView = (ListViewItems) v.findViewById(R.id.row_sp_Tier);
 	  
	            	  
	                  if(palletIdView != null){
	                	  palletIdView.setText(item.getPalletId());
	                  }
	                  if(zoneView != null){
	                	  zoneView.setText(item.getZone());
	                  }
	                  if(rowView != null){
	                	  rowView.setText(item.getRow());
	                  }
	                  if(columnView != null){
	                	  columnView.setText(item.getColumn());
	                  }    	   
	                  if(tierView != null){
	                	  tierView.setText(item.getTier());
	                  }    	               
	              }
	              
	              return v;
	          }

			@Override
			public int getCount() {
				return super.getCount();
			} 
			@Override
			public Pallet getItem(int position){
				return mPalletList.get(position);
			}

			 
	    }
}
