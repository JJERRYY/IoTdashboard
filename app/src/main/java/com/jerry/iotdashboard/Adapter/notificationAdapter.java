package com.jerry.iotdashboard.Adapter;

import android.widget.TextView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jerry.iotdashboard.R;
import com.jerry.iotdashboard.pojo.noticeBean;

import java.util.List;

public class notificationAdapter extends BaseQuickAdapter<noticeBean,BaseViewHolder> {


    public notificationAdapter() {
        super(R.layout.item_notice);
    }

    public notificationAdapter(List<noticeBean> noticeBeanList) {
        super(R.layout.item_notice,noticeBeanList);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, noticeBean noticeBean) {
        TextView time =  baseViewHolder.getView(R.id.item_time);
        TextView state = baseViewHolder.getView(R.id.item_state);

        String stateText = noticeBean.getType()+noticeBean.getValue()+noticeBean.getAction();

        time.setText(noticeBean.getTime());
        state.setText(stateText);
    }
}
