package org.example.base;

import cn.hutool.core.io.FileUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
@Component
@Slf4j
public class FfmpegVoiceMerager {
    public  String concat(String path, String filePath) throws Exception {
        File file = new File(path);
        if (file.isFile()) {
            throw new RuntimeException("提供的路径是一个文件，而不是目录");
        }
        if (file.listFiles()==null|| Objects.requireNonNull(file.listFiles()).length==0) {
            throw new RuntimeException("没有发现音频文件");
        }
        String property = AppConfig.tempDir();
        String filelist = property+ File.separator+ UUID.randomUUID()+".txt";
        File file1 = new File(filelist);
        FileUtil.touch(file1);
        FileUtil.writeLines(Arrays.stream(Objects.requireNonNull(file.listFiles())).map(s -> "file '" + s.getAbsolutePath() + "'").toList(), file1, "utf-8");
        File[] files = Objects.requireNonNull(file.listFiles());
        List<String> list = Lists.newArrayList();
        for (int i = 0; i < files.length; i++) {
            String s = "file '" + file.getAbsolutePath() + File.separator + "instruct_" + i + ".wav" + "'";
            list.add(s);
        }
        FileUtil.writeLines(list, file1, "utf-8");

        ProcessBuilder pb = new ProcessBuilder("ffmpeg",
//                "-v","debug",
                "-r","30",
                "-f", "concat",
                "-safe", "0",
                "-i", new File(filelist).getAbsolutePath(),
                "-c", "copy",
                filePath);
        pb.inheritIO().start().waitFor();
        FileUtil.del(new File(filelist).getAbsolutePath());
        return filePath;
    }
}
