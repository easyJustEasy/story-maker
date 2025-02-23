package org.example.base;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
@Slf4j
public class LocalVoiceGenerate {
    @Autowired
    private RunPythonScript runPythonScript;
    @Autowired
    private PythonPostApi pythonPostApi;
    @Autowired
    private FfmpegVoiceMerager voiceMerager;
    public  String generate(String text,String filePath) throws Exception {
        File file = pythonPostApi.runPython(text);
        voiceMerager.concat(file.getAbsolutePath(),filePath);

        return filePath;
    }
}
