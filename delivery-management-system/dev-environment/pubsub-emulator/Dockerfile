FROM google/cloud-sdk:alpine
RUN apk --update --no-cache add openjdk8-jre curl && gcloud components install --quiet beta pubsub-emulator && mkdir -p /var/pubsub
VOLUME /var/lib/pubsub
EXPOSE 8086
HEALTHCHECK CMD curl --fail http://localhost:8086/ || exit 1

CMD [ "gcloud", "beta", "emulators", "pubsub", "start", "--data-dir=/var/lib/pubsub", "--host-port=0.0.0.0:8086", "--log-http", "--verbosity=debug", "--user-output-enabled" ]