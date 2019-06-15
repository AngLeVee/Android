package com.udacity.sandwichclub.utils;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    public static Sandwich parseSandwichJson(String json)
    {
        Sandwich sandwich = new Sandwich();
        JSONObject data;

        //Catch JSONException in general and for all items that may be empty
        try{
            data = new JSONObject(json);

            JSONObject name = data.getJSONObject("name");
            sandwich.setMainName(name.getString("mainName"));

            try{
                JSONArray temp = name.getJSONArray("alsoKnownAs");
                if (temp.length() == 0 && temp.get(0).toString().equals(""))
                    sandwich.setAlsoKnownAs(null);
                else {
                    List<String> tempList = convertJSONArrayToList(temp);
                    sandwich.setAlsoKnownAs(tempList);
                }
            } catch (JSONException e){}

            try{ sandwich.setPlaceOfOrigin(data.getString("placeOfOrigin"));
            } catch (JSONException e) {}

            sandwich.setDescription(data.getString("description"));
            sandwich.setImage(data.getString("image"));

            JSONArray temp = data.getJSONArray("ingredients");
            List<String> tempList = convertJSONArrayToList(temp);
            sandwich.setIngredients(tempList);
        } catch (JSONException e) { e.printStackTrace(); }

        return sandwich;
    }

    /**
     * Convert a JSONArray into a list of strings
     * @return List<String>
     */
    private static List<String> convertJSONArrayToList(JSONArray array) {
        List<String> list = new ArrayList<>();
        try {
            for (int i = 0; i < array.length(); i++)
                list.add(array.getString(i));
        } catch (JSONException e) { e.printStackTrace(); }

        return list;
    }
}
