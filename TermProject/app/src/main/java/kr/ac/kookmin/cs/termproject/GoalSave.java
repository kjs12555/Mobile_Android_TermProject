package kr.ac.kookmin.cs.termproject;

import java.io.Serializable;

/**
 * Created by user on 2016-11-26.
 */

public class GoalSave implements Serializable{
    private int id;
    private String eventName;
    private int type;
    private int n;

    public GoalSave(int id, String eventName, int type, int n){
        this.id = id;
        this.eventName = eventName;
        this.type = type;
        this.n = n;
    }

    public int getId(){ return id; }

    public String getEventName(){ return eventName; }

    public int getType(){ return type; }

    public int getN(){ return n; }

    public void setType(int type){ this.type = type; }
}
