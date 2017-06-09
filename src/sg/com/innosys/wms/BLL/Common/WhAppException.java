package sg.com.innosys.wms.BLL.Common;

@SuppressWarnings("serial")
public class WhAppException extends Exception{	
	private String exception;
	
	public WhAppException(String exception){
		super(exception);
		this.exception = exception;		
	}
	
	public String getString(){
		return this.exception;
	}
	

}
