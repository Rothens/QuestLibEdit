package hu.silur.QuestEditor;

import hu.silur.QuestEditor.Model.Quest;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static hu.silur.QuestEditor.Model.Quest.*;

public class JSONHandler {

 public static List<Quest> parseJson(File input) {
     List<Quest> result = new ArrayList<>();
     JSONParser jsonParser = new JSONParser();
     try {
         JSONArray jsonArray = (JSONArray)jsonParser.parse(new FileReader(input));
         for (Object anArray : jsonArray) {
             JSONObject o = (JSONObject) anArray;
             Integer id = ((Long) o.get("id")).intValue();
             String title = (String) o.get("title");
             String desc = (String) o.get("description");
             String ong = (String) o.get("ongoing");
             String onf = (String) o.get("onfinished");
             ArrayList<Integer> qgivers = new ArrayList<>();
             ArrayList<Integer> qpreq = new ArrayList<>();
             Object ob = o.get("questgivers");
             if (ob != null && ob instanceof JSONArray) {
                 for (Long aLong : (Iterable<Long>) ob) {
                     qgivers.add(aLong.intValue());
                 }
             }

             ob = o.get("prerequisites");
             if (ob != null && ob instanceof JSONArray) {
                 for (Long aLong : (Iterable<Long>) ob) {
                     qpreq.add(aLong.intValue());
                 }
             }
             Quest lQuest = new Quest(id);
             lQuest.Title = title;
             lQuest.Description = desc;
             lQuest.OngoingText = ong;
             lQuest.OnfinishedText = onf;
             lQuest.QuestGivers = qgivers;
             lQuest.requirements = new ArrayList<>();
             JSONArray reqArray = (JSONArray)o.get("required");
             if (reqArray !=null)
             for (Object reqObject : reqArray) {
                 JSONObject lreqObject = (JSONObject)reqObject;
                 Integer lId, lCount;
                 String lType;
                 lId = ((Long)lreqObject.get("id")).intValue();
                 lCount=((Long)lreqObject.get("count")).intValue();
                 switch (((Long)lreqObject.get("type")).intValue()) {
                     case 0:
                         lType = "Kill";
                         break;
                     case 1:
                         lType = "Gather";
                         break;
                     case 2:
                         lType = "Use";
                         break;
                     case 3:
                         lType = "Visit";
                         break;
                     case 4:
                         lType = "Talk";
                         break;
                     default:
                         lType = "<ERROR>";
                 }


                 Quest.Requirement req = new Quest.Requirement(lId,lType,lCount);
                 lQuest.requirements.add(req);
             }

             result.add(lQuest);
         }
     } catch (IOException | ParseException e) {
         e.printStackTrace();
     }
     return result;
 }

}
