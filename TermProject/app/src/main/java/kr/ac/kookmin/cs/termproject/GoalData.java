package kr.ac.kookmin.cs.termproject;

import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

public class GoalData implements Serializable {
    private String goalName;
    private ArrayList<GoalSave> datas;
    private String start;
    private String end;

    public GoalData(String goalName, ArrayList<GoalSave> datas, String start, String end) {
        this.goalName = goalName;
        this.datas = datas;
        this.start = start;
        this.end = end;
    }

    public String getGoalName() {
        return goalName;
    }

    public ArrayList<GoalSave> getDatas() {
        return datas;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }
}
