package com.waxjx.largescale.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JdbcUtil {
    public static String extractHostFromJdbcUrl(String jdbcUrl) {
        // 定义正则表达式来匹配主机名部分
        Pattern pattern = Pattern.compile("jdbc:mysql://([^:/]+)");
        Matcher matcher = pattern.matcher(jdbcUrl);

        if (matcher.find()) {
            // 返回第一个捕获组，即主机名
            return matcher.group(1);
        }

        // 如果没有找到匹配，返回null或抛出异常
        return null;
    }
}
