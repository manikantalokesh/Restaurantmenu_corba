public class OrderDetails{
     private int count_fc = 0;
     private int count_cola = 0;
     private int orderStatus = 0;
     private float price_fc = 0;
     private float price_cola = 0;
     private float money = 0;
    public  void setCount( int fc_count_val, int cola_count_val, float money ){
        this.count_fc = fc_count_val;
        this.count_cola = cola_count_val;
        this.orderStatus = 1;
        this.price_fc = (float) (fc_count_val * 15.0);
        this.price_cola = (float) (cola_count_val * 5.0);
        this.money = money;}
    
    public  int FriedCCount(){
        return this.count_fc;
    }
    public  int ColaCount(){
        return this.count_cola;
    }
    public  float Total(){
        return (this.price_cola +this.price_fc);
    }
    public float money() {
        return this.money;
    }
    public float RemainingBalance() {
        return this.money - Total();
    }
}