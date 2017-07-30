[Please view the full Kops tutorial first](https://github.com/kubernetes/kops/blob/master/docs/aws.md)

Dependencies:
* Kops
* Kubectl
* awc-cli

1. Prepare the AWS Kops Iam user. Run `. init-iam-kops.sh` or if you would not like like your current environment modified, run `./init-iam-kops.sh` to get the required exports
1. Prepare Route 53. Run `./init-route-53.sh ${root domain} ${kubernetes sub domain}`
    * ie: `./init-route-53.sh pizzatent.co.nz kubernetes.pizzatent.co.nz`