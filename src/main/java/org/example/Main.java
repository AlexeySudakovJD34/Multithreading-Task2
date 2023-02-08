package org.example;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();
    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 1000; i++) {
            Runnable threadBody = () -> {
                String route = generateRoute("RLRFR", 100);
                int rightCommands = rightCounter(route);
                System.out.println(route + " -> количество команд R = " + rightCommands);

                // работаем с Map
                synchronized (sizeToFreq) {
                    if (sizeToFreq.containsKey(rightCommands)) {
                        sizeToFreq.replace(rightCommands, sizeToFreq.get(rightCommands) + 1);
                    } else {
                        sizeToFreq.put(rightCommands, 1);
                    }
                }
            };
            Thread thread = new Thread(threadBody);
            thread.start();
            thread.join();
        }
        // для упрощения делаем допущение, что в коллекции есть один максимум по значениям
        Integer maxKey = Collections.max(sizeToFreq.entrySet(), Map.Entry.comparingByValue()).getKey();
        System.out.println("Самое частое количество повторений " + maxKey
                + " встретилось " + sizeToFreq.get(maxKey) + " раз(а)");
        System.out.println("Другие размеры:");
        for (Integer key : sizeToFreq.keySet()) {
            if (key != maxKey) {
                System.out.println("- " + key + " (" + sizeToFreq.get(key) + " раз)");
            }
        }
    }
    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }
    public static int rightCounter(String route) {
        int counter = 0;
        for (int i = 0; i < route.length(); i++) {
            if (route.charAt(i) == 'R') {
                counter++;
            }
        }
        return counter;
    }
}

