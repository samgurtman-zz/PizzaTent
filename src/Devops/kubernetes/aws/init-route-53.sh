#!/bin/sh
# adapted from https://github.com/kubernetes/kops/blob/master/docs/aws.md
# Compatible with any shells supporting Strict POSIX

# https://stackoverflow.com/questions/592620/check-if-a-program-exists-from-a-bash-script
command -v jq >/dev/null 2>&1 || { echo >&2 "This script requires jq but it's not installed.  Aborting."; exit 1; }
command -v aws >/dev/null 2>&1 || { echo >&2 "This script requires aws-cli but it's not installed.  Aborting."; exit 1; } 
command -v uuidgen >/dev/null 2>&1 || { echo >&2 "This script requires uuidgen but it's not installed.  Aborting."; exit 1; } 

domain=$1
subdomain=$2
ID=$(uuidgen)
resource_records=$(aws route53 create-hosted-zone --name ${subdomain} --caller-reference $ID | jq -r '.DelegationSet.NameServers | map({Value: .})')
parent_zone_id=$(aws route53 list-hosted-zones | jq -r ".HostedZones[] | select(.Name==\"${domain}.\") | .Id")
batch=$(cat << EOF
{
  "Comment": "Create a subdomain NS record in the parent domain",
  "Changes": [
    {
      "Action": "CREATE",
      "ResourceRecordSet": {
        "Name": "${subdomain}",
        "Type": "NS",
        "TTL": 300,
        "ResourceRecords": ${resource_records}
      }
    }
  ]
}
EOF
)
aws route53 change-resource-record-sets --hosted-zone-id "${parent_zone_id}" --change-batch="${batch}"
