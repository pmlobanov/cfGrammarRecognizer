package ru.spbstu.telematics.java;

import java.util.*;

public class SimpleGrammarRecognizer {

    static final Set<String> PRONOUNS = Set.of("i", "you", "he", "she", "it", "we", "they");
    static final Set<String> VERB_ING = Set.of("working", "playing", "studying", "running", "cooking", "sleeping", "talking", "doing");
    static final Set<String> ADVERBS = Set.of("already", "just", "still", "recently", "quietly", "all day");
    static final Set<String> WH_WORDS = Set.of("what", "where", "when", "why", "how");

    private static final Random rnd = new Random();

    public static boolean recognize(String sentence) {
        List<String> tokens = tokenize(sentence);
        return parseSentence(tokens, 0) == tokens.size();
    }

    // --- Парсер (ваш код) ---

    private static int parseSentence(List<String> tokens, int pos) {
        int next;
        if ((next = parseAffirmative(tokens, pos)) > pos) return next;
        if ((next = parseNegative(tokens, pos)) > pos) return next;
        if ((next = parseYesNoQuestion(tokens, pos)) > pos) return next;
        if ((next = parseWhQuestion(tokens, pos)) > pos) return next;
        return pos;
    }

    private static int parseAffirmative(List<String> tokens, int pos) {
        int next = parseSubject(tokens, pos);
        if (next > pos && match(tokens, next, "had") && match(tokens, next + 1, "been")) {
            int v = next + 2;
            if (v < tokens.size() && VERB_ING.contains(tokens.get(v))) {
                int adv = parseAdverbial(tokens, v + 1);
                return adv;
            }
        }
        return pos;
    }

    private static int parseNegative(List<String> tokens, int pos) {
        int next = parseSubject(tokens, pos);
        if (next > pos) {
            if (match(tokens, next, "had") && match(tokens, next + 1, "not") && match(tokens, next + 2, "been")) {
                int v = next + 3;
                if (v < tokens.size() && VERB_ING.contains(tokens.get(v))) {
                    int adv = parseAdverbial(tokens, v + 1);
                    return adv;
                }
            }
            if (match(tokens, next, "hadn't") && match(tokens, next + 1, "been")) {
                int v = next + 2;
                if (v < tokens.size() && VERB_ING.contains(tokens.get(v))) {
                    int adv = parseAdverbial(tokens, v + 1);
                    return adv;
                }
            }
        }
        return pos;
    }

    private static int parseYesNoQuestion(List<String> tokens, int pos) {
        if (match(tokens, pos, "had")) {
            int next = parseSubject(tokens, pos + 1);
            if (next > pos + 1 && match(tokens, next, "been")) {
                int v = next + 1;
                if (v < tokens.size() && VERB_ING.contains(tokens.get(v))) {
                    int adv = parseAdverbial(tokens, v + 1);
                    if (match(tokens, adv, "?")) return adv + 1;
                }
            }
        }
        return pos;
    }

    private static int parseWhQuestion(List<String> tokens, int pos) {
        if (pos < tokens.size() && WH_WORDS.contains(tokens.get(pos))) {
            if (match(tokens, pos + 1, "had")) {
                int next = parseSubject(tokens, pos + 2);
                if (next > pos + 2 && match(tokens, next, "been")) {
                    int v = next + 1;
                    if (v < tokens.size() && VERB_ING.contains(tokens.get(v))) {
                        int adv = parseAdverbial(tokens, v + 1);
                        if (match(tokens, adv, "?")) return adv + 1;
                    }
                }
            }
        }
        return pos;
    }

    private static int parseSubject(List<String> tokens, int pos) {
        if (pos < tokens.size() && PRONOUNS.contains(tokens.get(pos))) return pos + 1;
        return pos;
    }


    private static int parseAdverbial(List<String> tokens, int pos) {
        if (pos < tokens.size() && ADVERBS.contains(tokens.get(pos))) return pos + 1;
        return pos;
    }

    private static boolean match(List<String> tokens, int pos, String word) {
        return pos < tokens.size() && tokens.get(pos).equals(word);
    }

    static List<String> tokenize(String sentence) {
        List<String> tokens = new ArrayList<>();
        StringBuilder token = new StringBuilder();
        for (char c : sentence.toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                token.append(Character.toLowerCase(c));
            } else {
                if (token.length() > 0) {
                    tokens.add(token.toString());
                    token.setLength(0);
                }
                if (c == '?') tokens.add("?");
            }
        }
        if (token.length() > 0) tokens.add(token.toString());
        return tokens;
    }

    // --- Генерация предложения ---

    public static String generateSentence() {
        // Выбираем случайный тип предложения
        int choice = rnd.nextInt(4);
        switch (choice) {
            case 0: return generateAffirmative();
            case 1: return generateNegative();
            case 2: return generateYesNoQuestion();
            default: return generateWhQuestion();
        }
    }

    private static String generateAffirmative() {
        StringBuilder sb = new StringBuilder();
        sb.append(randomFrom(PRONOUNS)).append(" had been ");
        sb.append(randomFrom(VERB_ING)).append(" ");
        sb.append(generateAdverbial());
        return sb.toString().trim();
    }

    private static String generateNegative() {
        StringBuilder sb = new StringBuilder();
        sb.append(randomFrom(PRONOUNS)).append(" ");
        if (rnd.nextBoolean()) {
            sb.append("had not been ");
        } else {
            sb.append("hadn't been ");
        }
        sb.append(randomFrom(VERB_ING)).append(" ");
        sb.append(generateAdverbial());
        return sb.toString().trim();
    }

    private static String generateYesNoQuestion() {
        StringBuilder sb = new StringBuilder();
        sb.append("had ").append(randomFrom(PRONOUNS)).append(" been ");
        sb.append(randomFrom(VERB_ING)).append(" ");
        sb.append(generateAdverbial());
        sb.append("?");
        return sb.toString().trim();
    }

    private static String generateWhQuestion() {
        StringBuilder sb = new StringBuilder();
        sb.append(randomFrom(WH_WORDS)).append(" had ");
        sb.append(randomFrom(PRONOUNS)).append(" been ");
        sb.append(randomFrom(VERB_ING)).append(" ");
        sb.append(generateAdverbial());
        sb.append("?");
        return sb.toString().trim();
    }

    private static String generateAdverbial() {
        StringBuilder sb = new StringBuilder();
        sb.append(randomFrom(ADVERBS)).append(" ");
        return sb.toString();
    }

    private static String randomFrom(Set<String> set) {
        int index = rnd.nextInt(set.size());
        Iterator<String> it = set.iterator();
        for (int i = 0; i < index; i++) it.next();
        return it.next();
    }

    // --- Тестирование ---

    public static void main(String[] args) {
        String[] examples = {
                "I had been working",
                "She had been working all day",
                "They hadn't been playing",
                "Had you been studying recently ?",
                "What had she been doing ?",
                "I had been running quietly",
                "We had been sleeping"
        };

        System.out.println("=== Проверка распознавания ===");
        for (String example : examples) {
            boolean valid = recognize(example);
            System.out.printf("'%s' -> %s%n", example, valid ? "VALID" : "INVALID");
        }

        System.out.println("\n=== Генерация предложений ===");
        for (int i = 0; i < 10; i++) {
            System.out.println(generateSentence());
        }
    }
}
