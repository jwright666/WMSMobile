package sg.com.innosys.wms.UI.GRN;

import android.annotation.SuppressLint;
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
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.StringTokenizer;

import sg.com.innosys.wms.BLL.Common.WhApp;
import sg.com.innosys.wms.BLL.Common.WhAppException;
import sg.com.innosys.wms.BLL.GRN.Pallet;
import sg.com.innosys.wms.R;

@SuppressLint("NewApi")
public class FragLocation_ScanMultiFields extends Fragment{
	private final String BLANKLOCATION ="-";
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
	private TextView tvwLocSeparator;
	private TextView tvwLocation;
	private EditText etwLocation;
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
 		etwLocation.setOnFocusChangeListener(etvOnFocusChanged);
		etwZone.setOnFocusChangeListener(etvOnFocusChanged);
		etwRow.setOnFocusChangeListener(etvOnFocusChanged);
		etwColumn.setOnFocusChangeListener(etvOnFocusChanged);
		etwTier.setOnFocusChangeListener(etvOnFocusChanged);
		if(actGrnPut.whApp.getWhMobileSettings() != null)
			tvwLocSeparator.setText(tvwLocSeparator.getText().toString() + " : " +actGrnPut.whApp.getWhMobileSettings().getLocSeparator());
	
		initializeDisplay();
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
		etwLocation = (EditText) vLocationFragView.findViewById(R.id.etwLoc_Location);
		tvwLocation = (TextView) vLocationFragView.findViewById(R.id.tvvLoc_Location);
		tvwLocSeparator = (TextView) vLocationFragView.findViewById(R.id.tvvLoc_LocSeparator);
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
		etwLocation.setText("");	
		try{
			//form mode values
			//1 = add mode; 2=edit mode; 3=delete mode
	  		if(actGrnPut.LOCATION_MODE == WhApp.ADDMODE){
				enableTextboxes(true);
				isRackFields(true);
				rbnRack.setChecked(true);
				btnSave.setEnabled(true);
				btnViewGrnDetails.setEnabled(false);
				btnEdit.setEnabled(false);
				btnDelete.setEnabled(false);
				clearAllTextboxes();
				
				//start logic for new change
				if(actGrnPut.isScanPalletID){
					etwPalletid.setEnabled(true);
					etwPalletid.requestFocus();	
				}
				else{
					//auto generate palletID
					
					//GetNextPalletID write to etwPalletID
					etwPalletid.setText(actGrnPut.whApp.getGrnHeader().generatePalletID());
					etwPalletid.setEnabled(false);
					etwLocation.requestFocus();					
					
				}				
				
			}
			else if(actGrnPut.LOCATION_MODE == WhApp.EDITMODE){
				populateLocation();		
				enableTextboxes(false);
				btnEdit.setEnabled(true);
				btnDelete.setEnabled(true);
				btnSave.setEnabled(true);
				btnViewGrnDetails.setEnabled(false);
			}
			else if(actGrnPut.LOCATION_MODE == WhApp.DELETEMODE){
				isRackFields(false);
				enableTextboxes(false);
				btnEdit.setEnabled(false);
				btnDelete.setEnabled(false);
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
		 etwPalletid.setBackgroundColor(Color.WHITE);
		 if(!set){
			 etwZone.setBackgroundColor(Color.parseColor("#66cdaa"));
			 etwRow.setBackgroundColor(Color.parseColor("#66cdaa"));
			 etwColumn.setBackgroundColor(Color.parseColor("#66cdaa"));
			 etwTier.setBackgroundColor(Color.parseColor("#66cdaa"));
		 }
		 else{
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
			 etwRow.setText(BLANKLOCATION); // 20140405 - gerry change to - instead of blank
			 etwColumn.setText(BLANKLOCATION); // 20140405 - gerry change to - instead of blank
			 etwTier.setText(BLANKLOCATION);// 20140405 - gerry change to - instead of blank
			 etwRow.setBackgroundColor(Color.parseColor("#d3d3d3"));
			 etwColumn.setBackgroundColor(Color.parseColor("#d3d3d3"));
			 etwTier.setBackgroundColor(Color.parseColor("#d3d3d3"));
			 etwLocation.setVisibility(View.GONE);
			 tvwLocation.setVisibility(View.GONE);
			 tvwLocSeparator.setVisibility(View.GONE);
		 }
		 else{
			 etwRow.setBackgroundColor(Color.parseColor("#87cefa"));
			 etwColumn.setBackgroundColor(Color.parseColor("#87cefa"));
			 etwTier.setBackgroundColor(Color.parseColor("#87cefa"));
			 etwLocation.setVisibility(View.VISIBLE);
			 tvwLocation.setVisibility(View.VISIBLE);
			 tvwLocSeparator.setVisibility(View.VISIBLE);
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
				if(bool){	
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
		
		newPallet.setHasRack(rbnRack.isChecked() ? true : false);
		
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
        			if(validateFields(errMsg)) {
    					if(actGrnPut.LOCATION_MODE == WhApp.ADDMODE){
    						if(!actGrnPut.whApp.getGrnHeader().isPalletIDExist(getActivity(), etwPalletid.getText().toString())){
                				fillPallet();
        		        		enableTextboxes(false); 
        		        		actGrnPut.isLocationSaved = true;
        		        		showSuccessErrorDialog(getString(R.string.msgSavedSuccess),getString(R.string.toastSaveInMemory));
        		        		btnSave.setEnabled(false);        						
        					}
        				}
        			}
        			else{
        				throw new WhAppException(errMsg[0].toString());
        			}
	        		actGrnPut.LOCATION_MODE = WhApp.DELETEMODE;	        		
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
    
    @SuppressLint("DefaultLocale")
    private void displayMultiFields(String scannedLocation, String separator){
    	if(scannedLocation.length() > 0){    		
    		
    		StringTokenizer tokens = new StringTokenizer(scannedLocation, separator);
    		
    		//compositeField = scannedLocation.trim().split(separator.toString()); // initialize the 4 fields(Zone, Row, Column, Tier) to be blank
    		etwZone.setText(tokens.nextToken().toString().toUpperCase(Locale.getDefault()));
    		if(rbg.getCheckedRadioButtonId()== rbnRack.getId()){
	     		switch(tokens.countTokens()){	     		
		    		case 1:// 	    	
		    			etwRow.setText(tokens.nextToken().toString().trim().toUpperCase(Locale.getDefault()));
		    			etwColumn.setText(BLANKLOCATION); //put "-" for blank column
		    			etwTier.setText(BLANKLOCATION);//put "-" for blank tier
		    			break;
		    		case 2: 	    	
		    			etwRow.setText(tokens.nextToken().toString().trim().toUpperCase(Locale.getDefault()));
		    			etwColumn.setText(tokens.nextToken().toString().trim().toUpperCase(Locale.getDefault()));
		    			etwTier.setText(BLANKLOCATION);//put "-" for blank tier
		    			break;
		    		case 3: 	    	
		    			etwRow.setText(tokens.nextToken().toString().trim().toUpperCase(Locale.getDefault()));
		    			etwColumn.setText(tokens.nextToken().toString().trim().toUpperCase(Locale.getDefault()));
		    			etwTier.setText(tokens.nextToken().toString().trim().toUpperCase(Locale.getDefault()));
		    			break;	    			
		    		default:
		    			break;    			
	     		}	    		
     		}
    		btnSave.requestFocus();	
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
						displayMultiFields(scannedLocation, actGrnPut.whApp.getWhMobileSettings().getLocSeparator());				
					}		
				}
			}
			catch(NullPointerException ex){
				ex.printStackTrace();
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
    public Boolean validateAgainstCompositeField(String[] compositeField, String[] outString) throws WhAppException{
    	outString[0] = "";
    	if(etwLocation.getVisibility() == View.VISIBLE && compositeField != null){
    		if(!compositeField[0].toString().equalsIgnoreCase(etwZone.getText().toString())){
    			outString[0] += getString(R.string.errNotSameZone);
    		}
    		if(!compositeField[1].toString().equalsIgnoreCase(etwRow.getText().toString())){
    			outString[0] += "\n"+getString(R.string.errNotSameRow);
    		}
    		if(!compositeField[2].toString().equalsIgnoreCase(etwColumn.getText().toString())){
    			outString[0] += "\n"+getString(R.string.errNotSameColumn);
    		}
    		if(!compositeField[3].toString().equalsIgnoreCase(etwTier.getText().toString())){
    			outString[0] += "\n"+getString(R.string.errNotSameTier);
    		}
    	}
    	if(!outString[0].toString().equals("")){
    		throw new WhAppException(outString[0]);
    	}    	
    	return true;
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
