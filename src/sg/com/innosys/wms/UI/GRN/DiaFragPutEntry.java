package sg.com.innosys.wms.UI.GRN;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import sg.com.innosys.wms.BLL.Common.WhApp;
import sg.com.innosys.wms.BLL.Common.WhAppException;
import sg.com.innosys.wms.BLL.Common.WhEnum.ConfirmGrnType;
import sg.com.innosys.wms.BLL.GRN.GrnHeader;
import sg.com.innosys.wms.BLL.GRN.GrnPut;
import sg.com.innosys.wms.BLL.GRN.PalletProduct;
import sg.com.innosys.wms.R;

public class DiaFragPutEntry extends DialogFragment{

	private View vDiaFragPutEntry; 
	private Dialog dgPutEntry;
	private Button btnSave;
	private Button btnCancel;
	private Button btnScanSerialNo;
	private ActGrnPut actGrnPut;
	//private DbWhGRN dbWhGrn;
	private GrnPut grnPut;
	//private WhApp whApp;
	private EditText etwProductCode;
	private EditText etwBatchNo;
	private EditText etwLotNo;
	private EditText etwQty;
	//necessary fields
	//private String prodCode;
	//private String batchNo;
	//private String lotNo;
	private int newQty;
	
	private PalletProduct selectPalletProd;
	
	
	 public static DiaFragPutEntry newInstance(PalletProduct selectPalletProd) {
		 DiaFragPutEntry f = new DiaFragPutEntry(selectPalletProd);
	        return f;	        
	    }
	 public static DiaFragPutEntry newInstance() {
		 DiaFragPutEntry f = new DiaFragPutEntry();
	        return f;	        
	    }

	 public DiaFragPutEntry(){}
	 
	 public DiaFragPutEntry(PalletProduct selectPalletProd){
		 this.selectPalletProd = selectPalletProd;
	 }
	 
	 private OnClickListener btnOnClickListener = new OnClickListener(){
    
     public void onClick(View btn) {
		try{	
			setFocus();
			try{
				newQty = Integer.parseInt(((EditText) vDiaFragPutEntry.findViewById(R.id.etwPut_Quantity)).getText().toString().trim());
			}
			catch(Exception ex){}
			
			//prodCode = etwProductCode.getText().toString().toUpperCase();
			//batchNo = etwBatchNo.getText().toString().toUpperCase();
			//lotNo = etwLotNo.getText().toString().toUpperCase();
			//qty =0;
			
			createPalletProduct();
			//from temp GRNPut object for validation purpose only
			grnPut =new GrnPut();
			grnPut.setPalleteId(actGrnPut.palletId.toUpperCase());
			grnPut.setProductCode(actGrnPut.selectedPalletProd.getProductCode());
			grnPut.setActQty(newQty);	
			//end temp GRNPut object
			
			switch (btn.getId()){
			case R.id.btnSavePutEntry:
				//Error:
				//This we got problem the dialog box will not show
				//it will continue the next statement instead of showing the dialog
				//need to be fix
				if(grnPut.validateFields(getActivity())){
					String[] prodErr= new String[1];
					if(actGrnPut.whApp.getGrnHeader().isGrnDetailFound(getActivity(), actGrnPut.selectedPalletProd, prodErr)){
						String[] qtyMsg = new String[1];
						if(isQtyExceed(actGrnPut.whApp.getGrnHeader(), qtyMsg)){
							showWarningDialog(getString(R.string.msgWarning),"\n" +qtyMsg[0].toString());							
						}
						else{
							if(actGrnPut.PUTTING_MODE == WhApp.ADDMODE){
								insertGrnPut(false);				
							}
							else if(actGrnPut.PUTTING_MODE == WhApp.EDITMODE){
								updateGrnPut(false);
							}
							
							if(actGrnPut.selectedPalletProd.getHasSerialNo() && actGrnPut.whApp.getGrnHeader().getConfirmGrnType() == ConfirmGrnType.CONFIRM_GRN_AFTER_UPLOAD){
								actGrnPut.bar.setSelectedNavigationItem(3);
								dgPutEntry.dismiss();
							}
						}
					}	
					else
						throw new Exception(getString(R.string.msgSavedFailed) +"\n"+prodErr[0].toString());
					

					refreshGrnPutList();
				}	
				break;
			case R.id.btnCancelPutEntry:		
				refreshGrnPutList();
				dgPutEntry.dismiss();
				break;				
			}
		}
		catch(WhAppException ex){
			actGrnPut.selectedPalletProd = null;
			showSuccessErrorDialog(getString(R.string.msgError), ex.getLocalizedMessage());
		}
		catch(Exception ex){
			actGrnPut.selectedPalletProd = null;
			showSuccessErrorDialog(getString(R.string.msgError), ex.getLocalizedMessage());
		}
	 }
	 };	 
	 
	 @Override
	 public void onActivityCreated(Bundle savedInstanceState){
		 super.onActivityCreated(savedInstanceState);
		
		 actGrnPut = (ActGrnPut) getActivity(); 
		 //whApp = (WhApp) actGrnPut.getApplication();
		 //dbWhGrn = new DbWhGRN(actGrnPut); 
		 //dbWhGrn.open();
		 //always get the GRN header from application
		 //actGrnPut..grnHeader = whApp.getGrnHeader();		 
		 
		 DiaFragPutEntry.newInstance(selectPalletProd);
		 btnSave = (Button) vDiaFragPutEntry.findViewById(R.id.btnSavePutEntry);
		 btnCancel =(Button) vDiaFragPutEntry.findViewById(R.id.btnCancelPutEntry);
		 btnScanSerialNo =(Button) vDiaFragPutEntry.findViewById(R.id.btnScanSerialNo);
		 etwProductCode = (EditText)vDiaFragPutEntry.findViewById(R.id.etwPut_ProductCode);
         etwBatchNo = (EditText)vDiaFragPutEntry.findViewById(R.id.etwPut_Batch);
         etwLotNo = (EditText)vDiaFragPutEntry.findViewById(R.id.etwPut_Lot);
         etwQty = (EditText)vDiaFragPutEntry.findViewById(R.id.etwPut_Quantity);	
         
         
         
		 btnSave.setOnClickListener(btnOnClickListener);
		 btnCancel.setOnClickListener(btnOnClickListener);
		 btnScanSerialNo.setOnClickListener(btnOnClickListener);
		 if(savedInstanceState != null){
			actGrnPut.palletId =savedInstanceState.getString(ActGrnPut.PALLETID);
			actGrnPut.PUTTING_MODE =savedInstanceState.getInt(ActGrnPut.PUTTINGMODE);
			selectPalletProd =(PalletProduct) savedInstanceState.getSerializable("PALLETPRODUCT");
		}
		 initializeDisplay();
		 getDialog().getWindow().setGravity(Gravity.RIGHT | Gravity.BOTTOM);

	 }
	 @Override 
	 public void onDestroy(){
		 super.onDestroy();
	 }
	 @Override 
	 public void onResume(){
		 super.onResume();
		 actGrnPut = (ActGrnPut) getActivity(); 	 
	 }
	 
	 @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {		 
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.diaPuttingEntry);
        builder.setView(getContentView());
        dgPutEntry = builder.create();
        dgPutEntry.setCanceledOnTouchOutside(false);
        return dgPutEntry;
    }	 
    @Override
    public void onSaveInstanceState(Bundle outState){
    	super.onSaveInstanceState(outState);
    	outState.putString(ActGrnPut.PALLETID, actGrnPut.palletId);
    	outState.putInt(ActGrnPut.PUTTINGMODE, actGrnPut.PUTTING_MODE);
    	outState.putSerializable("PALLETPRODUCT", selectPalletProd);
    }
	//use this method instead of onCreateView because
    //we got error when using onCreateView method
	private View getContentView() {
        LayoutInflater inflater = getActivity().getLayoutInflater();  
        vDiaFragPutEntry =	inflater.inflate(R.layout.frag_putentry, null);
        btnSave =(Button) vDiaFragPutEntry.findViewById(R.id.btnSavePutEntry);
        btnCancel =(Button) vDiaFragPutEntry.findViewById(R.id.btnCancelPutEntry);
        btnScanSerialNo =(Button) vDiaFragPutEntry.findViewById(R.id.btnScanSerialNo);
        etwProductCode = (EditText)vDiaFragPutEntry.findViewById(R.id.etwPut_ProductCode);
        etwBatchNo = (EditText)vDiaFragPutEntry.findViewById(R.id.etwPut_Batch);
        etwLotNo = (EditText)vDiaFragPutEntry.findViewById(R.id.etwPut_Lot);
        etwQty = (EditText)vDiaFragPutEntry.findViewById(R.id.etwPut_Quantity);
        btnScanSerialNo.setVisibility(View.GONE);
        return vDiaFragPutEntry;
	 }	
	private void clearTextBoxes(){
		etwProductCode.setText("");
        etwBatchNo.setText("");
        etwLotNo.setText("");
        etwQty.setText("");
        etwProductCode.requestFocus();
	 }
    private void setFocus(){	
		String prodCode = etwProductCode.getText().toString();
		if(prodCode.equals("")){
			etwProductCode.requestFocus();
			etwProductCode.selectAll();
		}
		String qty = etwQty.getText().toString();
		if(qty == "0" || qty == ""){
			etwQty.requestFocus();
			etwQty.selectAll();
		}		 
	 }
    private void enableFields(boolean set){
		 etwProductCode.setEnabled(set);
         etwBatchNo.setEnabled(set);
         etwLotNo.setEnabled(set);
         etwQty.setEnabled(true);   	
    }
    private void createPalletProduct(){
    	if(actGrnPut.PUTTING_MODE == WhApp.ADDMODE){
	    	actGrnPut.selectedPalletProd = new PalletProduct();
			actGrnPut.selectedPalletProd.setProductCode(etwProductCode.getText().toString().toUpperCase());
			actGrnPut.selectedPalletProd.setBatchNo(etwBatchNo.getText().toString().toUpperCase());
			actGrnPut.selectedPalletProd.setLotNo(etwLotNo.getText().toString().toUpperCase());		
    	}
    }

	private void initializeDisplay(){	
		
		 if(actGrnPut.PUTTING_MODE == WhApp.ADDMODE){
			 enableFields(true);
			 clearTextBoxes();
		 }
		 else if(actGrnPut.PUTTING_MODE == WhApp.EDITMODE){
			 etwProductCode.setText(selectPalletProd.getProductCode().toUpperCase());
	         etwBatchNo.setText(selectPalletProd.getBatchNo().toUpperCase());
	         etwLotNo.setText(selectPalletProd.getLotNo().toUpperCase());
	         etwQty.setText(Integer.toString(selectPalletProd.getActQty()));
	         enableFields(false);	         
	         etwQty.requestFocus();
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
	private Dialog showWarningDialog(String title, String msg){
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    builder.setMessage(msg)
	    	   .setTitle(title)
	           .setCancelable(false)
        	   .setPositiveButton(getString(R.string.msgBtnYes), new DialogInterface.OnClickListener() {
        		   public void onClick(DialogInterface dialog, int id) {	
        			   try {
        				   if(actGrnPut.PUTTING_MODE == WhApp.ADDMODE){
								insertGrnPut(true);				
							}
							else if(actGrnPut.PUTTING_MODE == WhApp.EDITMODE){
								updateGrnPut(true);
							}        					
						   if(actGrnPut.selectedPalletProd.getHasSerialNo() && actGrnPut.whApp.getGrnHeader().getConfirmGrnType() == ConfirmGrnType.CONFIRM_GRN_AFTER_UPLOAD){
								actGrnPut.bar.setSelectedNavigationItem(3);
								dgPutEntry.dismiss();
							}
							refreshGrnPutList();
        			   } catch (Exception e) {
						e.printStackTrace();
        			   }
            }		               
        })
        .setNegativeButton(getString(R.string.msgBtnNo), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
         	   //do nothing
            }
        });
	    AlertDialog alert = builder.create();
        alert.show();
                
			return alert;
	 }
	                                          
	
	
	 public Boolean isQtyExceed(GrnHeader grnHeader, String[] outMsg){		 
		Boolean retValue = false;
		int balQty = grnHeader.getBalanceQtyBatchLot(actGrnPut.selectedPalletProd.getProductCode(),
				actGrnPut.selectedPalletProd.getBatchNo(), actGrnPut.selectedPalletProd.getLotNo());
		if(actGrnPut.PUTTING_MODE == WhApp.ADDMODE){
			if(newQty > balQty){
				retValue = true;
				outMsg[0] = "This qty exceeds the total for the same product, batch, and lot. Do you wish to proceed?";
			}
		}
		else if(actGrnPut.PUTTING_MODE == WhApp.EDITMODE){
			balQty += actGrnPut.selectedPalletProd.getActQty();
			if(newQty > balQty ){
				retValue = true;
				outMsg[0] = "This qty exceeds the total for the same product, batch, and lot. Do you wish to proceed?";
			}
		}
		return retValue;
	 }
	 public void insertGrnPut(Boolean exceedQty) throws Exception {
		try{	
			actGrnPut.selectedPalletProd.setActQty(newQty);
			actGrnPut.whApp.getGrnHeader().addGrnPuts(actGrnPut.whApp.getGrnHeader().hmPallet.get(actGrnPut.palletId.toUpperCase()), 
						actGrnPut.selectedPalletProd, getActivity().getApplicationContext(), exceedQty);
			

			showSuccessErrorDialog(getString(R.string.msgAlert), getString(R.string.msgSavedSuccess));
			btnScanSerialNo.setEnabled(true);
			actGrnPut.LOCATION_MODE = WhApp.DELETEMODE;
			clearTextBoxes();
			}
		catch(Exception ex){throw ex;}
		}	
	 public void updateGrnPut(Boolean exceedQty) throws Exception {
		try{		
			actGrnPut.selectedPalletProd.setActQty(newQty);
			actGrnPut.whApp.getGrnHeader().updateGrnPut(getActivity(), actGrnPut.palletId.toUpperCase(), actGrnPut.selectedPalletProd);
			actGrnPut.whApp.getGrnHeader().hmPallet.get(actGrnPut.palletId.toUpperCase()).updatePalletProduct(actGrnPut.selectedPalletProd);
		}
		catch(Exception ex){throw ex;}
	 }	
	 private void refreshGrnPutList(){
		 FragItemQty fragItemQty = (FragItemQty)getFragmentManager().findFragmentByTag(FragItemQty.TAG);
		 fragItemQty.onResume();
	 }
}
