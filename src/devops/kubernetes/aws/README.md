[Please view the full Kops tutorial first](https://github.com/kubernetes/kops/blob/master/docs/aws.md)

Dependencies:
* Kops
* Kubectl
* awc-cli

1. Prepare the AWS Kops Iam user.
    * `. init-iam-kops.sh`
1. Prepare Route 53.
    * `. init-route-53.sh ${root-domain} ${sub-domain}`
    * ie: `./init-route-53.sh pizzatent.co.nz kubernetes.pizzatent.co.nz`
1. Prepare S3 state store.
    * `. init-s3-state-store.sh ${bucket}`
1. Create cluster 
    * Initialize cluster
        * `kops create cluster --zones ${availability-zones} ${sub-domain}`
    * Update autoscaling setup
        * `kops edit ig --name ${sub-domain} nodes`
    * Deploy cluster
        * `kops update cluster ${sub-domain} --yes`
