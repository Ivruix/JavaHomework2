package javahomework2;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        System.out.print("Enter path to the root folder: ");

        String path = in.nextLine();

        String normalizedPath;
        try {
            normalizedPath = Paths.get(path)
                    .toAbsolutePath()
                    .normalize()
                    .toString();
        } catch (InvalidPathException exception) {
            System.out.println("Invalid path.");
            return;
        }

        List<String> files;
        try {
            files = TextFileGetter.getTextFiles(normalizedPath);
        } catch (UncheckedIOException | IOException exception) {
            System.out.println("I/O exception has occurred.");
            return;
        }

        TopologicalSorter sorter = new TopologicalSorter();

        for (String file : files) {
            sorter.AddVertex(file);
        }

        for (String file : files) {
            Scanner fileScanner;
            try {
                fileScanner = new Scanner(Paths.get(file));
            } catch (UncheckedIOException | IOException exception) {
                System.out.println("I/O exception has occurred.");
                return;
            }

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                if (line.startsWith("require")) {
                    int startIndex = line.indexOf('\'');
                    int endIndex = line.lastIndexOf('\'');

                    String required = line.substring(startIndex + 1, endIndex);

                    sorter.AddEdge(file, Paths.get(normalizedPath, required).toString());
                }
            }
            fileScanner.close();
        }

        List<String> sortedFiles;
        try {
            sortedFiles = sorter.CalculateTopologicalSort();
        } catch (IllegalStateException exception) {
            System.out.println("Cyclic dependency detected, unable to sort files.");
            return;
        }

        for (String file : sortedFiles) {
            Scanner fileScanner;
            try {
                fileScanner = new Scanner(Paths.get(file));
            } catch (UncheckedIOException | IOException exception) {
                System.out.println("I/O exception has occurred.");
                return;
            }
            while (fileScanner.hasNextLine()) {
                System.out.println(fileScanner.nextLine());
            }
            fileScanner.close();
        }
    }
}