package com.web.chat.loggingfilter;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.AbstractRequestLoggingFilter;

import javax.servlet.http.HttpServletRequest;

@Component
public class MyFilter extends AbstractRequestLoggingFilter {
    private final ClientInfo info;

    public MyFilter() {
        this.info = new ClientInfo();
    }

    @Override
    protected void beforeRequest(HttpServletRequest request, String message) {

    }

    @Override
    protected void afterRequest(HttpServletRequest request, String message) {
    }
}
