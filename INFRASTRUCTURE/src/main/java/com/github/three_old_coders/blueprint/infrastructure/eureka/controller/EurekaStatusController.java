package com.github.three_old_coders.blueprint.infrastructure.eureka.controller;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.shared.Application;
import com.netflix.eureka.EurekaServerContextHolder;
import com.netflix.eureka.registry.PeerAwareInstanceRegistry;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.List;

@Controller
public class EurekaStatusController
{
    @RequestMapping("/")
    public String root()
    {
        return "redirect:/status";
    }

    @RequestMapping(value = "/status", method = RequestMethod.GET)
    @ResponseBody
    public String getStatus()
    {
        return getStatusInternally(null).toString();
    }

    @RequestMapping("/status/{applicationName}")
    @ResponseBody
    public String getStatus(final @PathVariable String applicationName)
    {
        return getStatusInternally(applicationName).toCompactString();
    }

    //
    // ----->> PRIVATE
    //

    private JSONObject getStatusInternally(final String applicationName)
    {
        final PeerAwareInstanceRegistry registry = EurekaServerContextHolder.getInstance().getServerContext().getRegistry();
        final JSONArray jaApps = new JSONArray();

        final List<Application> applications;
        if (null == applicationName) {
            applications = registry.getApplications().getRegisteredApplications();
        } else {
            final Application application = registry.getApplication(applicationName.toUpperCase());
            applications = null == application ? Collections.emptyList() : Collections.singletonList(application);
        }

        for (final Application ra : applications) {
            final JSONArray jaInstances = new JSONArray();
            final JSONObject joApp = new JSONObject("name", ra.getName(), "instances", jaInstances);
            jaApps.put(joApp);

            for (final InstanceInfo ii : ra.getInstances()) {
                final JSONObject joInstance = new JSONObject("status", ii.getStatus().toString(), "id", ii.getInstanceId(),
                                                             "hostname", ii.getHostName(), "ip", ii.getIPAddr(), "port", ii.getPort());
                jaInstances.put(joInstance);
            }
        }

        return new JSONObject("applications", jaApps);
    }
}
