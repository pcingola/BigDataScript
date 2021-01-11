package aws

import (
    "github.com/aws/aws-sdk-go/aws"
    "github.com/aws/aws-sdk-go/aws/session"
    "github.com/aws/aws-sdk-go/service/s3"
    "log"
    "strings"
)

type AwsS3 struct {
    clienstByRegion map[string]*s3.S3
}

/*
    Delete an object
    Input format: Comma separated string "region,bucket,key"
*/
func (awss3 *AwsS3) Delete(csv string) error {
    // Parse "CSV" string
    f := strings.SplitN(csv, ",", 3)
    region, bucket, key := f[0], f[1], f[2]
    // Get S3 client
    client := awss3.Client(region)
    // Delete object
    doi := s3.DeleteObjectInput{Bucket: aws.String(bucket), Key: aws.String(key)}
    _, err := client.DeleteObject(&doi)
    if err != nil {
        log.Printf("Error: Deleting AWS S3 file: region '%s', bucket '%s', key '%s'. Error: %v", region, bucket, key, err)
    }
    return err
}

// Create a new AwsS3
func NewS3() (*AwsS3) {
    awss3 := &AwsS3{}
    awss3.clienstByRegion = make(map[string]*s3.S3)
    return awss3
}

// Return client for 'region'
func (awss3 *AwsS3) Client(region string) (*s3.S3) {
    client, ok := awss3.clienstByRegion[region]
    if !ok {
        // Create a new config, add region
        opts := session.Options{
            Config: aws.Config{Region: aws.String(region)},
            SharedConfigState: session.SharedConfigEnable,
        }
        // Create a session and a client
        sess := session.Must(session.NewSessionWithOptions(opts))
        client = s3.New(sess)
        awss3.clienstByRegion[region] = client
    }
    return client
}
