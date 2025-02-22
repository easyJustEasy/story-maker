package org.example;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.example.base.RunPythonScript;
import org.example.base.VoiceGenerate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class VoiceGener {
    @Autowired
    private RunPythonScript runPythonScript;

    @Test
    public void test() throws Exception {
        String prompt = "请生成一个关于植物大战僵尸的故事大纲";
        runPythonScript.runPython(prompt);

    }
}
