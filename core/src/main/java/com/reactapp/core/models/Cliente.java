package com.reactapp.core.models;

import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Model(adaptables = Resource.class)
public class Cliente extends DefaultModel {

    @Expose
    private String nome;

}
