package ca.mcgill.mcb.pcingola.bigDataScript;

class T1 extends Thread {
	Zzz zzz;

	public T1(Zzz zzz) {
		this.zzz = zzz;
	}

	@Override
	public void run() {
		while (true)
			zzz.doSomething();
	}
}

class T2 extends Thread {
	final Zzz zzz;

	public T2(Zzz zzz) {
		this.zzz = zzz;
	}

	@Override
	public void run() {
		while (true)
			zzz.doSomethingElse();
	}
}

public class Zzz {
	public static final int SLEEP = 10;
	public static final int SLEEP_LONG = 500;

	public static Object cacheLock = new Object();
	public static Object tableLock = new Object();

	public boolean wait = false;

	public static void main(String[] args) throws Exception {
		Zzz zzz = new Zzz();

		T1 t1 = new T1(zzz);
		T2 t2 = new T2(zzz);

		t1.start();
		t2.start();
	}

	public void anotherMethod() {
		synchronized (tableLock) {

			while (!wait)
				sleep();

			synchronized (cacheLock) {
				doSomethingElse();
			}
		}
	}

	void doSomething() {
		System.out.println("Doing something: " + Thread.currentThread().getId());
		sleep();
	}

	void doSomethingElse() {
		System.out.println("Doing something else: " + Thread.currentThread().getId());
		sleep();
	}

	public void oneMethod() {
		synchronized (cacheLock) {
			sleepLong();
			wait = true;
			synchronized (tableLock) {
				doSomething();
			}
			wait = false;
		}
	}

	void sleep() {
		try {
			Thread.sleep(SLEEP);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void sleepLong() {
		try {
			Thread.sleep(SLEEP_LONG);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
