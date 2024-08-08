package hw_04_trane;
// superclass coffee 
public abstract class Coffee {
    private String name;
    private double price;
    private String description;
    private int calories;
// shared coffee info
    public Coffee(String name, double price, String description, int calories) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.calories = calories;
    }
// per instruction
    public abstract String prepare();

    //getters and setters
    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }
}
