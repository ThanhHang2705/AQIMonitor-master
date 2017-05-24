package fimo.uet.fairapp.customdata;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.DecimalFormat;

import fimo.uet.fairapp.R;

/**
 * Created by ThanhHang on 2/19/2017.
 */
public class XYMarkerView extends MarkerView {

    private TextView tvContent;
    private IAxisValueFormatter xAxisValueFormatter;
    private String type;
    private DecimalFormat format;

    public XYMarkerView(Context context, IAxisValueFormatter xAxisValueFormatter, String type) {
        super(context, R.layout.custom_marker_view);

        this.xAxisValueFormatter = xAxisValueFormatter;
        this.type=type;
        tvContent = (TextView) findViewById(R.id.tvContent);
//        format = new DecimalFormat("###.0");
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        tvContent.setText("Time: " + xAxisValueFormatter.getFormattedValue(e.getX(), null) + " , "+type+": " + (int)e.getY());

        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}

