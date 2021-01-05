package aws

import (
    "github.com/aws/aws-sdk-go/aws"
    "github.com/aws/aws-sdk-go/aws/session"
    "github.com/aws/aws-sdk-go/service/ec2"
)

type AwsS3 struct {
    session *session.Session
    client *s3.S3
    bucket string
    keys []string
}

func (awsec2 *AwsS3) Add(instanceId string) {
    awsec2.InstanceIds = append(awsec2.InstanceIds, instanceId)
}

// Delete all instances
func (awsec2 *AwsS3) Terminate() error {
    tinstin := &ec2.TerminateInstancesInput{
                InstanceIds: aws.StringSlice(awsec2.InstanceIds),
    }
    _, err := awsec2.client.TerminateInstances(tinstin)
    return err
}

// Create a new AwsS3
func NewEc2() (*AwsS3) {
    awsec2 := &AwsS3{}
    awsec2.session = session.Must(session.NewSessionWithOptions(session.Options{SharedConfigState: session.SharedConfigEnable}))
    awsec2.client = ec2.New(awsec2.session)
    return awsec2
}
