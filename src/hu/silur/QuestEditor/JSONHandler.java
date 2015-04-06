package hu.silur.QuestEditor;

import hu.silur.QuestEditor.Model.Quest;
import hu.silur.QuestEditor.Model.ReqType;
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
            JSONArray jsonArray = (JSONArray) jsonParser.parse(new FileReader(input));
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
                JSONArray reqArray = (JSONArray) o.get("required");
                if (reqArray != null)
                    for (Object reqObject : reqArray) {
                        JSONObject lreqObject = (JSONObject) reqObject;
                        Integer lId, lCount;
                        lId = ((Long) lreqObject.get("id")).intValue();
                        lCount = ((Long) lreqObject.get("count")).intValue();
                        int type = ((Long) lreqObject.get("type")).intValue();
                        type = Math.min(ReqType.values().length, Math.max(0, type)); //clamp the values, so we won't go OOB
                        Quest.Requirement req = new Quest.Requirement(lId, ReqType.values()[type], lCount);
                        lQuest.requirements.add(req);
                    }

                result.add(lQuest);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String makePrettyJson(List<Quest> questList) {
        String s = makeJson(questList).toJSONString();

        StringBuilder sb = new StringBuilder();
        int depth = 0;
        boolean inStr = false;
        boolean newline = false;
        boolean escaped = false;
        for(char c: s.toCharArray()){
            if(newline){
                sb.append("\r\n");
                newline = false;
                for(int i = 0; i < depth; i++){
                    sb.append("  ");
                }
            }



            if(c == '"' && !escaped){
                inStr = !inStr;
            }
            escaped = inStr && c == '\\';

            if(!inStr) {
                if (isOpen(c)) depth++;
                if (isClose(c)) {
                    depth--;
                    sb.append("\r\n");
                    for(int i = 0; i < depth; i++){
                        sb.append("  ");
                    }
                }

                if ((isOpen(c) || c == ',')) {
                    newline = true;
                }
            }
            sb.append(c);
        }

        return sb.toString();
    }

    private static boolean isOpen(char c){
        return c == '{' || c == '[';
    }

    private static boolean isClose(char c){
        return c == '}' || c == ']';
    }

    public static JSONArray makeJson(List<Quest> questList) {
        JSONArray result = new JSONArray();
        for (Quest q : questList) {

            JSONObject questObject = new JSONObject();
            questObject.put("id", q.id);
            questObject.put("title", q.Title);
            questObject.put("description", q.Description);
            questObject.put("ongoing", q.OngoingText);
            questObject.put("onfinished", q.OnfinishedText);
            JSONArray reqs = new JSONArray();
            for (Quest.Requirement req : q.requirements) {
                JSONObject currentReq = new JSONObject();
                currentReq.put("id", req.getId());
                currentReq.put("type", req.getrType().ordinal());
                currentReq.put("count", req.getCount());
                reqs.add(currentReq);
            }
            questObject.put("required", reqs);

//            JSONArray preReqs = new JSONArray();
//            for (Integer p : q.Prerequisites) {
//                preReqs.add(p);
//            }
            JSONArray givers = new JSONArray();
            givers.addAll(q.QuestGivers);
            questObject.put("questgivers",givers);
            result.add(questObject);

        }

        return result;
    }
}


