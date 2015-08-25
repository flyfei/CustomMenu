package com.tovi.custommenu;

import android.app.Activity;
import android.os.Bundle;

import cn.tovi.CustomMenu;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomMenu customMenu = new CustomMenu(this);
        setContentView(customMenu);
    }
}
