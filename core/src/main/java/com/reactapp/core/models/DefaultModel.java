package com.reactapp.core.models;

import com.google.gson.annotations.Expose;

import lombok.Data;

@Data
public abstract class DefaultModel {

    @Expose
    protected Integer id;
}
