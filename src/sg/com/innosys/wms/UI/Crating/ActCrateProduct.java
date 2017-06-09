package sg.com.innosys.wms.UI.Crating;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import sg.com.innosys.wms.BLL.Common.WhApp;
import sg.com.innosys.wms.BLL.Crating.GinCrate;
import sg.com.innosys.wms.BLL.Crating.GinCrateProduct;
import sg.com.innosys.wms.BLL.GIN.GinDetail;
import sg.com.innosys.wms.DAL.GIN.DbGin;
import sg.com.innosys.wms.R;
import sg.com.innosys.wms.UI.Common.TabListener;

public class ActCrateProduct extends Activity{
	public final String SELECTED_GINCRATE_PRODUCT ="SELECTEDGINCRATEPRODUCT";
	public final String UNALLOCATED_PRODUCT ="UNALLOCATEDPRODUCT";
	public final String SELECTEDTAB ="SELECTEDTAB";
	public final String CRATEPRODUCTMODE ="CRATEPRODUCTMODE";
	//this mode use to populate crate product either from unallocated product or existing crate product
	public final int CRATEPROD_MODE = 0;
	public final int UNALLOCATED_PROD_MODE = 1;	
	public int crateProductMode; //mode use for crate product entry
	//	
	public int crateMode;////mode use to populate crate header 
	public ActionBar bar;
	public View mActionBar;
	public GinCrate selectedGinCrate;
	public GinCrateProduct selectedGinCrateProduct;
	public WhApp whApp;
	public DbGin dbWhGin;
	//public GinHeader ginHeader;
	public GinDetail unAllocatedProd;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(getString(R.string.tabCrateProduct));
		whApp = (WhApp) this.getApplication();		
		dbWhGin = new DbGin(this);	
		//ginHeader = whApp.getGinHeader();
		//ginHeader = (GinHeader) getIntent().getSerializableExtra(ActCrateMain.GINHEADER);
		selectedGinCrate = (GinCrate) getIntent().getSerializableExtra(ActCrateMain.SELECTEDCRATE);
		crateMode = getIntent().getExtras().getInt(ActCrateMain.CRATEMODE);
        
        bar = getActionBar();
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        bar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);        
        
        bar.addTab(bar.newTab()
                .setText(R.string.tabCrateMaster)
                //used the customized tabListener to assigned fragment to specific tab
                .setTabListener(new TabListener<FragTabCrateMaster>(
                        this, "fragTabCrateMaster", FragTabCrateMaster.class)));
        
    	bar.addTab(bar.newTab()
                .setText(R.string.tabCrateDetail)
                //used the customized tabListener to assigned fragment to specific tab
                .setTabListener(new TabListener<FragTabCrateDetail>(
                        this, "fragTabCrateDetail", FragTabCrateDetail.class)));
        
        mActionBar = getLayoutInflater().inflate(R.layout.action_bar, null); 
		if(savedInstanceState!=null){
	        bar.setSelectedNavigationItem(savedInstanceState.getInt(SELECTEDTAB));
	        selectedGinCrate = (GinCrate) savedInstanceState.getSerializable(ActCrateMain.SELECTEDCRATE);
	        selectedGinCrateProduct = (GinCrateProduct) savedInstanceState.getSerializable(SELECTED_GINCRATE_PRODUCT);
	        unAllocatedProd = (GinDetail) savedInstanceState.getSerializable(UNALLOCATED_PRODUCT);
		    crateMode = savedInstanceState.getInt(ActCrateMain.CRATEMODE);
		    crateProductMode = savedInstanceState.getInt(CRATEPRODUCTMODE);
		}
    }
	@Override
    public void onSaveInstanceState(Bundle outState){    	
    	super.onSaveInstanceState(outState);
    	//TODO save current State
    	outState.putInt(SELECTEDTAB, getActionBar().getSelectedNavigationIndex());
    	outState.putInt(ActCrateMain.CRATEMODE, crateMode);
    	outState.putInt(CRATEPRODUCTMODE, crateProductMode);
    	outState.putSerializable(ActCrateMain.SELECTEDCRATE, selectedGinCrate);
    	outState.putSerializable(SELECTED_GINCRATE_PRODUCT, selectedGinCrateProduct);
    	outState.putSerializable(UNALLOCATED_PRODUCT, unAllocatedProd);
	}
    public void onResume(){
    	super.onResume(); 
    	//ginHeader = whApp.getGinHeader();
    }  
	@Override
    public void onPause(){
    	super.onPause();
    	//whApp.setGinHeader(ginHeader);
	}
	@Override
    public void onDestroy(){
		super.onDestroy();
    	//whApp.setGinHeader(ginHeader);
	}
	
}
