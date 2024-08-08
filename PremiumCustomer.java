package hw_04_trane;

public class PremiumCustomer extends Customer {

    public PremiumCustomer(String name, String email, String phoneNumber) {
        super(name, email, phoneNumber);
    }

    @Override
    public String payCoffee() {
        return "Premium customer can pay for the coffee using bitcoin, credit card, or cash. A 10% discount will be applied to the price.";
    }
}
