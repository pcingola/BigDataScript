package aws

import (
    "flag"
    "fmt"

    "github.com/aws/aws-sdk-go/aws/session"
    "github.com/aws/aws-sdk-go/service/sqs"
)

type AwsSqs struct {
    sqsSession session.Must
}

// Create a new AwsSqs
func NewAwsSqs() *AwsSqs {
    awssqs := &AwsSqs{}
    awssqs.sqsSession = session.Must(session.NewSessionWithOptions(session.Options{SharedConfigState: session.SharedConfigEnable,}))
    return awssqs
}
