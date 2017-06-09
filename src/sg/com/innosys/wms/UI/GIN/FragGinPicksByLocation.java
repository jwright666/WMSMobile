package sg.com.innosys.wms.UI.GIN;

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

import sg.com.innosys.wms.BLL.GIN.GinPick;
import sg.com.innosys.wms.R;
import sg.com.innosys.wms.R.id;
import sg.com.innosys.wms.R.layout;
import sg.com.innosys.wms.UI.Common.ListViewItems;

public class FragGinPicksByLocation extends Fragment {
	View vDetailFragmentView;
	ActGINMain actGinMain; 
	ListView lvList;
	GINPicksAdapter adpGINHeaderPicks;
	public int selectedRow;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);
		actGinMain = (ActGINMain) getActivity();
	
		//lvList = (ListView)getActivity().findViewById(R.id.lvvGinDetailList);
		try {
			RefreshAdapter();
		} catch (Exception e) {
			e.printStackTrace();
		}		
		lvList.setOnItemClickListener(new OnItemClickListener(){
			public void onItemClick(AdapterView<?> adapter, View view,
					int position, long arg3) {	
				selectedRow = position;
				GinPick selectedPick = (GinPick) adapter.getItemAtPosition(position);
				adpGINHeaderPicks.notifyDataSetChanged();
				try{					
					
					//start picking
					Intent intent = new Intent(getActivity(), ActGinPick.class);
					intent.putExtra(ActGINMain.SELECTEDITEM, selectedPick.getItemCode());
					intent.putExtra(ActGinPick.SELECTED_PICK, selectedPick);
					intent.putExtra(ActGinPick.PICKING_INDEX, position);
					startActivity(intent);
				}
				catch(Exception e){
					e.printStackTrace();
				}
		}			
		});			                
	}
	
	 @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
		 
		 vDetailFragmentView = inflater.inflate(R.layout.frag_picklist_by_location, container, false);
		 lvList = (ListView)vDetailFragmentView.findViewById(R.id.lvvGinPickListByLocation);

	  return vDetailFragmentView;
	 }	
	 
	 @Override
	public void onDestroy(){
		super.onDestroy();
	 }
	 
	@Override
	public void onResume() {
		 actGinMain = (ActGINMain) getActivity();
		 try {
			RefreshAdapter();
		} catch (Exception e) {
			e.printStackTrace();
		}
		 super.onResume();		 
	}

	@Override
	public void onPause() {
		super.onPause();
	}
	 
	public void RefreshAdapter() throws Exception
	 {
		ArrayList<GinPick> ginPickList = new ArrayList<GinPick>();
		if(actGinMain.whApp.getGinHeader().headerGinPicks.size() > 0){	
			actGinMain.whApp.getGinHeader().sortbyLocation();
			ginPickList = actGinMain.whApp.getGinHeader().headerGinPicks;		
		}
		
		adpGINHeaderPicks = new GINPicksAdapter(getActivity(), R.layout.row_ginpicklist_by_location, ginPickList);

		lvList.setAdapter(adpGINHeaderPicks); 		
	 }	

	public class GINPicksAdapter extends ArrayAdapter<GinPick> {
		 ArrayList<GinPick> mGinPickList;

	    	public GINPicksAdapter(Context context, int textViewResourceId,
	    			ArrayList<GinPick> ginPickList) {
	             super(context, textViewResourceId, ginPickList);
	             mGinPickList = ginPickList;
	 
	         }
	    	
			public View getView(int position, View convertView, ViewGroup parent) {
	              View v = convertView;
	              if (v == null) {

	                  LayoutInflater vi = (LayoutInflater)actGinMain.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	                  v = vi.inflate(R.layout.row_ginpicklist_by_location, null);
	                  
	                  
	              }
	              GinPick item = mGinPickList.get(position);	              
	              ///*
	              if (item != null) {
	            	  ListViewItems zoneView = (ListViewItems) v.findViewById(R.id.row_pickZone);
	            	  ListViewItems rowView = (ListViewItems) v.findViewById(R.id.row_pickRow);
	            	  ListViewItems columnView = (ListViewItems) v.findViewById(R.id.row_pickColumn);
	            	  ListViewItems tierView = (ListViewItems) v.findViewById(R.id.row_pickTier);
	            	  ListViewItems lineNoView = (ListViewItems) v.findViewById(R.id.row_pickItemCode);
	            	  ListViewItems prodCodeView = (ListViewItems) v.findViewById(R.id.row_pickProdCode);
	            	  ListViewItems actQtyView = (ListViewItems) v.findViewById(R.id.row_pickActQty);
	            	  ListViewItems srvQtyView = (ListViewItems) v.findViewById(R.id.row_pickSrvQty);
	            	  ListViewItems seqNoView = (ListViewItems) v.findViewById(R.id.row_pickSeqNo);

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
	                  if(lineNoView != null){
	                	  lineNoView.setText(item.getItemCode());
	                  }	
	                  if(prodCodeView != null){
	                	  prodCodeView.setText(item.getProductCode());
	                  }	                  
	                  if(actQtyView != null){
	                	  actQtyView.setText(Integer.toString(item.getActQty()));
	                  }   
	                  if(srvQtyView != null){
	                	  srvQtyView.setText(Integer.toString(item.getSrvQty()));
	                  }  
	                  if(seqNoView != null){              
	                	  seqNoView.setText(Integer.toString(item.getSeqNo()));  		  
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
			public GinPick getItem(int position){
				return mGinPickList.get(position);
			}
			 
	    }

}
