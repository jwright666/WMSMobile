package sg.com.innosys.wms.BLL.Common;

import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.os.Environment;

import org.apache.http.entity.StringEntity;
import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;
import org.ini4j.Wini;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Set;

import sg.com.innosys.wms.BLL.GIN.GinHeader;
import sg.com.innosys.wms.BLL.GRN.GrnHeader;
import sg.com.innosys.wms.DAL.Common.DbLogin;
import sg.com.innosys.wms.DAL.GIN.DbGin;
import sg.com.innosys.wms.DAL.GRN.DbGrn;
import sg.com.innosys.wms.R;
import sg.com.innosys.wms.R.string;

public class LoginInfo {
	
	private String userID;
	private String password;
	private String companyName;
	private String databaseName;
	private String androidID;
	private String dbServerName;
	private String webServiceIP;
	private String companyDisplayName;
	
	public LoginInfo(){
		this.userID = "";
		this.password ="";
		this.companyName = "";
		this.databaseName = "";
		this.androidID = "";
		this.dbServerName = "";
		this.webServiceIP = "";
		this.companyDisplayName ="";
	}
	
	public void setUserId(String userId){
		this.userID=userId;
	}
	public String getUserId(){
		return this.userID;
	}	
	public void setPassword(String password){
		this.password=password;
	}
	public String getPassword(){
		return this.password;
	}
	public void setCompanyName(String companyName){
		this.companyName=companyName;
	}
	public String getCompanyName(){
		return this.companyName;
	}	
	public void setDatabaseName(String databaseName){
		this.databaseName=databaseName;
	}
	public String getDatabaseName(){
		return this.databaseName;
	}	
	public void setAndroidID(String androidID){
		this.androidID=androidID;
	}
	public String getAndroidID(){
		return this.androidID;
	}		
	public void setDbServerName(String dbServerName){
		this.dbServerName=dbServerName;
	}
	public String getDbServerName(){
		return this.dbServerName;
	}		
	public void setWebServiceIP(String webServiceIP){
		this.webServiceIP=webServiceIP;
	}
	public String getWebServiceIP(){
		return this.webServiceIP;
	}			
	public void setCompanyDisplayName(String companyDisplayName){
		this.companyDisplayName=companyDisplayName;
	}
	public String getCompanyDisplayName(){
		return this.companyDisplayName;
	}
	
	public StringEntity jsonLoginInfoString() throws JSONException, UnsupportedEncodingException{
    	JSONStringer jsonGinHeader = new JSONStringer()
        .object()
        	.key("userLoginInfo")
        		.object()
                	.key("UserId").value(this.getUserId().toString().trim())
                	.key("Password").value(this.getPassword().toString().trim())
                	.key("CompanyName").value(this.getCompanyName().toString().trim())                	
                	.key("AndroidID").value(this.getAndroidID().toString().trim())
                    .key("DatabaseName").value(this.getDatabaseName().toString().trim())
                    .key("DbServerName").value(this.getDbServerName().toString().trim())
	            .endObject()
	    .endObject();            
        StringEntity entity = new StringEntity(jsonGinHeader.toString());
        return entity;
	}
	/* this is not in use, better use JSONStringer
	public JSONObject convertToJSONLoginInfo() throws JSONException{		
      	JSONObject jsonLoginInfo = new JSONObject();
      	jsonLoginInfo.put("UserId", this.getUserId().trim().toUpperCase());
      	jsonLoginInfo.put("Password", this.getPassword().trim().toUpperCase());
      	jsonLoginInfo.put("CompanyName", this.getCompanyName().trim().toUpperCase());
      	jsonLoginInfo.put("DatabaseName", this.getDatabaseName().trim().toUpperCase());
      	jsonLoginInfo.put("DbServerName", this.getDbServerName().trim().toUpperCase());
      	jsonLoginInfo.put("AndroidID", this.getAndroidID().trim().toUpperCase());

      	return jsonLoginInfo;  
  	}
  	*/
    public LoginInfo convertJSONObjToLoginInfo(JSONObject jsonObj) throws NumberFormatException, JSONException{
    	LoginInfo loginInfo = new LoginInfo();
    	loginInfo.setUserId(jsonObj.getString("UserId"));
    	loginInfo.setPassword(jsonObj.getString("Password"));  
    	loginInfo.setCompanyName(jsonObj.getString("CompanyName"));	            	
    	loginInfo.setDatabaseName(jsonObj.getString("DatabaseName"));
    	loginInfo.setDbServerName(jsonObj.getString("DbServerName")); 
    	loginInfo.setAndroidID(WhApp.device_ID.toString().toUpperCase());
		return loginInfo;
    }

    public static LoginInfo getLoginInfoByCompany(ArrayList<LoginInfo> loginInfoList, String companyDisplayName){
    	LoginInfo loginInfo = null;
    	if(loginInfoList.size() >0){
    		for(LoginInfo temp : loginInfoList){
    			if(temp.getCompanyDisplayName().trim().equalsIgnoreCase(companyDisplayName.trim())){
    				return temp;
    			}
    		}
    	}
    	
    	return loginInfo;
    }

    public static ArrayList<LoginInfo> getLoginInfos() throws IOException{    	
    	// This method reads a configuration file, and populates an arraylist of LoginInfo objects
    	// WmsMobile_Config.ini contains the list of companies and the corresponding databases and 
    	// web service IP. This file must reside in the /Root folder of the device
    	// To use this method, need to import third party init4j jar file 
    	//import org.ini4j.Ini;
    	//import org.ini4j.InvalidFileFormatException;
    	//import org.ini4j.Wini;

    	File dir = Environment.getExternalStorageDirectory();
    	
    	File file = new File(dir, File.separator + "WmsMobile_Config.ini");
    	
    	ArrayList<LoginInfo> list = new ArrayList<LoginInfo>();
        ///*
    	Wini ini = new Wini(file);    	
        Set<String> secHeadNames =ini.keySet();
        if(secHeadNames != null){
        	for(String secHeadName : secHeadNames){
        		if(secHeadName.contains("Company")){
            		Ini.Section secHead = ini.get(secHeadName);
	        		LoginInfo loginInfo = new LoginInfo();
	        		loginInfo.setCompanyName(secHead.get("CompanyName"));
	        		loginInfo.setDatabaseName(secHead.get("DBName"));
	        		loginInfo.setDbServerName(secHead.get("DBServer"));
	        		loginInfo.setWebServiceIP(secHead.get("WebserviceIP"));  
	        		loginInfo.setAndroidID(WhApp.device_ID.toString().toUpperCase()); 
	        		loginInfo.setCompanyDisplayName(secHead.get("DisplayCompanyName"));  
	        		
	        		list.add(loginInfo);
        		}
        	}
        }
    	//*/
		return list;
	}    

    public static String[] getSqlLogin() throws InvalidFileFormatException, IOException{
    	String[] retValue = new String[2];
    	File dir = Environment.getExternalStorageDirectory();
    	File file = new File(dir, File.separator + "WmsMobile_Config.ini");    	
    	
    	Wini ini = new Wini(file);
    	retValue[0] = ini.get("User", "UserName");
    	retValue[1] = ini.get("User", "Password");
    	
		return retValue;
    	
    }

    public Boolean addLoginInfo(Context context) throws Exception{
    	Boolean retValue = false;
    	DbLogin dbLoginInfo = new DbLogin(context);
    	try{
        	if(dbLoginInfo.db == null)
        		dbLoginInfo.openDB();
        	
    		dbLoginInfo.beginTransaction();
    		dbLoginInfo.insertLoginInfo(dbLoginInfo.db, this);	

    		dbLoginInfo.commitTransaction();
	   	}
	   	catch(WhAppException ex){
	   		throw ex;
	   	}
	   	catch(Exception ex){
	   		throw ex;
	   	}
    	finally{
    		dbLoginInfo.endTransaction();
    		dbLoginInfo.closeDB();
    	}
    	return retValue;
    }
    
    public static LoginInfo getLoginInfoFromSQLite(Context context) throws Exception{
    	LoginInfo loginInfo = null;
    	DbLogin dbLoginInfo = new DbLogin(context);    	
    	try{
        	if(dbLoginInfo.db == null)
        		dbLoginInfo.openDB();
        	
    		loginInfo = dbLoginInfo.getLoginInfo();
	   	}
	   	catch(WhAppException ex){
	   		throw ex;
	   	}
	   	catch(Exception ex){
	   		throw ex;
	   	}
		return loginInfo;
    }
    
    public Boolean validateLoginInfoFromSQLite(Context context) throws WhAppException, Exception{
    	DbLogin dbLoginInfo = new DbLogin(context);
    	try{
        	if(dbLoginInfo.db == null)
        		dbLoginInfo.openDB();
    		if(!dbLoginInfo.isValidLoginInfo(this)){
    			throw new WhAppException(context.getString(R.string.errInvalidPassword));
    		}    			    		
	   	}
	   	catch(WhAppException ex){
	   		throw ex;
	   	}
	   	catch(Exception ex){
	   		throw new WhAppException(ex.getLocalizedMessage());
	   	}
		return true;    	
    }
    
    public Boolean isLoginInfoValidToDelete(Context context, String[] outMsg) throws WhAppException, Exception{
    	boolean retValue = true;
    	outMsg[0] = "";
    	try{
	    	//get gin inside sqlite
    		ArrayList<GinHeader> ginList = new ArrayList<GinHeader>();
    		DbGin dbWhGin = new DbGin(context);
        	if(dbWhGin.db == null)
        		dbWhGin.openDB();
        	
	    	ginList = dbWhGin.getAllGinHeader();
	    	//get grn inside sqlite
	    	ArrayList<GrnHeader> grnList = new ArrayList<GrnHeader>();
    		DbGrn dbWhGrn = new DbGrn(context);
	    	grnList = dbWhGrn.getAllGrnHeader();  
	    	if(ginList.size() > 0 && grnList.size() > 0){
	    		retValue = false;
	    		outMsg[0]  = String.format(context.getString(R.string.errLogoutHavePendingTransaction), "GIN TRXNO(" + ginList.get(0).getTransactionNo()+")" + " and GRN TRXNO("+ grnList.get(0).getTransactionNo()+")");
	    	}	
	    	else if(ginList.size() > 0 && grnList.size() <= 0){
	    		retValue = false;
	    		outMsg[0]  = String.format(context.getString(R.string.errLogoutHavePendingTransaction), "GIN TRXNO(" + ginList.get(0).getTransactionNo()+")");
			}
	    	else if(ginList.size() <= 0 && grnList.size() > 0){
	    		retValue = false;
	    		outMsg[0]  = String.format(context.getString(R.string.errLogoutHavePendingTransaction), "GRN TRXNO("+ grnList.get(0).getTransactionNo()+")");
	    	}
	   	}
	   	catch(Exception ex){
	   		throw new WhAppException(ex.getLocalizedMessage());
	   	}
    	return retValue;
    }
    public Boolean deleteLoginInfo(Context context) throws WhAppException, Exception{ 
    	String[] errMsg = new String[1];
    	try{
    		if(isLoginInfoValidToDelete(context, errMsg)){
    			DbLogin dbLoginInfo = new DbLogin(context);
            	if(dbLoginInfo.db == null)
            		dbLoginInfo.openDB();
    			try{
	        		dbLoginInfo.beginTransaction();
	    			dbLoginInfo.deleteLoginInfo(dbLoginInfo.db);
	    			
	    			dbLoginInfo.commitTransaction();
    			}
    			catch(SQLiteException ex){
    				throw new WhAppException(ex.getLocalizedMessage());
    		   	}
    			catch(WhAppException ex){
    		   		throw ex;
    		   	}
    		   	catch(Exception ex){
    		   		throw new WhAppException(ex.getLocalizedMessage());
    		   	}
    	    	finally{
    	    		dbLoginInfo.endTransaction();
    	    		dbLoginInfo.closeDB();
    	    	}
    		} 
    		else{
		   		throw new WhAppException(errMsg[0].toString());    			
    		}
	   	}catch(WhAppException ex){
	   		throw ex;
	   	}
	   	catch(Exception ex){
	   		throw new WhAppException(ex.getLocalizedMessage());
	   	}
    	return true;
    }
}
