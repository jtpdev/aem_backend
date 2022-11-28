package com.reactapp.core.models;

import com.google.gson.annotations.Expose;
import com.reactapp.core.enums.Categoria;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Model(adaptables = Resource.class)
public class Produto extends DefaultModel {

    @Expose
    private String nome;
    @Expose
    private Categoria categoria;
    @Expose
    private BigDecimal preco;
}
