CustomMenu is a custom control that can help you quickly create Menu

Features：

* Only the left menu
* Only the right menu
* Together with the right and left menu
* About menus do not, CustomMenu equivalent of a layout

## Legend


* Only the left menu

  ![](https://github.com/flyfei/CustomMenu/blob/master/resources/only_left_menu.gif)

* Only the right menu

 ![](https://github.com/flyfei/CustomMenu/blob/master/resources/only_right_menu.gif)

* Together with the right and left menu
 
  ![](https://github.com/flyfei/CustomMenu/blob/master/resources/double_menu.gif)

* About menus do not, CustomMenu equivalent of a layout

  ![](https://github.com/flyfei/CustomMenu/blob/master/resources/no_menu.gif)
  
## For Users

```
CustomMenu customMenu = new CustomMenu(this);

//Setting Content Layout
ImageView contentView = new ImageView(this);
contentView.setBackgroundResource(R.drawable.main_view);
customMenu.setContentView(contentView);

//Setting the left menu
ImageView leftMenu = new ImageView(this);
leftMenu.setBackgroundResource(R.drawable.left_view);
customMenu.setLeftMenu(leftMenu);

//Setting the right menu
ImageView rightMenu = new ImageView(this);
rightMenu.setBackgroundResource(R.drawable.left_view);
customMenu.setRightMenu(rightMenu);
```

## Feedback

If you have any questions,please emailto:zhaotengfei9@gmail.com
