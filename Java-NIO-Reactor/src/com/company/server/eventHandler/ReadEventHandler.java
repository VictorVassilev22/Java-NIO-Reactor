package com.company.server.eventHandler;

import com.company.properties.Command;
import com.company.properties.Properties;
import com.company.server.quicksort.BasicQuickSortRunnable;
import com.company.server.quicksort.ParallelQuickSortRunnable;

import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReadEventHandler implements EventHandler {

    private final Selector selector;
    public ReadEventHandler(Selector selector) {
        this.selector = selector;
    }

    @Override
    public void handleEvent(SelectionKey handle) throws Exception {

        ByteBuffer inputBuffer = ByteBuffer.allocate(Properties.BYTE_SIZE);
        SocketChannel socketChannel = (SocketChannel) handle.channel();
        ExecutorService executorService = Executors.newFixedThreadPool(Properties.clientThreads);

        Thread.sleep(Properties.serverMessageDelayMillis);

        socketChannel.read(inputBuffer);
        inputBuffer.flip();
        byte[] buffer = new byte[inputBuffer.limit()];
        inputBuffer.get(buffer);

        final String commandString = new String(buffer);
        System.out.println("Executing " + commandString);
        inputBuffer.flip();

        final String[] commands = commandString.split(" ", 2);
        String integersArrayString = null;
        Command sortType = null;
        int[] integerArray = null;

        try {
            sortType = Command.valueOf(commands[0].toUpperCase());
            integersArrayString = commands[1];
            integerArray = parseToIntegerArray(integersArrayString);
        } catch (Exception exception) {
            sortType = Command.INVALID;
        }

        switch (sortType) {
            case BASIC:
                executorService.execute(new BasicQuickSortRunnable(selector,socketChannel, integerArray));
                break;
            case PARALLEL:
                executorService.execute(new ParallelQuickSortRunnable(selector,socketChannel, integerArray));
                break;
            default:
                sendError(socketChannel);
                break;
        }
    }

    static int[] parseToIntegerArray(String str) {
        String[] splitArray = str.split(", ");
        int[] array = new int[splitArray.length];
        for (int i = 0; i < splitArray.length; i++) {
            array[i] = Integer.parseInt(splitArray[i]);
        }
        return array;
    }

    private void sendError(SocketChannel socketChannel) throws ClosedChannelException {
        final String message = "Wrong input!";
        System.out.println(message);
        ByteBuffer inputBuffer = ByteBuffer.allocate(Properties.BYTE_SIZE);
        inputBuffer.put(message.getBytes(StandardCharsets.UTF_8));
        inputBuffer.flip();

        socketChannel.register(
                selector, SelectionKey.OP_WRITE, inputBuffer);
    }
}
