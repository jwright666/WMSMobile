package sg.com.innosys.wms.UI.GIN;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import sg.com.innosys.wms.BLL.Common.WhApp;
import sg.com.innosys.wms.BLL.Common.WhAppException;
import sg.com.innosys.wms.BLL.Common.WhEnum.GINPickSortOption;
import sg.com.innosys.wms.BLL.GIN.GinDetail;
import sg.com.innosys.wms.BLL.GIN.GinPick;
import sg.com.innosys.wms.R;

public class DiaFragPickEntry extends DialogFragment{
	private final String SELECTEDGINPICK ="SELECTEDGINPICK";
	private final String MODE = "MODE";
	private View vDiaFragPickEntry; 
	private Dialog dgPickEntry;
	private Button btnSave;
	private Button btnNext;
	private Button btnCancel;
	private Button btnClose;
	private EditText etwProductCode;
	private TextView etwUOM;	
	private TextView etwProductDesc;
	private EditText etwBatchNo;
	private EditText etwLotNo;
	private EditText etwZone;
	private EditText etwRow;
	private EditText etwCol;
	private EditText etwTier;
	private EditText etwQty;	
	private TextView tvwProductCode;
	private TextView tvwBatchNo;
	private TextView tvwLotNo;
	private TextView tvwZone;
	private TextView tvwRow;
	private TextView tvwCol;
	private TextView tvwTier;
	private TextView tvwQty;	
	private CheckBox chkIsRacked;

	private ActGinPick actGinPick;
	private int mode;
	private GinPick selectedGinPick;
	
	
	static DiaFragPickEntry newInstance() {
		 DiaFragPickEntry f = new DiaFragPickEntry();
	        return f;	        
	    }

	public DiaFragPickEntry(){
		actGinPick = (ActGinPick) getActivity(); }
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		 super.onActivityCreated(savedInstanceState);
		
		 actGinPick = (ActGinPick) getActivity();  
		 DiaFragPickEntry.newInstance();	
		 
		 etwProductCode.setOnFocusChangeListener(etvOnFocusChanged);
		 etwBatchNo.setOnFocusChangeListener(etvOnFocusChanged);
		 etwLotNo.setOnFocusChangeListener(etvOnFocusChanged);
		 etwZone.setOnFocusChangeListener(etvOnFocusChanged);
		 etwRow.setOnFocusChangeListener(etvOnFocusChanged);
		 etwCol.setOnFocusChangeListener(etvOnFocusChanged);
		 etwTier.setOnFocusChangeListener(etvOnFocusChanged);
		 etwQty.setOnFocusChangeListener(etvOnFocusChanged);
		 
		 btnSave.setOnClickListener(btnOnClickListener);
		 btnNext.setOnClickListener(btnOnClickListener);
		 btnClose.setOnClickListener(btnOnClickListener);
		 btnCancel.setOnClickListener(btnOnClickListener);
		 if(savedInstanceState != null){
			 mode = savedInstanceState.getInt(MODE);
			 selectedGinPick =(GinPick) savedInstanceState.getSerializable(SELECTEDGINPICK);
		}
	    if(actGinPick.whApp.getGINPickSortOption() == GINPickSortOption.SORT_BY_LOCATION){
	    	btnNext.setVisibility(View.VISIBLE);
	    }
	    else{
	    	btnNext.setVisibility(View.GONE);	    	
	    }
        if(mode == WhApp.ADDMODE){
            tvwZone.setVisibility(View.GONE);
            tvwRow.setVisibility(View.GONE);
            tvwCol.setVisibility(View.GONE);
            tvwTier.setVisibility(View.GONE);
            tvwQty.setVisibility(View.GONE);
            chkIsRacked.setVisibility(View.VISIBLE);
            chkIsRacked.setOnClickListener(chkCheckListener);
        }
		 populateData();
		 getDialog().getWindow().setGravity(Gravity.RIGHT | Gravity.BOTTOM);
		 getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN); 		 
	 }
	
	private void showEnableRacked(boolean set){
		 etwRow.setEnabled(set);
		 etwCol.setEnabled(set);
		 etwTier.setEnabled(set);
		 if(set == false){
			 etwRow.setBackgroundColor(getResources().getColor(R.color.disabledTextedit_bgColor));
			 etwCol.setBackgroundColor(getResources().getColor(R.color.disabledTextedit_bgColor));
			 etwTier.setBackgroundColor(getResources().getColor(R.color.disabledTextedit_bgColor));
		 }
		 else{
			 etwRow.setBackgroundColor(getResources().getColor(R.color.OptionalTextedit_bgColor));
			 etwCol.setBackgroundColor(getResources().getColor(R.color.OptionalTextedit_bgColor));
			 etwTier.setBackgroundColor(getResources().getColor(R.color.OptionalTextedit_bgColor));
		 }
	}
	private OnClickListener chkCheckListener = new OnClickListener(){

		public void onClick(View v) {
			if (((CheckBox) v).isChecked()) {
				showEnableRacked(true);
			}			
			else{
				showEnableRacked(false);
				etwRow.setText("");
				 etwCol.setText("");
				 etwTier.setText("");
			}
		}		
	};
	private OnClickListener btnOnClickListener = new OnClickListener(){	    
		public void onClick(View btn) {
			 String title ="";
    		 try {
		    	 if(btn == btnSave){
		    		 //do save picking   
					 GinPick newGinPick = createGinPickobject();
					 String[] errMsg = new String[1];
					 //first validate Product, batch and lot
					 if(actGinPick.selectedGinDetail.validateProdBatchLot(getActivity(), etwProductCode.getText().toString()
								, etwBatchNo.getText().toString(), etwLotNo.getText().toString(),errMsg)){
						 
						 switch(mode){
							 case WhApp.EDITMODE:
								 if(isQtyValidForEdit(errMsg)){
		    						 newGinPick.setActQty(Integer.parseInt(etwQty.getText().toString()));	
			    					 actGinPick.selectedGinDetail.updateGinPick(newGinPick, getActivity());			    					 

			    					 actGinPick.selectedGinPick = newGinPick;
			    				 }		
			    				 else{
			    					 throw new Exception(errMsg[0].toString());
			    				 }	
		    					 //auto pull next picking or next item
								 if(actGinPick.whApp.getGINPickSortOption() == GINPickSortOption.SORT_BY_ITEMCODE){
									 getNextPickingAndDetail();								 
								 }
								 break;
							 case WhApp.ADDMODE:
								 if(isQtyValidForNewLocation(errMsg)){	
					    			 newGinPick.setActQty(Integer.parseInt(etwQty.getText().toString()));	
					    			 newGinPick.setIsNew(true);
					    			 newGinPick.setHasRack(chkIsRacked.isChecked() ? true : false);
					    			 actGinPick.selectedGinDetail.addGinPick(newGinPick, getActivity());
			    				 }	
			    				 else{
			    					 throw new Exception(errMsg[0].toString());
			    				 }
					    		 //dgPickEntry.dismiss();
								 break;
							 default:
								 break;
						 }				 
	    				 title = getString(R.string.msgAlert);	
	    				 showSuccessErrorDialog(title, getString(R.string.msgSavedSuccess));   
	    				 
	    				 //show warning if act qty less than server qty
	    				 //this code is placed here in order to show first before the ther dialog
						 if(errMsg[0] != null){
							 title = getString(R.string.msgWarning);	
		    				 showSuccessErrorDialog(title, errMsg[0]);	
						 }						
					 }
					 else{
						 throw new Exception(errMsg[0].toString());
					 }
		    	 }
		    	 else if(btn == btnNext){
		    		 actGinPick.whApp.getGinHeader().sortbyLocation();
					//TODO get next picking from list and populate
					if(actGinPick.pickingIndex < actGinPick.whApp.getGinHeader().headerGinPicks.size()){
						actGinPick.pickingIndex++;
						actGinPick.selectedGinPick = actGinPick.whApp.getGinHeader().getNextPreviousPicking(actGinPick.pickingIndex);
						actGinPick.selectedGinDetail = actGinPick.whApp.getGinHeader().getGinDetail(actGinPick.selectedGinPick.getItemCode());
						
						btnNext.setEnabled(actGinPick.pickingIndex == actGinPick.whApp.getGinHeader().headerGinPicks.size() - 1 ? false : true);
						populateData();
					}
		    	 }
		    	 else if(btn == btnClose){
		    		 dgPickEntry.dismiss();
		    	 }
		    	 else if(btn == btnCancel){
		    		 populateData();
		    	 }
		    	 refreshHeader();
    		 } 
    		 catch (Exception e) {
				 title = getString(R.string.msgError);
				 e.printStackTrace();
				 showSuccessErrorDialog(title, getString(R.string.msgSavedFailed)+"\n" +e.getLocalizedMessage());
				
    		 }
		 }
	};
	private void refreshHeader(){
		 FragGinProduct fragGinProduct = (FragGinProduct)getFragmentManager().findFragmentByTag("fragGinProduct");
		 fragGinProduct.onResume();
		// fragGinProduct.populateGinDetail();
	}
	private void getNextPickingAndDetail() throws WhAppException, Exception
	{
		 boolean lastPick = false;
		 boolean lastDetail = false;
		 try{
			 //scan navigation option	
			 if(actGinPick.selectedGinDetail.ginPicks.size() == 1){
				 lastPick =true;
			 }
			 else if(actGinPick.selectedGinDetail.ginPicks.size() > 1){
				 if(!lastPick){
					 //get next picking
					 selectedGinPick = actGinPick.selectedGinDetail.getNextGinPick(actGinPick.selectedGinPick,lastPick);
					 lastPick = actGinPick.selectedGinDetail.isLastPick(actGinPick.selectedGinPick);
					 if(selectedGinPick != null)
					 {
		    			 mode = WhApp.EDITMODE;	    			 
			    		 populateData();
			    		 etwProductCode.requestFocus();
		    			 btnSave.setEnabled(true);	
					 }
				 }
			 }	
			 switch(actGinPick.whApp.getGinHeader().getScanOption()){
				 case COMPLETE_PICKING_ALL_ITEMS_BEFORE_SERIALNO:
					 if(lastPick && !lastDetail){
						 GinDetail nextDetail = actGinPick.whApp.getGinHeader().getNextGinDetail(actGinPick.selectedGinDetail, lastDetail);
						 if(nextDetail != null){
							 actGinPick.selectedGinDetail = nextDetail;
							 selectedGinPick = actGinPick.selectedGinDetail.ginPicks.get(0);
							 populateData();
						 }
					 }
					 break;
				 case SERIALNO_EACH_PICKING:
					 if(lastPick && actGinPick.selectedGinDetail.getHasSerialNo() == true){
						 //scan serial numbers
						 actGinPick.bar.setSelectedNavigationItem(1);
					 }
					 //dgPickEntry.dismiss();
					 break;
				 default:
					 break;
			 }	
		 }
		 catch(Exception ex){
			 throw ex;
		 }
	}
  	private OnFocusChangeListener etvOnFocusChanged = new OnFocusChangeListener(){
		public void onFocusChange(View view, boolean hasFocus) {
			// TODO Auto-generated method stub
			try {
				if(!hasFocus){
					EditText v = (EditText) view;
					v.setTextColor(Color.BLACK);
					if(view == etwProductCode){
						if(etwProductCode.getText().toString().trim().length() != 0){
							actGinPick.selectedGinDetail.validateProduct(getActivity(), etwProductCode.getText().toString());
						}
					}
					else if(view == etwBatchNo){
						if(etwBatchNo.getText().toString().trim().length() != 0){					
							actGinPick.selectedGinDetail.validateBatch(getActivity(), etwBatchNo.getText().toString());
						}
					}
					else if(view == etwLotNo){
						if(etwLotNo.getText().toString().trim().length() != 0){	
							actGinPick.selectedGinDetail.validateLot(getActivity(), etwLotNo.getText().toString());
						}
					}
					if(mode ==WhApp.EDITMODE)
					{
						if(view == etwZone){
							if(etwZone.getText().toString().trim().length() != 0){	
								if(!selectedGinPick.getIsNew() && !etwZone.getText().toString().trim().equalsIgnoreCase(selectedGinPick.getZone().toString().trim())){
									throw new Exception(getString(R.string.errZoneNotFound));
								}		
							}
						}
						else if(view == etwRow){
							if(etwRow.getText().toString().trim().length() != 0){	
								if(!selectedGinPick.getIsNew() && !etwRow.getText().toString().trim().equalsIgnoreCase(selectedGinPick.getRow().toString().trim())){
									throw new Exception(getString(R.string.errRowNotFound));
								}	
							}
						}
						else if(view == etwCol){
							if(etwCol.getText().toString().trim().length() != 0){
								if(!selectedGinPick.getIsNew() && !etwCol.getText().toString().trim().equalsIgnoreCase(selectedGinPick.getColumn().toString().trim())){
									throw new Exception(getString(R.string.errColumnNotFound));
								}	
							}
						}
						else if(view == etwTier){
							if(etwTier.getText().toString().trim().length() != 0){
								if(!selectedGinPick.getIsNew() && !etwTier.getText().toString().trim().equalsIgnoreCase(selectedGinPick.getTier().toString().trim())){
									throw new Exception(getString(R.string.errTierNotFound));
								}
							}
						}
					}					
				}
			}
			catch (Exception e) {
				String title = getString(R.string.msgError);
				e.printStackTrace();
				showSuccessErrorDialog(title, e.toString().replace("java.lang.Exception: ", ""));	
				EditText v = (EditText) view;
				v.setTextColor(Color.RED);
			}
		}		
	};
	
	@Override 
	public void onDestroy(){
		 super.onDestroy();
	 }
	@Override 
	public void onResume(){
		 super.onResume();	
		 btnNext.setEnabled(actGinPick.pickingIndex == actGinPick.whApp.getGinHeader().headerGinPicks.size() - 1 ? false : true); 
	 }
    @Override
    public void onSaveInstanceState(Bundle outState){
    	super.onSaveInstanceState(outState);
    	outState.putInt(MODE,mode);
    	outState.putSerializable(SELECTEDGINPICK, selectedGinPick);
    }
	
	private View getContentView() {
        LayoutInflater inflater = getActivity().getLayoutInflater();  
        vDiaFragPickEntry =	inflater.inflate(R.layout.frag_pickentry, null);
        btnSave =(Button) vDiaFragPickEntry.findViewById(R.id.btnSavePickEntry);
        btnCancel =(Button) vDiaFragPickEntry.findViewById(R.id.btnCancelPickEntry);
        btnNext =(Button) vDiaFragPickEntry.findViewById(R.id.btnNextPickEntry);
        btnClose =(Button) vDiaFragPickEntry.findViewById(R.id.btnClosePickEntry);
        etwProductCode = (EditText)vDiaFragPickEntry.findViewById(R.id.etwPick_ProductCode);
        etwUOM = (TextView)vDiaFragPickEntry.findViewById(R.id.etwPick_UOM);
        etwProductDesc = (TextView)vDiaFragPickEntry.findViewById(R.id.etwPick_ProductDesc);
        etwBatchNo = (EditText)vDiaFragPickEntry.findViewById(R.id.etwPick_Batch);
        etwLotNo = (EditText)vDiaFragPickEntry.findViewById(R.id.etwPick_Lot);
        etwZone = (EditText)vDiaFragPickEntry.findViewById(R.id.etwPick_Zone);
        etwRow = (EditText)vDiaFragPickEntry.findViewById(R.id.etwPick_Row);
        etwCol = (EditText)vDiaFragPickEntry.findViewById(R.id.etwPick_Column);
        etwTier = (EditText)vDiaFragPickEntry.findViewById(R.id.etwPick_Tier);
        etwQty = (EditText)vDiaFragPickEntry.findViewById(R.id.etwPick_Qty);

        tvwProductCode = (TextView)vDiaFragPickEntry.findViewById(R.id.tvvPick_ProductCode1);
        tvwBatchNo = (TextView)vDiaFragPickEntry.findViewById(R.id.tvvPick_Batch1);
        tvwLotNo = (TextView)vDiaFragPickEntry.findViewById(R.id.tvvPick_Lot1);
        tvwZone = (TextView)vDiaFragPickEntry.findViewById(R.id.tvvPick_Zone1);
        tvwRow = (TextView)vDiaFragPickEntry.findViewById(R.id.tvvPick_Row1);
        tvwCol = (TextView)vDiaFragPickEntry.findViewById(R.id.tvvPick_Column1);
        tvwTier = (TextView)vDiaFragPickEntry.findViewById(R.id.tvvPick_Tier1);
        tvwQty = (TextView)vDiaFragPickEntry.findViewById(R.id.tvvPick_Qty1);
        chkIsRacked = (CheckBox)vDiaFragPickEntry.findViewById(R.id.chk_IsRack);
        
        return vDiaFragPickEntry;
	}
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {		 
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.diaPickingEntry);
        builder.setView(getContentView());
        dgPickEntry = builder.create();
        dgPickEntry.setCanceledOnTouchOutside(false);
        return dgPickEntry;
    }
	private void enableEditableFields(boolean set){
		etwProductCode.setEnabled(set);		
        etwBatchNo.setEnabled(set);
        etwLotNo.setEnabled(set);
        etwZone.setEnabled(set);
        etwRow.setEnabled(set);
        etwCol.setEnabled(set);
        etwTier.setEnabled(set);
        etwQty.setEnabled(set);
	}
	private void clearEditableFields(){
		etwProductCode.setText("");	
        etwBatchNo.setText("");	
        etwLotNo.setText("");	
        etwZone.setText("");	
        etwRow.setText("");	
        etwCol.setText("");	
        etwTier.setText("");	
        etwQty.setText("0");
	}
	private void populateData(){
		enableEditableFields(true);
		etwUOM.setText(actGinPick.selectedGinDetail.getUOM().toUpperCase());	
		etwProductDesc.setText(actGinPick.selectedGinDetail.getProductDescription().toUpperCase());			
		//int balQty = actGinPick.selectedGinDetail.getServerQty() - actGinPick.selectedGinDetail.getActQty();
		int balQty = 0;
		if(mode == WhApp.EDITMODE){
			balQty = actGinPick.selectedGinPick.getSrvQty() - actGinPick.selectedGinPick.getActQty();
			tvwZone.setText(selectedGinPick.getZone().toUpperCase());
			tvwRow.setText(selectedGinPick.getRow().toUpperCase());
			tvwCol.setText(selectedGinPick.getColumn().toUpperCase());
			tvwTier.setText(selectedGinPick.getTier().toUpperCase());
			tvwQty.setText(Integer.toString(selectedGinPick.getActQty()));	
	        //etwQty.setText(Integer.toString(selectedGinPick.getActQty()));		
		}
		else if(mode == WhApp.ADDMODE){
			selectedGinPick = new GinPick(); 
			balQty = actGinPick.selectedGinDetail.getServerQty() - actGinPick.selectedGinDetail.getActQty();
		}	
		tvwQty.setText(Integer.toString(balQty));
		tvwProductCode.setText(actGinPick.selectedGinDetail.getProductCode().toUpperCase());
		tvwBatchNo.setText(actGinPick.selectedGinDetail.getBatchNo().toUpperCase());
		tvwLotNo.setText(actGinPick.selectedGinDetail.getLotNo().toUpperCase());
		clearEditableFields();
		/*
		etwProductCode.setText(actGinPick.selectedGinDetail.getProductCode().toUpperCase());
		etwProductDesc.setText(actGinPick.selectedGinDetail.getProductDescription().toUpperCase());			
        etwBatchNo.setText(actGinPick.selectedGinDetail.getBatchNo().toUpperCase());
        etwLotNo.setText(actGinPick.selectedGinDetail.getLotNo().toUpperCase());
        etwZone.setText(selectedGinPick.getZone().toUpperCase());
        etwRow.setText(selectedGinPick.getRow().toUpperCase());
        etwCol.setText(selectedGinPick.getColumn().toUpperCase());
        etwTier.setText(selectedGinPick.getTier().toUpperCase());	
        */
		etwProductCode.requestFocus();
	}
	private GinPick createGinPickobject() throws Exception{		
		GinPick newGinPick = new GinPick();
		newGinPick.setTransactionNo(actGinPick.selectedGinDetail.getTransactionNo());
		//newGinPick.setSeqNo(actGinPick.selectedGinDetail.getGinPickNextSeqNo(actGinPick));		
		newGinPick.setItemCode(actGinPick.selectedGinDetail.getItemCode());
		newGinPick.setProductCode(etwProductCode.getText().toString().toUpperCase());
		newGinPick.setZone(etwZone.getText().toString().toUpperCase());
		newGinPick.setRow(etwRow.getText().toString().toUpperCase());
		newGinPick.setColumn(etwCol.getText().toString().toUpperCase());
		newGinPick.setTier(etwTier.getText().toString().toUpperCase());
		//newGinPick.setActQty(Integer.parseInt(etwQty.getText().toString()));
		if(mode == WhApp.ADDMODE){
			newGinPick.setActQty(0);
			newGinPick.setSrvQty(0);
			//newGinPick.setSeqNo(actGinPick.selectedGinDetail.getGinPickNextSeqNo(getActivity()));				
			newGinPick.setIsNew(true);		
			if(actGinPick.selectedGinDetail.ginPicks.size() > 0)
				newGinPick.setHasRack(actGinPick.selectedGinDetail.ginPicks.get(0).getHasRack());
		}
		else{
			newGinPick.setActQty(selectedGinPick.getActQty());
			newGinPick.setSrvQty(selectedGinPick.getSrvQty());
			newGinPick.setSeqNo(selectedGinPick.getSeqNo());	
			newGinPick.setIsNew(selectedGinPick.getIsNew());			
		}
		return newGinPick;
	}

	private Boolean isQtyValidForNewLocation(String[] errMsg){
		boolean retValue = true;
		errMsg[0] = null;
		int newQty = Integer.parseInt(etwQty.getText().toString());
		int balQty = actGinPick.selectedGinDetail.getServerQty() - actGinPick.selectedGinDetail.getActQty();
		if(newQty > balQty){			
			errMsg[0]= getString(R.string.errExceedActQty)+ "\n" + getString(R.string.msgWarnBalanceQty) + " "+Integer.toString(balQty);
			retValue = false;
		}
		return retValue;
	}
	private Boolean isQtyValidForEdit(String[] errMsg){
		boolean retValue = true;
		errMsg[0] = null;
		int newQty = Integer.parseInt(etwQty.getText().toString());
		int avlBalQty =  actGinPick.selectedGinDetail.getServerQty() - (actGinPick.selectedGinDetail.getActQty() - selectedGinPick.getActQty());
		if(newQty > avlBalQty){
			errMsg[0]= getString(R.string.errExceedActQty);
			retValue = false;
		}
		else{
			if(!selectedGinPick.getIsNew()){
				if(newQty > selectedGinPick.getSrvQty()){			
					errMsg[0]= getString(R.string.errExceedActQty);
					retValue = false;
				}
				if(newQty < selectedGinPick.getSrvQty()){
					errMsg[0] = getString(R.string.msgWarnNotEqualQty);
					retValue = true;
				}
			}			
		}
		return retValue;
	}



	private Dialog showSuccessErrorDialog(final String title, final String msg){
		    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		    builder.setMessage(msg)
		    	   .setTitle(title)
		           .setCancelable(false);
		    AlertDialog alert = builder.create();
		    alert.setButton("OK", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {	
	            	   if(msg.equalsIgnoreCase(getString(R.string.msgSavedSuccess))){
	            		   actGinPick.bar.setSelectedNavigationItem(1);
	            		   dgPickEntry.dismiss();
	            	   	}
	               }
	               });
		        alert.show();	        
				return alert;
	}


}

