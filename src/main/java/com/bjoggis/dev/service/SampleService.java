package com.bjoggis.dev.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class SampleService {

    private static final Logger LOG = LoggerFactory.getLogger(SampleService.class);

    public void createNewFile() {
        String name = getFileName();

        Path path = Paths.get(System.getProperty("user.dir") + "\\files\\" + name);

        LOG.info("Trying to create file: " + path.toString());

        String content = "";

        try {
            Files.write(path, content.getBytes(StandardCharsets.UTF_8));
            LOG.info("Created new file: " + name);
        } catch (IOException e) {
            LOG.error("Failed to create file: " + name);
            e.printStackTrace();
        }
    }

    //Generating Unique File Name
    public String getFileName() {
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss").format(new Date());
        return "FILE_" + timeStamp + ".txt";
    }

    @PostConstruct
    public void deleteFiles() {
        File dir = new File(System.getProperty("user.dir") + "\\files\\");
        for(File file: dir.listFiles())
            if (!file.isDirectory())
                file.delete();
    }
}
