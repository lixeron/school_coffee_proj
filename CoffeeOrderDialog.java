package hw_04_trane;

import javax.swing.*;
import java.awt.*;
import java.util.List;
/*
 * Class used for creating the table for the information and status for the customer and their order.
 * This is technically abstraction otherwise my GUI class would be looking very fat in lines
 */
class CoffeeOrderDialog extends JDialog {
    private final JTextField nameField = new JTextField(10);
    private final JTextField emailField = new JTextField(10);
    private final JTextField phoneField = new JTextField(10);
    private final JComboBox<String> typeField = new JComboBox<>(new String[]{"Regular", "Premium"});
    private final JComboBox<String> coffeeSelection = new JComboBox<>(new String[]{"Espresso", "Filtered Coffee", "Cappuccino", "Latte", "Mocha"});
    private final List<Order> activeOrders;

    public CoffeeOrderDialog(JFrame parent, List<Order> activeOrders) {
        super(parent, "Order Coffee", true);
        this.activeOrders = activeOrders;
        setSize(400, 300);
        setLayout(new BorderLayout());
        add(buildFormPanel(), BorderLayout.CENTER);
        add(buildButtonsPanel(), BorderLayout.SOUTH);
    }

    private JPanel buildFormPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);
        panel.add(new JLabel("Type:"));
        panel.add(typeField);
        panel.add(new JLabel("Select Coffee:"));
        panel.add(coffeeSelection);
        return panel;
    }

    private JPanel buildButtonsPanel() {
        JButton btnSubmit = new JButton("Submit");
        btnSubmit.addActionListener(e -> submitOrder());
        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(e -> setVisible(false));
        JPanel panel = new JPanel();
        panel.add(btnSubmit);
        panel.add(btnCancel);
        return panel;
    }

    private void submitOrder() {
        String name = nameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String customerType = (String) typeField.getSelectedItem();
        String coffeeType = (String) coffeeSelection.getSelectedItem();
        String details = showCoffeeCustomization(coffeeType);
        activeOrders.add(new Order(name, details, customerType, "Active"));
        setVisible(false);
    }

    private String showCoffeeCustomization(String coffeeType) {
        JPanel customizationPanel = new JPanel(new GridLayout(0, 2));
        if (coffeeType.equals("Filtered Coffee")) {
            JComboBox<String> brewType = new JComboBox<>(new String[]{"Dark", "Medium", "Light"});
            customizationPanel.add(new JLabel("Select Brew Type:"));
            customizationPanel.add(brewType);
        } else {
            JTextField shots = new JTextField("1", 5);
            JComboBox<String> milkType = new JComboBox<>(new String[]{"Whole", "Skim", "Almond"});
            JTextField addIns = new JTextField(10);

            customizationPanel.add(new JLabel("Number of Shots:"));
            customizationPanel.add(shots);
            customizationPanel.add(new JLabel("Milk Type:"));
            customizationPanel.add(milkType);
            customizationPanel.add(new JLabel("Additional Ingredients:"));
            customizationPanel.add(addIns);
        }

        JOptionPane.showConfirmDialog(this, customizationPanel, "Customize your coffee", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        StringBuilder details = new StringBuilder(coffeeType);
        details.append("\nCustomizations:\n");
        for (int i = 1; i < customizationPanel.getComponentCount(); i += 2) {
            JComponent comp = (JComponent) customizationPanel.getComponent(i);
            if (comp instanceof JTextField) {
                details.append(((JLabel) customizationPanel.getComponent(i - 1)).getText()).append(" ").append(((JTextField) comp).getText()).append("\n");
            } else if (comp instanceof JComboBox) {
                details.append(((JLabel) customizationPanel.getComponent(i - 1)).getText()).append(" ").append(((JComboBox) comp).getSelectedItem()).append("\n");
            }
        }
        return details.toString();
    }
}

