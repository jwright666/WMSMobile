package sg.com.innosys.wms.UI.Common;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import sg.com.innosys.wms.R;
import sg.com.innosys.wms.R.id;
import sg.com.innosys.wms.R.layout;

public class DiaUpDownloadCriteria extends DialogFragment{
	
	private final String UPLOAD ="UPLOAD";
	private final String DOWNLOAD ="DOWNLOAD";
	private View vDiaFragCriteria; 
	private Dialog dgCriteria;
	private Button btnProceed;
	private Button btnCancel;
	private Spinner spnCriteriaTrxNo;
	private EditText etwCriteriaCustCode;
	private EditText etwCriteriaWhNo;
	private String title;
	
	
	 static DiaUpDownloadCriteria newInstance(String title) {
		 DiaUpDownloadCriteria f = new DiaUpDownloadCriteria(title);
	        return f;	        
	    }
	
	 DiaUpDownloadCriteria(String title){
		 this.title = title.toUpperCase();
	 }
	 
	
	private View getContentView() {
        LayoutInflater inflater = getActivity().getLayoutInflater();  
        vDiaFragCriteria =	inflater.inflate(R.layout.frag_uploaddownload, null);
        LinearLayout llTrxNo = (LinearLayout) vDiaFragCriteria.findViewById(R.id.ll_UpDownloadTrxNo);
        btnProceed =(Button) vDiaFragCriteria.findViewById(R.id.btnProceedUploadDownload);
        btnCancel =(Button) vDiaFragCriteria.findViewById(R.id.btnCancelUploadDownload);
        spnCriteriaTrxNo = (Spinner)vDiaFragCriteria.findViewById(R.id.spn_UpDownloadTrxNo);
        etwCriteriaCustCode = (EditText)vDiaFragCriteria.findViewById(R.id.etw_UpDownloadCustCode);
        etwCriteriaWhNo = (EditText)vDiaFragCriteria.findViewById(R.id.etw_UpdDownloadWhNo);
        
        if(this.title.equalsIgnoreCase(DOWNLOAD)){
        	llTrxNo.setVisibility(View.INVISIBLE);
        }
        else if(this.title.equalsIgnoreCase(UPLOAD)){
        	llTrxNo.setVisibility(View.VISIBLE);        	
        }
        
        return vDiaFragCriteria;
	 }	
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		
		
		return dgCriteria;
		
	}

}
