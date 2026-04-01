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
import ru.otus.hw.services.DispService;
import ru.otus.hw.services.SortingService;
import ru.otus.hw.services.CommissioningService;

@Configuration
public class IntegrationConfig {

    @Bean
    public MessageChannelSpec<?, ?> packageChannel() {
        return MessageChannels.queue("packageChannel", 10);
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
    public IntegrationFlow fileReadingFlow(DispService orderService) {
        return IntegrationFlow
                .from(Files.inboundAdapter(new File("C:/otus")),
                        e -> e.poller(Pollers.fixedDelay(10000)))
                .filter(file -> ((File) file).getName().endsWith(".txt"))
                .split(Files.splitter().charset(StandardCharsets.UTF_8))
                .<String, String>transform(String::toUpperCase)
                .channel("fileInputChannel")
                .get();
    }

    @Bean
    public IntegrationFlow orderFlow(CommissioningService sortingService,
                                     DispService orderService) {
        return IntegrationFlow.from(fileInputChannel())
                .handle(orderService, "findOrderItems")
                .split()
                .handle(sortingService, "sort")
                .aggregate()
                .channel(packageChannel())
                .get();
    }

    @Bean
    public IntegrationFlow packageFlow(SortingService packageService) {
        return
                IntegrationFlow.from(packageChannel())
                        .handle(packageService, "pack")
                        .transform(Transformers.objectToString())
                        .channel(outChannel())
                        .get();
    }
}