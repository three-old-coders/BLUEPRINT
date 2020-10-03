FROM azul/zulu-openjdk-alpine:11

RUN apk update
RUN apk upgrade
RUN apk --no-cache add clamav-daemon freshclam clamav-libunrar supervisor
RUN rm -rf /var/lib/apt/lists/*

# RUN sed -i 's/^Foreground .*$/Foreground true/g' /etc/clamav/clamd.conf &&  echo 'TCPSocket 3310' >> /etc/clamav/clamd.conf && sed -i 's/^Foreground .*$/Foreground true/g' /etc/clamav/freshclam.conf

COPY etc /etc/

# todo: detect file automatically
COPY docker_sb_clamav-FULL.jar app.jar

# todo force via rest interface?
RUN freshclam
RUN mkdir /run/clamav && chown clamav:clamav /run/clamav

# todo: make port configurable, see etc/clamav/clamd.conf
EXPOSE 3316/tcp
CMD ["supervisord", "-c", "/etc/supervisord.conf"]

