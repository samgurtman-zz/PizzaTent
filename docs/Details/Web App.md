## Stack 
* React
  * Customer facing web site

## External Services
* Google Cloud Firebase

## Alternatives
* Nginx on GKE
* Google Cloud Storage
* Cloudflare 

## Rationale
The customer facing webapp will be written in React. The primary reason is React has high developer mind share and industry buy in. Thus finding React developers is not difficult, nor is there as high of a worry of React being abandoned. In addition, using React will allow developers to more easily transition to the mobile team (React Native) if neccesary. There is also the possibility of code reuse between the web and mobile applications.

Using Firebase allows us to easily manage our static resources, and not worry about scalability. I would avoid using firebase specific functionality, limiting it to static file hosting, and certificate management. It will be relatively simple to transition to another if we just utilize it for static hosting.