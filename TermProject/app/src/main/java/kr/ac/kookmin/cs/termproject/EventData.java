package kr.ac.kookmin.cs.termproject;

public class EventData {
    private String name;
    private int id;
    private int type;

    public EventData(int id, String Name, int type){
        this.name = Name;
        this.id = id;
        this.type = type;
    }

    public int getId() { return this.id; }

    public String getName(){
        return this.name;
    }

    public int getType() { return this.type; }
}
