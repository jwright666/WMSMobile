package sg.com.innosys.wms.DAL.Common;

public class DatabaseConstants {

	public static final String DATABASE_NAME = "FM_80_WH.db";
	public static final int DATABASE_VERSION = 1;
	//constants for GRN header table
	public static final String TABLE_WH_GRN_HEADER = "WH_GRN_HEADER_TBL";
	//constants for GIN header table
	public static final String TABLE_WH_GIN_HEADER = "WH_GIN_HEADER_TBL";
	//This is shared in TABLE_WH_GRN_DETAIL,TABLE_WH_GRN_PUT,TABLE_WH_GRN_HEADER
	public static final String COLUMN_TRX_NO = "TransactionNO";
	
	public static final String COLUMN_WAREHOUSE_NO = "Wh_No";
	public static final String COLUMN_CUST_CODE = "CustCode";
	public static final String COLUMN_CUST_NAME = "CustName";
	public static final String COLUMN_GRN_DATE = "GrnDate";	
	//end for GRN header table constants
	
	//constants for GRN details table
	public static final String TABLE_WH_GRN_DETAIL = "WH_GRN_DETAIL_TBL";
	//constants for GIN details table
	public static final String TABLE_WH_GIN_DETAIL = "WH_GIN_DETAIL_TBL";	
	public static final String COLUMN_ITEM_CODE = "ItemCode";	
	public static final String COLUMN_SERIALFLAG = "SerialFlag";
	//This is shared in TABLE_WH_GRN_DETAIL,TABLE_WH_GRN_PUT
	public static final String COLUMN_SEQNO = "SequenceNo";	
	public static final String COLUMN_PRODUCTCODE = "ProductCode";
	
	public static final String COLUMN_PROD_DESCRIPTION = "Description";
	public static final String COLUMN_BATCH = "Batch";
	public static final String COLUMN_LOTNO = "LotNo";
	public static final String COLUMN_UOM = "UOM";
	public static final String COLUMN_ACT_QTY = "ActQty";
	public static final String COLUMN_SERVER_QTY = "ServerQty";
	public static final String COLUMN_REF_NO = "Ref_No"; //20140106 - gerry added
	public static final String COLUMN_REF_NO1 = "Ref_No1";
	public static final String COLUMN_REF_NO2 = "Ref_No2";
	public static final String COLUMN_REF_NO3 = "Ref_No3";
	public static final String COLUMN_REF_NO4 = "Ref_No4";
	//end for GRN details table constants

	public static final String COLUMN_CONFIRMED_GIN = "Confirmed_Gin";
	//constants for GIN Picking table
	public static final String TABLE_WH_GIN_PICK = "WH_GIN_PICK_TBL";
	public static final String COLUMN_ISNEW = "IsNew";
	//constants for GRN Putting table
	public static final String TABLE_WH_GRN_PUT = "WH_GRN_PUT_TBL";
	public static final String COLUMN_PALLETID = "PalletId";
	public static final String COLUMN_ZONE = "Zone";
	public static final String COLUMN_ISRACK = "IsRack";
	public static final String COLUMN_ROW_NO = "Row_No";
	public static final String COLUMN_COLUMN_NO = "Column_No";
	public static final String COLUMN_TIER = "Tier";

	public static final String TABLE_WH_GIN_SERIAL_NOS = "WH_GIN_SERIAL_NOS_TBL";
	public static final String COLUMN_DESC = "Description";
	public static final String COLUMN_SERIAL_ID = "Serial_Id";	
	public static final String COLUMN_SERIAL_No = "Serial_No";	
	
	public static final String TABLE_WH_GIN_CRATE = "WH_GIN_CRATE_TBL";
	public static final String COLUMN_CRATE_NO = "Crate_No";
	public static final String COLUMN_MARKING = "Crate_Marking";		
	
	public static final String TABLE_WH_GIN_CRATE_PRODUCT = "WH_GIN_CRATE_PRODUCT_TBL";	
	public static final String COLUMN_CRATEPROD_SEQNO = "CrateProd_SeqNo";		
	
	public static final String COLUMN_CONFIRM_GRNTYPE ="Comfirm_GrnType";
	
	public static final String TABLE_LOGIN_INFO = "WH_LOGIN_INFO_TBL";
	public static final String COLUMN_USERID = "User_Id";
	public static final String COLUMN_PASSWORD= "User_Password";	
	public static final String COLUMN_COMPANYNAME = "CompanyName";
	public static final String COLUMN_DATABASENAME= "DatabaseName";
	public static final String COLUMN_SERVERNAME = "ServerName";
	public static final String COLUMN_WEBSERVICEIP = "WebServiceIP";	
	public static final String COLUMN_COMPANYDISPLAYNAME = "Company_Display_Name";
	

	public static final String TABLE_WHMOBILE_SETTINGS = "WH_MOBILE_SETTINGS_TBL";
	public static final String COLUMN_SETTING_KEY ="SETTING_KEY";
	public static final String COLUMN_SETTING_VALUE ="SETTING_VALUE";
	
	//grnSerialNo at detail level
	//public static final String TABLE_WH_GRN_DETAIL_SERIAL_NOS = "WH_GRN_DETAIL_SERIAL_NOS_TBL";
	//grnSerialNo at putting level
	public static final String TABLE_WH_GRN_SERIAL_NOS = "WH_GRN_SERIAL_NOS_TBL";
	// Database table creation sql statement for grn header
	public static final String DATABASE_CREATE_GRN_HEADER_TBL = "create table "
			+ TABLE_WH_GRN_HEADER + "(" 
			+ COLUMN_TRX_NO + " integer primary key, " 
			+ COLUMN_WAREHOUSE_NO + " text, "
			+ COLUMN_CUST_CODE + " text, "
			+ COLUMN_CUST_NAME + " text, "
			+ COLUMN_CONFIRM_GRNTYPE + " integer, "
			+ COLUMN_GRN_DATE + " int, FOREIGN KEY("
			+ COLUMN_TRX_NO+ ") REFERENCES "
			+ TABLE_WH_GRN_DETAIL + "(" + COLUMN_TRX_NO + "));";
	// Database table creation sql statement for grn detail
	public static final String DATABASE_CREATE_GRN_DETAIL_TBL = "create table "
			+ TABLE_WH_GRN_DETAIL + "(" 
			+ COLUMN_TRX_NO + " integer, " 				
			+ COLUMN_SEQNO + " integer, " 
			+ COLUMN_PRODUCTCODE + " text, "
			+ COLUMN_PROD_DESCRIPTION + " text, "
			+ COLUMN_BATCH + " text, " 
			+ COLUMN_LOTNO + " text, "
			+ COLUMN_UOM + " text, "
			+ COLUMN_ACT_QTY + " integer, "
			+ COLUMN_SERVER_QTY + " integer, " 
			+ COLUMN_SERIALFLAG + " integer, "
			+ COLUMN_ITEM_CODE + " text, "
			+ COLUMN_REF_NO1 + " text, "
			+ COLUMN_REF_NO2 + " text, "
			+ COLUMN_REF_NO3 + " text, "
			+ COLUMN_REF_NO4 + " text, "
			+	" PRIMARY KEY("+ COLUMN_TRX_NO + "," + COLUMN_SEQNO + ","+ COLUMN_ITEM_CODE + ")"
			+	" FOREIGN KEY("+ COLUMN_TRX_NO + ") REFERENCES " + TABLE_WH_GRN_HEADER + "(" + COLUMN_TRX_NO + ")"
			+	" FOREIGN KEY("+ COLUMN_ITEM_CODE + ") REFERENCES " + TABLE_WH_GRN_PUT + "(" + COLUMN_ITEM_CODE + "));"; 
	// Database table creation sql statement for grn putting
	public static final String DATABASE_CREATE_GRN_PUT_TBL = "create table "
			+ TABLE_WH_GRN_PUT + "(" 
			+ COLUMN_TRX_NO + " integer, " 				
			+ COLUMN_SEQNO + " integer, " 			
			+ COLUMN_PRODUCTCODE + " text, "
			+ COLUMN_PALLETID + " text , "
			+ COLUMN_ZONE + " text, "
			+ COLUMN_ISRACK + " text, "
			+ COLUMN_ROW_NO + " text, "
			+ COLUMN_COLUMN_NO + " text, "
			+ COLUMN_TIER + " text, " 
			+ COLUMN_ACT_QTY + " integer, " 
			+ COLUMN_ITEM_CODE + " text, " 
			+	" PRIMARY KEY("+ COLUMN_TRX_NO + "," + COLUMN_SEQNO +  "," + COLUMN_PALLETID + ","+ COLUMN_ITEM_CODE +")"
			+	" FOREIGN KEY("+ COLUMN_TRX_NO + ","+ COLUMN_ITEM_CODE  +")"  
			+	" REFERENCES " + TABLE_WH_GRN_DETAIL + "(" + COLUMN_TRX_NO + ","+ COLUMN_ITEM_CODE + "));"; 
	
	public static final String DATABASE_CREATE_GRN_SERIAL_NO_TBL = "create table "
			+ TABLE_WH_GRN_SERIAL_NOS + "(" 
			+ COLUMN_TRX_NO + " integer, " 		
			+ COLUMN_PALLETID + " text , "		
			+ COLUMN_SERIAL_No + " text , " 	
			+ COLUMN_DESC + " text, "		
			+ COLUMN_SERIAL_ID + " integer , " 		
			+ COLUMN_ITEM_CODE + " text, "	
			+	" PRIMARY KEY("+ COLUMN_TRX_NO + "," + COLUMN_SERIAL_ID + "," + COLUMN_ITEM_CODE + "," + COLUMN_PALLETID + ")"
			+	" FOREIGN KEY("+ COLUMN_TRX_NO + "," + COLUMN_ITEM_CODE + "," + COLUMN_PALLETID + ")"
			+	" REFERENCES " + TABLE_WH_GRN_PUT + "(" + COLUMN_TRX_NO + "," + COLUMN_ITEM_CODE + "," + COLUMN_PALLETID +  "));"; 
	
	public static final String DELETE_ALL_DATA = " delete from " + TABLE_WH_GRN_PUT 
			+ " delete from " + TABLE_WH_GRN_DETAIL
			+ " delete from " + TABLE_WH_GRN_HEADER ;

	// Database table creation sql statement for gin header
	public static final String DATABASE_CREATE_GIN_HEADER_TBL = "create table "
			+ TABLE_WH_GIN_HEADER + "(" 
			+ COLUMN_TRX_NO + " integer primary key, " 
			+ COLUMN_REF_NO + " text, "  //20140106 - gerry added
			+ COLUMN_CUST_CODE + " text, "
			+ COLUMN_CUST_NAME + " text, "
			+ COLUMN_GRN_DATE + " int, " 
			+ COLUMN_CONFIRMED_GIN + " integer, "
			+  "FOREIGN KEY(" + COLUMN_TRX_NO+ ") " 
			+  "REFERENCES " +  TABLE_WH_GIN_DETAIL + "(" + COLUMN_TRX_NO + ")"			
			+  "FOREIGN KEY(" + COLUMN_TRX_NO+ ") " 
			+  "REFERENCES " +  TABLE_WH_GIN_CRATE_PRODUCT + "(" + COLUMN_TRX_NO + ")"	
			+  "FOREIGN KEY(" + COLUMN_TRX_NO+ ") " 
			+  "REFERENCES " +  TABLE_WH_GIN_CRATE + "(" + COLUMN_TRX_NO + "));";
	// Database table creation sql statement for gin detail
	public static final String DATABASE_CREATE_GIN_DETAIL_TBL = "create table "
			+ TABLE_WH_GIN_DETAIL + "(" 
			+ COLUMN_TRX_NO + " integer, " 				
			+ COLUMN_SEQNO + " integer, " 
			+ COLUMN_ITEM_CODE + " text, "
			+ COLUMN_WAREHOUSE_NO + " text, "
			+ COLUMN_PRODUCTCODE + " text, "
			+ COLUMN_PROD_DESCRIPTION + " text, "
			+ COLUMN_BATCH + " text, " 
			+ COLUMN_LOTNO + " text, "
			+ COLUMN_UOM + " text, "
			+ COLUMN_ACT_QTY + " integer, "
			+ COLUMN_SERVER_QTY + " integer, " 
			+ COLUMN_SERIALFLAG + " integer, "
			+	" PRIMARY KEY("+ COLUMN_TRX_NO + "," + COLUMN_SEQNO + "," + COLUMN_ITEM_CODE +")"
			+	" FOREIGN KEY("+ COLUMN_TRX_NO + ") REFERENCES " + TABLE_WH_GIN_HEADER + "(" + COLUMN_TRX_NO + ")"
			+	" FOREIGN KEY("+ COLUMN_ITEM_CODE + ") REFERENCES " + TABLE_WH_GIN_PICK + "(" + COLUMN_ITEM_CODE + "));"; 
	// Database table creation sql statement for gin picking
	public static final String DATABASE_CREATE_GIN_PICK_TBL = "create table "
			+ TABLE_WH_GIN_PICK + "(" 
			+ COLUMN_TRX_NO + " integer, " 				
			+ COLUMN_SEQNO + " integer, " 	
			+ COLUMN_ITEM_CODE + " text, "		
			+ COLUMN_PRODUCTCODE + " text, "
			+ COLUMN_PALLETID + " text , "
			+ COLUMN_ZONE + " text, "
			+ COLUMN_ISRACK + " text, "
			+ COLUMN_ROW_NO + " text, "
			+ COLUMN_COLUMN_NO + " text, "
			+ COLUMN_TIER + " text, " 
			+ COLUMN_ACT_QTY + " integer, " 
			+ COLUMN_SERVER_QTY + " integer, " 
			+ COLUMN_ISNEW + " integer, " 
			+	" PRIMARY KEY("+ COLUMN_TRX_NO + "," + COLUMN_SEQNO + "," + COLUMN_ITEM_CODE + ")"
			+	" FOREIGN KEY("+ COLUMN_TRX_NO + ","+ COLUMN_ITEM_CODE + ")"  
			+	" REFERENCES " + TABLE_WH_GIN_DETAIL + "(" + COLUMN_TRX_NO + ","+ COLUMN_ITEM_CODE + "));"; 
	// Database table creation sql statement for gin serial no
	public static final String DATABASE_CREATE_GIN_SERIAL_NO_TBL = "create table "
			+ TABLE_WH_GIN_SERIAL_NOS + "(" 
			+ COLUMN_TRX_NO + " integer, " 				
			+ COLUMN_SERIAL_ID + " integer , " 		
			+ COLUMN_ITEM_CODE + " text, "			
			+ COLUMN_SERIAL_No + " text , " 	
			+ COLUMN_DESC + " text, "
			+	" PRIMARY KEY("+ COLUMN_TRX_NO + "," + COLUMN_SERIAL_ID + "," + COLUMN_ITEM_CODE + ")"
			+	" FOREIGN KEY("+ COLUMN_TRX_NO + ","+ COLUMN_ITEM_CODE + ")"  
			+	" REFERENCES " + TABLE_WH_GIN_DETAIL + "(" + COLUMN_TRX_NO + ","+ COLUMN_ITEM_CODE + "));"; 
	// Database table creation sql statement for gin crate
	public static final String DATABASE_CREATE_GIN_CRATE_TBL = "create table "
			+ TABLE_WH_GIN_CRATE + "(" 
			+ COLUMN_TRX_NO + " integer, " 	
			+ COLUMN_CRATE_NO + " text, " 				
			+ COLUMN_DESC + " text, " 		
			+ COLUMN_MARKING + " text, "	
			+	" PRIMARY KEY("+ COLUMN_TRX_NO + "," + COLUMN_CRATE_NO + ")"
			+	" FOREIGN KEY("+ COLUMN_TRX_NO + ")"  
			+	" REFERENCES " + TABLE_WH_GIN_HEADER + "(" + COLUMN_TRX_NO + "));"; 
	// Database table creation sql statement for gin crate
	public static final String DATABASE_CREATE_GIN_CRATE_PRODUCT_TBL = "create table "
			+ TABLE_WH_GIN_CRATE_PRODUCT + "(" 
			+ COLUMN_TRX_NO + " integer, " 	
			+ COLUMN_CRATE_NO + " text, " 				
			+ COLUMN_ITEM_CODE + " text, " 		
			+ COLUMN_PRODUCTCODE + " text, "			
			+ COLUMN_UOM + " text, " 		
			+ COLUMN_ACT_QTY + " integer, "	
			+	" PRIMARY KEY("+ COLUMN_TRX_NO +"," + COLUMN_ITEM_CODE +"," + COLUMN_CRATE_NO + ")"
			+	" FOREIGN KEY("+ COLUMN_TRX_NO + ")"  
			+	" REFERENCES " + TABLE_WH_GIN_HEADER + "(" + COLUMN_TRX_NO + ")" 
			+	" FOREIGN KEY("+ COLUMN_ITEM_CODE + "," + COLUMN_PRODUCTCODE + ")"
			+	" REFERENCES " + TABLE_WH_GIN_DETAIL + "(" + COLUMN_ITEM_CODE + "," + COLUMN_PRODUCTCODE + "));"; 

	public static final String DATABASE_LOGIN_INFO_TBL = "create table "
			+ TABLE_LOGIN_INFO + "(" 
			+ COLUMN_USERID + " text, " 	
			+ COLUMN_PASSWORD + " text, " 				
			+ COLUMN_COMPANYNAME + " text, " 		
			+ COLUMN_DATABASENAME + " text, "			
			+ COLUMN_SERVERNAME + " text, " 		
			+ COLUMN_WEBSERVICEIP + " text, "	
			+ COLUMN_COMPANYDISPLAYNAME + " text, "	
			+	" PRIMARY KEY("+ COLUMN_USERID + "));"; 	
	
	public static final String DATABASE_WHMOBILE_SETTINGS_TBL = "create table "
			+ TABLE_WHMOBILE_SETTINGS + "(" 				
			+ COLUMN_SETTING_KEY + " text, " 		
			+ COLUMN_SETTING_VALUE + " text, "	
			+	" PRIMARY KEY("+ COLUMN_SETTING_KEY + "));"; 	
	
}
