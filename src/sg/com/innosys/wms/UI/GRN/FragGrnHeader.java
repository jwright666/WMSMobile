package sg.com.innosys.wms.UI.GRN;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import java.text.DateFormat;

import sg.com.innosys.wms.BLL.Common.WhEnum.ConfirmGrnType;
import sg.com.innosys.wms.R;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class FragGrnHeader extends Fragment {

	private View myFragmentView;
	private ActGRNMain actGrn;
	private Button btnPutting;
	private RadioButton rdbScanPalletID; // 20131231 Gerry added
	private RadioButton rdbAutoGeneratePalletID; // 20131231 Gerry added
	
	@SuppressLint("NewApi")
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);
		actGrn = (ActGRNMain)getActivity();

		populateGrnHeader();
		rdbAutoGeneratePalletID.setChecked(true);
		
		//btnPutting = (Button) getActivity().findViewById(R.id.m_btnPutting);
		btnPutting.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				//Now show Location when item selected
				Intent intent = new Intent(getActivity(), ActGrnPut.class);
				intent.putExtra("ISSHOWALLLOCATION", true);
				intent.putExtra("SCAN_PALLET_ID", rdbScanPalletID.isChecked() ? true : false); // 20131231 Gerry added
			    startActivity(intent);				
			}			
		}) ;			
	}	
	
	 @Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		  myFragmentView = inflater.inflate(R.layout.frag_grnheader, container, false);
		  btnPutting = (Button) myFragmentView.findViewById(R.id.m_btnPutting);
		  rdbScanPalletID = (RadioButton) myFragmentView.findViewById(R.id.rbnPut_ScanPalletID);// 20131231 Gerry added
		  rdbAutoGeneratePalletID = (RadioButton) myFragmentView.findViewById(R.id.rbnPut_GeneratePalletID);// 20131231 Gerry added
		  return myFragmentView;
	 }
	 public void populateGrnHeader(){
		 TextView tvwTrxNo = (TextView) getActivity().findViewById(R.id.etwGrnHeader_TrxNo);
		 TextView tvwWhNo = (TextView) getActivity().findViewById(R.id.etwGrnHeader_WhNo);
		 TextView tvwCustCode = (TextView) getActivity().findViewById(R.id.etwGrnHeader_CustCode);
		 TextView tvwCustName = (TextView) getActivity().findViewById(R.id.etwGrnHeader_CustName);
		 TextView tvwGRNDate = (TextView) getActivity().findViewById(R.id.etwGrnHeader_GrnDate);
		 TextView tvwConfirmType = (TextView) getActivity().findViewById(R.id.etwGrnHeader_ConfirmType);
		 if(actGrn.whApp.getGrnHeader() != null){
			 tvwTrxNo.setText(Integer.toString(actGrn.whApp.getGrnHeader().getTransactionNo()));
			 tvwWhNo.setText(actGrn.whApp.getGrnHeader().getWarehouseNo());
			 tvwCustCode.setText(actGrn.whApp.getGrnHeader().getCustCode());
			 tvwCustName.setText(actGrn.whApp.getGrnHeader().getCustName());
			 DateFormat dateFormat =
					    android.text.format.DateFormat.getDateFormat(actGrn.getApplicationContext());

			 tvwGRNDate.setText(dateFormat.format(actGrn.whApp.getGrnHeader().getGrnDate()).toString());
			 if(actGrn.whApp.getGrnHeader().getConfirmGrnType() == ConfirmGrnType.CONFIRM_GRN_BEFORE_UPLOAD){
				 tvwConfirmType.setVisibility(View.VISIBLE);
				 tvwConfirmType.setText(getString(R.string.msgNoSerialNoNeededForCustomer));				 
			 }			 
			 
		 }
	 }

}