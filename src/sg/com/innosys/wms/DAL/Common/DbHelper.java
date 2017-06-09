package sg.com.innosys.wms.DAL.Common;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper{
	// Database fields
	public SQLiteDatabase db;
	
	public DbHelper(Context context) {
		super(context, DatabaseConstants.DATABASE_NAME, null, DatabaseConstants.DATABASE_VERSION);
		
	} 	
	//this will open the SQLiteDatabase
	public void openDB(){
		db = getWritableDatabase();		
	}
	//this will close the SQLiteDatabase
	public void closeDB(){
		db.close();
	}
	//this will begin the transaction with in the SQLiteDatabase	
	public void beginTransaction(){
		this.openDB();
		db.beginTransaction();
	}
	//not used yet
	public void rollbackTransaction(){
		//do roll back transaction not sure if supported in SQLite
	}
	//this is equivalent to commit transaction for SQL,
	public void commitTransaction(){
		db.setTransactionSuccessful();
	}
	//this is equivalent to commit transaction for SQL,
	//provide that there is no error in the whole transaction
	//auto rollback the transaction when error encountered
	public void endTransaction(){
		db.endTransaction();		
	}
	/*
	protected int getRunningSeqNo(String tableName, int trxNo, String itemCode) throws Exception{
		int maxId = 0;
		try{
			String query = "select IFNULL(MAX(SequenceNo), 0) MAX_ID from %s ";
					
			query = String.format(query, columnName, tableName);
			
			Cursor cursor = db.rawQuery(query, null);
			if(cursor.getCount() >0)
				cursor.moveToFirst();
			while (!cursor.isAfterLast()) {				
				maxId=cursor.getInt(cursor.getColumnIndex("MAX_ID"));
				cursor.moveToNext();
			}
			cursor.close();
		}
		catch(Exception e){throw e;}
		return maxId+1;
	}
	*/
	protected int getRunningNo(String tableName, String columnnName, int trxNo, String itemCode) throws Exception{
		int maxId = 0;
		try{
			String query = "select IFNULL(MAX(%s), 0) MAX_ID from %s where TransactionNO = %d and ItemCode = '%s' ";
					
			query = String.format(query,columnnName, tableName, trxNo, itemCode);
			
			Cursor cursor = db.rawQuery(query, null);
			if(cursor.getCount() >0)
				cursor.moveToFirst();
			while (!cursor.isAfterLast()) {				
				maxId=cursor.getInt(cursor.getColumnIndex("MAX_ID"));
				cursor.moveToNext();
			}
			cursor.close();
		}
		catch(Exception e){throw e;}
		return maxId+1;
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		try
		{	
			db.execSQL(DatabaseConstants.DATABASE_LOGIN_INFO_TBL);
			db.execSQL(DatabaseConstants.DATABASE_CREATE_GRN_HEADER_TBL);
			db.execSQL(DatabaseConstants.DATABASE_CREATE_GRN_DETAIL_TBL);	
			db.execSQL(DatabaseConstants.DATABASE_CREATE_GRN_PUT_TBL);	
			//db.execSQL(DatabaseConstants.DATABASE_CREATE_GRN_DETAIL_SERIAL_NO_TBL);	
			db.execSQL(DatabaseConstants.DATABASE_CREATE_GRN_SERIAL_NO_TBL);	
			db.execSQL(DatabaseConstants.DATABASE_CREATE_GIN_HEADER_TBL);
			db.execSQL(DatabaseConstants.DATABASE_CREATE_GIN_DETAIL_TBL);	
			db.execSQL(DatabaseConstants.DATABASE_CREATE_GIN_PICK_TBL);	
			db.execSQL(DatabaseConstants.DATABASE_CREATE_GIN_SERIAL_NO_TBL);	
			db.execSQL(DatabaseConstants.DATABASE_CREATE_GIN_CRATE_TBL);	
			db.execSQL(DatabaseConstants.DATABASE_CREATE_GIN_CRATE_PRODUCT_TBL);
			db.execSQL(DatabaseConstants.DATABASE_WHMOBILE_SETTINGS_TBL);
		}
		catch(SQLException ex){
			ex.printStackTrace();
			throw ex;
		}		
		catch(Exception ex){
			ex.printStackTrace();
		}		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
}
