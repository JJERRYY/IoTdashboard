package com.jerry.iotdashboard.Adapter;

import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.jerry.iotdashboard.R;
import com.jerry.iotdashboard.pojo.dataBean;
import com.jerry.iotdashboard.pojo.dbBean;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.util.List;

public class dashboardAdapter extends BaseQuickAdapter<dbBean, BaseViewHolder> {
    static String TAG = "dashboardAdapter.Class";
    public dashboardAdapter() {
        super(R.layout.item_dashboard);
    }
    private DateTimeFormatter parser= ISODateTimeFormat.dateTimeNoMillis();


    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, dbBean dbBean) {
        Log.d(TAG, "convert: start"+dbBean.getName());
        ((TextView) baseViewHolder.getView(R.id.item_name)).setText(dbBean.getName());
        SwitchMaterial Switch= baseViewHolder.getView(R.id.item_switch);
        MaterialButton mButton = baseViewHolder.getView(R.id.item_btn);
        ((TextView) baseViewHolder.getView(R.id.item_value)).setText(dbBean.getValue());
        ((TextView) baseViewHolder.getView(R.id.item_lastUpdate)).setText(dbBean.getLastUpdate());

        //1为只显示按钮：蜂鸣器
        //2为只显示开关：灯、步进电机
        //3为啥都不显示：普通传感器
        switch (dbBean.getType()){
            case 1:
                Switch.setVisibility(View.INVISIBLE);
                mButton.setVisibility(View.VISIBLE);
                mButton.setBackgroundColor(getContext().getResources().getColor(R.color.yellow));
                mButton.setText("蜂鸣");
                break;
            case 2:
                Switch.setVisibility(View.VISIBLE);
                mButton.setVisibility(View.INVISIBLE);
                break;
            case 3:
                Switch.setVisibility(View.INVISIBLE);
                mButton.setVisibility(View.INVISIBLE);
                break;
        }
//        Switch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int position = getItemPosition(dbBean);
//                onClickListener.onButtonClicked(view, position,,ateEdit.getText().toString());
//                baseViewHolder.getAdapterPosition();
//            }
//        });
    }


}
