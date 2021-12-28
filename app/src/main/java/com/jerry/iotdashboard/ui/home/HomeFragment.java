package com.jerry.iotdashboard.ui.home;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ejlchina.okhttps.HTTP;
import com.ejlchina.okhttps.HttpResult;
import com.ejlchina.okhttps.OnCallback;
import com.google.gson.Gson;
import com.hjq.toast.ToastUtils;
import com.jerry.iotdashboard.AAChartCoreLib.AAChartCreator.AASeriesElement;
import com.jerry.iotdashboard.Adapter.QuickAdapter;
import com.jerry.iotdashboard.R;
import com.jerry.iotdashboard.databinding.FragmentHomeBinding;
import com.jerry.iotdashboard.pojo.dataBean;

import java.io.IOException;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

import com.jerry.iotdashboard.AAChartCoreLib.AAChartCreator.AAChartModel;
import com.jerry.iotdashboard.AAChartCoreLib.AAChartCreator.AAChartView;
import com.jerry.iotdashboard.AAChartCoreLib.AAChartCreator.AAMoveOverEventMessageModel;
import com.jerry.iotdashboard.AAChartCoreLib.AAChartEnum.AAChartType;
import com.jerry.iotdashboard.url;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

public class HomeFragment extends Fragment implements AAChartView.AAChartViewCallBack{

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private View root;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private QuickAdapter mQuickAdapter;
    private AAChartModel aaChartModel;
    private AAChartView aaChartView;

    private int receiveFlag =0;

    private dataBean jdata;
    private List<dataBean> dataBeans= new LinkedList<>();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        mRecyclerView = root.findViewById(R.id.rcView);
        mLayoutManager =  new LinearLayoutManager(getContext());
        mQuickAdapter = new QuickAdapter();
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mQuickAdapter);
        requestUrls();
//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
//        String url="https://io.adafruit.com/api/v2/abao/feeds/blood-oxygen/data/chart?hours=48";
//        OkHttps.async(url)
//                .setOnResponse(new OnCallback<HttpResult>() {
//                    @Override
//                    public void on(HttpResult data) {
//                         String dataBody =data.getBody().cache().toString();
//                         Gson gson = new Gson();
//                         jdata = gson.fromJson(dataBody, dataBean.class);
//
////                        try {
////                            aaChartModel = configureAAChartModel();
////                            ToastUtils.show("model完成");
////                        } catch (ParseException e) {
////                            e.printStackTrace();
////                        }
//
//                    }
//                })
//                .get();

//        setUpAAChartView();
//        while(aaChartModel==null){
//            ToastUtils.show("等待异步结果");
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }

//        if(aaChartModel!=null) {
//            aaChartView.aa_drawChartWithChartModel(aaChartModel);
//            ToastUtils.show("加载统计图");
//        }
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    void requestUrls(){
    HTTP http = HTTP.builder().callbackExecutor((Runnable run) -> {
        new Handler(Looper.getMainLooper()).post(run); // 在主线程执行
         })
            .build();

        for(String url: url.chartURLs){
            http.async(url)
                    .setOnResponse(new OnCallback<HttpResult>() {
                        @Override
                        public void on(HttpResult data) {
                            receiveFlag++;
                            String dataBody =data.getBody().toString();
                            Gson gson = new Gson();
                            jdata = gson.fromJson(dataBody, dataBean.class);
                            ToastUtils.show(jdata.getFeed().getName()+"Received");
                            dataBeans.add(jdata);
//                        try {
//                            aaChartModel = configureAAChartModel();
//                            ToastUtils.show("model完成");
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }

                            if(receiveFlag==5){
                                mQuickAdapter.setNewInstance(dataBeans);
                                ToastUtils.show("适配器数据压入");
                            }
                        }
                    })
                    .setOnException((IOException e) -> {
                        e.printStackTrace();
                        // 异常回调
                    })
                    .get();
        }

    }

    void setUpAAChartView() {
        aaChartView = root.findViewById(R.id.item_chart);
        aaChartView.callBack = this;
    }

    AAChartModel configureAAChartModel() throws ParseException {

        List<List<String>> datalist= jdata.getData();
        List<String> xAxis= new LinkedList<>();
        List<Float> yAxis= new LinkedList<>();
        DateTimeFormatter parser = ISODateTimeFormat.dateTimeNoMillis();
        for (List<String> item : datalist) {
            if(item.get(1).equals("-999.0")) continue;
            DateTime datetime = parser.parseDateTime(item.get(0));
            xAxis.add(datetime.toString("yyyy年MM月dd日HH时mm分ss秒"));
            yAxis.add(Float.parseFloat(item.get(1)));
        }
        aaChartModel = new AAChartModel()
                .chartType(AAChartType.Line)
//                .title("THE HEAT OF PROGRAMMING LANGUAGE")
//                .subtitle("Virtual Data")
                .backgroundColor("#000000")
                .categories(xAxis.toArray(new String[xAxis.size()]))
                .dataLabelsEnabled(true)
                .yAxisGridLineWidth(0f)
        .series(new AASeriesElement[]{
                new AASeriesElement().data(yAxis.toArray())
        });
        return aaChartModel;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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