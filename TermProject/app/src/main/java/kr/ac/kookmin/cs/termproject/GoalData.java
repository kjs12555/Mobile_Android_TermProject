package kr.ac.kookmin.cs.termproject;

import android.os.Parcelable;

import java.util.ArrayList;

public class GoalData {
    private String goalName;
    private ArrayList<GoalSave> datas;
    private int start;
    private int end;

    public GoalData(String goalName, ArrayList<GoalSave> datas, int start, int end){
        this.goalName = goalName;
        this.datas = datas;
        this.start = start;
        this.end = end;
    }

    public String getGoalName(){
        return goalName;
    }

    public ArrayList<GoalSave> getDatas() { return datas; }

    public int getStart() { return start; }

    public int getEnd() { return end; }
}
