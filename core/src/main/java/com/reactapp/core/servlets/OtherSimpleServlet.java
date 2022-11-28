package com.reactapp.core.servlets;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_RESOURCE_TYPES;
import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_METHODS;
import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_EXTENSIONS;

@Component(service = Servlet.class, property = {
                SLING_SERVLET_METHODS + "=" + HttpConstants.METHOD_GET,
                SLING_SERVLET_RESOURCE_TYPES + "=" + "servlet/test",
                SLING_SERVLET_EXTENSIONS + "=" + "json"

}, immediate = true)
public class OtherSimpleServlet extends SlingAllMethodsServlet {

        private static final long serialVersionUID = 1L;

        @Override
        protected void doGet(final SlingHttpServletRequest req,
                        final SlingHttpServletResponse resp) throws ServletException, IOException {
                resp.getWriter().write("Test");
        }
}