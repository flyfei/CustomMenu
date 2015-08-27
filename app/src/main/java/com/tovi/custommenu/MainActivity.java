package com.tovi.custommenu;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import cn.tovi.CustomMenu;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //设置内容全屏,虚拟按键可见
        Window window = getWindow();
        window.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);


        CustomMenu customMenu = new CustomMenu(this);

        //设置中间布局
        ImageView contentView = new ImageView(this);
        contentView.setBackgroundResource(R.drawable.main_view);
        customMenu.setContentView(contentView);

        //设置左菜单
        ImageView leftMenu = new ImageView(this);
        leftMenu.setBackgroundResource(R.drawable.left_view);
        customMenu.setLeftMenu(leftMenu);

        //设置右菜单
        ImageView rightMenu = new ImageView(this);
        rightMenu.setBackgroundResource(R.drawable.left_view);
        customMenu.setRightMenu(rightMenu);

        setContentView(customMenu);
    }
}
