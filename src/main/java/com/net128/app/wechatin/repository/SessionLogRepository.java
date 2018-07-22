package com.net128.app.wechatin.repository;

import com.net128.app.wechatin.controller.WeChatMockController;
import com.net128.app.wechatin.domain.message.CustomMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Repository
@EnableAsync
public class SessionLogRepository {
    private final static Logger logger = LoggerFactory.getLogger(SessionLogRepository.class);

    private final static String logExt=".log";

    @Value("${wechatin.logsession.pattern}")
    private String logSessionPattern;

    private BlockingQueue<CustomMessage> logEntries=new ArrayBlockingQueue<>(1000);

    public Map<String, File> getLogSessionMap() {
        return Collections.unmodifiableMap(logSessionMap);
    }

    private final Map<String, File> logSessionMap=new LinkedHashMap<>();
    private File currentLogFile;

    public void log(CustomMessage message) throws InterruptedException {
        logEntries.put(message);
    }

    @Async
    private void processLogEntries() throws IOException {
        List<CustomMessage> nextLogEntries = new ArrayList();
        logEntries.drainTo(nextLogEntries);
        File file = getCurrentLogFile(null);
        StringBuilder sb = new StringBuilder();
        for (CustomMessage message : nextLogEntries) {
            File newFile = getLogSessionFile(message);
            if (!newFile.equals(file)) {
                if (sb.length() > 0) {
                    writeLogData(file, sb.toString());
                    sb = new StringBuilder();
                }
                file = newFile;
            }
            sb.append(message.toJson());
            sb.append('\n');
        }
        if (sb.length() > 0) {
            writeLogData(file, sb.toString());
        }
    }

    private void writeLogData(File file, String logData) throws IOException {
        Files.write(Paths.get(file.getAbsolutePath()), logData.getBytes(StandardCharsets.UTF_8.name()), StandardOpenOption.APPEND);
    }

    private String getLogSessionId(CustomMessage message) {
        if (message != null && message.text != null && message.text.content != null) {
            String content = message.text.content;
            if (content.matches(logSessionPattern)) {
                String id = content.replaceAll(logSessionPattern, "$1");
                logger.debug("id {}", id);
            return id;}
        }
        return null;
    }

    private File getLogSessionFile(CustomMessage message) throws IOException {
        String id=getLogSessionId(message);
        if(id!=null) {
            getCurrentLogFile(id);
        }
        return currentLogFile;
    }

    public File getCurrentLogFile(String id) throws IOException {
        File file;
        if(id!=null) {
            file = logSessionMap.get(id);
            if (file == null) {
                file = createLogFile(id);
                logSessionMap.put(id, file);
            }
            currentLogFile = file;
        } else {
            file=currentLogFile;
        }
        return file;
    }

    private File createLogFile(String id) throws IOException {
        File file=File.createTempFile("logsession-"+id+"-", logExt);
        file.deleteOnExit();
        return file;
    }

    @PostConstruct
    protected void initLogFile() throws IOException {
        currentLogFile=createLogFile("00000");
        Thread t=new Thread() {
            public void run() {
                logger.info("{} started", getName());
                while (true) {
                    try {
                        processLogEntries();
                    } catch (IOException e) {
                        logger.error("Failed to process log entries", e);
                    }
                    try {
                        Thread.currentThread().sleep(100);
                    } catch (InterruptedException e) {
                        logger.warn("");
                        break;
                    }
                }
            }
        };
        t.setDaemon(true);
        t.setName("LogProcessor");
        t.start();
    }
}
