
package queue

import (
    "log"
    "aws"
)

var DEBUG bool = true

/*
This is used to connect to send a process results (stdout, stderr, exit status)
to a cloud queue (e.g. AWS SQS)
*/

type Queue struct {
    StdoutChan chan []byte
    StderrChan chan []byte
    exitChan chan string
    finishChan chan bool    // The queue sends to this channel when it finishes sending all messages

    awsSqs AwsSqs
}

// Send an exit string
func (q *Queue) Exit(exitStr string) {
    q.exitChan <- exitStr
}

// Create a new queue
func NewQueue(chanSize int) *Queue {
    q := &Queue{}
    q.StdoutChan = make(chan []byte, chanSize)
    q.StderrChan = make(chan []byte, chanSize)
    q.exitChan = make(chan string)
    q.finishChan = make(chan bool)
    return q
}

// Transmit data to queue system
func (q *Queue) Transmit() {
    run := true
    var exitStr string
    var out, err []byte
    for run {
		select {
        case exitStr = <-q.exitChan:
				run = false
				if DEBUG {
					log.Printf("Debug Transmit: Exit string received, '%s'\n", exitStr)
				}

			case out = <-q.StdoutChan:
                if DEBUG {
					log.Printf("Debug Transmit: STDOUT received, '%s'\n", out)
				}

            case err = <-q.StderrChan:
                if DEBUG {
					log.Printf("Debug Transmit: STDERR received, '%s'\n", err)
				}
		}
	}

    // Send signal to finish channel, indicating we are done
    q.finishChan <- true
}

// Wait for some signal to be sent on the 'finish' channel
func (q *Queue) WaitFinish() {
    <- q.finishChan
}
