package com.wlsj.request.dispatcher.impl;

import com.wlsj.constant.CharsetProperties;
import com.wlsj.exception.ResourceNotFoundException;
import com.wlsj.exception.base.ServletException;
import com.wlsj.request.Request;
import com.wlsj.request.dispatcher.RequestDispatcher;
import com.wlsj.resource.ResourceHandler;
import com.wlsj.response.Response;
import com.wlsj.template.TemplateResolver;
import com.wlsj.util.IOUtil;
import com.wlsj.util.MimeTypeUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.Charset;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class ApplicationRequestDispatcher implements RequestDispatcher {
    private String url;

    public ApplicationRequestDispatcher() {
    }

    public ApplicationRequestDispatcher(String url) {
        this.url = url;
    }

    @Override
    public void forward(Request request, Response response) throws ServletException, IOException {

        if (ResourceHandler.class.getResource(url) == null) {
            System.out.println("ApplicationRequestDispatcher : " + url);
            throw new ResourceNotFoundException();
        }
        System.out.println("ApplicationRequestDispatcher forword url is " + url);
        String body = new String(IOUtil.getBytesFromFile(url), CharsetProperties.UTF_8_CHARSET);
        response.setContentType("text/html;charset=utf-8");
        response.setBody(body.getBytes(CharsetProperties.UTF_8_CHARSET));
    }


}
