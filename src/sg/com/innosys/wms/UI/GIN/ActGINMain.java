package sg.com.innosys.wms.UI.GIN;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import sg.com.innosys.wms.BLL.Common.WhApp;
import sg.com.innosys.wms.BLL.Common.WhEnum.GINPickSortOption;
import sg.com.innosys.wms.BLL.Common.WhEnum.ScanNavigationOption;
import sg.com.innosys.wms.BLL.GIN.GinDetail;
import sg.com.innosys.wms.BLL.GIN.GinHeader;
import sg.com.innosys.wms.DAL.GIN.DbGin;
import sg.com.innosys.wms.R;

@SuppressLint("NewApi")
public class ActGINMain extends Activity{
	public static final String SELECTEDITEM = "SELECTEDITEM";
	public static final String HEADER = "HEADER";
	public static final String SELECTEDGINDETAIL = "SELECTEDGINDETAIL"; //20140404 gerry added for warning change if no picking 
	public WhApp whApp;
	public GinDetail selectedGinDetail;
	public DbGin dbWhGin;
	private RadioButton rbnCompleteScanBeforeSerialNo;
	private RadioButton rbnScanWithSerialNo;
	private RadioGroup rbg;
	private TextView txtScanOption;

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.main_gin);
       this.setTitle(R.string.tabDownLoadedGIN);   
       whApp = (WhApp) getApplication();   
       dbWhGin = new DbGin(this);

		txtScanOption = (TextView)findViewById(R.id.ScanningOption);
		rbg = (RadioGroup)findViewById(R.id.rbgScan_Option);
		rbnCompleteScanBeforeSerialNo = (RadioButton)findViewById(R.id.rbnOpt_CompleteScanBeforeSerialNo);
		rbnScanWithSerialNo = (RadioButton)findViewById(R.id.rbnOpt_ScanWithSerialNo);
		if(savedInstanceState != null){
		   selectedGinDetail = (GinDetail) savedInstanceState.getSerializable("SELECTEDGINDETAIL");
		   whApp.setGinHeader((GinHeader) savedInstanceState.getSerializable(HEADER));
		} 

		//rbg.setOnCheckedChangeListener(rbnCheckedChanged);	
		addRemoveFragment() ;
    }
    
    private void addRemoveFragment(){
		FragGinDetail fragGinDetail = (FragGinDetail) getFragmentManager().findFragmentById(R.id.FragGinDetail);
		FragGinPicksByLocation fragGinPickByLocation = (FragGinPicksByLocation) getFragmentManager().findFragmentById(R.id.FragGinPicksByLocation);
		
		if(whApp.getGINPickSortOption() == GINPickSortOption.SORT_BY_ITEMCODE){
			rbg.setVisibility(View.VISIBLE);
			txtScanOption.setVisibility(View.VISIBLE);
			rbg.setOnCheckedChangeListener(rbnCheckedChanged);	
			getFragmentManager().beginTransaction().hide(fragGinPickByLocation).commit();
			getFragmentManager().beginTransaction().show(fragGinDetail).commit();
			
		}  
		else if(whApp.getGINPickSortOption() == GINPickSortOption.SORT_BY_LOCATION){
			rbg.setVisibility(View.GONE);	
			txtScanOption.setVisibility(View.GONE);	
			whApp.getGinHeader().setScanNavigationOption(ScanNavigationOption.SERIALNO_EACH_PICKING);
			getFragmentManager().beginTransaction().hide(fragGinDetail).commit();
			getFragmentManager().beginTransaction().show(fragGinPickByLocation).commit();
		}
		getFragmentManager().executePendingTransactions();
    }
    
    @Override                           
    public void onSaveInstanceState(Bundle outState){
    	super.onSaveInstanceState(outState);
    	outState.putSerializable("SELECTEDGINDETAIL", selectedGinDetail);
    	outState.putSerializable(HEADER, whApp.getGinHeader());
    	if(selectedGinDetail != null){
    		outState.putString(SELECTEDITEM, selectedGinDetail.getItemCode());
    	}
    }
    private OnCheckedChangeListener rbnCheckedChanged = new OnCheckedChangeListener(){
		public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {				
		
		switch(checkedId){
			case R.id.rbnOpt_CompleteScanBeforeSerialNo:
				rbnCompleteScanBeforeSerialNo.setChecked(true);
				whApp.getGinHeader().setScanNavigationOption(ScanNavigationOption.COMPLETE_PICKING_ALL_ITEMS_BEFORE_SERIALNO);
				break;
			
			case R.id.rbnOpt_ScanWithSerialNo:
				rbnScanWithSerialNo.setChecked(true);
				whApp.getGinHeader().setScanNavigationOption(ScanNavigationOption.SERIALNO_EACH_PICKING);
				break;
			}				
		}								
	};
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
}
