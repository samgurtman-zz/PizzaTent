## Stack 
* Kubernetes (Google Container Engine)
   * Run containers
   * Automatically update DNS entries (Extenal-DNS helm)
   * Automatically manage load balancing (Kubenertes Ingress)
   * Auto scales both services and instances to be truly elastic
* Google Cloud Builder
   * Builds containers on Github trigger
   * Deploy secrets from GCE Object Storage using KMS keys to decrypt
   * Deploy helm chart after docker builds complete 
* Google Container Registry
  * Fast and easy access from Google Container Engine
* Github
   * Source Control
   * Superior developer experience
* Helm
   * Compose Kubernetes stack and allow for rolling updates
* Google KMS
   * Manage secrets encryption
* Google Object Storage
   * Store encrypted secrets and configuration data
