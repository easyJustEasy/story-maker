package org.example.base;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Map;
import java.util.UUID;

@Component
@Slf4j
public class PythonPostApi {
    public File runPython(String text) {
        log.info("run python start:"+text);
        File mkdir = FileUtil.mkdir(AppConfig.tempDir() + File.separator + UUID.randomUUID());
        HttpResponse httpResponse = HttpUtil.createPost("http://127.0.0.1:8000/get_voice").form(Map.of("tts_text",text,"path",mkdir.getAbsolutePath().replaceAll("\\\\", "\\\\\\\\"))).execute();
        String body = httpResponse.body();
        log.info("run python end:"+body);
        return mkdir;
    }
}
