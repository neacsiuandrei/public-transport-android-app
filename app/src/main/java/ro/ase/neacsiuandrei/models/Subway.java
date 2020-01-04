package ro.ase.neacsiuandrei.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import ro.ase.neacsiuandrei.MainActivity;

@Entity(tableName = "subways")
public class Subway implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int id = 0;
    private String destination;
    private int capacity;
    private String subwayLine;
    private int numberOfStations;
    private float rating;
    private boolean recommend;
    private int userID = MainActivity.userID;

    public Subway(){

    }

    public Subway(String destination, int capacity, String subwayLine, int numberOfStations, float rating, boolean recommend) {
        this.destination = destination;
        this.capacity = capacity;
        this.subwayLine = subwayLine;
        this.numberOfStations = numberOfStations;
        this.rating = rating;
        this.recommend = recommend;
    }

    public Subway(int id, String destination, int capacity, String subwayLine, int numberOfStations, float rating, boolean recommend) {
        this.id = id;
        this.destination = destination;
        this.capacity = capacity;
        this.subwayLine = subwayLine;
        this.numberOfStations = numberOfStations;
        this.rating = rating;
        this.recommend = recommend;
    }

    protected Subway(Parcel in) {
        destination = in.readString();
        capacity = in.readInt();
        subwayLine = in.readString();
        numberOfStations = in.readInt();
        rating = in.readFloat();
        recommend = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(destination);
        dest.writeInt(capacity);
        dest.writeString(subwayLine);
        dest.writeInt(numberOfStations);
        dest.writeFloat(rating);
        dest.writeByte((byte) (recommend ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Subway> CREATOR = new Creator<Subway>() {
        @Override
        public Subway createFromParcel(Parcel in) {
            return new Subway(in);
        }

        @Override
        public Subway[] newArray(int size) {
            return new Subway[size];
        }
    };

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getSubwayLine() {
        return subwayLine;
    }

    public void setSubwayLine(String subwayLine) {
        this.subwayLine = subwayLine;
    }

    public int getNumberOfStations() {
        return numberOfStations;
    }

    public void setNumberOfStations(int numberOfStations) {
        this.numberOfStations = numberOfStations;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public boolean isRecommend() {
        return recommend;
    }

    public void setRecommend(boolean recommend) {
        this.recommend = recommend;
    }

    @Override
    public String toString() {
        return "Subway{" +
                "destination='" + destination + '\'' +
                ", capacity=" + capacity +
                ", subwayLine='" + subwayLine + '\'' +
                ", numberOfStations=" + numberOfStations +
                ", rating=" + rating +
                ", recommend=" + recommend +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
