package com.wlsj.context;


import com.wlsj.context.holder.FilterHolder;
import com.wlsj.context.holder.ServletHolder;
import com.wlsj.cookie.Cookie;
import com.wlsj.exception.FilterNotFoundException;
import com.wlsj.exception.ServletNotFoundException;
import com.wlsj.filter.Filter;
import com.wlsj.listener.HttpSessionListener;
import com.wlsj.listener.ServletContextListener;
import com.wlsj.listener.ServletRequestListener;
import com.wlsj.listener.event.HttpSessionEvent;
import com.wlsj.listener.event.ServletContextEvent;
import com.wlsj.listener.event.ServletRequestEvent;
import com.wlsj.request.Request;
import com.wlsj.response.Response;
import com.wlsj.servlet.Servlet;
import com.wlsj.session.HttpSession;
import com.wlsj.session.IdleSessionCleaner;
import com.wlsj.util.UUIDUtil;
import com.wlsj.util.XMLUtil;
import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.util.AntPathMatcher;
import static com.wlsj.constant.ContextConstant.DEFAULT_SERVLET_ALIAS;
import static com.wlsj.constant.ContextConstant.DEFAULT_SESSION_EXPIRE_TIME;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class ServletContext {
    /*
      servlet别名 -> 类名
     */
    private Map<String, ServletHolder> servlets;
    /*
        url -> servlet别名
     */
    private Map<String, String> servletMapping;
    /*
      filter别名 -> 类名
     */
    private Map<String, FilterHolder> filters;
    /*
      url -> filter1
          -> filter2
          -> ..
          一对多模式
     */
    private Map<String, List<String>> filterMapping;

    /*
     监听器
     */
    private List<ServletContextListener> servletContextListeners;
    private List<HttpSessionListener> httpSessionListeners;
    private List<ServletRequestListener> servletRequestListeners;

    /*
       属性值
     */
    private Map<String, Object> attributes;

    /*
      应用sessions
     */
    private Map<String, HttpSession> sessions;

    /*
    路径匹配
     */
    private AntPathMatcher matcher;

    private IdleSessionCleaner idleSessionCleaner;

    public ServletContext() throws IllegalAccessException, InstantiationException, ClassNotFoundException{
        init();
    }

    public Servlet mapServlet(String url) throws ServletNotFoundException {
        String servletAlias = servletMapping.get(url);
        if (servletAlias != null) {
            return initAndGetServlet(servletAlias);
        }

        List<String> matchingPatterns = new ArrayList<>();
        Set<String> patterns = servletMapping.keySet();
        for (String pattern : patterns) {
            if (matcher.match(pattern, url)) {
                matchingPatterns.add(pattern);
            }
        }

        if (!matchingPatterns.isEmpty()) {
            Comparator<String> patternComparator = matcher.getPatternComparator(url);
            Collections.sort(matchingPatterns, patternComparator);
            String bestMatch = matchingPatterns.get(0);
            return initAndGetServlet(bestMatch);
        }

        return initAndGetServlet(DEFAULT_SERVLET_ALIAS);
    }

    private Servlet initAndGetServlet(String servletAlias) throws ServletNotFoundException {
        ServletHolder servletHolder = servlets.get(servletAlias);
        if (servletHolder == null) {
            throw new ServletNotFoundException();
        }

        if (servletHolder.getServlet() == null) {
            try {
                Servlet servlet = (Servlet)Class.forName(servletHolder.getServletClass()).newInstance();
                servlet.init();
                servletHolder.setServlet(servlet);
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return servletHolder.getServlet();
    }

    public List<Filter> mapFilter(String url) throws FilterNotFoundException {
        List<String> matchingPatterns = new ArrayList<>();
        Set<String> patterns = filterMapping.keySet();
        for (String pattern : patterns) {
            if (matcher.match(pattern, url)) {
                matchingPatterns.add(pattern);
            }
        }

        Set<String> filterAlias = matchingPatterns.stream().flatMap(pattern -> this.filterMapping.get(pattern).stream()).
                collect(Collectors.toSet());
        List<Filter> filters = new ArrayList<>();
        for (String alias : filterAlias) {
            filters.add(initAndGetFilter(alias));
        }
        return filters;
    }

    private Filter initAndGetFilter(String filterAlias) throws FilterNotFoundException {
        FilterHolder filterHolder = filters.get(filterAlias);
        if (filterHolder == null) {
            throw new FilterNotFoundException();
        }

        if (filterHolder.getFilter() == null) {
            try {
                Filter filter = (Filter) Class.forName(filterHolder.getFileterClass()).newInstance();
                filter.init();
                filterHolder.setFilter(filter);
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return filterHolder.getFilter();
    }

    /*
    应用初始化
     */
    public void init() throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        this.servlets = new HashMap<>();
        this.servletMapping = new HashMap<>();
        this.filters = new HashMap<>();
        this.filterMapping = new HashMap<>();
        this.servletContextListeners = new ArrayList<>();
        this.httpSessionListeners = new ArrayList<>();
        this.servletRequestListeners = new ArrayList<>();
        this.attributes = new HashMap<>();
        this.sessions = new HashMap<>();
        this.matcher = new AntPathMatcher();
        this.idleSessionCleaner = new IdleSessionCleaner();
        this.idleSessionCleaner.start();
        this.parseConfig();
        ServletContextEvent servletContextEvent = new ServletContextEvent(this);
        for (ServletContextListener servletContextListener : servletContextListeners) {
            servletContextListener.contextInitialized(servletContextEvent);
        }
    }

    /*
     destroy
     */
    public void destroy() {
        servlets.values().forEach(servletHolder -> {
            if (servletHolder.getServlet() != null) {
                servletHolder.getServlet().destroy();
            }
        });
        filters.values().forEach(filterHolder -> {
            if (filterHolder.getFilter() != null) {
                filterHolder.getFilter().destroy();
            }
        });
        ServletContextEvent servletContextEvent = new ServletContextEvent(this);
        for (ServletContextListener servletContextListener : servletContextListeners) {
            servletContextListener.contextDestroyed(servletContextEvent);
        }
    }
    /*
     parse web.xml
     */
    private void parseConfig() throws ClassNotFoundException, InstantiationException, IllegalAccessException{
//        Document doc = null;
//        try {
//            InputStream configFile = ServletContext.class.getResourceAsStream("/WEB-INF/web.xml");
//            doc = XMLUtil.getDocument(configFile);
//            //doc = XMLUtil.getDocument(new FileInputStream(new File("D:\\代码\\webserver\\src\\WEB-INF\\web.xml")));
//        } catch (IOException e) {
//            e.printStackTrace();
//
//        }
        InputStream configFile = ServletContext.class.getResourceAsStream("/WEB-INF/web.xml");
        Document doc = XMLUtil.getDocument(configFile);

        Element root = doc.getRootElement();

        List<Element> servletElements = root.elements("servlet");
        for (Element servlet : servletElements) {
            String key = servlet.element("servlet-name").getText();
            String value = servlet.element("servlet-class").getText();
            this.servlets.put(key, new ServletHolder(value));
        }

        List<Element> servletMappingElements = root.elements("servlet-mapping");
        for (Element servletMapping : servletMappingElements) {
            List<Element> urlPatterns = servletMapping.elements("url-pattern");
            String value= servletMapping.element("servlet-name").getText();
            for (Element url : urlPatterns) {
                this.servletMapping.put(url.getText(), value);
            }
        }

        List<Element> filters = root.elements("filter");
        for (Element filter : filters) {
            String key = filter.element("filter-name").getText();
            String value = filter.element("filter-class").getText();
            this.filters.put(key, new FilterHolder(value));
        }

        List<Element> filterMappingElement = root.elements("filter-mapping");
        for (Element filterMapping : filterMappingElement) {
            List<Element> urlPatterns = filterMapping.elements("url-pattern");
            String value = filterMapping.element("filter-name").getText();
            for (Element url : urlPatterns) {
                List<String> values = this.filterMapping.get(url.getText());
                if (values == null) {
                    values = new ArrayList<>();
                    this.filterMapping.put(url.getText(), values);
                }
                values.add(value);
            }
        }

        Element listener = root.element("listener");
        List<Element> listenerElements = root.elements("listener-class");
        for (Element listenerElement : listenerElements) {
            EventListener eventListener = (EventListener)Class.forName(listenerElement.getText()).newInstance();
            if (eventListener instanceof  ServletContextListener) {
                servletContextListeners.add((ServletContextListener)eventListener);
            }
            if (eventListener instanceof ServletRequestListener) {
                servletRequestListeners.add((ServletRequestListener)eventListener);
            }
            if (eventListener instanceof HttpSessionListener) {
                httpSessionListeners.add((HttpSessionListener)eventListener);
            }
        }
    }

    public HttpSession getSession(String JESESSIONID) {
        return sessions.get(JESESSIONID);
    }

    public HttpSession createSession(Response response) {
        HttpSession httpSession = new HttpSession(UUIDUtil.uuid());
        sessions.put(httpSession.getId(), httpSession);
        response.addCookie(new Cookie("JSESSIONID", httpSession.getId()));
        HttpSessionEvent httpSessionEvent = new HttpSessionEvent(httpSession);
        for (HttpSessionListener listener : httpSessionListeners) {
            listener.sessionCreated(httpSessionEvent);
        }
        return httpSession;
    }

    public void invalidSession(HttpSession httpSession) {
        sessions.remove(httpSession.getId());
        afterSessionDestroyed(httpSession);
    }

    public void cleanIdleSessions() {
        for (Iterator<Map.Entry<String, HttpSession>> it = sessions.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, HttpSession> entry = it.next();
            if (Duration.between(entry.getValue().getLastAccessed(), Instant.now()).getSeconds() >= DEFAULT_SESSION_EXPIRE_TIME) {
                afterSessionDestroyed(entry.getValue());
                it.remove();
            }
        }
    }

    private void afterSessionDestroyed(HttpSession httpSession) {
        HttpSessionEvent httpSessionEvent = new HttpSessionEvent(httpSession);
        for (HttpSessionListener listener : httpSessionListeners) {
            listener.sessionDestroyed(httpSessionEvent);
        }
    }

    public void afterRequestCreated(Request request) {
        ServletRequestEvent requestEvent = new ServletRequestEvent(this, request);
        for (ServletRequestListener listener : servletRequestListeners) {
            listener.requestInitialized(requestEvent);
        }
    }

    public void afterRequestDestroyed(Request request) {
        ServletRequestEvent requestEvent = new ServletRequestEvent(this, request);
        for (ServletRequestListener listener : servletRequestListeners) {
            listener.requestDestoyed(requestEvent);
        }
    }

    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    public void serAttribute(String key, Object value) {
        attributes.put(key, value);
    }



}
