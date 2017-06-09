package sg.com.innosys.wms.UI.GIN;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import sg.com.innosys.wms.BLL.Common.WhApp;
import sg.com.innosys.wms.BLL.GIN.GinDetail;
import sg.com.innosys.wms.BLL.GIN.GinPick;
import sg.com.innosys.wms.BLL.GIN.GinSerialNo;
import sg.com.innosys.wms.DAL.GIN.DbGin;
import sg.com.innosys.wms.R;
import sg.com.innosys.wms.R.layout;
import sg.com.innosys.wms.R.string;
import sg.com.innosys.wms.UI.Common.TabListener;

public class ActGinPick extends Activity{
	public static final String SERIALNO = "SERIALNO";
	public static final String SELECTEDGINDETAIL ="SELECTEDGINDETAIL";
	public final String SELECTEDTAB ="SELECTEDTAB";
	public static final String SELECTED_PICK = "SELECTEDPICK";
	public static final String PICKING_INDEX = "PICKING_INDEX";
    public ActionBar bar;
	public View mActionBar;
	public GinDetail selectedGinDetail;
	public WhApp whApp;
	public DbGin dbWhGin;
	public GinPick selectedGinPick;
	public GinSerialNo scannedSerialNo;
	public int pickingIndex;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		whApp = (WhApp) this.getApplication();
		dbWhGin = new DbGin(this);		
        //(GinDetail) getIntent().getSerializableExtra(ActGINMain.SELECTEDGINDETAIL);
        
        bar = getActionBar();
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        bar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);
        
        
        bar.addTab(bar.newTab()
                .setText("Product/Pick")
                //used the customized tabListener to assigned fragment to specific tab
                .setTabListener(new TabListener<FragGinProduct>(
                        this, FragGinProduct.TAG, FragGinProduct.class)));
        //if(selectedGinDetail.getHasSerialNo()){
        	bar.addTab(bar.newTab()
	                .setText(R.string.tabPutSerialNo)
	                //used the customized tabListener to assigned fragment to specific tab
	                .setTabListener(new TabListener<FragGinSerialNo>(
	                        this, FragGinSerialNo.TAG, FragGinSerialNo.class)));
        //}
        mActionBar = getLayoutInflater().inflate(R.layout.action_bar, null); 
		if(savedInstanceState!=null){
	        bar.setSelectedNavigationItem(savedInstanceState.getInt(SELECTEDTAB));
			selectedGinDetail = (GinDetail) savedInstanceState.getSerializable(SELECTEDGINDETAIL);
			selectedGinPick = (GinPick) savedInstanceState.getSerializable(SELECTED_PICK);
			pickingIndex = savedInstanceState.getInt(PICKING_INDEX);					
		}
		else{
	        try {
				selectedGinDetail = whApp.getGinHeader().getGinDetail(getIntent().getStringExtra(ActGINMain.SELECTEDITEM));
				selectedGinPick = (GinPick) getIntent().getExtras().getSerializable(SELECTED_PICK);
				pickingIndex = getIntent().getExtras().getInt(PICKING_INDEX);	
				//20140404 - gerry added to warn user for case item without picking
				if(selectedGinDetail.ginPicks.size() <= 0)
					showSuccessErrorDialog(getString(R.string.msgWarning), getString(R.string.msgWarnNoPickingDownloaded));

			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
    }
	@Override
    public void onResume(){
    	super.onResume();
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
    	outState.putInt(SELECTEDTAB, getActionBar().getSelectedNavigationIndex());
    	outState.putSerializable(SELECTEDGINDETAIL, selectedGinDetail);
    	outState.putSerializable(SELECTED_PICK, selectedGinPick);
    	outState.putInt(PICKING_INDEX, pickingIndex);
    }
    //20140404 - gerry added
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
}
