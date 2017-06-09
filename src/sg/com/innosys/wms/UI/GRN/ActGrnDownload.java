package sg.com.innosys.wms.UI.GRN;

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
import android.text.InputType;
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
import android.widget.LinearLayout;
import android.widget.ListView;
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

import sg.com.innosys.wms.BLL.Common.LoginInfo;
import sg.com.innosys.wms.BLL.Common.WhApp;
import sg.com.innosys.wms.BLL.Common.WhAppException;
import sg.com.innosys.wms.BLL.GRN.GrnDetail;
import sg.com.innosys.wms.BLL.GRN.GrnHeader;
import sg.com.innosys.wms.DAL.GRN.DbGrn;
import sg.com.innosys.wms.R;
import sg.com.innosys.wms.UI.Common.ListViewItems;

public class ActGrnDownload extends Activity{

    boolean wifiConnection = false;
    boolean mobileConnection = false;
    
	private Boolean isDownloaded = false;
	Button btnDownload;
	//Button btnNext;
	Button btnUpload;
	Button btnCheck;
	Button btnCancelGrn;
	
	EditText txtViewTrxNo;
	EditText txtViewCustCode;
	EditText txtViewWhNo;
	LinearLayout listContainer;
	public TextView tvwUserID;
	public TextView tvwDeviceId;
	public TextView tvwDatabase;
	
	ArrayList<GrnHeader> grnHeaderList = new ArrayList<GrnHeader>();
	ListView lvList;
	GrnHeaderListAdapter adpGRNHeaders;
	WhApp whApp;
	DbGrn dbWhGrn;
	Boolean validGrn;
	private String[] sqlLogin;
	private String sqlUserID;
	private String sqlUserPassword;
	
	public final String ISSHOWALLLOCATION ="ISSHOWALLLOCATION";
	
	//public String back
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grn_download);
		this.setTitle(R.string.btnGRN);
		whApp = (WhApp) getApplication();
		dbWhGrn = new DbGrn(this);

		tvwUserID = (TextView) findViewById(R.id.tvvUserId);
		tvwDeviceId = (TextView) findViewById(R.id.tvvDeviceId);
		tvwDatabase = (TextView) findViewById(R.id.tvvDatabase);
		txtViewTrxNo = (EditText)findViewById(R.id.d_etvTrxNo);
		txtViewCustCode = (EditText)findViewById(R.id.d_etwCustCode);
		txtViewWhNo = (EditText)findViewById(R.id.d_etvWh_No);
		lvList = (ListView) findViewById(R.id.lvvGrnDownloadedList);
		btnDownload = (Button)findViewById(R.id.d_btnDownload);
		btnCancelGrn = (Button)findViewById(R.id.d_btnCancelGrn);
		//btnNext = (Button)findViewById(R.id.d_btnNext);
		btnUpload = (Button)findViewById(R.id.d_btnUpload);
		btnCheck = (Button)findViewById(R.id.d_btnValidateGrn);
		listContainer = (LinearLayout) findViewById(R.id.d_lilHeaderView);
		//TextView grnDate = (TextView) findViewById(R.id.lvhGrnDate);
		//listContainer.setOnTouchListener(new ZoomAndPan(listContainer, grnDate, Anchor.CENTER));
		
		btnDownload.setOnClickListener(btnClick);	
		//btnNext.setOnClickListener(btnClick);
		btnUpload.setOnClickListener(btnClick);
		btnCheck.setOnClickListener(btnClick);	
		btnCancelGrn.setOnClickListener(btnClick);	
		//getAllGrnHeaderFromDB();
		if(savedInstanceState != null){
			grnHeaderList = (ArrayList<GrnHeader>) savedInstanceState.get("HEADERLIST");
			isDownloaded = savedInstanceState.getBoolean("ISDOWNLOADED");
		}	
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
		refreshAdapter();		

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
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void intializeButtons(){
		if(isDownloaded){
			btnDownload.setEnabled(false);
			btnUpload.setEnabled(true);
			btnCheck.setEnabled(true);
			btnCancelGrn.setEnabled(true);	
		}
		else{
			btnDownload.setEnabled(true);
			btnUpload.setEnabled(false);
			btnCheck.setEnabled(false);		
			btnCancelGrn.setEnabled(false);			
		}
	}

    public void refreshAdapter()
	 {		
    	try {//20141201 - gerry modified
			if(grnHeaderList.size() == 0){	
				if(whApp.getAllGrnHeaders().size() > 0 ){
					grnHeaderList.add(whApp.getGrnHeader());
					isDownloaded = true; 
					txtViewTrxNo.setText(Integer.toString(grnHeaderList.get(0).getTransactionNo()));	  						
				}
				//if(whApp.getGrnHeader() == null){ 
				//	grnHeaderList = whApp.getAllGrnHeaders();		
				//}
				//else{
				//	grnHeaderList.add(whApp.getGrnHeader());
				//}				
				//isDownloaded = true; 
				//txtViewTrxNo.setText(Integer.toString(grnHeaderList.get(0).getTransactionNo()));
			}			
	    	intializeButtons();
	    	adpGRNHeaders = new GrnHeaderListAdapter(this, R.layout.row_grndownloadedlist, grnHeaderList);
	    	lvList.setOnItemClickListener(itemClicked);	
			lvList.setAdapter(adpGRNHeaders);			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }		 
    
    @Override
    public void onSaveInstanceState(Bundle outState){
    	super.onSaveInstanceState(outState);
    	outState.putSerializable("HEADERLIST", grnHeaderList);
    	outState.putBoolean("ISDOWNLOADED", isDownloaded);
    	//outState.putSerializable("GRNHEADER", whApp.getGrnHeader());
    } 
    
    @Override
    public void onResume(){		
		if(whApp.getLoginInfo() == null){
			WhApp.device_ID = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
			try {
				LoginInfo.getLoginInfoFromSQLite(this);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
    	super.onResume();
    	refreshAdapter();
    }
	@Override
    public void onPause(){
    	super.onPause();
	}
	@Override
    public void onDestroy(){
    	super.onDestroy();
	}
	public void uploadObjectCollection() throws UnsupportedEncodingException, WhAppException, Exception{
        String serverName = URLEncoder.encode(whApp.getLoginInfo().getDbServerName(),"UTF-8");
        
        String url = WhApp.URL_string + "UploadGrnHeader?userid="+whApp.getLoginInfo().getUserId()+"&deviceId="+whApp.getLoginInfo().getAndroidID()
        		+"&serverName=" + serverName + "&dbName=" + whApp.getLoginInfo().getDatabaseName()
				+ "&sqlUserId=" + sqlLogin[0].toString() + "&sqlUserPassword=" + sqlUserPassword;
        
        GRNUploadTask grnUploadTask = new GRNUploadTask();
        grnUploadTask.execute(new String[] { url }); 
	}
    private void downloadGrnHeaders() throws UnsupportedEncodingException, WhAppException, Exception{
		//later we replace to the value from criteria..
		String custCode = txtViewCustCode.getText().toString().toUpperCase();//"Cust1";
		String wh_No = txtViewWhNo.getText().toString().toUpperCase();//"warehouse1";		

		String serverName = URLEncoder.encode(whApp.getLoginInfo().getDbServerName(),"UTF-8");
        
		String url = WhApp.URL_string + "GetGrnHeaders?CustCode="+custCode+"&WhNo="+wh_No +"&serverName=" + serverName + "&dbName=" + whApp.getLoginInfo().getDatabaseName()
											+ "&sqlUserId=" + sqlUserID + "&sqlUserPassword=" + sqlUserPassword; 
		
		GRNHeaderDownloadTask grnHeaderDownloadTask = new GRNHeaderDownloadTask();
		grnHeaderDownloadTask.execute(new String[] { url });       
		
	 }	 
    private void cancelDownloadedGRN(int trxNo) throws UnsupportedEncodingException, WhAppException, Exception{	
		//encode valid url string format replaced special characters such as "\", " " etc
		//in this case \ for serverName will be replaced with %5C for valid url 
        String serverName = URLEncoder.encode(whApp.getLoginInfo().getDbServerName(),"UTF-8");       
		
		String url = WhApp.URL_string +"CancelGrn?userid=" +whApp.getLoginInfo().getUserId()+"&deviceId="+ whApp.getLoginInfo().getAndroidID() +"&trxNo="+trxNo
							+"&serverName=" + serverName + "&dbName=" + whApp.getLoginInfo().getDatabaseName()
							+ "&sqlUserId=" + sqlUserID + "&sqlUserPassword=" + sqlUserPassword;
		
		GRNCancelDownloadTask grnCancelDownloadTask = new GRNCancelDownloadTask();
		grnCancelDownloadTask.execute(new String[] { url });   
	}
	private void downloadGrnDetails() throws UnsupportedEncodingException, WhAppException, Exception{
		String serverName = URLEncoder.encode(whApp.getLoginInfo().getDbServerName(),"UTF-8");
        
		String url = WhApp.URL_string + "GetGrnHeader/GetGrnDetails?trx="+whApp.getGrnHeader().getTransactionNo() +"&serverName=" + serverName + "&dbName=" + whApp.getLoginInfo().getDatabaseName()
										+ "&sqlUserId=" + sqlUserID + "&sqlUserPassword=" + sqlUserPassword; 
		GRNDetailDownloadTask grnDetailDownloadTask = new GRNDetailDownloadTask();
		grnDetailDownloadTask.execute(new String[] { url });   
	}
	private boolean lockedGrnHeader(GrnHeader selectGrnHeader ) throws InvalidFileFormatException, IOException, WhAppException{
		boolean isGrnHeaderLockedSuccess =false;		
		HttpClient  httpclient = new DefaultHttpClient();
		String serverName = URLEncoder.encode(whApp.getLoginInfo().getDbServerName(),"UTF-8");
        
		String reqURL = "LockGrnHeader?userid=" + whApp.getLoginInfo().getUserId()+"&deviceId="+ whApp.getLoginInfo().getAndroidID() +"&trxNo="+selectGrnHeader.getTransactionNo()
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
				isGrnHeaderLockedSuccess = true;								
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
		return isGrnHeaderLockedSuccess;		
	}
	private OnItemClickListener itemClicked = new OnItemClickListener(){
		public void onItemClick(AdapterView<?> adapter, View view, int position,
				long arg3) {
			//boolean isGrnHeaderLockedSuccess =false;
			//boolean insertGrnHeaderSuccess =false;
			try {
				whApp.setGrnHeader(grnHeaderList.get(position));
				if(!isDownloaded){	
					downloadGrnDetails();
				}
				else{			
	   				//Now show main activity			
	   				Intent intent = new Intent(ActGrnDownload.this, ActGRNMain.class);
	   				intent.putExtra("ISSHOWALLDETAILS", true);
	   				intent.putExtra("TRXNO", whApp.getGrnHeader().getTransactionNo());
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
	private OnClickListener btnClick = new OnClickListener(){	
		public void onClick(View btn) {
			 try{
				 if(btn == btnDownload){
					 //downLoadGRN();	
					 downloadGrnHeaders();
					 refreshAdapter();
				 }
				 else if(btn == btnUpload){
					String trxNo = txtViewTrxNo.getText().toString().toUpperCase();						
					if(!trxNo.toString().trim().equals("")){
						String checkMsg = whApp.getGrnHeader().checkGrnHeaderQytBeforeUpload(getApplicationContext());
						if(checkMsg == ""){
							showAlertDialog(getString(R.string.msgWarnUpload));
						}
						else{
							showAlertDialog(checkMsg +"\n\n"+getString(R.string.msgWarnUpload)+ "\n"
												+ getString(R.string.msgWarnUploadUnbal));
						}
					}
					else{
						showSuccessErrorDialog(getString(R.string.msgError),getString(R.string.errNoGrnToUpload));
					}						
				 }			 		 
				 else if(btn==btnCheck){
					//whApp.setGrnHeader(null);
					Intent intent = new Intent(ActGrnDownload.this, ActUnbalanceGrnDetail.class);
				    startActivity(intent);
				 }		
				 else if(btn == btnCancelGrn){
					 //TODO Cancel GIN
					 if(whApp.getGrnHeader() != null){
						 showAlertDialogToCancel(String.format(getString(R.string.msgWarnCancelDownload), "GRN"));						 
					 }
				 }	
	
			 } catch (WhAppException e) {
				e.printStackTrace();
				 showSuccessErrorDialog(getString(R.string.msgError),e.getLocalizedMessage().toString());
			} catch (Exception e) {
				e.printStackTrace();
				 showSuccessErrorDialog(getString(R.string.msgError),e.getLocalizedMessage().toString());
			}
		 }
		};
	
	private OnClickListener txtClick = new OnClickListener(){
		public void onClick(View v) {
			EditText editText = (EditText) v;
			editText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);	 
			editText = (EditText)findViewById(R.id.d_etwCustCode);
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
	private AlertDialog showAlertDialog(String msg){
	    AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setMessage(msg)
	    	   .setTitle(getString(R.string.msgAlert))
	           .setCancelable(false)
	           .setPositiveButton(getString(R.string.msgBtnYes), new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {	
	            	   try {
	            		   uploadObjectCollection();			            	
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
	            	   //do checking
	            	   btnCheck.performClick();
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
	            		  cancelDownloadedGRN(whApp.getGrnHeader().getTransactionNo());
						} catch (WhAppException e) {
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
	public class GrnHeaderListAdapter extends ArrayAdapter<GrnHeader> {
	 ArrayList<GrnHeader> mGrnHeaders;

    	public GrnHeaderListAdapter(Context context, int textViewResourceId,
    			ArrayList<GrnHeader> grnGrnHeaders) {
             super(context, textViewResourceId, grnGrnHeaders);
             mGrnHeaders = grnGrnHeaders;
 
         }
    	
		public View getView(int position, View convertView, ViewGroup parent) {
              View v = convertView;
              if (v == null) {

                  LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                  v = vi.inflate(R.layout.row_grndownloadedlist, null);	                  
              }
              
              GrnHeader item = mGrnHeaders.get(position);
              
              if (item != null) {
            	  ListViewItems trxNoView = (ListViewItems) v.findViewById(R.id.row_dlTrxNo);
            	  ListViewItems custCodeView = (ListViewItems) v.findViewById(R.id.row_dlCustomerCode);
            	  ListViewItems custNameView = (ListViewItems) v.findViewById(R.id.row_dlCustomerName);
            	  ListViewItems warehouseNoView = (ListViewItems) v.findViewById(R.id.row_dlWhNo);
            	  ListViewItems grnDateView = (ListViewItems) v.findViewById(R.id.row_dlGrnDate);
   	  
            	  
                  if(trxNoView != null){
                	  trxNoView.setText(Integer.toString(item.getTransactionNo()));
                  }
                  if(custCodeView != null){
                	  custCodeView.setText(item.getCustCode());
                  }
                  if(custNameView != null){                                   
                	  custNameView.setText(item.getCustName());
                  }
                  if(warehouseNoView != null){
                	  warehouseNoView.setText(item.getWarehouseNo());
                  }
                  
                  java.text.DateFormat dateFormat =
  					    android.text.format.DateFormat.getDateFormat(getApplicationContext());

                  if(grnDateView != null){
                	  grnDateView.setText(dateFormat.format(item.getGrnDate()).toString());
                  }  
              }
              
              return v;
          }

		@Override
		public int getCount() {
			return super.getCount();
		} 
		@Override
		public GrnHeader getItem(int position){
			return mGrnHeaders.get(position);
		}		 
    }

	private class GRNUploadTask extends AsyncTask<String, Integer, String> {
		private ProgressDialog dialog;
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
		        StringEntity entity = ActGrnDownload.this.whApp.getGrnHeader().jsonGrnHeaderString();
		        request.setEntity(entity);
		        entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
		        request.setEntity(entity);		
	        	HttpResponse response = httpclient.execute(request);  
	        	result = EntityUtils.toString(response.getEntity());	        	

 	    	    //publishProgress(100);  
	        } catch (ClientProtocolException e) {
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
	 			   showSuccessErrorDialog(getString(R.string.msgAlert), "GRN "+getString(R.string.msgUploadSuccess));
	 			   whApp.getGrnHeader().clearSQLiteDatabase(getApplicationContext());
		           isDownloaded = false;
		           txtViewTrxNo.setText("");
				   grnHeaderList = new ArrayList<GrnHeader>();
		           whApp.setGrnHeader(null);
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
	    	dialog = ProgressDialog.show(ActGrnDownload.this, getString(R.string.msgInProgress), String.format(getString(R.string.msgUploading), "GRN"), true);
		    dialog.setMax(100);
	    }
	  }
	private class GRNHeaderDownloadTask extends AsyncTask<String, Integer, String> {
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
	            JSONArray grnHeaderJSONList = new JSONArray(result);  
	            long count = grnHeaderJSONList.length();
	    		ActGrnDownload.this.grnHeaderList = new ArrayList<GrnHeader>();
	            if(grnHeaderJSONList.length() >0){
		            for(int i =0; i < grnHeaderJSONList.length(); i++){     	                
		                GrnHeader grnHeader = new GrnHeader();                
		                grnHeader=grnHeader.convertJSONObjToGrnHeader(grnHeaderJSONList.getJSONObject(i));		                
		                Boolean grnHeaderfound =false;
		                if(ActGrnDownload.this.grnHeaderList .size()>0){
		            	   for(GrnHeader grn : ActGrnDownload.this.grnHeaderList ){
		            		   if(grn.getTransactionNo() == grnHeader.getTransactionNo()){
		            			   grnHeaderfound = true;
		            			   break;
		            		   }
		            	   }
		                }
		                if(!grnHeaderfound){
		            	   //insert to the list in memory
		                	ActGrnDownload.this.grnHeaderList.add(grnHeader);
		                }
		               myProgress =  (int)(i * 100  / count );
		               this.publishProgress(myProgress);
		            }
	            } else{
	            	throw new WhAppException(getString(R.string.errNoGinFoundInCriteria));
	            }	    
 
	        } catch (ClientProtocolException e) {
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
	            
		    	ActGrnDownload.this.refreshAdapter();        	
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
	    	dialog = ProgressDialog.show(ActGrnDownload.this, getString(R.string.msgInProgress), String.format(getString(R.string.msgDownloading), "GRN Headers"), true);
		    myProgress = 0;
		    dialog.setMax(100);
	    }
	  }
	private class GRNCancelDownloadTask extends AsyncTask<String, Integer, String> {
		private ProgressDialog dialog;
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
	        } catch (ClientProtocolException e) {
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
	 			   showSuccessErrorDialog(getString(R.string.msgAlert), String.format(getString(R.string.msgDownloadHasBeenCancelled), "GRN"));
	 			   whApp.getGrnHeader().clearSQLiteDatabase(getApplicationContext());
		           isDownloaded = false;
		           txtViewTrxNo.setText("");
				   grnHeaderList = new ArrayList<GrnHeader>();
		           whApp.setGrnHeader(null);
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
    	    dialog.setTitle(String.format(getString(R.string.msgInProgress)) + ".." +Integer.toString(values[0]) + "%");
    	    dialog.getProgress();
	    }
	    @Override
	    protected void onPreExecute() {
		     // TODO Auto-generated method stub
	    	dialog = ProgressDialog.show(ActGrnDownload.this,getString(R.string.msgInProgress), String.format(getString(R.string.msgCancelling), "GRN"), true);
		    dialog.setMax(100);
	    }
	  }
	private class GRNDetailDownloadTask extends AsyncTask<String, Integer, String> {
		private ProgressDialog dialog;
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
	            JSONArray grnDetailList = new JSONArray(result); 
	            if(grnDetailList.length() >0){
		        	for(int j =0; j <grnDetailList.length(); j++){
		        		GrnDetail grnDetail = new GrnDetail();
		        		grnDetail = grnDetail.convertJSONObjToGrnDetail(grnDetailList.getJSONObject(j));
		            	//insert into memory
		        		whApp.getGrnHeader().grnDetails.add(grnDetail);   
		        	}
		        	if(whApp.getGrnHeader().grnDetails.size() >0 ){
			            //insert grnHeader to SQLite		        
			            whApp.insertGrnHeader();	
			            grnHeaderList = new ArrayList<GrnHeader>();
			            grnHeaderList.add(whApp.getGrnHeader());		   
			            
						isDownloaded = lockedGrnHeader(whApp.getGrnHeader()); 
			            txtViewTrxNo.setText(Integer.toString(whApp.getGrnHeader().getTransactionNo()));		
		   				Intent intent = new Intent(ActGrnDownload.this, ActGRNMain.class);
		   				intent.putExtra("ISSHOWALLDETAILS", true);
		   				intent.putExtra("TRXNO", whApp.getGrnHeader().getTransactionNo());
		   			    startActivity(intent);		
		        	}
	            } 
	            else{
	            	if(whApp.getGrnHeader() != null)
	            		cancelDownloadedGRN(whApp.getGrnHeader().getTransactionNo()); //unLockedGRN(); 
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
    	    dialog.setTitle(String.format(getString(R.string.msgInProgress)) + ".." +Integer.toString(values[0]) + "%");
	    }
	    @Override
	    protected void onPreExecute() {
		     // TODO Auto-generated method stub
	    	dialog = ProgressDialog.show(ActGrnDownload.this, getString(R.string.msgInProgress), String.format(getString(R.string.msgDownloading), "GRN Details"), true);
		    dialog.setMax(100);
	    }
	  }

	
	
	

}

	
