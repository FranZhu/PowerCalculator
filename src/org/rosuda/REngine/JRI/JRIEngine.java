package org.rosuda.REngine.JRI;

import org.rosuda.JRI.Mutex;
import org.rosuda.JRI.RMainLoopCallbacks;
import org.rosuda.JRI.Rengine;
import org.rosuda.REngine.REXPDouble;
import org.rosuda.REngine.REXPEnvironment;
import org.rosuda.REngine.REXPFactor;
import org.rosuda.REngine.REXPGenericVector;
import org.rosuda.REngine.REXPInteger;
import org.rosuda.REngine.REXPJavaReference;
import org.rosuda.REngine.REXPLanguage;
import org.rosuda.REngine.REXPList;
import org.rosuda.REngine.REXPLogical;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REXPNull;
import org.rosuda.REngine.REXPRaw;
import org.rosuda.REngine.REXPReference;
import org.rosuda.REngine.REXPS4;
import org.rosuda.REngine.REXPString;
import org.rosuda.REngine.REXPSymbol;
import org.rosuda.REngine.REXPUnknown;
import org.rosuda.REngine.REngine;
import org.rosuda.REngine.REngineCallbacks;
import org.rosuda.REngine.REngineConsoleHistoryInterface;
import org.rosuda.REngine.REngineEvalException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.REngineInputInterface;
import org.rosuda.REngine.REngineOutputInterface;
import org.rosuda.REngine.REngineUIInterface;
import org.rosuda.REngine.RList;

public class JRIEngine
  extends REngine
  implements RMainLoopCallbacks
{
  static final int NILSXP = 0;
  static final int SYMSXP = 1;
  static final int LISTSXP = 2;
  static final int CLOSXP = 3;
  static final int ENVSXP = 4;
  static final int PROMSXP = 5;
  static final int LANGSXP = 6;
  static final int SPECIALSXP = 7;
  static final int BUILTINSXP = 8;
  static final int CHARSXP = 9;
  static final int LGLSXP = 10;
  static final int INTSXP = 13;
  static final int REALSXP = 14;
  static final int CPLXSXP = 15;
  static final int STRSXP = 16;
  static final int DOTSXP = 17;
  static final int ANYSXP = 18;
  static final int VECSXP = 19;
  static final int EXPRSXP = 20;
  static final int BCODESXP = 21;
  static final int EXTPTRSXP = 22;
  static final int WEAKREFSXP = 23;
  static final int RAWSXP = 24;
  static final int S4SXP = 25;
  public static final long requiredAPIversion = 266L;
  static JRIEngine jriEngine = null;
  Rengine rni = null;
  REngineCallbacks callbacks = null;
  Mutex rniMutex = null;
  long R_UnboundValue;
  long R_NilValue;
  public REXPReference globalEnv;
  public REXPReference emptyEnv;
  public REXPReference baseEnv;
  public REXPReference nullValueRef;
  public REXPNull nullValue;
  
  class JRIPointer
  {
    long ptr;
    
    JRIPointer(long paramLong, boolean paramBoolean)
    {
      this.ptr = paramLong;
      if ((paramBoolean) && (paramLong != 0L) && (paramLong != JRIEngine.this.R_NilValue))
      {
        boolean bool = JRIEngine.this.rniMutex.safeLock();
        try
        {
          JRIEngine.this.rni.rniPreserve(paramLong);
        }
        finally
        {
          if (bool) {
            JRIEngine.this.rniMutex.unlock();
          }
        }
      }
    }
    
    protected void finalize()
      throws Throwable
    {
      try
      {
        if ((this.ptr != 0L) && (this.ptr != JRIEngine.this.R_NilValue))
        {
          boolean bool = JRIEngine.this.rniMutex.safeLock();
          try
          {
            JRIEngine.this.rni.rniRelease(this.ptr);
          }
          finally
          {
            if (bool) {
              JRIEngine.this.rniMutex.unlock();
            }
          }
        }
      }
      finally
      {
        super.finalize();
      }
    }
    
    long pointer()
    {
      return this.ptr;
    }
  }
  
  public static REngine createEngine()
    throws REngineException
  {
    if (jriEngine == null) {
      jriEngine = new JRIEngine();
    }
    return jriEngine;
  }
  
  public static REngine createEngine(String[] paramArrayOfString, REngineCallbacks paramREngineCallbacks, boolean paramBoolean)
    throws REngineException
  {
    if (jriEngine != null) {
      throw new REngineException(jriEngine, "engine already running - cannot use extended constructor on a running instance");
    }
    return JRIEngine.jriEngine = new JRIEngine(paramArrayOfString, paramREngineCallbacks, paramBoolean);
  }
  
  public Rengine getRni()
  {
    return this.rni;
  }
  
  public JRIEngine()
    throws REngineException
  {
    this(new String[] { "--no-save" }, (REngineCallbacks)null, false);
  }
  
  public JRIEngine(String[] paramArrayOfString)
    throws REngineException
  {
    this(paramArrayOfString, (REngineCallbacks)null, false);
  }
  
  public JRIEngine(String[] paramArrayOfString, RMainLoopCallbacks paramRMainLoopCallbacks)
    throws REngineException
  {
    this(paramArrayOfString, paramRMainLoopCallbacks, paramRMainLoopCallbacks != null);
  }
  
  public JRIEngine(String[] paramArrayOfString, REngineCallbacks paramREngineCallbacks, boolean paramBoolean)
    throws REngineException
  {
    if (!Rengine.jriLoaded) {
      throw new REngineException(null, "Cannot load JRI native library");
    }
    if (Rengine.getVersion() < 266L) {
      throw new REngineException(null, "JRI API version is too old, update rJava/JRI to match the REngine API");
    }
    this.callbacks = paramREngineCallbacks;
    
    this.rni = new Rengine(paramArrayOfString, paramBoolean, paramREngineCallbacks == null ? null : this);
    this.rniMutex = this.rni.getRsync();
    boolean bool = this.rniMutex.safeLock();
    try
    {
      if (!this.rni.waitForR()) {
        throw new REngineException(this, "Unable to initialize R");
      }
      if (Rengine.rniGetVersion() < 266L) {
        throw new REngineException(this, "JRI API version is too old, update rJava/JRI to match the REngine API");
      }
      this.globalEnv = new REXPReference(this, new Long(this.rni.rniSpecialObject(1)));
      this.nullValueRef = new REXPReference(this, new Long(this.R_NilValue = this.rni.rniSpecialObject(0)));
      this.emptyEnv = new REXPReference(this, new Long(this.rni.rniSpecialObject(2)));
      this.baseEnv = new REXPReference(this, new Long(this.rni.rniSpecialObject(3)));
      this.nullValue = new REXPNull();
      this.R_UnboundValue = this.rni.rniSpecialObject(4);
    }
    finally
    {
      if (bool) {
        this.rniMutex.unlock();
      }
    }
    lastEngine = this;
    if (jriEngine == null) {
      jriEngine = this;
    }
  }
  
  public JRIEngine(String[] paramArrayOfString, RMainLoopCallbacks paramRMainLoopCallbacks, boolean paramBoolean)
    throws REngineException
  {
    if (!Rengine.jriLoaded) {
      throw new REngineException(null, "Cannot load JRI native library");
    }
    if (Rengine.getVersion() < 266L) {
      throw new REngineException(null, "JRI API version is too old, update rJava/JRI to match the REngine API");
    }
    this.rni = new Rengine(paramArrayOfString, paramBoolean, paramRMainLoopCallbacks);
    this.rniMutex = this.rni.getRsync();
    boolean bool = this.rniMutex.safeLock();
    try
    {
      if (!this.rni.waitForR()) {
        throw new REngineException(this, "Unable to initialize R");
      }
      if (Rengine.rniGetVersion() < 266L) {
        throw new REngineException(this, "JRI API version is too old, update rJava/JRI to match the REngine API");
      }
      this.globalEnv = new REXPReference(this, new Long(this.rni.rniSpecialObject(1)));
      this.nullValueRef = new REXPReference(this, new Long(this.R_NilValue = this.rni.rniSpecialObject(0)));
      this.emptyEnv = new REXPReference(this, new Long(this.rni.rniSpecialObject(2)));
      this.baseEnv = new REXPReference(this, new Long(this.rni.rniSpecialObject(3)));
      this.nullValue = new REXPNull();
      this.R_UnboundValue = this.rni.rniSpecialObject(4);
    }
    finally
    {
      if (bool) {
        this.rniMutex.unlock();
      }
    }
    lastEngine = this;
    if (jriEngine == null) {
      jriEngine = this;
    }
  }
  
  public JRIEngine(Rengine paramRengine)
    throws REngineException
  {
    if (!Rengine.jriLoaded) {
      throw new REngineException(null, "Cannot load JRI native library");
    }
    this.rni = paramRengine;
    if (Rengine.rniGetVersion() < 265L) {
      throw new REngineException(this, "R JRI engine is too old - RNI API 1.9 (JRI 0.5) or newer is required");
    }
    this.rniMutex = this.rni.getRsync();
    boolean bool = this.rniMutex.safeLock();
    try
    {
      this.globalEnv = new REXPReference(this, new Long(this.rni.rniSpecialObject(1)));
      this.nullValueRef = new REXPReference(this, new Long(this.R_NilValue = this.rni.rniSpecialObject(0)));
      this.emptyEnv = new REXPReference(this, new Long(this.rni.rniSpecialObject(2)));
      this.baseEnv = new REXPReference(this, new Long(this.rni.rniSpecialObject(3)));
      this.nullValue = new REXPNull();
      this.R_UnboundValue = this.rni.rniSpecialObject(4);
    }
    finally
    {
      if (bool) {
        this.rniMutex.unlock();
      }
    }
    lastEngine = this;
    if (jriEngine == null) {
      jriEngine = this;
    }
  }
  
  public org.rosuda.REngine.REXP parse(String paramString, boolean paramBoolean)
    throws REngineException
  {
	  org.rosuda.REngine.REXP localObject1 = null;
    boolean bool = this.rniMutex.safeLock();
    try
    {
      long l = this.rni.rniParse(paramString, -1);
      if ((l == 0L) || (l == this.R_NilValue)) {
        throw new REngineException(this, "Parse error");
      }
      this.rni.rniPreserve(l);
      localObject1 = new REXPReference(this, new Long(l));
      if (paramBoolean) {
        try
        {
          localObject1 = resolveReference((org.rosuda.REngine.REXP)localObject1);
        }
        catch (REXPMismatchException localREXPMismatchException) {}
      }
    }
    finally
    {
      if (bool) {
        this.rniMutex.unlock();
      }
    }
    return localObject1;
  }
  
  public org.rosuda.REngine.REXP eval(org.rosuda.REngine.REXP paramREXP1, org.rosuda.REngine.REXP paramREXP2, boolean paramBoolean)
    throws REngineException, REXPMismatchException
  {
	org.rosuda.REngine.REXP localObject1 = null;
    long l1 = 0L;
    if ((paramREXP2 != null) && (!paramREXP2.isReference()))
    {
      if ((!paramREXP2.isEnvironment()) || (((REXPEnvironment)paramREXP2).getHandle() == null)) {
        throw new REXPMismatchException(paramREXP2, "environment");
      }
      l1 = ((JRIPointer)((REXPEnvironment)paramREXP2).getHandle()).pointer();
    }
    else if (paramREXP2 != null)
    {
      l1 = ((Long)((REXPReference)paramREXP2).getHandle()).longValue();
    }
    if (paramREXP1 == null) {
      throw new REngineException(this, "null object to evaluate");
    }
    if (!paramREXP1.isReference()) {
      if ((paramREXP1.isExpression()) || (paramREXP1.isLanguage())) {
        paramREXP1 = createReference(paramREXP1);
      } else {
        throw new REXPMismatchException(paramREXP2, "reference, expression or language");
      }
    }
    boolean bool = this.rniMutex.safeLock();
    try
    {
      long l2 = this.rni.rniEval(((Long)((REXPReference)paramREXP1).getHandle()).longValue(), l1);
      if (l2 == -1L) {
        throw new REngineEvalException(this, "Eval error (invalid input)", -1);
      }
      if (l2 == -2L) {
        throw new REngineEvalException(this, "error during evaluation", -2);
      }
      this.rni.rniPreserve(l2);
      localObject1 = new REXPReference(this, new Long(l2));
      if (paramBoolean) {
        localObject1 = resolveReference((org.rosuda.REngine.REXP)localObject1);
      }
    }
    finally
    {
      if (bool) {
        this.rniMutex.unlock();
      }
    }
    return localObject1;
  }
  
  public void assign(String paramString, org.rosuda.REngine.REXP paramREXP1, org.rosuda.REngine.REXP paramREXP2)
    throws REngineException, REXPMismatchException
  {
    long l = 0L;
    if ((paramREXP2 != null) && (!paramREXP2.isReference()))
    {
      if ((!paramREXP2.isEnvironment()) || (((REXPEnvironment)paramREXP2).getHandle() == null)) {
        throw new REXPMismatchException(paramREXP2, "environment");
      }
      l = ((JRIPointer)((REXPEnvironment)paramREXP2).getHandle()).pointer();
    }
    else if (paramREXP2 != null)
    {
      l = ((Long)((REXPReference)paramREXP2).getHandle()).longValue();
    }
    if (paramREXP1 == null) {
      paramREXP1 = this.nullValueRef;
    }
    if (!paramREXP1.isReference()) {
      paramREXP1 = createReference(paramREXP1);
    }
    boolean bool1 = this.rniMutex.safeLock();boolean bool2 = false;
    try
    {
      bool2 = this.rni.rniAssign(paramString, ((Long)((REXPReference)paramREXP1).getHandle()).longValue(), l);
    }
    finally
    {
      if (bool1) {
        this.rniMutex.unlock();
      }
    }
    if (!bool2) {
      throw new REngineException(this, "assign failed (probably locked binding");
    }
  }
  
  public org.rosuda.REngine.REXP get(String paramString, org.rosuda.REngine.REXP paramREXP, boolean paramBoolean)
    throws REngineException, REXPMismatchException
  {
	  org.rosuda.REngine.REXP localObject1 = null;
    long l1 = 0L;
    if ((paramREXP != null) && (!paramREXP.isReference()))
    {
      if ((!paramREXP.isEnvironment()) || (((REXPEnvironment)paramREXP).getHandle() == null)) {
        throw new REXPMismatchException(paramREXP, "environment");
      }
      l1 = ((JRIPointer)((REXPEnvironment)paramREXP).getHandle()).pointer();
    }
    else if (paramREXP != null)
    {
      l1 = ((Long)((REXPReference)paramREXP).getHandle()).longValue();
    }
    boolean bool = this.rniMutex.safeLock();
    try
    {
      long l2 = this.rni.rniFindVar(paramString, l1);
      if ((l2 == this.R_UnboundValue) || (l2 == 0L)) {
        return null;
      }
      this.rni.rniPreserve(l2);
      localObject1 = new REXPReference(this, new Long(l2));
      if (paramBoolean) {
        try
        {
          localObject1 = resolveReference((org.rosuda.REngine.REXP)localObject1);
        }
        catch (REXPMismatchException localREXPMismatchException) {}
      }
    }
    finally
    {
      if (bool) {
        this.rniMutex.unlock();
      }
    }
    return localObject1;
  }
  
  public org.rosuda.REngine.REXP resolveReference(org.rosuda.REngine.REXP paramREXP)
    throws REngineException, REXPMismatchException
  {
    Object localObject = null;
    if (paramREXP == null) {
      throw new REngineException(this, "resolveReference called on NULL input");
    }
    if (!paramREXP.isReference()) {
      throw new REXPMismatchException(paramREXP, "reference");
    }
    long l = ((Long)((REXPReference)paramREXP).getHandle()).longValue();
    if (l == 0L) {
      return this.nullValue;
    }
    return resolvePointer(l);
  }
  
  org.rosuda.REngine.REXP resolvePointer(long paramLong)
    throws REngineException, REXPMismatchException
  {
    if (paramLong == 0L) {
      return this.nullValue;
    }
    Object localObject1 = null;
    boolean bool = this.rniMutex.safeLock();
    try
    {
      int i = this.rni.rniExpType(paramLong);
      String[] arrayOfString = this.rni.rniGetAttrNames(paramLong);
      REXPList localREXPList = null;
      Object localObject4;
      Object localObject5;
      if ((arrayOfString != null) && (arrayOfString.length > 0))
      {
        long l1 = 0L;
        localObject4 = null;
        localObject5 = new RList();
        for (int k = 0; k < arrayOfString.length; k++)
        {
          long l6 = this.rni.rniGetAttr(paramLong, arrayOfString[k]);
          if ((l6 != 0L) && (l6 != this.R_NilValue))
          {
            if (arrayOfString[k].equals("jobj")) {
              l1 = l6;
            }
            org.rosuda.REngine.REXP localREXP1 = resolvePointer(l6);
            if ((localREXP1 != null) && (localREXP1 != this.nullValue))
            {
              ((RList)localObject5).put(arrayOfString[k], localREXP1);
              if ((arrayOfString[k].equals("class")) && (localREXP1.isString())) {
                localObject4 = localREXP1.asString();
              }
            }
          }
        }
        if (((RList)localObject5).size() > 0) {
          localREXPList = new REXPList((RList)localObject5);
        }
        if ((l1 != 0L) && (localObject4 != null) && ((((String)localObject4).equals("jobjRef")) || (((String)localObject4).equals("jarrayRef")) || (((String)localObject4).equals("jrectRef")))) {
          return new REXPJavaReference(this.rni.rniXrefToJava(l1), localREXPList);
        }
      }
      Object localObject2;
      Object localObject6;
      int [] localObject3;
      switch (i)
      {
      case 0: 
        return this.nullValue;
      case 16: 
        localObject2 = this.rni.rniGetStringArray(paramLong);
        localObject1 = new REXPString((String[])localObject2, localREXPList);
        break;
      case 13: 
        if (this.rni.rniInherits(paramLong, "factor"))
        {
          long l2 = this.rni.rniGetAttr(paramLong, "levels");
          if (l2 != 0L)
          {
            localObject5 = null;
            
            int m = this.rni.rniExpType(l2);
            if (m == 16)
            {
              localObject5 = this.rni.rniGetStringArray(l2);
              localObject6 = this.rni.rniGetIntArray(paramLong);
              localObject1 = new REXPFactor((int[])localObject6, (String[])localObject5, localREXPList);
            }
          }
        }
        if (localObject1 == null) {
          localObject1 = new REXPInteger(this.rni.rniGetIntArray(paramLong), localREXPList);
        }
        break;
      case 14: 
        localObject1 = new REXPDouble(this.rni.rniGetDoubleArray(paramLong), localREXPList);
        break;
      case 10: 
        localObject3 = this.rni.rniGetBoolArrayI(paramLong);
        byte [] localObject14 = new byte[localObject3.length];
        for (int j = 0; j < localObject3.length; j++) {
          localObject14[j] = ((localObject3[j] == 0) || (localObject3[j] == 1) ? (byte)localObject3[j] : -128);
        }
        localObject1 = new REXPLogical((byte[])localObject14, localREXPList);
        
        break;
      case 19: 
        long[] localObject23 = this.rni.rniGetVector(paramLong);
        org.rosuda.REngine.REXP[] localObject24 = new org.rosuda.REngine.REXP[localObject23.length];
        long l4 = this.rni.rniGetAttr(paramLong, "names");
        localObject6 = null;
        if ((l4 != 0L) && (this.rni.rniExpType(l4) == 16)) {
          localObject6 = this.rni.rniGetStringArray(l4);
        }
        for (int n = 0; n < localObject23.length; n++) {
          localObject24[n] = resolvePointer(localObject23[n]);
        }
        RList localRList = localObject6 == null ? new RList((org.rosuda.REngine.REXP[])localObject24) : new RList((org.rosuda.REngine.REXP[])localObject24, (String[])localObject6);
        localObject1 = new REXPGenericVector(localRList, localREXPList);
        
        break;
      case 24: 
        localObject1 = new REXPRaw(this.rni.rniGetRawArray(paramLong), localREXPList);
        break;
      case 2: 
      case 6: 
        RList localObject33 = new RList();
        
        long l3 = paramLong;
        while ((l3 != 0L) && (l3 != this.R_NilValue))
        {
          long l5 = this.rni.rniCAR(l3);
          long l7 = this.rni.rniTAG(l3);
          String str = null;
          if (this.rni.rniExpType(l7) == 1) {
            str = this.rni.rniGetSymbolName(l7);
          }
          org.rosuda.REngine.REXP localREXP2 = resolvePointer(l5);
          if (str == null) {
            ((RList)localObject33).add(localREXP2);
          } else {
            ((RList)localObject33).put(str, localREXP2);
          }
          l3 = this.rni.rniCDR(l3);
        }
        localObject1 = i == 6 ? new REXPLanguage((RList)localObject33, localREXPList) : new REXPList((RList)localObject33, localREXPList);
        
        break;
      case 1: 
        localObject1 = new REXPSymbol(this.rni.rniGetSymbolName(paramLong));
        break;
      case 4: 
        if (paramLong != 0L) {
          this.rni.rniPreserve(paramLong);
        }
        localObject1 = new REXPEnvironment(this, new JRIPointer(paramLong, false));
        break;
      case 25: 
        localObject1 = new REXPS4(localREXPList);
        break;
      case 3: 
      case 5: 
      case 7: 
      case 8: 
      case 9: 
      case 11: 
      case 12: 
      case 15: 
      case 17: 
      case 18: 
      case 20: 
      case 21: 
      case 22: 
      case 23: 
      default: 
        localObject1 = new REXPUnknown(i, localREXPList);
      }
    }
    finally
    {
      if (bool) {
        this.rniMutex.unlock();
      }
    }
    return (org.rosuda.REngine.REXP)localObject1;
  }
  
  public org.rosuda.REngine.REXP createReference(org.rosuda.REngine.REXP paramREXP)
    throws REngineException, REXPMismatchException
  {
    if (paramREXP == null) {
      throw new REngineException(this, "createReference from a NULL value");
    }
    if (paramREXP.isReference()) {
      return paramREXP;
    }
    long l = createReferencePointer(paramREXP);
    if (l == 0L) {
      return null;
    }
    boolean bool = this.rniMutex.safeLock();
    try
    {
      this.rni.rniPreserve(l);
    }
    finally
    {
      if (bool) {
        this.rniMutex.unlock();
      }
    }
    return new REXPReference(this, new Long(l));
  }
  
  long createReferencePointer(org.rosuda.REngine.REXP paramREXP)
    throws REngineException, REXPMismatchException
  {
    if (paramREXP.isReference())
    {
      REXPReference localREXPReference = (REXPReference)paramREXP;
      if (localREXPReference.getEngine() != this) {
        throw new REXPMismatchException(paramREXP, "reference (cross-engine reference is invalid)");
      }
      return ((Long)localREXPReference.getHandle()).longValue();
    }
    boolean bool = this.rniMutex.safeLock();
    int i = 0;
    try
    {
      long l1 = 0L;
      if (paramREXP.isNull()) {
        return this.R_NilValue;
      }
      int[] localObject1;
      Object localObject3;
      long l10;
      long l6;
      if (paramREXP.isLogical())
      {
        localObject1 = paramREXP.asIntegers();
        for (int k = 0; k < localObject1.length; k++) {
          localObject1[k] = (localObject1[k] == 0 ? 0 : localObject1[k] < 0 ? 2 : 1);
        }
        l1 = this.rni.rniPutBoolArrayI((int[])localObject1);
      }
      else if (paramREXP.isInteger())
      {
        l1 = this.rni.rniPutIntArray(paramREXP.asIntegers());
      }
      else if (paramREXP.isRaw())
      {
        l1 = this.rni.rniPutRawArray(paramREXP.asBytes());
      }
      else if (paramREXP.isNumeric())
      {
        l1 = this.rni.rniPutDoubleArray(paramREXP.asDoubles());
      }
      else if (paramREXP.isString())
      {
        l1 = this.rni.rniPutStringArray(paramREXP.asStrings());
      }
      else if (paramREXP.isEnvironment())
      {
    	  JRIPointer localObject111 = (JRIPointer)((REXPEnvironment)paramREXP).getHandle();
        if (localObject111 == null)
        {
          long l5 = this.rni.rniParse("new.env(parent=baseenv())", 1);
          l1 = this.rni.rniEval(l5, 0L);
        }
        else
        {
          l1 = ((JRIPointer)localObject111).pointer();
        }
      }
      else
      {
        int j;
        RList localRList1;
        int i1;
        long l12;
        long l13;
        if (paramREXP.isPairList())
        {
          if (paramREXP.isLanguage()) j = 1;
          else j=0;	//paramREXP.isLanguage();
          localRList1 = paramREXP.asList();
          l1 = this.R_NilValue;
          int m = localRList1.size();
          if (m == 0) {
            l1 = this.rni.rniCons(this.R_NilValue, 0L, 0L, (j != 0));
          } else {
            for (i1 = m - 1; i1 >= 0; i1--)
            {
              localObject3 = localRList1.at(i1);
              String str1 = localRList1.keyAt(i1);
              long l11 = 0L;
              if (str1 != null) {
                l11 = this.rni.rniInstallSymbol(str1);
              }
              l12 = createReferencePointer((org.rosuda.REngine.REXP)localObject3);
              if (l12 == 0L) {
                l12 = this.R_NilValue;
              }
              l13 = this.rni.rniCons(l12, l1, l11, (i1 == 0) && (j != 0));
              this.rni.rniPreserve(l13);
              this.rni.rniRelease(l1);
              l1 = l13;
            }
          }
        }
        else if (paramREXP.isList())
        {
          j = i;
          localRList1 = paramREXP.asList();
          long[] arrayOfLong = new long[localRList1.size()];
          for (i1 = 0; i1 < arrayOfLong.length; i1++)
          {
            localObject3 = localRList1.at(i1);
            if ((localObject3 == null) || (((org.rosuda.REngine.REXP)localObject3).isNull()))
            {
              arrayOfLong[i1] = this.R_NilValue;
            }
            else
            {
              l10 = createReferencePointer((org.rosuda.REngine.REXP)localObject3);
              if ((l10 != 0L) && (l10 != this.R_NilValue))
              {
                this.rni.rniProtect(l10);
                i++;
              }
              else
              {
                l10 = this.R_NilValue;
              }
              arrayOfLong[i1] = l10;
            }
          }
          l1 = this.rni.rniPutVector(arrayOfLong);
          if (j > i)
          {
            this.rni.rniUnprotect(i - j);
            i = j;
          }
        }
        else
        {
          if (paramREXP.isSymbol()) {
            return this.rni.rniInstallSymbol(paramREXP.asString());
          }
          if ((paramREXP instanceof REXPJavaReference))
          {
            Object localObject2 = ((REXPJavaReference)paramREXP).getObject();
            l6 = this.rni.rniJavaToXref(localObject2);
            this.rni.rniProtect(l6);
            long l9 = this.rni.rniInstallSymbol("jobj");
            l10 = this.rni.rniInstallSymbol("jclass");
            String str2 = "java/lang/Object";
            if (localObject2 != null)
            {
              str2 = localObject2.getClass().getName();
              str2 = str2.replace('.', '/');
            }
            l12 = this.rni.rniPutString(str2);
            this.rni.rniProtect(l12);
            l13 = this.rni.rniPutString("jobjRef");
            this.rni.rniProtect(l13);
            long l14 = this.rni.rniEval(this.rni.rniLCons(this.rni.rniInstallSymbol("new"), this.rni.rniCons(l13, this.rni.rniCons(l6, this.rni.rniCons(l12, this.R_NilValue, l10, false), l9, false))), 0L);
            




            this.rni.rniUnprotect(3);
            l1 = l14;
          }
        }
      }
      if (l1 == this.R_NilValue) {
        return l1;
      }
      if (l1 != 0L)
      {
        REXPList localREXPList = paramREXP._attr();
        if ((localREXPList == null) || (!localREXPList.isPairList())) {
          return l1;
        }
        RList localRList2 = localREXPList.asList();
        if ((localRList2 == null) || (localRList2.size() < 1) || (!localRList2.isNamed())) {
          return l1;
        }
        this.rni.rniProtect(l1);
        i++;
        for (int n = 0; n < localRList2.size(); n++)
        {
          org.rosuda.REngine.REXP localREXP = localRList2.at(n);
          localObject3 = localRList2.keyAt(n);
          if (localObject3 != null)
          {
            l10 = createReferencePointer(localREXP);
            if ((l10 != 0L) && (l10 != this.R_NilValue)) {
              this.rni.rniSetAttr(l1, (String)localObject3, l10);
            }
          }
        }
        return l1;
      }
    }
    finally
    {
      if (i > 0) {
        this.rni.rniUnprotect(i);
      }
      if (bool) {
        this.rniMutex.unlock();
      }
    }
    return 0L;
  }
  
  public void finalizeReference(org.rosuda.REngine.REXP paramREXP)
    throws REngineException, REXPMismatchException
  {
    if ((paramREXP != null) && (paramREXP.isReference()))
    {
      long l = ((Long)((REXPReference)paramREXP).getHandle()).longValue();
      boolean bool = this.rniMutex.safeLock();
      try
      {
        this.rni.rniRelease(l);
      }
      finally
      {
        if (bool) {
          this.rniMutex.unlock();
        }
      }
    }
  }
  
  public org.rosuda.REngine.REXP getParentEnvironment(org.rosuda.REngine.REXP paramREXP, boolean paramBoolean)
    throws REngineException, REXPMismatchException
  {
    Object localObject1 = null;
    long l1 = 0L;
    if ((paramREXP != null) && (!paramREXP.isReference()))
    {
      if ((!paramREXP.isEnvironment()) || (((REXPEnvironment)paramREXP).getHandle() == null)) {
        throw new REXPMismatchException(paramREXP, "environment");
      }
      l1 = ((JRIPointer)((REXPEnvironment)paramREXP).getHandle()).pointer();
    }
    else if (paramREXP != null)
    {
      l1 = ((Long)((REXPReference)paramREXP).getHandle()).longValue();
    }
    boolean bool = this.rniMutex.safeLock();
    try
    {
      long l2 = this.rni.rniParentEnv(l1);
      if ((l2 == 0L) || (l2 == this.R_NilValue)) {
        return null;
      }
      this.rni.rniPreserve(l2);
      localObject1 = new REXPReference(this, new Long(l2));
      if (paramBoolean) {
        localObject1 = resolveReference((org.rosuda.REngine.REXP)localObject1);
      }
    }
    finally
    {
      if (bool) {
        this.rniMutex.unlock();
      }
    }
    return (org.rosuda.REngine.REXP)localObject1;
  }
  
  public org.rosuda.REngine.REXP newEnvironment(org.rosuda.REngine.REXP paramREXP, boolean paramBoolean)
    throws REXPMismatchException, REngineException
  {
    Object localObject1 = null;
    boolean bool = this.rniMutex.safeLock();
    try
    {
      long l1 = 0L;
      if ((paramREXP != null) && (!paramREXP.isReference()))
      {
        if ((!paramREXP.isEnvironment()) || (((REXPEnvironment)paramREXP).getHandle() == null)) {
          throw new REXPMismatchException(paramREXP, "environment");
        }
        l1 = ((JRIPointer)((REXPEnvironment)paramREXP).getHandle()).pointer();
      }
      else if (paramREXP != null)
      {
        l1 = ((Long)((REXPReference)paramREXP).getHandle()).longValue();
      }
      if (l1 == 0L) {
        l1 = ((Long)this.globalEnv.getHandle()).longValue();
      }
      long l2 = this.rni.rniEval(this.rni.rniLCons(this.rni.rniInstallSymbol("new.env"), this.rni.rniCons(l1, this.R_NilValue, this.rni.rniInstallSymbol("parent"), false)), 0L);
      if (l2 != 0L) {
        this.rni.rniPreserve(l2);
      }
      localObject1 = new REXPReference(this, new Long(l2));
      if (paramBoolean) {
        localObject1 = resolveReference((org.rosuda.REngine.REXP)localObject1);
      }
    }
    finally
    {
      if (bool) {
        this.rniMutex.unlock();
      }
    }
    return (org.rosuda.REngine.REXP)localObject1;
  }
  
  public boolean close()
  {
    if (this.rni == null) {
      return false;
    }
    this.rni.end();
    return true;
  }
  
  public synchronized int tryLock()
  {
    int i = this.rniMutex.tryLock();
    return i == -1 ? 2 : i == 1 ? 0 : 1;
  }
  
  public synchronized int lock()
  {
    return this.rniMutex.safeLock() ? 1 : 2;
  }
  
  public synchronized void unlock(int paramInt)
  {
    if (paramInt == 1) {
      this.rniMutex.unlock();
    }
  }
  
  public boolean supportsReferences()
  {
    return true;
  }
  
  public boolean supportsEnvironments()
  {
    return true;
  }
  
  public boolean supportsLocking()
  {
    return true;
  }
  
  public REXPReference createRJavaRef(Object paramObject)
    throws REngineException
  {
    if (paramObject == null) {
      return null;
    }
    REXPReference localREXPReference = null;
    boolean bool = this.rniMutex.safeLock();
    try
    {
      org.rosuda.JRI.REXP localREXP = this.rni.createRJavaRef(paramObject);
      if (localREXP == null) {
        throw new REngineException(this, "Could not push java Object to R");
      }
      long l = localREXP.xp;
      this.rni.rniPreserve(l);
      localREXPReference = new REXPReference(this, new Long(l));
    }
    finally
    {
      if (bool) {
        this.rniMutex.unlock();
      }
    }
    return localREXPReference;
  }
  
  public void rWriteConsole(Rengine paramRengine, String paramString, int paramInt)
  {
    if ((this.callbacks != null) && ((this.callbacks instanceof REngineOutputInterface))) {
      ((REngineOutputInterface)this.callbacks).RWriteConsole(this, paramString, paramInt);
    }
  }
  
  public void rBusy(Rengine paramRengine, int paramInt)
  {
    if ((this.callbacks != null) && ((this.callbacks instanceof REngineUIInterface))) {
      ((REngineUIInterface)this.callbacks).RBusyState(this, paramInt);
    }
  }
  
  public synchronized String rReadConsole(Rengine paramRengine, String paramString, int paramInt)
  {
    if ((this.callbacks != null) && ((this.callbacks instanceof REngineInputInterface))) {
      return ((REngineInputInterface)this.callbacks).RReadConsole(this, paramString, paramInt);
    }
    try
    {
      wait();
    }
    catch (Exception localException) {}
    return "";
  }
  
  public void rShowMessage(Rengine paramRengine, String paramString)
  {
    if ((this.callbacks != null) && ((this.callbacks instanceof REngineOutputInterface))) {
      ((REngineOutputInterface)this.callbacks).RShowMessage(this, paramString);
    }
  }
  
  public String rChooseFile(Rengine paramRengine, int paramInt)
  {
    if ((this.callbacks != null) && ((this.callbacks instanceof REngineUIInterface))) {
      return ((REngineUIInterface)this.callbacks).RChooseFile(this, paramInt == 0);
    }
    return null;
  }
  
  public void rFlushConsole(Rengine paramRengine)
  {
    if ((this.callbacks != null) && ((this.callbacks instanceof REngineOutputInterface))) {
      ((REngineOutputInterface)this.callbacks).RFlushConsole(this);
    }
  }
  
  public void rSaveHistory(Rengine paramRengine, String paramString)
  {
    if ((this.callbacks != null) && ((this.callbacks instanceof REngineConsoleHistoryInterface))) {
      ((REngineConsoleHistoryInterface)this.callbacks).RSaveHistory(this, paramString);
    }
  }
  
  public void rLoadHistory(Rengine paramRengine, String paramString)
  {
    if ((this.callbacks != null) && ((this.callbacks instanceof REngineConsoleHistoryInterface))) {
      ((REngineConsoleHistoryInterface)this.callbacks).RLoadHistory(this, paramString);
    }
  }
}
