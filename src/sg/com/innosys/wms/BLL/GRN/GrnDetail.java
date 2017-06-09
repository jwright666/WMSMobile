package sg.com.innosys.wms.BLL.GRN;

import android.annotation.SuppressLint;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

import sg.com.innosys.wms.BLL.Common.WhAppException;
import sg.com.innosys.wms.DAL.GRN.DbGrn;
import sg.com.innosys.wms.R;
import sg.com.innosys.wms.R.string;

@SuppressWarnings("serial")
public class GrnDetail implements Serializable{ 
	private int transactionNo;
	private int seqNo;
	private String productCode;
	private String description;
	private String batchNo;
	private String lotNo;
	private String uom;
	private int actQty;
	private int serverQty;
	private Boolean hasSerialNo;
	public ArrayList<GrnSerialNo> grnSerialNos;
	public ArrayList<GrnPut> grnPuts;
    public String itemCode;
    public String ref_No1;
    public String ref_No2;
    public String ref_No3;
    public String ref_No4;

	public GrnDetail(){
    	this.transactionNo=0;
    	this.seqNo = 0;
        this.productCode = "";
        this.description = "";
        this.batchNo="";
        this.lotNo="";
        this.uom="";
        this.actQty=0;
        this.serverQty = 0;
        this.hasSerialNo = false;
        this.grnSerialNos= new ArrayList<GrnSerialNo>();
        this.grnPuts = new ArrayList<GrnPut>();	
        this.itemCode = "";
        this.ref_No1 = "";
        this.ref_No2 = "";
        this.ref_No3 = "";
        this.ref_No4 = "";
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
    public int getServerQty(){
    	return serverQty;
    }
    public void setServerQty(int serverQty){
    	this.serverQty = serverQty;
    }
    public String getUOM(){
    	return uom;
    }
    public void setUOM(String uom){
    	this.uom = uom;
    }    	
    public Boolean getHasSerialNo(){
    	return hasSerialNo;
    }
    public void setHasSerialNo(Boolean hasSerialNo){
    	this.hasSerialNo = hasSerialNo;
    } 	
    public String getItemCode(){
    	return itemCode;
    }
    public void setItemCode(String itemCode){
    	this.itemCode = itemCode;
    } 
    public String getRef_No1(){
    	return ref_No1;
    }
    public void setRef_No1(String ref_No1){
    	this.ref_No1 = ref_No1;
    } 
    public String getRef_No2(){
    	return ref_No2;
    }
    public void setRef_No2(String ref_No2){
    	this.ref_No2 = ref_No2;
    } 
    public String getRef_No3(){
    	return ref_No3;
    }
    public void setRef_No3(String ref_No3){
    	this.ref_No3 = ref_No3;
    } 
    public String getRef_No4(){
    	return ref_No4;
    }
    public void setRef_No4(String ref_No4){
    	this.ref_No4 = ref_No4;
    } 
    
    
    //
    public void updateActQty(){
    	this.actQty =0;
    	if(this.grnPuts.size() >0){    		
    		for(GrnPut grnPut : this.grnPuts){
    			this.actQty += grnPut.getActQty();
    		}
    	}    		
    }    
    //Convert GrnDetail to jsonObject
  	@SuppressLint({ "UseValueOf", "UseValueOf" })
	public JSONObject convertToJSONGrnDetail() throws JSONException{
      	JSONObject jsonGrnDetail = new JSONObject();
      	jsonGrnDetail.put("TransactionNo", this.getTransactionNo());
      	jsonGrnDetail.put("SeqNo", this.getSeqNo());
      	jsonGrnDetail.put("ProductCode", this.getProductCode().toString());
      	jsonGrnDetail.put("Description", "");//set to blank because no use if we upload, there is no field in sql table , it only cause an error if contain special characters
      	jsonGrnDetail.put("BatchNo", this.getBatchNo().toString());
      	jsonGrnDetail.put("LotNo", this.getLotNo().toString());        	
      	jsonGrnDetail.put("Uom", this.getUOM().toString());        	
      	jsonGrnDetail.put("ActQty", new Double(this.getActQty())); 
      	jsonGrnDetail.put("ServerQty", new Double(this.getServerQty())); 
      	jsonGrnDetail.put("SerialNoFlag", this.getHasSerialNo()); 
      	jsonGrnDetail.put("ItemCode", this.getItemCode()); 
      	jsonGrnDetail.put("Ref_No1", this.getRef_No1()); 
      	jsonGrnDetail.put("Ref_No2", this.getRef_No2()); 
      	jsonGrnDetail.put("Ref_No3", this.getRef_No3()); 
      	jsonGrnDetail.put("Ref_No4", this.getRef_No4()); 
      	
      	JSONArray jsonArrayGrnPuts = new JSONArray();
      	if(this.grnPuts.size()>0){
      		for(GrnPut grnPut : this.grnPuts){
      			jsonArrayGrnPuts.put(grnPut.convertToJSONGrnPut());
      		}
      	}
      	jsonGrnDetail.put("GrnPuts", jsonArrayGrnPuts); 
      	
      	JSONArray jsonArrayGrnSerialNos = new JSONArray();
      	if(this.grnSerialNos.size()>0){
      		for(GrnSerialNo grnSerialNo : this.grnSerialNos){
      			jsonArrayGrnSerialNos.put(grnSerialNo.convertToJSONGrnSerialNo());
      		}
      	}
      	jsonGrnDetail.put("GrnSerialNos", jsonArrayGrnSerialNos); 
      	
      	return jsonGrnDetail;  
  	}
    public GrnDetail convertJSONObjToGrnDetail(JSONObject jsonObj) throws NumberFormatException, JSONException{
    	GrnDetail grnDetail = new GrnDetail();
		grnDetail.setTransactionNo(Integer.parseInt(jsonObj.getString("TransactionNo")));	            	
    	grnDetail.setSeqNo(Integer.parseInt(jsonObj.getString("SeqNo")));
    	grnDetail.setProductCode(jsonObj.getString("ProductCode"));
    	grnDetail.setProductDescription(jsonObj.getString("Description"));
    	grnDetail.setBatchNo(jsonObj.getString("BatchNo"));
    	grnDetail.setLotNo(jsonObj.getString("LotNo"));
    	grnDetail.setUOM(jsonObj.getString("Uom"));    	            	
    	grnDetail.setServerQty(Integer.parseInt(jsonObj.getString("ServerQty")));
    	grnDetail.setHasSerialNo(jsonObj.getBoolean("SerialNoFlag"));    
    	grnDetail.setItemCode(jsonObj.getString("ItemCode"));    	   
    	grnDetail.setRef_No1(jsonObj.getString("Ref_No1"));    	   
    	grnDetail.setRef_No2(jsonObj.getString("Ref_No2"));    	   
    	grnDetail.setRef_No3(jsonObj.getString("Ref_No3"));    	   
    	grnDetail.setRef_No4(jsonObj.getString("Ref_No4"));    	   	         	
    	
    	return grnDetail;
    }
    public ArrayList<GrnPut> getGrnPutForUnbalanceGrnDetail() throws Exception{
		ArrayList<GrnPut> retValue = new ArrayList<GrnPut>();
		try{			
			retValue = this.grnPuts;
		}
		catch(Exception e){throw e;}
		return retValue;
	}
    
	public static Comparator<GrnPut> sortByPalletId = new Comparator<GrnPut>(){
		public int compare(GrnPut put1, GrnPut put2) {
			String palletId1 = put1.getPalletId().toString().toUpperCase();
			String palletId2 = put2.getPalletId().toString().toUpperCase();
			return palletId1.compareTo(palletId2);	
		}
	};

	public Boolean isPuttingExist(Context ctx, DbGrn dbWhGrn, GrnPut grnPut) throws Exception{
		boolean exist = false;
		try{
			for(GrnPut tempGrnPut : this.grnPuts){
				if(tempGrnPut.getTransactionNo() == grnPut.getTransactionNo()
					& tempGrnPut.getPalletId().trim().equalsIgnoreCase(grnPut.getPalletId().trim())
					& tempGrnPut.getItemCode().trim().equalsIgnoreCase(grnPut.getItemCode().trim())){
					
					throw new Exception(ctx.getString(R.string.msgProductExistInPallet)); // "Product already exist in putting list, Please update the exiting one instead of adding new. ");
				}				
			}
		}catch(Exception e){ throw e; }
		return exist;
	}
	//20130417 - modified add grnheader parameter
 	public Boolean addGrnSerialNo(GrnSerialNo serialNo, Context context) throws WhAppException, Exception{
    	Boolean retValue = false;
    	DbGrn dbWhGrn = new DbGrn(context);
    	try{
    		dbWhGrn.beginTransaction();	    		
    		if(!dbWhGrn.isSerialNoExist(this.getProductCode(), serialNo.getSerialNo())){
		    	dbWhGrn.insertGrnSerialNo(dbWhGrn.db, serialNo);	
		    	this.grnSerialNos.add(serialNo);		    			
    		}
	    	else{
	    		throw new WhAppException(context.getString(R.string.errDuplicateSerialNo));	    		
	    	}	

	    	dbWhGrn.commitTransaction();
	   	}
	   	catch(WhAppException ex){
	   		throw ex;
	   	}
	   	catch(Exception ex){
	   		throw ex;
	   	}
    	finally{
    		dbWhGrn.endTransaction();
    		dbWhGrn.closeDB();
    	}
    	return retValue;
    }
	 
    public Boolean updateGrnSerialNo(GrnSerialNo serialNo, String newSerialNo, Context context) throws WhAppException, Exception{
    	Boolean retValue = false;
    	DbGrn dbWhGrn = new DbGrn(context);
    	try{
    		dbWhGrn.beginTransaction();
	    	if(this.grnSerialNos.size() > 0){
	    		for(GrnSerialNo serNo : this.grnSerialNos){
	    			if(serNo.getTransactionNo() == serialNo.getTransactionNo()
	    				& serNo.getSerialID() == serialNo.getSerialID()
	    	    		& serNo.getItemCode().trim().equalsIgnoreCase(serialNo.getItemCode().trim())){
	    				
	    				if(!serialNo.getSerialNo().trim().equalsIgnoreCase(newSerialNo)){
	    					if(!dbWhGrn.isSerialNoExist(this.getProductCode(), newSerialNo)){
	    						serialNo.setSerialNo(newSerialNo.trim());
		    				} 
	    					else{
	    			    		throw new WhAppException(context.getString(R.string.errDuplicateSerialNo));	    						
	    					}
	    				}    				
	    				serialNo.setDescription(serialNo.getDescription());
	    				dbWhGrn.updateGrnSerialNo(dbWhGrn.db, serialNo);
	    				break;
	    			}
	    		}    		
	    		dbWhGrn.commitTransaction();
	    	}
	   	}
	   	catch(WhAppException ex){
	   		throw ex;
	   	}
	   	catch(Exception ex){
	   		throw ex;
	   	}
    	finally{
    		dbWhGrn.endTransaction();
    		dbWhGrn.closeDB();
    	}
    	return retValue;
    }
   
    public void deleteGrnSerialNoByPalletProduct(String palletID, String itemCode){
    	for(int i=0; i < this.grnSerialNos.size(); i++){
    		if(this.grnSerialNos.get(i).getPalletId().trim().equalsIgnoreCase(palletID)
    			&& this.grnSerialNos.get(i).getItemCode().trim().equalsIgnoreCase(itemCode)){
    			
    			this.grnSerialNos.remove(i);
    		}
    	}
    }

    
    //private Boolean isSerialNoExist(String prodCode, String newSerialNo, Context context) throws WhAppException{
    	//20130417 - modified to cater whole GRN for duplicate serial no    	
    	//for(GrnSerialNo serNo : det.grnSerialNos){
		//	if(serNo.getSerialNo().trim().equalsIgnoreCase(newSerialNo.trim())){
				
		//		throw new WhAppException(context.getString(R.string.errDuplicateSerialNo));
		//	}
		//}
		//return false;    	
    	
    //}   
    
    public boolean validateUnallocatedProductQty(Context context, int newQty) throws Exception{
    	if(newQty > this.actQty){    		
    		throw new Exception(context.getString(R.string.errExceedQtyFromUnAllocated));
    	}    	
		return true;    	
    }   

}



