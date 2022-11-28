package com.reactapp.core.daos;

import com.reactapp.core.models.Cliente;
import com.reactapp.core.services.ClienteService;
import org.osgi.service.component.annotations.Reference;

import java.util.List;

public interface ClienteDao extends DefaultDao<Cliente> {}
