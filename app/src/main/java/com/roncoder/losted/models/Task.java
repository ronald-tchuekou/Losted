package com.roncoder.losted.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Task implements Parcelable {
    private String message;
    private boolean finish;

    public Task() {
    }

    public Task(String message, boolean finish) {
        this.message = message;
        this.finish = finish;
    }

    protected Task(Parcel in) {
        message = in.readString();
        finish = in.readByte() != 0;
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isFinish() {
        return finish;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }

    @Override
    @NonNull
    public String toString() {
        return "Task{" +
                "message='" + message + '\'' +
                ", finish=" + finish +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(message);
        parcel.writeByte((byte) (finish ? 1 : 0));
    }
}
