Our production stack is described in the `production` helm chart. Variables in `values.yaml` will be replaced by secrets
as specified in `secrets.yaml`. In addition, kuberetes cluster secerets will be crated as specified in `secrets.yaml`. 
Secrets functionality is WIP.

The stack is automatically deployed using the Google Cloud Builder. This is quite buggy still but should trigger based 
on GitHub code push.