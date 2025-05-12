package ru.spbstu.telematics.java;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Класс для представления контекстно-свободной грамматики
 */
public class Grammar {
    // Хранение правил: ключ - нетерминал, значение - список продукций
    private final Map<String, List<List<String>>> rules = new HashMap<>();

    /**
     * Загружает грамматику из файла в формате БНФ
     * @param filename путь к файлу с грамматикой
     */
    public void loadFromFile(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                // Пропускаем пустые строки и комментарии
                if (line.isEmpty() || line.startsWith("/*")) continue;

                // Разделяем левую и правую части правила
                String[] parts = line.split("::=");
                if (parts.length != 2) {
                    System.err.println("Некорректная строка: " + line);
                    continue;
                }

                String lhs = parts[0].trim(); // Левая часть правила
                String rhs = parts[1].trim(); // Правая часть

                // Обрабатываем правую часть: разбиваем на альтернативы
                List<List<String>> productions = new ArrayList<>();
                for (String alt : rhs.split("\\|")) {
                    List<String> symbols = parseProduction(alt.trim());
                    productions.add(symbols);
                }

                // Сохраняем правило
                rules.put(lhs, productions);
            }
        }
    }

    /**
     * Парсит правую часть правила на отдельные символы
     * @param production строка с продукцией (например, "<Subject> \"had\"")
     * @return список символов (терминалов/нетерминалов)
     */
    private List<String> parseProduction(String production) {
        List<String> symbols = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (char c : production.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
                if (!inQuotes) {
                    symbols.add(current.toString());
                    current.setLength(0);
                }
            } else if (Character.isWhitespace(c) && !inQuotes) {
                if (current.length() > 0) {
                    symbols.add(current.toString());
                    current.setLength(0);
                }
            } else {
                current.append(c);
            }
        }

        if (current.length() > 0) {
            symbols.add(current.toString());
        }

        return symbols;
    }

    /**
     * Возвращает правила грамматики
     */
    public Map<String, List<List<String>>> getRules() {
        return rules;
    }
}
