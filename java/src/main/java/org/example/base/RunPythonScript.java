package org.example.base;

import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@Slf4j
public class RunPythonScript {
    @Value("${local.voice-model-dir}")
    private String voiceModeDir;
    @Value("${local.python-dir}")
    private String pythonDir;


    public File runPython(String text) {
        try {
            // 创建ProcessBuilder实例
            ProcessBuilder processBuilder = new ProcessBuilder();

            // 设置要执行的命令和参数
            List<String> command = new ArrayList<>();
            command.add(pythonDir);
            command.add(voiceModeDir);

            // 添加命令行参数（如果有）
            command.add("--text");
            command.add(text);
            command.add("--path");
            File mkdir = FileUtil.mkdir(AppConfig.tempDir() + File.separator + UUID.randomUUID());
            command.add(mkdir.getAbsolutePath().replaceAll("\\\\", "\\\\\\\\"));
            log.info("command :" + String.join(" ", command));
            processBuilder.command(command);
            processBuilder.redirectErrorStream(true);
            // 启动进程
            Process process = processBuilder.start();

            // 读取Python脚本的标准输出
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // 等待进程结束，并获取退出状态码
            int exitCode = process.waitFor();
            System.out.println("Exited with code: " + exitCode);
            return mkdir;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}