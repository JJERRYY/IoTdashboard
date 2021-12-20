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

import com.google.gson.Gson;
import com.jerry.iotdashboard.R;
import com.jerry.iotdashboard.databinding.FragmentHomeBinding;
import com.jerry.iotdashboard.pojo.JsonRootBean;
import com.jerry.iotdashboard.utils.http;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.jerry.iotdashboard.AAChartCoreLib.AAChartCreator.AAChartModel;
import com.jerry.iotdashboard.AAChartCoreLib.AAChartCreator.AAChartView;
import com.jerry.iotdashboard.AAChartCoreLib.AAChartCreator.AAMoveOverEventMessageModel;
import com.jerry.iotdashboard.AAChartCoreLib.AAChartEnum.AAChartType;

public class HomeFragment extends Fragment implements AAChartView.AAChartViewCallBack{

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private View root;
    private AAChartModel aaChartModel;
    private AAChartView aaChartView;

    private http http;

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
        http = new http();
        setUpAAChartView();
        aaChartView = root.findViewById(R.id.chart_blood);
        aaChartModel = configureAAChartModel();
//        aaChartView.aa_drawChartWithChartModel(aaChartModel);

        return root;
    }

    void setUpAAChartView() {
//        aaChartView.callBack = this;
    }

    AAChartModel configureAAChartModel()  {
        String url="https://io.adafruit.com/api/v2/abao/feeds/blood-oxygen/data/chart?hours=48";
        String jsonData="";
        try {
            jsonData= http.get(url);
        } catch (IOException e) {
            e.printStackTrace();
        }catch (RuntimeException e){
            e.printStackTrace();

        }
        Gson gson =new Gson();
        JsonRootBean jdata=gson.fromJson(jsonData,JsonRootBean.class);//解析掉第一层
        List<List<Date>> datalist= jdata.getData();
        List<String> xAxis= new LinkedList<>();
        List<Integer> yAxis= new LinkedList<>();
//        for (List<Date> item : datalist) {
//            item;
//        }
        aaChartModel = new AAChartModel()
                .chartType(AAChartType.Line)
//                .title("THE HEAT OF PROGRAMMING LANGUAGE")
//                .subtitle("Virtual Data")
                .backgroundColor("#000000")
                .categories(new String[]{"blood oxygen"})
                .dataLabelsEnabled(false)
                .yAxisGridLineWidth(0f);
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