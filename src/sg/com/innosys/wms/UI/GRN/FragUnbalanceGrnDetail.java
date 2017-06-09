package sg.com.innosys.wms.UI.GRN;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
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
import sg.com.innosys.wms.BLL.GRN.GrnPut;
import sg.com.innosys.wms.R;
import sg.com.innosys.wms.UI.Common.ListViewItems;


@SuppressLint("ShowToast")
public class FragUnbalanceGrnDetail extends Fragment {
	View vDetailFragmentView;
	ActUnbalanceGrnDetail actUnbalanceGrnDetail;
	ArrayList<GrnDetail> grnDetailList; 
	ListView lvList;
	UnbalanceGrnDetailsAdapter adpGRNDetails;
	public int selectedRow;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);
		actUnbalanceGrnDetail = (ActUnbalanceGrnDetail) getActivity();
		if(savedInstanceState != null){
			actUnbalanceGrnDetail.selectedGrnDetail = (GrnDetail) savedInstanceState.get(actUnbalanceGrnDetail.SELECTEDGRNDETAIL);
		}
		
		try {
			RefreshAdapter();
		} catch (Exception e) {
			e.printStackTrace();
		}

		 lvList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		 lvList.setSelector(android.R.color.darker_gray);
		 lvList.setDivider(null);
		 lvList.setDividerHeight(0);
		lvList.setOnItemClickListener(new OnItemClickListener(){
			public void onItemClick(AdapterView<?> adapter, View view,
				int position, long arg3) {	
				adapter.setSelection(position);
				selectedRow = position;
				actUnbalanceGrnDetail.selectedGrnDetail = grnDetailList.get(position);
				Toast.makeText(getActivity(), "selected GRN Detail TrxNo - " + actUnbalanceGrnDetail.selectedGrnDetail.getTransactionNo()
								+" SeqNo - "+actUnbalanceGrnDetail.selectedGrnDetail.getSeqNo(), 3).show();
				
				actUnbalanceGrnDetail.palletIDList = new ArrayList<String>();
				for(GrnPut put : actUnbalanceGrnDetail.selectedGrnDetail.grnPuts){
					actUnbalanceGrnDetail.palletIDList.add(put.getPalletId().toString().toUpperCase());
				}
				actUnbalanceGrnDetail.tvwSelectedProduct.setText("Product Code : " + actUnbalanceGrnDetail.selectedGrnDetail.getProductCode()
										+ "; LineNo : " + actUnbalanceGrnDetail.selectedGrnDetail.getItemCode());
				adpGRNDetails.notifyDataSetChanged();
				FragUnbalanceGrnPut fragUnbalanceGrnPut = (FragUnbalanceGrnPut)actUnbalanceGrnDetail.getFragmentManager().findFragmentById(R.id.FragUnbalanceGrnPut);
				fragUnbalanceGrnPut.refreshAdapter();
			}				
		});	

		if(actUnbalanceGrnDetail.selectedGrnDetail ==null & grnDetailList.size() >0){
			actUnbalanceGrnDetail.selectedGrnDetail = grnDetailList.get(0);
		}
	}
	
	 @Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
		 
		 vDetailFragmentView = inflater.inflate(R.layout.frag_grndetail, container, false);
		 lvList = (ListView)vDetailFragmentView.findViewById(R.id.lvvGrnDetailList);
		 
	  return vDetailFragmentView;
	 }
		 
	 @Override
	 public void onSaveInstanceState(Bundle outState){
		 super.onSaveInstanceState(outState);
		 outState.putSerializable(actUnbalanceGrnDetail.SELECTEDGRNDETAIL, actUnbalanceGrnDetail.selectedGrnDetail);
		 outState.putStringArrayList(actUnbalanceGrnDetail.PALLETIDs, actUnbalanceGrnDetail.palletIDList);
	 }	
	 
	 @Override
	 public void onDestroy(){
		super.onDestroy();
	 }
	 
	 @Override
	 public void onResume() {
		 try {
			 actUnbalanceGrnDetail = (ActUnbalanceGrnDetail) getActivity();
			 RefreshAdapter();
			 
			if(actUnbalanceGrnDetail.selectedGrnDetail == null & grnDetailList.size() >0){
				actUnbalanceGrnDetail.selectedGrnDetail = grnDetailList.get(0);
			}
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
		grnDetailList = actUnbalanceGrnDetail.whApp.getGrnHeader().getUnbalanceGrnDetailList();
		
		adpGRNDetails = new UnbalanceGrnDetailsAdapter(getActivity(), R.layout.row_grndetail, grnDetailList);
		lvList.setAdapter(adpGRNDetails); 
	 }	
	 public class UnbalanceGrnDetailsAdapter extends ArrayAdapter<GrnDetail> {
		 ArrayList<GrnDetail> mGrnDetailList;

	    	public UnbalanceGrnDetailsAdapter(Context context, int textViewResourceId,
	    			ArrayList<GrnDetail> grnDeatilList) {
	             super(context, textViewResourceId, grnDeatilList);
	             mGrnDetailList = grnDeatilList;
	 
	         }
	    	
			public View getView(int position, View convertView, ViewGroup parent) {
	              View v = convertView;
	              if (v == null) {

	                  LayoutInflater vi = (LayoutInflater)actUnbalanceGrnDetail.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	                  v = vi.inflate(R.layout.row_grndetail, null);
	                  
	              }
	              GrnDetail item = mGrnDetailList.get(position);
	              
	              if(position ==  selectedRow){
	            	  v.setBackgroundColor(Color.parseColor(getString(R.color.listView_selectedRowBgColor)));
	              }
	              else{
	            	  v.setBackgroundColor(Color.TRANSPARENT);	            	  
	              }
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
	            	  ListViewItems scannedSerialNosView = (ListViewItems) v.findViewById(R.id.row_detailScannedSerialNos);
	            	  
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
	                  if(scannedSerialNosView != null){
	                	  scannedSerialNosView.setText(Integer.toString(item.grnSerialNos.size()));
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
