package com.company.server.reactor;

import com.company.properties.Properties;
import com.company.server.eventHandler.AcceptEventHandler;
import com.company.server.eventHandler.ReadEventHandler;
import com.company.server.eventHandler.WriteEventHandler;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;

public class ReactorManager {
    private static final int SERVER_PORT = Properties.serverPort;

    public void startReactor(int port) throws Exception{

        ServerSocketChannel server = ServerSocketChannel.open();
        server.socket().bind(new InetSocketAddress(port));
        server.configureBlocking(Properties.isBlocking);

        Reactor reactor = new Reactor();
        reactor.registerChannel(SelectionKey.OP_ACCEPT, server);

        reactor.registerEventHandler(SelectionKey.OP_ACCEPT,
                new AcceptEventHandler(reactor.getSelector()));

        reactor.registerEventHandler(SelectionKey.OP_READ,
                new ReadEventHandler(reactor.getSelector()));

        reactor.registerEventHandler(SelectionKey.OP_WRITE,
                new WriteEventHandler());

        reactor.run();
    }

    public static void main(String[] args) {
        System.out.println("Server started at port : " + SERVER_PORT);
        try {
            new ReactorManager().startReactor(SERVER_PORT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
