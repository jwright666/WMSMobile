package sg.com.innosys.wms.DAL.Common;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import sg.com.innosys.wms.BLL.Common.LoginInfo;
import sg.com.innosys.wms.BLL.Common.WhAppException;

public class DbLogin extends DbHelper {
	
	public DbLogin(Context context){
		super(context);
	}
	
	public int insertLoginInfo(SQLiteDatabase dbLoginInfo, LoginInfo loginInfo) throws WhAppException, Exception{
		int insertId =0;
		try{
			ContentValues values = new ContentValues();
			values.put(DatabaseConstants.COLUMN_USERID, loginInfo.getUserId());
			values.put(DatabaseConstants.COLUMN_PASSWORD, loginInfo.getPassword());
			values.put(DatabaseConstants.COLUMN_COMPANYNAME, loginInfo.getCompanyName());
			values.put(DatabaseConstants.COLUMN_DATABASENAME, loginInfo.getDatabaseName());
			values.put(DatabaseConstants.COLUMN_SERVERNAME, loginInfo.getDbServerName());
			values.put(DatabaseConstants.COLUMN_WEBSERVICEIP, loginInfo.getWebServiceIP());
			values.put(DatabaseConstants.COLUMN_COMPANYDISPLAYNAME, loginInfo.getCompanyDisplayName());
			
			insertId = (int) dbLoginInfo.insert(DatabaseConstants.TABLE_LOGIN_INFO, null,
				values);			
		}catch(SQLiteException e){
			throw new WhAppException(e.toString());
		}
		catch(Exception e){
			throw e;
		}
		return insertId;	
	}
	public LoginInfo getLoginInfo() throws WhAppException, Exception {
		LoginInfo loginInfo = null;
		try{
			super.openDB();
			Cursor cursor = super.db.query(DatabaseConstants.TABLE_LOGIN_INFO, null, 
					null, null, null, null, null);
	
			if(cursor.getCount() > 0){
				cursor.moveToFirst();
			}
			while (!cursor.isAfterLast()) {
				loginInfo = cursorToLoginInfor(cursor);
				cursor.moveToNext();
			}
			// Make sure to close the cursor
			cursor.close();
		}catch(SQLiteException e){
			throw new WhAppException(e.toString());
		}
		catch(Exception e){
			throw e;
		}
		finally{
			super.closeDB();
		}
		return loginInfo;
	}
	private LoginInfo cursorToLoginInfor(Cursor cursor) throws Exception{
		LoginInfo loginInfo = new LoginInfo();
		loginInfo.setUserId(cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_USERID)));
		loginInfo.setPassword(cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_PASSWORD)));
		loginInfo.setCompanyName(cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_COMPANYNAME)));
		loginInfo.setDatabaseName(cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_DATABASENAME)));
		loginInfo.setDbServerName(cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_SERVERNAME)));
		loginInfo.setWebServiceIP(cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_WEBSERVICEIP)));
		loginInfo.setCompanyDisplayName(cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_COMPANYDISPLAYNAME)));
		
		return loginInfo;
	}

	public Boolean isValidLoginInfo(LoginInfo loginInfo) throws SQLiteException, Exception{
		boolean retValue = false;
		try{
			super.openDB();
			Cursor cursor = super.db.query(DatabaseConstants.TABLE_LOGIN_INFO, null, 
					DatabaseConstants.COLUMN_USERID + "='" + loginInfo.getUserId().trim() +"' AND "
					+ DatabaseConstants.COLUMN_PASSWORD + "='" + loginInfo.getPassword().trim()+"' AND "
					+ DatabaseConstants.COLUMN_COMPANYNAME + "='" + loginInfo.getCompanyName().trim()+"'", null, null, null, null);
	
			if(cursor.getCount() > 0){
				cursor.moveToFirst();
				if(loginInfo.getUserId().equalsIgnoreCase(cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_USERID)))
					&& loginInfo.getPassword().equalsIgnoreCase(cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_PASSWORD)))
					&& loginInfo.getCompanyName().equalsIgnoreCase(cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_COMPANYNAME)))
					){
					
					retValue = true;
				}
			}
			// Make sure to close the cursor
			cursor.close();
		}
		catch(SQLiteException e){
			throw e;
		}
		catch(Exception e){
			throw e;
		}
		finally{
			super.closeDB();
		}
		return retValue;
	}

	public void deleteLoginInfo(SQLiteDatabase dbLoginInfo) throws SQLiteException, Exception{
		try{	
			dbLoginInfo.delete(DatabaseConstants.TABLE_LOGIN_INFO, null, null);		
		}
		catch(SQLiteException ex){ throw ex; }
		catch(Exception ex){ throw ex; }
	}
}
