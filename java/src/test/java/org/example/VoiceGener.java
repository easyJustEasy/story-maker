package org.example;

import lombok.extern.slf4j.Slf4j;
import org.example.base.AppConfig;
import org.example.base.FfmpegVoiceMerager;
import org.example.base.PythonPostApi;
import org.example.base.RunPythonScript;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class VoiceGener {
    @Autowired
    private RunPythonScript runPythonScript;
    @Autowired
    private FfmpegVoiceMerager voiceMerager;
    @Autowired
    private PythonPostApi pythonPostApi;
    @Test
    public void test() throws Exception {
        String prompt = "请生成一个关于植物大战僵尸的故事大纲";
        File file = runPythonScript.runPython(prompt);
        String name = "植物大战僵尸";
        String path = AppConfig.tempDir()+File.separator+ "gushi"+File.separator+name+".wav";
        voiceMerager.concat(file.getAbsolutePath(), path);

    }
    @Test
    public void test1() throws Exception {
        String prompt = "请生成一个关于植物大战僵尸的故事大纲";
        File file = pythonPostApi.runPython(prompt, "temp/temp");
        String name = "植物大战僵尸";
//        String path = AppConfig.tempDir()+File.separator+ "gushi"+File.separator+name+".wav";
//        voiceMerager.concat(file.getAbsolutePath(), path);

    }
}
