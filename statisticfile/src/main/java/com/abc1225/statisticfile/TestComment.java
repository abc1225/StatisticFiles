package com.abc1225.statisticfile;

import java.util.ArrayList;

public class TestComment {

    public static ArrayList<String> mIncludeExt = new ArrayList<>();


/*    展开其他人还搜

    android

    github

    eclipse

    html5
    展开相关软件

    linux

    intellij idea

    ubuntu

     myeclipse
    相关程序设计语言

     php

    python

    c++

****    c语言
换一换 搜索热点
1麦莉友人指责锤弟新	748万
2醉驾男威胁民警	689万
3杨紫李现合体大片	674万
4清华教师妻子遇害	672万
5陈情令演唱会	668万
6女孩硬扛无德司机	654万
7葛优低调出席酒宴	647万
8闫妮与男友分手	637万
9人民网四问迪士尼	635万
10胡歌真的困了	632万*/

    // 将所有注释移除掉,替换成换行符
    private static String removeComment(String text){       /*我是注释我是注释我是注释我是注释我是注释我是注释我是注释我是注释*/
        return text.replaceAll("(?<!:)\\/\\/.*|\\/\\*(\\s|.)*?\\*\\/", "\n");
    }

    private static String getFileSuffix(String fileName){    ////////*我是注释我是注释我是注释我是注释我是注释我是注释我是注释我是注释*/
        return  /*我是注///*//*/////释我是*/ fileName.substring(fileName.lastIndexOf(".") + 1);////////*我是注释我
    }


}
