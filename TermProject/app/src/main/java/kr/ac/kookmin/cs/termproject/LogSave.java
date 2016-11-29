package kr.ac.kookmin.cs.termproject;

/**
 * Created by user on 2016-11-29.
 */

public class LogSave {
    private int id; //0
    private int latitude;   //1
    private int longitude;  //2
    private String cameraPath;  //3
    private String logName; //4
    private String eventName;   //5
    private String Note;    //6
    private String time;   //7
    private int foot;   //8
    private int type;   //9

    public LogSave(int id, int latitude, int longitude, String cameraPath, String logName, String eventName, String note, String time, int foot, int type) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.cameraPath = cameraPath;
        this.logName = logName;
        this.eventName = eventName;
        Note = note;
        this.time = time;
        this.foot = foot;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public int getLatitude() {
        return latitude;
    }

    public int getLongitude() {
        return longitude;
    }

    public String getCameraPath() {
        return cameraPath;
    }

    public String getLogName() {
        return logName;
    }

    public String getEventName() {
        return eventName;
    }

    public String getNote() {
        return Note;
    }

    public String getTime() {
        return time;
    }

    public int getFoot() {
        return foot;
    }

    public int getType() {
        return type;
    }
}
