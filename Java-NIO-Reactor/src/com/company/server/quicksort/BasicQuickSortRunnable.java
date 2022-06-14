package com.company.server.quicksort;

import com.company.properties.Properties;

import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class BasicQuickSortRunnable implements QuickSort, Runnable, Registrable{

    private final Selector selector;
    private final SocketChannel socketChannel;
    private int[] integerArray;

    public BasicQuickSortRunnable(Selector selector, SocketChannel socketChannel, int[] integerArray) {
        this.selector = selector;
        this.socketChannel = socketChannel;
        this.integerArray = integerArray;
    }

    @Override
    public void quicksort(int[] array, int left, int right) {
        if(left>=right){
            return;
        }

        int index = partition(array, left, right);
        quicksort(array, left, index-1);
        quicksort(array, index, right);
    }

    public void printType() {
        System.out.println("executing basic quicksort");
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

    String runQuickSort(int[] integerArray) {
        long start = System.nanoTime();
        quicksort(integerArray, 0, integerArray.length-1);
        long finish = System.nanoTime();
        long timeElapsed = finish - start;
        System.out.println("Elapsed time: " + timeElapsed);
        try{
            Thread.sleep(20_000);
        }catch(Exception e){
            e.printStackTrace();
        }
        return Arrays.toString(integerArray);
    }
}
