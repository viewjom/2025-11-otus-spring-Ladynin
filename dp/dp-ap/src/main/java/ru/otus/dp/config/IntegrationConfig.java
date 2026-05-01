package ru.otus.dp.config;

import java.io.File;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannelSpec;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.file.dsl.Files;
import org.springframework.integration.ip.tcp.TcpOutboundGateway;
import org.springframework.integration.ip.tcp.connection.AbstractClientConnectionFactory;
import org.springframework.integration.ip.tcp.connection.TcpNetClientConnectionFactory;
import ru.otus.dp.services.CdrService;
import ru.otus.dp.services.FileServiceImpl;

@Configuration
@RequiredArgsConstructor
public class IntegrationConfig {

    private @Value("${app.tcp.port}") int port;

    private String cdrRow;

    @Bean
    public MessageChannelSpec<?, ?> fileDeletingChannel() {
        return MessageChannels.queue(20);
    }

    @Bean
    public MessageChannelSpec<?, ?> outChannel() {
        return MessageChannels.publishSubscribe();
    }

/*
//Не ждем ответа от сервера
    @Bean
    public MessageHandler tcpOutboundGateway(AbstractClientConnectionFactory clientConnectionFactory) {
        TcpSendingMessageHandler handler = new TcpSendingMessageHandler();
        handler.setConnectionFactory(clientConnectionFactory);
        return handler;
    }
 */

    //Ждем ответа от сервера
    @Bean
    public TcpOutboundGateway tcpOutboundGateway(AbstractClientConnectionFactory clientConnectionFactory) {
        TcpOutboundGateway gateway = new TcpOutboundGateway();
        gateway.setConnectionFactory(clientConnectionFactory);
        return gateway;
    }

    @Bean
    public AbstractClientConnectionFactory clientConnectionFactory() {
        TcpNetClientConnectionFactory client = new TcpNetClientConnectionFactory("localhost", port);
        client.setSoTimeout(30000);
        client.setConnectTimeout(60);
        return client;
    }

    @Bean
    public IntegrationFlow fileProcessFlow(CdrService cdrService,
                                           FileServiceImpl fileService,
                                           @Value("${app.client.dir.in}") String inputDirName,
                                           @Value("${app.client.delay}") long delay,
                                           @Value("${app.client.extension}") String extension) {
        File dir = new File(inputDirName);

        return IntegrationFlow
                .from(Files.inboundAdapter(dir).patternFilter("*" + extension),
                        e -> e.poller(Pollers.fixedDelay(delay)))
                .split(Files.splitter().charset(StandardCharsets.UTF_8))
                //Собираем в один message
                //.transform(new FileToStringTransformer())
                .wireTap(sf -> sf.handle(message -> {
                    cdrRow = message.getPayload().toString();
                }))
                .handle(tcpOutboundGateway(clientConnectionFactory()))
                .handle((payload, headers) -> {
                    String reply = new String((byte[]) payload, StandardCharsets.UTF_8);
                    fileService.move(headers, reply, cdrRow);
                    return reply;
                })
                .channel(fileDeletingChannel())
                .get();
    }

    @Bean
    public IntegrationFlow finishFlow(FileServiceImpl fileService) {
        return
                IntegrationFlow.from(fileDeletingChannel())
                        .handle(fileService, "delete")
                        .channel(outChannel())
                        .get();
    }
}
