package sg.com.innosys.wms.BLL.GRN;

import android.content.Context;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class PalletProduct implements Serializable {
	private String itemCode;
	private String productCode;
	private String description;
	private String batchNo;
	private String lotNo;
	private int actQty;	
	private boolean hasSerialNo;
	public ArrayList<GrnSerialNo> grnSerialNos;

    public PalletProduct(){
        this.productCode = "";
        this.description = "";
        this.itemCode = "";
        this.batchNo="";
        this.lotNo="";
        this.actQty= 0;
        this.hasSerialNo = false;
        this.grnSerialNos = new ArrayList<GrnSerialNo>();
    } 
    public String getProductCode() {
        return productCode;
    }
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
    public String getProductDescription(){
    	return description;
    }
    public void setProductDescription(String description){
    	this.description = description;
    }        
    public String getBatchNo() {
        return batchNo;
    }
    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }
    public String getLotNo() {
        return lotNo;
    }
    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }
    public int getActQty(){
    	return actQty;
    }
    public void setActQty(int actQty){
    	this.actQty = actQty; 
    }
    public Boolean getHasSerialNo(){
    	return hasSerialNo;
    }
    public void setHasSerialNo(Boolean hasSerialNo){
    	this.hasSerialNo = hasSerialNo;
    } 	
    public String getItemCode() {
        return itemCode;
    }
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }	
    
    public Boolean updatePalletProductSerialNoInMemory(GrnSerialNo serialNo, Context context) throws Exception{
    	Boolean retValue = false;
    	try{
	    	if(this.grnSerialNos.size() > 0){
	    		for(GrnSerialNo serNo : this.grnSerialNos){
	    			if(serNo.getTransactionNo() == serialNo.getTransactionNo()
	    				& serNo.getSerialID() == serialNo.getSerialID()
	    				& serNo.getSerialNo().trim().equalsIgnoreCase(serialNo.getSerialNo().trim())){
	    				
	    				serNo.setDescription(serialNo.getDescription().trim());
	    				break;
	    			}
	    		}    		
	    	}
	   	}
	   	catch(Exception ex){
	   		throw ex;
	   	}
    	return retValue;
    }    
}
