package ru.spbstu.telematics.java;


import java.util.*;

/**
 * Консольный интерфейс для распознавания и генерации предложений
 * на основе грамматики Past Perfect Continuous.
 */
public class GrammarConsoleInterface {
    private static final Scanner scanner = new Scanner(System.in);
    private static final SimpleGrammarRecognizer recognizer = new SimpleGrammarRecognizer();
    private static final int DEFAULT_GENERATION_COUNT = 5;

    public static void main(String[] args) {
        boolean running = true;

        System.out.println("Добро пожаловать в распознаватель грамматики Past Perfect Continuous");

        while (running) {
            displayMainMenu();
            int choice = getIntInput(0, 3);

            switch (choice) {
                case 1:
                    handleSentenceValidation();
                    break;
                case 2:
                    handleSentenceGeneration();
                    break;
                case 3:
                    displayHelp();
                    break;
                case 0:
                    running = false;
                    System.out.println("Выход из программы. До свидания!");
                    break;
            }
        }

        scanner.close();
    }

    /**
     * Отображает главное меню.
     */
    private static void displayMainMenu() {
        System.out.println("\n===== ГЛАВНОЕ МЕНЮ =====");
        System.out.println("1. Проверить предложение");
        System.out.println("2. Сгенерировать предложения");
        System.out.println("3. Справка");
        System.out.println("0. Выход");
        System.out.print("Введите ваш выбор: ");
    }

    /**
     * Обрабатывает процесс проверки предложения.
     */
    private static void handleSentenceValidation() {
        System.out.println("\n===== ПРОВЕРКА ПРЕДЛОЖЕНИЯ =====");

        // Отображаем словарь перед проверкой
        displayVocabulary();

        scanner.nextLine(); // Очищаем буфер
        System.out.print("\nВведите предложение для проверки: ");
        String sentence = scanner.nextLine().trim();

        // Проверка на пустой ввод
        if (sentence.isEmpty()) {
            System.out.println("Ошибка: Из начального состояния нет эпсилон-перехода");
            return;
        }

        // Проверка на слова, которых нет в словаре
        List<String> tokens = SimpleGrammarRecognizer.tokenize(sentence);
        List<String> unknownWords = findUnknownWords(tokens);

        if (!unknownWords.isEmpty()) {
            System.out.println("Ошибка: Следующие слова отсутствуют в словаре:");
            System.out.println(String.join(", ", unknownWords));
            System.out.println("Используйте только слова из словаря, указанного выше.");
            return;
        }

        boolean isValid = SimpleGrammarRecognizer.recognize(sentence);

        System.out.println("\nРезультат: Предложение '" + sentence + "' " +
                (isValid ? "СООТВЕТСТВУЕТ" : "НЕ СООТВЕТСТВУЕТ") + " грамматике Past Perfect Continuous");

        if (!isValid) {
            System.out.println("\nВозможные причины ошибки:");
            System.out.println("- Неправильный порядок слов");
            System.out.println("- Отсутствуют обязательные части предложения");
            System.out.println("- Неправильная структура предложения");
        }
    }

    /**
     * Обрабатывает процесс генерации предложений.
     */
    private static void handleSentenceGeneration() {
        System.out.println("\n===== ГЕНЕРАЦИЯ ПРЕДЛОЖЕНИЙ =====");

        System.out.print("Сколько предложений вы хотите сгенерировать? (по умолчанию: " +
                DEFAULT_GENERATION_COUNT + "): ");

        int count;
        try {
            String input = scanner.next();
            if (input.trim().isEmpty()) {
                count = DEFAULT_GENERATION_COUNT;
            } else {
                count = Integer.parseInt(input);
                if (count <= 0) {
                    System.out.println("Некорректное количество. Используется значение по умолчанию: " + DEFAULT_GENERATION_COUNT);
                    count = DEFAULT_GENERATION_COUNT;
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Некорректный ввод. Используется значение по умолчанию: " + DEFAULT_GENERATION_COUNT);
            count = DEFAULT_GENERATION_COUNT;
        }

        System.out.println("\nГенерация " + count + " предложений в грамматике Past Perfect Continuous:");

        for (int i = 0; i < count; i++) {
            String sentence = SimpleGrammarRecognizer.generateSentence();
            System.out.println((i + 1) + ". " + sentence);

            // Проверяем сгенерированное предложение для демонстрации
            boolean isValid = SimpleGrammarRecognizer.recognize(sentence);
            if (!isValid) {
                System.out.println(" Сгенерированное предложение НЕ ВАЛИДНО");
            }
            else {
                System.out.println(" Сгенерированное предложение ВАЛИДНО");
            }
        }
    }

    /**
     * Отображает справочную информацию.
     */
    private static void displayHelp() {
        System.out.println("\n===== СПРАВКА =====");
        System.out.println("Эта программа позволяет проверять и генерировать предложения в грамматике Past Perfect Continuous.");

        System.out.println("\nПримеры правильных предложений:");
        System.out.println("- I had been working");
        System.out.println("- She had been studying recently");
        System.out.println("- They hadn't been playing");
        System.out.println("- Had you been cooking?");
        System.out.println("- What had she been doing?");

        System.out.println("\nСтруктуры предложений:");
        System.out.println("1. Утвердительные: [местоимение] had been [глагол-ing] [наречие]");
        System.out.println("2. Отрицательные: [местоимение] had not been/hadn't been [глагол-ing] [наречие]");
        System.out.println("3. Вопросы да/нет: Had [местоимение] been [глагол-ing] [наречие]?");
        System.out.println("4. WH-вопросы: [вопр.слово] had [местоимение] been [глагол-ing] [наречие]?");

        System.out.println("\nНажмите Enter для продолжения...");
        scanner.nextLine(); // Очищаем буфер
        scanner.nextLine(); // Ждем ввода пользователя
    }

    /**
     * Отображает словарь грамматики.
     */
    private static void displayVocabulary() {
        System.out.println("\n===== СЛОВАРЬ ГРАММАТИКИ =====");

        System.out.println("МЕСТОИМЕНИЯ (PRONOUNS):");
        System.out.println(formatSet(SimpleGrammarRecognizer.PRONOUNS));

        System.out.println("\nГЛАГОЛЫ С -ING (VERB_ING):");
        System.out.println(formatSet(SimpleGrammarRecognizer.VERB_ING));

        System.out.println("\nНАРЕЧИЯ (ADVERBS):");
        System.out.println(formatSet(SimpleGrammarRecognizer.ADVERBS));

        System.out.println("\nВОПРОСИТЕЛЬНЫЕ СЛОВА (WH_WORDS):");
        System.out.println(formatSet(SimpleGrammarRecognizer.WH_WORDS));

        System.out.println("\nСЛУЖЕБНЫЕ СЛОВА:");
        System.out.println("had, been, not, hadn't, ?");
    }

    /**
     * Форматирует множество строк для вывода.
     */
    private static String formatSet(Set<String> set) {
        List<String> sortedList = new ArrayList<>(set);
        Collections.sort(sortedList);
        return String.join(", ", sortedList);
    }

    /**
     * Находит слова, которых нет в словаре.
     */
    private static List<String> findUnknownWords(List<String> tokens) {
        List<String> unknownWords = new ArrayList<>();

        for (String token : tokens) {
            // Пропускаем знаки препинания и служебные слова
            if (token.equals("?") || token.equals("had") || token.equals("been") ||
                    token.equals("not") || token.equals("hadn't")) {
                continue;
            }

            // Проверяем, есть ли слово в словаре
            if (!SimpleGrammarRecognizer.PRONOUNS.contains(token) &&
                    !SimpleGrammarRecognizer.VERB_ING.contains(token) &&
                    !SimpleGrammarRecognizer.ADVERBS.contains(token) &&
                    !SimpleGrammarRecognizer.WH_WORDS.contains(token)) {
                unknownWords.add(token);
            }
        }

        return unknownWords;
    }

    /**
     * Получает целочисленный ввод от пользователя с проверкой.
     */
    private static int getIntInput(int min, int max) {
        int input = -1;
        boolean validInput = false;

        while (!validInput) {
            try {
                input = scanner.nextInt();
                if (input >= min && input <= max) {
                    validInput = true;
                } else {
                    System.out.print("Пожалуйста, введите число от " + min + " до " + max + ": ");
                }
            } catch (InputMismatchException e) {
                System.out.print("Некорректный ввод. Пожалуйста, введите число: ");
                scanner.next(); // Очищаем некорректный ввод
            }
        }

        return input;
    }
}
