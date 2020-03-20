package de._3oc_.t55bs.components;

import de._3oc_.t55bs.services.EBSColorTheme;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;

public class Alert
{
    @Parameter(name="theme", required = true, defaultPrefix = "literal") @Property private EBSColorTheme _theme;
    @Parameter(name="text", defaultPrefix = "literal") @Property private String _text;
}
