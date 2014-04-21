package uk.co.benjiweber.benjournal;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import org.slf4j.LoggerFactory;

import java.io.File;

public class JournalLogFactory {
    final static org.slf4j.Logger logger = LoggerFactory.getLogger(JournalLogFactory.class);

    private static Sequencer sequencer = new Sequencer() { };

    public static Logger createJournalLogger(String key, String file) {
        String baseName = file + File.separator + key;

        LoggerContext logCtx = (LoggerContext) LoggerFactory.getILoggerFactory();

        PatternLayoutEncoder logEncoder = new PatternLayoutEncoder();
        logEncoder.setContext(logCtx);
        logEncoder.setPattern("%msg%n");
        logEncoder.start();

        ConsoleAppender logConsoleAppender = new ConsoleAppender();
        logConsoleAppender.setContext(logCtx);
        logConsoleAppender.setName("console");
        logConsoleAppender.setEncoder(logEncoder);
        logConsoleAppender.start();

        logEncoder = new PatternLayoutEncoder();
        logEncoder.setContext(logCtx);
        logEncoder.setPattern("%msg%n");
        logEncoder.start();

        RollingFileAppender logFileAppender = new RollingFileAppender();
        logFileAppender.setContext(logCtx);
        logFileAppender.setName(key);
        logFileAppender.setEncoder(logEncoder);
        logFileAppender.setAppend(true);
        logFileAppender.setFile(baseName);

        TimeBasedRollingPolicy logFilePolicy = new TimeBasedRollingPolicy();
        logFilePolicy.setContext(logCtx);
        logFilePolicy.setParent(logFileAppender);
        logFilePolicy.setFileNamePattern(baseName + "-%d{yyyyMMddHHmm}-" + sequencer.next() + ".log");
        logFilePolicy.setMaxHistory(7);
        logFilePolicy.start();

        logFileAppender.setRollingPolicy(logFilePolicy);
        logFileAppender.start();

        Logger log = logCtx.getLogger(key);
        log.setAdditive(false);
        log.setLevel(Level.INFO);
        log.addAppender(logConsoleAppender);
        log.addAppender(logFileAppender);
        return log;
    }
}
