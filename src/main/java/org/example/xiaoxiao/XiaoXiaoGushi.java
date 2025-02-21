package org.example.xiaoxiao;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.example.base.TongYiDocGenerate;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class XiaoXiaoGushi {
    public static String generate(String prompt) throws Exception {

                   TongYiDocGenerate tongYiDocGenerate = new TongYiDocGenerate();
                        String result = tongYiDocGenerate.generate("""
                                故事概述：在一个充满奇思妙想的小城镇里，住着一位名叫飞飞的孩子。飞飞对世界充满了好奇心，并且特别热爱科学实验。他的家里有一个小小的实验室，里面堆满了各种来自日常生活的材料——从旧玩具零件到废弃的电子产品。每天，飞飞都会遇到生活中的小难题：比如如何更快地整理好自己的房间、怎样帮助邻居解决花园浇水的问题，或是寻找一种新的方式让自行车更加安全等等。面对这些挑战，飞飞总是能想到创新的方法，利用手头上的简单材料制作出令人惊叹的小发明来解决问题。每个故事都围绕一个小发明展开，描述飞飞是如何发现问题、思考解决方案、动手实践并最终成功解决问题的过程。同时，在每一次冒险中，飞飞也会学到一些重要的生活教训，比如团队合作的重要性、耐心和毅力的价值等。通过这一系列的故事，不仅展示了科学的魅力和创造力的力量，也鼓励孩子们在日常生活中勇于尝试、积极探索，发现身边的无限可能。
                                目标：根据故事小节描述，写一篇1300字左右的小故事，要求不能脱离整体故事概述的人物设定、故事情节。
                                ""","请根据故事小节生成故事内容,"+prompt);
                     return result;
    }

    public static void main(String[] args)  {
        File file = new File("temp/gushi.txt");
        String s = FileUtil.readString(file, StandardCharsets.UTF_8);
        String[] split = s.split("@@@");
        for (int i = 0; i < split.length; i++) {
            String t = split[i];
            if(StrUtil.isBlankIfStr(t)){continue;}
            String[] lines = t.split("[\\r\\n]+");
            List<String> list = Arrays.stream(lines).filter(StrUtil::isNotBlank).toList();
            System.out.println(list.get(0));
           File f =  new File("temp/gushi/gushi_"+list.get(0)+".txt");
            if (f.exists()) {
                continue;
            }
            FileUtil.touch(f);
            String generate = null;
            try {
                ThreadUtil.safeSleep(1000);
                generate = generate(t);
            } catch (Exception e) {
                try {
                    ThreadUtil.safeSleep(1000);
                    generate = generate(t);
                } catch (Exception ex) {
                    try {
                        ThreadUtil.safeSleep(1000);
                        generate = generate(t);
                    } catch (Exception exc) {
                        throw new RuntimeException(exc);
                    }
                }
            }
            FileUtil.writeString(generate,f,StandardCharsets.UTF_8);
        }
    }
}
