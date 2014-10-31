package org.rosuda.REngine;

import java.io.PrintStream;

public class REXPWrapper
{
  private static Class byte_ARRAY;
  private static Class short_ARRAY;
  private static Class int_ARRAY;
  private static Class long_ARRAY;
  private static Class float_ARRAY;
  private static Class double_ARRAY;
  private static Class boolean_ARRAY;
  private static Class String_ARRAY;
  private static Class Byte_ARRAY;
  private static Class Short_ARRAY;
  private static Class Integer_ARRAY;
  private static Class Long_ARRAY;
  private static Class Float_ARRAY;
  private static Class Double_ARRAY;
  private static Class Boolean_ARRAY;
  
  static
  {
    try
    {
      byte_ARRAY = Class.forName("[B");
      short_ARRAY = Class.forName("[S");
      int_ARRAY = Class.forName("[I");
      long_ARRAY = new long[1].getClass();
      float_ARRAY = Class.forName("[F");
      double_ARRAY = Class.forName("[D");
      boolean_ARRAY = Class.forName("[Z");
      
      String_ARRAY = Class.forName("[Ljava.lang.String;");
      
      Byte_ARRAY = Class.forName("[Ljava.lang.Byte;");
      Short_ARRAY = Class.forName("[Ljava.lang.Short;");
      Integer_ARRAY = Class.forName("[Ljava.lang.Integer;");
      Long_ARRAY = Class.forName("[Ljava.lang.Long;");
      Float_ARRAY = Class.forName("[Ljava.lang.Float;");
      Double_ARRAY = Class.forName("[Ljava.lang.Double;");
      Boolean_ARRAY = Class.forName("[Ljava.lang.Boolean;");
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
      System.err.println("problem while initiating the classes");
    }
  }
  
  public static REXP wrap(Object paramObject)
  {
    if ((paramObject instanceof REXP)) {
      return (REXP)paramObject;
    }
    Class localClass = paramObject.getClass();
    byte [] localObject1;
    if (localClass == Byte.class)
    {
      localObject1 = new byte[1];
      localObject1[0] = ((Byte)paramObject).byteValue();
      return new REXPRaw((byte[])localObject1);
    }
    if (localClass == Short.class) {
      return new REXPInteger(((Short)paramObject).intValue());
    }
    if (localClass == Integer.class) {
      return new REXPInteger(((Integer)paramObject).intValue());
    }
    if (localClass == Long.class) {
      return new REXPInteger(((Long)paramObject).intValue());
    }
    if (localClass == Float.class) {
      return new REXPDouble(((Float)paramObject).doubleValue());
    }
    if (localClass == Double.class) {
      return new REXPDouble(((Double)paramObject).doubleValue());
    }
    if (localClass == Boolean.class) {
      return new REXPLogical(((Boolean)paramObject).booleanValue());
    }
    if (localClass == String.class) {
      return new REXPString((String)paramObject);
    }
    if (localClass == String_ARRAY) {
      return new REXPString((String[])paramObject);
    }
    if (localClass == byte_ARRAY) {
      return new REXPRaw((byte[])paramObject);
    }
    int n;
    if (localClass == Byte_ARRAY)
    {
      byte [] localObject11;  localObject11 = (byte[])paramObject;
      int i = localObject11.length;
      byte[] arrayOfByte = new byte[localObject11.length];
      for (n = 0; n < i; n++) {
        arrayOfByte[n] = (new Byte(localObject11[n])).byteValue();
      }
      return new REXPRaw(arrayOfByte);
    }
    if (localClass == short_ARRAY)
    {
      short[] localObject21;  localObject21 = (short[])paramObject;
      int[] arrayOfInt = new int[localObject21.length];
      int m = arrayOfInt.length;
      for (n = 0; n < m; n++) {
        arrayOfInt[n] = localObject21[n];
      }
      return new REXPInteger(arrayOfInt);
    }
    int j;
    Object localObject2;
    if (localClass == Short_ARRAY)
    {
    	Short[] localObject31; localObject31 = (Short[])paramObject;
      j = localObject31.length;
      int[] localObject22;  localObject22 = new int[localObject31.length];
      for (n = 0; n < j; n++) {
        localObject22[n] = localObject31[n].intValue();
      }
      return new REXPInteger((int[])localObject22);
    }
    if (localClass == int_ARRAY) {
      return new REXPInteger((int[])paramObject);
    }
    if (localClass == Integer_ARRAY)
    {
    	Integer[] localObject41;  localObject41 = (Integer[])paramObject;
      j = localObject41.length;
      int[] localObject42; localObject42 = new int[localObject41.length];
      for (n = 0; n < j; n++) {
        localObject42[n] = localObject41[n].intValue();
      }
      return new REXPInteger((int[])localObject42);
    }
    if (localClass == long_ARRAY)
    {
      long[] localObject61 = (long[])paramObject;
      j = localObject61.length;
      int[] localObject62 = new int[localObject61.length];
      for (n = 0; n < j; n++) {
        localObject62[n] = ((int)localObject61[n]);
      }
      return new REXPInteger((int[])localObject62);
    }
    if (localClass == Long_ARRAY)
    {
      Long[] localObject71 = (Long[])paramObject;
      j = localObject71.length;
      int[] localObject72 = new int[localObject71.length];
      for (n = 0; n < j; n++) {
        localObject72[n] = localObject71[n].intValue();
      }
      return new REXPInteger((int[])localObject72);
    }
    if (localClass == float_ARRAY)
    {
      float[] localObject81 = (float[])paramObject;
      j = localObject81.length;
      double [] localObject82 = new double[localObject81.length];
      for (n = 0; n < j; n++) {
        localObject82[n] = localObject81[n];
      }
      return new REXPDouble((double[])localObject82);
    }
    if (localClass == Float_ARRAY)
    {
      Float [] localObject91 = (Float[])paramObject;
      j = localObject91.length;
      double [] localObject92 = new double[localObject91.length];
      for (n = 0; n < j; n++) {
        localObject92[n] = localObject91[n].doubleValue();
      }
      return new REXPDouble((double[])localObject92);
    }
    if (localClass == double_ARRAY) {
      return new REXPDouble((double[])paramObject);
    }
    if (localClass == Double_ARRAY)
    {
      Double [] localObject101 = (Double[])paramObject;
      double d = localObject101.length;
      double[] arrayOfDouble = new double[localObject101.length];
      for (int i2 = 0; i2 < d; i2++) {
        arrayOfDouble[i2] = localObject101[i2].doubleValue();
      }
      return new REXPDouble(arrayOfDouble);
    }
    if (localClass == boolean_ARRAY) {
      return new REXPLogical((boolean[])paramObject);
    }
    if (localClass == Boolean_ARRAY)
    {
    	Boolean[] localObject111 = (Boolean[])paramObject;
      int k = localObject111.length;
      boolean [] localObject112 = new boolean[localObject111.length];
      for (int i1 = 0; i1 < k; i1++) {
        localObject112[i1] = localObject111[i1].booleanValue();
      }
      return new REXPLogical((boolean[])localObject112);
    }
    return null;
  }
}
