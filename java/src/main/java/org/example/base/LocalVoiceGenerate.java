package org.example.base;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class LocalVoiceGenerate {
    @Autowired
    private RunPythonScript runPythonScript;
    @Autowired
    private PythonPostApi pythonPostApi;
    @Autowired
    private FfmpegVoiceMerager voiceMerager;
    private ExecutorService executorService = new ThreadPoolExecutor(5, 5, 2, TimeUnit.MINUTES, new LinkedBlockingQueue<>(100), new ThreadPoolExecutor.CallerRunsPolicy());
    public  String generate(String text,String filePath) throws Exception {
        executorService.submit(()->{
            File file = pythonPostApi.runPython(text,filePath);
            try {
                voiceMerager.concat(file.getAbsolutePath(),filePath);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        return filePath;
    }
}
