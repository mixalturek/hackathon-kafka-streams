package com.github.mixalturek.streams;

import ch.qos.logback.classic.LoggerContext;
import com.github.mixalturek.streams.config.StreamsServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

/**
 * Server that is running `Kafka Streams` stream processing.
 */
public class StreamsServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(StreamsServer.class);

    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler((thread, e) -> LOGGER.error("Uncaught exception: {}", thread, e));
        logLifeCycleEvent("STARTING APPLICATION");

        StreamsServerConfig config = StreamsServerConfig.load();

        try {
            CountDownLatch shutdownLatch = registerShutdownHook(config.getShutdownTimeout());
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
                    LOGGER.error("Waiting for exit of main thread failed", e);
                }

                logLifeCycleEvent("SHUTDOWN HOOK FINISHED");
            }
        };
    }
}
