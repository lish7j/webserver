package com.wlsj.network.dispatcher.bio;

import com.wlsj.exception.RequestInvalidException;
import com.wlsj.exception.base.ServletException;
import com.wlsj.network.dispatcher.AbstractDispatcher;
import com.wlsj.network.handler.bio.BioRequestHandler;
import com.wlsj.network.wrapper.SocketWrapper;
import com.wlsj.network.wrapper.bio.BioSocketWrapper;
import com.wlsj.request.Request;
import com.wlsj.response.Response;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class BioDispatcher extends AbstractDispatcher {


    @Override
    public void doDispatch(SocketWrapper socketWrapper) {
        BioSocketWrapper bioSocketWrapper = (BioSocketWrapper)socketWrapper;
        Socket socket = bioSocketWrapper.getSocket();
        Request request = null;
        Response response = null;
        try {
            BufferedInputStream bin = new BufferedInputStream(socket.getInputStream());
            byte[] buffer = null;
            try {
                buffer = new byte[bin.available()];
                int len = bin.read(buffer);
                //System.out.println("BioDispatcher " + bin.available());
                //System.out.println("31 lines BioDispatcher request content");
                //System.out.println(new String(buffer, 0, len));
                if (len < 0) {
                    throw new RequestInvalidException();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("39 BioDiapatcher " + new String(buffer));
            System.out.println("39 BioDiapatcher " + Thread.currentThread().getName());
            request = new Request(buffer);
            response = new Response();
            executor.execute(new BioRequestHandler(socketWrapper, servletContext, exceptionHandler, resourceHandler, request, response));
        } catch (ServletException e) {
            exceptionHandler.handle(e, response, socketWrapper);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}