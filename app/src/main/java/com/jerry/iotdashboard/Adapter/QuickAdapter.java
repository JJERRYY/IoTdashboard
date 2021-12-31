package com.jerry.iotdashboard.Adapter;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.google.gson.Gson;
import com.hjq.toast.ToastUtils;
import com.jerry.iotdashboard.AAChartCoreLib.AAChartCreator.AAMoveOverEventMessageModel;
import com.jerry.iotdashboard.AAChartCoreLib.AAChartCreator.AASeriesElement;
import com.jerry.iotdashboard.AAChartCoreLib.AAChartEnum.AAChartAnimationType;
import com.jerry.iotdashboard.AAChartCoreLib.AAChartEnum.AAChartSymbolStyleType;
import com.jerry.iotdashboard.AAChartCoreLib.AAChartEnum.AAChartType;
import com.jerry.iotdashboard.AAChartCoreLib.AATools.AAColor;
import com.jerry.iotdashboard.AAChartCoreLib.AATools.AAGradientColor;
import com.jerry.iotdashboard.AAChartCoreLib.AATools.AALinearGradientDirection;
import com.jerry.iotdashboard.MainActivity;
import com.jerry.iotdashboard.R;
import com.jerry.iotdashboard.mqtt.SmartMqtt;
import com.jerry.iotdashboard.mqtt.mqttConfig;
import com.jerry.iotdashboard.pojo.dataBean;
import com.jerry.iotdashboard.AAChartCoreLib.AAChartCreator.AAChartModel;
import com.jerry.iotdashboard.AAChartCoreLib.AAChartCreator.AAChartView;
import com.jerry.iotdashboard.pojo.noticeBean;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.text.ParseException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class QuickAdapter extends BaseQuickAdapter<dataBean, BaseViewHolder> implements AAChartView.AAChartViewCallBack{
    private AAChartModel aaChartModel;
    private AAChartView aaChartView;
    static String TAG = "QuickAdapter.class";
    private MainActivity activityContext;
    private HashSet<noticeBean> noticeBeans;
    private SmartMqtt mqtt;
    public QuickAdapter() {
        super(R.layout.item);
    }
    public QuickAdapter(Context activityContext){
        super(R.layout.item);
        this.activityContext=(MainActivity) activityContext;

    }



    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, dataBean dataBean) {
        noticeBeans= activityContext.getNoticeBeans();
        ((TextView) baseViewHolder.getView(R.id.item_name)).setText(dataBean.getFeed().getName());
        aaChartView =((AAChartView)baseViewHolder.getView(R.id.item_chart));
        aaChartView.callBack=this;
        Log.i(TAG,"convert"+baseViewHolder.getItemId());

        try {
            aaChartModel = configureAAChartModel(dataBean.getData(),dataBean.getFeed().getName());
            Log.i(TAG,"初始化图表"+dataBean.getFeed().getName());

        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(aaChartModel!=null) {
            aaChartView.aa_drawChartWithChartModel(aaChartModel);
            ToastUtils.show("加载"+dataBean.getFeed().getName()+"统计图");
        }

    }


    AAChartModel configureAAChartModel(List<List<String>> datalist,String dataName) throws ParseException {
        Map linearGradientColor = AAGradientColor.linearGradient(
                AALinearGradientDirection.ToBottom,
                "rgba(255, 239, 153,1)",//
                "rgba(255, 239, 153,1)"//
        );
//        List<List<String>> datalist= jdata.getData();
        List<String> xAxis= new LinkedList<>();
        List<Float> yAxis= new LinkedList<>();
        DateTimeFormatter parser = ISODateTimeFormat.dateTimeNoMillis();
        for (List<String> item : datalist) {
            if(item.get(1).equals("-999.0")) break;
            Float value= Float.parseFloat(item.get(1));
            DateTime datetime = parser.parseDateTime(item.get(0));
            xAxis.add(datetime.toString("yyyy年MM月dd日HH时mm分ss秒"));
            yAxis.add(value);

        }
        aaChartModel = new AAChartModel()
                .chartType(AAChartType.Areaspline)
//                .title("THE HEAT OF PROGRAMMING LANGUAGE")
//                .subtitle("Virtual Data")
                .backgroundColor("#ffffff")
                .categories(xAxis.toArray(new String[xAxis.size()]))
                .dataLabelsEnabled(true)
                .yAxisGridLineWidth(0f)
                .legendEnabled(false)
                .xAxisLabelsEnabled(false)
                .markerSymbolStyle(AAChartSymbolStyleType.BorderBlank)//设置折线连接点样式为:边缘白色
                .animationType(AAChartAnimationType.EaseOutCubic)
                .animationDuration(1200)
                .series(new AASeriesElement[]{
                        new AASeriesElement()
                                .name(dataName)
                                .fillColor(linearGradientColor)
                                .color(AAColor.rgbaColor(255, 215, 0, 1f))
                                .data(yAxis.toArray())
                });
        return aaChartModel;
    }



    @Override
    public void chartViewDidFinishLoad(AAChartView aaChartView) {
        System.out.println("🔥🔥🔥🔥🔥图表加载完成回调方法!!!!!!!! ");

    }

    @Override
    public void chartViewMoveOverEventMessage(AAChartView aaChartView, AAMoveOverEventMessageModel messageModel) {
        Gson gson = new Gson();
        System.out.println("👌👌👌👌👌move over event message " + gson.toJson(messageModel));
    }
}
