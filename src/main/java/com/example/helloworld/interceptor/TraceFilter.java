package com.example.helloworld.interceptor;

import io.micrometer.tracing.CurrentTraceContext;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class TraceFilter implements Filter {

    private static final String TRACE_ID_HEADER_NAME = "traceparent";
    public static final String DEFAULT = "00";
    public static final String X_TRACE_ID = "x-trace-id";
    public static final String X_SPAN_ID = "x-span-id";
    private final Tracer tracer;

    public TraceFilter(Tracer tracer) {
        this.tracer = tracer;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        if (request.getHeader(TRACE_ID_HEADER_NAME)!= null) {
            response.setHeader(TRACE_ID_HEADER_NAME, request.getHeader(TRACE_ID_HEADER_NAME));
            String traceId = request.getHeader(X_TRACE_ID);
            String spanId = request.getHeader(X_SPAN_ID);
            response.setHeader(X_TRACE_ID, traceId);
            response.setHeader(X_SPAN_ID, spanId);
        }
        if (!response.getHeaderNames().contains(TRACE_ID_HEADER_NAME)) {
            if(Optional.of(tracer).map(Tracer::currentTraceContext).map(CurrentTraceContext::context).isEmpty()) {
                chain.doFilter(req, res);
                return;
            }
            var context = tracer.currentTraceContext().context();
            var traceId = context.traceId();
            var parentId = context.spanId();
            var traceparent = DEFAULT + "-" + traceId + "-" + parentId + "-" + DEFAULT;
            response.setHeader(TRACE_ID_HEADER_NAME, traceparent);
            response.setHeader(X_TRACE_ID, traceId);
            response.setHeader(X_SPAN_ID, context.spanId());
        }
        chain.doFilter(req, res);
    }
}