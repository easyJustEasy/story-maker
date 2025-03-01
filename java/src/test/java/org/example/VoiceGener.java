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
    @Test
    public void test2() throws Exception {

      String  prompt= """
              ### 第三十四集 会发光的鞋子
                              
              夜晚降临，小镇上灯火通明，但街道上的路灯却显得有些昏暗。飞飞和他的朋友们决定晚上出去探险，但每次出门时，他们总是在黑暗中磕磕绊绊，甚至有一次差点撞到了路边停放的汽车。这让他们感到非常困扰。
                              
              “要是我们能在晚上看得更清楚就好了。”飞飞的朋友小明说。
                              
              “是啊，这样我们就能更好地探索这个小镇了。”小红附和道。
                              
              飞飞听后，心里萌生了一个想法。他回到家中，走进了自己的小小实验室。房间里摆满了各种废旧材料和工具，这些都是他平时收集起来的宝贝。飞飞坐在桌前，开始思考如何解决这个问题。
                              
              “如果我能发明一双会发光的鞋子，那该多好啊！”飞飞自言自语道，“这样我们就能在晚上安全地行走了。”
                              
              飞飞首先找来了几双旧运动鞋，然后从废弃的电子设备中拆下了LED灯和电池。他将LED灯固定在鞋子的前端和侧面，用导线连接起来。为了使鞋子能够根据脚步声调节亮度，飞飞还安装了一个声音传感器。当有脚步声时，传感器就会触发LED灯亮起，从而提供足够的光线。
                              
              经过几个小时的努力，飞飞终于完成了他的发明。他穿上新发明的鞋子，走出家门，邀请朋友们一起来试一试。他们来到小镇的一条偏僻小路上，那里没有路灯，夜色显得格外深沉。
                              
              “准备好了吗？”飞飞问道。
                """;
                String name = "voice";
        String path = AppConfig.tempDir()+File.separator+ "voice"+File.separator+name+".wav";

        File file = pythonPostApi.runPythonRemote(prompt, path,null);
        System.out.println(file.getAbsolutePath());

    }
}
