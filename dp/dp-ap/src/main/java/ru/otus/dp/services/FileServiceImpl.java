package ru.otus.dp.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private @Value("${app.input.dir.in}") String inputDirName;

    private @Value("${app.input.dir.out}") String outputDirName;

    private @Value("${app.input.extension}") String extension;

    @Override
    public void move(MessageHeaders messageHeaders, String reply, String cdrRow) {
        String fileName = String.format("%s/%s.%s", outputDirName,
                messageHeaders.get("file_name").toString(),
                reply);

        try {
            Files.writeString(Path.of(fileName),
                    cdrRow + System.lineSeparator(),
                    StandardOpenOption.APPEND,
                    StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete() {
        Path path = Paths.get(inputDirName);
        try (Stream<Path> walk = Files.walk(path)) {
            walk.filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(extension))
                    .forEach(p -> {
                        try {
                            Files.delete(p);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
