
/*
This is used to connect to send a process results (stdout, stderr, exit status)
to a cloud queue (e.g. AWS SQS)
*/

type Queue struct {
    stdoutCh chan string
    stderrCh chan string
    exitCh chan string
    finish chan bool    // The queue sends to this channel when it finishes sending all messages
}
