package com.reactapp.core.servlets;

import com.google.gson.Gson;
import com.reactapp.core.core.ServletConstants;
import com.reactapp.core.enums.Categoria;
import com.reactapp.core.models.Cliente;
import com.reactapp.core.services.ClienteService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.reactapp.core.core.utils.ParamUtils.isDecimal;
import static com.reactapp.core.core.utils.ParamUtils.isInteger;
import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_METHODS;
import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_RESOURCE_TYPES;

@Component(service = Servlet.class, property = {
        SLING_SERVLET_METHODS + "=" + HttpConstants.METHOD_GET,
        SLING_SERVLET_METHODS + "=" + HttpConstants.METHOD_POST,
        SLING_SERVLET_METHODS + "=" + HttpConstants.METHOD_PUT,
        SLING_SERVLET_METHODS + "=" + HttpConstants.METHOD_DELETE,
        SLING_SERVLET_RESOURCE_TYPES + "=" + ServletConstants.CLIENTE

}, immediate = true)
public class ClienteServlet extends DefaultServlet<Cliente> {

    @Reference
    private ClienteService clienteService;

    private static final long serialVersionUID = 1L;

    @Override
    protected void find(final SlingHttpServletRequest request,
                        final SlingHttpServletResponse response) throws ServletException, IOException {

        String possibleId = request.getParameter("id");

        if (isInteger(possibleId)) {
            Cliente cliente = clienteService.find(Integer.valueOf(possibleId));
            String json = toJson(cliente);
            response.getWriter().write(json);
        } else {
            Map<String, Object> params = new LinkedHashMap<>();
            if (request.getQueryString() != null) {
                for (String p : request.getQueryString().split("\\?")) {
                    String[] param = p.split("=");
                    if (param.length > 1) {
                        if (isInteger(param[1])) {
                            params.put(param[0], Integer.valueOf(param[1]));
                        } else if (isDecimal(param[1])) {
                            params.put(param[0], new BigDecimal(param[1]));
                        } else {
                            params.put(param[0], param[1]);
                        }
                    }
                }
            }
            List<Cliente> clientes = clienteService.list(params);
            String json = toJson(clientes);
            response.getWriter().write(json);
        }
    }

    @Override
    protected void update(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        String possibleId = request.getParameter("id");

        if (isInteger(possibleId)) {
            Cliente cliente = clienteService.update(Integer.valueOf(possibleId), getBody(request));
            String json = toJson(cliente);
            response.getWriter().write(json);
        } else {
            response.getWriter().write(writeMessage(NOT_ID_TO_UPDATE));
        }
    }

    @Override
    protected void save(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        Cliente cliente = clienteService.save(getBody(request));
        String json = toJson(cliente);
        response.getWriter().write(json);
    }

    @Override
    protected void delete(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        String possibleId = request.getParameter("id");

        if (isInteger(possibleId)) {
            clienteService.delete(Integer.valueOf(possibleId));
            response.getWriter().write(writeMessage(DELETE_ENTITY));
        } else {
            response.setStatus(400);
            response.getWriter().write(writeMessage(NOT_ID_TO_UPDATE));
        }
    }

}