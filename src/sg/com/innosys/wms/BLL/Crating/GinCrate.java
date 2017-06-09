package sg.com.innosys.wms.BLL.Crating;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import sg.com.innosys.wms.BLL.Common.WhAppException;
import sg.com.innosys.wms.DAL.GIN.DbGin;
import sg.com.innosys.wms.R;
import sg.com.innosys.wms.R.string;

@SuppressWarnings("serial")
public class GinCrate  implements Serializable {
	private int transactionNo;
	private String crateNo;
	private String crateDesc;
	private String crateMarking;
	public ArrayList<GinCrateProduct> crateProducts;
	
	public GinCrate(){
		this.transactionNo =0;
		this.crateNo="";
		this.crateDesc="";
		this.crateMarking="";
		this.crateProducts = new ArrayList<GinCrateProduct>();
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
    public String getCrateDesc() {
        return crateDesc;
    }
    public void setCrateDesc(String crateDesc) {
        this.crateDesc = crateDesc;
    }
    public String getCrateMarking() {
        return crateMarking;
    }
    public void setCrateMarking(String crateMarking) {
        this.crateMarking = crateMarking;
    }
    
	public JSONObject convertToJSONGinCrate() throws JSONException{		
      	JSONObject jsonGinCrate = new JSONObject();
      	jsonGinCrate.put("TransactionNo", this.getTransactionNo());
      	jsonGinCrate.put("CrateNo", this.getCrateNo().trim());
      	jsonGinCrate.put("CrateDesc", this.getCrateDesc().trim());
      	jsonGinCrate.put("CrateMarking", this.getCrateMarking().trim());
      	
      	JSONArray jsonArrayGinCrateDetails = new JSONArray();
      	if(this.crateProducts.size()>0){
      		for(GinCrateProduct tempCrateProduct : this.crateProducts){
      			jsonArrayGinCrateDetails.put(tempCrateProduct.convertToJSONGinCrateDetail());
      		}
      	}
      	jsonGinCrate.put("GinCrateProducts", jsonArrayGinCrateDetails);

      	return jsonGinCrate;  
  	}
    
    
    public int getActQtyForItemCode(String itemCode){
    	int retValue = 0;
    	if(this.crateProducts.size() >0){
    		for(GinCrateProduct crateProd : this.crateProducts){
    			if(crateProd.getItemCode().trim().equalsIgnoreCase(itemCode)){
    				retValue += crateProd.getActQty();
    			}
    		}
    	}
		return retValue;    	
    }
    public boolean insertCrateProd(Context ctx, GinCrateProduct crateProd) throws Exception{
    	boolean retValue =false;
    	DbGin dbWhGin = new DbGin(ctx);
    	try{
    		dbWhGin.beginTransaction();
    		if(crateProd.getActQty() != 0){       	
    			boolean exist = false;
    	    	if(this.crateProducts.size() <= 0){
	    			//insert to database
	    			dbWhGin.insertGinCrateProduct(dbWhGin.db, crateProd); 
	        		//add to memory ollection
	        		this.crateProducts.add(crateProd);
    	    	}
    	    	else{
    	    		for(GinCrateProduct tempCrateProd : this.crateProducts){
    	    			//check if item exist, if exist the do update else insert new
    	    			if(tempCrateProd.getTransactionNo() == crateProd.getTransactionNo()
    	    				& tempCrateProd.getCrateNo().trim().equalsIgnoreCase(crateProd.getCrateNo().trim())
    	    				& tempCrateProd.getItemCode().trim().equalsIgnoreCase(crateProd.getItemCode().trim())){
    	    				
    	    				//set new qty
    	    				crateProd.setActQty(tempCrateProd.getActQty() + crateProd.getActQty());
    	    				//update from database
    	    				dbWhGin.updateGinCrateProduct(dbWhGin.db, crateProd);   
    	    				//update qty from memory collection 
    	            		tempCrateProd.setActQty(crateProd.getActQty()); 
    	    				
    	    				exist = true;
    	    				break;
    	    			}
    	    		}
    	    		if(!exist){   
    	    			//insert to database
    	    			dbWhGin.insertGinCrateProduct(dbWhGin.db, crateProd); 
    	        		//add to memory ollection
    	        		this.crateProducts.add(crateProd);
    	        	}
    	    	}
    		}
    		else
    			throw new WhAppException(ctx.getString(R.string.errZeroCrateProductQty));
    		
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
    public boolean updateCrateProd(Context ctx, GinCrateProduct crateProd) throws Exception{
    	boolean retValue =false;
    	DbGin dbWhGin = new DbGin(ctx);
    	try{
    		dbWhGin.beginTransaction();
    		if(crateProd.getActQty() != 0){     			
        		for(GinCrateProduct tempCrateProd : this.crateProducts){
        			if(tempCrateProd.getTransactionNo() == crateProd.getTransactionNo()
        				& tempCrateProd.getCrateNo().trim().equalsIgnoreCase(crateProd.getCrateNo().trim())
        				& tempCrateProd.getItemCode().trim().equalsIgnoreCase(crateProd.getItemCode().trim())){

                		dbWhGin.updateGinCrateProduct(dbWhGin.db, crateProd);
                		tempCrateProd.setActQty(crateProd.getActQty());
                		break;
        			}
        		}
    		}
    		else
    			throw new Exception(ctx.getString(R.string.errZeroCrateProductQty));

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
    public boolean deleteCrateProd(Context ctx, GinCrateProduct crateProd) throws Exception{
    	boolean retValue =false;
    	DbGin dbWhGin = new DbGin(ctx);
    	try{
    		dbWhGin.beginTransaction();
    		if(crateProd.getActQty() != 0){     			
        		for(GinCrateProduct tempCrateProd : this.crateProducts){
        			if(tempCrateProd.getTransactionNo() == crateProd.getTransactionNo()
        				& tempCrateProd.getCrateNo().trim().equalsIgnoreCase(crateProd.getCrateNo().trim())
        				& tempCrateProd.getItemCode().trim().equalsIgnoreCase(crateProd.getItemCode().trim())){
        				
        				//remove from database
                		dbWhGin.deleteGinCrateProduct(dbWhGin.db, crateProd);
        				//remove from memory
        				this.crateProducts.remove(tempCrateProd);
        				break;
        			}
        		}
    		}
    		else
    			throw new WhAppException(ctx.getString(R.string.errZeroCrateProductQty));

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

}
