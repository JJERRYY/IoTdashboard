package com.jerry.iotdashboard;

import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.hjq.toast.ToastUtils;
import com.jerry.iotdashboard.databinding.ActivityMainBinding;
import com.jerry.iotdashboard.mqtt.SmartMqtt;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
        ToastUtils.init(getApplication());


//        XXPermissions.with(this)
//                // 申请单个权限
//                .permission(Permission.ACCEPT_HANDOVER)
//                // 申请多个权限
//                //.permission(Permission.Group.CALENDAR)
//                // 申请安装包权限
//                //.permission(Permission.REQUEST_INSTALL_PACKAGES)
//                // 申请悬浮窗权限
//                //.permission(Permission.SYSTEM_ALERT_WINDOW)
//                // 申请通知栏权限
//                //.permission(Permission.NOTIFICATION_SERVICE)
//                // 申请系统设置权限
//                //.permission(Permission.WRITE_SETTINGS)
//                // 设置权限请求拦截器
//                //.interceptor(new PermissionInterceptor())
//                // 设置不触发错误检测机制
//                //.unchecked()
//                .request(new OnPermissionCallback() {
//
//                    @Override
//                    public void onGranted(List<String> permissions, boolean all) {
//                        if (all) {
//                            ToastUtils.show("获取录音和日历权限成功");
//                        } else {
//                            ToastUtils.show("获取部分权限成功，但部分权限未正常授予");
//                        }
//                    }
//
//                    @Override
//                    public void onDenied(List<String> permissions, boolean never) {
//                        if (never) {
//                            ToastUtils.show("被永久拒绝授权，请手动授予录音和日历权限");
//                            // 如果是被永久拒绝就跳转到应用权限系统设置页面
//                            XXPermissions.startPermissionActivity(MainActivity.this, permissions);
//                        } else {
//                            ToastUtils.show("获取录音和日历权限失败");
//                        }
//                    }
//                });
    }

}