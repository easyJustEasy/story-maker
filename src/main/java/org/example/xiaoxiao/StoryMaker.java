package org.example.xiaoxiao;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.example.base.AppConfig;
import org.example.base.TongYiDocGenerate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class StoryMaker {
    @Autowired
    private TongYiDocGenerate docGenerate;
    private static final String SYSTEM_MESSAGE = """
            # 角色
            你是一位富有创意的儿童故事作家，擅长创作充满奇思妙想和科学元素的故事。你的角色是用生动有趣的方式激发孩子们的好奇心和创造力。
                        
            ## 技能
            ### 技能1：创作连贯的故事大纲
            - 根据提供的故事概述，生成指定集数的连续且有趣的故事大纲。
            - 每集故事都围绕一个积极的有意义的主题展开，描述主人公如何发现问题、思考解决方案、动手实践并最终成功解决问题的过程。
            - 确保每集故事具有趣味性和教育意义，鼓励孩子们勇于尝试、积极探索。
                        
            ### 技能2：编写吸引人的标题
            - 根据提供的故事概述，生成50集连续且有趣的故事大纲。
            - 为每一集故事编写吸引人的数字标题，如“第一集 xxx”、“第二集 xxx”等。
            - 标题应简洁明了，能够概括该集的主要内容或亮点。
                        
                        
            ### 技能3：传递生活教训
            - 在每集故事中融入重要的生活教训，如团队合作的重要性、耐心和毅力的价值等。
            - 通过故事情节自然地传达这些教训，使孩子们在享受故事的同时也能学到宝贵的人生经验。
                        
            ### 技能4：保持故事的一致性和连贯性
            - 确保整个系列的故事具有连贯性，每集故事之间有合理的过渡和联系。
            - 保持主要角色的性格和发展一致性，使读者能够跟随角色的成长轨迹。
            ### 技能5：学习知识，如数学、艺术、地理、历史等
            - 适当的插入一些知识，使小朋友能从中获得启发。
                        
            ## 限制
            - 每集故事标题命名方式为：第x集（如第一集 神秘小镇）。
            - 每集故事必须用"@@@"隔开方便程序解析
            - 仅生成故事大纲，不需要详细的文字内容。
            - 保持故事的趣味性和教育意义，避免过于复杂或难以理解的情节。
                        
            ## 大纲结构
            ```
            故事概述：【描述】
            @@@
            第一集   【标题】
            【描述】
            @@@
            第二集   【标题】
            【描述】
            @@@
            第三集   【标题】
            【描述】
            @@@
            ...
            第五十集   【标题】
            【描述】
            ```
                        
               
                        
            """;

    public void generate(String prompt) {
        File touch = FileUtil.touch(AppConfig.tempDir() + File.separator + "story.txt");
        String s = FileUtil.readString(touch, StandardCharsets.UTF_8);
        if (StrUtil.isBlankIfStr(s)) {

           continueWrite(s,prompt,touch);
        }else{
            String generate = generateWithRetry(prompt);
            FileUtil.appendString(generate, touch, StandardCharsets.UTF_8);
            for (int i = 0; i < 3; i++) {
              continueWrite(FileUtil.readString(touch, StandardCharsets.UTF_8),prompt,touch);
            }
        }

    }

    private String generateWithRetry(String prompt) {
        try {
           return docGenerate.generate(SYSTEM_MESSAGE, prompt);
        } catch (Exception e) {
            try {
                ThreadUtil.safeSleep(1000);
                return docGenerate.generate(SYSTEM_MESSAGE, prompt);
            } catch (Exception ex1) {
                try {
                    ThreadUtil.safeSleep(1000);
                    return docGenerate.generate(SYSTEM_MESSAGE, prompt);
                } catch (Exception ex2) {
                    log.info("exception", ExceptionUtil.getRootCause(ex2));
                    throw new RuntimeException(ex2);
                }
            }
        }
    }

    private void continueWrite(String originStr, String prompt, File touch) {
        String[] split = originStr.split("@@@");
        prompt = "请根据提示："+prompt+"续写50集,从第"+split.length+"集开始";
        System.out.println("continueWrite:"+prompt);
        String generate = generateWithRetry(prompt);
        FileUtil.appendString(generate, touch, StandardCharsets.UTF_8);
    }
}
