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
            final String defaultValue = joi.has("defaultValue") ? joi.getString("defaultValue") : null;
            final boolean required = joi.getBoolean("required");
            final String description = joi.getString("descriptions");
            if (joi.has("isOption")) {
                final String classType = joi.getString("type");
                final String name = joi.getString("shortestName");
                if ("boolean".equals(classType)) {
                    descriptionBuilder.booleanProperty(name, defaultValue, required, name, description).build();
                } else {
                    descriptionBuilder.property(
                        PropertyBuilder.builder()
                            .title(name).string(joi.getString("label").replace("<", "").replace(">", ""))
                            .description(description)
                            .defaultValue(defaultValue)
                            .required(required)
                            .build());
                }
            } else if (joi.has("isPositional")) {
                descriptionBuilder.property(
                        PropertyBuilder.builder().title(joi.getString("label"))
                                .string("arg " + joi.getInt("index") + " " + joi.getString("label").replace("<", "").replace(">", ""))
                                .description(description)
                                .defaultValue(defaultValue)
                                .required(required)
                                .build()
                );
            }
        }
    }
}
