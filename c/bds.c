/*-----------------------------------------------------------------------------
 *
 * ClousScript wrapper: Invoke BigDataScript's program (java)
 *
 * Note: Kill any process in the process groups after child finishes
 *
 *														Pablo Cingolani 2013
 *-----------------------------------------------------------------------------
 */

#include <sys/types.h>
#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <signal.h>
#include <string.h>


#define JAVA "java"
#define JAVA_MEM "-Xmx1G"
#define JAR_FILE "BigDataScript.jar"
#define BIGDATASCRIPT_CLASS "ca.mcgill.mcb.pcingola.bigDataScript.BigDataScript"
#define JAVA_ARGC 2
#define EXEC_ARGC 2


/**
 * Execute child process
 */
void execute(int argc, char *argv[]) {
	char **args;
	int i, j, n;

	/* Create new arguments array */
	n = argc - EXEC_ARGC;
	if( n <= 0 ) {
		printf("Error: No process to execute!\n");
		exit(1);
	}

	args = calloc( n , sizeof(char *) );
	for( i=EXEC_ARGC, j=0 ; i < argc ; i++ , j++ )
		args[j] = argv[i];

	/* Replace the child fork with a new process */
	if(execvp(args[0], args) == -1){
		fprintf(stderr,"Error: Cannot execute program\n");
		exit(1);
	}
}

/**
 * Execute java process
 */
void executeJavaBds(int argc, char *argv[]) {
	char **args;
	int i, n;

	/* Create new arguments array */
	n = argc + JAVA_ARGC;
	args = calloc( n , sizeof(char *) );
	args[0] = JAVA_MEM;
	args[1] = BIGDATASCRIPT_CLASS;

	/* Copy this program's arguments */
	for( i=0 ; i < argc ; i++ ) 
		args[i+JAVA_ARGC] = argv[i+1];
	args[n-1] = NULL;

	/* Replace the child fork with a new process */
	if(execvp(JAVA, args) == -1){
		fprintf(stderr,"Error: Cannot execute java program\n");
		exit(1);
	}
}

int isExec(int argc, char *argv[]) {
	if(( argc > 1 ) && (strcmp(argv[1],"exec") == 0)) return 1;
	return 0;
}

int isKill(int argc, char *argv[]) {
	if(( argc > 1 ) && (strcmp(argv[1],"kill") == 0)) return 1;
	return 0;
}

/*
 * Signal handler function
 */
void signalHandler(int signum) {
	printf("Captured signal %d!\n", signum);
	kill( - getpgrp(), SIGKILL );		/* Send a kill signal to process group */
	/* kill(0, SIGKILL); */				/* Send a SIGKILL signal to all process in current process group, then exit. This is probably a little bit too extreme since it will send kill signals to all parent processes as well */
	exit(1);
}

void showPid() {
	printf("PID\t%d\t%d\t%d\t\n", getpid(), getpgrp(), getsid( getpid() ) );
	fflush( stdout ); /* Make sure this gets to stdout right away (other process might expect this as first line */
}

/*
 * Main
 */
int main(int argc, char *argv[]){
	int retval;
	pid_t pid;

	/* Send a group kill signal and exit */
	if( isKill(argc, argv) ) {
		int pidToKill = atoi( argv[2] );
		printf("Killing group %d\n", pidToKill);
		kill( - pidToKill, SIGTERM );
		kill( - pidToKill, SIGHUP );
		exit(0);
	}

	/* Execute a child process */

	/* Set up signal handlers */
	signal(SIGABRT, &signalHandler);
	signal(SIGTERM, &signalHandler);
	signal(SIGINT,  &signalHandler);
	signal(SIGHUP,  &signalHandler);
	signal(SIGQUIT,  &signalHandler);
	signal(SIGALRM,  &signalHandler);
	signal(SIGVTALRM,  &signalHandler);
	signal(SIGPROF,  &signalHandler);

	if( isExec(argc, argv) ){
		// setpgid(0, 0);	/** Create a new process group */
		showPid();
	}

	/* Attempt to fork and check for errors */
	if( (pid=fork()) == -1){
		fprintf(stderr,"Fork error!\n");  /* something went wrong */
		exit(1);        
	}

	if(pid){
		/* A positive (non-negative) PID indicates the parent process */
		wait(&retval);				/* Wait for child process to end */
		kill( - pid, SIGKILL );		/* Send a kill signal to process group */
	} else{
		/* A zero PID indicates that this is the child process: Execute program */
		if( isExec(argc, argv) ) {
			execute(argc, argv);	/* Execute program from command line */
		} else {
			/* Execute java code */
			pid_t sid = setsid();	/* Create new session */
			showPid();
			executeJavaBds(argc, argv);
		}
	}

	/* Use same value a child porcess */
	return retval;
}

