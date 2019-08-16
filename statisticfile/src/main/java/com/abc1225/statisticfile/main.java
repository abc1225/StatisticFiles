package com.abc1225.statisticfile;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class main
{

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        // TODO Auto-generated method stub
        String test1 = "printf(\"Hello /* a comment /* a comment inside comment */ inside /* another comment inside comment */ string */ world\")";
        String test2 = "//*no recursion*/* file header */";
        String test3 = "//*no recursion*/* file header***********/************* Sample input program **********/**************/int spawn_workers(int worker_count) {/* The block below is supposed to spawn 100 workers.     But it creates many more.     Commented until I figure out why  for (int i = 0; i < worker_count; ++i) {    if(!fork()) {      /* This is the worker. Start working. */      do_work();    }  }  */  return 0; /* successfully spawned 100 workers */}int main() {printf(\"Hello /*a comment inside string*/ world\");  int worker_count = 0/*octal number*/144;  if (spawn_workers(worker_count) != 0) {    exit(-1);  }  return 0;}";

        String test = ReadFileToString(".\\statisticfile\\src\\main\\java\\com\\abc1225\\statisticfile\\TestComment.java");
//		System.out.println(removeComments(test));
//		System.out.println(removeCommentsWithQuote(test));
        System.out.println(removeCommentsWithQuoteAndDoubleEscape(test));
    }
    /**/

    /**
     * 简单的直接去掉星号斜杠注释段
     * @param code
     * @return
     */
    public static String removeComments(String code)
    {
        StringBuilder sb = new StringBuilder();
        int cnt = 0;
        for (int i = 0; i < code.length(); i++)
        {
            if(cnt == 0)
            {
                if(i+1 < code.length() && code.charAt(i) == '/' && code.charAt(i+1) == '*')
                {
                    cnt++;
                    i++;
                    continue;
                }
            }
            else
            {
                if(i+1 < code.length() && code.charAt(i) == '*' && code.charAt(i+1) == '/')
                {
                    cnt--;
                    i++;
                    continue;
                }
                if(i+1 < code.length() && code.charAt(i) == '/' && code.charAt(i+1) == '*')
                {
                    cnt++;
                    i++;
                    continue;
                }
            }
            if(cnt == 0)
            {
                sb.append(code.charAt(i));
            }
        }
        return sb.toString();
    }

    /**
     * 处理带双引号的注释
     * @param code
     * @return
     */
    public static String removeCommentsWithQuote(String code)
    {
        StringBuilder sb = new StringBuilder();
        int cnt = 0;
        boolean quoteFlag = false;
        for (int i = 0; i < code.length(); i++)
        {
            //如果没有开始双引号范围
            if(!quoteFlag)
            {
                //如果发现双引号开始
                if(code.charAt(i) == '\"')
                {
                    sb.append(code.charAt(i));
                    quoteFlag = true;
                    continue;
                }
                //不在双引号范围内
                else
                {
                    //处理/**/注释段
                    if(cnt == 0)
                    {
                        if(i+1 < code.length() && code.charAt(i) == '/' && code.charAt(i+1) == '*')
                        {
                            cnt++;
                            i++;
                            continue;
                        }
                    }
                    else
                    {
                        if(i+1 < code.length() && code.charAt(i) == '*' && code.charAt(i+1) == '/')
                        {
                            cnt--;
                            i++;
                            continue;
                        }
                        if(i+1 < code.length() && code.charAt(i) == '/' && code.charAt(i+1) == '*')
                        {
                            cnt++;
                            i++;
                            continue;
                        }
                    }
                    //如果没有发现/**/段或者已经处理完了嵌套的/**/
                    if(cnt == 0)
                    {
                        sb.append(code.charAt(i));
                        continue;
                    }
                }
            }
            //处理双引号段
            else
            {
                //如果发现双引号结束(非转移形式的双引号)
                if(code.charAt(i) == '\"' && code.charAt(i-1) != '\\')
                {
                    sb.append(code.charAt(i));
                    quoteFlag = false;
                }
                //双引号开始了但是还没有结束
                else
                {
                    sb.append(code.charAt(i));
                }
            }

        }
        return sb.toString();
    }

    /**
     * 处理双引号和双斜杠注释
     * @param code
     * @return
     */
    public static String removeCommentsWithQuoteAndDoubleEscape(String code)
    {
        StringBuilder sb = new StringBuilder();
        int cnt = 0;
        boolean quoteFlag = false;
        for (int i = 0; i < code.length(); i++)
        {
            //如果没有开始双引号范围
            if(!quoteFlag)
            {
                //如果发现双引号开始
                if(code.charAt(i) == '\"')
                {
                    sb.append(code.charAt(i));
                    quoteFlag = true;
                    continue;
                }
                //处理双斜杠注释
                else if(i+1 < code.length() && code.charAt(i) == '/' && code.charAt(i+1) == '/')
                {
                    while(code.charAt(i) != '\n')
                    {
                        i++;
                    }
                    continue;
                }
                //不在双引号范围内
                else
                {
                    //处理/**/注释段
                    if(cnt == 0)
                    {
                        if(i+1 < code.length() && code.charAt(i) == '/' && code.charAt(i+1) == '*')
                        {
                            cnt++;
                            i++;
                            continue;
                        }
                    }
                    else
                    {
                        //发现"*/"结尾
                        if(i+1 < code.length() && code.charAt(i) == '*' && code.charAt(i+1) == '/')
                        {
                            cnt--;
                            i++;
                            continue;
                        }
                        //发现"/*"嵌套
                        if(i+1 < code.length() && code.charAt(i) == '/' && code.charAt(i+1) == '*')
                        {
                            cnt++;
                            i++;
                            continue;
                        }
                    }
                    //如果没有发现/**/注释段或者已经处理完了嵌套的/**/注释段
                    if(cnt == 0)
                    {
                        sb.append(code.charAt(i));
                        continue;
                    }
                }
            }
            //处理双引号注释段
            else
            {
                //如果发现双引号结束(非转义形式的双引号)
                if(code.charAt(i) == '\"' && code.charAt(i-1) != '\\')
                {
                    sb.append(code.charAt(i));
                    quoteFlag = false;
                }
                //双引号开始了但是还没有结束
                else
                {
                    sb.append(code.charAt(i));
                }
            }
        }
        return sb.toString();
    }

    /**
     * 从一个文件读入到String
     * @param FilePath
     * @return
     */
    public static String ReadFileToString(String FilePath)
    {
        FileInputStream fis = null;
        BufferedReader br = null;
        try
        {
            fis = new FileInputStream(FilePath);
            br = new BufferedReader(new InputStreamReader(fis, "utf-8"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        //构建成String
        StringBuffer sb = new StringBuffer();
        String temp = null;
        try
        {
            while((temp = br.readLine()) != null)
            {
                sb.append(temp+'\n');
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return sb.toString();
    }
}