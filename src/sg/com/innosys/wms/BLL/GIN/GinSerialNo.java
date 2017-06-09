package sg.com.innosys.wms.BLL.GIN;

import org.json.JSONException;
import org.json.JSONObject;

import sg.com.innosys.wms.BLL.Common.SerialNo;

@SuppressWarnings("serial")
public class GinSerialNo extends SerialNo {

	public GinSerialNo(){
		super();
	}
	
	
	public JSONObject convertToJSONGinSerialNo() throws JSONException{		
      	JSONObject jsonGinSerialNo = new JSONObject();
      	jsonGinSerialNo.put("TransactionNo", this.getTransactionNo());
      	jsonGinSerialNo.put("SerialID", this.getSerialID());
      	jsonGinSerialNo.put("ItemCode", this.getItemCode().trim());
      	jsonGinSerialNo.put("SerialNo", this.getSerialNo().trim());
      	jsonGinSerialNo.put("Description", this.getDescription().trim());

      	return jsonGinSerialNo;  
  	}
	
}
