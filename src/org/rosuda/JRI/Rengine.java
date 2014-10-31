package org.rosuda.JRI;

import java.io.PrintStream;

public class Rengine
  extends Thread
{
  public static boolean jriLoaded;
  boolean loopHasLock = false;
  
  static
  {
    try
    {
      System.loadLibrary("jri");
      jriLoaded = true;
    }
    catch (UnsatisfiedLinkError localUnsatisfiedLinkError)
    {
      jriLoaded = false;
      String str = System.getProperty("jri.ignore.ule");
      if ((str == null) || (!str.equals("yes")))
      {
        System.err.println("Cannot find JRI native library!\nPlease make sure that the JRI native library is in a directory listed in java.library.path.\n");
        localUnsatisfiedLinkError.printStackTrace();
        System.exit(1);
      }
    }
  }
  
  static Thread mainRThread = null;
  public static final int SO_NilValue = 0;
  public static final int SO_GlobalEnv = 1;
  public static final int SO_EmptyEnv = 2;
  public static final int SO_BaseEnv = 3;
  public static final int SO_UnboundValue = 4;
  public static final int SO_MissingArg = 5;
  public static final int SO_NaString = 6;
  public static final int SO_BlankString = 7;
  
  public static long getVersion()
  {
    return 266L;
  }
  
  public static boolean versionCheck()
  {
    return getVersion() == rniGetVersion();
  }
  
  public static int DEBUG = 0;
  public int idleDelay = 200;
  static Rengine mainEngine = null;
  
  public static Rengine getMainEngine()
  {
    return mainEngine;
  }
  
  public static boolean inMainRThread()
  {
    return (mainRThread != null) && (mainRThread.equals(Thread.currentThread()));
  }
  
  boolean standAlone = true;
  boolean died;
  boolean alive;
  boolean runLoop;
  boolean loopRunning;
  String[] args;
  Mutex Rsync;
  RMainLoopCallbacks callback;
  
  public boolean isStandAlone()
  {
    return this.standAlone;
  }
  
  public Rengine(String[] paramArrayOfString, boolean paramBoolean, RMainLoopCallbacks paramRMainLoopCallbacks)
  {
    this.Rsync = new Mutex();
    this.died = false;
    this.alive = false;
    this.runLoop = paramBoolean;
    this.loopRunning = false;
    this.args = paramArrayOfString;
    this.callback = paramRMainLoopCallbacks;
    mainEngine = this;
    mainRThread = this;
    start();
    while ((!this.alive) && (!this.died)) {
      yield();
    }
  }
  
  public Rengine()
  {
    this.Rsync = new Mutex();
    this.died = false;
    this.alive = true;
    this.runLoop = false;
    this.loopRunning = true;
    this.standAlone = false;
    this.args = new String[] { "--zero-init" };
    this.callback = null;
    mainEngine = this;
    mainRThread = Thread.currentThread();
    rniSetupR(this.args);
  }
  
  synchronized int setupR()
  {
    return setupR(null);
  }
  
  synchronized int setupR(String[] paramArrayOfString)
  {
    int i = rniSetupR(paramArrayOfString);
    if (i == 0)
    {
      this.alive = true;this.died = false;
    }
    else
    {
      this.alive = false;this.died = true;
    }
    return i;
  }
  
  public long rniCons(long paramLong1, long paramLong2)
  {
    return rniCons(paramLong1, paramLong2, 0L, false);
  }
  
  public long rniLCons(long paramLong1, long paramLong2)
  {
    return rniCons(paramLong1, paramLong2, 0L, true);
  }
  
  public void addMainLoopCallbacks(RMainLoopCallbacks paramRMainLoopCallbacks)
  {
    this.callback = paramRMainLoopCallbacks;
  }
  
  public void startMainLoop()
  {
    this.runLoop = true;
  }
  
  public void jriWriteConsole(String paramString, int paramInt)
  {
    if (this.callback != null) {
      this.callback.rWriteConsole(this, paramString, paramInt);
    }
  }
  
  public void jriBusy(int paramInt)
  {
    if (this.callback != null) {
      this.callback.rBusy(this, paramInt);
    }
  }
  
  public String jriReadConsole(String paramString, int paramInt)
  {
    if (DEBUG > 1) {
      System.out.println("Rengine.jreReadConsole BEGIN " + Thread.currentThread());
    }
    if (this.loopHasLock)
    {
      this.Rsync.unlock();
      this.loopHasLock = false;
    }
    String str1 = this.callback == null ? null : this.callback.rReadConsole(this, paramString, paramInt);
    this.loopHasLock = this.Rsync.safeLock();
    if (!this.loopHasLock)
    {
      String str2 = "\n>>JRI Warning: jriReadConsole detected a possible deadlock [" + this.Rsync + "][" + Thread.currentThread() + "]. Proceeding without lock, but this is inherently unsafe.\n";
      jriWriteConsole(str2, 1);
      System.err.print(str2);
    }
    if (DEBUG > 1) {
      System.out.println("Rengine.jreReadConsole END " + Thread.currentThread());
    }
    return str1;
  }
  
  public void jriShowMessage(String paramString)
  {
    if (this.callback != null) {
      this.callback.rShowMessage(this, paramString);
    }
  }
  
  public void jriLoadHistory(String paramString)
  {
    if (this.callback != null) {
      this.callback.rLoadHistory(this, paramString);
    }
  }
  
  public void jriSaveHistory(String paramString)
  {
    if (this.callback != null) {
      this.callback.rSaveHistory(this, paramString);
    }
  }
  
  public String jriChooseFile(int paramInt)
  {
    if (this.callback != null) {
      return this.callback.rChooseFile(this, paramInt);
    }
    return null;
  }
  
  public void jriFlushConsole()
  {
    if (this.callback != null) {
      this.callback.rFlushConsole(this);
    }
  }
  
  public synchronized REXP eval(String paramString)
  {
    return eval(paramString, true);
  }
  
  public synchronized REXP eval(String paramString, boolean paramBoolean)
  {
    if (DEBUG > 0) {
      System.out.println("Rengine.eval(" + paramString + "): BEGIN " + Thread.currentThread());
    }
    boolean bool = this.Rsync.safeLock();
    try
    {
      long l1 = rniParse(paramString, 1);
      if (l1 != 0L)
      {
        long l2 = rniEval(l1, 0L);
        if (l2 != 0L)
        {
          REXP localREXP1 = new REXP(this, l2, paramBoolean);
          if (DEBUG > 0) {
            System.out.println("Rengine.eval(" + paramString + "): END (OK)" + Thread.currentThread());
          }
          return localREXP1;
        }
      }
    }
    finally
    {
      if (bool) {
        this.Rsync.unlock();
      }
    }
    if (DEBUG > 0) {
      System.out.println("Rengine.eval(" + paramString + "): END (ERR)" + Thread.currentThread());
    }
    return null;
  }
  
  public synchronized REXP idleEval(String paramString)
  {
    return idleEval(paramString, true);
  }
  
  public synchronized REXP idleEval(String paramString, boolean paramBoolean)
  {
    int i = this.Rsync.tryLock();
    if (i == 1) {
      return null;
    }
    int j = i == 0 ? 1 : 0;
    try
    {
      long l1 = rniParse(paramString, 1);
      if (l1 != 0L)
      {
        long l2 = rniEval(l1, 0L);
        if (l2 != 0L)
        {
          REXP localREXP1 = new REXP(this, l2, paramBoolean);
          return localREXP1;
        }
      }
    }
    finally
    {
      if (j != 0) {
        this.Rsync.unlock();
      }
    }
    return null;
  }
  
  public Mutex getRsync()
  {
    return this.Rsync;
  }
  
  public synchronized boolean waitForR()
  {
    return this.alive;
  }
  
  public void end()
  {
    this.alive = false;
    interrupt();
  }
  
  public void run()
  {
    if (DEBUG > 0) {
      System.out.println("Starting R...");
    }
    this.loopHasLock = this.Rsync.safeLock();
    try
    {
      if (setupR(this.args) == 0)
      {
        if ((!this.runLoop) && (this.loopHasLock))
        {
          this.Rsync.unlock();
          this.loopHasLock = false;
        }
        while (this.alive) {
          try
          {
            if (this.runLoop)
            {
              if (DEBUG > 0) {
                System.out.println("***> launching main loop:");
              }
              this.loopRunning = true;
              rniRunMainLoop();
              
              this.loopRunning = false;
              if (DEBUG > 0) {
                System.out.println("***> main loop finished:");
              }
              this.runLoop = false;
              this.died = true; return;
            }
            sleep(this.idleDelay);
            if (this.runLoop) {
              rniIdle();
            }
          }
          catch (InterruptedException localInterruptedException)
          {
            interrupted();
          }
        }
        this.died = true;
        if (DEBUG > 0) {
          System.out.println("Terminating R thread.");
        }
      }
      else
      {
        System.err.println("Unable to start R");
      }
    }
    finally
    {
      if (this.loopHasLock) {
        this.Rsync.unlock();
      }
    }
  }
  
  public boolean assign(String paramString1, String paramString2)
  {
    boolean bool1 = this.Rsync.safeLock();
    try
    {
      long l = rniPutString(paramString2);
      return rniAssign(paramString1, l, 0L);
    }
    finally
    {
      if (bool1) {
        this.Rsync.unlock();
      }
    }
  }
  
  public boolean assign(String paramString, REXP paramREXP)
  {
    boolean bool1 = this.Rsync.safeLock();
    try
    {
      if (paramREXP.Xt == -1) {
        return rniAssign(paramString, paramREXP.xp, 0L);
      }
      Object localObject1;
      long l2;
      boolean bool4;
      if ((paramREXP.Xt == 1) || (paramREXP.Xt == 32))
      {
        localObject1 = paramREXP.rtype == 1 ? new int[] { ((Integer)paramREXP.cont).intValue() } : (int[])paramREXP.cont;
        l2 = rniPutIntArray((int[])localObject1);
        return rniAssign(paramString, l2, 0L);
      }
      if ((paramREXP.Xt == 2) || (paramREXP.Xt == 33))
      {
        localObject1 = paramREXP.rtype == 2 ? new double[] { ((Double)paramREXP.cont).intValue() } : (double[])paramREXP.cont;
        l2 = rniPutDoubleArray((double[])localObject1);
        return rniAssign(paramString, l2, 0L);
      }
      if (paramREXP.Xt == 37)
      {
        long l1 = rniPutBoolArrayI((int[])paramREXP.cont);
        return rniAssign(paramString, l1, 0L);
      }
      if ((paramREXP.Xt == 3) || (paramREXP.Xt == 34))
      {
        String[] arrayOfString = paramREXP.rtype == 3 ? new String[] { (String)paramREXP.cont } : (String[])paramREXP.cont;
        l2 = rniPutStringArray(arrayOfString);
        return rniAssign(paramString, l2, 0L);
      }
    }
    finally
    {
      if (bool1) {
        this.Rsync.unlock();
      }
    }
    return false;
  }
  
  public boolean assign(String paramString, double[] paramArrayOfDouble)
  {
    return assign(paramString, new REXP(paramArrayOfDouble));
  }
  
  public boolean assign(String paramString, int[] paramArrayOfInt)
  {
    return assign(paramString, new REXP(paramArrayOfInt));
  }
  
  public boolean assign(String paramString, boolean[] paramArrayOfBoolean)
  {
    return assign(paramString, new REXP(paramArrayOfBoolean));
  }
  
  public boolean assign(String paramString, String[] paramArrayOfString)
  {
    return assign(paramString, new REXP(paramArrayOfString));
  }
  
  public REXP createRJavaRef(Object paramObject)
  {
    if (paramObject == null) {
      return null;
    }
    String str = paramObject.getClass().getName();
    boolean bool = this.Rsync.safeLock();
    try
    {
      long l = rniEval(rniLCons(rniInstallSymbol(".jmkref"), rniLCons(rniJavaToXref(paramObject), rniLCons(rniPutString(str), 0L))), 0L);
      REXP localREXP;
      if ((l <= 0L) && (l > -4L)) {
        return null;
      }
      return new REXP(this, l, false);
    }
    finally
    {
      if (bool) {
        this.Rsync.unlock();
      }
    }
  }
  
  native int rniSetupR(String[] paramArrayOfString);
  
  public native int rniSetupRJava(int paramInt1, int paramInt2);
  
  public native int rniRJavaLock();
  
  public native int rniRJavaUnlock();
  
  public synchronized native long rniParse(String paramString, int paramInt);
  
  public synchronized native long rniEval(long paramLong1, long paramLong2);
  
  public synchronized native void rniProtect(long paramLong);
  
  public synchronized native void rniUnprotect(int paramInt);
  
  public synchronized native String rniGetString(long paramLong);
  
  public synchronized native String[] rniGetStringArray(long paramLong);
  
  public synchronized native int[] rniGetIntArray(long paramLong);
  
  public synchronized native int[] rniGetBoolArrayI(long paramLong);
  
  public synchronized native double[] rniGetDoubleArray(long paramLong);
  
  public synchronized native byte[] rniGetRawArray(long paramLong);
  
  public synchronized native long[] rniGetVector(long paramLong);
  
  public synchronized native long rniPutString(String paramString);
  
  public synchronized native long rniPutStringArray(String[] paramArrayOfString);
  
  public synchronized native long rniPutIntArray(int[] paramArrayOfInt);
  
  public synchronized native long rniPutBoolArrayI(int[] paramArrayOfInt);
  
  public synchronized native long rniPutBoolArray(boolean[] paramArrayOfBoolean);
  
  public synchronized native long rniPutDoubleArray(double[] paramArrayOfDouble);
  
  public synchronized native long rniPutRawArray(byte[] paramArrayOfByte);
  
  public synchronized native long rniPutVector(long[] paramArrayOfLong);
  
  public synchronized native long rniGetAttr(long paramLong, String paramString);
  
  public synchronized native String[] rniGetAttrNames(long paramLong);
  
  public synchronized native void rniSetAttr(long paramLong1, String paramString, long paramLong2);
  
  public synchronized native boolean rniInherits(long paramLong, String paramString);
  
  public synchronized native long rniCons(long paramLong1, long paramLong2, long paramLong3, boolean paramBoolean);
  
  public synchronized native long rniCAR(long paramLong);
  
  public synchronized native long rniCDR(long paramLong);
  
  public synchronized native long rniTAG(long paramLong);
  
  public synchronized native long rniPutList(long[] paramArrayOfLong);
  
  public synchronized native long[] rniGetList(long paramLong);
  
  public synchronized native String rniGetSymbolName(long paramLong);
  
  public synchronized native long rniInstallSymbol(String paramString);
  
  public synchronized native void rniPrint(String paramString, int paramInt);
  
  public synchronized native void rniPrintValue(long paramLong);
  
  public synchronized native void rniPreserve(long paramLong);
  
  public synchronized native void rniRelease(long paramLong);
  
  public synchronized native long rniParentEnv(long paramLong);
  
  public synchronized native long rniFindVar(String paramString, long paramLong);
  
  public synchronized native long rniListEnv(long paramLong, boolean paramBoolean);
  
  public synchronized native long rniSpecialObject(int paramInt);
  
  public synchronized native long rniJavaToXref(Object paramObject);
  
  public synchronized native Object rniXrefToJava(long paramLong);
  
  public static native long rniGetVersion();
  
  public native int rniStop(int paramInt);
  
  public synchronized native boolean rniAssign(String paramString, long paramLong1, long paramLong2);
  
  public synchronized native int rniExpType(long paramLong);
  
  public native void rniRunMainLoop();
  
  public synchronized native void rniIdle();
}
