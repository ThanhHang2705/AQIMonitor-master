package com.example.thanhhang.mnsfimo.customdata;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * Created by ThanhHang on 2/19/2017.
 */

public class TimeAxisValueFormatter implements IAxisValueFormatter {
    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        int time = (int) value;
        return String.valueOf(time);
    }

   // @Override
    public int getDecimalDigits() {
        return 0;
    }
}
