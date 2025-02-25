package org.example.xiaoxiao;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadFactoryBuilder;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.example.base.LocalVoiceGenerate;
import org.example.base.RunPythonScript;
import org.example.base.TongYiDocGenerate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

@Component
@Slf4j
public class StoryMaker {
    private ExecutorService executorService = new ThreadPoolExecutor(5, 5, 2, TimeUnit.MINUTES, new LinkedBlockingQueue<>(100), new ThreadPoolExecutor.CallerRunsPolicy());
    @Autowired
    private TongYiDocGenerate docGenerate;
    @Autowired
    private LocalVoiceGenerate localVoiceGenerate;
    @Autowired
    private RunPythonScript runPythonScript;
    Process process = null;
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
    private static final String everyStoryPromt = """
            故事概述：%s
            目标：根据故事小节描述，写一篇1300字左右的小故事，要求不能脱离整体故事概述的人物设定、故事情节。
            """;

    @PostConstruct
    public void init() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (process != null && process.isAlive()) {
                long pid = process.pid();
                process.destroy(); // 终止进程
                System.out.println("Python server stopped.");

                log.info("shutdown python server ======>>>" + pid);
            }
            executorService.shutdown();
            log.info("shutdown now ======>>>");
        }));
    }

    public void generate(String prompt, File touch) {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        process = runPythonScript.startServer(countDownLatch);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("generate started====>>>");
        String s = FileUtil.readString(touch, StandardCharsets.UTF_8);
        if (StrUtil.isNotBlank(s)) {
            continueWrite(s, prompt, touch);
        } else {
            String generate = generateWithRetry(prompt);
            FileUtil.appendString(generate, touch, StandardCharsets.UTF_8);
            continueWrite(FileUtil.readString(touch, StandardCharsets.UTF_8), prompt, touch);
        }
        String storyPath = FileUtil.mkdir(touch.getParentFile().getAbsolutePath() + File.separator + "gushi").getAbsolutePath();
        generateEverySory(FileUtil.readString(touch, StandardCharsets.UTF_8), storyPath);
        while (true) {
        }
    }


    public void genrateEveryStoryAudio(String storyPath) {
        executorService.submit(() -> {
            System.out.println("genrateEveryStoryAudio:============================>" + storyPath);
            if (StrUtil.isBlankIfStr(storyPath)) {
                return;
            }

            File file = new File(storyPath);
            String s1 = file.getName().replaceAll(".txt", "");
            String audioPath = file.getParentFile().getAbsolutePath() + File.separator + s1 + ".wav";
            if (FileUtil.exist(audioPath)) {
                return;
            }
            String s = FileUtil.readString(file, StandardCharsets.UTF_8);
            try {
                localVoiceGenerate.generate(s, audioPath);
            } catch (Exception e) {
                try {
                    localVoiceGenerate.generate(s, audioPath);
                } catch (Exception ex) {
                    try {
                        localVoiceGenerate.generate(s, audioPath);
                    } catch (Exception exc) {
                        throw new RuntimeException(exc);
                    }
                }
            }

        });


    }

    public static String generate(String desc, String prompt) throws Exception {

        TongYiDocGenerate tongYiDocGenerate = new TongYiDocGenerate();
        return tongYiDocGenerate.generate(String.format(everyStoryPromt, desc), "请根据故事小节生成故事内容," + prompt);
    }

    private void generateEverySory(String s, String parentDir) {
        List<String> split = split(s);
        String desc = split.get(0);
        for (int i = 1; i < split.size(); i++) {
            String t = split.get(i);
            if (StrUtil.isBlankIfStr(t)) {
                continue;
            }
            String[] lines = t.split("[\\r\\n]+");
            List<String> list = Arrays.stream(lines).filter(StrUtil::isNotBlank).toList();
            File f = new File(parentDir + File.separator + list.get(0) + ".txt");
            if (f.exists()) {
                genrateEveryStoryAudio(f.getAbsolutePath());
                continue;
            }
            doGenerateEveryStory(desc, t, f);
            genrateEveryStoryAudio(f.getAbsolutePath());
        }
    }

    private void doGenerateEveryStory(String desc, String t, File f) {
        FileUtil.touch(f);
        String generate = null;
        try {
            ThreadUtil.safeSleep(300);
            generate = generate(desc, t);
        } catch (Exception e) {
            try {
                ThreadUtil.safeSleep(300);
                generate = generate(desc, t);
            } catch (Exception ex) {
                try {
                    ThreadUtil.safeSleep(300);
                    generate = generate(desc, t);
                } catch (Exception exc) {
                    throw new RuntimeException(exc);
                }
            }
        }
        FileUtil.writeString(generate, f, StandardCharsets.UTF_8);
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
        if (split(FileUtil.readString(touch, StandardCharsets.UTF_8)).size() >= 150) {
            return;
        }
        List<String> split = split(originStr);
        int last = 150 - split.size();
        if (last <= 0) {
            return;
        }
        prompt = "请根据提示：" + prompt + "续写" + (Math.min(last, 50)) + "集,从第" + split.size() + "集开始";
        System.out.println("continueWrite:" + prompt);
        String generate = generateWithRetry(prompt);
        FileUtil.appendString(generate, touch, StandardCharsets.UTF_8);
        if (split(FileUtil.readString(touch, StandardCharsets.UTF_8)).size() < 150) {
            continueWrite(FileUtil.readString(touch, StandardCharsets.UTF_8), prompt, touch);
        }
    }

    private List<String> split(String originStr) {
        return Arrays.stream(originStr.split("@@@")).filter(StrUtil::isNotBlank).toList();
    }
}
