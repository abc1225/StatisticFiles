package com.abc1225.statisticfile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * 申请软著使用 统计Android代码 其它代码可以修改扩展
 * 1. 统计目录下的代码有效行数
 * 2. 删除文件中的注释和空行  可处理的注释规则 行注释和块注释  【//  \/*  *\/】
 * 3. 存在的问题：
 *    A. 写在一行代码中间的注释，会增加一个换行, 可忽略，正常人不会这么写注释
 *      如:   return  \/*我是注///*//*/////释我是*\/  fileName.substring(fileName.lastIndexOf(".") + 1);
 *      处理完后变成如下:
 *              return
 *          fileName.substring(fileName.lastIndexOf(".") + 1);
 *    B. 代码中包含"/*"的字符串会导致栈溢出， 需要手动在removeComment() 函数中将其过滤掉
 */
public class StatisticFiles {

    public static ArrayList<String> mIncludeExt = new ArrayList<>();

    public static void main(String[] args) throws Exception {

        mIncludeExt.add(".java");
        //mIncludeExt.add(".txt");

        // 输入目录
        File inputFile = new File(".\\statisticfile\\src\\main");
        // 输出文件
        File outputFile = new File(".\\statisticfile\\statistic_files\\clean_files.txt");

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputFile));
        StringBuffer stringBuffer = new StringBuffer();

        System.out.println("InputDir is : "+inputFile.getAbsolutePath());

        // 读取文件内容
        readDir(inputFile, stringBuffer);
        System.out.println("Read file is over!");


        // 计算有效行数
        int line = countDirLines(inputFile);
        System.out.println("Total Line Number is : " + line);
//        try {
//            bufferedWriter.write("Read lines: "+line+"\n");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        write(stringBuffer.toString(), bufferedWriter);
    }
    // 遍历文件夹下所有文件
    private static void readDir(File dir, StringBuffer sb){
        File[] files = dir.listFiles();
        if (files!=null)
            for(File file:files){
                if(file.isDirectory()){
                    readDir(file, sb);
                }else {
                    if(file.length() !=0 ){
                        String suffix = "."+getFileSuffix(file.getName());
                        if(file.getName().endsWith(suffix) && mIncludeExt.contains(suffix)){
                            //System.out.println("read file:  "+file.getName()+"  file path: "+file.getPath()+" suffix: "+getFileSuffix(file.getName()));
                            sb.append(readFileToString(file));
                        }
                    }
                }
            }
    }

    //读取文件里面的内容
    // 1. 将整个文件读到字符串中, 读取一行添加换行符
    // 2. 替换所有注释内容为"\n"
    // 3. 遍历所有行，去除空行
    private static String readFileToString(File file){
        BufferedReader bufferedReader = null;
        StringBuilder builder = new StringBuilder();
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
            String line = null;
            // 读取每一行添加换行符 将整个文件读到字符串里
            StringBuilder builder1 = new StringBuilder();
            while((line = bufferedReader.readLine())!=null){
                String s = line.trim();
                if(s.length() == 0 || s.startsWith("//") || s.startsWith("Log.")){
                    continue;
                }
                builder1.append(line).append("\n");
            }
            String fileText = builder1.toString();
            if(fileText.length() > 0){
                fileText = removeComment(fileText);
            }

            // 替换所有注释内容为\n
            String[] lines = fileText.split("\n");

            // 遍历所有行，去除空行
            for (String s:lines) {
                String temp = s.trim();
                if(temp.length() == 0){
                    continue;
                }
                builder.append(s).append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                if(bufferedReader!=null){
                    bufferedReader.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return builder.toString();
    }

    //将读取的路径以及相应的内容写入指定的文件
    private static void write(String str, Writer writer){
        try {
            writer.write(str);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                if(writer!=null)
                    writer.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    // 遍历目录下所有文件,读取该目录的有效行数
    private static int countDirLines(File dir){
        File[] files = dir.listFiles();
        int line = 0;
        if (files != null)
            for(File file:files){
                if(file.isDirectory()){
                    line += countDirLines(file);
                }else {
                    if(file.length()!=0){
                        String suffix = "."+getFileSuffix(file.getName());
                        if(file.getName().endsWith(suffix) && mIncludeExt.contains(suffix)){
                            //System.out.println("count file line:  "+file.getName()+"  file path: "+file.getPath()+" suffix: "+getFileSuffix(file.getName()));
                            line += readFileLines(file);
                        }
                    }
                }
            }
        return line;
    }

    // 读取文件的有效行数
    private static int readFileLines(File file){

        BufferedReader bufferedReader = null;
        int lineCount = 0;
        StringBuilder builder = new StringBuilder();
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
            String line = null;
            // 读取每一行添加换行符 将整个文件读到字符串里
            StringBuilder builder1 = new StringBuilder();
            while((line = bufferedReader.readLine())!=null){
                String s = line.trim();
                if(s.length() == 0 || s.startsWith("//") || s.startsWith("Log.")){
                    continue;
                }
                builder1.append(line).append("\n");
            }
            String fileText = builder1.toString();
            if(fileText.length() > 0){
                fileText = removeComment(fileText);
            }

            // 替换所有注释内容为\n
            String[] lines = fileText.split("\n");

            // 遍历所有行，去除空行
            for (String s:lines) {
                String temp = s.trim();
                if(temp.length() == 0){
                    continue;
                }
                lineCount++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                if(bufferedReader!=null){
                    bufferedReader.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return lineCount;
    }

    // 将所有注释移除掉,替换成换行符
    // 过滤掉 image/* Android中获取系统图片
    private static String removeComment(String text){
        return text.replaceAll("(?<!:)\\/\\/.*|(?<!image)\\/\\*(\\s|.)*?\\*\\/", "\n");
    }

    private static String getFileSuffix(String fileName){
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

}