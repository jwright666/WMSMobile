package sg.com.innosys.wms.DAL.Common;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import sg.com.innosys.wms.BLL.Common.WhAppException;
import sg.com.innosys.wms.BLL.Common.WhMobileSettings;

@SuppressLint("DefaultLocale")
public class DbWhMobileSettings extends DbHelper{

	public DbWhMobileSettings(Context context) {
		super(context);
	}
	
	public Boolean doestWhMobileSettingsExist(){
		//TODO check if exist
		return false;
	}
	@SuppressLint("DefaultLocale")
	public int addWhMobileSettings(SQLiteDatabase dbWhMobileSettings, WhMobileSettings whMobileSettings) throws WhAppException, Exception{
		int insertId =0;
		try{
			Field[] fields = whMobileSettings.getClass().getDeclaredFields();
			Method[] methods = whMobileSettings.getClass().getDeclaredMethods();
			for (Field field : fields) {
				String name =  field.getName();
				Object value = "";//(String) field.get(whMobileSettings);				
				for(Method method : methods){
					String methodName = method.getName().toString();
					if(!methodName.toUpperCase().contains(("get"+name).toUpperCase())){
						continue;
					}
					else{
						ContentValues values = new ContentValues();
						value = method.invoke(whMobileSettings, null);
						values.put(DatabaseConstants.COLUMN_SETTING_KEY,  name.toUpperCase());
						values.put(DatabaseConstants.COLUMN_SETTING_VALUE,  (String)value);
						insertId = (int) dbWhMobileSettings.insert(DatabaseConstants.TABLE_WHMOBILE_SETTINGS, null,
								values);
						break;
					}
				}
			}		
						
		}catch(SQLiteException e){
			throw new WhAppException(e.toString());
		}
		catch(Exception e){
			throw e;
		}
		return insertId;	
	}
	public WhMobileSettings getWhMobileSettings() throws WhAppException, Exception {
		WhMobileSettings whMobileSettings = new WhMobileSettings();
		try{
			super.openDB();
			Cursor cursor = super.db.query(DatabaseConstants.TABLE_WHMOBILE_SETTINGS, null, 
					null, null, null, null, null);
	
			if(cursor.getCount() > 0){
				cursor.moveToFirst();
			}
			while (!cursor.isAfterLast()) {
				whMobileSettings = cursorToWhMobileSettings(cursor, whMobileSettings);
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
		return whMobileSettings;
	}
	@SuppressLint("DefaultLocale")
	private WhMobileSettings cursorToWhMobileSettings(Cursor cursor, WhMobileSettings whMobileSettings) throws Exception{
		String key_ID =  cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_SETTING_KEY));		
		Method[] methods = whMobileSettings.getClass().getDeclaredMethods();
		Object value = "";//(String) field.get(whMobileSettings);				
		for(Method method : methods){
			String methodName = method.getName().toString();
			if(methodName.toUpperCase().contains(("set"+key_ID).toUpperCase())){					
				value = cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_SETTING_VALUE));
				method.invoke(whMobileSettings, value.toString());
				break;
			}
		}	
		return whMobileSettings;
	}

	
	public void updateWhMobileSettings(SQLiteDatabase dbWhMobileSettings, WhMobileSettings whMobileSettings) throws SQLiteException, Exception{
		try{
			Field[] fields = whMobileSettings.getClass().getDeclaredFields();
			Method[] methods = whMobileSettings.getClass().getDeclaredMethods();
			for (Field field : fields) {
				String name =  field.getName();
				Object value = "";//(String) field.get(whMobileSettings);				
				for(Method method : methods){
					String methodName = method.getName().toString();
					if(methodName.toUpperCase().contains(("get"+name).toUpperCase())){						
						ContentValues values = new ContentValues();
						value = method.invoke(whMobileSettings, null);
						values.put(DatabaseConstants.COLUMN_SETTING_VALUE,  (String)value);
						dbWhMobileSettings.update(DatabaseConstants.TABLE_WHMOBILE_SETTINGS, values, 
								DatabaseConstants.COLUMN_SETTING_KEY + " = '" +name.toUpperCase() +"'", null);	
						break;
					}
				}
			}			
		}
		catch(SQLiteException ex){ throw ex; }
		catch(Exception ex){ throw ex; }
	}

}
