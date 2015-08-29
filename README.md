CustomMenu is a custom control that can help you quickly create Menu

Features：

* Only the left menu
* Only the right menu
* Together with the right and left menu
* About menus do not, CustomMenu equivalent of a layout


[中文](https://github.com/flyfei/CustomMenu/blob/master/README_CN.md)


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



## Thanks

[designzway](http://freebies.designzway.com/)


## Copyright and Licensing


```
The MIT License (MIT)

Copyright © 2015 Tovi

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
```