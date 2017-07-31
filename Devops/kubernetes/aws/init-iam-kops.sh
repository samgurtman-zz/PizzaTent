#!/bin/sh
# adapted from https://github.com/kubernetes/kops/blob/master/docs/aws.md
# Compatible with any shells supporting Strict POSIX

# https://stackoverflow.com/questions/592620/check-if-a-program-exists-from-a-bash-script
command -v jq >/dev/null 2>&1 || { echo >&2 "This script requires jq but it's not installed.  Aborting."; exit 1; }
command -v aws >/dev/null 2>&1 || { echo >&2 "This script requires aws-cli but it's not installed.  Aborting."; exit 1; } 

echo "Creating Kops IAM group"
aws iam create-group --group-name kops > /dev/null

aws iam attach-group-policy --policy-arn arn:aws:iam::aws:policy/AmazonEC2FullAccess --group-name kops
aws iam attach-group-policy --policy-arn arn:aws:iam::aws:policy/AmazonRoute53FullAccess --group-name kops
aws iam attach-group-policy --policy-arn arn:aws:iam::aws:policy/AmazonS3FullAccess --group-name kops
aws iam attach-group-policy --policy-arn arn:aws:iam::aws:policy/IAMFullAccess --group-name kops
aws iam attach-group-policy --policy-arn arn:aws:iam::aws:policy/AmazonVPCFullAccess --group-name kops

echo "Creating Kops IAM user"
aws iam create-user --user-name kops > /dev/null
aws iam add-user-to-group --user-name kops --group-name kops

#Generate AWS crednetials for kops user and print commands needed to export to environment
json_credentials=$(aws iam create-access-key --user-name kops)
export AWS_ACCESS_KEY_ID=$(echo "${json_credentials}" | jq -r '.AccessKey.AccessKeyId' )
export AWS_SECRET_ACCESS_KEY=$(echo "${json_credentials}" | jq -r '.AccessKey.SecretAccessKey')
echo export AWS_ACCESS_KEY_ID="${AWS_ACCESS_KEY_ID}"
echo export AWS_SECRET_ACCESS_KEY="${AWS_SECRET_ACCESS_KEY}"