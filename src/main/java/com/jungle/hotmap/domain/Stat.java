package com.jungle.hotmap.domain;

import javax.persistence.*;

public class Stat {
    private Long time;

    private Double latitude;

    private Double longitude;

    /**
     * @return time
     */
    public Long getTime() {
        return time;
    }

    /**
     * @param time
     */
    public void setTime(Long time) {
        this.time = time;
    }

    /**
     * @return latitude
     */
    public Double getLatitude() {
        return latitude;
    }

    /**
     * @param latitude
     */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    /**
     * @return longitude
     */
    public Double getLongitude() {
        return longitude;
    }

    /**
     * @param longitude
     */
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Stat{" +
                "time=" + time +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}