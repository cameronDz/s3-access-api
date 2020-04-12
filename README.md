# s3 access api #
simple spring boot api for accessing files (specifically JSON files) in s3 bucket

## setup/deployment ##
this application can be deployed through Heroku, and requires an S3 bucket configured correctly
1. first you need to set up the S3 bucket; you will need the following after setting up the bucket
    - bucket name
    - access key
    - aws region
    - secret key
1. in Heroku setting, under Config Vars, create the following Vars to match the above names
    - S3_ACCESS_KEY
    - S3_BUCKET_NAME
    - S3_BUCKET_REGION
    - S3_SECRET_KEY
1. you will also need to set up whether you want a variable in the config that says whether objected saved in S3 will be publically available; use the following variable to determine (set as true or false)
    - S3_BUCKET_IS_PUBLIC
1. if you want to index objects, you need to create the initial index file
1. after deployment, endpoint can be tested through Postman, and swagger docs will be deployed at /swagger-ui.html 

## features/endpoints ##
this API is design specifically JSON objects stored on S3. when you deploy the application, or run it locally, the swagger docs will give an in depth overview on all the endpoints. a here is a more general overview

### api pathing ###
since this API is dealing with JSON, all endpoints currently start with ```/json```. there are current 4 implemented methods (with a fifth one not implemented). __note__: when ever refering to a specific object key name in the bucket, the .json extension should NOT be included
1. ```/json/list``` a GET request for all JSON objects in the S3 bucket
1. ```/json/object/{key}``` a GET request for a specific JSON object with name as key.
1. ```/json/upload/{key}``` a POST request for a new JSON object to be put in the bucket
1. ```/json/update/{key}``` a PUT request to update an existing JSON object in the bucket
1. ```/json/delete/{key}``` (**not implemented**)a DELETE request to remove an existing JSON object in the bucket

## upcoming features ##
- [ ] enable feature flags on specific request types through config vars
- [ ] ability to lock api with secret
