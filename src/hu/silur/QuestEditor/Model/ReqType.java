package hu.silur.QuestEditor.Model;

/**
 * Created by Rothens on 2015.04.04..
 */
public enum ReqType {
    KILL,
    GATHER,
    USE,
    VISIT,
    TALK;

    public String getName(){
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}
