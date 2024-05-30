package com.github.three_old_coders.blueprint.spring;

import com.github.three_old_coders.blueprint.spring.app.SimpleJavaClassB_using_SimpleJavaClassA;
import com.github.three_old_coders.blueprint.spring.app.SpringBeanA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrivatePluginService
{
    @Autowired private SpringBeanA _sba;
    @Autowired private SimpleJavaClassB_using_SimpleJavaClassA _hypeMan;

    public void doSomething()
    {
        System.out.println("PrivatePluginService " + _sba.giveMeAnAAAAA() + " " + _sba);

        _hypeMan.letsGo("Hey1!");
    }
}
