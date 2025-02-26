package org.example.base;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Map;
import java.util.Objects;

@Component
@Slf4j
public class PythonPostApi {
    @Value("${local.remote-python}")
    private String remotePython;
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
        HttpResponse httpResponse = HttpUtil.createPost("http://127.0.0.1:8000/get_voice")
                .form(Map.of("tts_text",text,"path",mkdir.getAbsolutePath().replaceAll("\\\\", "\\\\\\\\"))).execute();
        String body = httpResponse.body();
        log.info("run python end:"+body);
        return mkdir;
    }
    public File runPythonRemote(String text, String filePath,String voice) {
        if(StrUtil.isBlankIfStr(voice)){
            voice = "longyue";
        }
        FileUtil.touch(filePath);
        HttpResponse httpResponse = HttpUtil.createPost(remotePython +"/get_voice_remote")
                .form(Map.of("tts_text",text,"audio",voice))
                .header("Accept", "audio/mpeg")
                .execute();
        InputStream body = httpResponse.bodyStream();
        try (OutputStream outputStream = new FileOutputStream(filePath)) { // 保存到文件或实时处理
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = body.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new File(filePath);
    }
}
