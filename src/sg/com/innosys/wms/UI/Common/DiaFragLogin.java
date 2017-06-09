package sg.com.innosys.wms.UI.Common;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.regex.Pattern;

import sg.com.innosys.wms.BLL.Common.LoginInfo;
import sg.com.innosys.wms.BLL.Common.WhApp;
import sg.com.innosys.wms.BLL.Common.WhAppException;
import sg.com.innosys.wms.BLL.Common.WhMobileSettings;
import sg.com.innosys.wms.R;
import sg.com.innosys.wms.R.id;
import sg.com.innosys.wms.R.layout;
import sg.com.innosys.wms.R.string;

public class DiaFragLogin extends DialogFragment {


	private Dialog dgLogin;
	private ArrayList<LoginInfo> loginInfoList; 	 
	private ArrayAdapter<CharSequence> spinAdapter;
	public View vDiaFragLogin;
	public Spinner spinCompany;
	public EditText etwDatabase;
	public EditText etwUserName;
	public EditText etwPassword;
	public TextView tvwDeviceID;
	public Button btnLogin;
	public Button btnCancel;
	public RelativeLayout pnlCompanyInfo;
	public ProgressBar progress;
	public TextView tvvValidating;
	private boolean validLogin = false;
	public EditText etwWebserviceIP;
	public TextView tvwWebserviceIP;
	
	private ActWmsMain actMain;
	
	static DiaFragLogin newInstance() {
		DiaFragLogin f = new DiaFragLogin();
	        return f;	        
	    }
	//static DiaFragLogin newInstance(int userAutentication) {
	//	DiaFragLogin f = new DiaFragLogin(userAutentication);
	//        return f;	        
	//    }
	public DiaFragLogin(){}
	//public DiaFragLogin(int userAutentication){
	//	ActWmsMain.userAutentication = userAutentication;
	//}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		
		actMain = (ActWmsMain) getActivity();
		if(savedInstanceState == null){
			DiaFragLogin.newInstance();
		}
		btnLogin.setOnClickListener(btnOnClickListener);
		btnCancel.setOnClickListener(btnOnClickListener);	

		etwWebserviceIP.addTextChangedListener(new TextWatcher() { 
			final Pattern PARTIAl_IP_ADDRESS =
			          Pattern.compile("^((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[0-9])\\.){0,3}"+
			                           "((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[0-9])){0,1}$"); 

		    public void onTextChanged(CharSequence s, int start, int before, int count) {}            
		    public void beforeTextChanged(CharSequence s,int start,int count,int after) {}            

		    private String mPreviousText = "";          
		    public void afterTextChanged(Editable s) {          
		        if(PARTIAl_IP_ADDRESS.matcher(s).matches()) {
		            mPreviousText = s.toString();
		        } else {
		            s.replace(0, s.length(), mPreviousText);
		        }
		    }
		});
		spinCompany.setOnItemSelectedListener(itemSelected);
		initializeDisplay();
	}
	@Override 
	public void onDestroy(){
		if(actMain.whApp.loginInfo == null || !validLogin){
			actMain.finish();
		}
		super.onDestroy();
		this.dismiss();
	 }
	@Override 
	public void onResume(){
		 super.onResume();
	 }	
		
	private void initializeDisplay(){
		if(ActWmsMain.userAutentication == ActWmsMain.autenticateFromServer){
        	etwUserName.setBackgroundColor(Color.parseColor("#87cefa"));
        	etwPassword.setBackgroundColor(Color.parseColor("#87cefa"));
       		etwUserName.setEnabled(true);
       		spinCompany.setEnabled(true);
       		etwUserName.requestFocus();
       	}
       	else{
       		int position = spinAdapter.getPosition(actMain.whApp.getLoginInfo().getCompanyDisplayName());
       		spinCompany.setSelection(position);
        	etwUserName.setText(actMain.whApp.getLoginInfo().getUserId());
        	etwUserName.setBackgroundColor(Color.parseColor("#66cdaa"));
        	etwPassword.setBackgroundColor(Color.parseColor("#87cefa"));
       		etwUserName.setEnabled(false);
       		spinCompany.setEnabled(false);
       		etwPassword.requestFocus();
       	}
	}
	private void fillAdapter() throws IOException{
		//hmLoginInfoList = new HashMap<String , LoginInfo>();
		//TODO read from .ini file for the company list
		ArrayList<CharSequence> companyNameList = new ArrayList<CharSequence>();
		//companyNameList.add("Company A");
		//companyNameList.add("Company B");
		//companyNameList.add("Company C");	
		loginInfoList = LoginInfo.getLoginInfos();
		
		try {
			//downloadConfig();
			if(loginInfoList.size() >0){
				for(LoginInfo loginInfo : loginInfoList){
					companyNameList.add(loginInfo.getCompanyDisplayName().toString().trim());
				}
			}	       	
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		spinAdapter = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_dropdown_item, companyNameList);//(getActivity(), R.array.Companies, android.R.layout.simple_spinner_dropdown_item);
		spinCompany.setAdapter(spinAdapter);		
	}	
	private View getContentView() {
		//getActivity().getWindow().requestFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        LayoutInflater inflater = getActivity().getLayoutInflater();  
        vDiaFragLogin =	inflater.inflate(R.layout.login, null);
       // pnlCompanyInfo = (RelativeLayout) vDiaFragLogin.findViewById(R.id.pnlCompany);
        btnLogin =(Button) vDiaFragLogin.findViewById(R.id.btnLogin);
        btnCancel =(Button) vDiaFragLogin.findViewById(R.id.btnlogin_Cancel);
        spinCompany = (Spinner)vDiaFragLogin.findViewById(R.id.spin_Company);
        etwDatabase = (EditText)vDiaFragLogin.findViewById(R.id.etwlogin_Database);
        etwUserName = (EditText)vDiaFragLogin.findViewById(R.id.etwlogin_UserName);
        etwPassword = (EditText)vDiaFragLogin.findViewById(R.id.etwlogin_UserPassword);   
        tvwDeviceID =(TextView) vDiaFragLogin.findViewById(R.id.tvvDeviceID);
        tvvValidating =(TextView) vDiaFragLogin.findViewById(R.id.tvvProgressxxxxx);
        etwWebserviceIP = (EditText)vDiaFragLogin.findViewById(R.id.etwlogin_WebserviceIP);   
        tvwWebserviceIP =(TextView) vDiaFragLogin.findViewById(R.id.tvvlogin_WebserviceIP);
        progress = (ProgressBar) vDiaFragLogin.findViewById(R.id.progressValidation);
        etwDatabase.setEnabled(false);

        tvwDeviceID.setText(tvwDeviceID.getText().toString()+WhApp.device_ID.toString().toUpperCase());
        try {
   		 	fillAdapter();
		} catch (IOException e) {
			e.printStackTrace();
			 showSuccessErrorDialog(getString(R.string.msgError), e.getLocalizedMessage());
		}	
		//spinAdapter = new ArrayAdapter<String>(actMain, R.id.spin_Company, companyNameList);
		
        return vDiaFragLogin;
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
 
	private OnItemSelectedListener itemSelected = new OnItemSelectedListener(){

		public void onItemSelected(AdapterView<?> adapter, View view, int position,
				long row) {
			String selectedCompany =adapter.getItemAtPosition(position).toString();
			LoginInfo loginInfo = LoginInfo.getLoginInfoByCompany(loginInfoList, selectedCompany);
			if(loginInfo != null){
				etwDatabase.setText(loginInfo.getDatabaseName());
				if(loginInfo.getWebServiceIP().equalsIgnoreCase("")  
					& ActWmsMain.userAutentication == ActWmsMain.autenticateFromServer){
					etwWebserviceIP.setVisibility(View.VISIBLE);
					tvwWebserviceIP.setVisibility(View.VISIBLE);
				}
				else{
					etwWebserviceIP.setVisibility(View.GONE);
					tvwWebserviceIP.setVisibility(View.GONE);					
				}
			}
			
		}

		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
		
	};
	private void fillLoginInfo() throws WhAppException{
		LoginInfo loginInfo = LoginInfo.getLoginInfoByCompany(loginInfoList, spinCompany.getSelectedItem().toString());
		loginInfo.setCompanyDisplayName(spinCompany.getSelectedItem().toString());
		loginInfo.setUserId(etwUserName.getText().toString());
		loginInfo.setPassword(etwPassword.getText().toString());
		//20130613 - gerry added to assign webservice ip keyin by user
		if(loginInfo.getWebServiceIP().equalsIgnoreCase("") & ActWmsMain.userAutentication == ActWmsMain.autenticateFromServer){
			if(etwWebserviceIP.getText().toString().equalsIgnoreCase("") ){
				throw new WhAppException("Please provide webservice IP address.");
			}
			else{
				loginInfo.setWebServiceIP(etwWebserviceIP.getText().toString());
			}
				
		}
		actMain.whApp.setLoginInfo(loginInfo);
	}
	private OnClickListener btnOnClickListener = new OnClickListener(){	    
		public void onClick(View btn) {
			 String[] outCompulsoryFieldsMsg = new String[1];;
    		 try {
		    	 if(btn == btnLogin){
		    		 //TODO login if valid  	
		    		 if(validateFields(outCompulsoryFieldsMsg)){
			    		 fillLoginInfo();
			    	     WhApp.URL_string = "http://" +actMain.whApp.getLoginInfo().getWebServiceIP() + WhApp.webServiceName;  
			    	   
			    	     //20130807 	
			    	     WhMobileSettings dbSettings = WhMobileSettings.getWhMobileSettings(actMain);
			    	     actMain.whApp.setMobileSettings(dbSettings);
						 //20130807 end 
			    	     
			    		 if(ActWmsMain.userAutentication == ActWmsMain.autenticateFromServer){
		    				 //progress.setVisibility(View.VISIBLE);
		    				 //tvvValidating.setVisibility(View.VISIBLE);
			    			 if(validateLoginInfoFromServer()){
				    			actMain.whApp.getLoginInfo().addLoginInfo(getActivity());		    				
				    			//20130807 
				    			//TODO replace with the values from server				    			
				    			 WhMobileSettings settingsFromServer = getMobileSettingsFromServer(); //new WhMobileSettings();
				    			 
			    				 //settingsFromServer.setCompany("Innosys Pte Ltd1");
			    				 //settingsFromServer.setLocSeparator("^");
			    				 
			    				 //server values
			    				 if(dbSettings.getCompany() == ""){
			    					 settingsFromServer.addWhMobileSettings(actMain); 
					    			 actMain.whApp.setMobileSettings(settingsFromServer);   				    			
			    				 }
			    				 else{
			    					 if(!dbSettings.getCompany().equalsIgnoreCase(settingsFromServer.getCompany())
			    						|| !dbSettings.getLocSeparator().equalsIgnoreCase(settingsFromServer.getLocSeparator())){
			    						 //TODO update the sqlite
				    					 settingsFromServer.updateWhMobileSettings(actMain); 
						    			 actMain.whApp.setMobileSettings(settingsFromServer); 
			    					 }
			    				 }
				    			//20130807 end
				    			validLogin = true; 
			    			 }
			    		 }
			    		 else if(ActWmsMain.userAutentication == ActWmsMain.autenticateFromSQLite){
			  	        	if(actMain.whApp.getLoginInfo().validateLoginInfoFromSQLite(actMain)){				    			
			  	        		validLogin = true; 			  	        		
			  	        	}
			    		 }	
			    		 
			    		//*
			    		 if(validLogin){
			 				actMain.refreshDisplay();   
			 				dgLogin.dismiss();
			    		 }
			    		 //*/
		    		 }
		    		 else{
		    			 actMain.whApp.setLoginInfo(null);
		    			 throw new WhAppException(outCompulsoryFieldsMsg[0].toString());
		    		 }	
		    	 }
		    	 else if(btn == btnCancel){
		    		 //TODO
		    		 if(actMain.whApp.getLoginInfo() != null){
		    			 //actMain.whApp.getLoginInfo().deleteLoginInfo(getActivity());	
		    			 actMain.whApp.setLoginInfo(null);
		    		 }
			    	 dgLogin.dismiss();
		    		 getActivity().finish();
			    	 //Intent intent = new Intent();
			    	 //intent.setClass(getActivity(), ActWmsMain.class);
		    		 //startActivity(intent);
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
	private Boolean validateFields(String[] outMsg){
		outMsg[0] = "";
		boolean retValue = true;
		if(etwUserName.getText().toString().equalsIgnoreCase("")){
			outMsg[0] += getString(R.string.errEmptyUserName);
			retValue = false;
		}
		if(etwPassword.getText().toString().equalsIgnoreCase("")){
			outMsg[0] += "\n"+getString(R.string.errEmptyUserPassword);
			retValue = false;
		}
		return retValue;		
	}
 	private Boolean validateLoginInfoFromServer() throws WhAppException, Exception
	{  
        String[] sqlLogin = LoginInfo.getSqlLogin();
        String sqlUserName = sqlLogin[0].toString();
        String sqlUserPassword = sqlLogin[1].toString();              
        
		String url = WhApp.URL_string + "ValidateLoginInfo?sqlUserId="+sqlUserName+"&sqlUserPassword="+sqlUserPassword;

		DefaultHttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setBooleanParameter("http.protocol.expect-continue", false);
		HttpPost request = new HttpPost(url);
		request.setHeader("User-Agent", "innosys.Android.app");
		request.setHeader("Accept", "application/json");
        request.setHeader("Content-type", "application/json");    
        try {         
	        StringEntity entity = actMain.whApp.getLoginInfo().jsonLoginInfoString();
	        request.setEntity(entity);
	        entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
	        request.setEntity(entity);
	        
	        String result ="";
        	HttpResponse response = httpclient.execute(request);  
        	result = EntityUtils.toString(response.getEntity()).trim();	

        	if(!result.equalsIgnoreCase("") && result != null){
        		throw new WhAppException(result);
        	} 
    		progress.setVisibility(View.GONE);
    		tvvValidating.setVisibility(View.GONE);       	
        }catch (ClientProtocolException e) {
    	    Log.e("ClientProtocolException", e.toString());
    	    throw new WhAppException(e.toString());
        } catch (IOException e) {
    	    Log.e("IO exception", e.toString());
    	    throw new WhAppException(e.toString());
        } catch (WhAppException e) {
    	    Log.e("ClientProtocolException", e.toString());
    	    throw e;
        }catch (Exception e) {
        	e.printStackTrace();
    	    throw new WhAppException(e.toString());
        }
        return true;
	}
 	@SuppressWarnings("unused")
	private WhMobileSettings getMobileSettingsFromServer() throws WhAppException, Exception
 	{
 		WhMobileSettings whMobileSettings = new WhMobileSettings();
 		String[] sqlLogin = LoginInfo.getSqlLogin();
	    String sqlUserName = sqlLogin[0].toString();
	    String sqlUserPassword = sqlLogin[1].toString();              

		String serverName = URLEncoder.encode(actMain.whApp.getLoginInfo().getDbServerName(),"UTF-8");
		String url = WhApp.URL_string + "GetMobileSettings?serverName="+ serverName +"&dbName="+actMain.whApp.getLoginInfo().getDatabaseName() +"&sqlUserId="+sqlUserName+"&sqlUserPassword="+sqlUserPassword;
	
		DefaultHttpClient httpclient = new DefaultHttpClient();
	    httpclient.getParams().setBooleanParameter("http.protocol.expect-continue", false);
	    HttpGet request = new HttpGet(url);
		request.setHeader("User-Agent", "innosys.Android.app");
		request.setHeader("Accept", "application/json");
        request.setHeader("Content-type", "application/json");
        String result ="";    
        try {
	    	HttpResponse response = httpclient.execute(request);  
	    	result = EntityUtils.toString(response.getEntity());		
	    	if(result.contains("Connection") || result.contains("xml")){
    			throw new WhAppException(result);
    		}	
	    	else{
	            JSONObject jsonWhMobileSettings = new JSONObject(result); 
	            whMobileSettings =   WhMobileSettings.convertJSONObjToWhMobileSettings(jsonWhMobileSettings) ;  	
	    	}
	    }catch (ClientProtocolException e) {
		    Log.e("ClientProtocolException", e.toString());
		    throw new WhAppException(e.toString());
	    } catch (IOException e) {
		    Log.e("IO exception", e.toString());
		    throw new WhAppException(e.toString());
	    } catch (WhAppException e) {
		    Log.e("ClientProtocolException", e.toString());
		    throw e;
	    }catch (Exception e) {
	    	e.printStackTrace();
		    throw new WhAppException(e.toString());
	    }
	    return whMobileSettings;
	}
}
