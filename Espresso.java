package hw_04_trane;

public class Espresso extends Coffee {
    private int shots;
    private String milkType;
    private String additionalIngredients;

    public Espresso(String name, double price, String description, int calories, int shots, String milkType, String additionalIngredients) {
        super(name, price, description, calories);
        this.shots = shots;
        this.milkType = milkType;
        this.additionalIngredients = additionalIngredients;
    }

    @Override
    public String prepare() {
        return "Preparing " + getName() + " with " + shots + " shots of espresso, " + milkType + " milk, " + additionalIngredients;
    }

    // getters and setters for shots, milkType, and additionalIngredients
    public int getShots() {
        return shots;
    }

    public void setShots(int shots) {
        this.shots = shots;
    }

    public String getMilkType() {
        return milkType;
    }

    public void setMilkType(String milkType) {
        this.milkType = milkType;
    }

    public String getAdditionalIngredients() {
        return additionalIngredients;
    }

    public void setAdditionalIngredients(String additionalIngredients) {
        this.additionalIngredients = additionalIngredients;
    }
}
