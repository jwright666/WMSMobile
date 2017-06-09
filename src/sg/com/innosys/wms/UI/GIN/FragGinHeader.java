package sg.com.innosys.wms.UI.GIN;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;

import sg.com.innosys.wms.BLL.Common.WhApp;
import sg.com.innosys.wms.BLL.GIN.GinHeader;
import sg.com.innosys.wms.R;

public class FragGinHeader extends Fragment{

	private View myFragmentView;
	private ActGINMain actGin;
	private GinHeader ginHeader;
	private WhApp whApp;
	private TextView tvwTrxNo;
	private TextView tvwCustCode;
	private TextView tvwCustName;
	private TextView tvwGINDate;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);
		actGin = (ActGINMain)getActivity();
		whApp = (WhApp) actGin.getApplication();
		ginHeader = whApp.getGinHeader();	
		
		populateGrnHeader();		
	}	
	
	 @Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,
	   Bundle savedInstanceState) {
	  
		  myFragmentView = inflater.inflate(R.layout.frag_ginheader, container, false);	

		  tvwTrxNo = (TextView) myFragmentView.findViewById(R.id.etwGinHeader_TrxNo);
		  tvwCustCode = (TextView) myFragmentView.findViewById(R.id.etwGinHeader_CustCode);
		  tvwCustName = (TextView) myFragmentView.findViewById(R.id.etwGinHeader_CustName);
		  tvwGINDate = (TextView) myFragmentView.findViewById(R.id.etwGinHeader_GinDate);
	  return myFragmentView;
	 }
	 
	 public void populateGrnHeader(){
		 if(ginHeader != null){
			 tvwTrxNo.setText(Integer.toString(ginHeader.getTransactionNo()));
			 tvwCustCode.setText(ginHeader.getCustCode());
			 tvwCustName.setText(ginHeader.getCustName());
			 DateFormat dateFormat =
					    android.text.format.DateFormat.getDateFormat(getActivity().getApplicationContext());

			 tvwGINDate.setText(dateFormat.format(ginHeader.getGinDate()).toString());
		 }
	 }
	 @Override
	 public void onDestroy(){
		super.onDestroy();
	 }
	 
	 @Override
	 public void onResume() {
		 actGin = (ActGINMain) getActivity();
		 super.onResume();		 
	}

	@Override
	 public void onPause() {
		super.onPause();
	}
}
