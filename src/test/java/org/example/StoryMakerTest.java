package org.example;

import lombok.extern.slf4j.Slf4j;
import org.example.xiaoxiao.StoryMaker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class StoryMakerTest {
    @Autowired
    private StoryMaker storyMaker;
    @Test
    public void test() throws Exception {
        String prompt = "请生成一个关于植物大战僵尸的故事大纲";
        storyMaker.generate(prompt);

    }
}
