package exec

import (
	"log"
	"syscall"
)

/*
	Kill all child process
*/
func (be *BdsExec) kill() {
	if DEBUG {
		log.Printf("Debug kill: Killing process\n")
	}
	be.cmd.Process.Kill()
	be.cmd.Process.Wait() // Reap their souls

	// Should we kill all process groups from taskLoggerFile?
	if be.TaskLoggerFile != "" {
		be.TaskLoggerCleanUpAll()
	}

	// Send a SIGKILL to the process group (just in case any child process is still executing)
	if DEBUG {
		log.Printf("Debug kill: Killing process group: kill(0, SIGHUP)\n")
	}

	// WARNING: The Kill(0) signal will also kill this process, so nothing
	// may be execited after this call (platform dependent)
	syscall.Kill(0, syscall.SIGHUP)

	if DEBUG {
		log.Printf("Debug kill: Killed process group: kill(0, SIGHUP)\n")
	}
}

// Kill a process group form command line
func (be *BdsExec) KillArgs() {
	be.KillProcessGroup(be.pidToKill)
}

/*
	Kill a process group
*/
func (be *BdsExec) KillProcessGroup(pid int) {
	if DEBUG {
		log.Printf("Debug KillProcessGroup: Killing process group %d\n", pid)
	}

	syscall.Kill(-pid, syscall.SIGHUP)
}
