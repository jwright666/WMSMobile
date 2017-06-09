package sg.com.innosys.wms.DAL.GRN;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;

import sg.com.innosys.wms.BLL.Common.WhAppException;
import sg.com.innosys.wms.BLL.Common.WhEnum.ConfirmGrnType;
import sg.com.innosys.wms.BLL.GRN.GrnDetail;
import sg.com.innosys.wms.BLL.GRN.GrnHeader;
import sg.com.innosys.wms.BLL.GRN.GrnPut;
import sg.com.innosys.wms.BLL.GRN.GrnSerialNo;
import sg.com.innosys.wms.BLL.GRN.PalletProduct;
import sg.com.innosys.wms.DAL.Common.DatabaseConstants;
import sg.com.innosys.wms.DAL.Common.DbHelper;

public class DbGrn extends  DbHelper{
	
	public DbGrn(Context context){
		super(context);
	}

	//Insert test data to grnHeader table
	public void insertGrnHeader(SQLiteDatabase dbGRN, GrnHeader grnHeader) throws Exception {
		try{
			ContentValues values = new ContentValues();
			values.put(DatabaseConstants.COLUMN_TRX_NO, grnHeader.getTransactionNo());
			values.put(DatabaseConstants.COLUMN_WAREHOUSE_NO, grnHeader.getWarehouseNo());
			values.put(DatabaseConstants.COLUMN_CUST_CODE, grnHeader.getCustCode());
			values.put(DatabaseConstants.COLUMN_CUST_NAME, grnHeader.getCustName());
			values.put(DatabaseConstants.COLUMN_GRN_DATE, grnHeader.getGrnDate().toString());
			int temp = grnHeader.getConfirmGrnType().getIntValue();
			values.put(DatabaseConstants.COLUMN_CONFIRM_GRNTYPE, temp);
		
			dbGRN.insert(DatabaseConstants.TABLE_WH_GRN_HEADER, null,
				values);
		}
		catch(SQLiteException ex){ throw ex; }
		catch(Exception e){throw e;}
	}
	//Insert test data to grnDetail table
	public void insertGrnDetail(SQLiteDatabase dbGRN, GrnDetail grnDetail) throws Exception {
		try{
			ContentValues values = new ContentValues();
			values.put(DatabaseConstants.COLUMN_TRX_NO, grnDetail.getTransactionNo());
			values.put(DatabaseConstants.COLUMN_SEQNO, grnDetail.getSeqNo());
			values.put(DatabaseConstants.COLUMN_PRODUCTCODE, grnDetail.getProductCode().toUpperCase());
			values.put(DatabaseConstants.COLUMN_PROD_DESCRIPTION, grnDetail.getProductDescription().toUpperCase());
			values.put(DatabaseConstants.COLUMN_BATCH, grnDetail.getBatchNo().toUpperCase());
			values.put(DatabaseConstants.COLUMN_LOTNO, grnDetail.getLotNo().toUpperCase());
			values.put(DatabaseConstants.COLUMN_UOM, grnDetail.getUOM().toUpperCase());
			values.put(DatabaseConstants.COLUMN_ACT_QTY, grnDetail.getActQty());
			values.put(DatabaseConstants.COLUMN_SERVER_QTY, grnDetail.getServerQty());
			values.put(DatabaseConstants.COLUMN_SERIALFLAG, grnDetail.getHasSerialNo() ? 1:0 );
			values.put(DatabaseConstants.COLUMN_ITEM_CODE, grnDetail.getItemCode().toUpperCase());
			values.put(DatabaseConstants.COLUMN_REF_NO1, grnDetail.getRef_No1().toUpperCase());
			values.put(DatabaseConstants.COLUMN_REF_NO2, grnDetail.getRef_No2().toUpperCase());
			values.put(DatabaseConstants.COLUMN_REF_NO3, grnDetail.getRef_No3().toUpperCase());
			values.put(DatabaseConstants.COLUMN_REF_NO4, grnDetail.getRef_No4().toUpperCase());
			
			dbGRN.insert(DatabaseConstants.TABLE_WH_GRN_DETAIL, null,
					values);		
		}
		catch(SQLiteException ex){ throw ex; }
		catch(Exception e){throw e;}
	}	
	//Insert data to grnPut table
	public int insertGrnPut(SQLiteDatabase dbGRN, GrnPut grnPut) throws Exception {
		int insertId =0;
		try{
			grnPut.setSeqNo(getRunningNo(DatabaseConstants.TABLE_WH_GRN_PUT, DatabaseConstants.COLUMN_SEQNO, grnPut.getTransactionNo(),grnPut.getItemCode().trim()));
			ContentValues values = new ContentValues();
			values.put(DatabaseConstants.COLUMN_TRX_NO, grnPut.getTransactionNo());
			values.put(DatabaseConstants.COLUMN_SEQNO, grnPut.getSeqNo());
			values.put(DatabaseConstants.COLUMN_PALLETID, grnPut.getPalletId().toUpperCase());
			values.put(DatabaseConstants.COLUMN_ZONE, grnPut.getZone().toUpperCase());
			values.put(DatabaseConstants.COLUMN_ISRACK, grnPut.getHasRack()?"T":"F");
			values.put(DatabaseConstants.COLUMN_PRODUCTCODE, grnPut.getProductCode().toUpperCase());
			values.put(DatabaseConstants.COLUMN_ROW_NO, grnPut.getRow().toUpperCase());
			values.put(DatabaseConstants.COLUMN_COLUMN_NO, grnPut.getColumn().toUpperCase());
			values.put(DatabaseConstants.COLUMN_TIER, grnPut.getTier().toUpperCase());
			values.put(DatabaseConstants.COLUMN_ACT_QTY, grnPut.getActQty());
			values.put(DatabaseConstants.COLUMN_ITEM_CODE, grnPut.getItemCode().toUpperCase());
			
			//values.put(DatabaseConstants.COLUMN_GRNPUT_ID, super.getRunningNo(DatabaseConstants.TABLE_WH_GRN_PUT, DatabaseConstants.COLUMN_GRNPUT_ID));
			
			dbGRN.insert(DatabaseConstants.TABLE_WH_GRN_PUT, null,
					values);	
		}
		catch(SQLiteException ex){ throw ex; }
		catch(Exception e){throw e;}
		return insertId;
	}
	public int insertGrnSerialNo(SQLiteDatabase dbGRN, GrnSerialNo grnSerialNo) throws Exception{
		int insertId =0;
		try{			
			grnSerialNo.setSerialID(getRunningNo(DatabaseConstants.TABLE_WH_GRN_SERIAL_NOS, DatabaseConstants.COLUMN_SERIAL_ID, grnSerialNo.getTransactionNo(), grnSerialNo.getItemCode().trim()));
			ContentValues values = new ContentValues();
			values.put(DatabaseConstants.COLUMN_TRX_NO, grnSerialNo.getTransactionNo());
			values.put(DatabaseConstants.COLUMN_SERIAL_ID, grnSerialNo.getSerialID());
			values.put(DatabaseConstants.COLUMN_ITEM_CODE, grnSerialNo.getItemCode().trim().toUpperCase());
			values.put(DatabaseConstants.COLUMN_SERIAL_No, grnSerialNo.getSerialNo().trim().toUpperCase());
			values.put(DatabaseConstants.COLUMN_DESC, grnSerialNo.getDescription().trim().toUpperCase());
			values.put(DatabaseConstants.COLUMN_PALLETID, grnSerialNo.getPalletId().trim().toUpperCase());
			
			insertId = (int) dbGRN.insert(DatabaseConstants.TABLE_WH_GRN_SERIAL_NOS, null,
					values);	
		}
		catch(SQLiteException ex){ throw ex; }
		catch(Exception e){
			throw e;
		}
		return insertId;
	}
	public int updateGrnSerialNo(SQLiteDatabase dbGRN, GrnSerialNo grnSerialNo) throws Exception{
		int updateId=0;	
		try{
			ContentValues values = new ContentValues();
			values.put(DatabaseConstants.COLUMN_SERIAL_No, grnSerialNo.getSerialNo().toUpperCase());
			values.put(DatabaseConstants.COLUMN_DESC, grnSerialNo.getDescription().toUpperCase());
			
			updateId = dbGRN.update(DatabaseConstants.TABLE_WH_GRN_SERIAL_NOS, values, 
										DatabaseConstants.COLUMN_TRX_NO + " = " + grnSerialNo.getTransactionNo() + " AND " +
										DatabaseConstants.COLUMN_SERIAL_ID + " = " + grnSerialNo.getSerialID() + " AND " +
										DatabaseConstants.COLUMN_ITEM_CODE + " = '" + grnSerialNo.getItemCode().trim()+ "'" + " AND " +
										DatabaseConstants.COLUMN_PALLETID + " = '" + grnSerialNo.getPalletId().trim()+ "'"
										, null);
		}
		catch(SQLiteException ex){ throw ex; }
		catch(Exception e){throw e;}
		return updateId;
	}

	
	public GrnHeader getGrnHeader(int trxNo) throws Exception{	
		GrnHeader newGrnHeader = new GrnHeader();
		try{	
			super.openDB();
			Cursor cursor = super.db.query(DatabaseConstants.TABLE_WH_GRN_HEADER,
					null, DatabaseConstants.COLUMN_TRX_NO + " = " + trxNo, null,
					null, null, null);
			if(cursor.getCount() >0){				
				cursor.moveToFirst();
				newGrnHeader = cursorToGrnHeader(cursor);	
				cursor.close();
				newGrnHeader.grnDetails = getAllGrnDetail(trxNo);
			}
		}
		catch(SQLiteException ex){ throw ex; }
		catch(Exception e){ throw e;}
		finally { super.closeDB(); }
		return newGrnHeader;
	}
	public GrnDetail getGrnDetail(int trxNo, int seqNo ) throws Exception{
		GrnDetail newGrnDetail = new GrnDetail();
		try{
			super.openDB();
			Cursor cursor = super.db.query(DatabaseConstants.TABLE_WH_GRN_DETAIL,
					null, DatabaseConstants.COLUMN_TRX_NO + " = " +trxNo+" AND "+
					DatabaseConstants.COLUMN_SEQNO + " = " + seqNo, null,
					null, null, null);
			
			if(cursor.getCount() >0){	
				cursor.moveToFirst();
				newGrnDetail = cursorToGrnDetail(cursor);
				cursor.close();
			}
		}
		catch(SQLiteException ex){ throw ex; }
		catch(Exception e){ throw e; }
		finally { super.closeDB(); }
		return newGrnDetail;
	}
	public GrnDetail getGrnDetail(String productCode, String batchNo, String lotNo) throws Exception{
		GrnDetail newGrnDetail = new GrnDetail();
		try{
			super.openDB();
			Cursor cursor = super.db.query(DatabaseConstants.TABLE_WH_GRN_DETAIL,
						null, DatabaseConstants.COLUMN_PRODUCTCODE + " = '" +productCode+"' AND "+
						DatabaseConstants.COLUMN_BATCH + " = '" + batchNo + "' AND "+
						DatabaseConstants.COLUMN_LOTNO + " = '" +lotNo	+"'"
						, null,null, null, null);
			
			if(cursor.getCount() >0){	
				cursor.moveToFirst();
				newGrnDetail = cursorToGrnDetail(cursor);
				cursor.close();
			}
		}
		catch(SQLiteException ex){ throw ex; }
		catch(Exception e){ throw e; }
		return newGrnDetail;
	}
	public ArrayList<GrnHeader> getAllGrnHeader() throws Exception {
		ArrayList<GrnHeader> grnHeaders = new ArrayList<GrnHeader>();
		try{
			super.openDB();
			Cursor cursor = super.db.query(DatabaseConstants.TABLE_WH_GRN_HEADER,
					null, null, null, null, null, null);
	
			if(cursor.getCount() > 0){
				cursor.moveToFirst();
			}
			while (!cursor.isAfterLast()) {
				GrnHeader grnHeader = cursorToGrnHeader(cursor);
				grnHeaders.add(grnHeader);
				cursor.moveToNext();
			}
			// Make sure to close the cursor
			cursor.close();
		}
		catch(SQLiteException ex){ throw ex; }
		catch(Exception e){ throw e; }
		finally {
			if(super.db.isOpen()){
				super.closeDB();				
			}
		}
		return grnHeaders;
	}
	public void deletegrnPut(SQLiteDatabase dbGRN, GrnPut grnPut, Boolean removeByPalletId) throws Exception {
		try{
			if(!removeByPalletId){
				dbGRN.delete(DatabaseConstants.TABLE_WH_GRN_PUT, 
						DatabaseConstants.COLUMN_TRX_NO + " = " + grnPut.getTransactionNo() 
						+ " AND " + DatabaseConstants.COLUMN_SEQNO + " = " + grnPut.getSeqNo() 
						+ " AND " + DatabaseConstants.COLUMN_PRODUCTCODE + " = '" + grnPut.getProductCode().toUpperCase().trim() + "'"
						+ " AND " + DatabaseConstants.COLUMN_ITEM_CODE + " = '" + grnPut.getItemCode().toUpperCase().trim() + "'"
						+ " AND " + DatabaseConstants.COLUMN_PALLETID + " = '" + grnPut.getPalletId().toUpperCase().trim() +"'", null);
				}
			else{
				dbGRN.delete(DatabaseConstants.TABLE_WH_GRN_PUT, 
						DatabaseConstants.COLUMN_TRX_NO + " = " + grnPut.getTransactionNo() 
						+ " AND " + DatabaseConstants.COLUMN_SEQNO + " = " + grnPut.getSeqNo() 
						+ " AND " + DatabaseConstants.COLUMN_PALLETID + " = '" + grnPut.getPalletId().toUpperCase()+ "'", null);
			}	
		}
		catch(SQLiteException ex){ throw ex; }
		catch(Exception e){ throw e; }		
	}
	public void deleteGrnSerialNoByPalletProduct(SQLiteDatabase dbGRN, int trxNo, String itemCode, String palletID) throws Exception{
		try{	
			dbGRN.delete(DatabaseConstants.TABLE_WH_GRN_SERIAL_NOS, 
				DatabaseConstants.COLUMN_TRX_NO + " = " + trxNo 
				+ " AND " + DatabaseConstants.COLUMN_ITEM_CODE + " = '" + itemCode.trim().toUpperCase() +"'"
				+ " AND " + DatabaseConstants.COLUMN_PALLETID + " = '" + palletID.trim().toUpperCase()+ "'", null);
		
		}
		catch(SQLiteException ex){ throw ex; }
		catch(Exception ex){ throw ex; }
	}
	public void deleteGrnSerialNoByPallet(SQLiteDatabase dbGRN, int trxNo, String palletID) throws Exception{
		try{	
			dbGRN.delete(DatabaseConstants.TABLE_WH_GRN_SERIAL_NOS, 
					DatabaseConstants.COLUMN_TRX_NO + " = " + trxNo
					+ " AND " +DatabaseConstants.COLUMN_PALLETID + " = '" + palletID.trim().toUpperCase()+ "'", null);
		
		}
		catch(SQLiteException ex){ throw ex; }
		catch(Exception ex){ throw ex; }
	}
	public ArrayList<GrnDetail> getAllGrnDetail(int trxNo) throws Exception {		
		ArrayList<GrnDetail> grnDetails = new ArrayList<GrnDetail>();
		try{
			super.openDB();
			Cursor cursor = super.db.query(DatabaseConstants.TABLE_WH_GRN_DETAIL,
					null, DatabaseConstants.COLUMN_TRX_NO + " = " + trxNo, null, null, null, DatabaseConstants.COLUMN_SEQNO);
	
			if(cursor.getCount() >0)
				cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				GrnDetail grnDetail = cursorToGrnDetail(cursor);
				grnDetails.add(grnDetail);
				cursor.moveToNext();
			}
			// Make sure to close the cursor
			cursor.close();
		}catch(Exception e){ throw e; }
		finally{ super.closeDB(); }
		return grnDetails;
	}
	public ArrayList<GrnPut> getGrnPutByGrnDetail(GrnDetail grnDetail) throws Exception {
		ArrayList<GrnPut> grnPuts = new ArrayList<GrnPut>();
		//String query = "Select distinct * from " + DatabaseConstants.TABLE_WH_GRN_PUT;
		try{
			super.openDB();
			Cursor cursor = super.db.query(DatabaseConstants.TABLE_WH_GRN_PUT,null, 
					DatabaseConstants.COLUMN_TRX_NO +" = " + grnDetail.getTransactionNo() + " AND " +
					DatabaseConstants.COLUMN_ITEM_CODE + " = '" + grnDetail.getItemCode().trim().toUpperCase() + "'", null, null, null, null);
			//Cursor cursor = dbGRN.rawQuery(query, null);
			if(cursor.getCount() >0){
				cursor.moveToFirst();
			}
			while (!cursor.isAfterLast()) {
				GrnPut grnPut = cursorToGrnPut(cursor);
				grnPuts.add(grnPut);
				cursor.moveToNext();
			}
			// Make sure to close the cursor
			cursor.close();
		}catch(Exception e){ throw e; }
		finally{ super.closeDB(); }
		return grnPuts;
	}
	public ArrayList<PalletProduct> getAllPalletProductByPalletId(int trxNo, String palletId) throws Exception {
		ArrayList<PalletProduct> palletProductList = new ArrayList<PalletProduct>();
		try{
			super.openDB();
			String query = "SELECT a." + DatabaseConstants.COLUMN_PRODUCTCODE +
							", b." + DatabaseConstants.COLUMN_PROD_DESCRIPTION +
							", b." + DatabaseConstants.COLUMN_BATCH +
							", b." + DatabaseConstants.COLUMN_LOTNO +
							", b." + DatabaseConstants.COLUMN_SERIALFLAG +
							", a." + DatabaseConstants.COLUMN_ITEM_CODE +
							", a." + DatabaseConstants.COLUMN_ACT_QTY + 
							" FROM " +DatabaseConstants.TABLE_WH_GRN_PUT + " a " +
							" INNER JOIN " + DatabaseConstants.TABLE_WH_GRN_DETAIL + " b " +
							" ON a." + DatabaseConstants.COLUMN_ITEM_CODE + " = b." + DatabaseConstants.COLUMN_ITEM_CODE +
							" AND a." + DatabaseConstants.COLUMN_TRX_NO + " = b." + DatabaseConstants.COLUMN_TRX_NO +
							" WHERE " + DatabaseConstants.COLUMN_PALLETID + " = '" + palletId +"'";
			//Cursor cursor = dbGRN.query(DatabaseConstants.TABLE_WH_GRN_PUT,
			//		null, DatabaseConstants.COLUMN_PALLETID + " = '" + palletId +"'", null, null, null, null);
			Cursor cursor = super.db.rawQuery(query, null);
			if(cursor.getCount() >0)
				cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				PalletProduct palletProduct = cursorToPalletProduct(cursor);
				palletProduct.grnSerialNos = getGrnPalletProductSerialNos(trxNo, palletProduct.getItemCode(), palletId);
				
				palletProductList.add(palletProduct);
				cursor.moveToNext();
			}
			// Make sure to close the cursor
			cursor.close();
		}catch(Exception e){throw e; }
		finally{ super.closeDB(); }
		return palletProductList;
	}
	public ArrayList<GrnPut> getAllGrnPut() throws Exception {
		ArrayList<GrnPut> grnPuts = new ArrayList<GrnPut>();
		try{
			super.openDB();
			Cursor cursor = super.db.query(DatabaseConstants.TABLE_WH_GRN_PUT,
					null, null, null, null, null, null);
	
			if(cursor.getCount() >0)
				cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				GrnPut grnPut = cursorToGrnPut(cursor);
				grnPuts.add(grnPut);
				cursor.moveToNext();
			}
			// Make sure to close the cursor
			cursor.close();
		}catch(Exception e){ throw e; }
		finally{ super.closeDB(); }
		return grnPuts;
	}
	public ArrayList<GrnPut> getGrnDetailPuts(int trxNo, int seqNo) throws Exception {
		ArrayList<GrnPut> grnPuts = new ArrayList<GrnPut>();
		try{
			super.openDB();
			Cursor cursor = super.db.query(DatabaseConstants.TABLE_WH_GRN_PUT, null,
					DatabaseConstants.COLUMN_TRX_NO +" = " + trxNo + " AND " +
					DatabaseConstants.COLUMN_SEQNO + " = " + seqNo, null, null, null, null);
	
			if(cursor.getCount() >0)
				cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				GrnPut grnPut = cursorToGrnPut(cursor);
				grnPuts.add(grnPut);
				cursor.moveToNext();
			}
			// Make sure to close the cursor
			cursor.close();
		}catch(Exception e){throw e; }
		finally{ super.closeDB(); }
		return grnPuts;
	}
	public ArrayList<GrnSerialNo> getGrnDetailSerialNos(GrnDetail grnDetail) throws Exception{
		ArrayList<GrnSerialNo> grnSerialNos = new ArrayList<GrnSerialNo>();
		try{
			if(!super.db.isOpen())
				super.openDB();
			Cursor cursor = super.db.query(DatabaseConstants.TABLE_WH_GRN_SERIAL_NOS,null, 
					DatabaseConstants.COLUMN_TRX_NO +" = " + grnDetail.getTransactionNo() + " AND " +
					DatabaseConstants.COLUMN_ITEM_CODE + " = '" + grnDetail.getItemCode()+ "'", null, null, null, null);
	
			if(cursor.getCount() > 0){
				cursor.moveToFirst();
			}
			while (!cursor.isAfterLast()) {
				GrnSerialNo grnSerialNo = cursorToGrnSerialNo(cursor);
				grnSerialNos.add(grnSerialNo);
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
		return grnSerialNos;
	}
	public ArrayList<GrnSerialNo> getGrnPalletProductSerialNos(int trxNo, String itemCode, String palletId) throws Exception{
		ArrayList<GrnSerialNo> grnSerialNos = new ArrayList<GrnSerialNo>();
		try{
			if(!super.db.isOpen())
				super.openDB();
			Cursor cursor = super.db.query(DatabaseConstants.TABLE_WH_GRN_SERIAL_NOS,null, 
					DatabaseConstants.COLUMN_TRX_NO +" = " + trxNo + " AND " +
					DatabaseConstants.COLUMN_PALLETID +" = '" + palletId+ "' AND " +
					DatabaseConstants.COLUMN_ITEM_CODE + " = '" + itemCode+ "'", null, null, null, null);
	
			if(cursor.getCount() > 0){
				cursor.moveToFirst();
			}
			while (!cursor.isAfterLast()) {
				GrnSerialNo grnSerialNo = cursorToGrnSerialNo(cursor);
				grnSerialNos.add(grnSerialNo);
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
		return grnSerialNos;
	}
	private PalletProduct cursorToPalletProduct(Cursor cursor){
		PalletProduct palletProduct = new PalletProduct();
		palletProduct.setProductCode(cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_PRODUCTCODE)));
		palletProduct.setProductDescription(cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_PROD_DESCRIPTION)));
		palletProduct.setBatchNo(cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_BATCH)));
		palletProduct.setLotNo(cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_LOTNO)));
		palletProduct.setItemCode(cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_ITEM_CODE)));
		
		if(cursor.getInt(cursor.getColumnIndex(DatabaseConstants.COLUMN_SERIALFLAG))==1)
			palletProduct.setHasSerialNo(true);
    	else
    		palletProduct.setHasSerialNo(false);

		palletProduct.setActQty(cursor.getInt(cursor.getColumnIndex(DatabaseConstants.COLUMN_ACT_QTY)));
		
		return palletProduct;
	}
	private GrnSerialNo cursorToGrnSerialNo(Cursor cursor){
		GrnSerialNo grnSerialNo = new GrnSerialNo();
		grnSerialNo.setTransactionNo(cursor.getInt(cursor.getColumnIndex(DatabaseConstants.COLUMN_TRX_NO)));	            	
		grnSerialNo.setSerialID(cursor.getInt(cursor.getColumnIndex(DatabaseConstants.COLUMN_SERIAL_ID)));
		grnSerialNo.setItemCode(cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_ITEM_CODE)));
		grnSerialNo.setSerialNo(cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_SERIAL_No)));  
		grnSerialNo.setDescription(cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_DESC)));	    
		grnSerialNo.setPalletId(cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_PALLETID)));	          	
		
		return grnSerialNo;
	}
	private GrnDetail cursorToGrnDetail(Cursor cursor) throws Exception{
		GrnDetail grnDetail = new GrnDetail();
		grnDetail.setTransactionNo(cursor.getInt(cursor.getColumnIndex(DatabaseConstants.COLUMN_TRX_NO)));
		grnDetail.setSeqNo(cursor.getInt(cursor.getColumnIndex(DatabaseConstants.COLUMN_SEQNO)));
		grnDetail.setProductCode(cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_PRODUCTCODE)));
		grnDetail.setProductDescription(cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_PROD_DESCRIPTION)));
		grnDetail.setBatchNo(cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_BATCH)));
		grnDetail.setLotNo(cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_LOTNO)));
		grnDetail.setUOM(cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_UOM)));
		grnDetail.setActQty(cursor.getInt(cursor.getColumnIndex(DatabaseConstants.COLUMN_ACT_QTY)));
		grnDetail.setServerQty(cursor.getInt(cursor.getColumnIndex(DatabaseConstants.COLUMN_SERVER_QTY)));
		grnDetail.setItemCode(cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_ITEM_CODE)));
		grnDetail.setRef_No1(cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_REF_NO1)));
		grnDetail.setRef_No2(cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_REF_NO2)));
		grnDetail.setRef_No3(cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_REF_NO3)));
		grnDetail.setRef_No4(cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_REF_NO4)));

		if(cursor.getInt(cursor.getColumnIndex(DatabaseConstants.COLUMN_SERIALFLAG))==1)
			grnDetail.setHasSerialNo(true);
    	else
    		grnDetail.setHasSerialNo(false);
		
		grnDetail.grnPuts = getGrnDetailPuts(cursor.getInt(cursor.getColumnIndex(DatabaseConstants.COLUMN_TRX_NO)),
							cursor.getInt(cursor.getColumnIndex(DatabaseConstants.COLUMN_SEQNO)));
		
		grnDetail.grnSerialNos = getGrnDetailSerialNos(grnDetail);
		return grnDetail;
	}
	@SuppressLint("SimpleDateFormat")
	private GrnHeader cursorToGrnHeader(Cursor cursor) throws Exception{
		GrnHeader grnHeader = new GrnHeader();
		grnHeader.setTransactionNo(cursor.getInt(cursor.getColumnIndex(DatabaseConstants.COLUMN_TRX_NO)));
		grnHeader.setWarehouseNo(cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_WAREHOUSE_NO)));
		grnHeader.setCustCode(cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_CUST_CODE)));
		grnHeader.setCustName(cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_CUST_NAME)));		
		//grnHeader.setGrnDate(new Date(cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_GRN_DATE))));
		//2014107 -gerry removed date. no need to upload back because there is no use at all		
		//String date = cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_GRN_DATE));
		//20140305 - gerry added to format date. Some OS versions hits error if date is not formatted by SimpleDateFormat
		//SimpleDateFormat formatter;
        //formatter = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
		//Date newDate = formatter.parse(date);
		//grnHeader.setGrnDate(newDate);
		
		int confirmGrnType = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.COLUMN_CONFIRM_GRNTYPE));
        if(confirmGrnType == 1){
            grnHeader.setConfirmGrnType(ConfirmGrnType.CONFIRM_GRN_AFTER_UPLOAD);          	
        }
        else if(confirmGrnType == 2){
            grnHeader.setConfirmGrnType(ConfirmGrnType.CONFIRM_GRN_BEFORE_UPLOAD);          	
        }
        else{
            grnHeader.setConfirmGrnType(ConfirmGrnType.NOT_USING_TABLET);          	
        }
		grnHeader.grnDetails = getAllGrnDetail(cursor.getInt(cursor.getColumnIndex(DatabaseConstants.COLUMN_TRX_NO)));
		return grnHeader;
	}
	private GrnPut cursorToGrnPut(Cursor cursor){
		GrnPut grnPut = new GrnPut();
		grnPut.setTransactionNo(cursor.getInt(cursor.getColumnIndex(DatabaseConstants.COLUMN_TRX_NO)));
		grnPut.setSeqNo(cursor.getInt(cursor.getColumnIndex(DatabaseConstants.COLUMN_SEQNO)));
		grnPut.setProductCode(cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_PRODUCTCODE)));
		grnPut.setPalleteId(cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_PALLETID)));
		grnPut.setZone(cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_ZONE)));
		if(cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_ISRACK)).toString().trim().equalsIgnoreCase("T")){
			grnPut.setHasRack(true);
		}
		else
			grnPut.setHasRack(false);
		grnPut.setRow(cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_ROW_NO)));
		grnPut.setColumn(cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_COLUMN_NO)));
		grnPut.setTier(cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_TIER)));
		grnPut.setActQty(cursor.getInt(cursor.getColumnIndex(DatabaseConstants.COLUMN_ACT_QTY)));
		grnPut.setItemCode(cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_ITEM_CODE)));
		return grnPut;
	}	
	public int updateGrnDetail(SQLiteDatabase dbGRN, GrnDetail grnDetail) throws Exception{
		int updateId=0;
		try{
			ContentValues values = new ContentValues();
			values.put(DatabaseConstants.COLUMN_ACT_QTY, grnDetail.getActQty());
			
			updateId = dbGRN.update(DatabaseConstants.TABLE_WH_GRN_DETAIL, values, 
										DatabaseConstants.COLUMN_TRX_NO + " = " + grnDetail.getTransactionNo() + " AND " +
										DatabaseConstants.COLUMN_SEQNO + " = " + grnDetail.getSeqNo(), null);
		}
		catch(Exception e){throw e;}
		return updateId;
	}
	public int updateGrnPut(SQLiteDatabase dbGRN,GrnPut grnPut) throws Exception{
		int updateId=0;	
		try{
			ContentValues values = new ContentValues();
			values.put(DatabaseConstants.COLUMN_ACT_QTY, grnPut.getActQty());
			
			updateId = dbGRN.update(DatabaseConstants.TABLE_WH_GRN_PUT, values, 
										DatabaseConstants.COLUMN_TRX_NO + " = " + grnPut.getTransactionNo() + " AND " +
										DatabaseConstants.COLUMN_SEQNO + " = " + grnPut.getSeqNo() + " AND " +
										DatabaseConstants.COLUMN_PALLETID + " = '" + grnPut.getPalletId() +"'" , null);
		}
		catch(Exception e){throw e;}	
		return updateId;
	}		
	public void clearGrnSerialNotTable(SQLiteDatabase dbGRN){
		dbGRN.delete(DatabaseConstants.TABLE_WH_GRN_SERIAL_NOS, null, null);
	}
	public void clearGrnPutTable(SQLiteDatabase dbGRN){
		dbGRN.delete(DatabaseConstants.TABLE_WH_GRN_PUT, null, null);
	}
	public void clearGrnDetailTable(SQLiteDatabase dbGRN){
		dbGRN.delete(DatabaseConstants.TABLE_WH_GRN_DETAIL, null, null);
	}
	public void clearGrnHeaderTable(SQLiteDatabase dbGRN){
		dbGRN.delete(DatabaseConstants.TABLE_WH_GRN_HEADER, null, null);
	}
	public void clearAllSQLiteData(SQLiteDatabase dbGRN) throws Exception{
		try{
			clearGrnSerialNotTable(dbGRN);
			clearGrnPutTable(dbGRN);
			clearGrnDetailTable(dbGRN);
			clearGrnHeaderTable(dbGRN);
		}
		catch(Exception e){ throw e; }
	}
	public Boolean isSerialNoExist(String prodCode, String newSerialNo) throws WhAppException, Exception {
		Boolean isExist = false;
		try{
			String query = "SELECT " + DatabaseConstants.COLUMN_SERIAL_No +
							" FROM " + DatabaseConstants.TABLE_WH_GRN_SERIAL_NOS +
							" WHERE " + DatabaseConstants.COLUMN_ITEM_CODE + " in (Select " + DatabaseConstants.COLUMN_ITEM_CODE + 
														" FROM " + DatabaseConstants.TABLE_WH_GRN_DETAIL +			
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
