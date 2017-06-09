package sg.com.innosys.wms.UI.GIN;

import android.annotation.SuppressLint;
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

import java.util.Locale;
import java.util.StringTokenizer;

import sg.com.innosys.wms.BLL.Common.WhApp;
import sg.com.innosys.wms.BLL.Common.WhAppException;
import sg.com.innosys.wms.BLL.GIN.GinDetail;
import sg.com.innosys.wms.BLL.GIN.GinPick;
import sg.com.innosys.wms.R;

@SuppressLint({ "DefaultLocale", "NewApi" }) ////20131230 system generated
public class DiaFragPickEntry_ScanMultiFields extends DialogFragment{
	private final String BLANKLOCATION ="-";
	private final String SELECTEDGINPICK ="SELECTEDGINPICK";
	private final String MODE = "MODE";
	private View vDiaFragPickEntry; 
	private Dialog dgPickEntry;
	private Button btnSave;
	//private Button btnNext;
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
	private EditText etwLocation;	
	private TextView tvwLocation;
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
	private static int mode;
	private static GinPick selectedGinPick;
	static DiaFragPickEntry_ScanMultiFields newInstance(int mode2, GinPick selectedGinPick2) {
		mode = mode2;
		selectedGinPick = selectedGinPick2;
		DiaFragPickEntry_ScanMultiFields f = new DiaFragPickEntry_ScanMultiFields();
	        return f;	        
	    }

	public DiaFragPickEntry_ScanMultiFields(){
	}


	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		 super.onActivityCreated(savedInstanceState);
		
		 actGinPick = (ActGinPick) getActivity();  
		 DiaFragPickEntry_ScanMultiFields.newInstance(mode, selectedGinPick);		
		 
		 etwProductCode.setOnFocusChangeListener(etvOnFocusChanged);
		 etwBatchNo.setOnFocusChangeListener(etvOnFocusChanged);
		 etwLotNo.setOnFocusChangeListener(etvOnFocusChanged);
		 etwZone.setOnFocusChangeListener(etvOnFocusChanged);
		 etwRow.setOnFocusChangeListener(etvOnFocusChanged);
		 etwCol.setOnFocusChangeListener(etvOnFocusChanged);
		 etwTier.setOnFocusChangeListener(etvOnFocusChanged);
		 etwQty.setOnFocusChangeListener(etvOnFocusChanged);
		 etwLocation.setOnFocusChangeListener(etvOnFocusChanged);
		 
		 btnSave.setOnClickListener(btnOnClickListener);
		 //btnNext.setOnClickListener(btnOnClickListener);
		 btnClose.setOnClickListener(btnOnClickListener);
		 btnCancel.setOnClickListener(btnOnClickListener);
		 if(savedInstanceState != null){
			 mode = savedInstanceState.getInt(MODE);
			 selectedGinPick =(GinPick) savedInstanceState.getSerializable(SELECTEDGINPICK);
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
				tvwLocation.setVisibility(View.VISIBLE); //20131230 Gerry added
				etwLocation.setVisibility(View.VISIBLE); //20131230 Gerry added
			}			
			else{
				showEnableRacked(false);
				etwRow.setText(BLANKLOCATION);
				etwCol.setText(BLANKLOCATION);
				etwTier.setText(BLANKLOCATION);
				tvwLocation.setVisibility(View.GONE); //20131230 Gerry added
				etwLocation.setVisibility(View.GONE); //20131230 Gerry added
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
			    					 throw new WhAppException(errMsg[0].toString());
			    				 }	
		    					 //auto pull next picking or next item
								  getNextPickingAndDetail();								 
								 break;
							 case WhApp.ADDMODE:
								 if(isQtyValidForNewLocation(errMsg)){	
					    			 newGinPick.setActQty(Integer.parseInt(etwQty.getText().toString()));	
					    			 newGinPick.setIsNew(true);
					    			 actGinPick.selectedGinDetail.addGinPick(newGinPick, getActivity());
			    				 }	
			    				 else{
			    					 throw new WhAppException(errMsg[0].toString());
			    				 }
					    		 //dgPickEntry.dismiss();
								 break;
							 default:
								 break;
						 }				 
						 
			    		 //actGinPick.ginHeader.updateGinDetailFromMemory(actGinPick.selectedGinDetail);
			    		 FragGinProduct fragGinProduct = (FragGinProduct)getFragmentManager().findFragmentByTag("fragGinProduct");
			    		 fragGinProduct.onResume();
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
						 throw new WhAppException(errMsg[0].toString());
					 }
		    	 }
		    	 else if(btn == btnClose){
		    		 dgPickEntry.dismiss();
		    	 }
		    	 else if(btn == btnCancel){
		    		 populateData();
		    	 }
    		 } 
		   	catch (WhAppException ex){
				 title = getString(R.string.msgError);
				 ex.printStackTrace();
				 showSuccessErrorDialog(title, getString(R.string.msgSavedFailed)+"\n" +ex.getLocalizedMessage());
		   	}
    		 catch (Exception e) {
				 title = getString(R.string.msgError);
				 e.printStackTrace();
				 showSuccessErrorDialog(title, getString(R.string.msgSavedFailed)+"\n" +e.getLocalizedMessage());
				
    		 }
		 }
	};
	private void getNextPickingAndDetail()throws WhAppException
	{
		 boolean lastPick = false;
		 boolean lastDetail = false;
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
						 //20140403 - gerry added - for case when next item has no picking
						 if(actGinPick.selectedGinDetail.ginPicks.size() >0){
							 selectedGinPick = actGinPick.selectedGinDetail.ginPicks.get(0);

							 populateData();
						 }
						 else{
							 dgPickEntry.dismiss();
						 }
						 //20140403 end
					 }
				 }
				 break;
			 case SERIALNO_EACH_PICKING:
				 if(lastPick && actGinPick.selectedGinDetail.getHasSerialNo() == true){
					 //scan serial numbers
					 actGinPick.bar.setSelectedNavigationItem(1);
				 }
				 dgPickEntry.dismiss();
				 break;
			 default:
				 break;
		 }		
	}
  	private OnFocusChangeListener etvOnFocusChanged = new OnFocusChangeListener(){
		public void onFocusChange(View view, boolean hasFocus) {
			// TODO Auto-generated method stub
			try {
				if(!hasFocus){
					EditText v = (EditText) view;
					v.setTextColor(Color.BLACK);		
					if(v == etwLocation){
						String scannedLocation = v.getText().toString();						
						displayMultiFields(scannedLocation, actGinPick.whApp.getWhMobileSettings().getLocSeparator());	
						//etwQty.requestFocus();//20140405 - gerry added to focus the qty field
					}		
				}
			}
			catch(NullPointerException ex){
				ex.printStackTrace();
				showSuccessErrorDialog(getString(R.string.msgError), ex.toString());	
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
	
	  
    @SuppressLint("DefaultLocale")
    private void displayMultiFields(String scannedLocation, String separator){
    	if(scannedLocation.length() > 0){   
    		
    		StringTokenizer tokens = new StringTokenizer(scannedLocation, separator);
    		
    		//compositeField = scannedLocation.trim().split(separator.toString()); // initialize the 4 fields(Zone, Row, Column, Tier) to be blank
    		etwZone.setText(tokens.nextToken().toString().trim().toUpperCase(Locale.getDefault()));
    		//if(selectedGinPick.getHasRack() == true){
     		switch(tokens.countTokens()){	     		
	    		case 1:// 	    	
	    			etwRow.setText(tokens.nextToken().toString().trim().toUpperCase(Locale.getDefault()));
	    			etwCol.setText(BLANKLOCATION); //put "-" for blank column
	    			etwTier.setText(BLANKLOCATION);//put "-" for blank tier
	    			break;
	    		case 2: 	    	
	    			etwRow.setText(tokens.nextToken().toString().trim().toUpperCase(Locale.getDefault()));
	    			etwCol.setText(tokens.nextToken().toString().trim().toUpperCase(Locale.getDefault()));
	    			etwTier.setText(BLANKLOCATION);//put "-" for blank tier
	    			break;
	    		case 3: 	    	
	    			etwRow.setText(tokens.nextToken().toString().trim().toUpperCase(Locale.getDefault()));
	    			etwCol.setText(tokens.nextToken().toString().trim().toUpperCase(Locale.getDefault()));
	    			etwTier.setText(tokens.nextToken().toString().trim().toUpperCase(Locale.getDefault()));
	    			break;	    			
	    		default://20140405 - gerry added to put " - " if only zone is keyed in
	    			etwRow.setText(BLANKLOCATION);//put "-" for blank column
	    			etwCol.setText(BLANKLOCATION);//put "-" for blank column
	    			etwTier.setText(BLANKLOCATION);//put "-" for blank column
	    			break;    			
     		}	
     		//}
    		btnSave.requestFocus();	
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
        if(mode == WhApp.ADDMODE){
            tvwZone.setVisibility(View.GONE);
            tvwRow.setVisibility(View.GONE);
            tvwCol.setVisibility(View.GONE);
            tvwTier.setVisibility(View.GONE);
            tvwQty.setVisibility(View.GONE);
            chkIsRacked.setVisibility(View.VISIBLE);
            chkIsRacked.setOnClickListener(chkCheckListener);
        }
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
       // btnNext =(Button) vDiaFragPickEntry.findViewById(R.id.btnNextPickEntry);
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

        tvwLocation = (TextView)vDiaFragPickEntry.findViewById(R.id.tvvPick_Location);
        etwLocation = (EditText)vDiaFragPickEntry.findViewById(R.id.etwPick_Location);
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
        etwLocation.setEnabled(set);
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
        etwLocation.setText("");	
        etwZone.setText("");	
        etwRow.setText("");	
        etwCol.setText("");	
        etwTier.setText("");	
        etwQty.setText("0");
	}
	private void populateData(){
		enableEditableFields(true);
		clearEditableFields();
		etwUOM.setText(actGinPick.selectedGinDetail.getUOM().toUpperCase());	
		etwProductDesc.setText(actGinPick.selectedGinDetail.getProductDescription().toUpperCase());	
		//20140421 - gerry replaced codes below
		//int balQty = actGinPick.selectedGinDetail.getServerQty() - actGinPick.selectedGinDetail.getActQty();		
		int balQty  = actGinPick.selectedGinDetail.getServerQty() - (actGinPick.selectedGinDetail.getActQty() - selectedGinPick.getActQty());
		
		if(mode == WhApp.EDITMODE){
			tvwZone.setText(selectedGinPick.getZone().toUpperCase());
			tvwRow.setText(selectedGinPick.getRow().toUpperCase());
			tvwCol.setText(selectedGinPick.getColumn().toUpperCase());
			tvwTier.setText(selectedGinPick.getTier().toUpperCase());
			//20140421 - gerry added
			if(selectedGinPick.getActQty() > 0){				
				balQty = selectedGinPick.getActQty();				
			}				
			if(balQty >= selectedGinPick.getSrvQty()){
				balQty = selectedGinPick.getSrvQty();
			}//20140421 end
		}
		else if(mode == WhApp.ADDMODE){
			selectedGinPick = new GinPick(); 
			//20140421 - gerry added
			balQty = actGinPick.selectedGinDetail.getServerQty() - actGinPick.selectedGinDetail.getActQty();
			//20140421 end
		}	
		
		tvwQty.setText(Integer.toString(balQty));
        etwQty.setText(Integer.toString(balQty));	//20140421 gerry added
		tvwProductCode.setText(actGinPick.selectedGinDetail.getProductCode().toUpperCase());
		tvwBatchNo.setText(actGinPick.selectedGinDetail.getBatchNo().toUpperCase());
		tvwLotNo.setText(actGinPick.selectedGinDetail.getLotNo().toUpperCase());
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
			//if(actGinPick.selectedGinDetail.ginPicks.size() > 0)
			//	newGinPick.setHasRack(actGinPick.selectedGinDetail.ginPicks.get(0).getHasRack());
			
			//20140402 - gerry added to set has Racked
			newGinPick.setHasRack(chkIsRacked.isChecked());					    			 
			//20140402 end
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
		if(etwZone.getText().toString().length() <= 0){			
			errMsg[0]= getString(R.string.errEmptyZone);
			retValue = false;
		}
		int newQty = Integer.parseInt(etwQty.getText().toString());
		int balQty = actGinPick.selectedGinDetail.getServerQty() - actGinPick.selectedGinDetail.getActQty();
		if(newQty > balQty){			
			errMsg[0]= getString(R.string.errExceedActQty)+ "\n" + getString(R.string.msgWarnBalanceQty) + " "+Integer.toString(balQty);
			retValue = false;
		}
		return retValue;
	}
	@SuppressLint("ShowToast")
	private Boolean isQtyValidForEdit(String[] errMsg){
		boolean retValue = true;
		errMsg[0] = null;
		int newQty = Integer.parseInt(etwQty.getText().toString());
		//20140421 - gerry replaced the codes below
		int avlBalQty =  actGinPick.selectedGinDetail.getServerQty() - (actGinPick.selectedGinDetail.getActQty() - selectedGinPick.getActQty());
		//int avlBalQty =  selectedGinPick.getSrvQty();
		//20140421 end
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


}

