package com.wlsj.network.handler.bio;

import com.wlsj.context.ServletContext;
import com.wlsj.context.WebApplication;
import com.wlsj.exception.FilterNotFoundException;
import com.wlsj.exception.ServletNotFoundException;
import com.wlsj.exception.handler.ExceptionHandler;
import com.wlsj.network.dispatcher.AbstractDispatcher;
import com.wlsj.network.handler.AbstractRequestHandler;
import com.wlsj.network.wrapper.SocketWrapper;
import com.wlsj.network.wrapper.bio.BioSocketWrapper;
import com.wlsj.request.Request;
import com.wlsj.resource.ResourceHandler;
import com.wlsj.response.Response;
import com.wlsj.servlet.Servlet;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

@Slf4j
@Setter
@Getter
public class BioRequestHandler extends AbstractRequestHandler {

    public BioRequestHandler(SocketWrapper socketWrapper, ServletContext servletContext, ExceptionHandler exceptionHandler, ResourceHandler resourceHandler,
                             Request request, Response response) throws ServletNotFoundException, FilterNotFoundException {
        super(socketWrapper, servletContext, exceptionHandler, resourceHandler, request, response);
    }

    @Override
    public void flushResponse() {
        isFinished = true;

        BioSocketWrapper bioSocketWrapper = (BioSocketWrapper)socketWrapper;
        byte[] bytes = response.getResponseBytes();
        OutputStream os = null;
        try {
            os = bioSocketWrapper.getSocket().getOutputStream();
            os.write(bytes);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                os.close();
                bioSocketWrapper.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        WebApplication.getServletContext().afterRequestDestroyed(request);
    }
}
