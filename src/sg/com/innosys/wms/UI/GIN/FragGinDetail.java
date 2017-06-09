package sg.com.innosys.wms.UI.GIN;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import sg.com.innosys.wms.BLL.GIN.GinDetail;
import sg.com.innosys.wms.R;
import sg.com.innosys.wms.R.color;
import sg.com.innosys.wms.R.id;
import sg.com.innosys.wms.R.layout;
import sg.com.innosys.wms.UI.Common.ListViewItems;

public class FragGinDetail extends Fragment {
	View vDetailFragmentView;
	ActGINMain actGin;
	ArrayList<GinDetail> ginDetailList; 
	ListView lvList;
	GINDetailsAdapter adpGINDetails;
	public int selectedRow;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);
		actGin = (ActGINMain) getActivity();
	
		//lvList = (ListView)getActivity().findViewById(R.id.lvvGinDetailList);
		try {
			RefreshAdapter();
		} catch (Exception e) {
			e.printStackTrace();
		}		
		lvList.setOnItemClickListener(new OnItemClickListener(){
			public void onItemClick(AdapterView<?> adapter, View view,
					int position, long arg3) {	
				actGin.selectedGinDetail = actGin.whApp.getGinHeader().ginDetails.get(position);
				selectedRow = position;
				adpGINDetails.notifyDataSetChanged();
				try{	
			
					//start picking
					Intent intent = new Intent(getActivity(), ActGinPick.class);
					intent.putExtra(ActGINMain.SELECTEDITEM, actGin.selectedGinDetail.getItemCode());
					intent.putExtra(ActGINMain.SELECTEDGINDETAIL, actGin.selectedGinDetail);
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
		 
		 vDetailFragmentView = inflater.inflate(R.layout.frag_gindetail, container, false);
		 lvList = (ListView)vDetailFragmentView.findViewById(R.id.lvvGinDetailList);

	  return vDetailFragmentView;
	 }	
	 
	 @Override
	public void onDestroy(){
		super.onDestroy();
	 }
	 
	@Override
	public void onResume() {
		 actGin = (ActGINMain) getActivity();
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
		adpGINDetails = new GINDetailsAdapter(getActivity(), R.layout.row_gindetail, actGin.whApp.getGinHeader().ginDetails);

		lvList.setAdapter(adpGINDetails); 		
	 }	

	public class GINDetailsAdapter extends ArrayAdapter<GinDetail> {
		 ArrayList<GinDetail> mGinDetailList;

	    	public GINDetailsAdapter(Context context, int textViewResourceId,
	    			ArrayList<GinDetail> ginDeatilList) {
	             super(context, textViewResourceId, ginDeatilList);
	             mGinDetailList = ginDeatilList;
	 
	         }
	    	
			public View getView(int position, View convertView, ViewGroup parent) {
	              View v = convertView;
	              if (v == null) {

	                  LayoutInflater vi = (LayoutInflater)actGin.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	                  v = vi.inflate(R.layout.row_gindetail, null);
	                  
	                  
	              }
	              GinDetail item = mGinDetailList.get(position);
	              if(position ==  selectedRow){
	            	  v.setBackgroundColor(Color.parseColor(getString(R.color.listView_selectedRowBgColor)));
	              }
	              else{
	            	  v.setBackgroundColor(Color.TRANSPARENT);	            	  
	              }
	              ///*
	              if (item != null) {
	            	  ListViewItems lineNoView = (ListViewItems) v.findViewById(R.id.row_detailSeqNo);
	            	  ListViewItems productCodeView = (ListViewItems) v.findViewById(R.id.row_detailProductCode);
	            	  ListViewItems descriptionView = (ListViewItems) v.findViewById(R.id.row_detailDescription);
	            	  ListViewItems batchView = (ListViewItems) v.findViewById(R.id.row_detailBatch);
	            	  ListViewItems lotView = (ListViewItems) v.findViewById(R.id.row_detailLot);
	            	  ListViewItems actQtyView = (ListViewItems) v.findViewById(R.id.row_detailActQuantity);
	            	  ListViewItems uomView = (ListViewItems) v.findViewById(R.id.row_detailUom);   
	            	  ListViewItems srvQtyView = (ListViewItems) v.findViewById(R.id.row_detailSrvQuantity);	            	 
	            	  ListViewItems whNoView = (ListViewItems) v.findViewById(R.id.row_detailWH_No);
	            	

	                  if(lineNoView != null){
	                	  lineNoView.setText(item.getItemCode());
	                  }
	                  if(productCodeView != null){
	                	  productCodeView.setText(item.getProductCode());
	                  }
	                  if(descriptionView != null){
	                	  descriptionView.setText(item.getProductDescription());
	                  }
	                  if(batchView != null){
	                	  batchView.setText(item.getBatchNo());
	                  }
	                  if(lotView != null){
	                	  lotView.setText(item.getLotNo());
	                  }
	                  if(actQtyView != null){
	                	  actQtyView.setText(Integer.toString(item.getActQty()));
	                  }
	                  if(uomView != null){
	                	  uomView.setText(item.getUOM());
	                  }    
	                  if(srvQtyView != null){
	                	  srvQtyView.setText(Integer.toString(item.getServerQty()));
	                  } 
	                  if(whNoView != null){
	                	  whNoView.setText(item.getWarehouseNo());
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
			public GinDetail getItem(int position){
				return mGinDetailList.get(position);
			}

			 
	    }

}
