package pc.util;

import java.io.PrintStream;

public class RNG
{
  private double theSeed = 0.123456789D;
  private double theMult = 63069.0D;
  private double theTerm = 0.84763521D;
  
  public RNG()
  {
    this.theSeed = ((System.currentTimeMillis() & 0xFFFF) / 65536.0D);
  }
  
  public RNG(double paramDouble)
  {
    setSeed(paramDouble);
  }
  
  public void setSeed(double paramDouble)
  {
    this.theSeed = Math.abs(paramDouble);
  }
  
  public void setMult(double paramDouble)
  {
    this.theMult = paramDouble;
  }
  
  public void setTerm(double paramDouble)
  {
    this.theTerm = paramDouble;
  }
  
  public double getSeed()
  {
    return this.theSeed;
  }
  
  public double getMult()
  {
    return this.theMult;
  }
  
  public double getTerm()
  {
    return this.theTerm;
  }
  
  public double unif()
  {
    double d = this.theMult * this.theSeed + this.theTerm;
    this.theSeed = (d - Math.floor(d));
    return this.theSeed;
  }
  
  public synchronized double norm()
  {
    this.flag = (!this.flag);
    if (this.flag) {
      return this.r * this.u2;
    }
    do
    {
      this.u1 = (2.0D * unif() - 1.0D);
      this.u2 = (2.0D * unif() - 1.0D);
      this.r = (this.u1 * this.u1 + this.u2 * this.u2);
    } while (this.r >= 1.0D);
    this.r = Math.sqrt(-2.0D * Math.log(this.r) / this.r);
    return this.r * this.u1;
  }
  
  private boolean flag = true;
  private double u1;
  private double u2;
  private double r;
  
  public static void main(String[] paramArrayOfString)
  {
    RNG localRNG1 = new RNG();
    RNG localRNG2 = new RNG(localRNG1.getSeed() + 1.E-015D);
    RNG localRNG3 = new RNG(localRNG1.getSeed() + 0.6417946D);
    for (int i = 0; i < 20; i++) {
      System.out.println(localRNG1.unif() + "\t" + localRNG2.unif() + "\t" + localRNG3.norm());
    }
  }
}
