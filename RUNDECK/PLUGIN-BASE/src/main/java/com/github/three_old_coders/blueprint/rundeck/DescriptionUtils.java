package com.github.three_old_coders.blueprint.rundeck;

import com.dtolabs.rundeck.plugins.util.DescriptionBuilder;
import com.dtolabs.rundeck.plugins.util.PropertyBuilder;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;

public class DescriptionUtils
{
    private DescriptionUtils()
    {
    }

    public static void addProperties(final DescriptionBuilder descriptionBuilder, final JSONObject jo)
    {
        final JSONArray args = jo.getJSONArray("args");
        for (int i=0; i < args.length(); i++) {
            final JSONObject joi = args.getJSONObject(i);
            if (joi.has("isOption")) {
                descriptionBuilder.property(
                        PropertyBuilder.builder().title(joi.getString("shortestName"))
                                .string(joi.getString("label").replace("<", "").replace(">", ""))
                                .description(joi.getString("descriptions"))
                                .defaultValue(joi.has("defaultValue") ? joi.getString("defaultValue") : null)
                                .required(joi.getBoolean("required"))
                                .build()
                );
            } else if (joi.has("isPositional")) {
                descriptionBuilder.property(
                        PropertyBuilder.builder().title(joi.getString("label"))
                                .string("arg " + joi.getInt("index") + " " + joi.getString("label").replace("<", "").replace(">", ""))
                                .description(joi.getString("descriptions"))
                                .defaultValue(joi.has("defaultValue") ? joi.getString("defaultValue") : null)
                                .required(joi.getBoolean("required"))
                                .build()
                );
            }
        }
    }
}
