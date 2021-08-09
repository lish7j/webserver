package com.wlsj.resource;

import com.wlsj.constant.CharsetProperties;
import com.wlsj.exception.ResourceNotFoundException;
import com.wlsj.exception.base.ServletException;
import com.wlsj.exception.handler.ExceptionHandler;
import com.wlsj.network.wrapper.SocketWrapper;
import com.wlsj.request.Request;
import com.wlsj.exception.RequestParseException;
import com.wlsj.response.Response;
import com.wlsj.template.TemplateResolver;
import com.wlsj.util.IOUtil;
import com.wlsj.util.MimeTypeUtil;

import java.io.IOException;


public class ResourceHandler {
    private ExceptionHandler exceptionHandler;

    public ResourceHandler(ExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    public void handle(Request request, Response response, SocketWrapper wrapper) {
        String url = request.getUrl();
        try {

            if (ResourceHandler.class.getResource(url) == null) {
                System.out.println("ResourceHandler 找不到该资源：" + url);
                throw new ResourceNotFoundException();
            }
            byte[] body = IOUtil.getBytesFromFile(url);
            if (url.endsWith(".html")) {
                body = TemplateResolver.resolve(new String(body, CharsetProperties.UTF_8_CHARSET), request)
                                       .getBytes(CharsetProperties.UTF_8_CHARSET);
            }
            response.setContentType(MimeTypeUtil.getTypes(url));
            response.setBody(body);
        } catch (IOException e) {
            exceptionHandler.handle(new RequestParseException(), response, wrapper);
        } catch (ServletException e) {
            exceptionHandler.handle(e, response, wrapper);
        }
    }
}
