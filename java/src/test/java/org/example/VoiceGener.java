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
        File file = new File("/Users/ppx/Documents/work/story-maker/java/temp/audio/temp");
        String name = "voice";
        String path = AppConfig.tempDir()+File.separator+ "gushi"+File.separator+name+".wav";
        voiceMerager.concat(file.getAbsolutePath(), path);

    }
    @Test
    public void test1() throws Exception {
        String prompt = """
                要知道福利可不是天天都有，主播下播之后，你直接到店里下单，就要回复原价了！
                赶紧拍，赶紧冲，赶紧抢，赶紧囤喽。
                如果你们拍完不想要了，都是可以 随时退，过期退，全额退，不收你一分钱手续费！
                """;
        //https://www.ai8.net/tutorial/2024/1231/596.html
        prompt= """
                        用开心的语气说<|endofprompt|> 哎呀妈呀，这福利可不是天天有啊！
                        主播一下播，你直接到店里下单，嘿，那可就得回复原价啦！
                        所以说，赶紧拍，赶紧冲，赶紧抢，赶紧囤喽。
                        手慢无哦！
                  
                        但是万一您拍完之后突然觉得：
                        哎？这个东西我不想要了咋办？
                        别担心，咱们这儿可是全额退，过期退，随时退
                        而且一分钱手续费都不收您的！
                        怎么样，是不是很贴心呢？
               
                """;
        File file = pythonPostApi.runPython(prompt, "temp/temp");
        String name = "voice";
        String path = AppConfig.tempDir()+File.separator+ "gushi"+File.separator+name+".wav";
        voiceMerager.concat(file.getAbsolutePath(), path);

    }
}
