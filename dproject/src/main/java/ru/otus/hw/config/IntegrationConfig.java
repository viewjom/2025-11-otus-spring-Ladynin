package ru.otus.hw.config;

import java.io.File;
import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.MessageChannelSpec;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.Transformers;
import org.springframework.integration.file.dsl.Files;
import org.springframework.integration.ip.tcp.connection.AbstractClientConnectionFactory;
import org.springframework.integration.ip.tcp.connection.AbstractServerConnectionFactory;
import org.springframework.integration.ip.tcp.connection.TcpNetClientConnectionFactory;
import org.springframework.integration.ip.tcp.connection.TcpNetServerConnectionFactory;
import org.springframework.integration.ip.tcp.inbound.TcpInboundGateway;
import org.springframework.integration.ip.tcp.inbound.TcpReceivingChannelAdapter;

import org.springframework.integration.ip.tcp.outbound.TcpOutboundGateway;
import org.springframework.integration.ip.tcp.serializer.ByteArrayLfSerializer;
import org.springframework.integration.ip.tcp.serializer.ByteArrayRawSerializer;
import org.springframework.messaging.MessageChannel;
import ru.otus.hw.services.DispService;
import ru.otus.hw.services.SortingService;
import ru.otus.hw.services.CommissioningService;

@Configuration
public class IntegrationConfig {

    // TCP Inbound: Получение данных
    @Bean
    public AbstractServerConnectionFactory serverFactory() {
        TcpNetServerConnectionFactory factory = new TcpNetServerConnectionFactory(9801); // Порт
        factory.setDeserializer(new ByteArrayRawSerializer());
       // factory.setDeserializer(ByteArrayLfSerializer.CRLF_SERIALIZER);
        return factory;
    }

    @Bean
    public TcpInboundGateway tcpInboundGateway(AbstractServerConnectionFactory serverFactory) {
        TcpInboundGateway gateway = new TcpInboundGateway();
        gateway.setConnectionFactory(serverFactory);
        //gateway.setRequestChannel(requestChannel());
        gateway.setRequestChannelName("tcpInputChannel");
        return gateway;
    }

    @Bean
    public IntegrationFlow tcpServerFlow() {
        return IntegrationFlow.from("tcpInputChannel")
            //    .<byte[], String>transform(payload -> "Echo: " + new String(payload))
                .<byte[], String>transform(payload -> "0001022N00")
                .handle(System.out::println) // Печать обработанного
                .get(); // Возвращает строку обратно в TcpInboundGateway
    }

    /*
    @Bean
    public MessageChannel requestChannel() {
        return new DirectChannel();
    }



    @ServiceActivator(inputChannel = "requestChannel")
    public String processMessage(String message) {
        System.out.println("requestChannel: " + message);
        return "0001022N00"; // Обработка
    }

     */







/*
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
    public IntegrationFlow fileReadingFlow(@Value("${app.input.delay}") long delay
            ,@Value("${app.input.dir}") String inputDirName
            , @Value("${app.input.file-suffix}") String inputFileSuffix) {
        File dir = new File(inputDirName);
        return IntegrationFlow
                .from(Files.inboundAdapter(dir),
                        e -> e.poller(Pollers.fixedDelay(delay)))
                .filter(file -> ((File) file).getName().endsWith(inputFileSuffix))
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

 */
}