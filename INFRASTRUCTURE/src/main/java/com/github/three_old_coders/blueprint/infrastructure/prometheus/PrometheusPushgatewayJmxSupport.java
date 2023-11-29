package com.github.three_old_coders.blueprint.infrastructure.prometheus;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.prometheus.client.exporter.HttpConnectionFactory;
import io.prometheus.client.exporter.PushGateway;
import lombok.SneakyThrows;

import javax.management.*;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.URL;
import java.util.Date;
import java.util.Map;
import java.util.TimerTask;

public class PrometheusPushgatewayJmxSupport
{
    public static final String timerServiceDomain = "TimerService";

    private final PrometheusMeterRegistry _pmr;
    private final String _processName;
    private final Map<String, String> _envMap;
    private final URL _url;
    private final HttpConnectionFactory _httpcf;


    private PushGateway _pgw;


    public PrometheusPushgatewayJmxSupport(final PrometheusMeterRegistry pmr,
                                           final String processName, final Map<String, String> envMap,
                                           final URL url, final HttpConnectionFactory httpcf)
    {
        _pmr = pmr;
        _processName = processName;
        _envMap = envMap;
        _url = url;
        _httpcf = httpcf;
    }

    @SneakyThrows
    public CompositeMeterRegistry bindAndStart()
    {
        final CompositeMeterRegistry cmr = new CompositeMeterRegistry(Clock.SYSTEM);
        cmr.add(_pmr);

        _pgw = new PushGateway(_url);
        if (null != _httpcf) {
            _pgw.setConnectionFactory(_httpcf);
        }
        // todo: configurable testOnInit?
        _pgw.push(_pmr.getPrometheusRegistry(), _processName, _envMap);     // test push

        // add meter listener to force a push otherwise meter may get garbage collected before we even had a chance
        // to contact our pushgateway.
        cmr.config().onMeterAdded(meter -> {
            try {
                _pgw.push(_pmr.getPrometheusRegistry(), _processName, _envMap);
            } catch (final Exception e) {
                e.printStackTrace();
            }
        });

        final TimerTask tt = new TimerTask()
        {
            @Override
            public void run()
            {
            try {
                _pgw.push(_pmr.getPrometheusRegistry(), _processName, _envMap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            }
        };

        // ----

        // Get a list of all locally registered MBeanServers
        final MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        // final MBeanServer mbs = MBeanServerFactory.createMBeanServer("TimerServiceServer");
        final ObjectInstance oiTimer = mbs.createMBean(
            "javax.management.timer.Timer",
            new ObjectName(timerServiceDomain, "service", "timer")
        );

        final NotificationListener nl = (notification, handback) -> tt.run();
        final NotificationFilter nf = notification -> true; // false -> ignores the notification

        mbs.addNotificationListener(oiTimer.getObjectName(), nl, nf, null);
        mbs.invoke(oiTimer.getObjectName(), "start", new Object[] {}, new String[] {});
        // public synchronized Integer addNotification(String type, String message, Object userData, Date date, long period)
        mbs.invoke(oiTimer.getObjectName(),
            "addNotification",
            new Object[] {"", "", tt, new Date(), 1000},
            new String[] {String.class.getName(), String.class.getName(), Object.class.getName(), Date.class.getName(), long.class.getName()}
        );

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                mbs.invoke(oiTimer.getObjectName(), "stop", new Object[] {}, new String[] {});
            } catch (final Exception e) {
                throw new IllegalStateException("unable to stop timer", e);
            }
        }));

        // ----

        // track jmx bean creations, no usage yet.
        final NotificationListener nlReg = (notification, handback) -> {
            final MBeanServerNotification nlNBS = (MBeanServerNotification) notification;
            if (MBeanServerNotification.REGISTRATION_NOTIFICATION.equals(nlNBS.getType())) {
                System.out.println("MBean Registered [" + nlNBS.getMBeanName() + "]");
//                try {
//                    _pgw.push(_pmr.getPrometheusRegistry(), _processName, _envMap);     // test push
//                } catch (final Exception e) {
//                    e.printStackTrace();
//                }
            } else if (MBeanServerNotification.UNREGISTRATION_NOTIFICATION.equals(nlNBS.getType())) {
                System.out.println("MBean Unregistered [" + nlNBS.getMBeanName() + "]");
            }
        };

        mbs.addNotificationListener(MBeanServerDelegate.DELEGATE_NAME, nlReg, nf, null);

        return cmr;
    }

    public MeterRegistry getMeterRegistry()
    {
        return _pmr;
    }
}
