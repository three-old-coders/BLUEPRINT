package de.threeoldcoders;

 import software.amazon.awscdk.App;
 import software.amazon.awscdk.StackProps;
 import software.amazon.awscdk.assertions.Template;
 import java.io.IOException;

 import java.util.ArrayList;
 import java.util.HashMap;

 import org.junit.jupiter.api.Test;

 // example test. To run these tests, uncomment this file, along with the
 // example resource in java/src/main/java/com/myorg/AwsEc2InstanceStack.java
 public class AwsEc2InstanceTest {

     @Test
     public void testStack() throws IOException {
         App app = new App();
         final AwsEc2InstanceApp.AppConfiguration.AppConfigurationBuilder appConfigBuilder= AwsEc2InstanceApp.AppConfiguration.builder();
         final AwsEc2InstanceApp.AppConfiguration ac = appConfigBuilder.environmentName("TEST").build();

         final AwsEc2InstanceStack stack = new AwsEc2InstanceStack(app, ac.getEnvironmentName()+"-AwsEc2InstanceStack", StackProps.builder()
                 // For more information, see https://docs.aws.amazon.com/cdk/latest/guide/environments.html
                 .build(),ac);

         final Template template = Template.fromStack(stack);

         template.hasOutput("publicIp",new HashMap<>() {{
             put("Description","public ip of the instance TEST-AwsEc2InstanceStackTEST-AwsEc2InstanceStack-x86-ec2");
             put("Export",new HashMap<>() {{
                 put("Name","TEST-AwsEc2InstanceStack-publicIp");
             }});
         }});

         template.hasResourceProperties("AWS::EC2::Instance",new HashMap<>() {{
             put("InstanceType","t3.micro");
             put("IamInstanceProfile",new HashMap<>() {{
                 put("Ref","TESTAwsEc2InstanceStackx86ec2InstanceProfileE0396FEE");
             }});
             put("Tags",new ArrayList<>() {{
                 add(new HashMap<>(){{
                     put("Key","Name");
                     put("Value","TEST-AwsEc2InstanceStack-x86-ec2");
                 }});
             }});
         }});
     }
 }
