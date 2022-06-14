package com.company.server.quicksort;

import com.company.properties.Properties;

import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ParallelQuickSortRunnable implements QuickSort, Runnable, Registrable {

    private final Selector selector;
    private final SocketChannel socketChannel;
    private int[] integerArray;

    public ParallelQuickSortRunnable(Selector selector, SocketChannel socketChannel, int[] integerArray) {
        this.selector = selector;
        this.socketChannel = socketChannel;
        this.integerArray = integerArray;

    }

    public void printType() {
        System.out.println("executing parallel quicksort");
    }

    @Override
    public void run() {
        String arrString = runQuickSort(integerArray);

        try {
            printType();
            register(arrString, socketChannel, selector);
        } catch (ClosedChannelException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void quicksort(int[] array, int left, int right) {
        ParallelQuickSort parallelQuickSort = new ParallelQuickSort(left, right, array);
        parallelQuickSort.quicksort(array, left, right);
    }

    String runQuickSort(int[] integerArray) {
        long start = System.nanoTime();
        quicksort(integerArray, 0, integerArray.length-1);
        long finish = System.nanoTime();
        long timeElapsed = finish - start;
        System.out.println("Elapsed time: " + timeElapsed);
        return Arrays.toString(integerArray);
    }
}
