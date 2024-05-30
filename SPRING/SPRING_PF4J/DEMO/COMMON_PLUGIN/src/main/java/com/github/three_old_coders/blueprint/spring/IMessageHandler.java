package com.github.three_old_coders.blueprint.spring;

import org.pf4j.ExtensionPoint;

public interface IMessageHandler
    extends ExtensionPoint
{
    boolean canHandle(final MessageDesc message);
}
