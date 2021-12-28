package com.jerry.iotdashboard.ui.dashboard;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.ejlchina.okhttps.HTTP;
import com.ejlchina.okhttps.HttpResult;
import com.ejlchina.okhttps.OnCallback;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.gson.Gson;
import com.hjq.toast.ToastUtils;
import com.jerry.iotdashboard.Adapter.dashboardAdapter;
import com.jerry.iotdashboard.R;
import com.jerry.iotdashboard.databinding.FragmentDashboardBinding;
import com.jerry.iotdashboard.mqtt.IMqttCallBack;
import com.jerry.iotdashboard.mqtt.SmartMqtt;
import com.jerry.iotdashboard.mqtt.mqttConfig;
import com.jerry.iotdashboard.pojo.dataBean;
import com.jerry.iotdashboard.pojo.dbBean;
import com.jerry.iotdashboard.url;
import com.lodz.android.hermes.contract.Hermes;
import com.lodz.android.hermes.contract.OnConnectListener;
import com.lodz.android.hermes.contract.OnSendListener;
import com.lodz.android.hermes.contract.OnSubscribeListener;
import com.lodz.android.hermes.modules.HermesAgent;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttOutputStream;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

public class DashboardFragment extends Fragment implements IMqttCallBack {
    private static String TAG="DashboardFragment";
    private FragmentDashboardBinding binding;
    private View root;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private dashboardAdapter mDashboardAdapter;

    private int receiveFlag =0;

    private SmartMqtt mqtt;
    private Hermes hermes;

    private dataBean jdata;
    private List<dataBean> dataBeans= new LinkedList<>();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        startMqtt();

        mRecyclerView = root.findViewById(R.id.rcView_dashboard);
        mLayoutManager =  new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
//        mLayoutManager = new LinearLayoutManager(getContext());
        mDashboardAdapter = new dashboardAdapter();

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mDashboardAdapter);
        mDashboardAdapter.addChildClickViewIds(R.id.item_btn,R.id.item_switch);



        mDashboardAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                if(view.getId()==R.id.item_btn){
//                    mqtt = SmartMqtt.getInstance();
//                    mqtt.init(getContext());
//                    mqtt.connect(mqttConfig.server,mqttConfig.clientId+System.nanoTime());
                    mqtt.sendData("3".getBytes(),mqttConfig.topic);
//                        hermes.connect();
//                        hermes.sendTopic(mqttConfig.topic,"3");
                }else{
                    SwitchMaterial switchMaterial = (SwitchMaterial) view;
                    View parentView= (View) view.getParent();
                    TextView textView = parentView.findViewById(R.id.item_name);
                    String name = textView.getText().toString();
                    if(name.equals("翻身器")){
                        if(switchMaterial.isChecked()){
                            mqtt.sendData("4".getBytes(StandardCharsets.UTF_8),mqttConfig.topic);
                            mqtt.sendData("4".getBytes(StandardCharsets.UTF_8),mqttConfig.topic);
                        }else{
                            mqtt.sendData("5".getBytes(StandardCharsets.UTF_8),mqttConfig.topic);
                            mqtt.sendData("5".getBytes(StandardCharsets.UTF_8),mqttConfig.topic);
                        }

                    }else{//灯
                        if(switchMaterial.isChecked()){
                            mqtt.sendData("1".getBytes(StandardCharsets.UTF_8),mqttConfig.topic);
//                            mqtt.sendData("".getBytes(StandardCharsets.UTF_8),mqttConfig.topic);
                        }else{
                            mqtt.sendData("2".getBytes(StandardCharsets.UTF_8),mqttConfig.topic);
//                            mqtt.sendData("5".getBytes(StandardCharsets.UTF_8),mqttConfig.topic);
                        }
                    }
                }
            }
        });

        requestUrls();
        return root;
    }

    void startMqtt(){
        mqtt = SmartMqtt.getInstance();
        mqtt.init(getContext());
        mqtt.connect(mqttConfig.server,mqttConfig.clientId+System.nanoTime());
//        mqtt.subscribe(mqttConfig.topic, 1);
    }

    void requestUrls(){
        HTTP http = HTTP.builder().callbackExecutor((Runnable run) -> {
            new Handler(Looper.getMainLooper()).post(run); // 在主线程执行
        })
                .build();

        for(String url: url.dbUrls){
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

                            if(receiveFlag== com.jerry.iotdashboard.url.dbUrls.length){
                                mDashboardAdapter.setNewInstance(convert2dbBean(dataBeans));
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
    private List<dbBean> convert2dbBean(List<dataBean> dataBeans){
         DateTimeFormatter parser= ISODateTimeFormat.dateTimeNoMillis();
        List<dbBean> dbBeans = new LinkedList<>();
        for(dataBean item:dataBeans){
            String name =item.getFeed().getName();
            String value =item.getLastData().get(1);
            DateTime datetime = parser.parseDateTime(item.getLastData().get(0));
            String lastUpdate =datetime.toString();
            Log.d(TAG, "convert2dbBean: "+name);
            switch(name){
                case "controller":
                    dbBean light= new dbBean()
                            .setName("灯泡")
                            .setType(2);
                    dbBean stepper= new dbBean()
                            .setName("翻身器")
                            .setType(2);
                    dbBean alarmer = new dbBean()
                            .setName("警报器")
                            .setType(1);
                    dbBeans.add(light);
                    dbBeans.add(stepper);
                    dbBeans.add(alarmer);
                    break;
                default:
                    dbBeans.add(new dbBean()
                            .setName(name)
                            .setType(3)
                            .setValue(value).setLastUpdate(lastUpdate));
            }

        }
        Log.d(TAG, "convert2dbBean: "+dbBeans.size());
        return dbBeans;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onActionSuccess(int action, IMqttToken asyncActionToken) {
        ToastUtils.show("action success"+action);
    }

    @Override
    public void onActionFailure(int action, IMqttToken asyncActionToken, Throwable exception) {
        ToastUtils.show("action failed"+action);
        exception.printStackTrace();
    }

    @Override
    public void onActionFailure(int action, Exception e) {
        e.printStackTrace();
    }

    @Override
    public void connectionLost(Throwable cause) {
        cause.printStackTrace();
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        ToastUtils.show("message arrived"+message.toString());
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        ToastUtils.show("success");
    }
}