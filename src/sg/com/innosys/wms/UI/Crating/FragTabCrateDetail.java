package sg.com.innosys.wms.UI.Crating;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import sg.com.innosys.wms.BLL.Common.WhAppException;
import sg.com.innosys.wms.BLL.Crating.GinCrateProduct;
import sg.com.innosys.wms.BLL.GIN.GinDetail;
import sg.com.innosys.wms.R;
import sg.com.innosys.wms.R.color;
import sg.com.innosys.wms.R.id;
import sg.com.innosys.wms.R.layout;
import sg.com.innosys.wms.R.string;
import sg.com.innosys.wms.UI.Common.ListViewItems;

public class FragTabCrateDetail extends Fragment{
	private View vCrateDetailFragView;
	private ActCrateProduct actCrateProd;
	private GINCrateContentsAdapter ginCrateContentsAdpt;
	private GINUnAllocatedProdAdapter ginUnallocProdAdpt;
	private ListView lvCrateContents;
	private ListView lvUnallocProds;
	private Button btnDelete;
	private Button btnSave;
	private Button btnCancel;
	private EditText etwCrateNo;
	private EditText etwItemCode;
	private EditText etwProdCode;
	private EditText etwUOM;
	private EditText etwActQty;	
	private int newQty;
	public int crateProdSelectedRow;
	public int unAllocProdSelectedRow;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);	
		actCrateProd = (ActCrateProduct) getActivity(); 
		actCrateProd.crateProductMode = 0;
		
		if(actCrateProd.selectedGinCrate != null)
			actCrateProd.whApp.getGinHeader().assignSelectedCrateFromMemory(actCrateProd.selectedGinCrate);
		
		btnSave.setOnClickListener(btnClicked);
		btnDelete.setOnClickListener(btnClicked);
		btnCancel.setOnClickListener(btnClicked);
		lvCrateContents.setOnItemClickListener(itemCrateProdClicked);
		lvUnallocProds.setOnItemClickListener(itemUnAllocProdClicked);
		
		//if(actCrateProd.crateProductMode == actCrateProd.whApp.DELETEMODE){
		//	actCrateProd.bar.selectTab(actCrateProd.bar.getTabAt(0));
		//}
		refreshAdapter();
    	populateData();
		btnSave.setEnabled(false);
		btnDelete.setEnabled(false);
	}
	
	@Override	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){
		
		vCrateDetailFragView = inflater.inflate(R.layout.frag_crateproduct, container, false);
		btnSave = (Button) vCrateDetailFragView.findViewById(R.id.btnSaveCrateProd);
		btnDelete = (Button) vCrateDetailFragView.findViewById(R.id.btnDeleteCrateProd);
		btnCancel = (Button) vCrateDetailFragView.findViewById(R.id.btnCancelCrateProd);
		etwCrateNo = (EditText) vCrateDetailFragView.findViewById(R.id.etwCrateProd_CrateNo);
		etwItemCode = (EditText) vCrateDetailFragView.findViewById(R.id.etwCrateProd_ItemCode);
		etwProdCode = (EditText) vCrateDetailFragView.findViewById(R.id.etwCrateProd_ProdCode);
		etwUOM = (EditText) vCrateDetailFragView.findViewById(R.id.etwCrateProd_UOM);
		etwActQty = (EditText) vCrateDetailFragView.findViewById(R.id.etwCrateProd_ActQty);
		lvCrateContents = (ListView) vCrateDetailFragView.findViewById(R.id.lvvCrateProdList);
		lvUnallocProds = (ListView) vCrateDetailFragView.findViewById(R.id.lvvUnallocProdList);
		return vCrateDetailFragView;
	}

	@Override
    public void onPause(){
    	super.onPause();
	}
	@Override
    public void onDestroy(){
    	super.onDestroy();
	}
	@Override
    public void onResume(){
    	super.onResume();
    	actCrateProd = (ActCrateProduct) getActivity();
		if(actCrateProd.selectedGinCrate != null)
			actCrateProd.whApp.getGinHeader().assignSelectedCrateFromMemory(actCrateProd.selectedGinCrate);
		
		refreshAdapter();
    	populateData();
		btnSave.setEnabled(false);
		btnDelete.setEnabled(false);
	}
	private void populateData(){
		clearFields();
		if(actCrateProd.selectedGinCrate != null){
			if(actCrateProd.crateProductMode == actCrateProd.CRATEPROD_MODE){
				populateExistCrateProd();
			}
			else if(actCrateProd.crateProductMode == actCrateProd.UNALLOCATED_PROD_MODE){
				populateUnallocatedProd();
			}
			setControls(true);
		}
		else
			setControls(false);
			
	}
	
 	private void populateExistCrateProd(){
		//TODO populate select crate
		if(actCrateProd.selectedGinCrateProduct != null){
			etwCrateNo.setText(actCrateProd.selectedGinCrateProduct.getCrateNo().toUpperCase());
			etwItemCode.setText(actCrateProd.selectedGinCrateProduct.getItemCode().toUpperCase());
			etwProdCode.setText(actCrateProd.selectedGinCrateProduct.getProductCode().toUpperCase());		
			etwUOM.setText(actCrateProd.selectedGinCrateProduct.getUOM().toUpperCase());
			etwActQty.setText(Integer.toString(actCrateProd.selectedGinCrateProduct.getActQty()));
		}
		btnDelete.setEnabled(true);
	}
	private void populateUnallocatedProd(){
		//TODO populate select crate
		if(actCrateProd.unAllocatedProd !=null){
			etwCrateNo.setText(actCrateProd.selectedGinCrate.getCrateNo().toUpperCase());
			etwItemCode.setText(actCrateProd.unAllocatedProd.getItemCode().toUpperCase());
			etwProdCode.setText(actCrateProd.unAllocatedProd.getProductCode().toUpperCase());		
			etwUOM.setText(actCrateProd.unAllocatedProd.getUOM().toUpperCase());
			etwActQty.setText(Integer.toString(actCrateProd.unAllocatedProd.getActQty()));
		}
		btnDelete.setEnabled(false);
	}
	private void creatCrateProObj(){
		newQty = Integer.parseInt(etwActQty.getText().toString());
		if(actCrateProd.crateProductMode == actCrateProd.UNALLOCATED_PROD_MODE){
			actCrateProd.selectedGinCrateProduct = new GinCrateProduct();
			actCrateProd.selectedGinCrateProduct.setTransactionNo(actCrateProd.selectedGinCrate.getTransactionNo());
			actCrateProd.selectedGinCrateProduct.setCrateNo(etwCrateNo.getText().toString().toUpperCase());
			actCrateProd.selectedGinCrateProduct.setItemCode(etwItemCode.getText().toString().toUpperCase());
			actCrateProd.selectedGinCrateProduct.setProductCode(etwProdCode.getText().toString().toUpperCase());
			actCrateProd.selectedGinCrateProduct.setUOM(etwUOM.getText().toString().toUpperCase());
		}
	}

	private void clearFields(){
		etwCrateNo.setText("");
		etwItemCode.setText("");
		etwProdCode.setText("");	
		etwUOM.setText("");
		etwActQty.setText("0");	
	}
	private void setControls(boolean set){
		etwCrateNo.setEnabled(!set);
		etwItemCode.setEnabled(!set);
		etwProdCode.setEnabled(!set);	
		etwUOM.setEnabled(!set);		
		etwActQty.setEnabled(set);	
		etwCrateNo.setBackgroundColor(Color.parseColor("#66cdaa"));
		etwItemCode.setBackgroundColor(Color.parseColor("#66cdaa"));
		etwProdCode.setBackgroundColor(Color.parseColor("#66cdaa"));
		etwUOM.setBackgroundColor(Color.parseColor("#66cdaa"));
		etwActQty.setBackgroundColor(Color.parseColor("#ffffff"));
		etwActQty.requestFocus();
	}
	
	public void refreshAdapter()
	 {
		if(actCrateProd.selectedGinCrate != null)
			ginCrateContentsAdpt = new GINCrateContentsAdapter(getActivity(), R.layout.row_cratedetail, actCrateProd.selectedGinCrate.crateProducts);
		else
			ginCrateContentsAdpt = new GINCrateContentsAdapter(getActivity(), R.layout.row_cratedetail, new ArrayList<GinCrateProduct>());
		
		lvCrateContents.setAdapter(ginCrateContentsAdpt);
		
		//temp get unallocated products
		ArrayList<GinDetail> unAllocProds = getUnallocatedProd();
		ginUnallocProdAdpt =new GINUnAllocatedProdAdapter(getActivity(), R.layout.row_cratedetail, unAllocProds );
		lvUnallocProds.setAdapter(ginUnallocProdAdpt); 	
		
		crateProdSelectedRow = ginCrateContentsAdpt.getCount() + 1;
		unAllocProdSelectedRow = ginUnallocProdAdpt.getCount() + 1;
		ginCrateContentsAdpt.notifyDataSetChanged();
		ginUnallocProdAdpt.notifyDataSetChanged();

	 }
	
	private ArrayList<GinDetail> getUnallocatedProd(){
		ArrayList<GinDetail> unAllocProds = new ArrayList<GinDetail>();
		try {
			unAllocProds = actCrateProd.whApp.getGinHeader().getUnAllocatedGinDetails();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return unAllocProds;
	}
	private OnClickListener btnClicked = new OnClickListener(){
		public void onClick(View btn) {
			// TODO Auto-generated method stub
			try {
				creatCrateProObj();				
				if(btn == btnSave){
					//TODO edit product qty inside the crate
					if(actCrateProd.crateProductMode == actCrateProd.CRATEPROD_MODE) {
						if(actCrateProd.whApp.getGinHeader().validateEditCrateProductQty(getActivity(), actCrateProd.selectedGinCrateProduct, newQty)){	
						
							actCrateProd.selectedGinCrateProduct.setActQty(newQty);
							actCrateProd.selectedGinCrate.updateCrateProd(getActivity(), actCrateProd.selectedGinCrateProduct);					
						}
					}
					//TODO add new product into the crate
					else if(actCrateProd.crateProductMode == actCrateProd.UNALLOCATED_PROD_MODE){
						if(actCrateProd.whApp.getGinHeader().validateAddCrateProductQtyFromUnAllocatedDetail(getActivity(), actCrateProd.selectedGinCrateProduct, newQty)){
						
							actCrateProd.selectedGinCrateProduct.setActQty(newQty);
							actCrateProd.selectedGinCrate.insertCrateProd(getActivity(), actCrateProd.selectedGinCrateProduct);	
							
						}
					}
					btnSave.setEnabled(false);
				}
				if(btn == btnDelete){
					//TODO delete crate and all product inside
					if(actCrateProd.selectedGinCrateProduct != null){
						actCrateProd.selectedGinCrate.deleteCrateProd(getActivity(), actCrateProd.selectedGinCrateProduct);
						btnSave.setEnabled(false);
						btnDelete.setEnabled(false);
						btnCancel.setEnabled(false);
					}
				}
				if(btn == btnCancel){
					//TODO cancel
					clearFields();
					setControls(true);
					etwActQty.setEnabled(false);
					btnSave.setEnabled(false);
					btnDelete.setEnabled(false);
				}
			} catch (WhAppException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				showSuccessErrorDialog(getString(R.string.msgError), e.getMessage().toString().replace("java.lang.Exception:", ""));
				if(actCrateProd.crateProductMode == actCrateProd.CRATEPROD_MODE) 
					etwActQty.setText(Integer.toString(actCrateProd.selectedGinCrateProduct.getActQty()));
				else if(actCrateProd.crateProductMode == actCrateProd.UNALLOCATED_PROD_MODE)
					etwActQty.setText(Integer.toString(actCrateProd.unAllocatedProd.getActQty()));
			}
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				showSuccessErrorDialog(getString(R.string.msgError), e.getMessage().toString().replace("java.lang.Exception:", ""));
				if(actCrateProd.crateProductMode == actCrateProd.CRATEPROD_MODE) 
					etwActQty.setText(Integer.toString(actCrateProd.selectedGinCrateProduct.getActQty()));
				else if(actCrateProd.crateProductMode == actCrateProd.UNALLOCATED_PROD_MODE)
					etwActQty.setText(Integer.toString(actCrateProd.unAllocatedProd.getActQty()));
			}
			refreshAdapter();
		}		
	};
	private OnItemClickListener itemCrateProdClicked = new OnItemClickListener(){
		public void onItemClick(AdapterView<?> adapter, View view,
				int position, long arg3) {					
			try{		
				crateProdSelectedRow = position;
				ginCrateContentsAdpt.notifyDataSetChanged();
				unAllocProdSelectedRow = ginUnallocProdAdpt.getCount() + 1;
				ginUnallocProdAdpt.notifyDataSetChanged();
				//select from crate product list
				actCrateProd.selectedGinCrateProduct = actCrateProd.selectedGinCrate.crateProducts.get(position);				
				actCrateProd.crateProductMode = actCrateProd.CRATEPROD_MODE;
				populateData();
				btnSave.setEnabled(true);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}				
	};	
	private OnItemClickListener itemUnAllocProdClicked = new OnItemClickListener(){
		public void onItemClick(AdapterView<?> adapter, View view,
				int position, long arg3) {						
			try{	
				unAllocProdSelectedRow = position;
				ginUnallocProdAdpt.notifyDataSetChanged();
				crateProdSelectedRow = ginCrateContentsAdpt.getCount() + 1;
				ginCrateContentsAdpt.notifyDataSetChanged();
				//select from unallocated product list
				actCrateProd.crateProductMode = actCrateProd.UNALLOCATED_PROD_MODE;
				actCrateProd.unAllocatedProd = actCrateProd.whApp.getGinHeader().getUnAllocatedGinDetails().get(position);
				populateData();				
				btnSave.setEnabled(true);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}				
	};
	
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
	public class GINCrateContentsAdapter extends ArrayAdapter<GinCrateProduct> {
		 ArrayList<GinCrateProduct> mGinCrateProdList;

	    	public GINCrateContentsAdapter(Context context, int textViewResourceId,
	    			ArrayList<GinCrateProduct> ginCrateProdList) {
	             super(context, textViewResourceId, ginCrateProdList);
	             mGinCrateProdList = ginCrateProdList;
	 
	         }
	    	
			public View getView(int position, View convertView, ViewGroup parent) {
	              View v = convertView;
	              if (v == null) {
	                  LayoutInflater vi = (LayoutInflater)actCrateProd.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	                  v = vi.inflate(R.layout.row_cratecontents, null);     
	              }
	              GinCrateProduct item = mGinCrateProdList.get(position);
	              if(position ==  crateProdSelectedRow){
	            	  v.setBackgroundColor(Color.parseColor(getString(R.color.listView_selectedRowBgColor)));
	              }
	              else{
	            	  v.setBackgroundColor(Color.TRANSPARENT);	            	  
	              }
	              ///*
	              if (item != null) {
	            	  ListViewItems crateNoView = (ListViewItems) v.findViewById(R.id.row_crateNo);
	            	  ListViewItems itemCodeView = (ListViewItems) v.findViewById(R.id.row_crateItemCode);
	            	  ListViewItems prodCodeView = (ListViewItems) v.findViewById(R.id.row_crateProdCode);
	            	  ListViewItems prodDescView = (ListViewItems) v.findViewById(R.id.row_crateProdDesc);
	            	  ListViewItems uomView = (ListViewItems) v.findViewById(R.id.row_crateUOM);
	            	  ListViewItems actQtyView = (ListViewItems) v.findViewById(R.id.row_crateQty);
	            	  
	                  if(crateNoView != null){
	                	  crateNoView.setText(item.getCrateNo());
	                  }
	                  if(itemCodeView != null){
	                	  itemCodeView.setText(item.getItemCode());
	                  }
	                  if(prodCodeView != null){
	                	  prodCodeView.setText(item.getProductCode());
	                  }	  
	                  if(prodDescView != null){
	                	  prodDescView.setText(actCrateProd.whApp.getGinHeader().getProductDesc(item.getProductCode().trim()));
	                  }
	                  if(uomView != null){
	                	  uomView.setText(item.getUOM());
	                  }
	                  if(actQtyView != null){
	                	  actQtyView.setText(Integer.toString(item.getActQty()));
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
			public GinCrateProduct getItem(int position){
				return mGinCrateProdList.get(position);
			}

			 
	    }

	public class GINUnAllocatedProdAdapter extends ArrayAdapter<GinDetail> {
		 ArrayList<GinDetail> mGinUnAllocProdList;

	    	public GINUnAllocatedProdAdapter(Context context, int textViewResourceId,
	    			ArrayList<GinDetail> ginUnAllocProdList) {
	             super(context, textViewResourceId, ginUnAllocProdList);
	             mGinUnAllocProdList = ginUnAllocProdList;
	 
	         }
	    	
			public View getView(int position, View convertView, ViewGroup parent) {
	              View v = convertView;
	              if (v == null) {

	                  LayoutInflater vi = (LayoutInflater)actCrateProd.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	                  v = vi.inflate(R.layout.row_cratecontents, null);  
	              }
	              GinDetail item = mGinUnAllocProdList.get(position);
	              if(position ==  unAllocProdSelectedRow){
	            	  v.setBackgroundColor(Color.parseColor(getString(R.color.listView_selectedRowBgColor)));
	              }
	              else{
	            	  v.setBackgroundColor(Color.TRANSPARENT);	            	  
	              }
	              ///*
	              if (item != null) {
	            	  ListViewItems crateNoView = (ListViewItems) v.findViewById(R.id.row_crateNo);
	            	  crateNoView.setVisibility(View.GONE);
	            	  ListViewItems itemCodeView = (ListViewItems) v.findViewById(R.id.row_crateItemCode);
	            	  ListViewItems prodCodeView = (ListViewItems) v.findViewById(R.id.row_crateProdCode);
	            	  ListViewItems prodDescView = (ListViewItems) v.findViewById(R.id.row_crateProdDesc);
	            	  ListViewItems uomView = (ListViewItems) v.findViewById(R.id.row_crateUOM);
	            	  ListViewItems unAllocatedQtyView = (ListViewItems) v.findViewById(R.id.row_crateQty);
	            	  
	                  if(itemCodeView != null){
	                	  itemCodeView.setText(item.getItemCode());
	                  }
	                  if(prodCodeView != null){
	                	  prodCodeView.setText(item.getProductCode());
	                  }	  
	                  if(prodDescView != null){
	                	  prodDescView.setText(item.getProductDescription().trim());
	                  }
	                  if(uomView != null){
	                	  uomView.setText(item.getUOM());
	                  }
	                  if(unAllocatedQtyView != null){
	                	  unAllocatedQtyView.setText(Integer.toString(item.getActQty()));
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
				return mGinUnAllocProdList.get(position);
			}

			 
	    }
}
