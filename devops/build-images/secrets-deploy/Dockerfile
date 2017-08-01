FROM gcr.io/cloud-builders/gsutil:latest
RUN apt-get update && apt-get install -y curl \
    && curl -SL https://github.com/mikefarah/yaml/releases/download/1.11/yaml_linux_amd64 -o /builder/yaml \
    && apt-get remove --purge -y curl && apt-get --purge -y autoremove && apt-get clean && rm -rf /var/lib/apt/lists/*
ADD deploy-secrets.sh /builder/deploy-secrets.sh
CMD ['bash','/builder/deploy-secrets.sh']