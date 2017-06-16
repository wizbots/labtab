package org.wizbots.labtab.model;

/**
 * Created by ashish on 8/5/17.
 */

public class Nuggests {
    String name;
    boolean isCheck;


    public Nuggests(String name, boolean isCheck) {
        this.name = name;
        this.isCheck = isCheck;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }


    @Override
    public boolean equals(Object obj) {
        String name = ((Nuggests) obj).getName();
        return this.name.trim().equalsIgnoreCase(name);
    }


}
