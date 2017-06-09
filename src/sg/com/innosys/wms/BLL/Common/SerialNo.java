package sg.com.innosys.wms.BLL.Common;

import java.io.Serializable;


@SuppressWarnings("serial")
public abstract class SerialNo implements Serializable{
	private int transactionNo;
	private int serialID;
	private String itemCode;
	private String serialNo;
	private String description;
	
	public SerialNo(){
    	this.transactionNo=0;
    	this.serialID = 0;
    	this.itemCode = "";
    	this.serialNo = "";
        this.description = "";
    }
	
    public int getTransactionNo() {
        return transactionNo;
    }
    public void setTransactionNo(int transactionNo) {
        this.transactionNo = transactionNo;
    }
    public int getSerialID() {
        return serialID;
    }
    public void setSerialID(int serialID) {
        this.serialID = serialID;
    }
    public String getItemCode() {
        return itemCode;
    }
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
    public String getSerialNo() {
        return serialNo;
    }
    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
