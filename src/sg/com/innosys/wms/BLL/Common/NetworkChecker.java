package sg.com.innosys.wms.BLL.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkChecker {
	private Context context;
	private boolean haveWifiConnection;
	private boolean haveMobileConnection;
	
	public NetworkChecker(Context context){
		this.context = context;
	}
	
	public boolean getHaveWifiConnection(){
		return haveWifiConnection;
	}
	public boolean getHaveMobileConnection(){
		return haveMobileConnection;
	}
	
	
	
	
	
	//this method can be use when switch device internet connection either wifi or 3G
	private boolean checkNetworkConnection() throws WhAppException{
		
	    ConnectivityManager connectivityManager = (ConnectivityManager)this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
	    if(networkInfos.length > 0){
		    for(NetworkInfo ni : networkInfos){	    	
		    	if(ni.getTypeName().equalsIgnoreCase("WIFI")){
		    		if(ni.isConnected()){
		    			haveWifiConnection = true;
		    		}
		    	} 	
		    	if(ni.getTypeName().equalsIgnoreCase("MOBILE")){
		    		if(ni.isConnected()){
		    			haveMobileConnection = true;
		    		}
		    	}
		    }
	    }
	    else{
	    	throw new WhAppException("No internet connection!");
	    }
		return haveWifiConnection || haveMobileConnection;    
	}
	

}
