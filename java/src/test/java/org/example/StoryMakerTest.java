package org.example;

import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.example.base.VoiceGenerate;
import org.example.xiaoxiao.StoryMaker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class StoryMakerTest {
    @Autowired
    private StoryMaker storyMaker;
    @Autowired
    private VoiceGenerate voiceGenerate;
    @Test
    public void test() throws Exception {
        String prompt = "请生成一个关于植物大战僵尸的故事大纲";
        storyMaker.generate(prompt);

    }
    @Test
    public void test1() throws Exception {
        String prompt = "请生成一个关于植物大战僵尸的故事大纲";
        File file = new File("temp/1.mp3");
        FileUtil.touch(file);
        voiceGenerate.generate(prompt,file.getAbsolutePath());

    }
}
