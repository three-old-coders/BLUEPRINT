FROM azul/zulu-openjdk-alpine:11

RUN apk --no-cache add clamav-daemon freshclam clamav-libunrar supervisor

# RUN sed -i 's/^Foreground .*$/Foreground true/g' /etc/clamav/clamd.conf &&  echo 'TCPSocket 3310' >> /etc/clamav/clamd.conf && sed -i 's/^Foreground .*$/Foreground true/g' /etc/clamav/freshclam.conf

# todo force via rest interface?
RUN freshclam

RUN mkdir /run/clamav && chown clamav:clamav /run/clamav
COPY etc /etc/

# todo: detect file automatically
COPY docker_sb_clamav-FULL.jar app.jar

# todo: make port configurable, see etc/clamav/clamd.conf
# EXPOSE 3310/tcp
EXPOSE 3316/tcp
CMD ["supervisord", "-c", "/etc/supervisord.conf"]
