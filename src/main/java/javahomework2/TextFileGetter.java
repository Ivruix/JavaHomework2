package javahomework2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class TextFileGetter {
    public static List<String> getTextFiles(String path) throws IOException {
        List<String> filePaths;
        try (Stream<Path> stream = Files.walk(Paths.get(path))) {
            filePaths = stream.filter(Files::isRegularFile)
                    .map(Path::toString)
                    .filter(filePath -> filePath.endsWith(".txt"))
                    .collect(Collectors.toList());
        }
        return filePaths;
    }
}
