package aws

import (
    "encoding/base64"
    "github.com/aws/aws-sdk-go/aws"
    "github.com/aws/aws-sdk-go/aws/session"
    "github.com/aws/aws-sdk-go/service/sqs"
    "log"
    "strings"
)

const MAX_MSG_SIZE = 250 * 1024
const SHOULD_SEND_MSG_SIZE = 100 * 1024
var TAB_AS_BYTES = []byte("\t")

type AwsSqs struct {
    queueName, queueUrl string
    session *session.Session
    client *sqs.SQS

    taskId string
    buffOut []byte
    buffErr []byte
    exit string
}

// Append to STDOUT buffer
func (awssqs *AwsSqs) AppendStdOut(msg []byte) {
    awssqs.buffOut = append(awssqs.buffOut, msg...)
    if awssqs.shouldSend() {
        awssqs.Send()
    }
}

// Append to STDERR buffer
func (awssqs *AwsSqs) AppendStdErr(msg []byte) {
    awssqs.buffErr = append(awssqs.buffErr, msg...)
    if awssqs.shouldSend() {
        awssqs.Send()
    }
}

// Delete an SQS queue
func (awssqs *AwsSqs) Delete() error {
    delQueIn := sqs.DeleteQueueInput{
        QueueUrl: &awssqs.queueUrl,
    }
    _, err := awssqs.client.DeleteQueue(&delQueIn)
    return err
}

// Encode bytes
func encodeBytes(b []byte) string {
    if b == nil {
        return ""
    }
    return base64.StdEncoding.EncodeToString(b)
}

// Encode a string
func encodeString(b string) string {
    if b == "" {
        return ""
    }
    return base64.StdEncoding.EncodeToString([]byte(b))
}

// Create a new AwsSqs
func NewSqs(qname string, taskId string) (*AwsSqs, error) {
    awssqs := &AwsSqs{}
    awssqs.taskId = taskId
    awssqs.session = session.Must(session.NewSessionWithOptions(session.Options{SharedConfigState: session.SharedConfigEnable}))
    awssqs.client = sqs.New(awssqs.session)

    awssqs.queueName = qname
    if strings.HasPrefix(qname, "https://") {
        // This is not a queue name, it's a queue URL
        awssqs.queueUrl = qname
    } else {
        // Look for the URL based on the name
        res, err := awssqs.client.GetQueueUrl(&sqs.GetQueueUrlInput{QueueName: &qname,})
        if err != nil {
            log.Printf("Error getting URL for AWS SQS queue '%s': %v\n", qname, err)
            return nil, err
        }
        awssqs.queueUrl = *res.QueueUrl
    }
    return awssqs, nil
}

// Send current buffers
func (awssqs *AwsSqs) Send() {
    sout := encodeBytes(awssqs.buffOut)
    serr := encodeBytes(awssqs.buffErr)
    sexit := encodeString(awssqs.exit)
    all := []string{awssqs.taskId, sout, serr, sexit}
    msg := strings.Join(all, "\t")

    // Reset buffers
    awssqs.buffOut, awssqs.buffErr, awssqs.exit = nil, nil, ""

    if len(msg) > MAX_MSG_SIZE {
        log.Printf("Error sending message: Message too long, dropping message. Message length %d, max length %d\n", len(msg), MAX_MSG_SIZE)
    } else {
        awssqs.SendString(msg)
    }
}

// Send bytes
func (awssqs *AwsSqs) SendBytes(msgBytes []byte) {
    awssqs.SendString(string(msgBytes))
}

// Send exit
func (awssqs *AwsSqs) SendExit(msg string) {
    awssqs.exit = msg
    awssqs.Send()
}

// Send string
func (awssqs *AwsSqs) SendString(msgStr string) {
    msg := sqs.SendMessageInput {
        MessageBody: aws.String(msgStr),
        QueueUrl: &awssqs.queueUrl,
    }
    _, err := awssqs.client.SendMessage(&msg)
    if err != nil {
        log.Printf("Error sending message to queue '%s': %v\n", awssqs.queueName, err)
    }
}

// Should we send a message?
// True if total length is over SHOULD_SEND_MSG_SIZE
func (awssqs *AwsSqs) shouldSend() bool {
    return (len(awssqs.buffOut) + len(awssqs.buffErr)) > SHOULD_SEND_MSG_SIZE
}
