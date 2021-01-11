package aws

import (
    "github.com/aws/aws-sdk-go/aws"
    "github.com/aws/aws-sdk-go/aws/session"
    "github.com/aws/aws-sdk-go/service/ec2"
)

type AwsEc2 struct {
    session *session.Session
    client *ec2.EC2
    InstanceIds []string
}

func (awsec2 *AwsEc2) Add(instanceId string) {
    awsec2.InstanceIds = append(awsec2.InstanceIds, instanceId)
}

// Delete all instances
func (awsec2 *AwsEc2) Terminate() error {
    tinstin := &ec2.TerminateInstancesInput{
                InstanceIds: aws.StringSlice(awsec2.InstanceIds),
    }
    _, err := awsec2.client.TerminateInstances(tinstin)
    return err
}

// Create a new AwsEc2
func NewEc2() (*AwsEc2) {
    awsec2 := &AwsEc2{}
    awsec2.session = session.Must(session.NewSessionWithOptions(session.Options{SharedConfigState: session.SharedConfigEnable}))
    awsec2.client = ec2.New(awsec2.session)
    return awsec2
}
