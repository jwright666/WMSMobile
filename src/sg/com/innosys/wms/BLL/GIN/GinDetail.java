package sg.com.innosys.wms.BLL.GIN;

import android.annotation.SuppressLint;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

import sg.com.innosys.wms.BLL.Common.SerialNo;
import sg.com.innosys.wms.BLL.Common.WhAppException;
import sg.com.innosys.wms.BLL.Crating.GinCrate;
import sg.com.innosys.wms.DAL.GIN.DbGin;
import sg.com.innosys.wms.R;
import sg.com.innosys.wms.R.string;

@SuppressWarnings("serial")
public class GinDetail implements Serializable {
	private int transactionNo;
	private int seqNo;
	private String itemCode;
	private String warehouseNo;
	private String productCode;
	private String description;
	private String batchNo;
	private String lotNo;
	private String uom;
	private int actQty;
	private int serverQty;
	private Boolean hasSerialNo;
    public String ref_No1;
    public String ref_No2;
    public String ref_No3;
    public String ref_No4;
	public ArrayList<GinPick> ginPicks;
	public ArrayList<GinSerialNo> ginSerialNos;

    public GinDetail(){
    	this.transactionNo=0;
    	this.seqNo = 0;
    	this.itemCode = "";
    	this.warehouseNo = "";
        this.productCode = "";
        this.description = "";
        this.batchNo="";
        this.lotNo="";
        this.uom="";
        this.actQty=0;
        this.serverQty = 0;
        this.hasSerialNo = false;
        this.ref_No1 = "";
        this.ref_No2 = "";
        this.ref_No3 = "";
        this.ref_No4 = "";
        this.ginPicks = new ArrayList<GinPick>();	
        this.ginSerialNos = new ArrayList<GinSerialNo>();
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
    public String getWarehouseNo() {
        return warehouseNo;
    }
	public void setWarehouseNo(String warehouseNo) {
	    this.warehouseNo = warehouseNo;
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
    
    
	@SuppressLint("UseValueOf")
	public JSONObject convertToJSONGinDetail() throws JSONException{
      	JSONObject jsonGinDetail = new JSONObject();
      	jsonGinDetail.put("TransactionNo", this.getTransactionNo());
      	jsonGinDetail.put("SeqNo", this.getSeqNo());
      	jsonGinDetail.put("ItemCode", this.getItemCode().trim().toUpperCase());
      	jsonGinDetail.put("WarehouseNo", this.getWarehouseNo().trim().toUpperCase());
      	jsonGinDetail.put("ProductCode", this.getProductCode().toString().trim().toUpperCase());
      	jsonGinDetail.put("Description", ""); 
      	jsonGinDetail.put("BatchNo", this.getBatchNo().toString().toUpperCase());        	
      	jsonGinDetail.put("LotNo", this.getLotNo().trim().toUpperCase());      	
      	jsonGinDetail.put("Uom", this.getUOM().trim().toUpperCase());
      	jsonGinDetail.put("ActQty", this.getActQty()); 
      	jsonGinDetail.put("ServerQty", this.getServerQty()); 
      	jsonGinDetail.put("SerialNoFlag", this.getHasSerialNo()); 
      	jsonGinDetail.put("Ref_No1", this.getRef_No1().toUpperCase()); 
      	jsonGinDetail.put("Ref_No2", this.getRef_No2().toUpperCase()); 
      	jsonGinDetail.put("Ref_No3", this.getRef_No3().toUpperCase()); 
      	jsonGinDetail.put("Ref_No4", this.getRef_No4().toUpperCase()); 
      	
      	JSONArray jsonArrayGinPicks = new JSONArray();
      	if(this.ginPicks.size()>0){
      		for(GinPick ginPick : this.ginPicks){
      			jsonArrayGinPicks.put(ginPick.convertToJSONGinPick());
      		}
      	}
      	jsonGinDetail.put("GinPicks", jsonArrayGinPicks); 
      	
      	JSONArray jsonArrayGrnSerialNos = new JSONArray();
      	if(this.ginSerialNos.size()>0){
      		for(GinSerialNo ginSerialNo : this.ginSerialNos){
      			jsonArrayGrnSerialNos.put(ginSerialNo.convertToJSONGinSerialNo());
      		}
      	}
      	jsonGinDetail.put("GinSerialNos", jsonArrayGrnSerialNos); 
      	
      	return jsonGinDetail;  
  	}
    public GinDetail convertJSONObjToGinDetail(JSONObject jsonOj) throws NumberFormatException, JSONException{
    	GinDetail ginDetail = new GinDetail();
		ginDetail.setTransactionNo(Integer.parseInt(jsonOj.getString("TransactionNo")));	            	
		ginDetail.setSeqNo(Integer.parseInt(jsonOj.getString("SeqNo")));
		ginDetail.setItemCode(jsonOj.getString("ItemCode"));
		ginDetail.setWarehouseNo(jsonOj.getString("WarehouseNo"));
		ginDetail.setProductCode(jsonOj.getString("ProductCode"));
		ginDetail.setProductDescription(jsonOj.getString("Description"));
		ginDetail.setBatchNo(jsonOj.getString("BatchNo"));
		ginDetail.setLotNo(jsonOj.getString("LotNo"));
    	ginDetail.setUOM(jsonOj.getString("Uom"));    	            	
    	ginDetail.setServerQty(Integer.parseInt(jsonOj.getString("ServerQty")));
    	ginDetail.setActQty(Integer.parseInt(jsonOj.getString("ActQty")));
		ginDetail.setHasSerialNo(jsonOj.getBoolean("SerialNoFlag"));
		
		return ginDetail;
    }
    
    //instead of setActQty i used updateActQty

    public void updateActQty(){
    	this.actQty = 0;
    	if(this.ginPicks.size() >0){
    		for(GinPick pick : this.ginPicks){
    			this.actQty += pick.getActQty();
    		}
    	}
    }
    public Boolean addGinPick(GinPick ginPick, Context context) throws Exception, WhAppException{
	   	Boolean retValue = false;
    	DbGin dbWhGin = new DbGin(context);	
	   	try{	   		
	    	dbWhGin.beginTransaction();
	   		//20140403 - gerry added - not allowed duplicate zone,row,col,tier
	   		for(GinPick pick: this.ginPicks){
	   			if(ginPick.getZone().equalsIgnoreCase(pick.getZone())
	   				&ginPick.getRow().equalsIgnoreCase(pick.getRow())
		   			& ginPick.getColumn().equalsIgnoreCase(pick.getColumn())
		   			& ginPick.getTier().equalsIgnoreCase(pick.getTier())){
	   				
	   					throw new WhAppException(context.getString(R.string.errDuplicatePickLocation));
	   			}	   			
	   		}
	   		//20140403 end
	    	dbWhGin.insertGinPick(dbWhGin.db, ginPick);
	    	this.ginPicks.add(ginPick);
	    	this.updateActQty();
	    	dbWhGin.updateGinDetail(dbWhGin.db, this);	
	    	retValue = true;
	    	
	    	dbWhGin.commitTransaction();
	   	}
	   	catch(WhAppException ex){
	   		throw ex;
	   	}
	   	catch(Exception ex){
	   		throw new WhAppException(ex.getLocalizedMessage());
	   	}
    	finally{
    		dbWhGin.endTransaction();
    		dbWhGin.closeDB();
    	}
   	return retValue;
   }
    public Boolean updateGinPick(GinPick ginPick, Context context) throws Exception{
    	Boolean retValue = false;
    	DbGin dbWhGin = new DbGin(context);
    	try{    			
	    	dbWhGin.beginTransaction();
    		if(validateGinPick(context, ginPick)){	
		    	for(GinPick pick : this.ginPicks){
		    		if(pick.getTransactionNo()== ginPick.getTransactionNo()
		    			& pick.getSeqNo()== ginPick.getSeqNo()	
		    			& pick.getItemCode().trim().equalsIgnoreCase(ginPick.getItemCode().trim())){	
		    			
		    				if(ginPick.getIsNew()){
		    					pick.setZone(ginPick.getZone());
		    					pick.setRow(ginPick.getRow());
		    					pick.setColumn(ginPick.getColumn());
		    					pick.setTier(ginPick.getTier());
		    				}
		    				//update the existing ginPick actQty from memory
		    				pick.setActQty(ginPick.getActQty());	    				
		    				//update ginPick from database
			    	    	dbWhGin.updateGinPick(dbWhGin.db, pick);
			    	    	//////////this.ginPicks.add(pick);
			    	    	//Update ginDetail from database
			    	    	this.updateActQty();
			    	    	dbWhGin.updateGinDetail(dbWhGin.db, this);		    	    	
			    	    	retValue = true;	 
			    	    	break;
		    		}
		    	}	
		    	dbWhGin.commitTransaction();
    		}
    	}
	   	catch(WhAppException ex){
	   		throw ex;
	   	}
	   	catch(Exception ex){
	   		throw new WhAppException(ex.getLocalizedMessage());
	   	}
    	finally{
    		dbWhGin.endTransaction();
    		dbWhGin.closeDB();
    	}
    	return retValue;
    }
    //Validate and show error
    //to do here
    /*
    public int getGinPickNextSeqNo(Context context) throws Exception{
    	int retValue = 0;
    	DbGin dbWhGin = new DbGin(context);	
    	try{
	    	dbWhGin.openDB();
	        retValue = dbWhGin.getGinPickNextSeqNo(this.getTransactionNo(), this.getItemCode());  
    	}catch(Exception e){ throw e; }
    	finally{
    		if(dbWhGin.db.isOpen()){
    			dbWhGin.closeDB();
    		}
    	}
    	return retValue;
    }
    */
    public Boolean updateGinSerialNo(GinSerialNo serialNo, String newSerialNo, Context context) throws Exception{
    	Boolean retValue = false;
    	DbGin dbWhGin = new DbGin(context);
    	try{
	    	dbWhGin.beginTransaction();
	    	if(this.ginSerialNos.size() > 0){
	    		for(GinSerialNo serNo : this.ginSerialNos){
	    			if(serNo.getTransactionNo() == serialNo.getTransactionNo()
	    				& serNo.getSerialID() == serialNo.getSerialID()
	    				& serNo.getItemCode().trim().equalsIgnoreCase(serialNo.getItemCode().trim())){
	    				
	    				if(!serialNo.getSerialNo().trim().equalsIgnoreCase(newSerialNo)){
	    					if(!dbWhGin.isSerialNoExist(this.getProductCode(), newSerialNo)){
	    						serialNo.setSerialNo(newSerialNo.trim());
		    				} 
	    					else{
	    			    		throw new WhAppException(context.getString(R.string.errDuplicateSerialNo));	    						
	    					}
	    				}    				
	    				serialNo.setDescription(serialNo.getDescription());	    				
	    				dbWhGin.updateGinSerialNo(dbWhGin.db, serialNo);
	    				break;
	    			}
	    		}    		
		    	dbWhGin.commitTransaction();
	    	}
	   	}
	   	catch(WhAppException ex){
	   		throw ex;
	   	}
	   	catch(Exception ex){
	   		throw ex;
	   	}
    	finally{
    		dbWhGin.endTransaction();
    		dbWhGin.closeDB();
    	}
    	return retValue;
    }
    public Boolean addGinSerialNo(GinSerialNo serialNo, Context context) throws Exception{
    	Boolean retValue = false;
    	DbGin dbWhGin = new DbGin(context);
    	try{
	    	dbWhGin.beginTransaction();    		
    		if(!dbWhGin.isSerialNoExist(this.getProductCode(), serialNo.getSerialNo())){
		    	dbWhGin.insertGinSerialNo(dbWhGin.db, serialNo);	
		    	this.ginSerialNos.add(serialNo);		    			
    		}
	    	else{
	    		throw new WhAppException(context.getString(R.string.errDuplicateSerialNo));	    		
	    	}
	    	dbWhGin.commitTransaction();
	   	}
    	catch(WhAppException ex){
    		throw ex;
    	}
	   	catch(Exception ex){
	   		throw ex;
	   	}
    	finally{
    		dbWhGin.endTransaction();
    		dbWhGin.closeDB();
    	}
    	return retValue;
    }
    //validate only for existing picking
    public Boolean validateZone(Context ctx, String productCode, String zone) throws Exception{
    	boolean valid = false;
		if(this.ginPicks.size()>0){
			for(GinPick pick : this.ginPicks){
				if(pick.getProductCode().trim().equalsIgnoreCase(productCode)
					& pick.getZone().trim().equalsIgnoreCase(zone)){					

					valid = true;
					break;
				}
			}
		}
		if(!valid){
			throw new Exception(ctx.getString(R.string.errProductNotFound));
		}
		return valid;
    }
   
    public Boolean validateGinPick(Context ctx, GinPick ginPick) throws Exception{
		boolean valid = false;
    	try{
    		if(!ginPick.getIsNew()){
	    		if(this.ginPicks.size()>0){
	    			for(GinPick pick : this.ginPicks){
	    				if(pick.getProductCode().trim().equalsIgnoreCase(ginPick.getProductCode())){
		    				//20140403 - gerry added to skip validation for none rack
		    				if(pick.getZone().trim().equalsIgnoreCase(ginPick.getZone())
		    					& !pick.getHasRack()){
		    					
		    					valid = true;
		    					break;
							}
		    				//20140403 end
		    				else if(pick.getZone().trim().equalsIgnoreCase(ginPick.getZone())
		    					& pick.getRow().trim().equalsIgnoreCase(ginPick.getRow())
		    					& pick.getColumn().trim().equalsIgnoreCase(ginPick.getColumn())
		    					& pick.getTier().trim().equalsIgnoreCase(ginPick.getTier())){
		    					
		    					valid = true;
		    					break;
		    				}
	    				}
	    			}
	    			if(!valid){
	    				boolean errFound = false;
	    				String errMsg = "";
						if(getGinPicksByProduct(ginPick.getProductCode().trim()).size() >0){
							for(GinPick pick:getGinPicksByProduct(ginPick.getProductCode().trim())){
								if(!pick.getZone().trim().equalsIgnoreCase(ginPick.getZone())
			    					& pick.getRow().trim().equalsIgnoreCase(ginPick.getRow())
			    					& pick.getColumn().trim().equalsIgnoreCase(ginPick.getColumn())
			    					& pick.getTier().trim().equalsIgnoreCase(ginPick.getTier())){
									
									errFound = true;
									errMsg += "\n*" + ctx.getString(R.string.errZoneNotFound);	
									break;
									}
								else if(pick.getZone().trim().equalsIgnoreCase(ginPick.getZone())
				    					& !pick.getRow().trim().equalsIgnoreCase(ginPick.getRow())
				    					& pick.getColumn().trim().equalsIgnoreCase(ginPick.getColumn())
				    					& pick.getTier().trim().equalsIgnoreCase(ginPick.getTier())){
	
									errFound = true;
									errMsg += "\n*" + ctx.getString(R.string.errRowNotFound);	
									break;
									}
								else if(pick.getZone().trim().equalsIgnoreCase(ginPick.getZone())
				    					& pick.getRow().trim().equalsIgnoreCase(ginPick.getRow())
				    					& !pick.getColumn().trim().equalsIgnoreCase(ginPick.getColumn())
				    					& pick.getTier().trim().equalsIgnoreCase(ginPick.getTier())){
									
									errFound = true;
									errMsg += "\n*" + ctx.getString(R.string.errColumnNotFound);	
									break;
									}
								else if(pick.getZone().trim().equalsIgnoreCase(ginPick.getZone())
				    					& pick.getRow().trim().equalsIgnoreCase(ginPick.getRow())
				    					& pick.getColumn().trim().equalsIgnoreCase(ginPick.getColumn())
				    					& !pick.getTier().trim().equalsIgnoreCase(ginPick.getTier())){
	
									errFound = true;
									errMsg += "\n*" + ctx.getString(R.string.errTierNotFound);	
									break;
									}
								
							}
							if(!errFound){
								errMsg = ctx.getString(R.string.errPickNotFound);
							}
							if(!errMsg.trim().equalsIgnoreCase("")){
								throw new Exception(errMsg);
							}
						}
						else{
							throw new Exception(ctx.getString(R.string.errPickNotFound));		
						}
	    			}
	    		}
    		}
    		else{
    			valid = true;
    		}    			
    	}
    	catch(Exception ex){
    		throw ex;
    	}
    	return valid;
    }
    //this is used to show the exact message for item validation
    private ArrayList<GinPick> getGinPicksByProduct(String prodCode){
    	ArrayList<GinPick> list = new ArrayList<GinPick>();
		for(GinPick pick: this.ginPicks){
			if(pick.getProductCode().toString().trim().equalsIgnoreCase(prodCode)){
				list.add(pick);
			}
    	}			
    	return list;
    }
  
    public Boolean validateProduct(Context ctx, String prodCode) throws Exception{
		boolean valid = true;
		if(!this.getProductCode().toString().trim().equalsIgnoreCase(prodCode)){
			throw new Exception(ctx.getString(R.string.errProductNotFound));
		}
		return valid;		
	}
    public Boolean validateBatch(Context ctx, String batch) throws Exception{
		boolean valid = true;
		if(!this.getBatchNo().toString().trim().equalsIgnoreCase(batch)){
			throw new Exception(ctx.getString(R.string.errBatchNotFound));
		}
		return valid;		
	}

    public Boolean validateLot(Context ctx, String lot) throws Exception{
		boolean valid = true;
		if(!this.getLotNo().toString().trim().equalsIgnoreCase(lot)){
			throw new Exception(ctx.getString(R.string.errLotNotFound));
		}
		return valid;		
	}
    public Boolean validateProdBatchLot(Context ctx, String prodCode, String batch, String lot, String[] errMsg) throws Exception{
		boolean retValue = true;
		errMsg[0] = null;
		try{
			if(!this.getProductCode().toString().trim().equalsIgnoreCase(prodCode)){		
				
				retValue = false;
				errMsg[0] = ctx.getString(R.string.errProductNotFound);
			}
			else{
				if(!this.getBatchNo().toString().trim().equalsIgnoreCase(batch)
					& this.getLotNo().toString().trim().equalsIgnoreCase(lot)){	
				
					retValue = false;
					errMsg[0] += "\n*" + ctx.getString(R.string.errBatchNotFound);
				}
				else if(this.getBatchNo().toString().trim().equalsIgnoreCase(batch)
						 & !this.getLotNo().toString().trim().equalsIgnoreCase(lot)){		
					
					retValue = false;
					errMsg[0] += "\n*" + ctx.getString(R.string.errLotNotFound);
				}
				else if(!this.getBatchNo().toString().trim().equalsIgnoreCase(batch)
						 & !this.getLotNo().toString().trim().equalsIgnoreCase(lot)){		
					
					retValue = false;
					errMsg[0] += "\n*" + ctx.getString(R.string.errBatch_LotNotFound);
				}
			}
			
		}
		catch(Exception ex){
			throw ex;
		}
		return retValue;
	}
	public static Comparator<SerialNo> sortBySerialNo = new Comparator<SerialNo>(){
		public int compare(SerialNo serial1, SerialNo serial2){
			String serialNo1 = serial1.getSerialNo().toString().toUpperCase();
			String serialNo2 = serial2.getSerialNo().toString().toUpperCase();
		return serialNo1.compareTo(serialNo2);		
		}
	};

	public int getUnAllocatedCrateQty() throws Exception{
		int retValue = 0;
		try{
			GinCrate crate =new GinCrate();
			int subTotal = crate.getActQtyForItemCode(this.getItemCode());
			
			retValue = this.getActQty() - subTotal;
		}
		catch(Exception ex){
			throw ex;
		}
		return retValue;  	
	}
	
	public GinDetail clone(){
		GinDetail cloneGinDetail = new GinDetail();
		cloneGinDetail.transactionNo = this.transactionNo;
		cloneGinDetail.seqNo = this.seqNo;
		cloneGinDetail.itemCode = this.itemCode;
		cloneGinDetail.warehouseNo = this.warehouseNo;
		cloneGinDetail.productCode = this.productCode;
		cloneGinDetail.description = this.description;
		cloneGinDetail.batchNo = this.batchNo;
		cloneGinDetail.lotNo = this.lotNo;
		cloneGinDetail.uom = this.uom;
		cloneGinDetail.actQty = this.actQty;
		cloneGinDetail.hasSerialNo = this.hasSerialNo;
		cloneGinDetail.ginPicks = this.ginPicks;
		cloneGinDetail.ginSerialNos = this.ginSerialNos;
		
		return cloneGinDetail;
	}
	
	//method to get next gin pick
	public GinPick getNextGinPick(GinPick currentPick, boolean lastPick){
		GinPick nextPick = null;
		if(this.ginPicks.size() > 1){
			for(int i =1; i < this.ginPicks.size() ;i++){
				if(i == currentPick.getSeqNo()){
					nextPick = this.ginPicks.get(i);
					break;
				}
			}
		}
		if(nextPick != null){
			if(nextPick.getSeqNo() == this.ginPicks.size()){
				lastPick =true;
			}
			else{
				lastPick =false;
			}
		}
		else{
			lastPick= true;
		}
		return nextPick;			
	}
	public boolean isLastPick(GinPick currentPick)
	{
		if(currentPick.getSeqNo() == this.ginPicks.size()){
			return true;	
		}	
		else{
			return false;
		}
	}
}
