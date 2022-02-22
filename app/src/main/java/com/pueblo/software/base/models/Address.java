package com.pueblo.software.base.models;

import java.io.Serializable;

public class Address implements Serializable {

    private static final long serialVersionUID = -6258097696620376642L;


    public Long id;
    public String street;
    public String buildingNr;
    public String flatNr;
    public String city;
    public String postalCode;
    public Double lat;
    public Double lng;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getBuildingNr() {
        return buildingNr;
    }

    public void setBuildingNr(String buildingNr) {
        this.buildingNr = buildingNr;
    }

    public String getFlatNr() {
        return flatNr;
    }

    public void setFlatNr(String flatNr) {
        this.flatNr = flatNr;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }


    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    @Override
    public String toString() {
        String string = street + " " + buildingNr;

        if (flatNr != null) {
            string += "/" + flatNr;
        }
        string += " " + postalCode + " " + city;

        return string;
    }
}