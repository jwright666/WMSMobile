package sg.com.innosys.wms.DAL.GIN;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import sg.com.innosys.wms.BLL.Common.WhAppException;
import sg.com.innosys.wms.BLL.Crating.GinCrate;
import sg.com.innosys.wms.BLL.Crating.GinCrateProduct;
import sg.com.innosys.wms.BLL.GIN.GinDetail;
import sg.com.innosys.wms.BLL.GIN.GinHeader;
import sg.com.innosys.wms.BLL.GIN.GinPick;
import sg.com.innosys.wms.BLL.GIN.GinSerialNo;
import sg.com.innosys.wms.DAL.Common.DatabaseConstants;
import sg.com.innosys.wms.DAL.Common.DbHelper;

public class DbGin extends DbHelper{
	//private SQLiteDatabase dbGIN;
	//this is inherit from DBHelper we can use db object for SQLiteDatabase
	
	public DbGin(Context context){
		super(context);
	}
	
	public int insertGinHeader(SQLiteDatabase dbGIN, GinHeader ginHeader) throws Exception{
		int insertId =0;
		try{
			ContentValues values = new ContentValues();
			values.put(DatabaseConstants.COLUMN_TRX_NO, ginHeader.getTransactionNo());
			values.put(DatabaseConstants.COLUMN_REF_NO, ginHeader.getReferenceNo());
			values.put(DatabaseConstants.COLUMN_CUST_CODE, ginHeader.getCustCode());
			values.put(DatabaseConstants.COLUMN_CUST_NAME, ginHeader.getCustName());
			values.put(DatabaseConstants.COLUMN_GRN_DATE, ginHeader.getGinDate().toString());
			values.put(DatabaseConstants.COLUMN_CONFIRMED_GIN, ginHeader.getIsConfirmedGin()?1 : 0);
			
			insertId = (int) dbGIN.insert(DatabaseConstants.TABLE_WH_GIN_HEADER, null,
				values);			
		}
		catch(Exception e){
			throw e;
		}
		return insertId;	
	}
	public int insertGinDetail(SQLiteDatabase dbGIN,GinDetail ginDetail) throws Exception{
		int insertId =0;
		try{
			ContentValues values = new ContentValues();
			values.put(DatabaseConstants.COLUMN_TRX_NO, ginDetail.getTransactionNo());
			values.put(DatabaseConstants.COLUMN_SEQNO, ginDetail.getSeqNo());
			values.put(DatabaseConstants.COLUMN_ITEM_CODE, ginDetail.getItemCode().toUpperCase());
			values.put(DatabaseConstants.COLUMN_WAREHOUSE_NO, ginDetail.getWarehouseNo().toUpperCase());
			values.put(DatabaseConstants.COLUMN_PRODUCTCODE, ginDetail.getProductCode().toUpperCase());
			values.put(DatabaseConstants.COLUMN_PROD_DESCRIPTION, ginDetail.getProductDescription().toUpperCase());
			values.put(DatabaseConstants.COLUMN_BATCH, ginDetail.getBatchNo().toUpperCase());
			values.put(DatabaseConstants.COLUMN_LOTNO, ginDetail.getLotNo().toUpperCase());
			values.put(DatabaseConstants.COLUMN_UOM, ginDetail.getUOM().toUpperCase());
			values.put(DatabaseConstants.COLUMN_ACT_QTY, ginDetail.getActQty());
			values.put(DatabaseConstants.COLUMN_SERVER_QTY, ginDetail.getServerQty());
			values.put(DatabaseConstants.COLUMN_SERIALFLAG, ginDetail.getHasSerialNo() ? 1:0 );
			insertId = (int)dbGIN.insert(DatabaseConstants.TABLE_WH_GIN_DETAIL, null,
					values);		
		}
		catch(Exception e){
			throw e;
		}
		return insertId;
	}
	public int insertGinPick(SQLiteDatabase dbGIN, GinPick ginPick) throws Exception{
		int insertId =0;
		try{
			//if(ginPick.getIsNew()){
			ginPick.setSeqNo(getRunningNo(DatabaseConstants.TABLE_WH_GIN_PICK, DatabaseConstants.COLUMN_SEQNO, ginPick.getTransactionNo(), ginPick.getItemCode().trim()));
			//}
			ContentValues values = new ContentValues();
			values.put(DatabaseConstants.COLUMN_TRX_NO, ginPick.getTransactionNo());
			values.put(DatabaseConstants.COLUMN_SEQNO, ginPick.getSeqNo());
			values.put(DatabaseConstants.COLUMN_ITEM_CODE, ginPick.getItemCode().toUpperCase());
			values.put(DatabaseConstants.COLUMN_PALLETID, ginPick.getPalletId().toUpperCase());
			values.put(DatabaseConstants.COLUMN_ZONE, ginPick.getZone().toUpperCase());
			values.put(DatabaseConstants.COLUMN_ISRACK, ginPick.getHasRack()?"T":"F");
			values.put(DatabaseConstants.COLUMN_PRODUCTCODE, ginPick.getProductCode().toUpperCase());
			values.put(DatabaseConstants.COLUMN_ROW_NO, ginPick.getRow().toUpperCase());
			values.put(DatabaseConstants.COLUMN_COLUMN_NO, ginPick.getColumn().toUpperCase());
			values.put(DatabaseConstants.COLUMN_TIER, ginPick.getTier().toUpperCase());
			values.put(DatabaseConstants.COLUMN_ACT_QTY, ginPick.getActQty());
			values.put(DatabaseConstants.COLUMN_SERVER_QTY, ginPick.getSrvQty());
			values.put(DatabaseConstants.COLUMN_ISNEW, ginPick.getIsNew()? 1 : 0);
			
			insertId = (int) dbGIN.insert(DatabaseConstants.TABLE_WH_GIN_PICK, null,
					values);	
		}
		catch(Exception e){
			throw e;
		}
		return insertId;
	}
	public int insertGinSerialNo(SQLiteDatabase dbGIN, GinSerialNo ginSerialNo) throws Exception{
		int insertId =0;
		try{		
			ginSerialNo.setSerialID(getRunningNo(DatabaseConstants.TABLE_WH_GIN_SERIAL_NOS, DatabaseConstants.COLUMN_SERIAL_ID, ginSerialNo.getTransactionNo(), ginSerialNo.getItemCode().trim()));
			ContentValues values = new ContentValues();
			values.put(DatabaseConstants.COLUMN_TRX_NO, ginSerialNo.getTransactionNo());
			values.put(DatabaseConstants.COLUMN_SERIAL_ID, ginSerialNo.getSerialID());
			values.put(DatabaseConstants.COLUMN_ITEM_CODE, ginSerialNo.getItemCode().toUpperCase());
			values.put(DatabaseConstants.COLUMN_SERIAL_No, ginSerialNo.getSerialNo().toUpperCase());
			values.put(DatabaseConstants.COLUMN_DESC, ginSerialNo.getDescription().toUpperCase());
			
			insertId = (int) dbGIN.insert(DatabaseConstants.TABLE_WH_GIN_SERIAL_NOS, null,
					values);	
		}
		catch(Exception e){
			throw e;
		}
		return insertId;
	}
	public int insertGinCrate(SQLiteDatabase dbGIN,GinCrate ginCrate) throws Exception{
		int insertId =0;
		try{			
			ContentValues values = new ContentValues();
			values.put(DatabaseConstants.COLUMN_TRX_NO, ginCrate.getTransactionNo());
			values.put(DatabaseConstants.COLUMN_CRATE_NO, ginCrate.getCrateNo().toUpperCase());
			values.put(DatabaseConstants.COLUMN_DESC, ginCrate.getCrateDesc().toUpperCase());
			values.put(DatabaseConstants.COLUMN_MARKING, ginCrate.getCrateMarking().toUpperCase());
			
			insertId = (int) dbGIN.insert(DatabaseConstants.TABLE_WH_GIN_CRATE, null,
					values);	
		}
		catch(Exception e){
			throw e;
		}
		return insertId;
	}
	public int insertGinCrateProduct(SQLiteDatabase dbGIN,GinCrateProduct ginCrateProduct) throws Exception{
		int insertId =0;
		try{			
			ContentValues values = new ContentValues();
			values.put(DatabaseConstants.COLUMN_TRX_NO, ginCrateProduct.getTransactionNo());
			values.put(DatabaseConstants.COLUMN_CRATE_NO, ginCrateProduct.getCrateNo().toUpperCase());
			values.put(DatabaseConstants.COLUMN_ITEM_CODE, ginCrateProduct.getItemCode().toUpperCase());
			values.put(DatabaseConstants.COLUMN_PRODUCTCODE, ginCrateProduct.getProductCode().toUpperCase());
			values.put(DatabaseConstants.COLUMN_UOM, ginCrateProduct.getUOM().toUpperCase());
			values.put(DatabaseConstants.COLUMN_ACT_QTY, ginCrateProduct.getActQty());
			
			insertId = (int) dbGIN.insert(DatabaseConstants.TABLE_WH_GIN_CRATE_PRODUCT, null,
					values);	
		}
		catch(SQLException e){
			throw e;			
		}
		catch(Exception e){
			throw e;
		}
		return insertId;
	}
	public int updateGinHeader(SQLiteDatabase dbGIN,GinHeader ginHeader) throws Exception{
		int updateId=0;
		try{
			ContentValues values = new ContentValues();
			values.put(DatabaseConstants.COLUMN_CONFIRMED_GIN, ginHeader.getIsConfirmedGin()? 1 : 0);
			
			updateId = dbGIN.update(DatabaseConstants.TABLE_WH_GIN_HEADER, values, 
										DatabaseConstants.COLUMN_TRX_NO + " = " + ginHeader.getTransactionNo(), null);
		}
		catch(Exception e){throw e;}		
		return updateId;
	}
	public int updateGinDetail(SQLiteDatabase dbGIN,GinDetail ginDetail) throws Exception{
		int updateId=0;
		try{
			ContentValues values = new ContentValues();
			values.put(DatabaseConstants.COLUMN_ACT_QTY, ginDetail.getActQty());
			
			updateId = dbGIN.update(DatabaseConstants.TABLE_WH_GIN_DETAIL, values, 
										DatabaseConstants.COLUMN_TRX_NO + " = " + ginDetail.getTransactionNo() + " AND " +
										DatabaseConstants.COLUMN_ITEM_CODE + " = '" + ginDetail.getItemCode() +"'", null);
			
		}
		catch(Exception e){throw e;}	
		return updateId;
	}
	public int updateGinPick(SQLiteDatabase dbGIN,GinPick ginPick) throws Exception{
		int updateId=0;	
		try{
			ContentValues values = new ContentValues();
			values.put(DatabaseConstants.COLUMN_ACT_QTY, ginPick.getActQty());	
			if(ginPick.getIsNew()){
				values.put(DatabaseConstants.COLUMN_ZONE, ginPick.getZone());
				values.put(DatabaseConstants.COLUMN_ROW_NO, ginPick.getRow());
				values.put(DatabaseConstants.COLUMN_COLUMN_NO, ginPick.getColumn());
				values.put(DatabaseConstants.COLUMN_TIER, ginPick.getTier());				
			}
			
			updateId = dbGIN.update(DatabaseConstants.TABLE_WH_GIN_PICK, values, 
										DatabaseConstants.COLUMN_TRX_NO + " = " + ginPick.getTransactionNo() + " AND " +
										DatabaseConstants.COLUMN_SEQNO + " = " + ginPick.getSeqNo() + " AND " +
										DatabaseConstants.COLUMN_ITEM_CODE + " = '" + ginPick.getItemCode().trim()+ "'"
										, null);
		}
		catch(Exception e){throw e;}		
		return updateId;
	}
	public int updateGinSerialNo(SQLiteDatabase dbGIN, GinSerialNo ginSerialNo) throws Exception{
		int updateId=0;	
		try{
			ContentValues values = new ContentValues();
			values.put(DatabaseConstants.COLUMN_SERIAL_No, ginSerialNo.getSerialNo().toUpperCase());
			values.put(DatabaseConstants.COLUMN_DESC, ginSerialNo.getDescription().toUpperCase());
			
			updateId = dbGIN.update(DatabaseConstants.TABLE_WH_GIN_SERIAL_NOS, values, 
										DatabaseConstants.COLUMN_TRX_NO + " = " + ginSerialNo.getTransactionNo() + " AND " +
										DatabaseConstants.COLUMN_SERIAL_ID + " = " + ginSerialNo.getSerialID() + " AND " +
										DatabaseConstants.COLUMN_ITEM_CODE + " = '" + ginSerialNo.getItemCode().trim().toUpperCase()+ "'" 
										, null);
		}
		catch(Exception e){throw e;}
		return updateId;
	}
	public int updateGinCrate(SQLiteDatabase dbGIN, GinCrate ginCrate) throws Exception{
		int updateId=0;	
		try{
			ContentValues values = new ContentValues();
			values.put(DatabaseConstants.COLUMN_DESC, ginCrate.getCrateDesc().toUpperCase());
			values.put(DatabaseConstants.COLUMN_MARKING, ginCrate.getCrateMarking().toUpperCase());
			
			updateId = dbGIN.update(DatabaseConstants.TABLE_WH_GIN_CRATE, values, 
										DatabaseConstants.COLUMN_TRX_NO + " = " + ginCrate.getTransactionNo() + " AND " +
										DatabaseConstants.COLUMN_CRATE_NO + " = '" + ginCrate.getCrateNo().trim() +"'" 
										, null);		
		}
		catch(Exception e){throw e;}	
		return updateId;
	}
	public int updateGinCrateProduct(SQLiteDatabase dbGIN,GinCrateProduct ginCrateProd) throws Exception{
		int updateId=0;	
		try{
			ContentValues values = new ContentValues();
			values.put(DatabaseConstants.COLUMN_ACT_QTY, ginCrateProd.getActQty());
			
			updateId = dbGIN.update(DatabaseConstants.TABLE_WH_GIN_CRATE_PRODUCT, values, 
										DatabaseConstants.COLUMN_TRX_NO + " = " + ginCrateProd.getTransactionNo() + " AND " +
										DatabaseConstants.COLUMN_CRATE_NO + " = '" + ginCrateProd.getCrateNo().trim() +"'" + " AND " +
										DatabaseConstants.COLUMN_ITEM_CODE + " = '" + ginCrateProd.getItemCode().trim() +"'"		
										, null);		
		}
		catch(Exception e){throw e;}	
		return updateId;
	}
	
	public void deleteGinCrate(SQLiteDatabase dbGIN,GinCrate ginCrate) throws Exception {
		try{
			dbGIN.delete(DatabaseConstants.TABLE_WH_GIN_CRATE, 
					DatabaseConstants.COLUMN_TRX_NO + " = " + ginCrate.getTransactionNo() 
					+ " AND " + DatabaseConstants.COLUMN_CRATE_NO + " = '" + ginCrate.getCrateNo().trim() +"'" , null);
		}
		catch(Exception e){throw e;}			
	}
	public void deleteAllProductInCrate(SQLiteDatabase dbGIN,GinCrate ginCrate) throws Exception {
		try{
			dbGIN.delete(DatabaseConstants.TABLE_WH_GIN_CRATE_PRODUCT, 
					DatabaseConstants.COLUMN_TRX_NO + " = " + ginCrate.getTransactionNo() 
					+ " AND " + DatabaseConstants.COLUMN_CRATE_NO + " = '" + ginCrate.getCrateNo().trim() +"'" , null);
		}
		catch(Exception e){throw e;}			
	}
	public void deleteGinCrateProduct(SQLiteDatabase dbGIN,GinCrateProduct ginCrateProd) throws Exception {
		try{
			dbGIN.delete(DatabaseConstants.TABLE_WH_GIN_CRATE_PRODUCT, 
					DatabaseConstants.COLUMN_TRX_NO + " = " + ginCrateProd.getTransactionNo() 
					+ " AND " + DatabaseConstants.COLUMN_CRATE_NO + " = '" + ginCrateProd.getCrateNo().trim() +"'" 
					+ " AND " + DatabaseConstants.COLUMN_ITEM_CODE + " = '" + ginCrateProd.getItemCode().trim() +"'"
					, null);
		}
		catch(Exception e){throw e;}			
	}
	
	public ArrayList<GinHeader> getAllGinHeader() throws Exception {
		ArrayList<GinHeader> ginHeaders = new ArrayList<GinHeader>();
		try{
			super.openDB();
			Cursor cursor = super.db.query(DatabaseConstants.TABLE_WH_GIN_HEADER,
					null, null, null, null, null, null);
	
			if(cursor.getCount() > 0){
				cursor.moveToFirst();
			}
			while (!cursor.isAfterLast()) {
				GinHeader ginHeader = cursorToGinHeader(cursor);
				ginHeaders.add(ginHeader);
				cursor.moveToNext();
			}
			// Make sure to close the cursor
			cursor.close();
		}
		catch(Exception e){
			throw e;
		}
		finally{
			if(super.db.isOpen()){
				super.closeDB();				
			}
		}
		return ginHeaders;
	}
	public GinHeader getGinHeader(int trxNo) throws Exception {
		GinHeader ginHeader = new GinHeader();
		try{
			super.openDB();
			Cursor cursor = super.db.query(DatabaseConstants.TABLE_WH_GIN_HEADER, null, 
					DatabaseConstants.COLUMN_TRX_NO +" = " + trxNo, null, null, null, null);
	
			if(cursor.getCount() > 0){
				cursor.moveToFirst();
			}
			while (!cursor.isAfterLast()) {
				ginHeader = cursorToGinHeader(cursor);
				cursor.moveToNext();
			}
			// Make sure to close the cursor
			cursor.close();
		}
		catch(Exception e){
			throw e;
		}
		finally{
			super.closeDB();
		}
		return ginHeader;
	}
	@SuppressLint("SimpleDateFormat")
	private GinHeader cursorToGinHeader(Cursor cursor) throws Exception{
		GinHeader ginHeader = new GinHeader();
		ginHeader.setTransactionNo(cursor.getInt(cursor.getColumnIndex(DatabaseConstants.COLUMN_TRX_NO)));
		ginHeader.setReferenceNo(cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_REF_NO)));
		ginHeader.setCustCode(cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_CUST_CODE)));
		ginHeader.setCustName(cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_CUST_NAME)));
		String date = cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_GRN_DATE));
		//2014107 -gerry removed date. no need to upload back because there is no use at all		
		//20140305 - gerry added to format date. Some OS versions hits error if date is not formatted by SimpleDateFormat
		//SimpleDateFormat formatter;
        //formatter = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
		//Date newDate = formatter.parse(date);		
		//ginHeader.setGrnDate(newDate);//(new Date(date));
		//20140304
		//2014107 end
		
		if(cursor.getInt(cursor.getColumnIndex(DatabaseConstants.COLUMN_CONFIRMED_GIN))==1)
			ginHeader.setIsConfirmedGin(true);
    	else
    		ginHeader.setIsConfirmedGin(false);
		ginHeader.ginDetails = getGinDetails(cursor.getInt(cursor.getColumnIndex(DatabaseConstants.COLUMN_TRX_NO)));
		ginHeader.ginCrates = getGinCrates(ginHeader);
		return ginHeader;
	}
	public ArrayList<GinDetail> getGinDetails(int trxNo) throws Exception{
		ArrayList<GinDetail> ginDetails = new ArrayList<GinDetail>();
		try{
			if(!super.db.isOpen())
				super.openDB();
			Cursor cursor = super.db.query(DatabaseConstants.TABLE_WH_GIN_DETAIL,null, 
					DatabaseConstants.COLUMN_TRX_NO +" = " + trxNo, null, null, null, null);
	
			if(cursor.getCount() > 0){
				cursor.moveToFirst();
			}
			while (!cursor.isAfterLast()) {
				GinDetail ginDetail = cursorToGinDetail(cursor);
				ginDetails.add(ginDetail);
				cursor.moveToNext();
			}
			// Make sure to close the cursor
			cursor.close();
		}
		catch(Exception e){
			throw e;
		}
		finally{
			super.closeDB();
		}
		return ginDetails;
	}
	private GinDetail cursorToGinDetail(Cursor cursor) throws Exception{
		GinDetail ginDetail = new GinDetail();
		ginDetail.setTransactionNo(cursor.getInt(cursor.getColumnIndex(DatabaseConstants.COLUMN_TRX_NO)));	            	
		ginDetail.setSeqNo(cursor.getInt(cursor.getColumnIndex(DatabaseConstants.COLUMN_SEQNO)));
		ginDetail.setItemCode(cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_ITEM_CODE)));
		ginDetail.setWarehouseNo(cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_WAREHOUSE_NO)));
		ginDetail.setProductCode(cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_PRODUCTCODE)));
		ginDetail.setProductDescription(cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_PROD_DESCRIPTION)));
		ginDetail.setBatchNo(cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_BATCH)));
		ginDetail.setLotNo(cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_LOTNO)));
    	ginDetail.setUOM(cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_UOM)));    	        	
    	ginDetail.setActQty(cursor.getInt(cursor.getColumnIndex(DatabaseConstants.COLUMN_ACT_QTY)));              	
    	ginDetail.setServerQty(cursor.getInt(cursor.getColumnIndex(DatabaseConstants.COLUMN_SERVER_QTY)));       	
    	if(cursor.getInt(cursor.getColumnIndex(DatabaseConstants.COLUMN_SERIALFLAG))==1)
    		ginDetail.setHasSerialNo(true);
    	else
    		ginDetail.setHasSerialNo(false);
    	ginDetail.ginPicks = getGinPicks(ginDetail);
    	ginDetail.ginSerialNos = getGinSerialNos(ginDetail);
		return ginDetail;
	}
	public ArrayList<GinPick> getGinPicks(GinDetail ginDetail) throws Exception{
		ArrayList<GinPick> ginPicks = new ArrayList<GinPick>();
		try{
			if(!super.db.isOpen())
				super.openDB();
			
			Cursor cursor = super.db.query(DatabaseConstants.TABLE_WH_GIN_PICK,null, 
					DatabaseConstants.COLUMN_TRX_NO +" = " + ginDetail.getTransactionNo() + " AND " +
					DatabaseConstants.COLUMN_ITEM_CODE + " = '" + ginDetail.getItemCode()+ "'", null, null, null, null);
	
			if(cursor.getCount() > 0){
				cursor.moveToFirst();
			}
			while (!cursor.isAfterLast()) {
				GinPick ginPick = cursorToGinPick(cursor);
				ginPicks.add(ginPick);
				cursor.moveToNext();
			}
			// Make sure to close the cursor
			cursor.close();
		}
		catch(Exception e){
			throw e;
		}
		finally{
			super.closeDB();
		}
		return ginPicks;
	}

	private GinPick cursorToGinPick(Cursor cursor){
		GinPick ginPick = new GinPick();
		ginPick.setTransactionNo(cursor.getInt(cursor.getColumnIndex(DatabaseConstants.COLUMN_TRX_NO)));	            	
		ginPick.setSeqNo(cursor.getInt(cursor.getColumnIndex(DatabaseConstants.COLUMN_SEQNO)));
		ginPick.setItemCode(cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_ITEM_CODE)));
		ginPick.setProductCode(cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_PRODUCTCODE)));  
		ginPick.setPalleteId(cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_PALLETID)));	            	
		ginPick.setZone(cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_ZONE)));
		if(cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_ISRACK)).toString().trim().equalsIgnoreCase("T")){
			ginPick.setHasRack(true);
		}
		else{
			ginPick.setHasRack(false);
		}
		ginPick.setRow(cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_ROW_NO)));  
		ginPick.setColumn(cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_COLUMN_NO)));
		ginPick.setTier(cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_TIER))); 
		ginPick.setActQty(cursor.getInt(cursor.getColumnIndex(DatabaseConstants.COLUMN_ACT_QTY)));
		ginPick.setSrvQty(cursor.getInt(cursor.getColumnIndex(DatabaseConstants.COLUMN_SERVER_QTY)));

    	if(cursor.getInt(cursor.getColumnIndex(DatabaseConstants.COLUMN_ISNEW))==1)
    		ginPick.setIsNew(true);
    	else
    		ginPick.setIsNew(false);
		
		return ginPick;
	}
	public ArrayList<GinSerialNo> getGinSerialNos(GinDetail ginDetail) throws Exception{
		ArrayList<GinSerialNo> ginSerialNos = new ArrayList<GinSerialNo>();
		try{
			if(!super.db.isOpen())
				super.openDB();
			Cursor cursor = super.db.query(DatabaseConstants.TABLE_WH_GIN_SERIAL_NOS,null, 
					DatabaseConstants.COLUMN_TRX_NO +" = " + ginDetail.getTransactionNo() + " AND " +
					DatabaseConstants.COLUMN_ITEM_CODE + " = '" + ginDetail.getItemCode()+ "'", null, null, null, null);
	
			if(cursor.getCount() > 0){
				cursor.moveToFirst();
			}
			while (!cursor.isAfterLast()) {
				GinSerialNo ginSerialNo = cursorToGinSerialNo(cursor);
				ginSerialNos.add(ginSerialNo);
				cursor.moveToNext();
			}
			// Make sure to close the cursor
			cursor.close();
		}
		catch(Exception e){
			throw e;
		}
		finally{
			super.closeDB();
		}
		return ginSerialNos;
	}
	private GinSerialNo cursorToGinSerialNo(Cursor cursor){
		GinSerialNo ginSerialNo = new GinSerialNo();
		ginSerialNo.setTransactionNo(cursor.getInt(cursor.getColumnIndex(DatabaseConstants.COLUMN_TRX_NO)));	            	
		ginSerialNo.setSerialID(cursor.getInt(cursor.getColumnIndex(DatabaseConstants.COLUMN_SERIAL_ID)));
		ginSerialNo.setItemCode(cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_ITEM_CODE)));
		ginSerialNo.setSerialNo(cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_SERIAL_No)));  
		ginSerialNo.setDescription(cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_DESC)));	            	
		
		return ginSerialNo;
	}
	public ArrayList<GinCrate> getGinCrates(GinHeader ginHeader) throws Exception{
		ArrayList<GinCrate> ginCrates = new ArrayList<GinCrate>();
		try{
			if(!super.db.isOpen())
				super.openDB();
			Cursor cursor = super.db.query(DatabaseConstants.TABLE_WH_GIN_CRATE,null, 
					DatabaseConstants.COLUMN_TRX_NO +" = " + ginHeader.getTransactionNo(), null, null, null, null);
	
			if(cursor.getCount() > 0){
				cursor.moveToFirst();
			}
			while (!cursor.isAfterLast()) {
				GinCrate ginCrate = cursorToGinCrate(cursor);
				ginCrates.add(ginCrate);
				cursor.moveToNext();
			}
			// Make sure to close the cursor
			cursor.close();
		}
		catch(Exception e){
			throw e;
		}
		finally{
			super.closeDB();
		}
		return ginCrates;
	}
	private GinCrate cursorToGinCrate(Cursor cursor) throws Exception{
		GinCrate ginCrate = new GinCrate();
		ginCrate.setTransactionNo(cursor.getInt(cursor.getColumnIndex(DatabaseConstants.COLUMN_TRX_NO)));	            	
		ginCrate.setCrateNo(cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_CRATE_NO)));
		ginCrate.setCrateDesc(cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_DESC)));
		ginCrate.setCrateMarking(cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_MARKING)));              	
		ginCrate.crateProducts = getGinCrateProducts(ginCrate);
		return ginCrate;
	}
	public ArrayList<GinCrateProduct> getGinCrateProducts(GinCrate ginCrate) throws Exception{
		ArrayList<GinCrateProduct> ginCrateProducts = new ArrayList<GinCrateProduct>();
		try{
			if(!super.db.isOpen())
				super.openDB();
			Cursor cursor = super.db.query(DatabaseConstants.TABLE_WH_GIN_CRATE_PRODUCT,null, 
					DatabaseConstants.COLUMN_TRX_NO +" = " + ginCrate.getTransactionNo() + " AND " + 
					DatabaseConstants.COLUMN_CRATE_NO +" = '" + ginCrate.getCrateNo().trim() +"'", null, null, null, null);
	
			if(cursor.getCount() > 0){
				cursor.moveToFirst();
			}
			while (!cursor.isAfterLast()) {
				GinCrateProduct ginCrateProduct = cursorToGinCrateProduct(cursor);
				ginCrateProducts.add(ginCrateProduct);
				cursor.moveToNext();
			}
			// Make sure to close the cursor
			cursor.close();
		}
		catch(Exception e){
			throw e;
		}
		finally{
			super.closeDB();
		}
		return ginCrateProducts;
	}
	private GinCrateProduct cursorToGinCrateProduct(Cursor cursor){
		GinCrateProduct ginCrateProduct = new GinCrateProduct();
		ginCrateProduct.setTransactionNo(cursor.getInt(cursor.getColumnIndex(DatabaseConstants.COLUMN_TRX_NO)));	            	
		ginCrateProduct.setCrateNo(cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_CRATE_NO)));
		ginCrateProduct.setItemCode(cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_ITEM_CODE)));
		ginCrateProduct.setProductCode(cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_PRODUCTCODE)));  
		ginCrateProduct.setUOM(cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_UOM))); 
		ginCrateProduct.setActQty(cursor.getInt(cursor.getColumnIndex(DatabaseConstants.COLUMN_ACT_QTY)));             	
		
		return ginCrateProduct;
	}
/*
	public int getGinPickNextSeqNo(int trxNo, String itemCode) throws Exception{
		int lastSeqNo = 0;
		try{
			if(!super.db.isOpen())
				super.openDB();
			String query = "select max(" + DatabaseConstants.COLUMN_SEQNO +") LAST_SEQNO from "+ DatabaseConstants.TABLE_WH_GIN_PICK +
							" where " + DatabaseConstants.COLUMN_TRX_NO+ " = " + trxNo +" AND " +
								DatabaseConstants.COLUMN_ITEM_CODE + " = '" + itemCode + "'";
			Cursor cursor = super.db.rawQuery(query, null);
			if(cursor.getCount() >0)
				cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				lastSeqNo = cursor.getInt(cursor.getColumnIndex("LAST_SEQNO"));
				cursor.moveToNext();
			}
			cursor.close();
		}
		catch(Exception e){throw e;}
		finally{ super.closeDB();}
		return lastSeqNo +1;
	}
*/	
	public Boolean confirmedGin(SQLiteDatabase dbGIN, int trxNo) throws Exception{
		boolean retValue =false;	
		try{
			ContentValues values = new ContentValues();
			values.put(DatabaseConstants.COLUMN_CONFIRMED_GIN, 1);
			
			int updateId = dbGIN.update(DatabaseConstants.TABLE_WH_GIN_HEADER, values, 
										DatabaseConstants.COLUMN_TRX_NO + " = " + trxNo 
										, null);
			if(updateId !=0){
				retValue = true;
			}						
		}
		catch(Exception e){throw e;}	
		return retValue;
	}

	public void clearGinCrateTable(SQLiteDatabase dbGIN){
		dbGIN.delete(DatabaseConstants.TABLE_WH_GIN_CRATE, null, null);
	}
	public void clearGrnCrateProductTable(SQLiteDatabase dbGIN){
		dbGIN.delete(DatabaseConstants.TABLE_WH_GIN_CRATE_PRODUCT, null, null);
	}
	public void clearGinSerialNoTable(SQLiteDatabase dbGIN){
		dbGIN.delete(DatabaseConstants.TABLE_WH_GIN_SERIAL_NOS, null, null);
	}
	public void clearGinPickTable(SQLiteDatabase dbGIN){
		dbGIN.delete(DatabaseConstants.TABLE_WH_GIN_PICK, null, null);
	}
	public void clearGinDetailTable(SQLiteDatabase dbGIN){
		dbGIN.delete(DatabaseConstants.TABLE_WH_GIN_DETAIL, null, null);
	}
	public void clearGinHeaderTable(SQLiteDatabase dbGIN){
		dbGIN.delete(DatabaseConstants.TABLE_WH_GIN_HEADER, null, null);
	}
	public void clearAllSQLiteTransactionData(SQLiteDatabase dbGIN) throws Exception{
		try{
			clearGrnCrateProductTable(dbGIN);
			clearGinCrateTable(dbGIN);
			clearGinSerialNoTable(dbGIN);
			clearGinPickTable(dbGIN);
			clearGinDetailTable(dbGIN);
			clearGinHeaderTable(dbGIN);
		}
		catch(Exception e){ throw e; }
	}
	public Boolean isSerialNoExist(String prodCode, String newSerialNo) throws WhAppException, Exception {
		Boolean isExist = false;
		try{
			String query = "SELECT " + DatabaseConstants.COLUMN_SERIAL_No +
							" FROM " + DatabaseConstants.TABLE_WH_GIN_SERIAL_NOS +
							" WHERE " + DatabaseConstants.COLUMN_ITEM_CODE + " in (Select " + DatabaseConstants.COLUMN_ITEM_CODE + 
														" FROM " + DatabaseConstants.TABLE_WH_GIN_DETAIL +			
														" WHERE " + DatabaseConstants.COLUMN_PRODUCTCODE + " = '" + prodCode +"')";							
								//SELECT Serial_No FROM wh_grn_serial_nos_tbl
								//where ItemCode in (Select ItemCode from wh_grn_detail_tbl where ProductCode = 'P1')
								
							//" INNER JOIN " + DatabaseConstants.TABLE_WH_GRN_DETAIL + " b " +
							//" ON a." + DatabaseConstants.COLUMN_ITEM_CODE + " = b." + DatabaseConstants.COLUMN_ITEM_CODE +
							//" AND a." + DatabaseConstants.COLUMN_TRX_NO + " = b." + DatabaseConstants.COLUMN_TRX_NO +
							//" WHERE " + DatabaseConstants.COLUMN_PALLETID + " = '" + palletId +"'";
			//Cursor cursor = dbGRN.query(DatabaseConstants.TABLE_WH_GRN_PUT,
			//		null, DatabaseConstants.COLUMN_PALLETID + " = '" + palletId +"'", null, null, null, null);
			Cursor cursor = super.db.rawQuery(query, null);
			if(cursor.getCount() >0)				
				cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				String temp = cursor.getString(0);
				if(temp.equalsIgnoreCase(newSerialNo))
				{
					isExist = true;
					break;
				}				
				cursor.moveToNext();
			}
			// Make sure to close the cursor
			cursor.close();
		}catch(SQLException e){throw new WhAppException(e.getMessage()); }
		catch(Exception e){throw e; }
		return isExist;
	}
}
