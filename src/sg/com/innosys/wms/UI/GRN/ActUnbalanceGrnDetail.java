package sg.com.innosys.wms.UI.GRN;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import sg.com.innosys.wms.BLL.Common.WhApp;
import sg.com.innosys.wms.BLL.GRN.GrnDetail;
import sg.com.innosys.wms.BLL.GRN.GrnPut;
import sg.com.innosys.wms.R;

public class ActUnbalanceGrnDetail extends Activity {	
	public WhApp whApp;
	private Button btnPutting;
	public TextView tvwSelectedProduct;
	

	public final String ISSHOWALLLOCATION ="ISSHOWALLLOCATION";
	public final String SELECTEDGRNDETAIL = "SELECTGRNDETAIL";
	public final String PALLETIDs = "PALLET_IDs";
	public GrnDetail selectedGrnDetail;
	public ArrayList<String> palletIDList;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unbalance_grndetails);
        btnPutting = (Button) findViewById(R.id.btnUnbalance_Putting);     
        tvwSelectedProduct = (TextView) findViewById(R.id.tvw_SelectedUnbalanceProduct);    
        btnPutting.setOnClickListener(btnClick);
        whApp = (WhApp) getApplication();
        palletIDList = new ArrayList<String>();       	
        
        if(savedInstanceState != null){
        	selectedGrnDetail = (GrnDetail) savedInstanceState.getSerializable(SELECTEDGRNDETAIL);
        }	 
        
    }
    private OnClickListener btnClick = new OnClickListener(){		 
		 public void onClick(View btn) {
			 if(selectedGrnDetail !=null ){
				 palletIDList = new ArrayList<String>();
				 if(selectedGrnDetail.grnPuts.size() > 0){
					 for(GrnPut put : selectedGrnDetail.grnPuts){
						 palletIDList.add(put.getPalletId().toString().toUpperCase());
					 }
				 }			 
				//Now show main activity
				Intent intent = new Intent(ActUnbalanceGrnDetail.this, ActGrnPut.class);
				intent.putExtra(ISSHOWALLLOCATION, false);
				intent.putStringArrayListExtra(PALLETIDs, palletIDList);
				intent.putExtra(SELECTEDGRNDETAIL, selectedGrnDetail);
			    startActivity(intent);	
			 }
			 else{
				 Toast.makeText(getApplicationContext(), "All Product are balance qty. ", 5).show();
			 }
		 }
		};		
		 
	 @Override
	 public void onSaveInstanceState(Bundle outState){
		 super.onSaveInstanceState(outState);
		 outState.putSerializable(SELECTEDGRNDETAIL, selectedGrnDetail);
		 outState.putSerializable(PALLETIDs, palletIDList);
	 }		
    @Override
    public void onResume(){
    	
    	super.onResume();
    }
}