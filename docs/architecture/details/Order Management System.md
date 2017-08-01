## Stack 
* Spring Boot
* Spring Security
* Spring MVC Rest
* Docker
* Stripe

## External services

* Google Container Engine (GKE)
* Google Pub/Sub
* Google Cloud SQL (Postgres)


## Rationale
Using Google over AWS allows us to use managed kubernetes (google container engine) which is a far better offering than ECS. Running Kubernetes on AWS is quite intesnive, and for small deployments much costlier due to having to run your own managers.
In addition, We can always deploy Kubernetes on another provider if we need to move away from Google Cloud Engine, wehreas that is not possible with ECS.
Google Pub/Sub is equivalent to SQS for our purposes. Google Cloud SQL offers both Postgres and MySql, which allows us to switch to another vendor relatively easily. I've chosen MySQL because it's set up for high availability in GCE while postgres is not. 
In addition using Google allows us to use Cloud Spanner if we ever hit massive loads.