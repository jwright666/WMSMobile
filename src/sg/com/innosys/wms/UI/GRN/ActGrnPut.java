package sg.com.innosys.wms.UI.GRN;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

import sg.com.innosys.wms.BLL.Common.WhApp;
import sg.com.innosys.wms.BLL.Common.WhEnum.ConfirmGrnType;
import sg.com.innosys.wms.BLL.GRN.GrnDetail;
import sg.com.innosys.wms.BLL.GRN.GrnPut;
import sg.com.innosys.wms.BLL.GRN.GrnSerialNo;
import sg.com.innosys.wms.BLL.GRN.PalletProduct;
import sg.com.innosys.wms.DAL.GRN.DbGrn;
import sg.com.innosys.wms.R;
import sg.com.innosys.wms.UI.Common.TabListener;

@SuppressLint("NewApi")
public class ActGrnPut extends Activity{
	public View vwContents;
	public View mActionBar;
	public int currentTab;
	public FragLocation fragLocation;
	public FragItemQty fragItemQty;
    public DbGrn dbWhGrn;
    //public GrnHeader grnHeader;
    public GrnDetail grnDetail;
    public GrnPut grnPut;
    public ActionBar bar;
     
    public WhApp whApp;
    public String palletId ="";
   
    public static final String SELECTEDTAB = "SELECTED_TAB";
    public static final String LOCATIONMODE = "LOCATION_MODE";
    public static final String PUTTINGMODE = "PUTTING_MODE";
    public static final String ISLOCATIONSAVED = "ISLOCATIONSAVED";
    public static final String PALLETID= "PALLETID";
	public static final String PALLET_SCAN = "SCAN_PALLET_ID";
    
	//location form mode values
    public final String ISSHOWALLLOCATION = "ISSHOWALLLOCATION";
	public final String PALLETIDs = "PALLET_IDs";
	public final String SELECTEDGRNDETAIL = "SELECTGRNDETAIL";
	public final String PALLETPRODUCT = "PALLETPRODUCT";
	public final String SERIALNO = "SERIALNO";
    
    public int LOCATION_MODE;  
    public int PUTTING_MODE;	    
    public Boolean isLocationSaved;    
    public Boolean isShowAllLocation;
    public Boolean isScanPalletID; // 20131231 Gerry added
	public ArrayList<String> palletIDList;
	
	public GrnDetail selectedGrnDetail;
	public PalletProduct selectedPalletProd;
	public GrnSerialNo selectedSerialNo;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbWhGrn = new DbGrn(this);
        this.setTitle("Good Received Putting");
    	//dbWhGrn.open();    	
    	
    	isShowAllLocation = getIntent().getExtras().getBoolean(ISSHOWALLLOCATION);
    	palletIDList = getIntent().getExtras().getStringArrayList(PALLETIDs);//.getStringArrayListExtra(PALLETIDs);
    	selectedGrnDetail = (GrnDetail) getIntent().getExtras().getSerializable(SELECTEDGRNDETAIL);
    	
    	isScanPalletID = getIntent().getExtras().getBoolean(PALLET_SCAN); // 20131231 Gerry added
    	
    	LOCATION_MODE = WhApp.DELETEMODE;
    	PUTTING_MODE = WhApp.DELETEMODE;
        isLocationSaved = false;
        whApp = (WhApp) getApplication();
        //grnHeader = whApp.getGrnHeader();
        
        bar = getActionBar();
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        bar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);

        bar.addTab(bar.newTab()
                .setText(R.string.tabSearch)
                //used the customized tabListener to assigned fragment to specific tab
                .setTabListener(new TabListener<FragSearchPutting>(
                        this, FragSearchPutting.TAG, FragSearchPutting.class)));
        bar.addTab(bar.newTab()
                .setText(R.string.tabPutLocation)
                //used the customized tabListener to assigned fragment to specific tab //FragLocation.TAG
                //.setTabListener(new TabListener<FragLocation>(
                //        this, FragLocation.TAG, FragLocation.class)));
                .setTabListener(new TabListener<FragLocation_ScanMultiFields>(
                       this, FragLocation_ScanMultiFields.TAG, FragLocation_ScanMultiFields.class)));
        bar.addTab(bar.newTab()
                .setText(R.string.tabPutQuantity)
                //used the customized tabListener to assigned fragment to specific tab
                .setTabListener(new TabListener<FragItemQty>(
                        this, FragItemQty.TAG, FragItemQty.class)));
        ///*
        if(whApp.getGrnHeader().getConfirmGrnType() == ConfirmGrnType.CONFIRM_GRN_AFTER_UPLOAD){
        	bar.addTab(bar.newTab()
                .setText(R.string.tabPutSerialNo)
                //used the customized tabListener to assigned fragment to specific tab
                .setTabListener(new TabListener<FragGrnSerialNo>(
                        this, FragGrnSerialNo.TAG, FragGrnSerialNo.class)));
        }
   		//*/
       mActionBar = getLayoutInflater().inflate(
               R.layout.action_bar, null);
       
       if (savedInstanceState != null) {
           bar.setSelectedNavigationItem(savedInstanceState.getInt(SELECTEDTAB));
           LOCATION_MODE = savedInstanceState.getInt(LOCATIONMODE);
           PUTTING_MODE = savedInstanceState.getInt(PUTTINGMODE);
           isLocationSaved = savedInstanceState.getBoolean(ISLOCATIONSAVED);
           palletId = savedInstanceState.getString(PALLETID);       
           isScanPalletID = savedInstanceState.getBoolean(PALLET_SCAN);       	
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
    public void onResume(){
    	super.onResume();
    	try{
    		dbWhGrn = new DbGrn(this);
	    	isShowAllLocation = getIntent().getExtras().getBoolean(ISSHOWALLLOCATION);
	    	palletIDList = getIntent().getStringArrayListExtra(PALLETIDs);
	    	selectedGrnDetail = (GrnDetail) getIntent().getExtras().getSerializable(SELECTEDGRNDETAIL);
	       	isScanPalletID = getIntent().getExtras().getBoolean(PALLET_SCAN); // 20131231 Gerry added
    	}
    	catch(Exception e){e.printStackTrace();}
	}    
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SELECTEDTAB, getActionBar().getSelectedNavigationIndex());
        outState.putInt(LOCATIONMODE, LOCATION_MODE);
        outState.putInt(PUTTINGMODE, PUTTING_MODE);
        outState.putBoolean(ISLOCATIONSAVED, isLocationSaved);
        outState.putString(PALLETID, palletId);
        outState.putBoolean(ISSHOWALLLOCATION, isShowAllLocation);
        outState.putStringArrayList(PALLETIDs, palletIDList);
        outState.putBoolean(PALLET_SCAN, isScanPalletID);
    }    

}
