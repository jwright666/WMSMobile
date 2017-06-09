package sg.com.innosys.wms.BLL.GRN;

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
import java.util.HashMap;

import sg.com.innosys.wms.BLL.Common.WhAppException;
import sg.com.innosys.wms.BLL.Common.WhEnum.ConfirmGrnType;
import sg.com.innosys.wms.DAL.GRN.DbGrn;
import sg.com.innosys.wms.R;

@SuppressWarnings("serial")
public class GrnHeader implements Serializable {
	private int transactionNo;
	private String wareHouseNo;
	private String custCode;
	private String custName;
	private Date grnDate;
	private ConfirmGrnType confirmGrnType;	
	public ArrayList<GrnDetail> grnDetails;
	public HashMap<String,Pallet> hmPallet;

	public GrnHeader(){
		this.transactionNo=0;
		this.wareHouseNo ="";
		this.custCode = "";
		this.custName = "";
		this.grnDate = new Date();
		this.confirmGrnType = ConfirmGrnType.NOT_USING_TABLET;
		this.grnDetails= new ArrayList<GrnDetail>();
		this.hmPallet = new HashMap<String,Pallet>();
		} 
	
	public int getTransactionNo() {
	        return transactionNo;
	    }
    public void setTransactionNo(int transactionNo) {
        this.transactionNo = transactionNo;
    	}
	public String getWarehouseNo() {
        return wareHouseNo;
    }
	public void setWarehouseNo(String wareHouseNo) {
	    this.wareHouseNo = wareHouseNo;
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
	public Date getGrnDate() {
        return grnDate;
	    }
	public void setGrnDate(Date grnDate) {
	    this.grnDate = grnDate;
		}	    
	public ConfirmGrnType getConfirmGrnType() {
        return confirmGrnType;
	    }
	public void setConfirmGrnType(ConfirmGrnType confirmGrnType) {
	    this.confirmGrnType = confirmGrnType;
		}	    
 
	public StringEntity jsonGrnHeaderString() throws JSONException, UnsupportedEncodingException{
		JSONArray arrayDetail = new JSONArray();
    	if(this.grnDetails.size() >0){
    		for(GrnDetail tempGrnDetail : this.grnDetails){
    			//convert to jsonObject then add to jsonArray of grnDetails
            	arrayDetail.put(tempGrnDetail.convertToJSONGrnDetail());  
    		}
    	}    
		long epochTime = this.getGrnDate().getTime();
    	String date = "/Date(" + String.valueOf(epochTime)+ ")/";
    	JSONStringer jsonGrnHeader = new JSONStringer()
        .object()
        	.key("grnHeader")
        		.object()
                	.key("TransactionNo").value(this.getTransactionNo())
                	.key("CustCode").value(this.getCustCode().toString())
                	.key("CustName").value(this.getCustName().toString())
                	.key("WareHouseNo").value(this.getWarehouseNo())	                	
                	.key("GrnDate").value(date)
                    .key("GrnDetails").value(arrayDetail)
	            .endObject()
	    .endObject();   
    	//20141007 - gerry modified replaced code
        //StringEntity entity = new StringEntity(jsonGrnHeader.toString().replace("\\", ""));
        StringEntity entity = new StringEntity(jsonGrnHeader.toString());
      //20141007 end
        return entity;
	}
 	public GrnHeader convertJSONObjToGrnHeader(JSONObject jsonObj) throws JSONException{
	    String jsonDate = jsonObj.getString("GrnDate");            	
	    String timestamp = (jsonDate.substring(6, jsonDate.length()-6)).replace("+", "");           
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTimeInMillis(Long.parseLong(timestamp));
	        
	    GrnHeader grnHeader = new GrnHeader();                
	    grnHeader.setTransactionNo(jsonObj.getInt("TransactionNo"));
	    grnHeader.setCustCode(jsonObj.getString("CustCode"));
	    grnHeader.setCustName(jsonObj.getString("CustName"));
        grnHeader.setWarehouseNo(jsonObj.getString("WareHouseNo"));               
        grnHeader.setGrnDate(calendar.getTime());     
        
        int confirmGrnType =jsonObj.getInt("ConfirmGrnType");
        //for testing
        //int confirmGrnType = 1;
        if(confirmGrnType == 1){
            grnHeader.setConfirmGrnType(ConfirmGrnType.CONFIRM_GRN_AFTER_UPLOAD);          	
        }
        else if(confirmGrnType == 2){
            grnHeader.setConfirmGrnType(ConfirmGrnType.CONFIRM_GRN_BEFORE_UPLOAD);          	
        }
        else{
            grnHeader.setConfirmGrnType(ConfirmGrnType.NOT_USING_TABLET);          	
        }
        
	    return grnHeader;	            
	}
	public int getBalanceQtyBatchLot(String prodCode, String batch, String lot){
		int retValue =0;
		if(this.grnDetails.size() >0){
			for(GrnDetail tempGrnDetail : this.grnDetails){
				if(tempGrnDetail.getProductCode().toString().trim().equalsIgnoreCase(prodCode.trim())
					& tempGrnDetail.getBatchNo().toString().trim().equalsIgnoreCase(batch.trim())
					& tempGrnDetail.getLotNo().toString().trim().equalsIgnoreCase(lot.trim())){					
					
					int srvQty = tempGrnDetail.getServerQty();
					int actQty = tempGrnDetail.getActQty();
					retValue += (srvQty - actQty);
				}
			}		
		}	
		
		return retValue;
	}
	
	public Boolean updateGrnPut(Context context, String palletId, PalletProduct palletProd) throws Exception{
		DbGrn dbWhGrn = new DbGrn(context);	
		try{	
	    	//sqlite open and begin transaction
	    	//this method is called from DBHelper
			dbWhGrn.beginTransaction();			
	    	if(this.grnDetails.size()>0){
	    		for(GrnDetail tempGrnDetail : this.grnDetails){    								
					if(tempGrnDetail.getProductCode().toString().trim().equalsIgnoreCase(palletProd.getProductCode().trim())
							& tempGrnDetail.getBatchNo().toString().trim().equalsIgnoreCase(palletProd.getBatchNo().trim())
							& tempGrnDetail.getLotNo().toString().trim().equalsIgnoreCase(palletProd.getLotNo().trim())){													
						if(tempGrnDetail.grnPuts.size()>0){
							for(GrnPut tempGrnPut : tempGrnDetail.grnPuts){
								if(tempGrnPut.getPalletId().trim().equalsIgnoreCase(palletId)){									
									//TODO update grnPut
									tempGrnPut.setActQty(palletProd.getActQty());
									dbWhGrn.updateGrnPut(dbWhGrn.db, tempGrnPut);
									//update details
									tempGrnDetail.updateActQty();
									dbWhGrn.updateGrnDetail(dbWhGrn.db, tempGrnDetail);
								}								
							}
						}
					}
	    		}	    		
	    	}
	    	dbWhGrn.commitTransaction();  
		}
		catch(Exception e){ throw e; }
		finally { 
			dbWhGrn.endTransaction(); 
			dbWhGrn.closeDB();
		}
		return false;		
	}

	public Boolean addGrnPuts(Pallet pallet, PalletProduct palletProduct, Context context, Boolean exceedQty) throws Exception{
    	Boolean retValue = false;
		DbGrn dbWhGrn = new DbGrn(context);
    	try{
	    	GrnPut grnPut = new GrnPut();	    	
	    	//sqlite open and begin transaction
	    	//this method is called from DBHelper
			dbWhGrn.beginTransaction();			
	    	if(this.grnDetails.size()>0){
	    		for(GrnDetail tempGrnDetail : this.grnDetails){    			
					//start create GrnPut object
	    			//qty should be filled later	
	    			grnPut = new GrnPut();
					grnPut.setTransactionNo(tempGrnDetail.getTransactionNo());
					//grnPut.setSeqNo(tempGrnDetail.getSeqNo());
					grnPut.setPalleteId(pallet.getPalletId().toUpperCase());
					grnPut.setZone(pallet.getZone().toUpperCase());
					grnPut.setHasRack(pallet.getHasRack());
					grnPut.setRow(pallet.getRow());
					grnPut.setColumn(pallet.getColumn());
					grnPut.setTier(pallet.getTier());
					grnPut.setProductCode(palletProduct.getProductCode().toUpperCase());
					grnPut.setItemCode(tempGrnDetail.getItemCode());
					
					if(tempGrnDetail.getProductCode().toString().trim().equalsIgnoreCase(palletProduct.getProductCode().trim())
							& tempGrnDetail.getBatchNo().toString().trim().equalsIgnoreCase(palletProduct.getBatchNo().trim())
							& tempGrnDetail.getLotNo().toString().trim().equalsIgnoreCase(palletProduct.getLotNo().trim())){													
						
						//If product exist in putting list force the user to update the existing one instead of creating new
    					if(!tempGrnDetail.isPuttingExist(context, dbWhGrn, grnPut)){	    
    						//get the available Balance qty of grnDetail
    	    				//int grnDetBalQty = tempGrnDetail.getServerQty() - tempGrnDetail.getActQty();
    	    				//case when putQty more than balance qty for this grnDetails based on    	    				
    	    				//this validation temporary disabled to allowed actQty exceed the server qty
    	    				//if(grnDetBalQty > 0){						
		    					//case when putQty less than balance qty for this grnDetail
    	    				/*
			    				if(grnDetBalQty >= palletProduct.getActQty()){
			    					grnPut.setActQty(palletProduct.getActQty());  
			    				}
		    					else{
		    						palletProduct.setActQty(palletProduct.getActQty() - grnDetBalQty);   
			    					grnPut.setActQty(grnDetBalQty); 
			    				} 	    		
			    			*/			
    	    				grnPut.setActQty(palletProduct.getActQty()); 
		    				//now write to database
		    				dbWhGrn.insertGrnPut(dbWhGrn.db, grnPut);  
		    				//now add to GrnDetail.grnPuts collection
		    				tempGrnDetail.grnPuts.add(grnPut);
		    				//recalculate GrnDetail actQty
		    				tempGrnDetail.updateActQty();
		    				//update GrnDetail from database
		    				dbWhGrn.updateGrnDetail(dbWhGrn.db, tempGrnDetail);
		    			
		    				//assinged palletProduct itemCode and serialNo flag
		    				palletProduct.setItemCode(tempGrnDetail.getItemCode());		
		    				palletProduct.setHasSerialNo(tempGrnDetail.getHasSerialNo());
		    				
		    				pallet.palletProductList.add(palletProduct);
		    				//this.hmPallet.put(pallet.getColumn(), pallet);
		        			retValue = true;
		    				//exit if no remaining putQty
		        			//if(palletProduct.getActQty() == 0)
		    				break;      
		    				//}
	    				}
					}
	    		}	    		
		    }   	    	
		    dbWhGrn.commitTransaction();  
    	}
    	catch(WhAppException ex){throw ex;}
    	catch(Exception ex){throw ex;}
		finally { 
			dbWhGrn.endTransaction(); 
			dbWhGrn.closeDB();
		}
    	return retValue;
    }	
    //this is used to show the exact message for item validation
    private ArrayList<GrnDetail> getGrnDetailsByProduct(String prodCode){
    	ArrayList<GrnDetail> list = new ArrayList<GrnDetail>();
		for(GrnDetail det: this.grnDetails){
			if(det.getProductCode().toString().trim().equalsIgnoreCase(prodCode)){
				list.add(det);
			}
    	}
    	return list;
    }
    public Boolean isGrnDetailFound(Context ctx, PalletProduct palletProd, String[] errMsg ){
    	Boolean retValue = false;
    	errMsg[0] = null;
    	if(this.grnDetails.size() >0){
			for(GrnDetail tempGrnDetail : this.grnDetails){
				if(tempGrnDetail.getProductCode().toString().trim().equalsIgnoreCase(palletProd.getProductCode().trim())
						& tempGrnDetail.getBatchNo().toString().trim().equalsIgnoreCase(palletProd.getBatchNo().trim())
						& tempGrnDetail.getLotNo().toString().trim().equalsIgnoreCase(palletProd.getLotNo().trim())){					

					if(getGrnDetailsByProduct(palletProd.getProductCode()).size() >0){
						for(GrnDetail det:getGrnDetailsByProduct(palletProd.getProductCode())){
							if(det.getBatchNo().toString().trim().equalsIgnoreCase(palletProd.getBatchNo())
								&& det.getLotNo().toString().trim().equalsIgnoreCase(palletProd.getLotNo())){
								
								retValue = true;
								break;
							}
						}					
					}
					break;
				}
			}		
			if(!retValue){
				Boolean errorFound = false;
				if(getGrnDetailsByProduct(palletProd.getProductCode()).size() >0){
					for(GrnDetail det:getGrnDetailsByProduct(palletProd.getProductCode())){
						if(det.getBatchNo().toString().trim().equalsIgnoreCase(palletProd.getBatchNo())){
							errMsg[0] =ctx.getString(R.string.errLotNotFound);//"No LOT NO found for this product. ";		
							errorFound = true;
							break;
						}
					}
					if(!errorFound){
						for(GrnDetail det:getGrnDetailsByProduct(palletProd.getProductCode())){
							if(det.getLotNo().toString().trim().equalsIgnoreCase(palletProd.getLotNo())){
								errMsg[0] =ctx.getString(R.string.errBatchNotFound);//"No BATCH NO found for this product. ";	
								break;
								}
						}
						if(errMsg[0]== null){
							errMsg[0] ="*"+ctx.getString(R.string.errBatchNotFound);//"No BATCH NO found for this product. ";	
							errMsg[0] += "\n*" +ctx.getString(R.string.errLotNotFound);//"No LOT NO found for this product. ";	
						}
					}
				}
				else{
					errMsg[0] = "*"+ctx.getString(R.string.errProductNotFound);//"No PRODUCT CODE found. ";	
					errMsg[0] += "\n*"+ctx.getString(R.string.errBatchNotFound);//"No BATCH NO found for this product. ";	
					errMsg[0] += "\n*" +ctx.getString(R.string.errLotNotFound);	//"No LOT NO found for this product. ";	
				}
			}
		}	
    	return retValue;
    }
    public void deleteGrnPutFromPalletId(Context context, Pallet pallet) throws Exception{
		DbGrn dbWhGrn = new DbGrn(context);
    	try{
			dbWhGrn.beginTransaction();//call method from DBHelper
			for(GrnDetail tempDetail:this.grnDetails){
				if(tempDetail.grnSerialNos.size() > 0){
					//delete serialNos from database
					dbWhGrn.deleteGrnSerialNoByPallet(dbWhGrn.db, tempDetail.getTransactionNo(), pallet.getPalletId());   		
					for(int i=tempDetail.grnSerialNos.size() -1; i>=0; i--){			
	    				if(tempDetail.grnSerialNos.get(i).getPalletId().trim().equalsIgnoreCase(pallet.getPalletId())){		    				
		    									
	    					tempDetail.grnSerialNos.remove(i);
    					}
					}					
				}
				
				if(tempDetail.grnPuts.size() >0){
					for(int j = tempDetail.grnPuts.size() - 1; j >= 0; j--){
						if(tempDetail.grnPuts.get(j).getPalletId().trim().equalsIgnoreCase(pallet.getPalletId())
	    					&& tempDetail.grnPuts.get(j).getItemCode().trim().equalsIgnoreCase(tempDetail.getItemCode())){		    				

	    					dbWhGrn.deletegrnPut(dbWhGrn.db, tempDetail.grnPuts.get(j), true);
							tempDetail.grnPuts.remove(tempDetail.grnPuts.get(j));
	    					tempDetail.updateActQty();
	    					dbWhGrn.updateGrnDetail(dbWhGrn.db, tempDetail);  
							
							this.hmPallet.remove(pallet.getPalletId().toUpperCase());							
						}
					}
				}							
			}					
		
    		dbWhGrn.commitTransaction();//call method from DBHelper
    	}
    	catch(Exception ex)
    	{ 
    		throw ex; 
    	}    	
		finally { 
			dbWhGrn.endTransaction(); 
			dbWhGrn.closeDB();
		}
    }
    public void deleteGrnPutFromPalletProduct(Context context,Pallet pallet, PalletProduct palletProduct) throws Exception{
		DbGrn dbWhGrn = new DbGrn(context);
    	try{
    		dbWhGrn.beginTransaction();//call method from DBHelper
    		for(GrnDetail grnDetail : this.grnDetails){
    			if(grnDetail.getProductCode().toString().trim().equalsIgnoreCase(palletProduct.getProductCode().toString().trim())
					& grnDetail.getBatchNo().toString().trim().equalsIgnoreCase(palletProduct.getBatchNo().toString().trim())
					& grnDetail.getLotNo().toString().trim().equalsIgnoreCase(palletProduct.getLotNo().toString().trim())){
    				
    				if(grnDetail.grnSerialNos.size() > 0){
	    				//delete serialNos from database
						dbWhGrn.deleteGrnSerialNoByPalletProduct(dbWhGrn.db, grnDetail.getTransactionNo(), grnDetail.getItemCode(), pallet.getPalletId());   			
						//int serailNoCount = grnDetail.grnSerialNos.size();		
						for(int i=grnDetail.grnSerialNos.size() -1; i>=0; i--){			
		    				if(grnDetail.grnSerialNos.get(i).getPalletId().trim().equalsIgnoreCase(pallet.getPalletId())
			    				&& grnDetail.grnSerialNos.get(i).getItemCode().trim().equalsIgnoreCase(palletProduct.getItemCode())){		    				
			    									
								grnDetail.grnSerialNos.remove(i);
	    					}
						}
    				}
					for(int j = grnDetail.grnPuts.size() - 1; j >= 0; j--){
	    				if(grnDetail.grnPuts.get(j).getPalletId().trim().equalsIgnoreCase(pallet.getPalletId())
	    					&& grnDetail.grnPuts.get(j).getItemCode().trim().equalsIgnoreCase(palletProduct.getItemCode())){		    				
	    				
	    					dbWhGrn.deletegrnPut(dbWhGrn.db, grnDetail.grnPuts.get(j), false);
	    					grnDetail.grnPuts.remove(grnDetail.grnPuts.get(j));
	    					grnDetail.updateActQty();
	    					dbWhGrn.updateGrnDetail(dbWhGrn.db, grnDetail);  
	    				}
	    			} 	    			
	    		}
    		}
    		dbWhGrn.commitTransaction();//call method from DBHelper
    	}
    	catch(Exception ex)
    	{
    		throw ex; 
    	}    	
		finally { 
			dbWhGrn.endTransaction(); 
			dbWhGrn.closeDB();
		}
    }

	public static Comparator<Pallet> sortByPalletId = new Comparator<Pallet>(){
		public int compare(Pallet pallet1, Pallet pallet2){
			String palletId1 = pallet1.getPalletId().toString().toUpperCase();
			String palletId2 = pallet2.getPalletId().toString().toUpperCase();
			return palletId1.compareTo(palletId2);		
		}
	};
	

	public String checkGrnSerailNosBeforeUpload(Context context){
		int serialNoQtyCounter =0;
		String serialNoQtyErrString = null;
		for(Pallet pallet : new ArrayList<Pallet>(hmPallet.values())){
			for(PalletProduct palletProd : pallet.palletProductList){
				if(palletProd.getHasSerialNo()){
					if(palletProd.getActQty() != palletProd.grnSerialNos.size()){
						if(serialNoQtyCounter == 0){
							serialNoQtyErrString = context.getString(R.string.errUnbalanceprodSerialNo);
						}
						serialNoQtyErrString +="\n" +context.getString(R.string.strCode) +": "+ palletProd.getProductCode() +
								"\t" +context.getString(R.string.lvhActQuantity)+": "+ palletProd.getActQty() +
								"\t" +context.getString(R.string.tvwNoOfScannedSerial)+": " + palletProd.grnSerialNos.size() +
								"\t" +context.getString(R.string.tvwPallet_Id)+": " + pallet.getPalletId();
						
						serialNoQtyCounter++;
					}
				}
			}
		}
		return serialNoQtyErrString;
	}
	public String checkGrnHeaderQytBeforeUpload(Context context) throws WhAppException{
		String puttingQtyOutString = "";
		try{
			if(this.grnDetails.size()>0){
				int puttingQtyCounter =0;
				int puttingCount = 0;				
				String serialNoQtyOutString ="";
				
				for(GrnDetail det: this.grnDetails){
					//this will check if there is putting done before uploading
					if(det.getActQty() > 0){
						puttingCount++;
					}					
					if(det.getActQty() != det.grnSerialNos.size()){
						//20121129 added only ConfirmGrnType.CONFIRM_GRN_AFTER_UPLOAD will be check
						if(det.getHasSerialNo() && this.confirmGrnType == ConfirmGrnType.CONFIRM_GRN_AFTER_UPLOAD){	
							serialNoQtyOutString = checkGrnSerailNosBeforeUpload(context);
						}						
					}	
					if(det.getActQty() != det.getServerQty()){
							if(puttingQtyCounter == 0){
								puttingQtyOutString = context.getString(R.string.errUnbalanceprodQty);
							}
							puttingQtyOutString +="\n" +context.getString(R.string.strCode) +": "+ det.getProductCode() +
									"\t" +context.getString(R.string.lvhActQuantity)+": "+ det.getActQty() +
									"\t" +context.getString(R.string.strExpectedQty)+": " + det.getServerQty();							
						
							puttingQtyCounter++;
						}	
					}		
					if(puttingCount <= 0){
						throw new WhAppException(context.getString(R.string.errCannotUploadWithoutPutting));
					}
					if(!serialNoQtyOutString.equals("")){
						throw new WhAppException(serialNoQtyOutString);
					}
			}	
		}		
		catch(WhAppException e){
			throw e;
		}
		catch(Exception e){
			return e.toString();
		}		
		return puttingQtyOutString;
	}
	public ArrayList<GrnDetail> getUnbalanceGrnDetailList() throws Exception{
		ArrayList<GrnDetail> retValue = new ArrayList<GrnDetail>();
		try{
			if(this.grnDetails.size()>0){
				for(GrnDetail det : this.grnDetails){
					if(det.getActQty() != det.getServerQty()){
						retValue.add(det);
					}
				}
			}
		}
		catch(Exception e){throw e;}
		return retValue;
		
	}	
	public void clearSQLiteDatabase(Context context) throws Exception{
		DbGrn dbWhGrn = new DbGrn(context);
		try{			
    		dbWhGrn.beginTransaction();//call method from DBHelper
    		
    		dbWhGrn.clearAllSQLiteData(dbWhGrn.db);
			
			dbWhGrn.commitTransaction();
		}catch(Exception e){ throw e; }
		finally { 
			dbWhGrn.endTransaction(); 
			dbWhGrn.closeDB();
		}
	}
	
	public GrnDetail getSelectedGrnDetailForSerialNo(PalletProduct palletProd){
		for(GrnDetail tempDetail : this.grnDetails){
			if(tempDetail.getItemCode().trim().equalsIgnoreCase(palletProd.getItemCode().trim())){
				return tempDetail;
			}				
		}
		return null;
	}	

	//validate duplicate palletId
	public boolean isPalletIDExist(Context ctx, String palletID) throws WhAppException{
		for(String tempID : this.hmPallet.keySet()){
			if(tempID.toString().trim().equalsIgnoreCase(palletID.trim())){
				throw new WhAppException(ctx.getString(R.string.errDuplicatePalletID));
			}
		}	
		return false;
	}

	//20131231 - gerry added this method to auto generate pallet id 
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String generatePalletID(){
		String retPalletID = "";	
		String strTrxNo = formatTransactionNoString(Integer.toString(this.getTransactionNo()));
		if(this.hmPallet.size() > 0){
			ArrayList generatedIDs = new ArrayList();
			for(String tempID : this.hmPallet.keySet()){
				if(tempID.startsWith(strTrxNo)){
					boolean isNumberic= true;
					int id = 0;
					try{										
						id = Integer.parseInt(tempID.substring(tempID.length() - 3, tempID.length()));
					}
					catch(Exception ex){ isNumberic = false; }
					
					if(isNumberic)
					generatedIDs.add(id);
				}
			}			
			int lastNo = 0;
			if(generatedIDs.size() >0 ){
				lastNo = Collections.max(generatedIDs);		
			}
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
			retPalletID = strTrxNo +"-"+ nextNo; 
		}
		else{	
			retPalletID = strTrxNo + "-001";
		}
		return retPalletID;
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
}
