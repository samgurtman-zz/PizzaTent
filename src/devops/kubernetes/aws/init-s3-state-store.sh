#!/bin/sh
# adapted from https://github.com/kubernetes/kops/blob/master/docs/aws.md
# Compatible with any shells supporting Strict POSIX

# https://stackoverflow.com/questions/592620/check-if-a-program-exists-from-a-bash-script
command -v aws >/dev/null 2>&1 || { echo >&2 "This script requires aws-cli but it's not installed.  Aborting."; exit 1; } 

aws s3api create-bucket --bucket "${1}" --region us-east-1 > /dev/null
aws s3api put-bucket-versioning --bucket "${1}" --versioning-configuration Status=Enabled > /dev/null
export KOPS_STATE_STORE=s3://${1}
echo export KOPS_STATE_STORE=s3://${1}