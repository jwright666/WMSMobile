package sg.com.innosys.wms.UI.Crating;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URLEncoder;

import sg.com.innosys.wms.BLL.Common.LoginInfo;
import sg.com.innosys.wms.BLL.Common.WhApp;
import sg.com.innosys.wms.BLL.Common.WhAppException;
import sg.com.innosys.wms.BLL.Crating.GinCrate;
import sg.com.innosys.wms.BLL.GIN.GinHeader;
import sg.com.innosys.wms.DAL.GIN.DbGin;
import sg.com.innosys.wms.R;

public class ActCrateMain extends Activity{

	public static final String SELECTEDCRATE = "SELECTEDCRATE";
	public static final String GINHEADER = "GINHEADER";
	public static final String CRATEMODE ="CRATEMODE";
	
	public WhApp whApp;
	public GinCrate selectedGinCrate;
	public DbGin dbWhGin;
	//public GinHeader ginHeader;
	private Button btnAddNewCrate;
	private Button btnUpload;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.main_crate);
       this.setTitle(R.string.tabCrateMain);   
       whApp = (WhApp) getApplication();   
       dbWhGin = new DbGin(this);
       
       btnAddNewCrate = (Button) findViewById(R.id.btnAddNewCrate);
       btnUpload = (Button) findViewById(R.id.btnUploadGINandCrateData);
       //ginHeader = whApp.getGinHeader();
       if(whApp.getGinHeader() == null){
    	   try {
    		   whApp.setGinHeader(dbWhGin.getAllGinHeader().get(0));

    	   } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
    	   }
       }
	   //if(whApp.getGinHeader().getIsConfirmedGin()){
		   btnAddNewCrate.setOnClickListener(btnClicked);
		   btnUpload.setOnClickListener(btnClicked);
	   //}
       if(savedInstanceState != null){
    	   selectedGinCrate = (GinCrate) savedInstanceState.getSerializable(SELECTEDCRATE);
    	   whApp.setGinHeader((GinHeader) savedInstanceState.getSerializable(GINHEADER));
       }        
       StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	   StrictMode.setThreadPolicy(policy); 
    }
    @Override                           
    public void onSaveInstanceState(Bundle outState){
    	super.onSaveInstanceState(outState);
    	outState.putSerializable(SELECTEDCRATE, selectedGinCrate);
    	outState.putSerializable(GINHEADER, whApp.getGinHeader());
    }   
    @Override 
    public void onResume(){
    	super.onResume(); 
    	//ginHeader = whApp.getGinHeader();
    }  
	@Override
    public void onPause(){
		super.onPause();
	   if(whApp.getGinHeader().getIsConfirmedGin()){
		   btnAddNewCrate.setOnClickListener(btnClicked);
		   btnUpload.setOnClickListener(btnClicked);
	   }
	}
	@Override
    public void onDestroy(){
		super.onDestroy();
	}
	
	private void uploadObjectCollection() throws org.apache.http.ParseException, Exception{		
        String[] sqlLogin = LoginInfo.getSqlLogin();
        String sqlUserName = sqlLogin[0].toString();
        String sqlUserPassword = sqlLogin[1].toString();              

        String serverName = URLEncoder.encode(whApp.getLoginInfo().getDbServerName(),"UTF-8");
        
        String url = WhApp.URL_string + "UploadGinHeader?userid=" + whApp.getLoginInfo().getUserId() + "&deviceId=" + whApp.getLoginInfo().getAndroidID()
				+"&serverName=" + serverName + "&dbName=" + whApp.getLoginInfo().getDatabaseName()
				+ "&sqlUserId=" + sqlUserName + "&sqlUserPassword=" + sqlUserPassword;

        UploadGIN uploadTask = new UploadGIN();
        uploadTask.execute(new String[] {url} );
	}
	
	private OnClickListener btnClicked = new OnClickListener(){

		public void onClick(View btn) {

			try {
			if(btn == btnAddNewCrate){
				// TODO call ActCrateProduct, // show new intent for ActCrateProduct
				if(whApp.getGinHeader().getIsConfirmedGin()){
					Intent intent = new Intent(ActCrateMain.this, ActCrateProduct.class);
					intent.putExtra(CRATEMODE, WhApp.ADDMODE);
					if(selectedGinCrate != null)
						intent.putExtra(SELECTEDCRATE, selectedGinCrate);
					
					startActivity(intent);	
				}
				else{
					throw new WhAppException(getString(R.string.msgCantDoCratingForUnConfirmedGIN));
				}
			}
			if(btn == btnUpload){				
				String validateMsg = whApp.getGinHeader().validateGinDetailActQtyToCrate(getApplicationContext());
				
				if(validateMsg== null){
					showAlertDialogToUploadGIN(getString(R.string.msgConfirmUploadGINandCrating));
				}
				else{
					throw new WhAppException(validateMsg);
				}
			}
		}catch (WhAppException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			showSuccessErrorDialog(getString(R.string.msgError), e.getMessage().toString());
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			showSuccessErrorDialog(getString(R.string.msgError), e.getMessage().toString());
		}
		}
	};
	
	private Dialog showSuccessErrorDialog(String title, String msg){
	    AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setMessage(msg)
	    	   .setTitle(title)
	           .setCancelable(false);
	    AlertDialog alert = builder.create();
	    alert.setButton(getString(R.string.msgBtnOK), new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {	            	   
	               }});
	        alert.show();	        
			return alert;
	}
	private AlertDialog showAlertDialogToUploadGIN(String msg){
	    AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setMessage(msg)
	    	   .setTitle(getString(R.string.msgAlert))
	           .setCancelable(false)
	           .setPositiveButton(getString(R.string.msgBtnYes), new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {	
	            	   try {
	            		   //TODO upload GIN and crating informations
	            		   uploadObjectCollection();
	            		   /*if(output.toString().trim().equalsIgnoreCase("") || output.toString().trim() == null){
	            			   showSuccessErrorDialog(getString(R.string.msgAlert), "GIN "+getString(R.string.msgUploadSuccess));
	            			   //Clean up SQLite. remove all data from SQLite			            		
	            			   //remove ginheader from memory..	           			   
					       }
	            		   else{
	            			   throw new WhAppException(output); 
	            		   }
	            		   */
	            		   
						}catch (ParseException e) {
							e.printStackTrace();
							showSuccessErrorDialog(getString(R.string.msgError), e.getMessage().toString());
						} catch (Exception e) {
							e.printStackTrace();
							showSuccessErrorDialog(getString(R.string.msgError), e.getMessage().toString());
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

	private class UploadGIN extends AsyncTask<String, Integer, String> {
		private ProgressDialog dialog;
		int myProgress;
	    @Override
	    protected String doInBackground(String... urls) {
	      String result = "";
	      for (String url : urls) {
	        DefaultHttpClient client = new DefaultHttpClient();
	        client.getParams().setBooleanParameter("http.protocol.expect-continue", false);
			HttpPost request = new HttpPost(url);
			request.setHeader("User-Agent", "innosys.Android.app");
			request.setHeader("Accept", "application/json");
	        request.setHeader("Content-type", "application/json");    
	        try {         
		        StringEntity entity = whApp.getGinHeader().jsonGinHeaderString();
		        request.setEntity(entity);
		        entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
		        request.setEntity(entity);		        

	        	HttpResponse response = client.execute(request);  
	        	result = EntityUtils.toString(response.getEntity());

	        } catch (ClientProtocolException e) {
	    	    Log.e("ClientProtocolException", e.toString());
	    	    result = e.toString();
	        } catch (IOException e) {
	    	    Log.e("IO exception", e.toString());
	    	    result = e.toString();
	        }catch (Exception e) {
	        	e.printStackTrace();
	    	    result = e.toString();
	        }
	      }
	      return result;
	    }

	    @Override
	    protected void onPostExecute(String result) {
	    	try{
		    	if(result.equalsIgnoreCase("") || result == null){
		 		   showSuccessErrorDialog(getString(R.string.msgAlert), "GIN "+getString(R.string.msgUploadSuccess));
	 			   whApp.getGinHeader().clearSQLiteDatabase(getApplicationContext()); 
	 			   //whApp.setGinHeader(null);
	 			   FragGinHeaderForCrate fragGinHeaderForCrate = (FragGinHeaderForCrate)getFragmentManager().findFragmentById(R.id.fragGinHeaderForCrate);
	 			   fragGinHeaderForCrate.clearFields();	
	 			   FragGinCrateDetail fragGinCrateDetail = (FragGinCrateDetail)getFragmentManager().findFragmentById(R.id.fragGinCrateDetail);
	 			   fragGinCrateDetail.clearAdapter();
	 			   btnAddNewCrate.setEnabled(false);
	 			   btnUpload.setEnabled(false);
		    	}
		    	else{
		    		throw new WhAppException(result.toString());
		    	} 
	    	}catch(WhAppException e){
	 			 showSuccessErrorDialog(getString(R.string.msgError), e.getLocalizedMessage());		    		
	    	}catch(Exception e){
	 			 showSuccessErrorDialog(getString(R.string.msgError), e.getLocalizedMessage());		    		
	    	}
	    	dialog.dismiss();
	    }
	    
	    @Override
	    protected void onProgressUpdate(Integer... values) {
		    // TODO Auto-generated method stub
	    	dialog.setProgress(values[0]);
	    }
	    @Override
	    protected void onPreExecute() {
		     // TODO Auto-generated method stub
	    	dialog = ProgressDialog.show(ActCrateMain.this, getString(R.string.msgInProgress), String.format(getString(R.string.msgUploading), "GIN"), true);	
		    myProgress = 0;
		    dialog.setMax(100);
	    }
	  }

}
