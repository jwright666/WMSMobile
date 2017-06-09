package sg.com.innosys.wms.UI.Crating;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.ParseException;

import sg.com.innosys.wms.BLL.Common.WhApp;
import sg.com.innosys.wms.BLL.Common.WhAppException;
import sg.com.innosys.wms.BLL.Crating.GinCrate;
import sg.com.innosys.wms.R;

public class FragTabCrateMaster extends Fragment{

	private View vCrateMasterFragView;
	private Button btnDelete;
	private Button btnSave;
	private EditText etwCrateNo;
	private EditText etwCrateDesc;
	private EditText etwCrateMarking;
	private ActCrateProduct actCrateProd;
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);	
		actCrateProd = (ActCrateProduct) getActivity(); 
		btnSave.setOnClickListener(btnClicked);
		btnDelete.setOnClickListener(btnClicked);
		if(savedInstanceState!=null){
	        actCrateProd.selectedGinCrate = (GinCrate) savedInstanceState.getSerializable(ActCrateMain.SELECTEDCRATE);
	        actCrateProd.crateMode = savedInstanceState.getInt(ActCrateMain.CRATEMODE);
		}
		
		setControls();
	}	 
	
	@Override	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){
		
		vCrateMasterFragView = inflater.inflate(R.layout.frag_cratemaster, container, false);
		btnSave = (Button) vCrateMasterFragView.findViewById(R.id.btnSaveCrate);
		btnDelete = (Button) vCrateMasterFragView.findViewById(R.id.btnDeleteCrate);
		etwCrateNo = (EditText) vCrateMasterFragView.findViewById(R.id.etwCrate_No);
		etwCrateDesc = (EditText) vCrateMasterFragView.findViewById(R.id.etwCrate_Desc);
		etwCrateMarking = (EditText) vCrateMasterFragView.findViewById(R.id.etwCrate_Marking);
		
		return vCrateMasterFragView;
	}
	
	@Override
    public void onSaveInstanceState(Bundle outState){    	
    	super.onSaveInstanceState(outState);
    	//TODO save current State
    	outState.putInt(ActCrateMain.CRATEMODE, actCrateProd.crateMode);
    	outState.putSerializable(ActCrateMain.SELECTEDCRATE, actCrateProd.selectedGinCrate);
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
    	setControls();
    	createCrateObject();
	}
	private void populateData(){
		//TODO populate select crate
		if(actCrateProd.selectedGinCrate != null){
			etwCrateNo.setText(actCrateProd.selectedGinCrate.getCrateNo().toUpperCase());
			etwCrateDesc.setText(actCrateProd.selectedGinCrate.getCrateDesc().toUpperCase());
			etwCrateMarking.setText(actCrateProd.selectedGinCrate.getCrateMarking().toUpperCase());	
		}
	}

	private void createCrateObject(){
		//if(actCrateProd.crateMode == WhApp.ADDMODE){
			actCrateProd.selectedGinCrate = new GinCrate();
			actCrateProd.selectedGinCrate.setTransactionNo(actCrateProd.whApp.getGinHeader().getTransactionNo());
			actCrateProd.selectedGinCrate.setCrateNo(etwCrateNo.getText().toString().toUpperCase());
			actCrateProd.selectedGinCrate.setCrateDesc(etwCrateDesc.getText().toString().toUpperCase());
			actCrateProd.selectedGinCrate.setCrateMarking(etwCrateMarking.getText().toString().toUpperCase());
		//}
	}
	private void clearFields(){
		etwCrateNo.setText("");
		etwCrateDesc.setText("");
		etwCrateMarking.setText("");		
	}
	private void setControls(){
		//TODO color control by mode
		switch(actCrateProd.crateMode){
			case WhApp.EDITMODE:
				populateData();
				btnSave.setEnabled(true);
				btnDelete.setEnabled(true);
				etwCrateNo.setBackgroundColor(Color.parseColor("#66cdaa"));
				etwCrateDesc.setBackgroundColor(Color.parseColor("#ffffff"));
				etwCrateMarking.setBackgroundColor(Color.parseColor("#ffffff"));
				etwCrateNo.setEnabled(false);
				etwCrateDesc.setEnabled(true);
				etwCrateMarking.setEnabled(true);
				break;
			case WhApp.ADDMODE:
				btnSave.setEnabled(true);
				btnDelete.setEnabled(false);
				etwCrateNo.setEnabled(false);
				etwCrateDesc.setEnabled(true);
				etwCrateMarking.setEnabled(true);
				clearFields();
				etwCrateNo.setText(actCrateProd.whApp.getGinHeader().generateCrateNo());					
				etwCrateNo.setBackgroundColor(Color.parseColor("#66cdaa"));
				etwCrateDesc.setBackgroundColor(Color.parseColor("#ffffff"));
				etwCrateMarking.setBackgroundColor(Color.parseColor("#ffffff"));
				etwCrateDesc.requestFocus();
				break;
			case WhApp.DELETEMODE:
				btnSave.setEnabled(false);
				btnDelete.setEnabled(false);
				etwCrateNo.setEnabled(false);
				etwCrateDesc.setEnabled(false);
				etwCrateMarking.setEnabled(false);
				//clearFields();
				etwCrateNo.setText(actCrateProd.whApp.getGinHeader().generateCrateNo());					
				etwCrateNo.setBackgroundColor(Color.parseColor("#66cdaa"));
				etwCrateDesc.setBackgroundColor(Color.parseColor("#66cdaa"));
				etwCrateMarking.setBackgroundColor(Color.parseColor("#66cdaa"));
				etwCrateDesc.requestFocus();
				break;
		}
		
	}
	private OnClickListener btnClicked = new OnClickListener(){
		public void onClick(View btn) {
			// TODO Auto-generated method stub
			try {
				if(btn == btnSave){
					//TODO add new crate					
					createCrateObject();	
					if(actCrateProd.crateMode == WhApp.ADDMODE){				
						actCrateProd.whApp.getGinHeader().addCrate(getActivity(), actCrateProd.selectedGinCrate);			
					}
					else if(actCrateProd.crateMode == WhApp.EDITMODE){					
						actCrateProd.whApp.getGinHeader().updateCrate(getActivity(), actCrateProd.selectedGinCrate);
					}
					showSuccessErrorDialog(getString(R.string.msgAlert), getString(R.string.msgSavedSuccess));
					actCrateProd.crateMode = WhApp.DELETEMODE;
					setControls();
				}
				if(btn == btnDelete){
					showAlertDialog(getActivity().getString(R.string.msgWarnDeleteNotEmptyCrate));	
				}
			} catch (WhAppException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				showSuccessErrorDialog(getString(R.string.msgError), e.getLocalizedMessage().toString());
			}			
			catch (Exception e) {
				e.printStackTrace();
				showSuccessErrorDialog(getString(R.string.msgError), e.getLocalizedMessage().toString());
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
						populateData();	            	   
	               }});
	        alert.show();	        
			return alert;
	 }
	private AlertDialog showAlertDialog(String msg){
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    builder.setMessage(msg)
	    	   .setTitle(getString(R.string.msgAlert))
	           .setCancelable(false)
	           .setPositiveButton(getString(R.string.msgBtnYes), new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {	
	            	   try {
	            		    actCrateProd.whApp.getGinHeader().deleteCrate(getActivity(), actCrateProd.selectedGinCrate);
							actCrateProd.selectedGinCrate = null;
							actCrateProd.crateMode = WhApp.DELETEMODE;
							setControls();
							showSuccessErrorDialog(getString(R.string.msgAlert), getString(R.string.msgDeleteSuccess));	
							clearFields();
						} catch (ParseException e) {
							e.printStackTrace();
							throw e;
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
}
