package de.threeoldcoders;

import lombok.SneakyThrows;
import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.services.ec2.*;
import software.amazon.awscdk.services.iam.*;
import software.constructs.Construct;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;


public class AwsEc2InstanceStack extends Stack {
    private final AwsEc2InstanceApp.AppConfiguration _appConfig;

    public AwsEc2InstanceStack(final Construct scope, final String id, final StackProps props, final AwsEc2InstanceApp.AppConfiguration appConfiguration) {
        super(scope, id, props);
        _appConfig=appConfiguration;

        // one single public subnet
        final SubnetConfiguration publicSubnet = SubnetConfiguration.builder()
                .name(id+" publicSubnet")
                .subnetType(SubnetType.PUBLIC)
                .build();

        final Vpc.Builder builder = Vpc.Builder.create(this, id+"-vpc");
        final Vpc vpc = builder
                .natGateways(0)
                .maxAzs(1)
                .subnetConfiguration(List.of(
                        publicSubnet
                ))
                .build();

        final ISecurityGroup securityGroup = SecurityGroup.Builder.create(this, id + "-sg")
                .securityGroupName(id)
                .vpc(vpc)
                .build();

        securityGroup.addIngressRule(Peer.anyIpv4(), Port.tcp(22),"allow SSH access from anywhere"); // ssh access
        securityGroup.addIngressRule(Peer.anyIpv4(), Port.tcp(80),"allow HTTP traffic from anywhere"); // http access
        securityGroup.addIngressRule(Peer.anyIpv4(), Port.tcp(443),"allow HTTPS traffic from anywhere"); // https access

        // 👇 create an example role for the EC2 Instance based on a managed aws policy
        final Role webserverRole = Role.Builder.create(this, prefixWithEnvironmentName("webserver-role"))
                .assumedBy(ServicePrincipal.Builder.create("ec2.amazonaws.com").build())
                .managedPolicies(List.of(ManagedPolicy.fromAwsManagedPolicyName("AmazonS3ReadOnlyAccess"),
                                         ManagedPolicy.fromAwsManagedPolicyName("AmazonSSMManagedEC2InstanceDefaultPolicy"),
                                         ManagedPolicy.fromAwsManagedPolicyName("AWSOpsWorksCloudWatchLogs")))
                .build();

        final Instance x86EC2Instance = Instance.Builder.create(this, id + "-x86-ec2")
                .instanceName(id + "-x86-ec2")
                .machineImage(MachineImage.latestAmazonLinux2())
                .instanceType(InstanceType.of(
                        InstanceClass.BURSTABLE3,
                        InstanceSize.MICRO
                ))
                .requireImdsv2(true)
                .securityGroup(securityGroup)
                .vpc(vpc)
                .vpcSubnets(
                        SubnetSelection.builder()
                                .subnetType(SubnetType.PUBLIC)
                                .build()
                )
                .keyName(_appConfig.getKeyPairName())
                .role(webserverRole)
                .userData(readUserData("/userdata.sh"))
                .build();

        x86EC2Instance.getInstance().toString();

        CfnOutput.Builder.create(this,"publicIp")
                .exportName(getStackName()+"-publicIp")
                .description(sanitize("public ip of the instance "+x86EC2Instance))
                .value(x86EC2Instance.getInstancePublicIp())
                .build();

        CfnOutput.Builder.create(this,"publicDns")
                .exportName(getStackName()+"-publicDns")
                .description(sanitize("public dns name of the "+x86EC2Instance))
                .value(x86EC2Instance.getInstancePublicDnsName())
                .build();
    }

    private String sanitize(final String value) {
        return value.replaceAll("[^a-zA-Z0-9- ]", "");
    }

    @SneakyThrows
    private UserData readUserData(final String userDataResource)
    {
        return UserData.custom(Files.readString(Paths.get(Objects.requireNonNull(getClass().getResource(userDataResource)).toURI()), StandardCharsets.UTF_8));
    }

    private String prefixWithEnvironmentName(final String value) {
        return _appConfig.getEnvironmentName() + "-" + value;
    }
}
