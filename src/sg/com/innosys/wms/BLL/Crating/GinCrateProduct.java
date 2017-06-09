package sg.com.innosys.wms.BLL.Crating;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

@SuppressWarnings("serial")
public class GinCrateProduct implements Serializable{
	private int transactionNo;
	private String crateNo;
	private String itemCode;
	private String productCode;
	private String uom;
	private int actQty;
	
	public GinCrateProduct(){
		this.transactionNo =0;
		this.crateNo="";
		this.itemCode="";
		this.productCode ="";
		this.uom="";
		this.actQty=0;
	}
	
	public JSONObject convertToJSONGinCrateDetail() throws JSONException{		
      	JSONObject jsonGinCrateDetail = new JSONObject();
      	jsonGinCrateDetail.put("TransactionNo", this.getTransactionNo());
      	jsonGinCrateDetail.put("CrateNo", this.getCrateNo().trim());
      	jsonGinCrateDetail.put("ItemCode", this.getItemCode().trim());
      	jsonGinCrateDetail.put("ProductCode", this.getProductCode().trim());
      	jsonGinCrateDetail.put("Uom", this.getUOM().trim());
      	jsonGinCrateDetail.put("ActQty", this.getActQty());  

      	return jsonGinCrateDetail;  
  	}
	
	public int getTransactionNo() {
        return transactionNo;
    }
    public void setTransactionNo(int transactionNo) {
        this.transactionNo = transactionNo;
    }
    public String getCrateNo() {
        return crateNo;
    }
    public void setCrateNo(String crateNo) {
        this.crateNo = crateNo;
    }
    public String getItemCode() {
        return itemCode;
    }
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
    public String getProductCode() {
        return productCode;
    }
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
    public String getUOM() {
        return uom;
    }
    public void setUOM(String uom) {
        this.uom = uom;
    }
	public int getActQty() {
        return actQty;
    }
    public void setActQty(int actQty) {
        this.actQty = actQty;
    }
}
