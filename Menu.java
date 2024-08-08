package hw_04_trane;

import java.util.ArrayList;
import java.util.List;

public class Menu {
    private List<Coffee> coffeeList;

    public Menu() {
        coffeeList = new ArrayList<>();
    }

    // method to add coffee to the menu
    public void addCoffee(Coffee coffee) {
        coffeeList.add(coffee); 
    }

    // method to get a list of all coffees
    public List<Coffee> getCoffees() {
        return coffeeList;
    }

    // method to get coffee details for display in the GUI
    public String displayCoffeeDetails() {
        StringBuilder details = new StringBuilder();
        for (Coffee coffee : coffeeList) {
            details.append("Name: ").append(coffee.getName())
                   .append(", Price: $").append(String.format("%.2f", coffee.getPrice()))
                   .append(", Calories: ").append(coffee.getCalories())
                   .append(" cal, Description: ").append(coffee.getDescription())
                   .append("\n");
        }
        return details.toString();
    }
}