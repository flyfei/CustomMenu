# CustomMenu

[V0.0.1](https://github.com/flyfei/CustomMenu/archive/V0.0.1.zip)

![CustomMenu Gif](https://github.com/flyfei/CustomMenu/blob/master/resources/custom_menu.gif)


SDK为21时，添加阴影效果

![CustomMenu Gif](https://github.com/flyfei/CustomMenu/blob/develop/resources/custom_menu2.gif)


* 仅使用左menu


* 仅使用右menu


* 使用左右menu


* 不使用menu



## 使用说明


```
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
```

## 发现问题

如果发现问题，请 emailto:zhaotengfei9@gamil.com