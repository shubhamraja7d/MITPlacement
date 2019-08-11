package com.sr7d.mitplacement;

public class PlacementItem {
    private long placementDate;
    private String placementName;

    public PlacementItem() {
    }

    public PlacementItem(long placementDate, String placementName) {
        this.placementDate = placementDate;
        this.placementName = placementName;
    }

    public long getPlacementDate() {
        return placementDate;
    }

    public void setPlacementDate(long placementDate) {
        this.placementDate = placementDate;
    }

    public String getPlacementName() {
        return placementName;
    }

    public void setPlacementName(String placementName) {
        this.placementName = placementName;
    }
}
