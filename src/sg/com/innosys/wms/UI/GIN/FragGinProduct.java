package sg.com.innosys.wms.UI.GIN;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import sg.com.innosys.wms.BLL.Common.WhApp;
import sg.com.innosys.wms.BLL.Common.WhAppException;
import sg.com.innosys.wms.BLL.Common.WhEnum.GINPickSortOption;
import sg.com.innosys.wms.BLL.GIN.GinPick;
import sg.com.innosys.wms.R;
import sg.com.innosys.wms.R.id;
import sg.com.innosys.wms.R.layout;
import sg.com.innosys.wms.UI.Common.ListViewItems;

@SuppressLint("NewApi")//20131230 -system generated
public class FragGinProduct extends Fragment {
	public static final String TAG = "fragGinProduct";
	private View myFragmentView;
	private ActGinPick actGinPick;
	private TextView tvwLineNo;
	private TextView tvwProdCode;
	private TextView tvwProdDesc;
	private TextView tvwBatch;
	private TextView tvwLot;
	private TextView tvwUOM;
	private TextView tvwSrvQty;
	private TextView tvwActQty;
	private TextView tvwWhNo;
	private TextView tvwScannedSerialNo;
	private TextView tvwHasSerialNo;
	private Button btnPicknewlocation;
	private ListView lvPickList;
	private GINPicksAdapter adpGinPicks;
	private LinearLayout lllist;
	private LinearLayout llNavigation;
	private Button btnNextPicking;
	private Button btnPreviousPicking;
	private Button btnEditPicking;
	private LinearLayout llLocation;
	private TextView tvwZone;
	private TextView tvwRow;
	private TextView tvwColumn;
	private TextView tvwTier;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);
		actGinPick = (ActGinPick)getActivity();	
		if(savedInstanceState == null){
			if(actGinPick.whApp.getGINPickSortOption() == GINPickSortOption.SORT_BY_LOCATION){
				btnPicknewlocation.setVisibility(View.GONE);
				lllist.setVisibility(View.GONE);
				llNavigation.setVisibility(View.VISIBLE);
				llLocation.setVisibility(View.VISIBLE);
				WhApp.formMode = WhApp.EDITMODE;
				
				//20140417 - gerry replaced to use the multifields scanning
				//DiaFragPickEntry.newInstance(WhApp.EDITMODE, actGinPick.selectedGinPick).show(getFragmentManager(), "dialog");
				DiaFragPickEntry_ScanMultiFields.newInstance(WhApp.EDITMODE, actGinPick.selectedGinPick).show(getFragmentManager(), "dialog");
			}
			else{
				btnPicknewlocation.setVisibility(View.VISIBLE);
				lllist.setVisibility(View.VISIBLE);
				llNavigation.setVisibility(View.GONE);
				llLocation.setVisibility(View.GONE);
			}
		}
	}	
	
	 @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	   Bundle savedInstanceState) {
	  
		  myFragmentView = inflater.inflate(R.layout.frag_ginproduct, container, false);
		  lllist = (LinearLayout)myFragmentView.findViewById(R.id.ll_ginPickList);
		  lvPickList = (ListView) myFragmentView.findViewById(R.id.lvvGinPickList);
		  tvwLineNo = (TextView) myFragmentView.findViewById(R.id.etv_ginProdLineNo);
		  tvwProdCode = (TextView) myFragmentView.findViewById(R.id.etv_ginProdPCode);
		  tvwProdDesc = (TextView) myFragmentView.findViewById(R.id.etv_ginProdPDesc);
		  tvwBatch = (TextView) myFragmentView.findViewById(R.id.etv_ginProdBatch_No);
		  tvwLot = (TextView) myFragmentView.findViewById(R.id.etv_ginProdLot_No);
		  tvwUOM = (TextView) myFragmentView.findViewById(R.id.etv_ginProdUOM);
		  tvwSrvQty = (TextView) myFragmentView.findViewById(R.id.etv_ginProdSrvQty);
		  tvwActQty = (TextView) myFragmentView.findViewById(R.id.etv_ginProdActQty);
		  tvwWhNo = (TextView) myFragmentView.findViewById(R.id.etv_ginProdWh_No);
		  tvwScannedSerialNo = (TextView) myFragmentView.findViewById(R.id.etv_ginProdScanSerialNos);
		  tvwHasSerialNo = (TextView) myFragmentView.findViewById(R.id.etv_hasSerialNo);
		  btnPicknewlocation = (Button) myFragmentView.findViewById(R.id.btn_ginProdPickNewLocation);

		  llNavigation = (LinearLayout)myFragmentView.findViewById(R.id.ll_ginPickNavigation);
		  btnPreviousPicking = (Button) myFragmentView.findViewById(R.id.btn_ginPreviousPick);
		  btnEditPicking = (Button) myFragmentView.findViewById(R.id.btn_ginEditPick);
		  btnNextPicking = (Button) myFragmentView.findViewById(R.id.btn_ginNextPick);

		  llLocation = (LinearLayout) myFragmentView.findViewById(R.id.ll_ginPickLocation);
		  tvwZone = (TextView) myFragmentView.findViewById(R.id.etv_PickZone);
		  tvwRow = (TextView) myFragmentView.findViewById(R.id.etv_PickRow);
		  tvwColumn = (TextView) myFragmentView.findViewById(R.id.etv_PickColumn);
		  tvwTier = (TextView) myFragmentView.findViewById(R.id.etv_PickTier);

	  return myFragmentView;
	 }
	private OnClickListener btnClickListener = new View.OnClickListener(){
		public void onClick(View btn) {
			try{
				switch(btn.getId()){
					case R.id.btn_ginProdPickNewLocation:
						// TODO pick new Location		
						//show dialog fragment to fill up new location
						WhApp.formMode =WhApp.ADDMODE;
						actGinPick.selectedGinPick = new GinPick();
						//DiaFragPickEntry.newInstance().show(getFragmentManager(), "dialog");//20131230 - gerry replaced old pick entry
						DiaFragPickEntry_ScanMultiFields.newInstance(WhApp.ADDMODE, new GinPick()).show(getFragmentManager(), "dialog");
						break;
					default:
						if(btn.getId() == R.id.btn_ginPreviousPick){
							actGinPick.whApp.getGinHeader().sortbyLocation();
							//TODO get previous picking from list and populate
							if(actGinPick.pickingIndex > 0){
								actGinPick.pickingIndex--;
								actGinPick.selectedGinPick = actGinPick.whApp.getGinHeader().getNextPreviousPicking(actGinPick.pickingIndex);
								actGinPick.selectedGinDetail = actGinPick.whApp.getGinHeader().getGinDetail(actGinPick.selectedGinPick.getItemCode());
								
								btnPreviousPicking.setEnabled(actGinPick.pickingIndex == 0 ? false : true);
								btnNextPicking.setEnabled(actGinPick.pickingIndex == actGinPick.whApp.getGinHeader().headerGinPicks.size() - 1 ? false : true);
								populateGinDetail();
							}
						}
						else if(btn.getId() == R.id.btn_ginNextPick){
							actGinPick.whApp.getGinHeader().sortbyLocation();
							//TODO get next picking from list and populate
							if(actGinPick.pickingIndex < actGinPick.whApp.getGinHeader().headerGinPicks.size()){
								actGinPick.pickingIndex++;
								actGinPick.selectedGinPick = actGinPick.whApp.getGinHeader().getNextPreviousPicking(actGinPick.pickingIndex);
								actGinPick.selectedGinDetail = actGinPick.whApp.getGinHeader().getGinDetail(actGinPick.selectedGinPick.getItemCode());
								
								btnPreviousPicking.setEnabled(actGinPick.pickingIndex == 0 ? false : true);
								btnNextPicking.setEnabled(actGinPick.pickingIndex == actGinPick.whApp.getGinHeader().headerGinPicks.size() - 1 ? false : true);
								populateGinDetail();
								WhApp.formMode = WhApp.EDITMODE;
								
								//20140417 - gerry replaced to use the multifields scanning
								//DiaFragPickEntry.newInstance(WhApp.EDITMODE, actGinPick.selectedGinPick).show(getFragmentManager(), "dialog");
								DiaFragPickEntry_ScanMultiFields.newInstance(WhApp.EDITMODE, actGinPick.selectedGinPick).show(getFragmentManager(), "dialog");
							}
						}
						else if(btn.getId() == R.id.btn_ginEditPick){
							//TODO get next picking from list and populate
							WhApp.formMode = WhApp.EDITMODE;
							
							//20140417 - gerry replaced to use the multifields scanning
							//DiaFragPickEntry.newInstance(WhApp.EDITMODE, actGinPick.selectedGinPick).show(getFragmentManager(), "dialog");
							DiaFragPickEntry_ScanMultiFields.newInstance(WhApp.EDITMODE, actGinPick.selectedGinPick).show(getFragmentManager(), "dialog");
						}
				}
			}
			catch(WhAppException e){
				e.printStackTrace();
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	 }; 
	 
	private OnItemClickListener itemClickListener = new OnItemClickListener(){
		public void onItemClick(AdapterView<?> adp, View view, int position,
				long arg3) {
			// TODO show Dialog Picking Entry
			actGinPick.selectedGinPick = actGinPick.selectedGinDetail.ginPicks.get(position);
			//DiaFragPickEntry.newInstance(WhApp.EDITMODE, actGinPick.selectedGinPick).show(getFragmentManager(), "dialog");
			DiaFragPickEntry_ScanMultiFields.newInstance(WhApp.EDITMODE, actGinPick.selectedGinPick).show(getFragmentManager(), "dialog");
		}
	}; 

	public void populateGinDetail(){
		///*
		if(actGinPick.selectedGinDetail != null){
			tvwLineNo.setText(actGinPick.selectedGinDetail.getItemCode());
			tvwProdCode.setText(actGinPick.selectedGinDetail.getProductCode());
			tvwProdDesc.setText(actGinPick.selectedGinDetail.getProductDescription());
			tvwWhNo.setText(actGinPick.selectedGinDetail.getWarehouseNo());
			if(actGinPick.selectedGinDetail.getBatchNo().trim() ==""){
				tvwBatch.setText("(null)");
			}
			else{
				tvwBatch.setText(actGinPick.selectedGinDetail.getBatchNo());				
			}
			if(actGinPick.selectedGinDetail.getLotNo().trim()==""){
				tvwLot.setText("(null)");
			}
			else{
				tvwLot.setText(actGinPick.selectedGinDetail.getLotNo());				
			}
			tvwScannedSerialNo.setText(Integer.toString(actGinPick.selectedGinDetail.ginSerialNos.size()));
			tvwUOM.setText(actGinPick.selectedGinDetail.getUOM());
			tvwSrvQty.setText(Integer.toString(actGinPick.selectedGinDetail.getServerQty()));
			tvwActQty.setText(Integer.toString(actGinPick.selectedGinDetail.getActQty()));
			tvwHasSerialNo.setText(actGinPick.selectedGinDetail.getHasSerialNo()?"Yes":"No");	
			
			//TODO add extra fields 
			if(actGinPick.selectedGinPick != null){
				tvwZone.setText(actGinPick.selectedGinPick.getZone());
				tvwRow.setText(actGinPick.selectedGinPick.getRow());
				tvwColumn.setText(actGinPick.selectedGinPick.getColumn());
				tvwTier.setText(actGinPick.selectedGinPick.getTier());
				//20140421 - gerry added codes below to display the correct qty from selected location
				tvwSrvQty.setText(Integer.toString(actGinPick.selectedGinPick.getSrvQty()));
				tvwActQty.setText(Integer.toString(actGinPick.selectedGinPick.getActQty()));
				//end 20140421
			}
			
			if(!actGinPick.whApp.getGinHeader().getIsConfirmedGin()){
				  btnPicknewlocation.setOnClickListener(btnClickListener);
				  lvPickList.setOnItemClickListener(itemClickListener);
				  btnPreviousPicking.setOnClickListener(btnClickListener);
				  btnEditPicking.setOnClickListener(btnClickListener);
				  btnNextPicking.setOnClickListener(btnClickListener);
			 }
		}
		//*/
	 }
	 @Override
	public void onDestroy(){
		super.onDestroy();
	 }
	@Override
	public void onResume() {
		super.onResume();	
		//actGinPick = (ActGinPick) getActivity();
		populateGinDetail();
		if(actGinPick.whApp.getGINPickSortOption() == GINPickSortOption.SORT_BY_LOCATION){
			btnPreviousPicking.setEnabled(actGinPick.pickingIndex == 0 ? false : true);
			btnNextPicking.setEnabled(actGinPick.pickingIndex == actGinPick.whApp.getGinHeader().headerGinPicks.size() - 1 ? false : true);
		}
		else{
			refreshAdapter();
		}
	}	
	@Override
	public void onPause() {
		super.onPause();
	}
    @Override
    public void onSaveInstanceState(Bundle outState){
    	super.onSaveInstanceState(outState);
    	outState.putInt(actGinPick.SELECTEDTAB, actGinPick.getActionBar().getSelectedNavigationIndex());
    	outState.putSerializable(ActGinPick.SELECTEDGINDETAIL, actGinPick.selectedGinDetail);
    	outState.putSerializable(ActGinPick.SELECTED_PICK, actGinPick.selectedGinPick);
    	outState.putInt(ActGinPick.PICKING_INDEX, actGinPick.pickingIndex);
    }
	
	private void refreshAdapter(){
		//actGinPick.selectGinDetail.ginPicks = new ArrayList<GinPick>();
		adpGinPicks = new GINPicksAdapter(getActivity(), R.layout.row_ginpicklist, actGinPick.selectedGinDetail.ginPicks);
		lvPickList.setAdapter(adpGinPicks); 	
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

	                  LayoutInflater vi = (LayoutInflater)actGinPick.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	                  v = vi.inflate(R.layout.row_ginpicklist, null);
	                  
	                  
	              }
	              GinPick item = mGinPickList.get(position);	              
	              ///*
	              if (item != null) {
	            	  ListViewItems zoneView = (ListViewItems) v.findViewById(R.id.row_pickZone);
	            	  ListViewItems rowView = (ListViewItems) v.findViewById(R.id.row_pickRow);
	            	  ListViewItems columnView = (ListViewItems) v.findViewById(R.id.row_pickColumn);
	            	  ListViewItems tierView = (ListViewItems) v.findViewById(R.id.row_pickTier);
	            	  ListViewItems actQtyView = (ListViewItems) v.findViewById(R.id.row_pickActQty);
	            	  ListViewItems srvQtyView = (ListViewItems) v.findViewById(R.id.row_pickSrvQty);
	            	  ListViewItems equalQtyView = (ListViewItems) v.findViewById(R.id.row_pickEqualQty);

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
	                  if(actQtyView != null){
	                	  actQtyView.setText(Integer.toString(item.getActQty()));
	                  }   
	                  if(srvQtyView != null){
	                	  srvQtyView.setText(Integer.toString(item.getSrvQty()));
	                  }  
	                  if(equalQtyView != null){
	                	  if(item.getSrvQty() == 0){
	                		  equalQtyView.setText("New Loc");
		                	  equalQtyView.setBackgroundColor(Color.parseColor("#ffffff"));
	                	  }
	                	  else if(item.getActQty() != item.getSrvQty()){
	                		  equalQtyView.setText("Not equal");
		                	  equalQtyView.setBackgroundColor(Color.RED);	                		  
	                	  }
	                	  else{
	                		  equalQtyView.setText("Equal");
		                	  equalQtyView.setBackgroundColor(Color.GREEN);
	                	  }	                		  
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