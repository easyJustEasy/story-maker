package org.example.xiaoxiao;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import org.example.base.AppConfig;
import org.example.base.LocalVoiceGenerate;
import org.example.base.TongYiVoiceGenerate;

import java.io.File;
import java.nio.charset.StandardCharsets;

public class XiaoXiaoVoice {
    public static void main(String[] args) {
//        TongYiVoiceGenerate tongYiVoiceGenerate = new TongYiVoiceGenerate();
        LocalVoiceGenerate tongYiVoiceGenerate = new LocalVoiceGenerate();
        File file = new File("temp/gushi");
        File[] files = file.listFiles();
        for (File file1 : files) {
            String name = file1.getName().replaceAll(".txt","");
            try {
                String path = AppConfig.tempDir()+File.separator+ "gushi"+File.separator+name+".wav";
                System.out.println("path"+path);
                tongYiVoiceGenerate.generate(StrUtil.trim(FileUtil.readString(file1, StandardCharsets.UTF_8)),path.replaceAll("gushi_",""));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
//        File name1 = new File("/Users/ppx/Documents/zhuzhu/work/java-doc-gen/temp/gushi/gushi_第一集   小小实验室的秘密.txt");
//        try {
//            voiceGenerate.generate("在一个充满奇思妙想的小城镇里，住着一位名叫飞飞的孩子。飞飞对世界充满了好奇心，并且特别热爱科学实验。他家的阁楼里有一个小小的实验室，里面堆满了各种来自日常生活的材料——从旧玩具零件到废弃的电子产品。每天，飞飞都会遇到生活中的小难题，而这一次，他遇到了一个特别棘手的问题。","test".replaceAll("gushi_",""));
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
    }
}
