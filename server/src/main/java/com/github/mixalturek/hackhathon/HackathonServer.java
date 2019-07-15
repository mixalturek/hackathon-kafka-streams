package com.github.mixalturek.hackhathon;

import ch.qos.logback.classic.LoggerContext;
import com.github.mixalturek.hackhathon.config.ConfigUtils;
import com.github.mixalturek.hackhathon.config.StreamsServerConfig;
import com.github.mixalturek.hackhathon.streams.HackathonStreams;
import org.apache.kafka.streams.KafkaStreams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

/**
 * Server that is running `Kafka Streams` stream processing.
 */
public class HackathonServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(HackathonServer.class);

    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler((thread, e) -> LOGGER.error("Uncaught exception: {}", thread, e));
        logLifeCycleEvent("STARTING APPLICATION");

        StreamsServerConfig config = StreamsServerConfig.load();
        CountDownLatch shutdownLatch = registerShutdownHook(config.getShutdownTimeout());

        try (KafkaStreams ignored = startStreamProcessing(config)) {
            logLifeCycleEvent("APPLICATION STARTED");
            shutdownLatch.await();
            logLifeCycleEvent("STOPPING APPLICATION");
        } catch (Exception e) {
            LOGGER.error("Startup or shutdown failed", e);
            Arrays.stream(e.getSuppressed())
                    .forEach(throwable -> LOGGER.error("Suppressed exception", throwable));
        }

        logLifeCycleEvent("APPLICATION STOPPED");
        LOGGER.info("Stopping logging subsystem");
        ((LoggerContext) LoggerFactory.getILoggerFactory()).stop();
    }

    private static void logLifeCycleEvent(String message) {
        LOGGER.info("====================== {} ======================", message);
    }

    private static CountDownLatch registerShutdownHook(Duration shutdownTimeout) {
        CountDownLatch shutdownLatch = new CountDownLatch(1);
        Runtime.getRuntime().addShutdownHook(shutdownHook(shutdownLatch, Thread.currentThread(), shutdownTimeout));
        return shutdownLatch;
    }

    private static Thread shutdownHook(CountDownLatch shutdownLatch, Thread mainThread, Duration shutdownTimeout) {
        return new Thread("ShutdownHook") {
            @Override
            public void run() {
                logLifeCycleEvent("SHUTDOWN HOOK");
                shutdownLatch.countDown();

                // Immediately after all shutdown hooks finish, Java process would stop. There is no waiting for finish
                // of any running non-daemon thread so proper shutdown would not happen.
                try {
                    mainThread.join(shutdownTimeout.toMillis());
                } catch (InterruptedException e) {
                    LOGGER.error("Waiting for exit of main thread interrupted", e);
                }

                logLifeCycleEvent("SHUTDOWN HOOK FINISHED");
            }
        };
    }

    private static KafkaStreams startStreamProcessing(StreamsServerConfig config) {
        LOGGER.info("Starting stream processing");
        return new HackathonStreams()
                .startKafkaStreams(ConfigUtils.toProperties(config.getKafkaStreams()), config.getInputTopic());
    }
}
