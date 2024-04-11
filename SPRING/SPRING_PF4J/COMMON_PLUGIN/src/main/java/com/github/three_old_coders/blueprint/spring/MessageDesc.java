package com.github.three_old_coders.blueprint.spring;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

@Accessors(prefix = "_")
@AllArgsConstructor
@ToString
public class MessageDesc implements Serializable
{
    @Serial private static final long serialVersionUID = 1L;

    @Getter @NonNull private final String _type;
}
