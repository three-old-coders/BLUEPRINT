# Welcome to your CDK Java project!

This is a blank project for CDK development with Java.

The `cdk.json` file tells the CDK Toolkit how to execute your app.

It is a [Maven](https://maven.apache.org/) based project, so you can open this project with any Maven compatible Java IDE to build and run tests.

## Useful commands

 * `mvn package`     compile and run tests
 * `cdk ls`          list all stacks in the app
 * `cdk synth`       emits the synthesized CloudFormation template
 * `cdk deploy`      deploy this stack to your default AWS account/region
 * `cdk diff`        compare deployed stack with current state
 * `cdk docs`        open CDK documentation

Enjoy!

## Usage
This cdk app creates micro ec2 instance for testing docker stuff

 * setup your AWS [cdk](https://docs.aws.amazon.com/cdk/v2/guide/getting_started.html) and access credentials environment `export AWS_PROFILE=xxxxx`  
 * `cd /BLUEPRINT/DOCKER/DOCKER_TRAEFIK_PROMETHEUS_plus/aws-ec2-instance` navigate to project
 * `cdk bootstrap -c accountId=123456789012 -c region=eu-central-1 -c environmentName=develop -c keyPairName=XXX-XXX`
    bootstrap the selected AWS environment, if not already done. When you bootstrap an environment, AWS deploys a cloudformation stack called
    'CDKToolkit', which creates a new Amazon S3 bucket, Amazon ECR Repsitory and IAM roles.
 * `cdk deploy -c accountId=123456789012 -c region=eu-central-1 -c environmentName=develop -c keyPairName=XXX-XXX` deploys the stack
 * use ssh `ssh -i ~/.ssh/XXX-XXX.pem ec2-user@ec2-XXX-XXX-X-XXX.eu-central-1.compute.amazonaws.com` or use AWS Systems Manager `aws ssm start-session --target i-xxxxxxxxxxxxx` to login to the instance
 * `cdk destroy -c accountId=123456789012 -c region=eu-central-1 -c environmentName=develop -c keyPairName=XXX-XXX` deletes the stack
     
