package org.example.xiaoxiao;

import cn.hutool.core.io.FileUtil;

import java.io.File;

public class Rename {
    public static void main(String[] args) {
        File file = new File("temp/gushi");
        File[] files = file.listFiles();
        for (File file1 : files) {
            if(file1.getName().contains(".mp3")){
                String name = file1.getName();
                FileUtil.rename(file1,name.replace(".txt",""),true);
            }
        }
    }
}
