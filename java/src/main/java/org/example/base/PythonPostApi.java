package org.example.base;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Component
@Slf4j
public class PythonPostApi {
    public File runPython(String text, String filePath) {
        log.info("run python start:{}",filePath);
        String dirName = new File(filePath).getName().replaceAll(".wav","").trim();
        File mkdir = FileUtil.mkdir(AppConfig.tempDir() + File.separator+"audio"+File.separator + dirName);
        File[] files = Objects.requireNonNull(mkdir.listFiles());
        int length =files.length;
        if (length>0) {
            for (File file : files) {
                FileUtil.del(file);
            }
        }
        HttpResponse httpResponse = HttpUtil.createPost("http://127.0.0.1:8000/get_voice").form(Map.of("tts_text",text,"path",mkdir.getAbsolutePath().replaceAll("\\\\", "\\\\\\\\"))).execute();
        String body = httpResponse.body();
        log.info("run python end:"+body);
        return mkdir;
    }
}
