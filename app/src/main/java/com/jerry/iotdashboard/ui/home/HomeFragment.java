package com.jerry.iotdashboard.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.ejlchina.okhttps.HttpResult;
import com.ejlchina.okhttps.OkHttps;
import com.ejlchina.okhttps.OnCallback;
import com.google.gson.Gson;
import com.jerry.iotdashboard.R;
import com.jerry.iotdashboard.databinding.FragmentHomeBinding;
import com.jerry.iotdashboard.pojo.JsonRootBean;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.jerry.iotdashboard.AAChartCoreLib.AAChartCreator.AAChartModel;
import com.jerry.iotdashboard.AAChartCoreLib.AAChartCreator.AAChartView;
import com.jerry.iotdashboard.AAChartCoreLib.AAChartCreator.AAMoveOverEventMessageModel;
import com.jerry.iotdashboard.AAChartCoreLib.AAChartEnum.AAChartType;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

public class HomeFragment extends Fragment implements AAChartView.AAChartViewCallBack{

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private View root;
    private AAChartModel aaChartModel;
    private AAChartView aaChartView;

    private JsonRootBean jdata;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        root = binding.getRoot();

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        String url="https://io.adafruit.com/api/v2/abao/feeds/blood-oxygen/data/chart?hours=48";
        OkHttps.async(url)
                .setOnResponse(new OnCallback<HttpResult>() {
                    @Override
                    public void on(HttpResult data) {
                         String dataBody =data.getBody().cache().toString();
                         Gson gson = new Gson();
                         jdata = gson.fromJson(dataBody,JsonRootBean.class);

                        try {
                            aaChartModel = configureAAChartModel();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        aaChartView.aa_drawChartWithChartModel(aaChartModel);
                    }
                })
                .get();

        setUpAAChartView();

        return root;
    }

    void setUpAAChartView() {
        aaChartView = root.findViewById(R.id.chart_blood);
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
                .dataLabelsEnabled(false)
                .yAxisGridLineWidth(0f)
        .series(yAxis.toArray(new Float[0]));
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