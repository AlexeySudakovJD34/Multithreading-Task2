package org.example;

import java.util.*;

public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();
    public static void main(String[] args) throws InterruptedException {
        List<Thread> threads1 = new ArrayList<>();
        List<Thread> threads2 = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            Thread thread1 = new Thread(() -> {
                String route = generateRoute("RLRFR", 100);
                int rightCommands = rightCounter(route);
                System.out.println(route + " -> количество команд R = " + rightCommands);
                synchronized (sizeToFreq) {
                    if (sizeToFreq.containsKey(rightCommands)) {
                        sizeToFreq.replace(rightCommands, sizeToFreq.get(rightCommands) + 1);
                    } else {
                        sizeToFreq.put(rightCommands, 1);
                    }
                    sizeToFreq.notify();
                }
            });
            threads1.add(thread1);
            thread1.start();

            Thread thread2 = new Thread(() -> {
                while (!Thread.interrupted()) {
                    synchronized (sizeToFreq) {
                        try {
                            sizeToFreq.wait();
                        } catch (InterruptedException e) {
                            return;
                        }
                        int foo = maxKeyCalc(sizeToFreq);
                    }
                }
            });
            threads2.add(thread2);
            thread2.start();
        }
        for (Thread thread1 : threads1) {
            thread1.join();
        }
        for (Thread thread2 : threads2) {
            thread2.interrupt();
        }
        System.out.println();
        Integer maxKey = maxKeyCalc(sizeToFreq);
        System.out.println("Другие размеры:");
        for (Integer key : sizeToFreq.keySet()) {
            if (!key.equals(maxKey)) {
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
    public static Integer maxKeyCalc(Map<Integer, Integer> map) {
        Integer maxKey = Collections.max(map.entrySet(), Map.Entry.comparingByValue()).getKey();
        System.out.println("Самое частое количество повторений " + maxKey
                + " встретилось " + sizeToFreq.get(maxKey) + " раз(а)");
        return maxKey;
    }
}

