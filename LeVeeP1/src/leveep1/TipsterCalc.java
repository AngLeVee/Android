/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leveep1;

/**
 *
 * @author Angela
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
        diners = 1;
        bill = 0.0;
        subtotal = 0.0;
        perDiner = 0.0;
        tipAmount = 0.0;
    }
    
    public void setInputData(double tipIn, int dinersIn, double billIn)
    {
        tip = tipIn;
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
