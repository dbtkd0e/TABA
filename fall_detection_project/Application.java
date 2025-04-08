package com.fall_detection_project.function_implement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.ffmpeg.global.avutil;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        System.out.println("java.library.path: " + System.getProperty("java.library.path"));

        try {
            Loader.load(avutil.class);
            System.out.println("Library loaded successfully using Loader!");
        } catch (Exception e) {
            e.printStackTrace();
        }

        SpringApplication.run(Application.class, args);
    }
}
