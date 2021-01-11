
package queue

import (
    "aws"
    "log"
	"time"
)

const DEBUG = false
const QUEUE_SIZE = 1024
const TIME_OUT = 950 * time.Millisecond

/*
This is used to connect to send a process results (stdout, stderr, exit status)
to a cloud queue (e.g. AWS SQS)
*/

type Queue struct {
    StdoutChan chan []byte
    StderrChan chan []byte
    exitChan chan string
    finishChan chan bool    // The queue sends to this channel when it finishes sending all messages

    awsSqs *aws.AwsSqs
}

// Send an exit string
func (q *Queue) Exit(exitStr string) {
    q.exitChan <- exitStr
}

// Create a new queue
func NewQueue(qname string, taskId string) (*Queue, error) {
    q := &Queue{}
    q.StdoutChan = make(chan []byte, QUEUE_SIZE)
    q.StderrChan = make(chan []byte, QUEUE_SIZE)
    q.exitChan = make(chan string)
    q.finishChan = make(chan bool)

    s, err := aws.NewSqs(qname, taskId)
    q.awsSqs = s

    return q, err
}

// Transmit data to queue system
func (q *Queue) Transmit() {
    run := true
    var exitStr string
    var sout, serr []byte
    for run {
		select {
            case exitStr = <-q.exitChan:
				run = false
				if DEBUG {
					log.Printf("Debug Transmit: Exit string received, '%s'\n", exitStr)
				}
                q.awsSqs.SendExit(exitStr)

			case sout = <-q.StdoutChan:
                if DEBUG {
					log.Printf("Debug Transmit: STDOUT received, '%s'\n", sout)
				}
                q.awsSqs.AppendStdOut(sout)

            case serr = <-q.StderrChan:
                if DEBUG {
					log.Printf("Debug Transmit: STDERR received, '%s'\n", serr)
				}
                q.awsSqs.AppendStdErr(serr)

            case <-time.After(TIME_OUT):
                if DEBUG {
					log.Printf("Debug Transmit: Timer send\n")
				}
                q.awsSqs.Send()
		}
	}

    // Send signal to finish channel, indicating we are done
    q.finishChan <- true
}

// Wait for some signal to be sent on the 'finish' channel
func (q *Queue) WaitFinish() {
    <- q.finishChan
}
