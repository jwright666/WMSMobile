package sg.com.innosys.wms.UI.GRN;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.TextView;

import java.util.ArrayList;

import sg.com.innosys.wms.BLL.Common.SerialNo;
import sg.com.innosys.wms.BLL.Common.WhApp;
import sg.com.innosys.wms.BLL.Common.WhEnum.ConfirmGrnType;
import sg.com.innosys.wms.BLL.GRN.GrnSerialNo;
import sg.com.innosys.wms.BLL.GRN.PalletProduct;
import sg.com.innosys.wms.R;
import sg.com.innosys.wms.UI.Common.ListViewItems;

public class FragGrnSerialNo extends Fragment {
	public static final String TAG = "fragGrnSerialNo"; 
	private ActGrnPut actGrnPut ;
	private View vFragmentSerialNoView;
	private Button btnScanSerialNo;
	private ListView lvSerialNoList;
	private GRNSerialNoAdapter adptSerialNoList;
	//private GrnSerialNo scannedSerialNo;
	private TextView etwSerialNosTobeScan;
	private TextView etwSerialNosTotalScanned;
	private TextView etwProdCode;		
	private TextView etwBatchNo;	
	private TextView etwLotNo;	
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		actGrnPut = (ActGrnPut)getActivity();
		if(actGrnPut.selectedPalletProd == null || actGrnPut.selectedPalletProd.equals(new PalletProduct())){
			showSuccessErrorDialog(getString(R.string.msgWarning), getString(R.string.msgWarnNoSelectedPalletProd));
			actGrnPut.bar.setSelectedNavigationItem(2);			
		}
		else if(!actGrnPut.selectedPalletProd.getHasSerialNo() || actGrnPut.whApp.getGrnHeader().getConfirmGrnType() == ConfirmGrnType.CONFIRM_GRN_BEFORE_UPLOAD){
			showSuccessErrorDialog(getString(R.string.msgWarning), getString(R.string.msgSerialNoNotNecessary));
			actGrnPut.bar.setSelectedNavigationItem(2);
		}			 
		else{			
			btnScanSerialNo.setOnClickListener(btnClickListener);		
			if(savedInstanceState !=null){
				actGrnPut.selectedSerialNo = (GrnSerialNo) savedInstanceState.getSerializable(actGrnPut.SERIALNO);
			}	
			refreshAdapter();
		}
	}
	@Override 
	public void onDestroy(){
		 super.onDestroy();
	 }
	@Override 
	public void onResume(){
		 super.onResume();
		 actGrnPut = (ActGrnPut) getActivity(); 
		 if(actGrnPut.selectedPalletProd == null || actGrnPut.selectedPalletProd == new PalletProduct() || !actGrnPut.selectedPalletProd.getHasSerialNo()){
				actGrnPut.bar.setSelectedNavigationItem(2);
			}
		 else{
			 refreshAdapter(); 
		 }
	 }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){
		
		vFragmentSerialNoView = inflater.inflate(R.layout.frag_serialno, container, false);
		lvSerialNoList = (ListView) vFragmentSerialNoView.findViewById(R.id.lvvGinSerialNoList);
		btnScanSerialNo = (Button) vFragmentSerialNoView.findViewById(R.id.btn_ginScanSerialNo);
		etwSerialNosTobeScan = (TextView) vFragmentSerialNoView.findViewById(R.id.etw_ginSerialNoTobeScan);
		etwSerialNosTotalScanned =  (TextView) vFragmentSerialNoView.findViewById(R.id.etw_ginSerialNoTotalScanned);
		etwProdCode =  (TextView) vFragmentSerialNoView.findViewById(R.id.etw_ProdCode);
		etwBatchNo =  (TextView) vFragmentSerialNoView.findViewById(R.id.etw_BatchNo);
		etwLotNo =  (TextView) vFragmentSerialNoView.findViewById(R.id.etw_LotNo);
		return vFragmentSerialNoView;
	}
	
	private OnClickListener btnClickListener =  new OnClickListener(){
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//DiaScanSerialNo.newInstance(actGinPick.whApp.ADDMODE, null).show(getFragmentManager(), "dialog");
			///*

			WhApp.formMode = WhApp.ADDMODE;
			if(actGrnPut.selectedPalletProd.grnSerialNos.size() < actGrnPut.selectedPalletProd.getActQty()){
				//show dialog scan
				DiaScanGrnSerialNo.newInstance().show(getFragmentManager(), "dialog");
			}
			else{
				showSuccessErrorDialog(getString(R.string.msgAlert),getString(R.string.errExceedSerialNo));
			}
			//*/
		}
	};
	private OnItemClickListener itemCLick = new OnItemClickListener(){
		public void onItemClick(AdapterView<?> adpt, View view, int position,
				long arg3) {
			//scannedSerialNo = actGrnPut.selectedPalletProd.grnSerialNos.get(position);
			actGrnPut.selectedSerialNo = actGrnPut.selectedPalletProd.grnSerialNos.get(position);
			//show dialog scan	
			WhApp.formMode = WhApp.EDITMODE;	
			//DiaScanGrnSerialNo.newInstance(WhApp.EDITMODE, scannedSerialNo).show(getFragmentManager(), "dialog");
			DiaScanGrnSerialNo.newInstance().show(getFragmentManager(), "dialog");
		}		
	};

	private void refreshAdapter(){
		//actGinPick.selectGinDetail.ginPicks = new ArrayList<GinPick>();
		//Comparator<GinSerialNo> comparebyPalletId = GinDetail.sortBySerialNo;
		//Collections.sort(actGinPick.selectedGinDetail.ginSerialNos,comparebyPalletId);
		
		adptSerialNoList = new GRNSerialNoAdapter(getActivity(), R.layout.row_serialnolist,  actGrnPut.selectedPalletProd.grnSerialNos);
		lvSerialNoList.setAdapter(adptSerialNoList);
		lvSerialNoList.setOnItemClickListener(itemCLick);	
		if(actGrnPut.selectedPalletProd != null){
			etwSerialNosTobeScan.setText(Integer.toString(actGrnPut.selectedPalletProd.getActQty()));
			etwSerialNosTotalScanned.setText(Integer.toString(actGrnPut.selectedPalletProd.grnSerialNos.size()));
			etwProdCode.setText( actGrnPut.selectedPalletProd.getProductCode());
			etwBatchNo.setText( actGrnPut.selectedPalletProd.getBatchNo());
			etwLotNo.setText( actGrnPut.selectedPalletProd.getLotNo());
		
		}
	}
	private Dialog showSuccessErrorDialog(String title, String msg){
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    builder.setMessage(msg)
	    	   .setTitle(title)
	           .setCancelable(false);
	    AlertDialog alert = builder.create();
	    alert.setButton("OK", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {	            	   
	               }});
	        alert.show();	        
			return alert;
	}
	public class GRNSerialNoAdapter extends ArrayAdapter<GrnSerialNo> {
		 ArrayList<GrnSerialNo> mGrnSerialNoList;

	    	public GRNSerialNoAdapter(Context context, int textViewResourceId,
	    			ArrayList<GrnSerialNo> ginSerialNoList) {
	             super(context, textViewResourceId, ginSerialNoList);
	             mGrnSerialNoList = ginSerialNoList;
	 
	         }
	    	
			public View getView(int position, View convertView, ViewGroup parent) {
	              View v = convertView;
	              if (v == null) {
	                  LayoutInflater vi = (LayoutInflater)actGrnPut.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	                  v = vi.inflate(R.layout.row_serialnolist, null);  
	              }
	              SerialNo item = mGrnSerialNoList.get(position);	              
	              ///*
	              if (item != null) {
	            	  ListViewItems serialNoView = (ListViewItems) v.findViewById(R.id.row_serialNo);
	            	  ListViewItems serialNoDescView = (ListViewItems) v.findViewById(R.id.row_serialNoDesc);
	            	 
	                  if(serialNoView != null){
	                	  serialNoView.setText(item.getSerialNo());
	                  }
	                  if(serialNoDescView != null){
	                	  serialNoDescView.setText(item.getDescription());
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
			public GrnSerialNo getItem(int position){
				return mGrnSerialNoList.get(position);
			}

			 
	    }
}