package sg.com.innosys.wms.UI.Common;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings.Secure;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import sg.com.innosys.wms.BLL.Common.LoginInfo;
import sg.com.innosys.wms.BLL.Common.WhApp;
import sg.com.innosys.wms.BLL.Common.WhAppException;
import sg.com.innosys.wms.R;
import sg.com.innosys.wms.R.id;
import sg.com.innosys.wms.R.layout;
import sg.com.innosys.wms.R.string;
import sg.com.innosys.wms.UI.Crating.ActCrateMain;
import sg.com.innosys.wms.UI.GIN.ActGinDownload;
import sg.com.innosys.wms.UI.GRN.ActGrnDownload;


public class ActWmsMain extends Activity{
	
	Button btnLogout;
	Button btnGrn;
	Button btnGin;
	Button btnCrating;
	Button btnStockCheck;
	public TextView tvwUserID;
	public TextView tvwDatabase;
	public WhApp whApp;
	
	public static int userAutentication;
	public static final int autenticateFromSQLite =1;
	public static final int autenticateFromServer =2;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wmsmain);		
		whApp = (WhApp) getApplication();
		if(WhApp.device_ID == null){
			WhApp.device_ID = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
		}
		btnLogout = (Button) findViewById(R.id.btnLogout);
		btnGrn = (Button) findViewById(R.id.btnGRN);
		btnGin = (Button) findViewById(R.id.btnGIN);
		btnCrating = (Button) findViewById(R.id.btnCrating);
		btnStockCheck = (Button) findViewById(R.id.btnStockCheck);
		tvwUserID = (TextView) findViewById(R.id.tvvUserId);
		tvwDatabase = (TextView) findViewById(R.id.tvvDatabase);
		//btnLogin.setEnabled(false);
		//btnGin.setEnabled(false);
		//btnCrating.setEnabled(false);//not yet ready
		btnStockCheck.setEnabled(false);
		
		btnLogout.setOnClickListener(btnClick);
		btnGrn.setOnClickListener(btnClick);
		btnGin.setOnClickListener(btnClick);
		btnCrating.setOnClickListener(btnClick);
		btnStockCheck.setOnClickListener(btnClick);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
		
		validateUser();
		enableCreating();

	}
	
	private void enableCreating(){
		try {
			if(whApp.getAllGinHeaders().size() > 0){
				btnCrating.setEnabled(true);
			}else{
				btnCrating.setEnabled(false);				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void validateUser(){
		try {
			if(whApp.getLoginInfo() == null){				
				if(LoginInfo.getLoginInfoFromSQLite(this) == null){
					userAutentication = autenticateFromServer;				
				}
				else{
					userAutentication = autenticateFromSQLite;
					whApp.setLoginInfo(LoginInfo.getLoginInfoFromSQLite(this));
					refreshDisplay();   				
				}
				DiaFragLogin.newInstance().show(getFragmentManager(), "dialog");	
			}
			else{
				refreshDisplay();    	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void refreshDisplay(){
		if(whApp.getLoginInfo() != null){	
			tvwUserID.setText(whApp.getLoginInfo().getUserId());
			tvwDatabase.setText(whApp.getLoginInfo().getCompanyName()+"[" + whApp.getLoginInfo().getDatabaseName()+"]");				
		}
		else{
			tvwUserID.setText(new LoginInfo().getUserId());
			tvwDatabase.setText(new LoginInfo().getCompanyName()+"[" + new LoginInfo().getDatabaseName()+"]");	
		}
	}
	@Override 
	public void onDestroy(){
		 super.onDestroy();
	 }
	@Override 
	public void onResume(){			
		if(WhApp.device_ID == null){
			WhApp.device_ID = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
		}
		enableCreating();	
		super.onResume();
	 }
	private OnClickListener btnClick = new OnClickListener(){
		public void onClick(View view) {
			switch(view.getId()){
			case R.id.btnGRN:
				//do GRN
				Intent intGrn = new Intent(ActWmsMain.this, ActGrnDownload.class);
				startActivity(intGrn);
				break;
			case R.id.btnGIN:
				//do GIN
				Intent intGin = new Intent(ActWmsMain.this, ActGinDownload.class);
				startActivity(intGin);
				break;
			case R.id.btnCrating:
				//do Crating
				Intent intCrate = new Intent(ActWmsMain.this, ActCrateMain.class);
				startActivity(intCrate);
				break;
			case R.id.btnStockCheck:
				//do stock check
				break;
			case R.id.btnLogout:
				if(whApp.getLoginInfo() != null){
					try {
						String[] outString = new String[1];
						if(whApp.getLoginInfo().isLoginInfoValidToDelete(ActWmsMain.this, outString)){
							showLogoutWarningDialog(getString(R.string.msgWarning), getString(R.string.msgConfirmLogout)).show();							
							whApp.getLoginInfo().deleteLoginInfo(getApplicationContext());
						}
						else{
							showLogoutWarningDialog(getString(R.string.msgWarning),outString[0].toString() + getString(R.string.msgConfirmLogout)).show();
						}					
					} catch (WhAppException e) {
						e.printStackTrace();
						showSuccessErrorDialog(getString(R.string.msgError),e.getLocalizedMessage());
					} catch (Exception e) {
						e.printStackTrace();
						showSuccessErrorDialog(getString(R.string.msgError),e.getLocalizedMessage());
					}
				}
				break;
			}		
		}			
	};
	
	private Dialog showSuccessErrorDialog(String title, String msg){
	    AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
	private Dialog showLogoutWarningDialog(String title, String msg){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setMessage(msg)
	    	   .setTitle(title)
	           .setCancelable(false)
        	   .setPositiveButton(getString(R.string.msgBtnYes), new DialogInterface.OnClickListener() {
        		   public void onClick(DialogInterface dialog, int id) {							
						whApp.setLoginInfo(null);	
						ActWmsMain.this.finish();						
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
