package com.github.three_old_coders.blueprint.spring;

import com.github.three_old_coders.blueprint.spring.app.SpringBeanA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrivatePluginService
{
    @Autowired private SpringBeanA _sba;

    public void doSomething()
    {
        System.out.println("PrivatePluginService " + _sba.giveMeAnAAAAA() + " " + _sba);
    }
}
