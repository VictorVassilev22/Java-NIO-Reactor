package com.company.server.eventHandler;

import java.nio.channels.SelectionKey;

public interface EventHandler {
    public void handleEvent(SelectionKey handle) throws Exception;
}
