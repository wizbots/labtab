package org.wizbots.labtab.model;

import java.util.Comparator;

/**
 * Created by ashish on 9/2/17.
 */

public class LocationResponse {

    String id;
    String name;

    public LocationResponse() {
    }

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

    public static Comparator<LocationResponse> locationResponseComparator
            = new Comparator<LocationResponse>() {

        public int compare(LocationResponse loc1, LocationResponse loc2) {

            String locationName1 = loc1.getName();
            String locationName2 = loc2.getName();

            //ascending order
            return locationName1.compareTo(locationName2);

            //descending order
            //return fruitName2.compareTo(fruitName1);
        }

    };
}
