# Quarkus AWS Lambda with Native Compilation

This project demonstrates how to create AWS Lambda functions using Quarkus and how to compile them to native executables for improved cold start performance and reduced memory usage.

## Project Overview

This is a simple Quarkus application that provides AWS Lambda functions. The project includes:

- Multiple Lambda function handlers (TestLambda, AnotherLambda, StreamLambda)
- Simple input/output data models
- Service layer with business logic
- Support for both JVM and native compilation modes

## Creating Lambda Functions with Quarkus

To create a new Lambda function in this project:

1. Create a class that implements one of the AWS Lambda handler interfaces:
   - `RequestHandler<InputType, OutputType>` for simple request/response functions
   - `RequestStreamHandler` for functions that process streams

2. Annotate your handler class with `@Named("function-name")` to identify it:

```java
@Named("my-function")
public class MyLambda implements RequestHandler<InputObject, OutputObject> {

    @Inject
    ProcessingService service;

    @Override
    public OutputObject handleRequest(InputObject input, Context context) {
        return service.process(input).setRequestId(context.getAwsRequestId());
    }
}
```

3. You can inject CDI beans using `@Inject` to implement your business logic

## Building the Project

### JVM Mode (Standard Java)

To build the project in JVM mode:

```bash
./mvnw clean package
```

This creates:
- `target/function.zip` - The deployment package for AWS Lambda
- `target/sam.jvm.yaml` - SAM template for JVM deployment

### Native Mode

To build the project in native mode:

```bash
./mvnw clean package -Pnative
```

This creates:
- `target/function.zip` - The native executable packaged for AWS Lambda
- `target/sam.native.yaml` - SAM template for native deployment

Native compilation requires GraalVM or a compatible native compiler to be installed.

## Testing Locally

### Using AWS SAM CLI

To test your Lambda function locally using the AWS SAM CLI:

```bash
# For JVM mode
sam local invoke -t target/sam.jvm.yaml -e events/event.json

# For native mode
sam local invoke -t target/sam.native.yaml -e events/event.json
```

Create a sample event file at `events/event.json` with content like:

```json
{
  "name": "John",
  "greeting": "Hello"
}
```

## Deploying to AWS

### Using AWS SAM

You can deploy your Lambda function using the AWS SAM CLI:

```bash
# For JVM mode
sam deploy --template-file target/sam.jvm.yaml --stack-name quarkus-lambda-jvm --capabilities CAPABILITY_IAM

# For native mode
sam deploy --template-file target/sam.native.yaml --stack-name quarkus-lambda-native --capabilities CAPABILITY_IAM
```

### Using AWS CDK

It's recommended to use AWS CDK for infrastructure as code to deploy your Lambda functions. This provides more flexibility and better integration with other AWS services.

## Performance Comparison

Native compilation offers several advantages for AWS Lambda:

- Faster cold start times (typically under 100ms vs. several seconds for JVM)
- Lower memory usage (can run with as little as 128MB vs. 256MB+ for JVM)
- Reduced execution costs due to lower memory requirements and faster execution

## Additional Resources

- [Quarkus AWS Lambda Guide](https://quarkus.io/guides/amazon-lambda)
- [Quarkus Native Guide](https://quarkus.io/guides/building-native-image)
- [AWS Lambda Developer Guide](https://docs.aws.amazon.com/lambda/latest/dg/welcome.html)
- [AWS SAM Developer Guide](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/what-is-sam.html)
