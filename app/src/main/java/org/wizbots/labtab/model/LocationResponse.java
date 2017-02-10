package org.wizbots.labtab.model;

/**
 * Created by ashish on 9/2/17.
 */

public class LocationResponse {

    String id;
    String name;

    public LocationResponse(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
