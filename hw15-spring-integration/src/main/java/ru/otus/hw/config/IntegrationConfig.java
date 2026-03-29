package ru.otus.hw.config;
/*
import com.sun.jna.platform.win32.Advapi32Util;
import java.io.File;
import java.nio.charset.StandardCharsets;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.integration.annotation.Router;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannelSpec;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.PollerSpec;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.integration.file.dsl.Files;
import org.springframework.messaging.MessageChannel;


import ru.otus.hw.services.ReadService;
import ru.otus.hw.services.ReadServiceImpl;


@Configuration
@RequiredArgsConstructor
public class IntegrationConfig {

    private final ReadService readService;

    @Bean
    public MessageChannelSpec<?, ?> itemsChannel() {
        return MessageChannels.queue(10);
    }

    @Bean
    public MessageChannelSpec<?, ?> foodChannel() {
        return MessageChannels.publishSubscribe();
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerSpec poller() {
        return Pollers.fixedRate(100).maxMessagesPerPoll(2);
    }

    @Router(inputChannel="allChannel")
    public String route(Object payload) {
        if (payload instanceof Advapi32Util.Account) {
            return "outputChannel";
        }
        return "outputChannel";
    }


    @Bean
    public IntegrationFlow fileReadingFlow(ReadService readService) {
        return IntegrationFlow
                // Сканируем папку /input
                .from(Files.inboundAdapter(new File("C:/otus"))
                                .patternFilter("*.txt"), // Читать только .txt файлы
                        e -> e.poller(Pollers.fixedDelay(5000))) // Опрос каждые 5 сек
                .handle(message ->message.getPayload().toString()
                       // readService.create(message.getPayload().toString())
                     //   message -> {message.getPayload().toString();}
                //        System.out::println
                )

                   // System.out.println("Читаю файл: " + file.getName());
                    // Обработка файла (например, чтение через FileToStringTransformer)

                          .channel("outputChannel")
                .get();
    }

    @Bean
    public IntegrationFlow fileOutChannel() {
        return f -> f
                //    .channel("p2p")
                .channel("pubSub")
                .handle(message -> System.out.println("Сообщение получено: "));

    }

    @Bean
    public IntegrationFlow orders() {
        return f -> f
                .<Order, Boolean>route(Order::isPriority,
                        mapping -> mapping
                                .subFlowMapping(true, sf -> sf
                                        .channel("outputChannel")
                                )
                                .subFlowMapping(false, sf -> sf
                                        .channel("notIcedOrders")
                                )
                )

*/
/*
    @Bean
    public IntegrationFlow cafeFlow(KitchenService kitchenService) {
        return IntegrationFlows.from(itemsChannel())
                .split()
                .handle(kitchenService, "cook")
                .<Food, Food>transform(f -> new Food(f.name().toUpperCase()))
                .aggregate()
                .channel(foodChannel())
                .get();
    }






    @Bean
    public IntegrationFlow nextFlow() {
        return IntegrationFlows.from(foodChannel())
                .channel("pubSub")
                .handle(message -> System.out.println("Сообщение получено: "))
                .get();

    }



}

 */



//------------------------------------------------------------------------------------------------
/*

@Configuration
@RequiredArgsConstructor
public class IntegrationConfig {


    @Bean
    public MessageChannelSpec<?, ?> p2p() {
        return MessageChannels.queue("p2p", 10);
    }


    @Bean
    public MessageChannelSpec<?, ?> pubSub() {
        return MessageChannels.publishSubscribe("pubSub");
    }

    @Bean
    public MessageChannelSpec<?, ?> linesChannel() {
        return MessageChannels.publishSubscribe("linesChannel");
    }



    @Bean
    public MessageChannel fileInputChannel() {
        return MessageChannels.direct().get();
    }


    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerMetadata poller() {
        return Pollers
                .fixedRate(1000)
                .get();
    }




    @Bean
    public IntegrationFlow fileReadingFlow(ReadServiceImpl readService) {
        return IntegrationFlows
                .from(Files.inboundAdapter(new File("C:/otus"))
                                .patternFilter("*.txt"),
                        // 2. Определение поллера (Poller)
                        e -> e.poller(Pollers.fixedDelay(10000)))
                .split(Files.splitter().charset(StandardCharsets.UTF_8)) // Разделение на строки
                .<String, String>transform(String::toUpperCase)
                .channel("linesChannel")
                // 3. Обработка файла
                .handle(message -> {
                    //System.out.println("Файл получен: " + message.getPayload()
                    readService.create(message.getPayload().toString());
                })
                   .channel("fileOutChannel.input")
                .get();
    }

    @Bean
    public IntegrationFlow fileOutChannel() {
        return f -> f
            //    .channel("p2p")
                .channel("pubSub")
                .handle(message -> System.out.println("Сообщение получено: "));

    }
}

 */
