package com.reactapp.core.services.impl;

import com.reactapp.core.daos.ProdutoDao;
import com.reactapp.core.models.Produto;
import com.reactapp.core.services.ProdutoService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.List;
import java.util.Map;

@Component(service = ProdutoService.class, immediate = true)
public class ProdutoServiceImpl implements ProdutoService {

    @Reference
    private ProdutoDao produtoDao;

    @Override
    public Produto find(Integer id) {
        return produtoDao.findById(id);
    }

    @Override
    public List<Produto> list(Map<String, Object> params) {
        return produtoDao.list(params);
    }

    @Override
    public Produto save(Produto model) {
        return produtoDao.save(model);
    }

    @Override
    public Produto update(Integer id, Produto model) {
        model.setId(id);
        return produtoDao.update(model);
    }
    @Override
    public void delete(Integer id) {
        produtoDao.delete(id);
    }
}
