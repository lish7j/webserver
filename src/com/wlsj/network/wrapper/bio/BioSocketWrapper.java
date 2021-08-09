package com.wlsj.network.wrapper.bio;

import com.wlsj.network.wrapper.SocketWrapper;

import java.io.IOException;
import java.net.Socket;

public class BioSocketWrapper implements SocketWrapper {
    private Socket socket;

    public BioSocketWrapper(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
