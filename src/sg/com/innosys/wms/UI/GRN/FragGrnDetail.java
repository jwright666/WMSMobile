package sg.com.innosys.wms.UI.GRN;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import sg.com.innosys.wms.BLL.GRN.GrnDetail;
import sg.com.innosys.wms.R;
import sg.com.innosys.wms.UI.Common.ListViewItems;


public class FragGrnDetail extends Fragment {
	View vDetailFragmentView;
	ActGRNMain actGrnMain;
	ArrayList<GrnDetail> grnDetailList; 
	ListView lvList;
	GRNDetailsAdapter adpGRNDetails;
	GrnDetail selectedGrnDetail;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);
		actGrnMain = (ActGRNMain) getActivity();
		try {
			RefreshAdapter();
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
		lvList.setOnItemClickListener(new OnItemClickListener(){
			public void onItemClick(AdapterView<?> adapter, View view,
					int position, long arg3) {		
				selectedGrnDetail = actGrnMain.whApp.getGrnHeader().grnDetails.get(position);
				Toast.makeText(getActivity(), getString(R.string.toastPressPuttingButton), 3).show();
				adpGRNDetails.notifyDataSetChanged();			
			}				
		});			                
	}
	
	 @Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
		 
		 vDetailFragmentView = inflater.inflate(R.layout.frag_grndetail, container, false);
		 lvList = (ListView)vDetailFragmentView.findViewById(R.id.lvvGrnDetailList);

		 
	  return vDetailFragmentView;
	 }	
	 
	 @Override
	 public void onDestroy(){
		super.onDestroy();
	 }
	 
	 @Override
	 public void onResume() {
		 actGrnMain = (ActGRNMain) getActivity();
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
		adpGRNDetails = new GRNDetailsAdapter(getActivity(), R.layout.row_grndetail, actGrnMain.whApp.getGrnHeader().grnDetails);
		lvList.setAdapter(adpGRNDetails); 		
	 }	
	 public class GRNDetailsAdapter extends ArrayAdapter<GrnDetail> {
		 ArrayList<GrnDetail> mGrnDetailList;

	    	public GRNDetailsAdapter(Context context, int textViewResourceId,
	    			ArrayList<GrnDetail> grnDeatilList) {
	             super(context, textViewResourceId, grnDeatilList);
	             mGrnDetailList = grnDeatilList;
	 
	         }
	    	
			public View getView(int position, View convertView, ViewGroup parent) {
	              View v = convertView;
	              if (v == null) {

	                  LayoutInflater vi = (LayoutInflater)actGrnMain.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	                  v = vi.inflate(R.layout.row_grndetail, null);
	                  
	              }
	              GrnDetail item = mGrnDetailList.get(position);
	              
	              ///*
	              if (item != null) {
	            	  ListViewItems seqNoView = (ListViewItems) v.findViewById(R.id.row_detailSeqNo);
	            	  ListViewItems lineNoView = (ListViewItems) v.findViewById(R.id.row_detailItemCode);
	            	  ListViewItems productCodeView = (ListViewItems) v.findViewById(R.id.row_detailProductCode);
	            	  ListViewItems descriptionView = (ListViewItems) v.findViewById(R.id.row_detailDescription);
	            	  ListViewItems batchView = (ListViewItems) v.findViewById(R.id.row_detailBatch);
	            	  ListViewItems lotView = (ListViewItems) v.findViewById(R.id.row_detailLot);
	            	  ListViewItems actQtyView = (ListViewItems) v.findViewById(R.id.row_detailActQuantity);
	            	  ListViewItems uomView = (ListViewItems) v.findViewById(R.id.row_detailUom);   
	            	  ListViewItems srvQtyView = (ListViewItems) v.findViewById(R.id.row_detailSrvQuantity);
	            	  ListViewItems serialNoCountView = (ListViewItems) v.findViewById(R.id.row_detailScannedSerialNos);

	                  if(seqNoView != null){
	                	  seqNoView.setText(Integer.toString(item.getSeqNo()));
	                  }
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
	                  if(serialNoCountView != null){
	                	  serialNoCountView.setText(Integer.toString(item.grnSerialNos.size()));
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
			public GrnDetail getItem(int position){
				return mGrnDetailList.get(position);
			}

			 
	    }

}
