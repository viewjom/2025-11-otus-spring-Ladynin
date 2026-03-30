package ru.otus.hw.config;

import java.io.File;
import java.nio.charset.StandardCharsets;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.MessageChannelSpec;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.Transformers;
import org.springframework.integration.file.dsl.Files;
import org.springframework.messaging.MessageChannel;
import ru.otus.hw.services.OrderService;
import ru.otus.hw.services.PackageService;
import ru.otus.hw.services.SortingService;

@Configuration
public class IntegrationConfig {

    @Bean
    public MessageChannelSpec<?, ?> packageChannel() {
        return MessageChannels.queue("itemsChannel", 10);
    }

    @Bean
    public MessageChannelSpec<?, ?> outChannel() {
        return MessageChannels.publishSubscribe();
    }

    @Bean
    public MessageChannel fileInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow fileReadingFlow(OrderService orderService) {
        return IntegrationFlow
                .from(Files.inboundAdapter(new File("C:/otus"))
                                .patternFilter("*.txt"),
                        e -> e.poller(Pollers.fixedDelay(10000))
                )
                .split(Files.splitter().charset(StandardCharsets.UTF_8))
                .<String, String>transform(String::toUpperCase)
                .route(File.class, file -> {
                    return "fileInputChannel";
                })
                .get();
    }

    @Bean
    public IntegrationFlow orderFlow(SortingService sortingService,
                                     OrderService orderService) {
        return IntegrationFlow.from(fileInputChannel())
                .handle(orderService, "findOrderItems")
                .split()
                .handle(sortingService, "sort")
                .aggregate()
                .channel(packageChannel())
                .get();
    }

    @Bean
    public IntegrationFlow packageFlow(PackageService packageService) {
        return
                IntegrationFlow.from(packageChannel())
                        .bridge(e -> e.poller(Pollers.fixedDelay(40000)))
                        .handle(packageService, "pack")
                        .transform(Transformers.objectToString())
                        .channel(outChannel())
                        .get();
    }
}