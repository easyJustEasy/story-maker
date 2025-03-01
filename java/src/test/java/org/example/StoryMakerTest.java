package org.example;

import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.example.base.AppConfig;
import org.example.base.DocGenerateFactory;
import org.example.base.DocGenerateType;
import org.example.base.TongYiVoiceGenerate;
import org.example.maker.StoryMaker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.Objects;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class StoryMakerTest {
    @Autowired
    private StoryMaker storyMaker;
    @Autowired
    private DocGenerateFactory docGenerateFactory;

    @Test
    public void test() throws Exception {
        String prompt = "请生成一个名叫小志的小朋友的故事，希望小志聪明喜欢做手工，热爱学习，并且会很多的科学知识，要求最少50集";
        File touch = FileUtil.touch(AppConfig.tempDir() + File.separator + "story.txt");
        storyMaker.setDocGenerate(docGenerateFactory.docGenerate(DocGenerateType.OLLAMA_DEEP_SEEK));
        storyMaker.generate(prompt, touch);
    }

    @Test
    public void testAudio() throws Exception {

        storyMaker.genrateEveryStoryAudio("E:\\work\\story-maker\\java\\target\\test-classes\\temp\\gushi");
    }

    @Test
    public void test1() throws Exception {
        String prompt = "请生成一个关于植物大战僵尸的故事大纲";
        File file = new File("temp/1.txt");
        FileUtil.touch(file);
        docGenerateFactory.docGenerate(DocGenerateType.OLLAMA_DEEP_SEEK).generate(prompt, file.getAbsolutePath());

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
