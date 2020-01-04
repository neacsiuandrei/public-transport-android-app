package ro.ase.neacsiuandrei.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import ro.ase.neacsiuandrei.MainActivity;

@Entity(tableName = "buses")
public class Bus implements Parcelable {
    @PrimaryKey
    @ColumnInfo(name = "number")
    private int number;
    @ColumnInfo(name = "capacity")
    private int capacity;
    @ColumnInfo(name = "avgDuration")
    private int avgDuration;
    @ColumnInfo(name = "rating")
    private float rating;
    @ColumnInfo(name = "recommend")
    private boolean recommend;
    private int userID = MainActivity.userID;

    public Bus() {
    }

    public Bus(int number, int capacity, int avgDuration, float rating, boolean recommend) {
        this.number = number;
        this.capacity = capacity;
        this.avgDuration = avgDuration;
        this.rating = rating;
        this.recommend=recommend;
    }

    @Ignore
    protected Bus(Parcel in) {
        number = in.readInt();
        capacity = in.readInt();
        avgDuration = in.readInt();
        rating = in.readFloat();
        recommend = in.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Bus> CREATOR = new Creator<Bus>() {
        @Override
        public Bus createFromParcel(Parcel in) {
            return new Bus(in);
        }

        @Override
        public Bus[] newArray(int size) {
            return new Bus[size];
        }
    };

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getAvgDuration() {
        return avgDuration;
    }

    public void setAvgDuration(int avgDuration) {
        this.avgDuration = avgDuration;
    }

    @Override
    public String toString() {
        return "Bus{" +
                "number=" + number +
                ", capacity=" + capacity +
                ", avgDuration=" + avgDuration +
                ", rating=" + rating +
                ", recommend=" + recommend +
                '}';
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
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeInt(number);
        parcel.writeInt(capacity);
        parcel.writeInt(avgDuration);
        parcel.writeFloat(rating);
        parcel.writeByte((byte) (recommend ? 1 : 0));
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}

