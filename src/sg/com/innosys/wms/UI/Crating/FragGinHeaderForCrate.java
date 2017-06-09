package sg.com.innosys.wms.UI.Crating;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;

import sg.com.innosys.wms.R;
import sg.com.innosys.wms.R.id;
import sg.com.innosys.wms.R.layout;

public class FragGinHeaderForCrate extends Fragment{

	private View myFragmentView;
	private ActCrateMain actCrate;
	private TextView tvwTrxNo;
	private TextView tvwCustCode;
	private TextView tvwCustName;
	private TextView tvwGINDate;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);
		actCrate = (ActCrateMain)getActivity();
		
		populateGinHeader();		
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
	 public void clearFields(){
		 tvwTrxNo.setText("");
		 tvwCustCode.setText("");
		 tvwCustName.setText("");
		 tvwGINDate.setText("");		 
	 }
	 
	 public void populateGinHeader(){
		 if(actCrate.whApp.getGinHeader() != null){
			 tvwTrxNo.setText(Integer.toString(actCrate.whApp.getGinHeader().getTransactionNo()));
			 tvwCustCode.setText(actCrate.whApp.getGinHeader().getCustCode());
			 tvwCustName.setText(actCrate.whApp.getGinHeader().getCustName());
			 DateFormat dateFormat =
					    android.text.format.DateFormat.getDateFormat(getActivity().getApplicationContext());

			 tvwGINDate.setText(dateFormat.format(actCrate.whApp.getGinHeader().getGinDate()).toString());
		 }
		 else{
			 clearFields();
		 }
	 }
	 @Override
	 public void onDestroy(){
		super.onDestroy();
	 }
	 
	 @Override
	 public void onResume() {
		 actCrate = (ActCrateMain) getActivity();
		 super.onResume();	
		 populateGinHeader();		 
	}

	@Override
	 public void onPause() {
		super.onPause();
	}
}
