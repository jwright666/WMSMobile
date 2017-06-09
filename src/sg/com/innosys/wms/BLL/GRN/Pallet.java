package sg.com.innosys.wms.BLL.GRN;

import android.content.Context;
import android.os.Parcel;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

import sg.com.innosys.wms.BLL.Common.WhAppException;
import sg.com.innosys.wms.R;

@SuppressWarnings("serial")
public class Pallet implements Serializable{
	private String palletId;
	private String zone;
	private Boolean hasRack;
	private String row;
	private String column;
	private String tier;
	public ArrayList<PalletProduct> palletProductList;
	
	public Pallet(){
		this.palletId = "";
		this.zone = "";
		this.hasRack = true;
		this.row = "";
		this.column = "";
		this.tier = "";
		this.palletProductList = new ArrayList<PalletProduct>();
	}
	public Pallet(String palletId,String zone,Boolean hasRack, String row,String column,String tier,
	 			ArrayList<PalletProduct> palletProductList){
		this.palletId = palletId;
		this.zone = zone;
		this.hasRack = hasRack;
		this.row = row;
		this.column = column;
		this.tier = tier;
		this.palletProductList = palletProductList;
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
	    
    
 	public Comparator<PalletProduct> sortByProductCode = new Comparator<PalletProduct>(){
 		public int compare(PalletProduct palletProduct1, PalletProduct palletProduct2){
 			String prodCode1 = palletProduct1.getProductCode().toString().toUpperCase();
 			String prodCode2 = palletProduct2.getProductCode().toString().toUpperCase();
 			return prodCode1.compareTo(prodCode2); 				
 		}
 	};
 	
    public void addPalletProduct(PalletProduct palletProduct){
    	//ArrayList<PalletProduct> newPalletProList = new ArrayList<PalletProduct>();
    	if(this.palletProductList.size() >0){
    		for(PalletProduct tempPalletProd : this.palletProductList){
    			//Boolean found = false;
    			if(tempPalletProd.getProductCode().trim().equalsIgnoreCase(palletProduct.getProductCode().trim())
    					& tempPalletProd.getBatchNo().trim().equalsIgnoreCase(palletProduct.getBatchNo().trim())
    					& tempPalletProd.getLotNo().trim().equalsIgnoreCase(palletProduct.getLotNo().trim())){

    				//found = true;
    				tempPalletProd.setActQty(tempPalletProd.getActQty() + palletProduct.getActQty());
    				//this.palletProductList.add(tempPalletProd);
    				break;
    			}
    			else{
    				this.palletProductList.add(palletProduct);
    				break;
    			}
    		}   		
    	}
    	else{
    		this.palletProductList.add(palletProduct);
    	}
    }

    public void deletePalletProduct(PalletProduct palletProd){
    	for(PalletProduct tempPalletProd : this.palletProductList){
    			//Boolean found = false;
			if(tempPalletProd.getItemCode().trim().equalsIgnoreCase(palletProd.getItemCode().trim())
					& tempPalletProd.getProductCode().trim().equalsIgnoreCase(palletProd.getProductCode().trim())
					& tempPalletProd.getBatchNo().trim().equalsIgnoreCase(palletProd.getBatchNo().trim())
					& tempPalletProd.getLotNo().trim().equalsIgnoreCase(palletProd.getLotNo().trim())){

				this.palletProductList.remove(tempPalletProd);
				break;
			}
		}   		
    }
    public void updatePalletProduct(PalletProduct palletProd){
    	for(PalletProduct tempPalletProd : this.palletProductList){
    			//Boolean found = false;
			if(tempPalletProd.getItemCode().trim().equalsIgnoreCase(palletProd.getItemCode().trim())){
				
				tempPalletProd.setActQty(palletProd.getActQty());
				break;
			}
		}   		
    }
   
    public Boolean ValidateAgainstCompositeField(Context context, String[] compositeField) throws WhAppException{
    	String errMsg = "";
    	if(compositeField != null){
    		if(compositeField[0].toString().equalsIgnoreCase(this.zone)){
    			errMsg += context.getString(R.string.errNotSameZone);
    		}
    		if(compositeField[1].toString().equalsIgnoreCase(this.row)){
    			errMsg += "\n"+context.getString(R.string.errNotSameRow);
    		}
    		if(compositeField[2].toString().equalsIgnoreCase(this.column)){
    			errMsg += "\n"+context.getString(R.string.errNotSameColumn);
    		}
    		if(compositeField[3].toString().equalsIgnoreCase(this.tier)){
    			errMsg += "\n"+context.getString(R.string.errNotSameTier);
    		}
    	}
    	if(!errMsg.equals("")){
    		throw new WhAppException(errMsg);
    	}    	
    	return true;
    }
    private void writeObject(java.io.ObjectOutputStream out)
 	       throws IOException {
 	     // write 'this' to 'out'...
 	
    }  

	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		
	}
}	
