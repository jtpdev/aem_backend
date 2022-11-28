package com.reactapp.core.servlets;

import com.google.gson.Gson;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

public abstract class DefaultServlet<T> extends SlingAllMethodsServlet {

    public static final String DELETE_ENTITY = "Registro excluído com sucesso";
    public static final String NOT_ID_TO_UPDATE = "Não há id da entidade para ser atualizado";

    protected Gson gson;

    @Override
    public void init() throws ServletException {
        gson = new Gson();
        super.init();
    }

    public T fromJSON(String json) {
        return gson.fromJson(json, getType());
    }

    public String toJson(T obj) {
        return gson.toJson(obj);
    }

    public String toJson(Collection<T> obj) {
        return gson.toJson(obj);
    }

    public String writeMessage(String message){
        return "{ message:" + message + "}";
    }

    public T getBody(HttpServletRequest request) throws IOException {

        String body = null;
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {
                bufferedReader = request.getReader();
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    throw ex;
                }
            }
        }

        body = stringBuilder.toString();
        return fromJSON(body);
    }

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        try {
            find(request, response);
        } catch (Exception e) {
            throwErrorMessage(response, e);
        }
    }

    @Override
    protected void doPut(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        try {
            update(request, response);
        } catch (Exception e) {
            throwErrorMessage(response, e);
        }
    }

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        try {
            save(request, response);
        } catch (Exception e) {
            throwErrorMessage(response, e);
        }
    }

    @Override
    protected void doDelete(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        try {
            delete(request, response);
        } catch (Exception e) {
            throwErrorMessage(response, e);
        }
    }

    private void throwErrorMessage(SlingHttpServletResponse response, Exception e) throws IOException {
        response.getWriter().write(writeMessage("Erro ao realizar essa operação"));
    }

    protected abstract void find(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException;

    protected abstract void save(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException;

    protected abstract void update(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException;

    protected abstract void delete(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException;

    private Type getType() {
        return ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }
}
