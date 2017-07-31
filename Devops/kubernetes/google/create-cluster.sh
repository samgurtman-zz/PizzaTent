#/bin/sh
gcloud container clusters create pizzatent --zone australia-southeast1-a --additional-zones=australia-southeast1-b \
    --num-nodes=1 --enable-autoscaling --min-nodes=1 --max-nodes=3 --num-nodes=1 --cluster-version=1.7.2 \
    --enable-autorepair --enable-autoupgrade --scopes "https://www.googleapis.com/auth/ndev.clouddns.readwrite"

#init helm
helm init