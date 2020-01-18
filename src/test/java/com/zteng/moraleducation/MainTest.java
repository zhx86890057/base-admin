package com.zteng.moraleducation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainTest {
    public static void main(String[] args) {
        // 按指定模式在字符串查找
        String line = "九(01)班";
        String pattern = "(\\D+)(\\d+)";

        // 创建 Pattern 对象
        Pattern r = Pattern.compile(pattern);

        // 现在创建 matcher 对象
        Matcher m = r.matcher(line);
        System.out.println("Found value: " + m.group(2) );

        if (m.find( )) {
            System.out.println("Found value: " + m.group(2) );
        } else {
            System.out.println("NO MATCH");
        }
    }
}
