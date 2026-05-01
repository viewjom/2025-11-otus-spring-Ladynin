package ru.otus.hw.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.ip.dsl.Tcp;
import org.springframework.integration.ip.tcp.connection.AbstractServerConnectionFactory;
import org.springframework.integration.ip.tcp.connection.TcpNetServerConnectionFactory;
import ru.otus.hw.loader.CdrLoader;

@Configuration
public class TcpServerConfig {

    @Bean
    public IntegrationFlow tcpServerFlow(AbstractServerConnectionFactory connectionFactory,
                                         CdrLoader cdrService) {
        return IntegrationFlow.from(Tcp.inboundGateway(connectionFactory))
                .handle((payload, headers) -> {
                    String message = new String((byte[]) payload);
                    return message;
                   // return cdrService.load(message);
                    //System.out.println("Received: " + message);
                    // Ответ клиенту
                   // return "0";
                })
                .handle(cdrService, "load")
                .get();
    }

    @Bean
    public AbstractServerConnectionFactory serverFactory(@Value("${app.server.port}") int port) {
        return new TcpNetServerConnectionFactory(port); // Порт
    }
}
