package sg.com.innosys.wms.UI.GRN;

import android.app.Activity;
import android.os.Bundle;

import sg.com.innosys.wms.BLL.Common.WhApp;
import sg.com.innosys.wms.R;

public class ActGRNMain extends Activity{
	
	//private int trxNo;
	public WhApp whApp;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_grn);
        this.setTitle(R.string.tabDownLoadedGRN);       
       
       whApp = (WhApp) getApplication();
       //if(whApp.getGrnHeader() == null )
       // 	whApp.createGrnHeader();
       // then this line will get the global grnHeader object, to be shared by all activities
       
       //getGrnHeader();
        
    }
    @Override
    public void onResume(){
    	super.onResume();
    	try{
	        //if(whApp.getGrnHeader() == null )
	        // 	whApp.createGrnHeader();
	        //getGrnHeader();
    	}
    	catch(Exception e){
			e.printStackTrace();}
    }  
	
}