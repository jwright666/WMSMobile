package sg.com.innosys.wms.BLL.GRN;

import org.json.JSONException;
import org.json.JSONObject;

import sg.com.innosys.wms.BLL.Common.SerialNo;

@SuppressWarnings("serial")
public class GrnSerialNo extends SerialNo{

	private String palletId;
	
	public GrnSerialNo(){
		super();
		
		this.palletId ="";
	}
	

    public String getPalletId() {
        return palletId;
    }
    public void setPalletId(String palletId) {
        this.palletId = palletId;
    }
	
    //Convert GrnPut to jsonObject
   	public JSONObject convertToJSONGrnSerialNo() throws JSONException{
      	JSONObject jsonGrnSerialNo = new JSONObject();
      	jsonGrnSerialNo.put("TransactionNo", this.getTransactionNo());
      	jsonGrnSerialNo.put("SerialID", this.getSerialID());
      	jsonGrnSerialNo.put("ItemCode", this.getItemCode().toString().trim().toUpperCase());
      	jsonGrnSerialNo.put("SerialNo", this.getSerialNo().toString().trim().toUpperCase());
      	jsonGrnSerialNo.put("Description", this.getDescription().toString());
      	
      	return jsonGrnSerialNo;  
  	}
  	
	
}
