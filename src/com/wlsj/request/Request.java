package com.wlsj.request;

import com.wlsj.constant.CharConstant;
import com.wlsj.constant.CharsetProperties;
import com.wlsj.context.ServletContext;
import com.wlsj.context.WebApplication;
import com.wlsj.cookie.Cookie;
import com.wlsj.enumeration.RequestMethod;
import com.wlsj.exception.RequestInvalidException;
import com.wlsj.exception.RequestParseException;
import com.wlsj.network.handler.AbstractRequestHandler;
import com.wlsj.request.dispatcher.RequestDispatcher;
import com.wlsj.request.dispatcher.impl.ApplicationRequestDispatcher;
import com.wlsj.session.HttpSession;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

@Getter
@Setter
@Slf4j
public class Request {
    private AbstractRequestHandler requestHandler;
    private RequestMethod requsetMethod;
    private String url;
    private Map<String, List<String>> params;
    private Map<String, List<String>> headers;
    private Map<String, Object> attributes;
    private ServletContext servletContext;
    private Cookie[] cookies;
    private HttpSession session;

    public String getParameter(String key) {
        List<String> parameters = params.get(key);
        if (parameters == null) {
            return null;
        }
        return parameters.get(0);
    }

    public Request(byte[] data) throws RequestParseException, RequestInvalidException, IOException {
        this.attributes = new HashMap<>();
        String[] lines = null;
        try {
            lines = URLDecoder.decode(new String(data, CharsetProperties.UTF_8), CharsetProperties.UTF_8_CHARSET.toString()).split(CharConstant.CRLF);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (lines.length <= 1) {
           /* lines = new String[2];
            lines[0] = "GET / HTTP/1.1";
            lines[1] = "Accept-Language: zh-cn";
            System.out.println("56 Request :" );
            if (lines[0] != null)
                System.out.print(lines[0]);
         */

            throw new RequestInvalidException();
        }
        try {
            parseHeaders(lines);
            if (headers.containsKey("Content-Length") && !headers.get("Content-Length").get(0).equals("0")) {
                parseBody(lines[lines.length - 1]);
            }
        } catch (Throwable e) {
            e.printStackTrace();
            throw new RequestParseException();
        }
        WebApplication.getServletContext().afterRequestCreated(this);


    }

    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    public RequestDispatcher getRequestDispatcher(String url) {
        return new ApplicationRequestDispatcher(url);
    }

    public HttpSession getSession(boolean createIfNotExists) {
        if (session != null)
            return session;
        for (Cookie cookie : cookies) {
            if (cookie.getKey().equals("JSESSIONID")) {
                HttpSession currnetSession = servletContext.getSession(cookie.getValue());
                if (currnetSession != null) {
                    session = currnetSession;
                    return session;
                }
            }
        }
        if (!createIfNotExists)
            return null;
        session = servletContext.createSession(requestHandler.getResponse());
        return session;
    }

    public HttpSession getSession() {
        return getSession(true);
    }

    public String getServletPath() {
        return url;
    }

    public void parseHeaders(String[] lines) {
        String firstLine = lines[0];
        String[] firstLineSlice = firstLine.split(CharConstant.BLANK);
        this.requsetMethod = RequestMethod.valueOf(firstLineSlice[0]);

        String rawUrl = firstLineSlice[1];
        String[] urlSlice = rawUrl.split("\\?");
        this.url = urlSlice[0];

        if (urlSlice.length > 1) {
            parseParams(urlSlice[1]);
        }

        String header;
        this.headers = new HashMap<>();
        for (int i = 1; i < lines.length; i++) {
            header = lines[i];
            if (header.equals("")) {
                break;
            }
            int colonIndex = header.indexOf(":");
            String key = header.substring(0, colonIndex);
            String[] values = header.substring(colonIndex + 2).split(",");
            headers.put(key, Arrays.asList(values));
        }

        if (headers.containsKey("Cookie")) {
            String[] rawCookies = headers.get("Cookie").get(0).split("; ");
            this.cookies = new Cookie[rawCookies.length];
            for (int i = 0; i < rawCookies.length; i++) {
                String[] kv = rawCookies[i].split("=");
                this.cookies[i] = new Cookie(kv[0], kv[1]);
            }
            headers.remove("Cookie");
        } else {
            this.cookies = new Cookie[0];
        }


    }

    public void parseBody(String body) throws UnsupportedEncodingException{
        byte[] bytes = body.getBytes(CharsetProperties.UTF_8_CHARSET);
        List<String> lengths = this.headers.get("Content-Length");
        if (lengths != null) {
            int length = Integer.parseInt(lengths.get(0));
            parseParams(new String(bytes, 0, Math.min(length, bytes.length), CharsetProperties.UTF_8).trim());

        } else {
            parseParams(body.trim());
        }
        if (this.params == null) {
            params = new HashMap<>();
        }

    }

    private void parseParams(String paramString) {
        String[] urlParams = paramString.split("&");

        if (this.params == null) {
            this.params = new HashMap<>();
        }
        for (String param : urlParams) {
            String[] keyValue = param.split("=");
            String key = keyValue[0];
            String[] values = keyValue[1].split(",");
            this.params.put(key, Arrays.asList(values));
        }
    }

    public AbstractRequestHandler getRequestHandler() {
        return requestHandler;
    }

    public void setRequestHandler(AbstractRequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    public RequestMethod getRequsetMethod() {
        return requsetMethod;
    }

    public void setRequsetMethod(RequestMethod requsetMethod) {
        this.requsetMethod = requsetMethod;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, List<String>> getParams() {
        return params;
    }

    public void setParams(Map<String, List<String>> params) {
        this.params = params;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, List<String>> headers) {
        this.headers = headers;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public Cookie[] getCookies() {
        return cookies;
    }

    public void setCookies(Cookie[] cookies) {
        this.cookies = cookies;
    }

    public void setSession(HttpSession session) {
        this.session = session;
    }
}
