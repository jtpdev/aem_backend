package com.reactapp.core.services.impl;

import com.reactapp.core.daos.NotaFiscalDao;
import com.reactapp.core.models.NotaFiscal;
import com.reactapp.core.services.NotaFiscalService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.List;
import java.util.Map;

@Component(service = NotaFiscalService.class, immediate = true)
public class NotaFiscalServiceImpl implements NotaFiscalService {

    @Reference
    private NotaFiscalDao notaFiscalDao;

    @Override
    public NotaFiscal find(Integer id) {
        return notaFiscalDao.findById(id);
    }

    @Override
    public List<NotaFiscal> list(Map<String, Object> params) {
        return notaFiscalDao.list(params);
    }

    @Override
    public NotaFiscal save(NotaFiscal model) {
        return notaFiscalDao.save(model);
    }

    @Override
    public NotaFiscal update(Integer id, NotaFiscal model) {
        model.setId(id);
        return notaFiscalDao.update(model);
    }

    @Override
    public void delete(Integer id) {
        notaFiscalDao.delete(id);
    }
}
