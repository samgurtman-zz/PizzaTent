FROM osrm/osrm-backend:latest
RUN apk add --no-cache curl && mkdir /data  \
  && curl http://download.geofabrik.de/australia-oceania/new-zealand-latest.osm.pbf -o /data/new-zealand-latest.osm.pbf \
  && osrm-extract -p /opt/car.lua /data/new-zealand-latest.osm.pbf && osrm-contract /data/new-zealand-latest.osrm
HEALTHCHECK CMD curl --fail http://localhost:5000/ || exit 1

ENTRYPOINT ['osrm-routed']
CMD ['/data/new-zealand-latest.osrm']