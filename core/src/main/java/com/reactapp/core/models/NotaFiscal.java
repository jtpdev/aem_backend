package com.reactapp.core.models;

import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Model(adaptables = Resource.class)
public class NotaFiscal extends DefaultModel {

    @Expose
    private Integer numero;

    @Expose
    private List<Integer> idsProduto;

    @Expose
    private Integer idCliente;

    @Expose
    private BigDecimal valor;
}
