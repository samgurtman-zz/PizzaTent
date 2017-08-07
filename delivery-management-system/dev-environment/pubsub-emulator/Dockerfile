FROM google/cloud-sdk:alpine
RUN apk --update --no-cache add openjdk8-jre && gcloud components install --quiet beta pubsub-emulator && mkdir -p /var/pubsub
VOLUME /var/lib/pubsub
EXPOSE 8086
CMD [ "gcloud", "beta", "emulators", "pubsub", "start", "--data-dir=/var/lib/pubsub", "--host-port=0.0.0.0:8086", "--log-http", "--verbosity=debug", "--user-output-enabled" ]