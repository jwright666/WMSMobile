package sg.com.innosys.wms.UI.GRN;

import android.R.color;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import sg.com.innosys.wms.BLL.Common.WhApp;
import sg.com.innosys.wms.BLL.Common.WhAppException;
import sg.com.innosys.wms.BLL.GRN.GrnSerialNo;
import sg.com.innosys.wms.R;

@SuppressLint("DefaultLocale")
public class DiaScanGrnSerialNo extends DialogFragment{
	private final String MODE = "MODE";
	private ActGrnPut actGrnPut;
	private Dialog dgScanSerialNo;
	private Button btnSave;
	private Button btnClose;
	private Button btnClose1;
	private View vDiaFragScanSerialNo; 
	private RelativeLayout rlSerialDesc;
	private EditText etwSerialNo;
	private EditText etwSerialDesc;
	private EditText etwHiddenField;
	//private GrnSerialNo serialNo;
	//private int mode;
	

	public static DiaScanGrnSerialNo newInstance() {
		DiaScanGrnSerialNo f = new DiaScanGrnSerialNo();
	        return f;	        
	    }
	public DiaScanGrnSerialNo(){
		actGrnPut = (ActGrnPut) getActivity(); 
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		
		actGrnPut = (ActGrnPut) getActivity();  
		DiaScanGrnSerialNo.newInstance();
		btnSave.setOnClickListener(btnOnClickListener);
		btnClose.setOnClickListener(btnOnClickListener);
		btnClose1.setOnClickListener(btnOnClickListener);
		etwSerialNo.setOnFocusChangeListener(etvOnFocusChanged);
		etwHiddenField.setOnFocusChangeListener(etvOnFocusChanged);
		//tvvserialNo.setOnClickListener(btnOnClickListener);

		//etwSerialNo.addTextChangedListener(txtWatcher);
		
		if(savedInstanceState != null){
			actGrnPut.selectedSerialNo= (GrnSerialNo) savedInstanceState.getSerializable(actGrnPut.SERIALNO);
			WhApp.formMode = savedInstanceState.getInt(MODE);
		}
        if(WhApp.formMode == WhApp.ADDMODE){
        	rlSerialDesc.setVisibility(View.GONE);
        	btnClose.setVisibility(View.VISIBLE);
        }
        else if(WhApp.formMode == WhApp.EDITMODE){
        	rlSerialDesc.setVisibility(View.VISIBLE);  
        	btnClose.setVisibility(View.GONE);    	
        }
        populateSerial();
		getDialog().getWindow().setGravity(Gravity.RIGHT | Gravity.CENTER);
	 }
	@Override
	public void onSaveInstanceState(Bundle outState){
		super.onSaveInstanceState(outState);
		outState.putInt(MODE, WhApp.formMode);
		if(actGrnPut.selectedSerialNo != null){
			outState.putSerializable(actGrnPut.SERIALNO, actGrnPut.selectedSerialNo);
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
	     populateSerial();
	 }
	
	private View getContentView() {
        LayoutInflater inflater = getActivity().getLayoutInflater();  
        vDiaFragScanSerialNo =	inflater.inflate(R.layout.diafrag_scan_serial_no, null);
        btnSave =(Button) vDiaFragScanSerialNo.findViewById(R.id.btnScan_saveSerialNo);
        btnClose =(Button) vDiaFragScanSerialNo.findViewById(R.id.btnScan_closeSerialNo);
        btnClose1 =(Button) vDiaFragScanSerialNo.findViewById(R.id.btnScan_closeSerialNo1);
        rlSerialDesc = (RelativeLayout)vDiaFragScanSerialNo.findViewById(R.id.rlScan_serialDesc);
        etwSerialNo = (EditText)vDiaFragScanSerialNo.findViewById(R.id.etwScan_serialNo);
        etwSerialDesc = (EditText)vDiaFragScanSerialNo.findViewById(R.id.etwScan_serialDesc);
        etwHiddenField = (EditText)vDiaFragScanSerialNo.findViewById(R.id.etwScan_SerialNoHiddenField);     

		//etwSerialNo.setOnFocusChangeListener(etvOnFocusChanged);
		etwHiddenField.setBackgroundColor(color.transparent);
		//etwHiddenField.setOnFocusChangeListener(etvOnFocusChanged);
		//tvvserialNo.setOnFocusChangeListener(etvOnFocusChanged);
        return vDiaFragScanSerialNo;
	}
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {	
		//actGinPick = (ActGinPick) getActivity(); 	 
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.diaSerialNoEntry);
        builder.setView(getContentView());
        dgScanSerialNo = builder.create();
        dgScanSerialNo.setCanceledOnTouchOutside(false);
        return dgScanSerialNo;
    }
	private OnFocusChangeListener etvOnFocusChanged = new OnFocusChangeListener(){
		public void onFocusChange(View view, boolean hasFocus) {
			String title ="";
    		try {
				if(!hasFocus){
					if(view == etwSerialNo){
						if(!etwSerialNo.getText().toString().trim().equals("")){
							if(WhApp.formMode == WhApp.ADDMODE){
								if(actGrnPut.selectedPalletProd.grnSerialNos.size() < actGrnPut.selectedPalletProd.getActQty()){
									//TODO add serial
									createSerialNo();
									//add to both database memory for grn detail serial no
									actGrnPut.whApp.getGrnHeader().getSelectedGrnDetailForSerialNo(actGrnPut.selectedPalletProd).addGrnSerialNo(actGrnPut.selectedSerialNo, getActivity());
									//add to memory for palletProduct serial no
									actGrnPut.selectedPalletProd.grnSerialNos.add(actGrnPut.selectedSerialNo);
									
						    		etwSerialNo.setText("");
						    		FragGrnSerialNo fragSerialNo = (FragGrnSerialNo)getFragmentManager().findFragmentByTag(FragGrnSerialNo.TAG);
						    		fragSerialNo.onResume();
						    		if(actGrnPut.selectedPalletProd.grnSerialNos.size() == actGrnPut.selectedPalletProd.getActQty()){
								    	 dgScanSerialNo.dismiss();
						    		}						    		
								}
								else{
									throw new WhAppException(getString(R.string.errExceedSerialNo));
								}
							}
						}
						else{
							throw new WhAppException(getString(R.string.errBlankSerialNo));
						}
					}
				}
				else{
					if(view == etwHiddenField){
						etwSerialNo.requestFocus();						
					}
				}
    		}catch (WhAppException e) {
				 title = getString(R.string.msgError);
				 e.printStackTrace();
				 showSuccessErrorDialog(title, getString(R.string.msgSavedFailed)+"\n" +e.getLocalizedMessage().toString());
		 	 } 
    		catch (Exception e) {
				 title = getString(R.string.msgError);
				 e.printStackTrace();
				 showSuccessErrorDialog(title, getString(R.string.msgSavedFailed)+"\n" +e.getLocalizedMessage().toString());
		 	 }
		}		
	};
	
	private OnClickListener btnOnClickListener = new OnClickListener(){	    
		public void onClick(View btn) {
			 String title ="";
	   		 try {
		    	 if(btn == btnSave){
		    		 //TODO update serialNo
		    		 if(!etwSerialNo.getText().toString().trim().equals("")){
		    			 actGrnPut.selectedSerialNo.setDescription(etwSerialDesc.getText().toString().toUpperCase());		    		 
			    		 //update to both database and memory for grn detail serial no
						 actGrnPut.whApp.getGrnHeader().getSelectedGrnDetailForSerialNo(actGrnPut.selectedPalletProd).updateGrnSerialNo(actGrnPut.selectedSerialNo, etwSerialNo.getText().toString().trim(), getActivity());
						//update to memory for palletProduct serial no
						 actGrnPut.selectedPalletProd.updatePalletProductSerialNoInMemory(actGrnPut.selectedSerialNo, getActivity());
			    		
						 //*/ 
			    		 FragGrnSerialNo fragSerialNo = (FragGrnSerialNo)getFragmentManager().findFragmentByTag(FragGrnSerialNo.TAG);
			    		 fragSerialNo.onResume();
				    	 dgScanSerialNo.dismiss();
		    		 }
		    		 else{
		    			//etwSerialNo.setText(serialNo.getSerialNo().toString());
		    			 //etwSerialDesc.setText(serialNo.getDescription().toString());
		    			 throw new WhAppException(getString(R.string.errBlankSerialNo));
		    		 }
		    	 }
		    	 else if(btn == btnClose){
		    		 dgScanSerialNo.dismiss();
		    	 }
		    	 else if(btn == btnClose1){
		    		 dgScanSerialNo.dismiss();
		    	 }
	   		 }catch (WhAppException e) {
				 title = getString(R.string.msgError);
				 e.printStackTrace();
				 showSuccessErrorDialog(title, getString(R.string.msgSavedFailed)+"\n" +e.getLocalizedMessage().toString());
		 	 }  catch (Exception e) {
				 title = getString(R.string.msgError);
				 e.printStackTrace();
				 String errMsg = e.toString().replace("java.lang.Exception: ", "");
				 showSuccessErrorDialog(title, getString(R.string.msgSavedFailed)+"\n" +errMsg);
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
	@SuppressLint("DefaultLocale")
	private GrnSerialNo createSerialNo(){
		actGrnPut.selectedSerialNo = new GrnSerialNo();
		actGrnPut.selectedSerialNo.setTransactionNo(actGrnPut.whApp.getGrnHeader().getSelectedGrnDetailForSerialNo(actGrnPut.selectedPalletProd).getTransactionNo());
		actGrnPut.selectedSerialNo.setItemCode(actGrnPut.selectedPalletProd.getItemCode());
		actGrnPut.selectedSerialNo.setSerialNo(etwSerialNo.getText().toString().toUpperCase());
		actGrnPut.selectedSerialNo.setPalletId(actGrnPut.palletId.toString().toUpperCase());
		actGrnPut.selectedSerialNo.setDescription("");
		return actGrnPut.selectedSerialNo;
	}
	
	private void populateSerial(){
		if(actGrnPut.selectedSerialNo != null){
			etwSerialNo.setText(actGrnPut.selectedSerialNo.getSerialNo());
			etwSerialDesc.setText(actGrnPut.selectedSerialNo.getDescription());
			//etwSerialNo.setEnabled(false);
		}
		else{
	        etwSerialNo.requestFocus();
			//etwSerialNo.setEnabled(true);
			etwSerialNo.setText("");
		}
	}
}

