package sg.com.innosys.wms.UI.GIN;

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

import sg.com.innosys.wms.BLL.Common.WhApp;
import sg.com.innosys.wms.BLL.GIN.GinSerialNo;
import sg.com.innosys.wms.R;
import sg.com.innosys.wms.UI.Common.ListViewItems;

public class FragGinSerialNo extends Fragment {
	public static final String TAG = "fragGinSerialNo"; 
	private ActGinPick actGinPick;
	private View vFragmentSerialNoView;
	private Button btnScanSerialNo;
	private ListView lvSerialNoList;
	private GINSerialNoAdapter adptSerialNoList;
	//private GinSerialNo scannedSerialNo;
	private TextView etwSerialNosTobeScan;
	private TextView etwSerialNosTotalScanned;	
	private TextView etwProdCode;		
	private TextView etwBatchNo;	
	private TextView etwLotNo;	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		actGinPick = (ActGinPick)getActivity();
		if(savedInstanceState !=null){
			actGinPick.scannedSerialNo = (GinSerialNo) savedInstanceState.getSerializable(ActGinPick.SERIALNO);
		}	
	}
	@Override 
	public void onDestroy(){
		 super.onDestroy();
	 }
	@Override 
	public void onResume(){
		 super.onResume();
		 actGinPick = (ActGinPick) getActivity();
		 if(!actGinPick.selectedGinDetail.getHasSerialNo()){
				showSuccessErrorDialog(getString(R.string.msgWarning), getString(R.string.msgSerialNoNotNecessary));
			}	
		 refreshAdapter(); 
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
			if(actGinPick.selectedGinDetail.ginSerialNos.size() < actGinPick.selectedGinDetail.getActQty()){
				//show dialog scan
				DiaScanGinSerialNo.newInstance().show(getFragmentManager(), "dialog");
			}
			else{
				showSuccessErrorDialog(getString(R.string.msgAlert),getString(R.string.errExceedSerialNo));
			}
			// */
		}
	};
	private OnItemClickListener itemCLick = new OnItemClickListener(){
		public void onItemClick(AdapterView<?> adpt, View view, int position,
				long arg3) {
			actGinPick.scannedSerialNo = (GinSerialNo)actGinPick.selectedGinDetail.ginSerialNos.get(position);
			//show dialog scan	
			WhApp.formMode = WhApp.EDITMODE;
			//DiaScanGinSerialNo.newInstance(WhApp.EDITMODE, scannedSerialNo).show(getFragmentManager(), "dialog");
			DiaScanGinSerialNo.newInstance().show(getFragmentManager(), "dialog");
			}
		
	};

	private void refreshAdapter(){
		//actGinPick.selectGinDetail.ginPicks = new ArrayList<GinPick>();
		//Comparator<GinSerialNo> comparebyPalletId = GinDetail.sortBySerialNo;
		//Collections.sort(actGinPick.selectedGinDetail.ginSerialNos,comparebyPalletId);
		if(!actGinPick.whApp.getGinHeader().getIsConfirmedGin()){
			lvSerialNoList.setOnItemClickListener(itemCLick);
			btnScanSerialNo.setOnClickListener(btnClickListener);
		}
		adptSerialNoList = new GINSerialNoAdapter(getActivity(), R.layout.row_serialnolist, actGinPick.selectedGinDetail.ginSerialNos);
		lvSerialNoList.setAdapter(adptSerialNoList);
		etwSerialNosTobeScan.setText(Integer.toString(actGinPick.selectedGinDetail.getActQty()));
		etwSerialNosTotalScanned.setText(Integer.toString(actGinPick.selectedGinDetail.ginSerialNos.size()));	
		if(actGinPick.selectedGinDetail != null){
			etwProdCode.setText( actGinPick.selectedGinDetail.getProductCode());
			etwBatchNo.setText( actGinPick.selectedGinDetail.getBatchNo());
			etwLotNo.setText( actGinPick.selectedGinDetail.getLotNo());
		}
	}
	private Dialog showSuccessErrorDialog(String title, final String msg){
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    builder.setMessage(msg)
	    	   .setTitle(title)
	           .setCancelable(false);
	    AlertDialog alert = builder.create();
	    alert.setButton("OK", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {	
	            	   if(msg.equalsIgnoreCase(getString(R.string.msgSerialNoNotNecessary))){
	            		   //go back to first tab
	       				   actGinPick.bar.setSelectedNavigationItem(0);
	            	   }
	               }});
	        alert.show();	        
			return alert;
	}
	public class GINSerialNoAdapter extends ArrayAdapter<GinSerialNo> {
		 ArrayList<GinSerialNo> mGinSerialNoList;

	    	public GINSerialNoAdapter(Context context, int textViewResourceId,
	    			ArrayList<GinSerialNo> ginSerialNoList) {
	             super(context, textViewResourceId, ginSerialNoList);
	             mGinSerialNoList = ginSerialNoList;
	 
	         }
	    	
			public View getView(int position, View convertView, ViewGroup parent) {
	              View v = convertView;
	              if (v == null) {

	                  LayoutInflater vi = (LayoutInflater)actGinPick.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	                  v = vi.inflate(R.layout.row_serialnolist, null);  
	              }
	              GinSerialNo item = (GinSerialNo) mGinSerialNoList.get(position);	              
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
			public GinSerialNo getItem(int position){
				return mGinSerialNoList.get(position);
			}

			 
	    }
}