package com.tovi.custommenu;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import cn.tovi.CustomMenu;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();

    CustomMenu customMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //设置内容全屏,虚拟按键可见
        Window window = getWindow();
        window.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);


        customMenu = new CustomMenu(this);

        //Setting Content Layout
//        ImageView contentView = new ImageView(this);
//        contentView.setBackgroundResource(R.drawable.main_view);
        customMenu.setContentView(R.layout.activity_main);

        customMenu.setRightShadow(R.drawable.middle_right);
        customMenu.setLeftShadow(R.drawable.middle_left);
        //Setting the left menu
        ImageView leftMenu = new ImageView(this);
        leftMenu.setBackgroundResource(R.drawable.left_view);
        customMenu.setLeftMenu(leftMenu);

        //Setting the right menu
        ImageView rightMenu = new ImageView(this);
        rightMenu.setBackgroundResource(R.drawable.left_view);
        customMenu.setRightMenu(rightMenu);

        setContentView(customMenu);
    }

    private void leftMenu() {
        if (customMenu.getState() == CustomMenu.State.CLOSE_MENU) {
            customMenu.openLeftMenuIfPossible();
        } else if (customMenu.getState() == CustomMenu.State.LEFT_MENU_OPENS) {
            customMenu.closeMenu();
        } else {
            Log.e(TAG, "CustomMenu State:" + customMenu.getState());
        }
    }

    private void rightMenu() {
        if (customMenu.getState() == CustomMenu.State.CLOSE_MENU) {
            customMenu.openRightMenuIfPossible();
        } else if (customMenu.getState() == CustomMenu.State.RIGHT_MENU_OPENS) {
            customMenu.closeMenu();
        } else {
            Log.e(TAG, "CustomMenu State:" + customMenu.getState());
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_left_menu:
                leftMenu();
                break;
            case R.id.btn_right_menu:
                rightMenu();
                break;
        }
    }
}
