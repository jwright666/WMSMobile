package sg.com.innosys.wms.BLL.Common;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;

import sg.com.innosys.wms.DAL.Common.DbWhMobileSettings;

public class WhMobileSettings {
	
	//public static final String LOCSEPARATOR ="LocSeparator";
	//public static final String COMPANY = "Company";
	
	private String company;
	private String locSeparator;
	
	public WhMobileSettings(){
		this.company = "";
		this.locSeparator = "";
	}
	
	public String getCompany(){
		return company;		
	}
	public void setCompany(String value){
		this.company = value;
	}
	public String getLocSeparator(){
		return this.locSeparator;
	}
	public void setLocSeparator(String value){
		this.locSeparator = value;
	}

    @SuppressWarnings("unchecked")
	public static WhMobileSettings convertJSONObjToWhMobileSettings(JSONObject jsonObj) throws JSONException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
    	WhMobileSettings mobileSettings = new WhMobileSettings();  
		Object value = "";
		//get all keys in json object
	    Iterator<String> iter = jsonObj.keys(); 		
	    //get all methods in WhMobileSettings class
		Method[] methods = mobileSettings.getClass().getMethods();
        while (iter.hasNext()) {
	        String key = iter.next();
			for(Method method : methods){
				String methodName = method.getName().toString();
				if(methodName.toUpperCase().contains(("set"+key).toUpperCase())){					
					value = jsonObj.getString(key);	
					method.invoke(mobileSettings, value.toString());
					break;
				}
			}  
        }
		return mobileSettings;
    }
	
	
	public Boolean addWhMobileSettings(Context context) throws Exception{
		//TODO add mobile settings to sqlite
	 	Boolean retValue = false;
	 	DbWhMobileSettings dbWhMobileSettings = new DbWhMobileSettings(context);
    	try{
        	if(dbWhMobileSettings.db == null)
        		dbWhMobileSettings.openDB();
        	
        	dbWhMobileSettings.beginTransaction();
        	dbWhMobileSettings.addWhMobileSettings(dbWhMobileSettings.db, this);	

        	dbWhMobileSettings.commitTransaction();
	   	}
	   	catch(WhAppException ex){
	   		throw ex;
	   	}
	   	catch(Exception ex){
	   		throw ex;
	   	}
    	finally{
    		dbWhMobileSettings.endTransaction();
    		dbWhMobileSettings.closeDB();
    	}
    	return retValue;
	}
	
	public static WhMobileSettings getWhMobileSettings(Context context) throws Exception{
		WhMobileSettings whMobileSettings = new WhMobileSettings();
    	DbWhMobileSettings dbWhMobileSettings = new DbWhMobileSettings(context);    	
    	try{
        	if(dbWhMobileSettings.db == null)
        		dbWhMobileSettings.openDB();
        	
        	whMobileSettings = dbWhMobileSettings.getWhMobileSettings();
	   	}
	   	catch(WhAppException ex){
	   		throw ex;
	   	}
	   	catch(Exception ex){
	   		throw ex;
	   	}
		return whMobileSettings;
    }

	public Boolean updateWhMobileSettings(Context context) throws Exception{
		//TODO update mobile settings to sqlite
	 	Boolean retValue = false;
	 	DbWhMobileSettings dbWhMobileSettings = new DbWhMobileSettings(context);
    	try{
        	if(dbWhMobileSettings.db == null)
        		dbWhMobileSettings.openDB();
        	
        	dbWhMobileSettings.beginTransaction();
        	dbWhMobileSettings.updateWhMobileSettings(dbWhMobileSettings.db, this);	

        	dbWhMobileSettings.commitTransaction();
	   	}
	   	catch(WhAppException ex){
	   		throw ex;
	   	}
	   	catch(Exception ex){
	   		throw ex;
	   	}
    	finally{
    		dbWhMobileSettings.endTransaction();
    		dbWhMobileSettings.closeDB();
    	}
    	return retValue;
	}
	
	
}
