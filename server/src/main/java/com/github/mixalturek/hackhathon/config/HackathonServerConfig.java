package com.github.mixalturek.hackhathon.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigBeanFactory;
import com.typesafe.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * Configuration of the application.
 */
@SuppressWarnings("unused")
public class HackathonServerConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(HackathonServerConfig.class);

    public static HackathonServerConfig load() {
        Config config = ConfigFactory.load().getConfig("streamsServer");
        LOGGER.info("Dump of raw configuration:\n{}", ConfigUtils.formatKeyValues(config));
        return ConfigBeanFactory.create(config, HackathonServerConfig.class);
    }

    private Duration shutdownTimeout;
    private Config kafkaStreams;
    private String inputTopic;
    private String outputTopic;

    public Duration getShutdownTimeout() {
        return shutdownTimeout;
    }

    public void setShutdownTimeout(Duration shutdownTimeout) {
        this.shutdownTimeout = shutdownTimeout;
    }

    public Config getKafkaStreams() {
        return kafkaStreams;
    }

    public void setKafkaStreams(Config kafkaStreams) {
        this.kafkaStreams = kafkaStreams;
    }

    public String getInputTopic() {
        return inputTopic;
    }

    public void setInputTopic(String inputTopic) {
        this.inputTopic = inputTopic;
    }

    public String getOutputTopic() {
        return outputTopic;
    }

    public void setOutputTopic(String outputTopic) {
        this.outputTopic = outputTopic;
    }
}
