package sg.com.innosys.wms.BLL.Common;

import android.app.Application;

import java.util.ArrayList;

import sg.com.innosys.wms.BLL.Common.WhEnum.GINPickSortOption;
import sg.com.innosys.wms.BLL.GIN.GinDetail;
import sg.com.innosys.wms.BLL.GIN.GinHeader;
import sg.com.innosys.wms.BLL.GIN.GinPick;
import sg.com.innosys.wms.BLL.GRN.GrnDetail;
import sg.com.innosys.wms.BLL.GRN.GrnHeader;
import sg.com.innosys.wms.BLL.GRN.GrnPut;
import sg.com.innosys.wms.BLL.GRN.Pallet;
import sg.com.innosys.wms.BLL.GRN.PalletProduct;
import sg.com.innosys.wms.DAL.GIN.DbGin;
import sg.com.innosys.wms.DAL.GRN.DbGrn;

public class WhApp extends Application {
	//using server local IP if using the same domain
	//public final static String URL_string = "http://192.168.44.165/AndroidService/AndroidService.svc/";
	//using server public IP if out the office
	//public final String URL_string = "http://203.117.219.1/AndroidService.svc/";
	//public String userID = "ipl";
	
	public static String URL_string;
	//public final static String webServiceName = "/AndroidService/AndroidService.svc/";
	public final static String webServiceName = "/AndroidService.svc/";
	public static String device_ID;
	public LoginInfo loginInfo;
	
	public static final int ADDMODE = 1;
	public static final int EDITMODE = 2;
	public static final int DELETEMODE = 3;
	
	public static int formMode;
	
	
	private GrnHeader grnHeader;
	//private GrnDetail grnDetail; // this variable is for Gerry's testing only
	private DbGrn dbWhGrn;

	private GinHeader ginHeader;
	private DbGin dbWhGin;
	private GINPickSortOption ginPickSortOption;
	
	private WhMobileSettings whMobileSettings;

	@Override
    public void onCreate() {
    }

    @Override
    public void onTerminate() {
    }
    public void onResume(){
    }

    public Boolean insertGrnHeader() throws Exception{
    	try{
	    	dbWhGrn = new DbGrn(this);
	    	dbWhGrn.beginTransaction();//this method from DBHelper open sqlite and begin transaction 
    		dbWhGrn.insertGrnHeader(dbWhGrn.db, grnHeader);    	
    		if(grnHeader.grnDetails.size() > 0){
    			for(GrnDetail det : grnHeader.grnDetails){
    				dbWhGrn.insertGrnDetail(dbWhGrn.db, det);
    			}
    		}  		    		
    		dbWhGrn.commitTransaction();
    	}catch(Exception e){throw e;}
    	finally{
    		dbWhGrn.endTransaction();
    		dbWhGrn.closeDB();
    	}
    	
    	return true;
    }
    public Boolean insertGinHeader() throws Exception{
    	try{
	    	dbWhGin = new DbGin(this);
	    	dbWhGin.beginTransaction();
	    	//insert GinHeader to SQLite
    		dbWhGin.insertGinHeader(dbWhGin.db, ginHeader);
    		if(ginHeader.ginDetails.size() >0){
    			for(GinDetail ginDetail: ginHeader.ginDetails){
    				//insert GinDetail to SQLite
    	    		dbWhGin.insertGinDetail(dbWhGin.db, ginDetail);
    	    		if(ginDetail.ginPicks.size() >0 ){
    	    			for(GinPick ginPick : ginDetail.ginPicks){
    	    				//insert GinPick to SQLite
    	    	    		dbWhGin.insertGinPick(dbWhGin.db, ginPick);  				
    	    			}
    	    		}
    			}
    		}
    		
    		dbWhGin.commitTransaction();    		
    	}catch(Exception e){throw e;}
    	finally{
    		dbWhGin.endTransaction();
    		dbWhGin.closeDB();
    	}
    	return true;
    }  
    public ArrayList<GrnHeader> getAllGrnHeaders() throws Exception{
    	ArrayList<GrnHeader> list = new ArrayList<GrnHeader>();
    	try{
	    	dbWhGrn = new DbGrn(this);
	    	if(dbWhGrn.db == null)
	    		dbWhGrn.openDB();
	    	
	    	list = dbWhGrn.getAllGrnHeader();
	    	if(list.size() > 0)
	    		grnHeader = list.get(0);
	    	
	    	createGrnHeader();    	
		}catch(Exception e){throw e;}
		finally{
			if(dbWhGrn.db.isOpen()){
				dbWhGrn.closeDB();				
			}
		}
		return list;    	
    }
    public ArrayList<GinHeader> getAllGinHeaders() throws Exception{
    	ArrayList<GinHeader> list = new ArrayList<GinHeader>();
    	try{
	    	dbWhGin = new DbGin(this);
	    	dbWhGin.openDB();
	    	list = dbWhGin.getAllGinHeader();
			
		}catch(Exception e){throw e;}
		finally{
			if(dbWhGin.db.isOpen()){
				dbWhGin.closeDB();				
			}
		}
		return list;    	
    }
    //This method is called in case user turn off the device with uploading
    //this form the GRN inside the SQLite
    public void createGrnHeader() throws Exception{     
      if(grnHeader != null){	 
    	  if(grnHeader.grnDetails.size()>0){
    		  for(GrnDetail grnDetail : grnHeader.grnDetails){
    			  grnDetail.grnPuts = dbWhGrn.getGrnPutByGrnDetail(grnDetail);
    			  //to replace by the collection of serial no.
    			  grnDetail.grnSerialNos = dbWhGrn.getGrnDetailSerialNos(grnDetail);
    			  //grnDetail.setActQty();
    			  //dbWhGrn.updateGrnDetail(grnDetail);
  	        	  if(grnDetail.grnPuts.size()>0){
      				  for(GrnPut grnPut : grnDetail.grnPuts){ 
						Pallet tempPallet = new Pallet();
						tempPallet.setPalleteId(grnPut.getPalletId().toString());
						tempPallet.setZone(grnPut.getZone().toString());
						tempPallet.setRow(grnPut.getRow().toString());
						tempPallet.setColumn(grnPut.getColumn().toString());
						tempPallet.setHasRack(grnPut.getHasRack());
						tempPallet.setTier(grnPut.getTier().toString());
						//Add to hashMap
						grnHeader.hmPallet.put(tempPallet.getPalletId().toUpperCase(), tempPallet);
						//loop to all grnPutby PalletId PalletProduct
						ArrayList<PalletProduct> tempProductList = dbWhGrn.getAllPalletProductByPalletId(grnPut.getTransactionNo(), grnPut.getPalletId().toString().toUpperCase());
						ArrayList<PalletProduct> palletProductList = new ArrayList<PalletProduct>();
						if(tempProductList.size() > 0){
							for(PalletProduct tempPalletProduct : tempProductList){
								Boolean found = false;
								if(palletProductList.size() <= 0){
									palletProductList.add(tempPalletProduct);
								}
								else{
									for(PalletProduct palletProduct : palletProductList){
		  								if(tempPalletProduct.getProductCode().trim().equalsIgnoreCase(palletProduct.getProductCode().trim())
		  									& tempPalletProduct.getBatchNo().trim().equalsIgnoreCase(palletProduct.getBatchNo().trim())
		  									& tempPalletProduct.getLotNo().trim().equalsIgnoreCase(palletProduct.getLotNo().trim())){
		  									
		  									found = true;
		  									tempPalletProduct.grnSerialNos = dbWhGrn.getGrnPalletProductSerialNos(grnHeader.getTransactionNo(), tempPalletProduct.getItemCode(), tempPallet.getPalletId());
		  									//addQty = palletProduct.getActQty();
		  									palletProductList.remove(palletProduct);
		  									tempPalletProduct.setActQty(tempPalletProduct.getActQty() + palletProduct.getActQty());
		  									palletProductList.add(tempPalletProduct);
		  									break;
		  								}
		  							}									
									//tempPalletProduct.setActQty(tempPalletProduct.getActQty() + addQty);										
									
									if(!found){
	  									if(tempPalletProduct.getActQty() >0){
	  										palletProductList.add(tempPalletProduct);
	  									}
	  								}
								}				
							}
						}	
						grnHeader.hmPallet.get(tempPallet.getPalletId().toUpperCase()).palletProductList = palletProductList;
					
    				  }
    			  }	        		  
    		  }
    	  }
      }
      //dbWhGrn.close();
    }
   
    // All activities gets the same object
    public GrnHeader getGrnHeader(){	    	
    	return grnHeader;
    }
    
    public void setGrnHeader(GrnHeader grnHeader){
    	this.grnHeader = grnHeader;
    }
    
    //TODO this method for Gerry's testing only
    //public GrnDetail getGrnDetail(int one, int two){
    //	return grnDetail;
    //}
    
    
    // All activities gets the same object
    public GinHeader getGinHeader(){	    	
    	return ginHeader;
    }    
    public void setGinHeader(GinHeader ginHeader){
    	this.ginHeader = ginHeader;
    }   
    
    public LoginInfo getLoginInfo(){	    	
    	return loginInfo;
    }    
    public void setLoginInfo(LoginInfo loginInfo){
    	this.loginInfo = loginInfo;
    }    
    
    public GINPickSortOption getGINPickSortOption(){
    	return ginPickSortOption;
    }    
    public void setGINPickSortOption(GINPickSortOption sortOption){
    	this.ginPickSortOption = sortOption;
    } 
    
    public WhMobileSettings getWhMobileSettings(){
    	return this.whMobileSettings;
    }
    public void setMobileSettings(WhMobileSettings value){
    	this.whMobileSettings = value;
    }
}
