package sg.com.innosys.wms.UI.GRN;

import android.app.ActionBar;
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
import java.util.Collections;
import java.util.Comparator;

import sg.com.innosys.wms.BLL.Common.WhApp;
import sg.com.innosys.wms.BLL.Common.WhEnum.ConfirmGrnType;
import sg.com.innosys.wms.BLL.GRN.PalletProduct;
import sg.com.innosys.wms.R;
import sg.com.innosys.wms.UI.Common.ListViewItems;

public class FragItemQty extends Fragment {
	public static final String TAG = "fragItemQty"; 
	private TextView tvvSelectedProd;
	private TextView tvvConfirmType;
	private View vFragmentItemQtyView;
	private Button btnAdd;
	//private Button btnEdit;
	//private Button btnDelete;
	private ActGrnPut actGrnPut;
	private GrnPutListAdapter adpGrnPutListAdapter;
	private ListView lvList;
	private ArrayList<PalletProduct> palletProductList;
	private ActionBar bar;
	
	@Override
	public void onActivityCreated(final Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		actGrnPut = (ActGrnPut) getActivity();
		bar= actGrnPut.getActionBar();
		palletProductList = new ArrayList<PalletProduct>();
		actGrnPut.selectedPalletProd = null;
		if(savedInstanceState != null){
			actGrnPut.LOCATION_MODE = savedInstanceState.getInt(ActGrnPut.LOCATIONMODE);
			actGrnPut.isLocationSaved = savedInstanceState.getBoolean(ActGrnPut.ISLOCATIONSAVED);
			actGrnPut.PUTTING_MODE= savedInstanceState.getInt(ActGrnPut.PUTTINGMODE); 
			actGrnPut.palletId =savedInstanceState.getString(ActGrnPut.PALLETID);
		}
		tvvSelectedProd = (TextView) actGrnPut.findViewById(R.id.tvvSelectProduct);
		btnAdd = (Button) actGrnPut.findViewById(R.id.btnAddPutList);
		//btnEdit = (Button) actGrnPut.findViewById(R.id.btnEditPutList);	
		//btnDelete = (Button) actGrnPut.findViewById(R.id.btnDeletePutList);
		
		btnAdd.setOnClickListener(btnClicked);
		//btnEdit.setOnClickListener(btnClicked); //logic not yet confirmed
		//btnDelete.setOnClickListener(btnClicked);
		lvList.setOnItemClickListener(itemClick);
		if(!actGrnPut.isLocationSaved && actGrnPut.isShowAllLocation){
			bar.selectTab(bar.getTabAt(1));	
		}
		else{
			initializeDisplay();
			refreshAdapter();			
		}
	}
	public OnItemClickListener itemClick = new OnItemClickListener(){

		public void onItemClick(AdapterView<?> adp, View view, int position,
				long arg3) {
			//grnPutsList = dbWhGrn.getAllGrnPut();
			actGrnPut.selectedPalletProd = (PalletProduct) palletProductList.get(position);			
			//if(actGrnPut.isShowAllLocation){
			///*			
			showOptionDialog(getString(R.string.tvwSelectedProduct), " ProductCode- "+ actGrnPut.selectedPalletProd.getProductCode() +
													"\n\t\tBatchNo- "+ actGrnPut.selectedPalletProd.getBatchNo() +
													"\n\t\t\tLotNo- "+ actGrnPut.selectedPalletProd.getLotNo()).show();
			//}	
			//*/
		}
	};
	private OnClickListener btnClicked = new OnClickListener(){

		public void onClick(View btn) {
			if(btn == btnAdd){
				actGrnPut.PUTTING_MODE = WhApp.ADDMODE;
				DiaFragPutEntry.newInstance(actGrnPut.selectedPalletProd).show(getFragmentManager(), "dialog");
			}			
		}		
	};
	
	@Override
 	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){
		
		vFragmentItemQtyView = inflater.inflate(R.layout.frag_putlist, container, false);
		lvList = (ListView)vFragmentItemQtyView.findViewById(R.id.lvvGrnPutList);	
		btnAdd = (Button) vFragmentItemQtyView.findViewById(R.id.btnAddPutList);	
		//btnEdit = (Button) vFragmentItemQtyView.findViewById(R.id.btnEditPutList);	
		//btnDelete = (Button) vFragmentItemQtyView.findViewById(R.id.btnDeletePutList);
		tvvSelectedProd = (TextView) vFragmentItemQtyView.findViewById(R.id.tvvSelectProduct);
		tvvConfirmType = (TextView) vFragmentItemQtyView.findViewById(R.id.etwGrnHeader_ConfirmType);
		
		return vFragmentItemQtyView;
	}
	@Override
    public void onSaveInstanceState(Bundle outState){
		actGrnPut = (ActGrnPut) getActivity();
    	outState.putBoolean(ActGrnPut.ISLOCATIONSAVED, actGrnPut.isLocationSaved); 
    	outState.putInt(ActGrnPut.PUTTINGMODE, actGrnPut.PUTTING_MODE); 
    	outState.putString(ActGrnPut.PALLETID, actGrnPut.palletId);
    	outState.putInt(ActGrnPut.LOCATIONMODE, actGrnPut.LOCATION_MODE);
    	outState.putBoolean(actGrnPut.ISSHOWALLLOCATION, actGrnPut.isShowAllLocation);
    	outState.putSerializable(actGrnPut.PALLETPRODUCT, actGrnPut.selectedPalletProd);
    	super.onSaveInstanceState(outState);
    } 
	
	public void refreshAdapter()
	 {
		if(actGrnPut.whApp.getGrnHeader().hmPallet.get(actGrnPut.palletId.toString().toUpperCase()) == null){	
			palletProductList = new ArrayList<PalletProduct>();
		}
		else
			palletProductList = actGrnPut.whApp.getGrnHeader().hmPallet.get(actGrnPut.palletId.toString().toUpperCase()).palletProductList;
		
		if(palletProductList.size() >0){
			Comparator<PalletProduct> comparebyProductCode = actGrnPut.whApp.getGrnHeader().hmPallet.get(actGrnPut.palletId.toString().toUpperCase()).sortByProductCode;
			Collections.sort(palletProductList,comparebyProductCode);
		}
		adpGrnPutListAdapter = new GrnPutListAdapter(getActivity(), R.layout.row_grndetail, palletProductList );
		lvList.setAdapter(adpGrnPutListAdapter); 		
	 }		

	public void displayUnbalanceItem(){
		int balQty =actGrnPut.whApp.getGrnHeader().getBalanceQtyBatchLot(actGrnPut.selectedGrnDetail.getProductCode(), 
				actGrnPut.selectedGrnDetail.getBatchNo(), 
				actGrnPut.selectedGrnDetail.getLotNo());	
		tvvSelectedProd.setVisibility(View.VISIBLE);
		if(actGrnPut.selectedGrnDetail != null){
			tvvSelectedProd.setText("Unbalance Product Code: " + actGrnPut.selectedGrnDetail.getProductCode() 
								+ "\t\tBatch:" + actGrnPut.selectedGrnDetail.getBatchNo()
								+ "\nLot:" + actGrnPut.selectedGrnDetail.getLotNo()
								+ "\t\tBalance Qty:" 
								+ balQty);
		}
		else{
			tvvSelectedProd.setVisibility(View.GONE);
		}
		
	}
	private void initializeDisplay(){
		if(actGrnPut.whApp.getGrnHeader().getConfirmGrnType() == ConfirmGrnType.CONFIRM_GRN_BEFORE_UPLOAD){
			tvvConfirmType.setText(getString(R.string.msgNoSerialNoNeededForCustomer));				 
		 }
		else{
			tvvConfirmType.setVisibility(View.GONE);
		}
		
		if(!actGrnPut.isShowAllLocation){
			displayUnbalanceItem();
		}
		else{
			tvvSelectedProd.setVisibility(View.INVISIBLE);	
			tvvSelectedProd.setText("");
		}
	}
    
	@Override
    public void onPause(){
    	super.onPause();
    	initializeDisplay();
	}
	@Override
    public void onDestroy(){
    	super.onDestroy();
    	initializeDisplay();
	}
    @Override
    public void onResume(){
    	super.onResume();
    	actGrnPut = (ActGrnPut) getActivity();
    	if(!actGrnPut.isLocationSaved){
			bar.selectTab(bar.getTabAt(1));	
		}
		else{
			initializeDisplay();
			refreshAdapter();			
		}
    }
	public class GrnPutListAdapter extends ArrayAdapter<PalletProduct> {
		 ArrayList<PalletProduct> mPalletProductList;

	    	public GrnPutListAdapter(Context context, int textViewResourceId,
	    			ArrayList<PalletProduct> palletProductList) {
	             super(context, textViewResourceId, palletProductList);
	             mPalletProductList = palletProductList;
	 
	         }
	    	
			public View getView(int position, View convertView, ViewGroup parent) {
	              View v = convertView;
	              if (v == null) {

	                  LayoutInflater vi = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	                  v = vi.inflate(R.layout.row_grnputlist, null);	                  
	              }
	              
	              PalletProduct item = mPalletProductList.get(position);
	              
	              if (item != null) {
	            	  ListViewItems productCodeView = (ListViewItems) v.findViewById(R.id.row_putProductCode);
	            	  ListViewItems batchView = (ListViewItems) v.findViewById(R.id.row_putBatch);
	            	  ListViewItems lotView = (ListViewItems) v.findViewById(R.id.row_putLot);
	            	  ListViewItems actQtyView = (ListViewItems) v.findViewById(R.id.row_putQuantity);
	            	  ListViewItems actHasSerialNo = (ListViewItems) v.findViewById(R.id.row_putHasSerialNo);

	            	 
	                  if(productCodeView != null){
	                	  productCodeView.setText(item.getProductCode().toUpperCase());
	                  }
	                  if(batchView != null){
	                	  batchView.setText(item.getBatchNo().toUpperCase());
	                  }
	                  if(lotView != null){
	                	  lotView.setText(item.getLotNo().toUpperCase());
	                  }
	                  if(actQtyView != null){
	                	  actQtyView.setText(Integer.toString(item.getActQty()));
	                  }    	   
	                  if(actHasSerialNo != null){
	                	  actHasSerialNo.setText(item.getHasSerialNo()?"YES":"NO");
	                  }                  
	              }
	              
	              return v;
	          }

			@Override
			public int getCount() {
				return super.getCount();
			} 
			@Override
			public PalletProduct getItem(int position){
				return mPalletProductList.get(position);
			}

			 
	    }
	private AlertDialog showAlertDialog(String msg){
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    builder.setMessage(msg)
	    	   .setTitle("Warning")
	           .setCancelable(false)
	           .setPositiveButton("YES", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {	
	            	   try {
	            			actGrnPut.whApp.getGrnHeader().deleteGrnPutFromPalletProduct(getActivity(),actGrnPut.whApp.getGrnHeader().hmPallet.get(actGrnPut.palletId.toUpperCase()), actGrnPut.selectedPalletProd);
	            			actGrnPut.whApp.getGrnHeader().hmPallet.get(actGrnPut.palletId.toUpperCase()).palletProductList.remove(actGrnPut.selectedPalletProd);
	            			refreshAdapter();
	            	   } catch (Exception e) {
						e.printStackTrace();
	        			showSuccessErrorDialog(getString(R.string.msgError), e.getLocalizedMessage());
	            	   }
	       	      }		               
	           })
	           .setNegativeButton("NO", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {	
	            	  //do nothing	            	   
	               }
	           });
	    AlertDialog alert = builder.create();
	        alert.show();
	        
			return alert;		        
		 }
	private AlertDialog showOptionDialog(String title, String msg){
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	   
	    builder.setMessage(msg)
	    	   .setTitle(title)
	           .setCancelable(false)
	           .setPositiveButton(getString(R.string.btnDelete), new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {	
		             //TODO edit
		             doDelete();
	       	      }		               
	           })
	           .setNegativeButton(getString(R.string.btnScanSerialNo), new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {	
	            	 //TODO delete select palletProduct
	            	 doScanSerialNo();
		   		  }
	           });
	           /*
	           .setNeutralButton(getString(R.string.btnEdit), new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {	
	            	  //TODO scan serial No	      
	            	  doEdit();
	               }
	           })*/
	    AlertDialog alert = builder.create();
	        alert.show();
	        alert.setCanceledOnTouchOutside(true);        
	   
			return alert;		        
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
	
	private void doEdit(){
    	//if(!actGrnPut.isShowAllLocation){
			actGrnPut.PUTTING_MODE = WhApp.EDITMODE;
			
			DiaFragPutEntry.newInstance(actGrnPut.selectedPalletProd).show(getFragmentManager(), "dialog");
			try {
				actGrnPut.whApp.createGrnHeader();
			//actGrnPut.whApp.createGrnHeader(actGrnPut.whApp.getGrnHeader().getTransactionNo());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	//}
	}
	private void doDelete(){
		//TODO delete select palletProduct
 	   if((actGrnPut.whApp.getGrnHeader().hmPallet.get(actGrnPut.palletId.toUpperCase()) != null)
 		   & (actGrnPut.selectedPalletProd != null)){
				showAlertDialog(getString(R.string.msgSerialNoWillBeDelete)+"\n" +getString(R.string.msgConfirmDeleteProdFromPallet));
			}
	}
	private void doScanSerialNo(){
		if(actGrnPut.selectedPalletProd.getHasSerialNo() && actGrnPut.whApp.getGrnHeader().getConfirmGrnType() == ConfirmGrnType.CONFIRM_GRN_AFTER_UPLOAD){
         	  actGrnPut.selectedGrnDetail = actGrnPut.whApp.getGrnHeader().getSelectedGrnDetailForSerialNo(actGrnPut.selectedPalletProd);
	   		  actGrnPut.bar.setSelectedNavigationItem(3);
  	    }
		else{
			showSuccessErrorDialog(getString(R.string.msgWarning), getString(R.string.msgSerialNoNotNecessary));
		}
	}
	
}
