docker-compose up -d

web based auth (admin/admin)
http://traefik.localhost            --> traefik admin ui
    issues

http://prometheus.localhost:9090    --> prometheus ui
    issues
        - missing image version
        - ports opened?
        - localhost:9090 (works, not wanted), prometheus.localhost:9090 (works)
        - menu "status | target" shows wrong endpoint URLs

pushgateway
    issues
        - we do we need expose for internal communication, it's opened anyway?
        - who is allowed to push metrics? Check security in JAVA class and PGW settings
        - not shown in prometheus ui
        - honor_labels do we need it?

grafana
    issuse
        - name of job changed, no dashboard at all.
        - datasource deletion not possible, just deleting the db prior re-starting
