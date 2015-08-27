# CustomMenu


* 仅使用左menu

  ![](https://github.com/flyfei/CustomMenu/blob/master/resources/only_left_menu.gif)

* 仅使用右menu

  ![](https://github.com/flyfei/CustomMenu/blob/master/resources/only_right_menu.gif)

* 使用左右menu

  ![](https://github.com/flyfei/CustomMenu/blob/master/resources/double_menu.gif)

* 不使用menu

  ![](https://github.com/flyfei/CustomMenu/blob/master/resources/no_menu.gif)


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

如果发现问题，请 emailto:zhaotengfei9@gmail.com
