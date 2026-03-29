package ru.otus.hw.config;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.logging.Handler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlowBuilder;
import org.springframework.integration.dsl.MessageChannelSpec;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.PollerSpec;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.file.dsl.Files;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import ru.otus.hw.services.TrafficService;

@Configuration
public class TestConfig {

    @Bean
    public MessageChannel dataChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel callChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannelSpec<?, ?> billChannel() {

        return MessageChannels.publishSubscribe();
    }

    @Bean
    public MessageChannelSpec<?, ?> buildingChannel() {
        return MessageChannels.publishSubscribe();
    }


    @Bean
    public IntegrationFlow fileReadingFlow() {
        return IntegrationFlow
                .from(Files.inboundAdapter(new File("C:/otus"))
                                .patternFilter("*.txt"),
                        e -> e.poller(Pollers.fixedDelay(10000)))
                .<File, String>route(
                        file -> "callChannel",//file.getName().startsWith("data") ? "dataChannel" : "otherChannel",

                        mapping -> mapping
                                //  .channelMapping("dataChannel", "dataChannel")
                                .channelMapping("callChannel", "callChannel")
                )
                .get();
    }

    @Bean
    public IntegrationFlow callFlow(TrafficService trafficService) {
        return IntegrationFlow.from(callChannel())
                .split(Files.splitter().charset(StandardCharsets.UTF_8))
                .<String, String>transform(String::toUpperCase)
                .handle(message -> {
                    trafficService.calculateCall(message.getPayload().toString());
                })
               // .aggregate()
                .channel(billChannel())
                .get();
    }


    @Bean
    public IntegrationFlow billFlow() {
        return IntegrationFlow.from(billChannel())
                //.split()
                .handle(
                message -> System.out.println(message.getPayload())
                )
               // .handle(inhabitateService, "inhabitate")

                //.channel(inhabitedChannel())
                .get();
    }


}

