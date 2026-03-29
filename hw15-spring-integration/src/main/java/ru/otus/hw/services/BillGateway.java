package ru.otus.hw.services;

import java.io.File;
import org.springframework.integration.annotation.Gateway;
        import org.springframework.integration.annotation.MessagingGateway;


        import java.util.Collection;
import ru.otus.hw.domain.Traffic;

@MessagingGateway
public interface BillGateway {
    @Gateway(requestChannel = "callChannel", replyChannel = "billChannel")
    Traffic prepare(File res);
}