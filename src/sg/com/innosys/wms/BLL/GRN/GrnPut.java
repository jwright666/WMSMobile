package sg.com.innosys.wms.BLL.GRN;

import android.annotation.SuppressLint;
import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import sg.com.innosys.wms.R;
import sg.com.innosys.wms.R.string;

@SuppressWarnings("serial")
public class GrnPut implements Serializable{
	private int transactionNo;
	private int seqNo;
	private String productCode;
	private String palletId;
	private String zone;
	private Boolean hasRack;
	private String row;
	private String column;
	private String tier;
	private int actQty;
	private String itemCode;
	
	public GrnPut(int transactionNo,int seqNo, String productCode, String palletId,
			String zone,Boolean hasRack,String row, String column, String tier, 
			int actQty) {
		this.transactionNo = transactionNo;
		this.seqNo = seqNo;
		this.productCode = productCode;
		this.palletId = palletId;
		this.zone=zone;
		this.hasRack=hasRack;
		this.row=row;
		this.column = column;
		this.tier= tier;
		this.actQty = actQty;	
		}
	public GrnPut(){
		this.transactionNo=0;
		this.seqNo = 0;
		this.productCode = "";
		this.palletId = "";
		this.zone="";
		this.hasRack=false;
		this.row="";
		this.column = "";
		this.tier= "";
		this.actQty = 0;	
		}  

	public int getTransactionNo() {
        return transactionNo;
    }
    public void setTransactionNo(int transactionNo) {
        this.transactionNo = transactionNo;
    }
    public int getSeqNo() {
        return seqNo;
    }
    public void setSeqNo(int seqNo) {
        this.seqNo = seqNo;
    }
    public String getProductCode() {
        return productCode;
    }
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
    public String getPalletId(){
    	return palletId;
    }
    public void setPalleteId(String palletId){
    	this.palletId = palletId;
    }        
    public String getZone() {
        return zone;
    }
    public void setZone(String zone) {
        this.zone = zone;
    }
    public Boolean getHasRack() {
        return hasRack;
    }
    public void setHasRack(Boolean hasRack) {
        this.hasRack = hasRack;
    }
    public String getRow(){
    	return row;
    }
    public void setRow(String row){
    	this.row = row;
    }   
    public String getColumn(){
    	return column;
    }
    public void setColumn(String column){
    	this.column = column;
    }   
    public String getTier(){
    	return tier;
    }
    public void setTier(String tier){
    	this.tier = tier;
    }   
    public int getActQty(){
    	return actQty;
    }
    public void setActQty(int actQty){
		this.actQty = actQty;   
    } 
    public String getItemCode(){
    	return itemCode;
    }
    public void setItemCode(String itemCode){
    	this.itemCode = itemCode;
    } 
    
    public Boolean validateFields(Context ctx) throws Exception{
    	if(productCode.equals("")){
    		throw new Exception(ctx.getString(R.string.errEmptyProductCode));
    	}
    	if(palletId.equals("")){
    		throw new Exception(ctx.getString(R.string.errEmptyPalletId));
    	}
    	if(actQty == 0){
    		throw new Exception(ctx.getString(R.string.errInvalidQty));
    	}
    	return true;
    }

  //Convert GrnPut to jsonObject
   	@SuppressLint("UseValueOf")
	public JSONObject convertToJSONGrnPut() throws JSONException{
      	JSONObject jsonGrnPut = new JSONObject();
      	jsonGrnPut.put("TransactionNo", this.getTransactionNo());
      	jsonGrnPut.put("SeqNo", this.getSeqNo());
      	jsonGrnPut.put("ProductCode", this.getProductCode().toString().trim().toUpperCase());
      	jsonGrnPut.put("PalletID", this.getPalletId().toString().trim().toUpperCase());
      	jsonGrnPut.put("Zone", this.getZone().toString().trim().toUpperCase());
      	jsonGrnPut.put("HasRack", this.getHasRack());
      	jsonGrnPut.put("Row", this.getRow().toString().trim().toUpperCase());     	
      	jsonGrnPut.put("Column", this.getColumn().toString().trim().toUpperCase());       	
      	jsonGrnPut.put("Tier", this.getTier().toString().trim().toUpperCase());      	
      	jsonGrnPut.put("ActQty", this.getActQty());       	
      	jsonGrnPut.put("ItemCode", this.getItemCode().toString().trim().toUpperCase());   
      	
      	return jsonGrnPut;  
  	}
  	
}
