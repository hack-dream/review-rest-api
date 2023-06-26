# review-rest-api

## Example of local usage

```sh
mvn clean package
docker build . -t review-rest-api
docker run -p 127.0.0.1:8080:8080 review-rest-api
```