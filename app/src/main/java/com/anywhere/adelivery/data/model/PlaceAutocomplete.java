package com.anywhere.adelivery.data.model;

/**
 * PlaceAutocomplete class is model class use to handle places data
 */
public class PlaceAutocomplete {

    public String placeId;
    public String placePrimaryText;
    public String description;

    //empty constructor need here to perform gson parsing.
    public PlaceAutocomplete(){
    }

    public PlaceAutocomplete(String placeId, String placePrimaryText, String
            description) {
        this.placeId = placeId;
        this.placePrimaryText=placePrimaryText;
        this.description = description;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(final String placeId) {
        this.placeId = placeId;
    }

    public String getPlacePrimaryText() {
        return placePrimaryText;
    }

    public void setPlacePrimaryText(final String placePrimaryText) {
        this.placePrimaryText = placePrimaryText;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "PlaceAutocomplete{" +
                "placeId=" + placeId +
                ", placePrimaryText=" + placePrimaryText +
                ", description=" + description +
                '}';
    }

    @Override
    public boolean  equals (Object object) {
        boolean result = false;
        if (object == null || object.getClass() != getClass()) {
            result = false;
        } else {
            PlaceAutocomplete contactsModel = (PlaceAutocomplete) object;
            if (this.placeId.equals(contactsModel.getPlaceId())) {
                result = true;
            }
        }
        return result;
    }

}
