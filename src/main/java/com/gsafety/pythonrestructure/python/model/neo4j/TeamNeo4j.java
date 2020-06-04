package com.gsafety.pythonrestructure.python.model.neo4j;

import lombok.Data;

@Data
public class TeamNeo4j {

    private String id;
    private String name;
    private String longitude;
    private String latitude;
    private String province;

}