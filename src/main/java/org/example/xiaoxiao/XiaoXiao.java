package org.example.xiaoxiao;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.io.FileUtil;
import com.alibaba.dashscope.app.Application;
import com.alibaba.dashscope.app.ApplicationParam;
import com.alibaba.dashscope.app.ApplicationResult;
import com.alibaba.fastjson.JSONObject;
import io.reactivex.Flowable;
import lombok.extern.slf4j.Slf4j;
import org.example.base.AppConfig;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public class XiaoXiao {
    public static void main(String[] args) throws Exception {

        ApplicationParam param = ApplicationParam.builder()
                .apiKey(AppConfig.apiKey())
                .appId("439591947c28436e8b14ed0e35da476c")
                .prompt("接着从23集生成")
                .build();

        Application application = new Application();
        Flowable<ApplicationResult> applicationResultFlowable = application.streamCall(param);
        File file = new File("temp/xiaoxiao.txt");
        FileUtil.touch(file);
        AtomicReference<String> s = new AtomicReference<>("");
        applicationResultFlowable.subscribe(result -> {
            log.info("TongYiAgentGenerate result:{}", JSONObject.toJSONString(result));
            s.set(result.getOutput().getText());
        }, throwable -> {
            log.error("error when generate :" + ExceptionUtil.getRootCause(throwable));
        },() -> {
            FileUtil.appendString(s.get(),file, StandardCharsets.UTF_8);

        });

    }
}
