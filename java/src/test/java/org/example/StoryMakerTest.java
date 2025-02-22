package org.example;

import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.example.base.AppConfig;
import org.example.base.TongYiVoiceGenerate;
import org.example.xiaoxiao.StoryMaker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class StoryMakerTest {
    @Autowired
    private StoryMaker storyMaker;
    @Autowired
    private TongYiVoiceGenerate tongYiVoiceGenerate;

    @Test
    public void test() throws Exception {
        String prompt = "请生成一个关于植物大战僵尸的故事大纲";
        File touch = FileUtil.touch(AppConfig.tempDir() + File.separator + "story.txt");
        storyMaker.generate(prompt, touch);
    }

    @Test
    public void testAudio() throws Exception {

        storyMaker.genrateEveryStoryAudio("E:\\work\\story-maker\\java\\target\\test-classes\\temp\\gushi");
    }

    @Test
    public void test1() throws Exception {
        String prompt = "请生成一个关于植物大战僵尸的故事大纲";
        File file = new File("temp/1.mp3");
        FileUtil.touch(file);
        tongYiVoiceGenerate.generate(prompt, file.getAbsolutePath());

    }

    @Test
    public void test22() throws Exception {
        File file = new File("E:\\work\\story-maker\\java\\target\\test-classes\\temp\\b4ded684-724d-4615-b5a0-8670e861e713");
        File[] files = Objects.requireNonNull(file.listFiles());
        for (int i = 0; i < files.length; i++) {
            String s = "file '" + file.getAbsolutePath() + File.separator + "instruct_" + i + ".wav" + "'";
            System.out.println(s);
        }

    }
}
