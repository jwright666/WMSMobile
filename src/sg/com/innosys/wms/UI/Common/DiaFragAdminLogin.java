package sg.com.innosys.wms.UI.Common;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import sg.com.innosys.wms.BLL.Common.WhAppException;
import sg.com.innosys.wms.R;
import sg.com.innosys.wms.UI.GIN.ActGinPick;

@SuppressLint("NewApi")
public class DiaFragAdminLogin extends DialogFragment {
	
	public ActGinPick actGinPick;
	public View vDiaFragLogin;
	private Dialog dgLogin;
	private Button btnLogin;
	private Button btnCancel;
	private EditText etwUserName;
	private EditText etwPassword;
	
	public static DiaFragAdminLogin newInstance() {
		DiaFragAdminLogin f = new DiaFragAdminLogin();
	        return f;	        
	    }
	public DiaFragAdminLogin(){}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		
		actGinPick = (ActGinPick) getActivity();
		btnLogin.setOnClickListener(btnOnClickListener);
		btnCancel.setOnClickListener(btnOnClickListener);
		
	}
	@Override 
	public void onDestroy(){
		super.onDestroy();
		this.dismiss();
	 }
	@Override 
	public void onResume(){
		 super.onResume();
	 }

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {		 
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.diaLoginInfo);
        builder.setView(getContentView());
        dgLogin = builder.create();
        dgLogin.setCanceledOnTouchOutside(false);
        return dgLogin;
    }	
	private View getContentView() {
		//getActivity().getWindow().requestFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        LayoutInflater inflater = getActivity().getLayoutInflater();  
        vDiaFragLogin =	inflater.inflate(R.layout.login, null);
       // pnlCompanyInfo = (RelativeLayout) vDiaFragLogin.findViewById(R.id.pnlCompany);
        btnLogin =(Button) vDiaFragLogin.findViewById(R.id.btnAdmin_login);
        btnCancel =(Button) vDiaFragLogin.findViewById(R.id.btnAdmin_CancelLogin);
        etwUserName = (EditText)vDiaFragLogin.findViewById(R.id.etwAdmin_Username);
        etwPassword = (EditText)vDiaFragLogin.findViewById(R.id.etwAdmin_Password);   
		return vDiaFragLogin;		
	}
	private OnClickListener btnOnClickListener = new OnClickListener(){

		public void onClick(View btn) {
			// TODO Auto-generated method stub
			try{
				validateFields();
				if(btn == btnLogin){
					//TODO validate login from server
				}
				else if(btn == btnCancel){
					//TODO
				}
			}
	   		 catch (WhAppException e) {
				 e.printStackTrace();
				 showSuccessErrorDialog(getString(R.string.msgLoginFailed), e.getLocalizedMessage());				
	   		 }			
	   		 catch (Exception e) {
				 e.printStackTrace();
				 showSuccessErrorDialog(getString(R.string.msgLoginFailed), e.getLocalizedMessage());				
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
	@SuppressWarnings("unused")
	private Boolean validateFields() throws WhAppException{
		try{
			if(etwUserName.getText().toString().equalsIgnoreCase("")){
				throw new WhAppException(getString(R.string.errEmptyUserName));
			}
			if(etwPassword.getText().toString().equalsIgnoreCase("")){
				throw new WhAppException(getString(R.string.errEmptyUserPassword));
			}
		}catch (WhAppException e) {	
			throw e;
  		 }
  		 catch (Exception e) {	
  			 throw new WhAppException(e.getLocalizedMessage());
  		 }		
		return true;		
	}
}
