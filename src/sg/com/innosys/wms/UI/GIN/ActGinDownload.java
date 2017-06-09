package sg.com.innosys.wms.UI.GIN;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.ini4j.InvalidFileFormatException;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import sg.com.innosys.wms.BLL.Common.LoginInfo;
import sg.com.innosys.wms.BLL.Common.WhApp;
import sg.com.innosys.wms.BLL.Common.WhAppException;
import sg.com.innosys.wms.BLL.Common.WhEnum.GINPickSortOption;
import sg.com.innosys.wms.BLL.GIN.GinDetail;
import sg.com.innosys.wms.BLL.GIN.GinHeader;
import sg.com.innosys.wms.BLL.GIN.GinPick;
import sg.com.innosys.wms.DAL.GIN.DbGin;
import sg.com.innosys.wms.R;
import sg.com.innosys.wms.UI.Common.ListViewItems;

public class ActGinDownload extends Activity{
	private Button btnDownload;
	private Button btnUpload;
	private Button btnConfirmGin;
	private Button btnCancelGin;
	private EditText txtViewTrxNo;
	private EditText txtViewCustCode;
	private ListView lvList;
	
	public TextView tvwUserID;
	public TextView tvwDeviceId;
	public TextView tvwDatabase;
	private RadioButton rbnSortByLocation;
	private RadioButton rbnSortByLineNo;
	private RadioGroup rbg;
	
	private WhApp whApp;
	private DbGin dbWhGin;
	private GinHeaderListAdapter adpGINHeaders;
	private ArrayList<GinHeader> ginHeaderList = new ArrayList<GinHeader>();
	private Boolean isDownloaded = false;
	
	private String[] sqlLogin;
	private String sqlUserID;
	private String sqlUserPassword;
	
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gin_download);
		this.setTitle(R.string.btnGIN);
		whApp = (WhApp) getApplication();
		tvwUserID = (TextView) findViewById(R.id.tvvUserId);
		tvwDeviceId = (TextView) findViewById(R.id.tvvDeviceId);
		tvwDatabase = (TextView) findViewById(R.id.tvvDatabase);
		txtViewTrxNo = (EditText)findViewById(R.id.d_etvTrxNo);
		txtViewCustCode = (EditText)findViewById(R.id.d_etwCustCode);
		lvList = (ListView) findViewById(R.id.lvvGinDownloadedList);
		btnDownload = (Button)findViewById(R.id.d_btnDownload);
		btnUpload = (Button)findViewById(R.id.d_btnUpload);
		btnConfirmGin = (Button)findViewById(R.id.d_btnConfirmGin);
		btnCancelGin = (Button)findViewById(R.id.d_btnCancelGin);
		
		btnDownload.setOnClickListener(btnClick);
		btnUpload.setOnClickListener(btnClick);
		btnConfirmGin.setOnClickListener(btnClick);
		btnCancelGin.setOnClickListener(btnClick);

		rbg = (RadioGroup)findViewById(R.id.rbgSort_Option);
		rbnSortByLocation = (RadioButton)findViewById(R.id.rbnOpt_SortByLocation);
		rbnSortByLineNo = (RadioButton)findViewById(R.id.rbnOpt_SortByLineNo);

		rbg.setOnCheckedChangeListener(rbnCheckedChanged);
		//initialize to if first launch the activity
		//rbnSortByLocation.setChecked(true); 
		rbnSortByLineNo.setChecked(true);
		rbnSortByLocation.setEnabled(true);//temporary disable
		
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
		if(savedInstanceState != null){
			ginHeaderList = (ArrayList<GinHeader>) savedInstanceState.get("HEADERLIST");
			isDownloaded = savedInstanceState.getBoolean("ISDOWNLOADED");
			if(whApp.getGINPickSortOption() == GINPickSortOption.SORT_BY_LOCATION)
				rbnSortByLocation.setChecked(true);
			else
				rbnSortByLineNo.setChecked(true);
		}

		tvwUserID.setText(whApp.getLoginInfo().getUserId().toString());
		tvwDeviceId.setText(whApp.getLoginInfo().getAndroidID().toString());
		tvwDatabase.setText(whApp.getLoginInfo().getCompanyName()+"[" + whApp.getLoginInfo().getDatabaseName()+"]");

		getSQLLogin();
	}
	private void getSQLLogin(){
		try {
			sqlLogin = LoginInfo.getSqlLogin();
			sqlUserID = sqlLogin[0].toString();
			sqlUserPassword = sqlLogin[1].toString();			
		} catch (InvalidFileFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
    public void onResume(){
    	super.onResume();
    	whApp = (WhApp) getApplication();
		try {
			if(whApp.getLoginInfo() == null){
				WhApp.device_ID = Secure.getString(getContentResolver(), Secure.ANDROID_ID);			
				LoginInfo.getLoginInfoFromSQLite(this);
			}
			refreshAdapter();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
    public void onSaveInstanceState(Bundle outState){
    	super.onSaveInstanceState(outState);
    	outState.putSerializable("HEADERLIST", ginHeaderList);
    	outState.putBoolean("ISDOWNLOADED", isDownloaded);
    } 

	private void intializeButtons(){
		if(isDownloaded){
			btnDownload.setEnabled(false);
			btnUpload.setEnabled(true);
			btnConfirmGin.setEnabled(true);
			btnCancelGin.setEnabled(true);
		}
		else{
			btnDownload.setEnabled(true);
			btnUpload.setEnabled(false);
			btnConfirmGin.setEnabled(false);
			btnCancelGin.setEnabled(false);			
		}
	}
	
	public void refreshAdapter() {
	try{
		if(ginHeaderList.size() <= 0){		
			if(whApp.getAllGinHeaders().size() > 0 ){
				ginHeaderList.add(whApp.getGinHeader());
				isDownloaded = true; 
				txtViewTrxNo.setText(Integer.toString(ginHeaderList.get(0).getTransactionNo()));	  						
			}
		}	
    	intializeButtons();
    	adpGINHeaders = new GinHeaderListAdapter(this, R.layout.row_grndownloadedlist, ginHeaderList);
    	lvList.setOnItemClickListener(itemClicked);	
		lvList.setAdapter(adpGINHeaders);	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }		 
	@SuppressLint("ShowToast")
	private OnClickListener btnClick = new OnClickListener(){	
		public void onClick(View btn) {
			 try {
				 if(btn == btnDownload){				 
						downloadGinHeaders();	
						refreshAdapter();					
				 }
				 else if(btn == btnUpload){
					 if(whApp.getGinHeader().getIsConfirmedGin()){
						 //TODO upload Gin 
						 if(whApp.getGinHeader().ginCrates.size() == 0){
							 showAlertDialogToUpload(getString(R.string.msgWarnUploadGIN) + " Without crating");
						 }
						 else{
							 showAlertDialogToUpload(getString(R.string.msgWarnUploadGIN));
						 }						 
					 }
					 else{
						 showSuccessErrorDialog(getString(R.string.msgAlert),getString(R.string.errConfirmedGINBeforeUpload));
					 }
				 }
				 else if(btn == btnConfirmGin){
					 if(!whApp.getGinHeader().getIsConfirmedGin()){
						 String checkGIN = whApp.getGinHeader().validateGinDetailActQtyToSrvQty(getApplicationContext());
						 if(!checkGIN.trim().equals("")){
							 showAlertDialogToConfirm(checkGIN.toString() + "\n\n" + getString(R.string.msgWarnConfirmGIN));
						 }
						 else{
							 showAlertDialogToConfirm(getString(R.string.msgWarnConfirmGIN));
						 }
					 }
					 else{
						 showSuccessErrorDialog(getString(R.string.msgAlert),getString(R.string.msgAlreadyConfirmedGIN));
					 }
				 }		
				 else if(btn == btnCancelGin){
					 //TODO Cancel GIN
					 showAlertDialogToCancel(String.format(getString(R.string.msgWarnCancelDownload), "GIN"));
				 }			 
			 } 			
		 	catch (ClientProtocolException e) {
				e.printStackTrace();
				 showSuccessErrorDialog(getString(R.string.msgError),e.getLocalizedMessage().toString());
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
				 showSuccessErrorDialog(getString(R.string.msgError),e.getLocalizedMessage().toString());
			} catch (WhAppException e) {
				e.printStackTrace();
				 showSuccessErrorDialog(getString(R.string.msgError),e.getLocalizedMessage().toString());
			} catch (Exception e) {
				e.printStackTrace();
				showSuccessErrorDialog(getString(R.string.msgError),e.getLocalizedMessage().toString());
			}
		 }
		};
	private OnItemClickListener itemClicked = new OnItemClickListener(){
		public void onItemClick(AdapterView<?> adapter, View view, int position,
				long arg3) {
			try {	
				whApp.setGinHeader(ginHeaderList.get(position));				
				if(!isDownloaded){			
					downloadGinDetails();
				}						
				else{
   		            //20130213
   		            whApp.getGinHeader().createGinPicksByLocation();	
	   				Intent intent = new Intent(ActGinDownload.this, ActGINMain.class);
	   				startActivity(intent);	
   				}
   			    
			} catch (WhAppException e) {
				whApp.setGinHeader(null);
				e.printStackTrace();
	            showSuccessErrorDialog(getString(R.string.msgError), e.getLocalizedMessage());
			}	catch (Exception e) {
				whApp.setGinHeader(null);
				e.printStackTrace();
	            showSuccessErrorDialog(getString(R.string.msgError), e.getLocalizedMessage());
			}			
       }			
	};
    private OnCheckedChangeListener rbnCheckedChanged = new OnCheckedChangeListener(){
		public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {				
		
		switch(checkedId){
			case R.id.rbnOpt_SortByLocation:
				//rbnSortByLocation.setChecked(true);
				whApp.setGINPickSortOption(GINPickSortOption.SORT_BY_LOCATION);
				break;
			
			case R.id.rbnOpt_SortByLineNo:
				//rbnSortByLineNo.setChecked(true);
				whApp.setGINPickSortOption(GINPickSortOption.SORT_BY_ITEMCODE);
				break;
			}				
		}								
	};
	
	private void downloadGinHeaders() throws org.apache.http.ParseException, JSONException, WhAppException, Exception{
		//later we replace to the value from criteria..
		String custCode = txtViewCustCode.getText().toString().toUpperCase();//"Cust1";
		int trxNo =0;
		if(!txtViewTrxNo.getText().toString().trim().equals("")){
			trxNo = Integer.parseInt(txtViewTrxNo.getText().toString());
		}
		//encode valid url string format replaced special characters such as "\", " " etc
		//in this case \ for serverName will be replaced with %5C for valid url 
        String serverName = URLEncoder.encode(whApp.getLoginInfo().getDbServerName(),"UTF-8"); 
        
		String url = WhApp.URL_string + "GetGinHeaders?CustCode="+custCode+"&TrxNo="+trxNo +"&serverName=" + serverName + "&dbName=" + whApp.getLoginInfo().getDatabaseName()
				+ "&sqlUserId=" + sqlUserID + "&sqlUserPassword=" + sqlUserPassword; 
		
		GINHeaderDownloadTask downloadTask = new GINHeaderDownloadTask();
		downloadTask.execute(new String[] { url });	
		
	 }	 	
	private void downloadGinDetails() throws UnsupportedEncodingException, WhAppException, Exception{
		//encode valid url string format replaced special characters such as "\", " " etc
		//in this case \ for serverName will be replaced with %5C for valid url 
        String serverName = URLEncoder.encode(whApp.getLoginInfo().getDbServerName(),"UTF-8");
		
		String url = WhApp.URL_string + "GetGinHeader/GetGinDetails?trx=" + whApp.getGinHeader().getTransactionNo() + "&serverName=" + serverName
						+ "&dbName=" + whApp.getLoginInfo().getDatabaseName() + "&sqlUserId=" + sqlUserID + "&sqlUserPassword=" + sqlUserPassword; 
		
		GINDetailDownloadTask ginDetailDownloadTask = new GINDetailDownloadTask();
		ginDetailDownloadTask.execute(new String[] {url} );		
		
	}
	
	private void uploadObjectCollection() throws org.apache.http.ParseException, JSONException, WhAppException, Exception{
		//encode valid url string format replaced special characters such as "\", " " etc
		//in this case \ for serverName will be replaced with %5C for valid url 
        String serverName = URLEncoder.encode(whApp.getLoginInfo().getDbServerName(),"UTF-8");
        
		String url = WhApp.URL_string + "UploadGinHeader?userid="+ whApp.getLoginInfo().getUserId() +"&deviceId="+whApp.getLoginInfo().getAndroidID()
											+"&serverName=" + serverName + "&dbName=" + whApp.getLoginInfo().getDatabaseName()
											+ "&sqlUserId=" + sqlUserID + "&sqlUserPassword=" + sqlUserPassword;
		
		GINUploadTask uploadTask = new GINUploadTask();
        uploadTask.execute(new String[] {url} );	 
	}
	private boolean lockedGinHeader(GinHeader selectGinHeader) throws InvalidFileFormatException, IOException, WhAppException{
		boolean isGinHeaderLockedSuccess =false;	
		//encode valid url string format replaced special characters such as "\", " " etc
		//in this case \ for serverName will be replaced with %5C for valid url 
        String serverName = URLEncoder.encode(whApp.getLoginInfo().getDbServerName(),"UTF-8");
        
		HttpClient  httpclient = new DefaultHttpClient();
		
		String reqURL = "LockGinHeader?userid=" +whApp.getLoginInfo().getUserId()+"&deviceId="+ whApp.getLoginInfo().getAndroidID() +"&trxNo="+selectGinHeader.getTransactionNo()
							+"&serverName=" + serverName + "&dbName=" + whApp.getLoginInfo().getDatabaseName()
							+ "&sqlUserId=" + sqlUserID + "&sqlUserPassword=" + sqlUserPassword;
		
		HttpGet requestLock = new HttpGet(WhApp.URL_string + reqURL); 				
		requestLock.setHeader("User-Agent", "innosys.Android.app");
		requestLock.setHeader("Accept", "application/json");
		requestLock.setHeader("Content-type", "application/json");         

		String lockResponse=null;
		try {
			HttpResponse response = httpclient.execute(requestLock);
			lockResponse = EntityUtils.toString(response.getEntity());
			
			if(lockResponse.toString().trim().equalsIgnoreCase("") || lockResponse.toString().trim() == null){ 
					isGinHeaderLockedSuccess = true;
			}
			else{
				throw new WhAppException(lockResponse.toString().replace("\\u000a", ""));
			}			
		} catch (WhAppException e1) {
			e1.printStackTrace();
			throw e1;
		}catch (ClientProtocolException e1) {
			e1.printStackTrace();
			throw new WhAppException( e1.toString());
		} catch (IOException e1) {
			e1.printStackTrace();
			throw new WhAppException( e1.toString());
		}
		return isGinHeaderLockedSuccess;	
	}
	private void cancelDownloadedGIN(int trxNo) throws UnsupportedEncodingException, ClientProtocolException, WhAppException, Exception{
		//encode valid url string format replaced special characters such as "\", " " etc
		//in this case \ for serverName will be replaced with %5C for valid url 
        String serverName = URLEncoder.encode(whApp.getLoginInfo().getDbServerName(),"UTF-8");      
		
		String url = WhApp.URL_string + "CancelGin?userid=" +whApp.getLoginInfo().getUserId()+"&deviceId="+ whApp.getLoginInfo().getAndroidID() +"&trxNo="+trxNo
							+"&serverName=" + serverName + "&dbName=" + whApp.getLoginInfo().getDatabaseName()
							+ "&sqlUserId=" + sqlUserID + "&sqlUserPassword=" + sqlUserPassword;		
		
		GINCancelDownloadTask cancelDownloadTask = new GINCancelDownloadTask();
		cancelDownloadTask.execute(new String[] { url });
	}
	
	
	//not yet in used waiting for specs to confirm GIN
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
	private AlertDialog showAlertDialogToConfirm(String msg){
	    AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setMessage(msg)
	    	   .setTitle(getString(R.string.msgAlert))
	           .setCancelable(false)
	           .setPositiveButton(getString(R.string.msgBtnYes), new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {	
	            	   try {
	            		   //TODO GIN confirmation
	            		   whApp.getGinHeader().confirmGin(getApplicationContext());
	            		   
	            		   showSuccessErrorDialog(getString(R.string.msgAlert), getString(R.string.msgWarnConfirmGINSuccess));
	            		   
						} catch (ParseException e) {
							e.printStackTrace();
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
	private AlertDialog showAlertDialogToUpload(String msg){
	    AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setMessage(msg)
	    	   .setTitle(getString(R.string.msgAlert))
	           .setCancelable(false)	           
	           .setPositiveButton(getString(R.string.msgBtnYes), new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {	
	            	   try {
	            		   //TODO GIN Upload
	            		   uploadObjectCollection();
	            		   /*
	            		   if(output.toString().trim().equalsIgnoreCase("") || output.toString().trim() == null){
	            			   showSuccessErrorDialog(getString(R.string.msgAlert), "GIN "+getString(R.string.msgUploadSuccess));
	            			 //Clean up SQLite. remove all data from SQLite
			            		whApp.getGinHeader().clearSQLiteDatabase(getApplicationContext());
					        	isDownloaded = false;
					        	txtViewTrxNo.setText("");
								ginHeaderList = new ArrayList<GinHeader>();
					        	whApp.setGinHeader(null);
								refreshAdapter();
	            		   }
	            		   else{
	            			   showSuccessErrorDialog(getString(R.string.msgUploadFailed), output); 
	            		   }
	            		   */
						} catch (ParseException e) {
							e.printStackTrace();
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
	private AlertDialog showAlertDialogToCancel(String msg) throws WhAppException{
	    AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setMessage(msg)
	    	   .setTitle(getString(R.string.msgAlert))
	           .setCancelable(false)	           
	           .setPositiveButton(getString(R.string.msgBtnYes), new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {	
	            	   try {
	            		   //TODO GIN Upload
	            		   cancelDownloadedGIN(whApp.getGinHeader().getTransactionNo());
	            		   /*if(cancelDownloadedGIN(whApp.getGinHeader().getTransactionNo())){
	            			   showSuccessErrorDialog(getString(R.string.msgAlert), String.format(getString(R.string.msgDownloadHasBeenCancelled), "GIN"));
	            			 //Clean up SQLite. remove all data from SQLite
			            		whApp.getGinHeader().clearSQLiteDatabase(getApplicationContext());
					        	isDownloaded = false;
					        	txtViewTrxNo.setText("");
								ginHeaderList = new ArrayList<GinHeader>();
					        	whApp.setGinHeader(null);
								refreshAdapter();
	            		   }
	            		   */
						} catch (ParseException e) {
							e.printStackTrace();
							showSuccessErrorDialog(getString(R.string.msgError), e.getLocalizedMessage());
						} catch (Exception e) {
							e.printStackTrace();
							showSuccessErrorDialog(getString(R.string.msgError), e.getLocalizedMessage());
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
	
	public class GinHeaderListAdapter extends ArrayAdapter<GinHeader> {
		 ArrayList<GinHeader> mGinHeaders;

    	public GinHeaderListAdapter(Context context, int textViewResourceId,
    			ArrayList<GinHeader> ginHeaders) {
             super(context, textViewResourceId, ginHeaders);
             mGinHeaders = ginHeaders;
 
         }
    	
		public View getView(int position, View convertView, ViewGroup parent) {
              View v = convertView;
              if (v == null) {

                  LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                  v = vi.inflate(R.layout.row_gindownloadedlist, null);	                  
              }
              
              GinHeader item = mGinHeaders.get(position);
              
              if (item != null) {
            	  ListViewItems trxNoView = (ListViewItems) v.findViewById(R.id.row_dlTrxNo);
            	  ListViewItems custCodeView = (ListViewItems) v.findViewById(R.id.row_dlCustomerCode);
            	  ListViewItems custNameView = (ListViewItems) v.findViewById(R.id.row_dlCustomerName);            	  
            	  ListViewItems referenceNoView = (ListViewItems) v.findViewById(R.id.row_dlReferenceNo);//20131230 - Gerry added  referenceNo
            	  ListViewItems grnDateView = (ListViewItems) v.findViewById(R.id.row_dlGrnDate);
            	  
            	  
                  if(trxNoView != null){
                	  trxNoView.setText(Integer.toString(item.getTransactionNo()));
                  }
                  if(referenceNoView != null){
                	  referenceNoView.setText(item.getReferenceNo());
                  }
                  if(custCodeView != null){
                	  custCodeView.setText(item.getCustCode());
                  }
                  if(custNameView != null){                                   
                	  custNameView.setText(item.getCustName());
                  }                  
                  java.text.DateFormat dateFormat =
  					    android.text.format.DateFormat.getDateFormat(getApplicationContext());

                  if(grnDateView != null){
                	  grnDateView.setText(dateFormat.format(item.getGinDate()).toString());
                  }  
              }              
              return v;
          }

		@Override
		public int getCount() {
			return super.getCount();
		} 
		@Override
		public GinHeader getItem(int position){
			return mGinHeaders.get(position);
		}			 
	}

	private class GINUploadTask extends AsyncTask<String, Integer, String> {
		private ProgressDialog dialog;
		int myProgress;
	    @Override
	    protected String doInBackground(String... urls) {
	      String result = "";
	      for (String url : urls) {
	        DefaultHttpClient httpclient = new DefaultHttpClient();
	        httpclient.getParams().setBooleanParameter("http.protocol.expect-continue", false);
			HttpPost request = new HttpPost(url);
			request.setHeader("User-Agent", "innosys.Android.app");
			request.setHeader("Accept", "application/json");
	        request.setHeader("Content-type", "application/json");    
	        try {         
		        StringEntity entity = whApp.getGinHeader().jsonGinHeaderString();
		        request.setEntity(entity);
		        entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
		        request.setEntity(entity);	
	        	HttpResponse response = httpclient.execute(request);  
	        	result = EntityUtils.toString(response.getEntity());  
	        }  catch (ClientProtocolException e) {
	    		Log.d("ClientProtocolException", e.getLocalizedMessage());
	        	e.printStackTrace();
	        	result = e.getLocalizedMessage();		
	        } catch (IOException e) {
	    		Log.d("IOException", e.getLocalizedMessage());
	        	e.printStackTrace();
	        	result = e.getLocalizedMessage();	
	        }catch (Exception e) {
	    		Log.d("Exception", e.getLocalizedMessage());
	        	e.printStackTrace();
	        	result = e.getLocalizedMessage();	
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
	 			   isDownloaded = false;
		           txtViewTrxNo.setText("");
		           ginHeaderList = new ArrayList<GinHeader>();
		           whApp.setGinHeader(null);
		           refreshAdapter();    		
		    	}
		    	else{
		    		throw new WhAppException(result.toString());
		    	} 
	    	} catch (JSONException e) {
	    		Log.d("JSONException", e.getLocalizedMessage());
	        	e.printStackTrace();
	 			showSuccessErrorDialog(getString(R.string.msgError), e.getLocalizedMessage());	
	    	} catch (WhAppException e) {
	    		Log.d("WhAppException", e.getLocalizedMessage());
	        	e.printStackTrace();
	 			showSuccessErrorDialog(getString(R.string.msgError), e.getLocalizedMessage());		
			}catch(Exception e){
	    		Log.d("Exception", e.getLocalizedMessage());
	        	e.printStackTrace();        	
	 			showSuccessErrorDialog(getString(R.string.msgError), e.getLocalizedMessage());		    		
	    	}
	    	dialog.dismiss();
	    }
	    
	    @Override
	    protected void onProgressUpdate(Integer... values) {
		    // TODO Auto-generated method stub
	    	dialog.setProgress(values[0]);
    	    dialog.setTitle(String.format(getString(R.string.msgInProgress)) + ".." +Integer.toString(values[0]) + "%");	 
	    }
	    @Override
	    protected void onPreExecute() {
		     // TODO Auto-generated method stub
	    	dialog = ProgressDialog.show(ActGinDownload.this, getString(R.string.msgInProgress), String.format(getString(R.string.msgUploading), "GIN"), true);
		    myProgress = 0;
		    dialog.setMax(100);
	    }
	  }
	private class GINHeaderDownloadTask extends AsyncTask<String, Integer, String> {
		private ProgressDialog dialog;
		int myProgress;
	    @Override
	    protected String doInBackground(String... urls) {
	      String result = "";
	      for (String url : urls) {
	        DefaultHttpClient httpclient = new DefaultHttpClient();
	        httpclient.getParams().setBooleanParameter("http.protocol.expect-continue", false);
			HttpGet request = new HttpGet(url);
			request.setHeader("User-Agent", "innosys.Android.app");
			request.setHeader("Accept", "application/json");
	        request.setHeader("Content-type", "application/json");    
	        try {    
	        	ResponseHandler<String> responseHandler = new BasicResponseHandler();
	            result = httpclient.execute(request, responseHandler); 	  
	            JSONArray ginHeaderJSONList = new JSONArray(result); 	
	            long count = ginHeaderJSONList.length();
	            if(ginHeaderJSONList.length() >0){ 
		    		ginHeaderList = new ArrayList<GinHeader>();
		            for(int i =0; i < ginHeaderJSONList.length(); i++){     	                
		                GinHeader ginHeader = new GinHeader();                
		                ginHeader=ginHeader.convertJSONObjToGinHeader(ginHeaderJSONList.getJSONObject(i));
		                
		                Boolean ginHeaderfound =false;
		                if(ginHeaderList.size()>0){
		            	   for(GinHeader gin : ginHeaderList){
		            		   if(gin.getTransactionNo() == ginHeader.getTransactionNo()){
		            			   ginHeaderfound = true;
		            			   break;
		            		   }
		            	   }
		                }
		                if(!ginHeaderfound){
		            	   //insert to the list in memory
		            	   ginHeaderList.add(ginHeader);
		                } 
		                int progress =  (int)(i * 100  / count );
		                this.publishProgress(progress);
		            }
	            } else{
	            	throw new WhAppException(getString(R.string.errNoGinFoundInCriteria));
	            }
	            
	        }  catch (ClientProtocolException e) {
	    		Log.d("ClientProtocolException", e.getLocalizedMessage());
	        	e.printStackTrace();
	        	result = e.getLocalizedMessage();		
	        } catch (IOException e) {
	    		Log.d("IOException", e.getLocalizedMessage());
	        	e.printStackTrace();
	        	result = e.getLocalizedMessage();	
	        }catch (Exception e) {
	    		Log.d("Exception", e.getLocalizedMessage());
	        	e.printStackTrace();
	        	result = e.getLocalizedMessage();	
	        }
	      }
	      return result;
	    }

	    @Override
	    protected void onPostExecute(String result) {
	    	try{
	    		if(result.contains("Connection") || result.contains("xml")){
	    			throw new WhAppException("You have network problem, please check your internet connection");
	    		}
	    		
		    	ActGinDownload.this.refreshAdapter();        	
	    	} catch (WhAppException e) {
	    		Log.d("WhAppException", e.getLocalizedMessage());
	        	e.printStackTrace();
	 			showSuccessErrorDialog(getString(R.string.msgError), e.getLocalizedMessage());		
			}catch(Exception e){
	    		Log.d("Exception", e.getLocalizedMessage());
	        	e.printStackTrace();        	
	 			showSuccessErrorDialog(getString(R.string.msgError), e.getLocalizedMessage());		    		
	    	}
	    	dialog.dismiss();
	    }
	    
	    @Override
	    protected void onProgressUpdate(Integer... values) {	    	
	    	dialog.setProgress(values[0]);
	    	dialog.setTitle("Downloading..");
    	    dialog.setMessage(String.format(getString(R.string.msgInProgress)) + ".." +Integer.toString(values[0]) + "%");
	    }
	    @Override
	    protected void onPreExecute() {
		     // TODO Auto-generated method stub
	    	dialog = ProgressDialog.show(ActGinDownload.this, getString(R.string.msgInProgress), String.format(getString(R.string.msgDownloading), "GIN Headers"), true);
		    myProgress = 0;
		    dialog.setMax(100);
	    }
	  }
	private class GINDetailDownloadTask extends AsyncTask<String, Integer, String> {
		private ProgressDialog dialog;
		int myProgress;
	    @Override
	    protected String doInBackground(String... urls) {
	      String result = "";
	      for (String url : urls) {
	        DefaultHttpClient httpclient = new DefaultHttpClient();
	        httpclient.getParams().setBooleanParameter("http.protocol.expect-continue", false);
			HttpGet request = new HttpGet(url);
			request.setHeader("User-Agent", "innosys.Android.app");
			request.setHeader("Accept", "application/json");
	        request.setHeader("Content-type", "application/json");    
	        try {    
	        	ResponseHandler<String> responseHandler = new BasicResponseHandler();
	            result = httpclient.execute(request, responseHandler); 
	        }  catch (ClientProtocolException e) {
	    		Log.d("ClientProtocolException", e.getLocalizedMessage());
	        	e.printStackTrace();
	        	result = e.getLocalizedMessage();		
	        } catch (IOException e) {
	    		Log.d("IOException", e.getLocalizedMessage());
	        	e.printStackTrace();
	        	result = e.getLocalizedMessage();	
	        }catch (Exception e) {
	    		Log.d("Exception", e.getLocalizedMessage());
	        	e.printStackTrace();
	        	result = e.getLocalizedMessage();	
	        }
	      }
	      return result;
	    }

	    @Override
	    protected void onPostExecute(String result) {
	    	try{
	    		if(result.contains("Connection")){
	    			throw new WhAppException(result);
	    		}
	    		JSONArray ginDetailJSONArray = new JSONArray(result);  
	    		if(ginDetailJSONArray.length() > 0){
		        	for(int j =0; j <ginDetailJSONArray.length(); j++){
		        		GinDetail ginDetail = new GinDetail();
		        		ginDetail = ginDetail.convertJSONObjToGinDetail(ginDetailJSONArray.getJSONObject(j));
		        		
		        		JSONArray ginPicksJSONArray = ginDetailJSONArray.getJSONObject(j).getJSONArray("GinPicks");
		        		for(int i=0; i< ginPicksJSONArray.length(); i++){
		        			GinPick ginPick = new GinPick();
		            		ginPick = ginPick.convertJSONObjToGinPick(ginPicksJSONArray.getJSONObject(i));
		            		
		            		ginDetail.ginPicks.add(ginPick);
		        		}
		        		if(ginDetail.ginPicks.size() >0){
			        		//insert into memory
			        		whApp.getGinHeader().ginDetails.add(ginDetail);
		        		}
		        	}   
		        	if(whApp.getGinHeader().ginDetails.size() >0){
			        	whApp.insertGinHeader();
			            ginHeaderList = new ArrayList<GinHeader>();
			            ginHeaderList.add(whApp.getGinHeader());
			            //20130213
			            
	   		            whApp.getGinHeader().createGinPicksByLocation();	
	   		            //20150119 - gerry transfer this code here to be in the same threading
						isDownloaded = lockedGinHeader(whApp.getGinHeader()); 
			            txtViewTrxNo.setText(Integer.toString(whApp.getGinHeader().getTransactionNo()));
						
		   				Intent intent = new Intent(ActGinDownload.this, ActGINMain.class);
		   				startActivity(intent);
		        	}
	            } 
	    		else{	            
	            	if(whApp.getGinHeader() != null)
	            		cancelDownloadedGIN(whApp.getGinHeader().getTransactionNo()); //unLockedGIN(); 
	            }
		    	ActGinDownload.this.refreshAdapter();        	
	    	} catch (JSONException e) {
	    		Log.d("JSONException", e.getLocalizedMessage());
	        	e.printStackTrace();
	 			showSuccessErrorDialog(getString(R.string.msgError), e.getLocalizedMessage());	
	    	} catch (WhAppException e) {
	    		Log.d("WhAppException", e.getLocalizedMessage());
	        	e.printStackTrace();
	 			showSuccessErrorDialog(getString(R.string.msgError), e.getLocalizedMessage());		
			}catch(Exception e){
	    		Log.d("Exception", e.getLocalizedMessage());
	        	e.printStackTrace();        	
	 			showSuccessErrorDialog(getString(R.string.msgError), e.getLocalizedMessage());		    		
	    	}
	    	dialog.dismiss();
	    }
	    
	    @Override
	    protected void onProgressUpdate(Integer... values) {
	    	dialog.setProgress(values[0]);
    	    dialog.setTitle(String.format(getString(R.string.msgInProgress)) + ".." +Integer.toString(values[0]) + "%");
	    }
	    @Override
	    protected void onPreExecute() {
		     // TODO Auto-generated method stub
	    	dialog = ProgressDialog.show(ActGinDownload.this, getString(R.string.msgInProgress), String.format(getString(R.string.msgDownloading), "GIN Details"), true);
		    myProgress = 0;
		    dialog.setMax(100);
	    }
	  }
	private class GINCancelDownloadTask extends AsyncTask<String, Integer, String> {
		private ProgressDialog dialog;
		int myProgress;
	    @Override
	    protected String doInBackground(String... urls) {
	      String result = "";
	      for (String url : urls) {
	        DefaultHttpClient httpclient = new DefaultHttpClient();
	        httpclient.getParams().setBooleanParameter("http.protocol.expect-continue", false);
	        HttpGet request = new HttpGet(url);
			request.setHeader("User-Agent", "innosys.Android.app");
			request.setHeader("Accept", "application/json");
	        request.setHeader("Content-type", "application/json");    
	        try {
	        	HttpResponse response = httpclient.execute(request);
	        	result = EntityUtils.toString(response.getEntity());
	        }  catch (ClientProtocolException e) {
	    		Log.d("ClientProtocolException", e.getLocalizedMessage());
	        	e.printStackTrace();
	        	result = e.getLocalizedMessage();		
	        } catch (IOException e) {
	    		Log.d("IOException", e.getLocalizedMessage());
	        	e.printStackTrace();
	        	result = e.getLocalizedMessage();	
	        }catch (Exception e) {
	    		Log.d("Exception", e.getLocalizedMessage());
	        	e.printStackTrace();
	        	result = e.getLocalizedMessage();	
	        }
	      }
	      return result;
	    }

	    @Override
	    protected void onPostExecute(String result) {
	    	try{ 
	    		if(result.equalsIgnoreCase("") || result == null){
	 			   showSuccessErrorDialog(getString(R.string.msgAlert), String.format(getString(R.string.msgDownloadHasBeenCancelled), "GIN"));
	 			   whApp.getGinHeader().clearSQLiteDatabase(getApplicationContext());
	 			   isDownloaded = false;
		           txtViewTrxNo.setText("");
		           ginHeaderList = new ArrayList<GinHeader>();
		           whApp.setGinHeader(null);
		           refreshAdapter();    		
		    	}
		    	else{
		    		throw new WhAppException(result.toString());
		    	}  	                  	
	    	} catch (JSONException e) {
	    		Log.d("JSONException", e.getLocalizedMessage());
	        	e.printStackTrace();
	 			showSuccessErrorDialog(getString(R.string.msgError), e.getLocalizedMessage());	
	    	} catch (WhAppException e) {
	    		Log.d("WhAppException", e.getLocalizedMessage());
	        	e.printStackTrace();
	 			showSuccessErrorDialog(getString(R.string.msgError), e.getLocalizedMessage());		
			}catch(Exception e){
	    		Log.d("Exception", e.getLocalizedMessage());
	        	e.printStackTrace();        	
	 			showSuccessErrorDialog(getString(R.string.msgError), e.getLocalizedMessage());		    		
	    	}
	    	dialog.dismiss();
	    }
	    
	    @Override
	    protected void onProgressUpdate(Integer... values) {
	    	dialog.setProgress(values[0]);
    	    dialog.setMessage(String.format(getString(R.string.msgInProgress)) + ".." +Integer.toString(values[0]) + "%");
	   }
	    @Override
	    protected void onPreExecute() {
		     // TODO Auto-generated method stub
	    	dialog = ProgressDialog.show(ActGinDownload.this,getString(R.string.msgInProgress), String.format(getString(R.string.msgCancelling), "GIN"), true);
		    myProgress = 0;
		    dialog.setMax(100);
		    dialog.incrementProgressBy(10);
	    }
	  }


	
}
