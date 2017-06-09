package sg.com.innosys.wms.BLL.GIN;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

@SuppressWarnings("serial")
public class GinPick implements Serializable{
	private int transactionNo;
	private int seqNo;
	private String itemCode;
	private String productCode;
	private String palletId;
	private String zone;
	private Boolean hasRack;
	private String row;
	private String column;
	private String tier;
	private int actQty;
	private int srvQty;
	private Boolean isNew;

	public GinPick(){
		this.transactionNo=0;
		this.seqNo = 0;
		this.itemCode ="";
		this.productCode = "";
		this.palletId = "";
		this.zone="";
		this.hasRack=true;
		this.row="";
		this.column = "";
		this.tier= "";
		this.actQty = 0;	
		this.srvQty =0;
		this.isNew = true;
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
    public int getSrvQty(){
    	return srvQty;
    }
    public void setSrvQty(int srvQty){
		this.srvQty = srvQty;   
    }
    public Boolean getIsNew() {
        return isNew;
    }
    public void setIsNew(Boolean isNew) {
        this.isNew = isNew;
    }
    
	public JSONObject convertToJSONGinPick() throws JSONException{		
      	JSONObject jsonGinPick = new JSONObject();
      	jsonGinPick.put("TransactionNo", this.getTransactionNo());
      	jsonGinPick.put("SeqNo", this.getSeqNo());
      	jsonGinPick.put("ItemCode", this.getItemCode().trim().toUpperCase());
      	jsonGinPick.put("ProductCode", this.getProductCode().trim().toUpperCase());
      	jsonGinPick.put("PalletID", this.getPalletId().trim().toUpperCase());
      	jsonGinPick.put("Zone", this.getZone().trim().toUpperCase());
      	jsonGinPick.put("HasRack", this.getHasRack());
      	jsonGinPick.put("Row", this.getRow().trim().toUpperCase());
      	jsonGinPick.put("Column", this.getColumn().trim().toUpperCase());
      	jsonGinPick.put("Tier", this.getTier().trim().toUpperCase());
      	jsonGinPick.put("ActQty", this.getActQty()); 
      	jsonGinPick.put("SrvQty", this.getSrvQty());

      	return jsonGinPick;  
  	}
    public GinPick convertJSONObjToGinPick(JSONObject jsonObj) throws NumberFormatException, JSONException{
    	GinPick ginPick = new GinPick();
		ginPick.setTransactionNo(Integer.parseInt(jsonObj.getString("TransactionNo")));	            	
		ginPick.setSeqNo(Integer.parseInt(jsonObj.getString("SeqNo")));
		ginPick.setItemCode(jsonObj.getString("ItemCode"));
		ginPick.setProductCode(jsonObj.getString("ProductCode"));  
		ginPick.setPalleteId(jsonObj.getString("PalletID"));	            	
		ginPick.setZone(jsonObj.getString("Zone"));
		ginPick.setHasRack(jsonObj.getBoolean("HasRack"));
		ginPick.setRow(jsonObj.getString("Row"));  
		ginPick.setColumn(jsonObj.getString("Column"));
		ginPick.setTier(jsonObj.getString("Tier")); 
		ginPick.setActQty(Integer.parseInt(jsonObj.getString("ActQty")));
		ginPick.setSrvQty(Integer.parseInt(jsonObj.getString("SrvQty")));
		ginPick.setIsNew(false);
		return ginPick;
    }
}
