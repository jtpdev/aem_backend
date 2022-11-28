package com.reactapp.core.servlets;

import com.reactapp.core.core.ServletConstants;
import com.reactapp.core.enums.Categoria;
import com.reactapp.core.models.Produto;
import com.reactapp.core.services.ProdutoService;
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
        SLING_SERVLET_RESOURCE_TYPES + "=" + ServletConstants.PRODUTO

}, immediate = true)
public class ProdutoServlet extends DefaultServlet<Produto> {

    @Reference
    private ProdutoService produtoService;

    private static final long serialVersionUID = 1L;

    @Override
    protected void find(final SlingHttpServletRequest request,
                        final SlingHttpServletResponse response) throws ServletException, IOException {

        String possibleId = request.getParameter("id");

        if (isInteger(possibleId)) {
            Produto produto = produtoService.find(Integer.valueOf(possibleId));
            String json = toJson(produto);
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
                        } else if (isCategoria(param[1])) {
                            params.put(param[0], Categoria.from(param[1]));
                        } else {
                            params.put(param[0], param[1]);
                        }
                    }
                }
            }
            List<Produto> produtos = produtoService.list(params);
            String json = toJson(produtos);
            response.getWriter().write(json);
        }
    }

    @Override
    protected void update(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        String possibleId = request.getParameter("id");

        if (isInteger(possibleId)) {
            Produto produto = produtoService.update(Integer.valueOf(possibleId), getBody(request));
            String json = toJson(produto);
            response.getWriter().write(json);
        } else {
            response.getWriter().write(writeMessage(NOT_ID_TO_UPDATE));
        }
    }

    @Override
    protected void save(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        Produto produto = produtoService.save(getBody(request));
        String json = toJson(produto);
        response.getWriter().write(json);
    }

    @Override
    protected void delete(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        String possibleId = request.getParameter("id");

        if (isInteger(possibleId)) {
            produtoService.delete(Integer.valueOf(possibleId));
            response.getWriter().write(writeMessage(DELETE_ENTITY));
        } else {
            response.setStatus(400);
            response.getWriter().write(writeMessage(NOT_ID_TO_UPDATE));
        }
    }

    private boolean isCategoria(String value) {
        return !Arrays.stream(Categoria.values())
                .filter(c -> c.name().equals(value))
                .collect(Collectors.toList())
                .isEmpty();
    }

}