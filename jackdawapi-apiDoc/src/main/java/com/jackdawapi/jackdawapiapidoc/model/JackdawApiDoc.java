package com.jackdawapi.jackdawapiapidoc.model;

import lombok.Data;

import java.util.HashMap;

@Data
public class JackdawApiDoc {

    String method;

    String prefix;

    String path;

    HashMap<String,String> header;

    String returnValue;

}
