package com.jerry.iotdashboard.ui.notifications;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hjq.toast.ToastUtils;
import com.jerry.iotdashboard.Adapter.QuickAdapter;
import com.jerry.iotdashboard.Adapter.notificationAdapter;
import com.jerry.iotdashboard.MainActivity;
import com.jerry.iotdashboard.R;
import com.jerry.iotdashboard.databinding.FragmentDashboardBinding;
import com.jerry.iotdashboard.databinding.FragmentNotificationsBinding;
import com.jerry.iotdashboard.pojo.noticeBean;

import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private notificationAdapter notificationAdapter;

    private HashSet<noticeBean> noticeBeans;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
//        noticeBeans = ((MainActivity) getActivity()).getNoticeBeans();
//        List<noticeBean> noticeBeanList= new LinkedList<>(noticeBeans);

        mRecyclerView = root.findViewById(R.id.rcView_notification);
        mLayoutManager =  new LinearLayoutManager(getContext());
        notificationAdapter = new notificationAdapter();
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(notificationAdapter);

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        noticeBeans = ((MainActivity) getActivity()).getNoticeBeans();
        List<noticeBean> noticeBeanList= new LinkedList<>(noticeBeans);
        notificationAdapter.setNewInstance(noticeBeanList);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}