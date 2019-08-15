## ReadMe ##

--------------

### 功能说明 ###

     * 申请软著使用 统计Android代码 其它代码可以修改扩展
     * 1. 统计目录下的有效代码行数
     * 2. 删除文件中的注释和空行, 生成提交源代码  可处理的注释规则 行注释和块注释  【//  \/*  *\/】
     * 3. 存在的问题： 写在一行代码中间的注释，会增加一个换行, 可忽略，正常人不会这么写注释
     *      如:   return  \/*我是注///*//*/////释我是*\/  fileName.substring(fileName.lastIndexOf(".") + 1);
     *      处理完后变成如下:
     *              return
     *          fileName.substring(fileName.lastIndexOf(".") + 1);