package org.example.base;

import cn.hutool.core.util.StrUtil;
import org.example.App;

import java.io.File;

public class AppConfig {
    public static synchronized String apiKey() throws Exception {
        String dashscopeApiKey = System.getenv("DASHSCOPE_API_KEY");
        if (StrUtil.isBlankIfStr(dashscopeApiKey)) {
            throw new RuntimeException("百炼APIKey为空！请先设置");
        }
        return dashscopeApiKey;
    }

    public static String agentId() {
        String dashscopeApiKey = System.getenv("JAVA_AGENT_ID");
        if (StrUtil.isBlankIfStr(dashscopeApiKey)) {
            throw new RuntimeException("JAVA_AGENT_ID为空！请先设置");
        }
        return dashscopeApiKey;
    }

    public static String tempDir() {
        return  System.getProperty("user.dir")+ File.separator+ "temp";
    }

}
