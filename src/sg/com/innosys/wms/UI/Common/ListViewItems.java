package sg.com.innosys.wms.UI.Common;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

public class ListViewItems  extends TextView {
    private boolean isHeader = false;
    private Paint linePaint;

    public ListViewItems(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public ListViewItems(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ListViewItems(Context context) {
        super(context);
        init();
    }

    public void init(){
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(Color.parseColor("#000000"));
    }
    
    public boolean isHeader() {
        return isHeader;
    }

    public void setHeader(boolean isHeader) {
        this.isHeader = isHeader;
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        ///* now cannot be used as header because it will also disappear when scrolled
        if(isHeader){
            canvas.drawColor(Color.parseColor("#AAFFFF99"));
        }
        //*/
        
        if(!this.getText().toString().equalsIgnoreCase("")){
	        canvas.drawLine(0, 0, getMeasuredWidth(), 0,linePaint);
	        canvas.drawLine(0, getMeasuredHeight(), getMeasuredWidth(), getMeasuredHeight(),linePaint);
	        canvas.drawLine(0,0, 0, getMeasuredHeight(),linePaint);
        }
    }
}
