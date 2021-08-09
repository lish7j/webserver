package com.wlsj.response;

import com.wlsj.cookie.Cookie;
import com.wlsj.enumeration.HttpStatus;
import com.wlsj.network.handler.AbstractRequestHandler;

import java.io.*;
import java.nio.ByteBuffer;
import java.security.cert.CRL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.wlsj.constant.CharsetProperties.UTF_8_CHARSET;
import static com.wlsj.constant.ContextConstant.DEFAULT_CONTENT_TYPE;
import static com.wlsj.constant.CharConstant.BLANK;
import static com.wlsj.constant.CharConstant.CRLF;
import static com.wlsj.constant.CharsetProperties.UTF_8;

public class Response {
    private StringBuilder headerAppender;
    private List<Cookie> cookies;
    private List<Header> headers;
    private HttpStatus status = HttpStatus.OK;
    private String contentType = DEFAULT_CONTENT_TYPE;
    private byte[] body = new byte[0];
    private AbstractRequestHandler requestHandler;

    public Response() {
        this.headerAppender = new StringBuilder();
        this.cookies = new ArrayList<>();
        this.headers = new ArrayList<>();
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public void addCookie(Cookie cookie) {
        this.cookies.add(cookie);
    }

    public void addHeader(Header header) {
        this.headers.add(header);
    }

    private void buildHeader() {
        headerAppender.append("HTTP/1.1").append(BLANK).append(status.getCode()).append(BLANK).append(status).append(CRLF);
        headerAppender.append("Date: ").append(new Date()).append(CRLF);
        headerAppender.append("Content-type: ").append(contentType).append(CRLF);
        if (headers != null) {
            for (Header header : headers) {
                headerAppender.append(header.getKey()).append(":").append(BLANK).append(header.getValue()).append(CRLF);
            }
        }

        if (cookies.size() > 0) {
            for (Cookie cookie : cookies) {
                headerAppender.append("Set-Cookie: ").append(cookie.getKey()).append("=").append(cookie.getValue()).append(CRLF);
            }
        }
        headerAppender.append("Content-Length: ");
    }

    private void buildBody() {
        this.headerAppender.append(body.length).append(CRLF).append(CRLF);
    }

    private void buildResponse() {
        buildHeader();
        buildBody();
    }

    public ByteBuffer[] getResponseByteBuffer() {
        buildResponse();
        byte[] header = this.headerAppender.toString().getBytes(UTF_8_CHARSET);
        ByteBuffer[] response = {ByteBuffer.wrap(header), ByteBuffer.wrap(body)};
        return response;
    }

    public byte[] getResponseBytes() {
        buildResponse();
        byte[] header = this.headerAppender.toString().getBytes(UTF_8_CHARSET);
        byte[] response = new byte[header.length + body.length];
        System.arraycopy(header, 0, response, 0, header.length);
        System.arraycopy(body, 0, response, header.length, body.length);


        return response;
    }

    public void setRequestHandler(AbstractRequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }



}
