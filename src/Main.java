import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    public static final BlockingQueue<String> textsQueueA = new ArrayBlockingQueue<>(100);
    public static final BlockingQueue<String> textsQueueB = new ArrayBlockingQueue<>(100);
    public static final BlockingQueue<String> textsQueueC = new ArrayBlockingQueue<>(100);
    public static final int textLength = 100_000;
    public static final int wordsAmount = 10_000;

    public static void main(String[] args) {

        String[] words = new String[wordsAmount];

        Thread queue = new Thread(() -> {
            for (int i = 0; i < words.length; i++) {
                words[i] = generateText("abc", textLength);
                try {
                    textsQueueA.put(words[i]);
                    textsQueueB.put(words[i]);
                    textsQueueC.put(words[i]);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        queue.start();

        Thread queueA = new Thread(() -> calc('a', textsQueueA));
        queueA.start();

        Thread queueB = new Thread(() -> calc('b', textsQueueB));
        queueB.start();

        Thread queueC = new Thread(() -> calc('c', textsQueueC));
        queueC.start();

    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static void calc(char letter, BlockingQueue<String> textsQueue) {
        int counter = 0;
        int counterMax = 0;


        for (int i = 0; i < wordsAmount; i++) {
            try {
                String word = textsQueue.take();
                for (int j = 0; j < word.length(); j++) {
                    if (word.charAt(j) == letter) {
                        counter++;
                    }
                }
                if (counter > counterMax) {

                    counterMax = counter;
                }
                counter = 0;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println("Наибольшее количество букв " + letter + " : " + counterMax);
    }
}