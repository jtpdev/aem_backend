package com.reactapp.core.services.impl;

import com.reactapp.core.daos.ClienteDao;
import com.reactapp.core.models.Cliente;
import com.reactapp.core.services.ClienteService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.List;
import java.util.Map;

@Component(service = ClienteService.class, immediate = true)
public class ClienteServiceImpl implements ClienteService {

    @Reference
    private ClienteDao clienteDao;

    @Override
    public Cliente find(Integer id) {
        return clienteDao.findById(id);
    }

    @Override
    public List<Cliente> list(Map<String, Object> params) {
        return clienteDao.list(params);
    }

    @Override
    public Cliente save(Cliente model) {
        return clienteDao.save(model);
    }

    @Override
    public Cliente update(Integer id, Cliente model) {
        model.setId(id);
        return clienteDao.update(model);
    }

    @Override
    public void delete(Integer id) {
        clienteDao.delete(id);
    }
}
