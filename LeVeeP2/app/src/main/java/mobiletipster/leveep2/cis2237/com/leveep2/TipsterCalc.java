//Program: LeVeeP2
//File: TipsterCalc
//Programmer: Angela LeVee (alevee@cnm.edu)
//Objective: Holds class for calculation of tips

package mobiletipster.leveep2.cis2237.com.leveep2;

/**
 * Created by Angela on 9/21/2016.
 */
public class TipsterCalc {

    private double tip;
    private int diners;
    private double bill;
    private double tipAmount;
    private double subtotal;
    private double perDiner;

    public void TipsterCalc()
    {
        tip = 0;
        diners = 0;
        bill = 0.0;
        subtotal = 0.0;
        perDiner = 0.0;
        tipAmount = 0.0;
    }

    public void setInputData(double tipIn, int dinersIn, double billIn)
    {
        tip = (double)tipIn/100;
        diners = dinersIn;
        bill = billIn;

        Calculate();
    }

    public double getTipAmount()
    {
        return tipAmount;
    }

    public double getSubtotal()
    {
        return subtotal;
    }

    public double getPerDiner()
    {
        return perDiner;
    }

    private void Calculate ()
    {
        tipAmount = tip * bill;
        subtotal = bill + tipAmount;
        perDiner = subtotal/(double)diners;
    }
}
