package pc.powercalculator.apps;

//import pc.powercalculator.GenericChi2GUI;
import pc.powercalculator.PowerCalculator;
import pc.stat.dist.Chi2;
import pc.util.StatTalker;

public class LogisticSLR   extends PowerCalculator //GenericChi2GUI
{
  private static String title = "Simple Logistic Regression";
  public double slope;
  public double[] params;
  public boolean random;
  public double base_n;
  public String chi2Fcn;
  public String statTalker;
  public double df;
  public double n;
  
  public void initChi2()
  {
    this.params = new double[2];
    this.random = false;
    this.base_n = 10.0D;
    this.chi2Fcn = "lrchi2";
    this.statTalker = "x <- 1:10";
    this.statTalker ="lrchi2 <- function(beta,n=1) {nb <- length(x);p <- exp(x*beta);p <- p / (1+p);g <- anova(glm(p ~ x, family=binomial, weights=rep(n/nb, nb)));(g$Deviance)[2]; }; 0;";
  }
  
  public void gui()
  {
    bar("alpha", 0.05D);
    bar("n", 100.0D);
    slider("slope", 1.0D);
    ointerval("power", 0.0D, 0.0D, 1.0D);
  }
  
  public void click()
  {
    this.df = 1.0D;
    this.params[0] = this.slope;
    this.params[1] = this.n;
    //double power = Chi2.power(this.n * this.proChi2 / this.proN, this.df, this.Alpha);
  }
  
  public LogisticSLR()
  {
    super(title);
  }
  
  public static void main(String[] paramArrayOfString)
  {
    new LogisticSLR();
  }
}
