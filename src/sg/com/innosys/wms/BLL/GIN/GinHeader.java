package sg.com.innosys.wms.BLL.GIN;


import android.annotation.SuppressLint;
import android.content.Context;

import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import sg.com.innosys.wms.BLL.Common.WhAppException;
import sg.com.innosys.wms.BLL.Common.WhEnum.ScanNavigationOption;
import sg.com.innosys.wms.BLL.Crating.GinCrate;
import sg.com.innosys.wms.BLL.Crating.GinCrateProduct;
import sg.com.innosys.wms.DAL.GIN.DbGin;
import sg.com.innosys.wms.R;

@SuppressWarnings("serial")
public class GinHeader implements Serializable{
	private int transactionNo;
	private String custCode;
	private String custName;
	private Date ginDate;
	private Boolean isConfirmedGin;
	private String referenceNo; // 20131230 Gerry added PIL change
	private ScanNavigationOption scanOption;
	public ArrayList<GinDetail> ginDetails;
	public ArrayList<GinCrate> ginCrates;
	public ArrayList<GinPick> headerGinPicks;
	
	public GinHeader(){
		this.transactionNo=0;
		this.custCode = "";
		this.custName = "";
		this.ginDate = new Date();
		this.isConfirmedGin = false;
		this.referenceNo = "";
		this.ginDetails= new ArrayList<GinDetail>();
		this.ginCrates= new ArrayList<GinCrate>();
		this.scanOption = ScanNavigationOption.COMPLETE_PICKING_ALL_ITEMS_BEFORE_SERIALNO;
		this.headerGinPicks = new ArrayList<GinPick>();
		} 
	
	public int getTransactionNo() {
	        return transactionNo;
	    }
    public void setTransactionNo(int transactionNo) {
        this.transactionNo = transactionNo;
    	}
    public String getCustCode() {
        return custCode;
	    }
	public void setCustCode(String custCode) {
	    this.custCode = custCode;
		}
	public String getCustName() {
        return custName;
	    }
	public void setCustName(String custName) {
	    this.custName = custName;
		}
	// 20131230 Gerry added PIL change
	public String getReferenceNo() {
        return referenceNo;
	    }
	public void setReferenceNo(String refNo) {
	    this.referenceNo = refNo;
		}
	//20131230 end
	public Date getGinDate() {
        return ginDate;
	    }
	public void setGrnDate(Date ginDate) {
	    this.ginDate = ginDate;
		}	 
    public Boolean getIsConfirmedGin(){
    	return isConfirmedGin;
    	}
    public void setIsConfirmedGin(Boolean isConfirmedGin){
    	this.isConfirmedGin = isConfirmedGin;
    	} 
    public ScanNavigationOption getScanOption(){
    	return scanOption;
    	}
    public void setScanNavigationOption(ScanNavigationOption scanOption){
    	this.scanOption = scanOption;
    	} 
    
    public StringEntity jsonGinHeaderString() throws JSONException, UnsupportedEncodingException{
		JSONArray arrayDetail = new JSONArray();
    	if(this.ginDetails.size() >0){
    		for(GinDetail tempGinDetail : this.ginDetails){
    			//convert to jsonObject then add to jsonArray of grnDetails
            	arrayDetail.put(tempGinDetail.convertToJSONGinDetail());  
    		}
    	}    
    	JSONArray arrayCrates = new JSONArray();
    	if(this.ginCrates.size() >0){
    		for(GinCrate tempGinCrate : this.ginCrates){
    			arrayCrates.put(tempGinCrate.convertToJSONGinCrate());
    		}
    	}
		long epochTime = this.getGinDate().getTime();
    	String date = "/Date(" + String.valueOf(epochTime)+ ")/";
    	JSONStringer jsonGinHeader = new JSONStringer()
        .object()
        	.key("ginHeader")
        		.object()
                	.key("TransactionNo").value(this.getTransactionNo())
                	.key("CustCode").value(this.getCustCode().toString())
                	.key("CustName").value(this.getCustName().toString())       
                	.key("ReferenceNo").value(this.getReferenceNo().toString())   // 20131230 Gerry added PIL change            	
                	.key("GinDate").value(date)
                    .key("GinDetails").value(arrayDetail)
                    .key("GinCrates").value(arrayCrates)
                    .key("PdaOption").value(this.getScanOption().getIntValue()) //TODO temporary set to 1
	            .endObject()
	    .endObject();            
    	//20141007 - gerry modified replaced code
        //StringEntity entity = new StringEntity(jsonGrnHeader.toString().replace("\\", ""));
        StringEntity entity = new StringEntity(jsonGinHeader.toString());
        //20141007 end
        return entity;
	}
	public GinHeader convertJSONObjToGinHeader(JSONObject jsonObj) throws JSONException,Exception{
		String jsonDate = jsonObj.getString("GinDate");            	
    	String timestamp = (jsonDate.substring(6, jsonDate.length()-6)).replace("+", "");
    	//Date createdOn = new Date(Long.parseLong(timestamp));	                
    	Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(timestamp));
        
        GinHeader ginHeader = new GinHeader();                
        ginHeader.setTransactionNo(Integer.parseInt(jsonObj.getString("TransactionNo")));
        ginHeader.setCustCode(jsonObj.getString("CustCode"));
        ginHeader.setCustName(jsonObj.getString("CustName"));        
        ginHeader.setReferenceNo(jsonObj.getString("ReferenceNo"));      // 20131230 Gerry added PIL change     
        ginHeader.setGrnDate(calendar.getTime());
        
        return ginHeader;
	}

	public GinDetail getGinDetail(String itemCode) throws Exception{
		GinDetail retValue = null;
		try{
			if(this.ginDetails.size() >0){
				for(int i =0; i< this.ginDetails.size(); i++){
					if(this.ginDetails.get(i).getItemCode().toString().equalsIgnoreCase(itemCode.toString())){						
						retValue = this.ginDetails.get(i);
						break;
					}
				}
			}			
		}
		catch(Exception e){
			throw e;
		}		
		return retValue;
	}
	public String getProductDesc(String prodCode){
		String desc ="";
		for(GinDetail det : this.ginDetails){
			if(det.getProductCode().trim().equalsIgnoreCase(prodCode.trim())){
				desc = det.getProductDescription().toUpperCase();
				break;
			}
			else{
				desc = "Product not found. ";
			}
		}
		return desc;
	}

	public Boolean validateProdBatchLotFromHeader(Context ctx, String prodCode, String batch, String lot, String[] errMsg) throws Exception{
		boolean retValue = false;
		errMsg[0] = null;
		try{
			if(this.ginDetails.size() >0){
				for(GinDetail tempGrnDetail : this.ginDetails){
					if(tempGrnDetail.getProductCode().toString().trim().equalsIgnoreCase(prodCode)
						& tempGrnDetail.getBatchNo().toString().trim().equalsIgnoreCase(batch)
						& tempGrnDetail.getLotNo().toString().trim().equalsIgnoreCase(lot)){					
	
						retValue = true;
						break;
					}
				}
				if(!retValue){
    				boolean errFound = false;
    				if(getGinDetailsByProduct(prodCode).size()>0){
    					for(GinDetail det : getGinDetailsByProduct(prodCode)){
    						if(!det.getBatchNo().toString().trim().equalsIgnoreCase(batch)
    							& det.getLotNo().toString().trim().equalsIgnoreCase(lot)){
    							
    							errMsg[0] = ctx.getString(R.string.errBatchNotFound);
    							errFound = true;
    							break;
    						}
    						else if(det.getBatchNo().toString().trim().equalsIgnoreCase(batch)
    							& !det.getLotNo().toString().trim().equalsIgnoreCase(lot)){
    							
    							errMsg[0] = ctx.getString(R.string.errLotNotFound);
    							errFound = true;
    							break;
        					}
    					}
    					if(!errFound){
    						errMsg[0] = ctx.getString(R.string.errBatch_LotNotFound);
    					}
    				}
    				else{
    					errMsg[0] = ctx.getString(R.string.errProductNotFound);
    				}					
				}
			}
		}
		catch(Exception ex){
			throw ex;
		}
		return retValue;
	}
	//this is used to show the exact message for item validation
    private ArrayList<GinDetail> getGinDetailsByProduct(String prodCode){
    	ArrayList<GinDetail> list = new ArrayList<GinDetail>();
		for(GinDetail det: this.ginDetails){
			if(det.getProductCode().toString().trim().equalsIgnoreCase(prodCode)){
				list.add(det);
			}
    	}
    	return list;
    }
 
    //Start Crating codes
    public String validateGinDetailActQtyToSrvQty(Context context) throws WhAppException{
    	String actQtyOutString = "";
		try{
			if(this.ginDetails.size()>0){
				int actQtyCounter =0;
				int serialErrCount = 0;		
				int pickingErrorCount = 0;				
				String serialNoQtyMsg = "";
				String pickingQtyMsg ="";
				int pickingCounter=0;
				String noPickingDetailMsg ="";
				
				for(GinDetail det: this.ginDetails){
					//this will check if there is picking done before confimation
					if(det.ginPicks.size() > 0){
						int ginPickTotalQty =0;
						for(GinPick tempPick : det.ginPicks){
							ginPickTotalQty += tempPick.getActQty();
							if(tempPick.getActQty() != 0){
								pickingCounter++;
							}
						}							
						if(det.getActQty() != ginPickTotalQty){
							if(pickingErrorCount == 0){
								pickingQtyMsg = context.getString(R.string.errUnbalanceprodQty);
							}
							pickingQtyMsg +="\n" +context.getString(R.string.strCode) +": "+ det.getProductCode() +
									"\t" +context.getString(R.string.strPickingActQty)+": "+ ginPickTotalQty+
									"\t" +context.getString(R.string.strItemActQty)+": " + det.getActQty();
							
							pickingErrorCount++;
						}
					}					
					else {
						noPickingDetailMsg += "\nSeqNo: " + det.getSeqNo() + "\tProdCode: " + det.getProductCode() + "\tExptd Qty: "+ det.getServerQty();
					}
					if(!noPickingDetailMsg.equalsIgnoreCase("")){
						throw new WhAppException(context.getString(R.string.errCannotConfirmGinWithOutPicking) +"\n" +noPickingDetailMsg);						
					}				
					
					
					if(det.getActQty() != det.ginSerialNos.size()){
						if(det.getHasSerialNo()){							
							if(serialErrCount == 0){
								serialNoQtyMsg = context.getString(R.string.errUnbalanceprodSerialNo);
							}
							serialNoQtyMsg +="\n" +context.getString(R.string.strCode) +": "+ det.getProductCode() +
											"\t" +context.getString(R.string.lvhActQuantity)+": "+ det.getActQty() +
											"\t" +context.getString(R.string.tvwNoOfScannedSerial)+": " + det.ginSerialNos.size();	
							
							serialErrCount++;	
						}						
					}	
					if(det.getActQty() != det.getServerQty()){
						if(actQtyCounter == 0){
							actQtyOutString = context.getString(R.string.errUnbalanceprodQty);
						}
						actQtyOutString +="\n" +context.getString(R.string.strCode) +": "+ det.getProductCode() +
								"\t" +context.getString(R.string.lvhActQuantity)+": "+ det.getActQty() +
								"\t" +context.getString(R.string.strExpectedQty)+": " + det.getServerQty();							
					
						actQtyCounter++;
					}	
				}
				if(pickingCounter == 0){
					throw new WhAppException(context.getString(R.string.errCannotConfirmGinWithOutPicking));
				}
				if(pickingErrorCount > 0){
					throw new WhAppException(pickingQtyMsg);
				}
				if(serialErrCount > 0){
					throw new WhAppException(serialNoQtyMsg);
				}
			}							
		}		
		catch(WhAppException e){
			throw e;
		}
		catch(Exception e){
			return e.toString();
		}		
		return actQtyOutString;  	
    }
    public String validateGinDetailActQtyToCrate(Context ctx) throws Exception{
    	String retValue = null;
    	if(getUnAllocatedGinDetails().size() > 0){
			int counter = 0;
			for(GinDetail tempGinDetail : getUnAllocatedGinDetails()){	
				if(tempGinDetail.getActQty() != 0){
					if(counter == 0){
						retValue = ctx.getString(R.string.errCrateQtyAndPickingNotEqual) + " :";
					}
					retValue += "\nProd "  +ctx.getString(R.string.strCode) +": " +tempGinDetail.getProductCode();
					retValue += "\t  " +ctx.getString(R.string.lvhActQuantity)+": " + Integer.toString(this.getDetailByItemCode(tempGinDetail.getTransactionNo(),tempGinDetail.getItemCode()).getActQty())
							+"\t "+ctx.getString(R.string.tvwUnAllocatedQty)+": "+Integer.toString(tempGinDetail.getActQty());
				}
				counter ++;
			}
		}
		return retValue;    	
    }
    public boolean validateAddCrate(Context ctx, GinCrate ginCrate) throws WhAppException{
    	boolean retValue = true;
    	if(!ginCrate.getCrateNo().trim().equals("")){
    		for(GinCrate tempCrate : this.ginCrates){
    			if(tempCrate.getTransactionNo() == ginCrate.getTransactionNo()
    				& tempCrate.getCrateNo().trim().equalsIgnoreCase(ginCrate.getCrateNo())){
    				
    				throw new WhAppException(ctx.getString(R.string.errCrateNoExist));
    			}
    		}
    	}
		else
			throw new WhAppException(ctx.getString(R.string.errBlankCrateNo));    
    	
    	return retValue;    	
    }
    public boolean addCrate(Context ctx, GinCrate ginCrate) throws Exception{
    	boolean retValue =false;
    	DbGin dbWhGin = new DbGin(ctx);
    	try{
    		dbWhGin.beginTransaction();
    		if(validateAddCrate(ctx,  ginCrate)){
        		dbWhGin.insertGinCrate(dbWhGin.db, ginCrate);
        		this.ginCrates.add(ginCrate);        		

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
    public boolean updateCrate(Context ctx, GinCrate ginCrate) throws Exception{
    	boolean retValue =false;
    	DbGin dbWhGin = new DbGin(ctx);
    	try{
    		dbWhGin.beginTransaction();
    		for(GinCrate tempCrate : this.ginCrates){
    			if(tempCrate.getTransactionNo() == ginCrate.getTransactionNo()
    				& tempCrate.getCrateNo().trim().equalsIgnoreCase(ginCrate.getCrateNo().trim())){
    				
    				//assigned value to update memory collection
    				ginCrate.crateProducts = tempCrate.crateProducts;
    				
    				dbWhGin.updateGinCrate(dbWhGin.db, ginCrate);
    				tempCrate.setCrateDesc(ginCrate.getCrateDesc().toUpperCase());
    				tempCrate.setCrateMarking(ginCrate.getCrateMarking().toUpperCase());
    				break;
    			}
    		}    
    		dbWhGin.commitTransaction();   
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
    public boolean deleteCrate(Context ctx, GinCrate ginCrate) throws Exception{
    	boolean retValue =false;
    	DbGin dbWhGin = new DbGin(ctx);
    	try{
    		dbWhGin.beginTransaction();//called from BDHelper
    		for(GinCrate crate : this.ginCrates){
    			if(crate.getTransactionNo() == ginCrate.getTransactionNo()
    				& crate.getCrateNo().trim().equalsIgnoreCase(ginCrate.getCrateNo().trim())){    				

    				this.ginCrates.remove(crate);
    				dbWhGin.deleteAllProductInCrate(dbWhGin.db, crate);
    				dbWhGin.deleteGinCrate(dbWhGin.db, crate);
    				break;
    			}
    		}
    		dbWhGin.commitTransaction(); //called from BDHelper
    	}
    	catch(Exception ex){
    		throw ex;
    	}
    	finally{
    		dbWhGin.endTransaction();//called from BDHelper
    		dbWhGin.closeDB();//called from BDHelper
    	}
    	return retValue;
    }
    public ArrayList<GinDetail> getUnAllocatedGinDetails() throws Exception{
    	ArrayList<GinDetail> retValue = new ArrayList<GinDetail>();    
    	ArrayList<GinDetail> clonedDetails = new ArrayList<GinDetail>();    
    	
    	for(GinDetail cloneDet : this.ginDetails){
    		GinDetail newGinDetail = cloneDet.clone();
    		clonedDetails.add(newGinDetail);
    	}
    	if(clonedDetails.size() > 0){
			//loop to gin details
			for(GinDetail det : clonedDetails){
				int origItemQty = this.getGinDetail(det.getItemCode()).getActQty();
				int unallocatedQty = origItemQty;
				for(GinCrate crate : this.ginCrates){
					unallocatedQty -= crate.getActQtyForItemCode(det.getItemCode());
				}
				if(unallocatedQty > 0){
					det.setActQty(unallocatedQty);				
					retValue.add(det);					
				}
			}
		}  	
    	return retValue;
    	
    }
    public Boolean validateAddCrateProductQtyFromUnAllocatedDetail(Context ctx, GinCrateProduct crateProd, int newQty) throws Exception{
    	
    	for(GinDetail tempDetail : this.getUnAllocatedGinDetails()){
    		if(tempDetail.getTransactionNo() == crateProd.getTransactionNo()
        		& tempDetail.getItemCode().trim().equalsIgnoreCase(crateProd.getItemCode().trim())){
    			
    			int balQty = tempDetail.getActQty();
    			if(newQty > balQty){    			
    				throw new Exception(ctx.getString(R.string.errCrateProdQtyExceedFromUnAllocatedQty));
    			}
    			
    			break;
    		}
    	}
    	
		return true;    	
    }
    public Boolean validateEditCrateProductQty(Context ctx, GinCrateProduct crateProd, int newQty) throws Exception{
    	boolean itemFullyAllocated = true;
    	for(GinDetail tempDetail : this.getUnAllocatedGinDetails()){
    		if(tempDetail.getTransactionNo() == crateProd.getTransactionNo()
    			& tempDetail.getItemCode().trim().equalsIgnoreCase(crateProd.getItemCode().trim())){    			

    			itemFullyAllocated = false;
        		int balQty = tempDetail.getActQty() + crateProd.getActQty();
    			if(newQty > balQty){    			
    				throw new Exception(ctx.getString(R.string.errCrateProdQtyExceedFromUnAllocatedQty));
    			}
    			break;
    		}
    	}
    	if(itemFullyAllocated){
    		if(newQty > crateProd.getActQty()){
				throw new Exception(ctx.getString(R.string.errCrateProdQtyExceedFromUnAllocatedQty));    			
    		}
    	}
		return true;    	
    }
    public void assignSelectedCrateFromMemory(GinCrate crate){
    	for(GinCrate tempCrate: this.ginCrates){
    		if(crate.getTransactionNo() == tempCrate.getTransactionNo()
				& crate.getCrateNo().trim().equalsIgnoreCase(tempCrate.getCrateNo().trim())){    
    			
    			crate.crateProducts = tempCrate.crateProducts;
    		}
    	}
    }
    public boolean confirmGin(Context context) throws Exception{
    	boolean retValue =false;
    	DbGin dbWhGin = new DbGin(context);
    	try{
    		dbWhGin.beginTransaction();//called from BDHelper
    		
    		dbWhGin.confirmedGin(dbWhGin.db, this.getTransactionNo());
    		this.setIsConfirmedGin(true);    		
    		dbWhGin.commitTransaction(); //called from BDHelper
    	}
    	catch(Exception ex){
    		throw ex;
    	}
    	finally{
    		dbWhGin.endTransaction();//called from BDHelper
    		dbWhGin.closeDB();//called from BDHelper
    	}
    	return retValue;
    	
    }

	public GinDetail getDetailByItemCode(int trxNo, String itemCode){
		for(GinDetail det : this.ginDetails){
			if(det.getTransactionNo() == trxNo
					&& det.getItemCode().trim().equalsIgnoreCase(itemCode)){
				
				return det;
			}			
		}
		return null;
	}

	//20121029 - auto populate next item
	public GinDetail getNextGinDetail(GinDetail currentDetail, boolean lastDetail){
		GinDetail nextDetail = null;
		if(this.ginDetails.size() > 1){
			for(int i =1; i < this.ginDetails.size() ;i++){
				if(i == currentDetail.getSeqNo()){
					nextDetail = this.ginDetails.get(i);
					break;
				}
			}
		}
		if(nextDetail!=null){
			if(nextDetail.getSeqNo() == this.ginDetails.size()){
				lastDetail= true;
			}
		}
		return nextDetail;			
	}
	private String formatTransactionNoString(String trxNo){
		switch(trxNo.length()){
			case 1:
				trxNo = "00000" +trxNo;
				break;
			case 2:
				trxNo = "0000" +trxNo;
				break;	
			case 3:
				trxNo = "000" +trxNo;
				break;
			case 4:
				trxNo = "00" +trxNo;
				break;
			case 5:
				trxNo = "0" +trxNo;
				break;
			default:
				break;
		}
		return trxNo;
	}
	public String generateCrateNo(){
		String retCrateNo = "";		
		if(this.ginCrates.size() > 0){
			String lastCrateNo = this.ginCrates.get(this.ginCrates.size() - 1).getCrateNo();
			int lastNo = Integer.parseInt(lastCrateNo.substring(8, lastCrateNo.length()));
			
			String nextNo = Integer.toString(lastNo + 1);
			switch(nextNo.length()){
				case 1:
					nextNo = "00" +nextNo;
					break;
				case 2:
					nextNo = "0" +nextNo;
					break;
				default:
					break;				
			}			
			retCrateNo = lastCrateNo.substring(0,8) + nextNo; 
		}
		else{
			String trxNo =formatTransactionNoString(Integer.toString(this.getTransactionNo()));			
			
			retCrateNo = trxNo +"CR"+ "001";
		}
		return retCrateNo;
	}

	public void clearSQLiteDatabase(Context context) throws WhAppException, Exception{
		DbGin dbWhGin = new DbGin(context);
		try{			
			dbWhGin.beginTransaction();//call method from DBHelper
    		
			dbWhGin.clearAllSQLiteTransactionData(dbWhGin.db);			
			dbWhGin.commitTransaction();			
		}catch(Exception e){ throw new WhAppException(e.getLocalizedMessage()); }
		finally { 
			dbWhGin.endTransaction(); 
			dbWhGin.closeDB();
		}
	}
	 //Create new collection of Gin pick for header
    public void createGinPicksByLocation(){
    	if(this.ginDetails.size() >0 && this.headerGinPicks.size() == 0){
    		for(GinDetail det : this.ginDetails){
    			if(det.ginPicks.size() > 0){
    				for(GinPick pick : det.ginPicks){
    					this.headerGinPicks.add(pick);
    				}
    			}
    		}
    	}
    }
    
    public void sortbyLocation(){
    	if(this.headerGinPicks.size() > 0){
			Comparator<GinPick> comparebyLocation = GinHeader.compareByLocation;
			Collections.sort(this.headerGinPicks, comparebyLocation);
    	}    	
    }
    public GinPick getNextPreviousPicking(int index){
    	GinPick retValue = null;
    	if(this.headerGinPicks.size() > 0){
    		retValue = this.headerGinPicks.get(index);
    	}  
    	return retValue;
    }    
    //for newly added picking; in case new location
    //we don't know where the exact position after insert into the collection
    public GinPick getCurrentPicking(String itemCode, int SeqNo){
    	GinPick retValue = null;
    	if(this.headerGinPicks.size() > 0){
    		for(int i =0; i < this.headerGinPicks.size(); i++){
    			if(this.headerGinPicks.get(i).getItemCode().equalsIgnoreCase(itemCode) 
    					&& this.headerGinPicks.get(i).getSeqNo() == SeqNo){

    	    		retValue = this.headerGinPicks.get(i);
    	    		break;
    			}
    		}
    	}  
    	return retValue;
    }
	@SuppressLint("DefaultLocale")
	public static Comparator<GinPick> compareByLocation = new Comparator<GinPick>(){
		public int compare(GinPick pick1, GinPick pick2){
			String pickZone1 = pick1.getZone().toString().toUpperCase();
			String pickRow1 = pick1.getRow().toString().toUpperCase();
			String pickCol1 = pick1.getColumn().toString().toUpperCase();
			String pickTier1 = pick1.getTier().toString().toUpperCase();
			String pickItemCode1 = pick1.getItemCode().toString().toUpperCase();
			String pickProdCode1 = pick1.getProductCode().toString().toUpperCase();
			String pickZone2 = pick2.getZone().toString().toUpperCase();
			String pickRow2 = pick2.getRow().toString().toUpperCase();
			String pickCol2 = pick2.getColumn().toString().toUpperCase();
			String pickTier2 = pick2.getTier().toString().toUpperCase();
			String pickItemCode2 = pick2.getItemCode().toString().toUpperCase();
			String pickProdCode2 = pick2.getProductCode().toString().toUpperCase();
			
			int compareByZone = pickZone1.compareTo(pickZone2);	
			int compareByRow = pickRow1.compareTo(pickRow2);	
			int compareByColumn = pickCol1.compareTo(pickCol2);	
			int compareByTier = pickTier1.compareTo(pickTier2);	
			int compareByItemCode = pickItemCode1.compareTo(pickItemCode2);	
			int compareByProdCode = pickProdCode1.compareTo(pickProdCode2);	
			
			return (compareByZone == 0 ? (compareByRow==0 ? (compareByColumn == 0 ? (compareByTier == 0 ? (compareByItemCode == 0 ? compareByProdCode : compareByItemCode) : compareByTier) : compareByColumn) : compareByRow) : compareByZone);
			
			
			//return palletId1.compareTo(palletId2);		
		}
	};
	
}
