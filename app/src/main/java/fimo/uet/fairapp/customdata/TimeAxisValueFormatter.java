package fimo.uet.fairapp.customdata;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ThanhHang on 2/19/2017.
 */

public class TimeAxisValueFormatter implements IAxisValueFormatter {
    private String[] mValues;

    public TimeAxisValueFormatter() {
        mValues = new String[24];
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
        Date Time = null;
        try {
            Time = df.parse(df.format(c.getTime()));
            Calendar cal = Calendar.getInstance();
            cal.setTime(Time);
            int current_hours = cal.get(Calendar.HOUR_OF_DAY);
            for (int i=23;i>=0;i--){
//                if ((current_hours+i+1)<=12){
//                    mValues[i] = String.valueOf(current_hours+i+1)+"AM";
//                } else if((current_hours+i+1)<24 && (current_hours+i+1)>12){
//                    mValues[i] = String.valueOf(current_hours+i-12+1)+"PM";
//                }else if ((current_hours+i+1)<36 && (current_hours+i)>=24){
//                    mValues[i]= String.valueOf(current_hours+i-24+1)+"AM";
//                }else if((current_hours+i+1)>=36){
//                    mValues[i]= String.valueOf(current_hours+i-36+1)+"PM";
//                }
                if(current_hours>=i){
                    mValues[23-i] = String.valueOf(current_hours-i);
                }else{
                    mValues[23-i] = String.valueOf(23-(i-current_hours));
                }
            }
        } catch (ParseException e1) {
            e1.printStackTrace();
        }

    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        // "value" represents the position of the label on the axis (x or y)
        int a = (int) value-1;
        String b = mValues[(int) value-1];
        return mValues[(int) value-1];
    }

    /** this is only needed if numbers are returned, else return 0 */
    public String[] getmValues() {
        return mValues;
    }
}
