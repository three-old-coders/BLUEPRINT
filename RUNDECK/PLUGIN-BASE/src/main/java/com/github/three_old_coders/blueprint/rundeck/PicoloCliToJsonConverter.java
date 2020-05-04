package com.github.three_old_coders.blueprint.rundeck;

import org.apache.commons.io.IOUtils;
import org.apache.tapestry5.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PicoloCliToJsonConverter
{
    public static void main(final String[] args) throws Exception
    {
        final File destinationDir = new File(args[0]);
        destinationDir.mkdirs();

        final List<Class<?>> classes = new ArrayList<>();
        for (int i=1; i < args.length; i++) {
            classes.add(Class.forName(args[i]));
        }

        final Map<Class<?>, JSONObject> jsons = PicoliUtils.createJSONsFrom(classes.toArray(new Class<?>[0]));
        for (final JSONObject jo : jsons.values()) {
            final File filename = new File(destinationDir, jo.getString("id"));
            try (final Writer w = new FileWriter(filename)) {
                IOUtils.write(jo.toString(), w);
            }
        }
    }
}
