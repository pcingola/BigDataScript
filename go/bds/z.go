package main

import (
	"fmt"
	"os"
	"os/signal"
)

func main() {
	// Set up channel on which to send signal notifications.
	// We must use a buffered channel or risk missing the signal
	// if we're not ready to receive when the signal is sent.
	osSignal := make(chan os.Signal, 1)
	signal.Notify(osSignal, os.Interrupt, os.Kill)

	// Block until a signal is received.
	waitSignal(osSignal)
	fmt.Println("Done!\n")
}

func waitSignal(osSignal chan os.Signal) int {
	select {
	case <-osSignal:
		fmt.Println("Got signal!")
	}
	return 1
}
