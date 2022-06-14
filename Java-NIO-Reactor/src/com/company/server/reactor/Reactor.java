package com.company.server.reactor;

import com.company.properties.Properties;
import com.company.server.eventHandler.EventHandler;

import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Reactor {
    private final Map<Integer, EventHandler> registeredHandlers = new ConcurrentHashMap<>();
    private final Selector selector;

    public Reactor() throws Exception {
        selector = Selector.open();
    }

    public Selector getSelector() {
        return selector;
    }

    public void registerEventHandler(int eventType, EventHandler eventHandler) {
        registeredHandlers.put(eventType, eventHandler);
    }

    public void registerChannel(int eventType, SelectableChannel channel) throws Exception {
        channel.register(selector, eventType);
    }

    public void run() throws Exception {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(()->{
            try{
                loopHandles();
            }catch (Exception exception){
                exception.printStackTrace();
            }
        }, 0, Properties.serverLoopDelayMillis, TimeUnit.MILLISECONDS);
    }

    private void loopHandles() throws Exception {
        selector.select();

        Set<SelectionKey> readyHandlers = selector.selectedKeys();
        Iterator<SelectionKey> handleIterator = readyHandlers.iterator();

        while (handleIterator.hasNext()) {
            SelectionKey handle = handleIterator.next();

            if (handle.isAcceptable()) {
                EventHandler handler = registeredHandlers.get(SelectionKey.OP_ACCEPT);
                handler.handleEvent(handle);
            }

            if (handle.isReadable()) {
                EventHandler handler = registeredHandlers.get(SelectionKey.OP_READ);
                handler.handleEvent(handle);
                handleIterator.remove();
            }

            if (handle.isWritable()) {
                EventHandler handler = registeredHandlers.get(SelectionKey.OP_WRITE);
                handler.handleEvent(handle);
                handleIterator.remove();
            }
        }

    }

}
