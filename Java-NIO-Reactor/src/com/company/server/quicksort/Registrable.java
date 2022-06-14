package com.company.server.quicksort;

import com.company.properties.Properties;

import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public interface Registrable {

    default void register(String message, SocketChannel socketChannel, Selector selector) throws ClosedChannelException {

        ByteBuffer resultBuffer = ByteBuffer.allocateDirect(Properties.BYTE_SIZE);
        resultBuffer.put(message.getBytes(StandardCharsets.UTF_8));
        resultBuffer.flip();

        selector.wakeup();
        socketChannel.register(
                selector, SelectionKey.OP_WRITE, resultBuffer);
    }

}
