package fimo.uet.fairapp.customdata;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DecimalFormat;

/**
 * Created by ThanhHang on 2/19/2017.
 */

public class MyAxisValueFormatter implements IAxisValueFormatter
{

    private DecimalFormat mFormat;

    public MyAxisValueFormatter() {
            mFormat = new DecimalFormat("###,###,###,##0");
        }
    public int getDecimalDigits() {
        return 1;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return mFormat.format(value); //+ " $";
    }
}
