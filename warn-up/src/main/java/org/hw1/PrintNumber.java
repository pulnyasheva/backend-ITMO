package org.hw1;

public class PrintNumber {
    private static final int COUNT_SYMBOLS = 3;
    private static final int COUNT_NUMBER = 6;
    private volatile static int currentNumber = 1;
    private static final Object objectToLock = new Object();

    public void print() {
        for (int i = 1; i <= COUNT_SYMBOLS; i++) {
            int numberThread = i;
            new Thread(() -> {
                for (int j = 0; j < COUNT_NUMBER; j++) {
                    synchronized (objectToLock) {
                        while (currentNumber != numberThread) {
                            try {
                                objectToLock.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        System.out.print(numberThread);
                        int newNumber = (numberThread + 1) % COUNT_SYMBOLS;
                        currentNumber = newNumber == 0 ? COUNT_SYMBOLS : newNumber;
                        objectToLock.notifyAll();
                    }
                }
            }).start();
        }
    }

    public static void main(String[] args) {
        PrintNumber printNumber = new PrintNumber();
        printNumber.print();
    }
}
