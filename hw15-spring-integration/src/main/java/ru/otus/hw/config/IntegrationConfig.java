package ru.otus.hw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.MessageChannelSpec;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.PollerSpec;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.scheduling.PollerMetadata;
import ru.otus.hw.services.PackageService;
import ru.otus.hw.services.SortingService;

@Configuration
public class IntegrationConfig {

    @Bean
    public MessageChannelSpec<?, ?> itemsChannel() {
        return MessageChannels.queue(10);
    }

    @Bean
    public MessageChannelSpec<?, ?> packageChannel() {
        return MessageChannels.publishSubscribe();
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerSpec poller() {
        return Pollers.fixedRate(100).maxMessagesPerPoll(2);
    }

    @Bean
    public IntegrationFlow orderFlow(SortingService sortingService) {
        return IntegrationFlow.from(itemsChannel())
                .split()
                .handle(sortingService, "sort")
                .aggregate()
                .channel(packageChannel())
                .get();
    }

    @Bean
    public IntegrationFlow packageFlow(PackageService packageService) {
        return IntegrationFlow.from(packageChannel())
                .handle(packageService, "pack")
                .get();
    }
}