package sg.com.innosys.wms.UI.GRN;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import sg.com.innosys.wms.BLL.Common.WhApp;
import sg.com.innosys.wms.BLL.Common.WhAppException;
import sg.com.innosys.wms.BLL.GRN.Pallet;
import sg.com.innosys.wms.R;

public class FragLocation extends Fragment{
	public static final String TAG = "fragLocation"; 
	private View vLocationFragView;
	private Button btnEdit;
	private Button btnDelete;
	private Button btnSave;
	private Button btnCancel;
	private Button btnViewGrnDetails;
	private RadioButton rbnRack;
	private RadioButton rbnNotRack;
	private RadioGroup rbg;
	private EditText etwPalletid;
	private EditText etwZone;
	private EditText etwRow;
	private EditText etwColumn;
	private EditText etwTier;
	private ActGrnPut actGrnPut;
	private ActionBar bar;
			
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);	
		actGrnPut = (ActGrnPut) getActivity(); 
		bar= actGrnPut.getActionBar();		
		if(savedInstanceState != null){
			actGrnPut.LOCATION_MODE = savedInstanceState.getInt(ActGrnPut.LOCATIONMODE);
			actGrnPut.palletId =savedInstanceState.getString(ActGrnPut.PALLETID);
		
		}
		if(actGrnPut.palletId == null || actGrnPut.LOCATION_MODE == WhApp.DELETEMODE){
			actGrnPut.bar.setSelectedNavigationItem(0);
			Toast.makeText(getActivity(), getString(R.string.errNoSelectedPallet), 3).show();
		}
		
		
		btnSave = (Button) actGrnPut.findViewById(R.id.btnSaveLocation);
		btnCancel = (Button) actGrnPut.findViewById(R.id.btnCancelLocation);
		btnViewGrnDetails = (Button) actGrnPut.findViewById(R.id.btnViewGrnDetail);
		rbg = (RadioGroup)actGrnPut.findViewById(R.id.rbgLoc_IsRack);
		rbnRack = (RadioButton)actGrnPut.findViewById(R.id.rbnLoc_Rack);
		rbnNotRack = (RadioButton)actGrnPut.findViewById(R.id.rbnLoc_NotRack);
		btnEdit = (Button) actGrnPut.findViewById(R.id.btnEditPallet);
		btnDelete = (Button) actGrnPut.findViewById(R.id.btnDeletePallet);
		
		btnEdit.setOnClickListener(buttonClickListener);
		btnDelete.setOnClickListener(buttonClickListener);
		btnSave.setOnClickListener(buttonClickListener);
		btnCancel.setOnClickListener(buttonClickListener);
		btnViewGrnDetails.setOnClickListener(buttonClickListener);
		rbg.setOnCheckedChangeListener(rbnCheckedChanged);
		
		initializeDisplay();
		etwPalletid.requestFocus();
	}	 
	private OnCheckedChangeListener rbnCheckedChanged = new OnCheckedChangeListener(){
				public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {				
				
			switch(checkedId){
			case R.id.rbnLoc_Rack:
				rbnRack.setChecked(true);
				isRackFields(true);
				break;
			
			case R.id.rbnLoc_NotRack:
				rbnNotRack.setChecked(true);
            	isRackFields(false);
				break;
			}				
		}								
	};
	
	@Override	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){
		
		vLocationFragView = inflater.inflate(R.layout.frag_grnlocation, container, false);
		etwPalletid = (EditText) vLocationFragView.findViewById(R.id.etwLoc_PalletId);
		etwZone = (EditText) vLocationFragView.findViewById(R.id.etwLoc_Zone);
		etwRow = (EditText) vLocationFragView.findViewById(R.id.etwLoc_Row);
		etwColumn = (EditText) vLocationFragView.findViewById(R.id.etwLoc_Column);
		etwTier= (EditText) vLocationFragView.findViewById(R.id.etwLoc_Tier);
		rbg = (RadioGroup)vLocationFragView.findViewById(R.id.rbgLoc_IsRack);
		rbnRack = (RadioButton)vLocationFragView.findViewById(R.id.rbnLoc_Rack);
		rbnNotRack = (RadioButton)vLocationFragView.findViewById(R.id.rbnLoc_NotRack);
		btnEdit = (Button) vLocationFragView.findViewById(R.id.btnEditPallet);
		btnDelete = (Button) vLocationFragView.findViewById(R.id.btnDeletePallet);
		btnSave = (Button) vLocationFragView.findViewById(R.id.btnSaveLocation);
		btnCancel = (Button) vLocationFragView.findViewById(R.id.btnCancelLocation);
		btnViewGrnDetails = (Button) vLocationFragView.findViewById(R.id.btnViewGrnDetail);
		return vLocationFragView;
	}
    
	@Override
    public void onSaveInstanceState(Bundle outState){
		actGrnPut = (ActGrnPut) getActivity();
    	outState.putBoolean(ActGrnPut.ISLOCATIONSAVED, actGrnPut.isLocationSaved); 
    	outState.putInt(ActGrnPut.LOCATIONMODE, actGrnPut.LOCATION_MODE); 
    	outState.putString(ActGrnPut.PALLETID, actGrnPut.palletId);
    	
    	super.onSaveInstanceState(outState);
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
    	actGrnPut = (ActGrnPut) getActivity();
    	initializeDisplay();
	}
	private void initializeDisplay(){
		etwPalletid.requestFocus();
		try{
			clearAllTextboxes();
			//form mode values
			//1 = add mode; 2=edit mode; 3=delete mode
	  		if(actGrnPut.LOCATION_MODE == WhApp.ADDMODE){
				enableTextboxes(true);
				isRackFields(true);
				rbnRack.setChecked(true);
				btnSave.setEnabled(true);
				btnViewGrnDetails.setEnabled(false);
			}
			else if(actGrnPut.LOCATION_MODE == WhApp.EDITMODE){
				populateLocation();		
				enableTextboxes(false);
				btnSave.setEnabled(true);
				btnViewGrnDetails.setEnabled(false);
			}
			else if(actGrnPut.LOCATION_MODE == WhApp.DELETEMODE){
				isRackFields(false);
				enableTextboxes(false);
				btnSave.setEnabled(false);
				btnCancel.setEnabled(true);
				btnViewGrnDetails.setEnabled(true);
			}
		}
		catch(Exception ex){ex.printStackTrace();}
	}
	private void enableTextboxes(Boolean set){
		 etwPalletid.setEnabled(set);
		 etwZone.setEnabled(set);
		 etwRow.setEnabled(set);
		 etwColumn.setEnabled(set);
		 etwTier.setEnabled(set);
		 rbnRack.setEnabled(set);
		 rbnNotRack.setEnabled(set);
		 if(!set){
			 etwPalletid.setBackgroundColor(Color.parseColor("#66cdaa"));
			 etwZone.setBackgroundColor(Color.parseColor("#66cdaa"));
			 etwRow.setBackgroundColor(Color.parseColor("#66cdaa"));
			 etwColumn.setBackgroundColor(Color.parseColor("#66cdaa"));
			 etwTier.setBackgroundColor(Color.parseColor("#66cdaa"));
		 }
		 else{
			 etwPalletid.setBackgroundColor(Color.parseColor("#87cefa"));
			 etwZone.setBackgroundColor(Color.parseColor("#87cefa"));
			 etwRow.setBackgroundColor(Color.WHITE);
			 etwColumn.setBackgroundColor(Color.WHITE);
			 etwTier.setBackgroundColor(Color.WHITE);
		 }
		 
	}
	private void clearAllTextboxes(){	
		 etwPalletid.setText("");
		 etwZone.setText("");
		 etwRow.setText("");
		 etwColumn.setText("");
		 etwTier.setText("");
		 etwPalletid.setBackgroundColor(Color.parseColor("#87cefa"));
		 etwZone.setBackgroundColor(Color.parseColor("#87cefa"));
		 etwRow.setBackgroundColor(Color.WHITE);
		 etwColumn.setBackgroundColor(Color.WHITE);
		 etwTier.setBackgroundColor(Color.WHITE);
	}
	private void isRackFields(Boolean set){
		etwRow.setEnabled(set);
		etwColumn.setEnabled(set);
		etwTier.setEnabled(set);
		 if(!set){
			 etwRow.setText("");
			 etwColumn.setText("");
			 etwTier.setText("");
			 etwRow.setBackgroundColor(Color.parseColor("#d3d3d3"));
			 etwColumn.setBackgroundColor(Color.parseColor("#d3d3d3"));
			 etwTier.setBackgroundColor(Color.parseColor("#d3d3d3"));
		 }
		 else{
			 etwRow.setBackgroundColor(Color.parseColor("#87cefa"));
			 etwColumn.setBackgroundColor(Color.parseColor("#87cefa"));
			 etwTier.setBackgroundColor(Color.parseColor("#87cefa"));
		 }
	}
	public void populateLocation(){
		if(!actGrnPut.palletId.trim().equalsIgnoreCase("")){
			Pallet tempPallet = null;
			if(actGrnPut.whApp.getGrnHeader().hmPallet.get(actGrnPut.palletId) != null){
				tempPallet = actGrnPut.whApp.getGrnHeader().hmPallet.get(actGrnPut.palletId);
				etwPalletid.setText(tempPallet.getPalletId().toString().toUpperCase());
				etwZone.setText(tempPallet.getZone().toString().toUpperCase());
				etwRow.setText(tempPallet.getRow().toString().toUpperCase());
				etwColumn.setText(tempPallet.getColumn().toString().toUpperCase());
				etwTier.setText(tempPallet.getTier().toString().toUpperCase());
				//Pallet p = (Pallet)actGrnPut.grnHeader.hmPallet.get(actGrnPut.palletId).g;
				Boolean bool =tempPallet.getHasRack();
				if(bool)	{	
					rbnRack.setChecked(true);
				}
				else{
					rbnNotRack.setChecked(true);
				}
			}
		}
	}
	
	private void fillPallet() throws Exception{
		try{
		Pallet newPallet = new Pallet();
		newPallet.setPalleteId(etwPalletid.getText().toString().toUpperCase());
		newPallet.setZone(etwZone.getText().toString().toUpperCase());
		newPallet.setRow(etwRow.getText().toString().toUpperCase());
		newPallet.setColumn(etwColumn.getText().toString().toUpperCase());
		newPallet.setTier(etwTier.getText().toString().toUpperCase());	
		if(rbnRack.isChecked()){
			newPallet.setHasRack(true);
		}
		else
			newPallet.setHasRack(false);	
		
		actGrnPut.palletId = newPallet.getPalletId().toUpperCase();	
		//transfer to BLL
		actGrnPut.whApp.getGrnHeader().hmPallet.put(newPallet.getPalletId().toUpperCase(),newPallet);
	
		if(!actGrnPut.isShowAllLocation){
			actGrnPut.palletIDList.add(newPallet.getPalletId().toUpperCase());
		}
		}catch(Exception ex){throw ex;}
	}
	
	private OnClickListener buttonClickListener = new View.OnClickListener() {
        public void onClick(View v) {
        	try {
	        	if(v == btnEdit){
	        		//do edit
	        		actGrnPut.LOCATION_MODE = WhApp.EDITMODE;//for now set to delete because no function for edit yet
	        		actGrnPut.isLocationSaved = true;
	        		initializeDisplay();
	        	}
	        	if(v == btnDelete){
	        		//do delete
					if(actGrnPut.whApp.getGrnHeader().hmPallet.get(actGrnPut.palletId.toUpperCase()) != null){						
						showAlertDialog(getString(R.string.msgWarnConfirmRemovePallet));					
					}
	        	}
	        	if(v == btnSave){
        			String[] errMsg = new String[1];
        			if(validateFields(errMsg)){  
    					if(actGrnPut.LOCATION_MODE == WhApp.ADDMODE){
    						if(!actGrnPut.whApp.getGrnHeader().isPalletIDExist(getActivity(), etwPalletid.getText().toString())){
                				fillPallet();
        		        		enableTextboxes(false); 
        		        		actGrnPut.isLocationSaved = true;
        		        		actGrnPut.LOCATION_MODE = WhApp.DELETEMODE;
        		        		showSuccessErrorDialog(getString(R.string.msgSavedSuccess),getString(R.string.toastSaveInMemory));
        		        		btnSave.setEnabled(false);        						
        					}
        				}
        			}
        			else{
        				throw new WhAppException(errMsg[0].toString());
        			}	        		
	            }
	            if(v == btnCancel){
	            	if(actGrnPut.LOCATION_MODE == WhApp.ADDMODE)
	            		clearAllTextboxes(); 
	            	
	            	actGrnPut.LOCATION_MODE = WhApp.DELETEMODE;
	            	actGrnPut.isLocationSaved = false;
	    			bar.selectTab(bar.getTabAt(0));	
	            }
	            if(v == btnViewGrnDetails){
	            	//Now show Location when item selected
					Intent intent = new Intent(getActivity(), ActGRNMain.class);
				    startActivity(intent);
	            } 
        	}catch(WhAppException ex){ 
    			showSuccessErrorDialog(getString(R.string.msgError), ex.getMessage());
    		}
    		catch(Exception ex){ 
    			showSuccessErrorDialog(getString(R.string.msgError), ex.getMessage());
    		}
        }
    };
    
    private Boolean validateFields(String[] outString) throws WhAppException{    
    	outString[0] = getString(R.string.msgSavedFailed);
    	boolean retValue = true;
    	if(etwPalletid.getText().toString().equals("")){
    		etwPalletid.requestFocus();
    		outString[0] += "\n*" +getString(R.string.errEmptyPalletId);
    		retValue = false;
    	}
    	if(etwZone.getText().toString().equals("")){
    		etwZone.requestFocus();
    		outString[0] += "\n*" +getString(R.string.errEmptyZone);
    		retValue = false;
    	}
    	if(rbg.getCheckedRadioButtonId() == rbnRack.getId()){
    		if(etwRow.getText().toString().trim().equals("")){
        		etwRow.requestFocus();    			
        		outString[0] += "\n*" +getString(R.string.errEmptyRow);
        		retValue = false;
    		}
    		if(etwColumn.getText().toString().trim().equals("")){
        		etwColumn.requestFocus();    			
        		outString[0] += "\n*" +getString(R.string.errEmptyColumn);
        		retValue = false;
    		}
    		if(etwTier.getText().toString().trim().equals("")){
        		etwTier.requestFocus();    			
        		outString[0] += "\n*" +getString(R.string.errEmptyTier);
        		retValue = false;
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

	private AlertDialog showAlertDialog(String msg) throws Exception{
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    builder.setMessage(msg)
	    	   .setTitle("Warning")
	           .setCancelable(false)
	           .setPositiveButton("YES", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {	
	            	   try {
						actGrnPut.whApp.getGrnHeader().deleteGrnPutFromPalletId(actGrnPut, actGrnPut.whApp.getGrnHeader().hmPallet.get(actGrnPut.palletId.toUpperCase()));
						actGrnPut.whApp.getGrnHeader().hmPallet.remove(actGrnPut.palletId.toUpperCase());
						actGrnPut.LOCATION_MODE = WhApp.DELETEMODE;
						clearAllTextboxes();
						enableTextboxes(false);		
		    			showSuccessErrorDialog(getString(R.string.msgAlert), getString(R.string.msgDeleteSuccess));					
	            	   } catch (Exception e) {
							e.printStackTrace();
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
}
