package com.reactapp.core.servlets;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;
import org.apache.sling.api.servlets.HttpConstants;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

@Component(service = { Servlet.class })
@SlingServletResourceTypes(resourceTypes = "/bin/other-test", methods = HttpConstants.METHOD_GET, extensions = "txt")
public class OtherTestServlet extends SlingAllMethodsServlet {

        @Override
        protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
                        throws ServletException, IOException {
                response.getWriter().write("Test");
        }
}
