package com.nyu.cs9033.eta.models;

import android.os.Parcel;
import android.os.Parcelable;


public class Trip implements Parcelable {

    // Member fields should exist here, what else do you need for a trip?
    // Please add additional fields
    private String name;
    private String origin;
    private String destination;
    private int fare;


    public int getFare() {
        return fare;
    }

    public void setFare(int fare) {
        this.fare = fare;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Parcelable creator. Do not modify this function.
     */
    public static final Parcelable.Creator<Trip> CREATOR = new Parcelable.Creator<Trip>() {
        public Trip createFromParcel(Parcel p) {
            return new Trip(p);
        }

        public Trip[] newArray(int size) {
            return new Trip[size];
        }
    };

    /**
     * Create a Trip model object from a Parcel. This
     * function is called via the Parcelable creator.
     *
     * @param p The Parcel used to populate the
     *          Model fields.
     */
    public Trip(Parcel p) {
        //
        setName(p.readString());
        setOrigin(p.readString());
        setDestination(p.readString());
        setFare(p.readInt());
        // TODO - fill in here
    }

    /**
     * Create a Trip model object from arguments
     *
     * @param name Add arbitrary number of arguments to
     *             instantiate Trip class based on member variables.
     */
    public Trip(String name, String origin, String destination, int Fare) {

        this.name = name;
        this.origin = origin;
        this.destination = destination;
        this.fare = Fare;
        // TODO - fill in here, please note you must have more arguments here
    }

    /**
     * Serialize Trip object by using writeToParcel.
     * This function is automatically called by the
     * system when the object is serialized.
     *
     * @param dest  Parcel object that gets written on
     *              serialization. Use functions to write out the
     *              object stored via your member variables.
     * @param flags Additional flags about how the object
     *              should be written. May be 0 or PARCELABLE_WRITE_RETURN_VALUE.
     *              In our case, you should be just passing 0.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(name);
        dest.writeString(origin);
        dest.writeString(destination);
        dest.writeInt(fare);
        // TODO - fill in here
    }

    /**
     * Feel free to add additional functions as necessary below.
     */

    /**
     * Do not implement
     */
    @Override
    public int describeContents() {
        // Do not implement!
        return 0;
    }
}
