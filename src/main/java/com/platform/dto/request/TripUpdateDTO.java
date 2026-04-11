package com.platform.dto.request;

import com.platform.model.enums.TripStatus;

public class TripUpdateDTO {

    private TripStatus newStatus;
    private String geoPoint;   // for location updates
    private String otp;        // for parcel delivery completion

    public TripStatus getNewStatus() { return newStatus; }
    public void setNewStatus(TripStatus newStatus) { this.newStatus = newStatus; }

    public String getGeoPoint() { return geoPoint; }
    public void setGeoPoint(String geoPoint) { this.geoPoint = geoPoint; }

    public String getOtp() { return otp; }
    public void setOtp(String otp) { this.otp = otp; }
}