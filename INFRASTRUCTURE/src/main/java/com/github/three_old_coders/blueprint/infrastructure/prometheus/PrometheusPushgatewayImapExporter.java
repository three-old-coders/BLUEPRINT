package com.github.three_old_coders.blueprint.infrastructure.prometheus;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.prometheus.client.Predicate;
import io.prometheus.client.exporter.BasicAuthHttpConnectionFactory;
import io.prometheus.client.exporter.HttpConnectionFactory;
import lombok.SneakyThrows;
import org.apache.commons.lang.StringUtils;

import javax.mail.*;
import javax.mail.search.FlagTerm;
import java.io.FileReader;
import java.io.Reader;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class PrometheusPushgatewayImapExporter
{
    private final MeterRegistry _mr;
    private final Properties _ps;

    // prometheus objects
    @SneakyThrows
    public PrometheusPushgatewayImapExporter(final Map<String, String> envMap, final Properties ps)
    {
        _ps = ps;

        final PrometheusMeterRegistry pmr = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);

        final String processName = PrometheusPushgatewayImapExporter.class.getSimpleName();
        final URL url = new URL("http://localhost/pushgateway");
        final HttpConnectionFactory httpcf = new BasicAuthHttpConnectionFactory("admin", "admin");

        // ---- NON GENERIC PART

        final PrometheusPushgatewayJmxSupport ppgjmxs = new PrometheusPushgatewayJmxSupport(pmr, processName, envMap, url, httpcf);
        _mr = ppgjmxs.bindAndStart();
    }

    public void readPostboxesAndPushMetrics()
    {
        for (int index=1; index < 30; index++) {
            final String mailUrl = _ps.getProperty(index + ".url");
            URI uri = null;
            if (null != mailUrl) {
                try {
                    uri = new URI(mailUrl);
                } catch (final Exception e) {
                    System.err.println("invalid mail url [" + mailUrl + "]");
                    e.printStackTrace();
                }
            }

            if (null != uri) {
                // 1.url=imap://support%40gd-inside.de@f-gd.de
                // 1.user=username@xyz.com
                // 1.password=password
                // 1.folder=INBOX
                final String username = _ps.getProperty(index + ".user");
                final String password = _ps.getProperty(index + ".password");
                final String folder = _ps.getProperty(index + ".folder");

                final Properties connectionProperties = new Properties();
                for (int propertiesIndex = 1; propertiesIndex < 20; propertiesIndex++) {
                    // 1.properties.key_1=mail.imap.starttls.enable:true
                    // 1.properties.value_1=true
                    final String key = _ps.getProperty(index + ".properties.key_" + propertiesIndex);
                    if (null != key) {
                        final String value = _ps.getProperty(index + ".properties.value_" + propertiesIndex);
                        connectionProperties.put(key, value);
                    }
                }

                final LocalDate ldToday = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                final Predicate<Message> pm = message -> {
                    final LocalDate ldMail;
                    try {
                        ldMail = message.getReceivedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    } catch (final MessagingException e) {
                        // we ignore the message for our day gauge
                        return false;
                    }
                    return ldMail.equals(ldToday);
                };

                final Session session = Session.getDefaultInstance(connectionProperties);

                try (final Store store = session.getStore(uri.getScheme())) {
                    store.connect(uri.getHost(), uri.getPort(), username, password);

                    try (final Folder inbox = store.getFolder(folder)) {
                        inbox.open(Folder.READ_ONLY);

                        for (int metricsIndex = 1; metricsIndex < 20; metricsIndex++) {
                            // 1.metric_1=UNREAD
                            // 1.metric_2=READ
                            final String metrics = StringUtils.trimToEmpty(_ps.getProperty(index + ".metric_" + metricsIndex));
                            if (null != metrics) {
                                if ("UNREAD".equalsIgnoreCase(metrics)) {
                                    final Message[] unseenMessages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
                                    final AtomicInteger c1 = new AtomicInteger(unseenMessages.length);
                                    final Gauge gauge1 = Gauge
                                            .builder(mailUrl, c1, value -> c1.get())
                                            .description("unread messages")
                                            .tags("TYPE", "unread", "PERIOD", "all")
                                            .register(_mr);
                                    System.out.println(mailUrl + " unread: " + c1.get());

                                    final List<Message> unseenMessagesToday = Arrays.stream(unseenMessages).filter(pm::test).collect(Collectors.toList());
                                    final AtomicInteger c2 = new AtomicInteger(unseenMessagesToday.size());
                                    final Gauge gauge2 = Gauge
                                            .builder(mailUrl, c2, value -> c2.get())
                                            .description("unread messages (today)")
                                            .tags("TYPE", "unread", "PERIOD", "today")
                                            .register(_mr);
                                    System.out.println(mailUrl + " unread (today): " + c2.get());
                                } else if ("READ".equalsIgnoreCase(metrics)) {
                                    final Message[] seenMessages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), true));
                                    final AtomicInteger c1 = new AtomicInteger(seenMessages.length);
                                    final Gauge gauge1 = Gauge
                                            .builder(mailUrl, c1, value -> c1.get())
                                            .description("read messages")
                                            .tags("TYPE", "read", "PERIOD", "all")
                                            .register(_mr);
                                    System.out.println(mailUrl + " read: " + c1.get());

                                    final List<Message> seenMessagesToday = Arrays.stream(seenMessages).filter(pm::test).collect(Collectors.toList());
                                    final AtomicInteger c2 = new AtomicInteger(seenMessagesToday.size());
                                    final Gauge gauge2 = Gauge
                                            .builder(mailUrl, c2, value -> c2.get())
                                            .description("read messages (today)")
                                            .tags("TYPE", "read", "PERIOD", "today")
                                            .register(_mr);
                                    System.out.println(mailUrl + " read (today): " + c2.get());
                                }
                            }
                        }
                    } catch (final Exception e) {
                        System.err.println("unable to open [mailUrl] inbox");
                        e.printStackTrace();
                    }
                } catch (final Exception e) {
                    System.err.println("unable to open [mailUrl]");
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     *
     * @param args  0: path to config
     */
    @SneakyThrows
    public static void main(final String[] args)
    {
        final Properties ps = new Properties();
        try (final Reader r = new FileReader(args[0], StandardCharsets.UTF_8)) {
            ps.load(r);
        } catch (final Exception e) {
            throw new IllegalStateException("unable to load config [" + (null != args && args.length > 0 ? args[0] : "?") + "]");
        }

        final Map<String, String> envMap = new HashMap<>();
        for (int i = 1; i < args.length; i += 2) {
            envMap.put(args[i], args[i+1]);
        }

        final PrometheusPushgatewayImapExporter ppgwie = new PrometheusPushgatewayImapExporter(envMap, ps);
        ppgwie.readPostboxesAndPushMetrics();

        System.exit(0);
    }
}
