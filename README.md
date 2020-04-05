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
1. if you want to index objects, you need to create the initial index file
1. after deployment, endpoint can be tested through Postman, and swagger docs will be deployed at /swagger-ui.html#/

## upcoming features ##
- [ ] multiple index files
- [ ] enable feature flags on specific request types through config vars
- [ ] ability to lock api with secret

## next steps ##
- create unit tests
- <del>connect static page app</del>
- <del>outline heroku deploy</del>
- <del>running/deploy steps</del>
- set to be more abstract:
  - should be able to use and implement in multiple API services at a repo level for data retrieval
