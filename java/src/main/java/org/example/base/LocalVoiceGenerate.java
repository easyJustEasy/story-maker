package org.example.base;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



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

       pythonPostApi.runPythonRemote(text,filePath);



        return filePath;
    }
}
