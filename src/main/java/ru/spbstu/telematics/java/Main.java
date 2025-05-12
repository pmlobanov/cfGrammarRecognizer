package ru.spbstu.telematics.java;


import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Main {
    // Для тестирования
    public static void main(String[] args) {
        Grammar grammar = new Grammar();
        try {
            grammar.loadFromFile("grammar.txt");
            System.out.println("Успешно загружено правил: " + grammar.getRules().size());

            // Вывод всех правил
            for (Map.Entry<String, List<List<String>>> entry : grammar.getRules().entrySet()) {
                System.out.println(entry.getKey() + " ::= ");
                for (List<String> prod : entry.getValue()) {
                    System.out.println("  " + String.join(" ", prod));
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка чтения файла: " + e.getMessage());
        }
    }
}