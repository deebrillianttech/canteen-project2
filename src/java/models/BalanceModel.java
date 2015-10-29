package models;

/**
 *
 * @author ataulislam.raihan
 */
public class BalanceModel {
    private double cost;
    private double paid;

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getPaid() {
        return paid;
    }

    public void setPaid(double paid) {
        this.paid = paid;
    }
    
    public double getBalance(){
        return (this.paid - this.cost);
    }
}
