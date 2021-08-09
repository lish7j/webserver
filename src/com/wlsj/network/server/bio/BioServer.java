package com.wlsj.network.server.bio;

import com.wlsj.network.dispatcher.bio.BioDispatcher;
import com.wlsj.network.server.Server;
import com.wlsj.network.wrapper.bio.BioSocketWrapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@Slf4j
public class BioServer extends Server {
    private ServerSocket serverSocket;

    private BioDispatcher bioDispatcher;
    private volatile boolean isRunning = true;

    public BioServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            bioDispatcher = new BioDispatcher();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(int port) {
        Socket socket = null;
        while (isRunning) {
            try {

                socket = serverSocket.accept();
                bioDispatcher.doDispatch(new BioSocketWrapper(socket));
            } catch (IOException e) {
                e.printStackTrace();
                close();
            }
        }

    }

    public static void main(String[] args) {
        BioServer bioEndPoint = new BioServer(9090);
        bioEndPoint.start(9);
    }

    @Override
    public void close() {
        isRunning = false;
        bioDispatcher.shutdown();
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public boolean isRunning() {
        return isRunning;
    }
}
