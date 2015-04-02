package hu.silur.QuestEditor.Model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.List;

/**
 * Created by silur on 02/04/15.
 */
public class Quest {

    public int id;
    public String Title;
    public String Description;
    public String OngoingText;
    public String OnfinishedText;
    public List<Integer> QuestGivers;
    public List<Requirement> requirements;


    public static class Requirement {
        private final IntegerProperty id;
        public final StringProperty type;
        public final IntegerProperty count;

        public int getId() {
            return id.get();
        }

        public void setId(int id) {
            this.id.set(id);
        }

        public String getType() {
            return type.get();
        }

        public void setType(String type) {
            this.type.set(type);
        }

        public int getCount() {
            return count.get();
        }


        public void setCount(int count) {
            this.count.set(count);
        }

        public Requirement(Integer _id, String _type, Integer _count) {
            this.id = new SimpleIntegerProperty(_id);
            this.type = new SimpleStringProperty(_type);
            this.count = new SimpleIntegerProperty(_count);
        }
    }
}
