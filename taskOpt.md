
# Task parameters

TODO: Need 'disk', size in GB

TODO: Add TaskOpts

TODO: Include base libraries

```
class TaskOpts {
	int cpus := 1
	int mem := -1
	int disk := -1		// Disk size in GB
	forkChpPrefix := 'tmp/bds'	// Fork checkppoint prefix
	...
}

class HpcOpts extends TaskOpts {
	string queue := ''
}

class CloudOpts extends TaskOpts {
	string instance		// Instance type
	string bucket		// Bucket (e.g. for checkpoints, logs, etc)
	string image		// Image to use when launching an instance
	int instanceRetry := 50  // How many times to retry to create an instance
	int instanceRetryTime := 300	// Ranom backoff time between retries
	string{} cmdLine	// Instance command line options (unescaped)
}

class AwsOpts extends CloudOpts {
	string instanceProfile
	string cloudAwsSecurityGroupId
	string subnetId
	// SQS options
	string sqsQueueName
	// Spot instance options
	bool spot
	int spotTime
}

class GcOpts extends CloudOpts {
}

```

