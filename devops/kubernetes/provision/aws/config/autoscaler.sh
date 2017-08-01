CLOUD_PROVIDER=aws
IMAGE=gcr.io/google_containers/cluster-autoscaler:v0.5.4
MIN_NODES=1
MAX_NODES=5
AWS_REGION=ap-southeast-2
GROUP_NAME="nodes.kubes.pizzatent.co.nz"
SSL_CERT_PATH="/etc/ssl/certs/ca-certificates.crt" # (/etc/ssl/certs for gce)

addon=cluster-autoscaler.yml
wget -O ${addon} https://raw.githubusercontent.com/kubernetes/kops/master/addons/cluster-autoscaler/v1.6.0.yaml

sed -i -e "s@{{CLOUD_PROVIDER}}@${CLOUD_PROVIDER}@g" "${addon}"
sed -i -e "s@{{IMAGE}}@${IMAGE}@g" "${addon}"
sed -i -e "s@{{MIN_NODES}}@${MIN_NODES}@g" "${addon}"
sed -i -e "s@{{MAX_NODES}}@${MAX_NODES}@g" "${addon}"
sed -i -e "s@{{GROUP_NAME}}@${GROUP_NAME}@g" "${addon}"
sed -i -e "s@{{AWS_REGION}}@${AWS_REGION}@g" "${addon}"
sed -i -e "s@{{SSL_CERT_PATH}}@${SSL_CERT_PATH}@g" "${addon}"

kubectl replace -f ${addon} -v 10