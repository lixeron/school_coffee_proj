package hw_04_trane;

public class RegularCustomer extends Customer {

    public RegularCustomer(String name, String email, String phoneNumber) {
        super(name, email, phoneNumber);
    }

    @Override
    public String payCoffee() {
        return "Regular customer can pay for the coffee using credit card or cash.";
    }
}
