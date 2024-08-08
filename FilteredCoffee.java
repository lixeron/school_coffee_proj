package hw_04_trane;

public class FilteredCoffee extends Coffee {
    private String brewType; // dark, light, medium

    public FilteredCoffee(String name, double price, String description, int calories, String brewType) {
        super(name, price, description, calories);
        this.brewType = brewType;
    }

    @Override
    public String prepare() {
        return "Preparing " + getName() + " with a " + brewType + " brew.";
    }

    // getter and setter for brewType
    public String getBrewType() {
        return brewType;
    }

    public void setBrewType(String brewType) {
        this.brewType = brewType;
    }
}
