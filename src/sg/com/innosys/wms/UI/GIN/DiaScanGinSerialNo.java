package sg.com.innosys.wms.UI.GIN;

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
import sg.com.innosys.wms.BLL.GIN.GinSerialNo;
import sg.com.innosys.wms.R;

@SuppressLint("NewApi") // 20140106 gerry added
public class DiaScanGinSerialNo extends DialogFragment{
	private final String MODE = "MODE";
	private ActGinPick actGinPick;
	private Dialog dgScanSerialNo;
	private Button btnSave;
	private Button btnClose;
	private Button btnClose1;
	private View vDiaFragScanSerialNo; 
	private RelativeLayout rlSerialDesc;
	private EditText etwSerialNo;
	private EditText etwSerialDesc;
	private EditText etwHiddenField;
	private GinSerialNo serialNo;
	//private int mode;
	

	static DiaScanGinSerialNo newInstance() {
		DiaScanGinSerialNo f = new DiaScanGinSerialNo();
	        return f;	        
	    }
	public DiaScanGinSerialNo(){
		actGinPick = (ActGinPick) getActivity(); 
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		
		actGinPick = (ActGinPick) getActivity();  
		btnSave.setOnClickListener(btnOnClickListener);
		btnClose.setOnClickListener(btnOnClickListener);
		btnClose1.setOnClickListener(btnOnClickListener);
		etwSerialNo.setOnFocusChangeListener(etvOnFocusChanged);
		etwHiddenField.setOnFocusChangeListener(etvOnFocusChanged);
		//tvvserialNo.setOnClickListener(btnOnClickListener);

		//etwSerialNo.addTextChangedListener(txtWatcher);

    	serialNo = actGinPick.scannedSerialNo; // 20140106 gerry added
    	
		if(savedInstanceState != null){
			actGinPick.scannedSerialNo= (GinSerialNo) savedInstanceState.getSerializable(ActGinPick.SERIALNO);
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
		if(serialNo != null){
			outState.putSerializable(ActGinPick.SERIALNO, serialNo);
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
        //tvvserialNo = (TextView)vDiaFragScanSerialNo.findViewById(R.id.tvvScan_serialNo);       

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
							 if(WhApp.formMode==WhApp.ADDMODE){
								if(actGinPick.selectedGinDetail.ginSerialNos.size() < actGinPick.selectedGinDetail.getActQty()){
									//do add serial
									createSerialNo();
									actGinPick.selectedGinDetail.addGinSerialNo(serialNo, getActivity());							
						    		etwSerialNo.setText("");
									
						    		FragGinSerialNo fragSerialNo = (FragGinSerialNo)getFragmentManager().findFragmentByTag(FragGinSerialNo.TAG);
						    		fragSerialNo.onResume();
						    		if(actGinPick.selectedGinDetail.ginSerialNos.size() == actGinPick.selectedGinDetail.getActQty()){
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
				///*
				else{
					if(view == etwHiddenField){
						etwSerialNo.requestFocus();						
					}
				}
				//*/
    		}catch (WhAppException e) {
				 title = getString(R.string.msgError);
				 e.printStackTrace();
				 showSuccessErrorDialog(title, getString(R.string.msgSavedFailed)+"\n" +e.getLocalizedMessage().toString());
		 	 }  catch (Exception e) {
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
		    		 if(!etwSerialNo.getText().toString().trim().equals("")){
			    		 //do update serialNo
			    		 serialNo.setDescription(etwSerialDesc.getText().toString().toUpperCase());
			    		 actGinPick.selectedGinDetail.updateGinSerialNo(serialNo, etwSerialNo.getText().toString(), getActivity());
	
			    		 FragGinSerialNo fragSerialNo = (FragGinSerialNo)getFragmentManager().findFragmentByTag(FragGinSerialNo.TAG);
			    		 fragSerialNo.onResume();
			    		 dgScanSerialNo.dismiss();
			    		 
		    		 }
		    		 else{
		    			 throw new WhAppException(getString(R.string.errBlankSerialNo));
		    		 }
		    	 }
		    	 else if(btn == btnClose){
		    		 dgScanSerialNo.dismiss();
		    	 }
		    	 else if(btn == btnClose1){
		    		 dgScanSerialNo.dismiss();
		    	 }
	   		 } 
	   		 catch (WhAppException e) {
				 title = getString(R.string.msgError);
				 e.printStackTrace();                                          
				 showSuccessErrorDialog(title, getString(R.string.msgSavedFailed)+"\n" +e.getLocalizedMessage().toString());
    			 etwSerialNo.setText(serialNo.getSerialNo().toString());
    			 etwSerialDesc.setText(serialNo.getDescription().toString());
		 	 }
	   		 catch (Exception e) {
				 title = getString(R.string.msgError);
				 e.printStackTrace();
				 showSuccessErrorDialog(title, getString(R.string.msgSavedFailed)+"\n" +e.getLocalizedMessage().toString());
    			 etwSerialNo.setText(serialNo.getSerialNo().toString());
    			 etwSerialDesc.setText(serialNo.getDescription().toString());
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
	private GinSerialNo createSerialNo(){
		serialNo = new GinSerialNo();
		serialNo.setTransactionNo(actGinPick.selectedGinDetail.getTransactionNo());
		serialNo.setItemCode(actGinPick.selectedGinDetail.getItemCode());
		serialNo.setSerialNo(etwSerialNo.getText().toString().toUpperCase());
		serialNo.setDescription("");
		return serialNo;
	}
	
	private void populateSerial(){
		if(serialNo != null){
			etwSerialNo.setText(serialNo.getSerialNo());
			etwSerialDesc.setText(serialNo.getDescription());
			etwSerialDesc.requestFocus();
		}
		else{
	        etwSerialNo.requestFocus();
			//etwSerialNo.setEnabled(true);
			etwSerialNo.setText("");
		}
	}
}
