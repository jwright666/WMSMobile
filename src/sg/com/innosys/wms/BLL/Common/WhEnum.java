package sg.com.innosys.wms.BLL.Common;

public class WhEnum {
	public enum ConfirmGrnType{
		   NOT_USING_TABLET(0), 
		   CONFIRM_GRN_AFTER_UPLOAD(1),
		   CONFIRM_GRN_BEFORE_UPLOAD(2);
		  
		   private int value;
	       ConfirmGrnType(int value){
	         this.value = value;
	       }
	       public int getIntValue() {
	           return value;
	       }	
	};
	public enum ScanNavigationOption{
		   COMPLETE_PICKING_ALL_ITEMS_BEFORE_SERIALNO(0), 
		   SERIALNO_EACH_PICKING(1);
		  
		   private int value;
		   ScanNavigationOption(int value){
	         this.value = value;
	       }
	       public int getIntValue() {
	           return value;
	       }	
	};

	public enum GINPickSortOption{
		   SORT_BY_LOCATION(0), 
		   SORT_BY_ITEMCODE(1);
		  
		   private int value;
		   GINPickSortOption(int value){
	         this.value = value;
	       }
	       public int getIntValue() {
	           return value;
	       }	
	};
}
