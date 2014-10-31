package org.rosuda.JRI;

import java.io.PrintStream;

public class Mutex
{
  public static boolean verbose = false;
  private boolean locked = false;
  private Thread lockedBy = null;
  
  public synchronized void lock()
  {
    while (this.locked)
    {
      if (this.lockedBy == Thread.currentThread()) {
        System.err.println("FATAL ERROR: org.rosuda.JRI.Mutex detected a deadlock! The application is likely to hang indefinitely!");
      }
      if (verbose) {
        System.out.println("INFO: " + toString() + " is locked by " + this.lockedBy + ", but " + Thread.currentThread() + " waits for release (no timeout)");
      }
      try
      {
        wait();
      }
      catch (InterruptedException localInterruptedException)
      {
        if (verbose) {
          System.out.println("INFO: " + toString() + " caught InterruptedException");
        }
      }
    }
    this.locked = true;
    this.lockedBy = Thread.currentThread();
    if (verbose) {
      System.out.println("INFO: " + toString() + " locked by " + this.lockedBy);
    }
  }
  
  public synchronized boolean lockWithTimeout(long paramLong)
  {
    if (this.locked)
    {
      if (this.lockedBy == Thread.currentThread()) {
        System.err.println("FATAL ERROR: org.rosuda.JRI.Mutex detected a deadlock! The application is likely to hang indefinitely!");
      }
      if (verbose) {
        System.out.println("INFO: " + toString() + " is locked by " + this.lockedBy + ", but " + Thread.currentThread() + " waits for release (timeout " + paramLong + " ms)");
      }
      try
      {
        wait(paramLong);
      }
      catch (InterruptedException localInterruptedException)
      {
        if (verbose) {
          System.out.println("INFO: " + toString() + " caught InterruptedException");
        }
      }
    }
    if (!this.locked)
    {
      this.locked = true;
      this.lockedBy = Thread.currentThread();
      if (verbose) {
        System.out.println("INFO: " + toString() + " locked by " + this.lockedBy);
      }
      return true;
    }
    if (verbose) {
      System.out.println("INFO: " + toString() + " timeout, failed to obtain lock for " + Thread.currentThread());
    }
    return false;
  }
  
  public synchronized int tryLock()
  {
    if (verbose) {
      System.out.println("INFO: " + toString() + " tryLock by " + Thread.currentThread());
    }
    if (this.locked) {
      return this.lockedBy == Thread.currentThread() ? -1 : 1;
    }
    this.locked = true;
    this.lockedBy = Thread.currentThread();
    if (verbose) {
      System.out.println("INFO: " + toString() + " locked by " + this.lockedBy);
    }
    return 0;
  }
  
  public synchronized boolean safeLock()
  {
    if ((this.locked) && (this.lockedBy == Thread.currentThread()))
    {
      if (verbose) {
        System.out.println("INFO: " + toString() + " unable to provide safe lock for " + Thread.currentThread());
      }
      return false;
    }
    lock();
    return true;
  }
  
  public synchronized boolean safeLockWithTimeout(long paramLong)
  {
    if ((this.locked) && (this.lockedBy == Thread.currentThread()))
    {
      if (verbose) {
        System.out.println("INFO: " + toString() + " unable to provide safe lock (deadlock detected) for " + Thread.currentThread());
      }
      return false;
    }
    return lockWithTimeout(paramLong);
  }
  
  public synchronized void unlock()
  {
    if ((this.locked) && (this.lockedBy != Thread.currentThread())) {
      System.err.println("WARNING: org.rosuda.JRI.Mutex was unlocked by other thread than locked! This may soon lead to a crash...");
    }
    this.locked = false;
    if (verbose) {
      System.out.println("INFO: " + toString() + " unlocked by " + Thread.currentThread());
    }
    notify();
  }
  
  public String toString()
  {
    return super.toString() + "[" + (this.locked ? "" : "un") + "locked" + (!this.locked ? "" : new StringBuffer().append(", by ").append(this.lockedBy == Thread.currentThread() ? "current" : "another").append(" thread").toString()) + "]";
  }
}
