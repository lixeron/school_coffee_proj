package hw_04_trane;
// MUST FULL SCREEN OR ELSE IT'LL LOOK REALLY BAD
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.Color;



public class CoffeeShopGUI extends JFrame {
    private JButton btnOrderCoffee, btnViewOrders, btnPrepare, btnPayment, btnExit;
    private List<Order> activeOrders;
    private String filePath = System.getProperty("user.home") + File.separator + "coffeeShopOrders.csv";
    // decorating this beauty
    private static final Color COLOR_ORANGE = new Color(255, 165, 0);
    private static final Color COLOR_BLACK = new Color(0, 0, 0);
    private static final Color COLOR_WHITE = new Color(255, 255, 255);
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 24);
    private static final Font COFFEE_FONT = new Font("Arial", Font.PLAIN, 18);
    
    public CoffeeShopGUI() { //displaying GUI
        setTitle("I SPENT TOO LONG ON THIS");
        setSize(800, 600); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        activeOrders = new ArrayList<>();
        initializeButtons();

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(btnOrderCoffee);
        buttonPanel.add(btnViewOrders);
        buttonPanel.add(btnPrepare);
        buttonPanel.add(btnPayment);
        buttonPanel.add(btnExit);
        add(buttonPanel, BorderLayout.SOUTH);

        setupDecorativeDisplay();  // COMETO LIFE MY DECORATION
        loadOrdersFromFile();
        // it's supposed to be saving but it isn't unsure why...
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("Window closing, attempting to save orders.");
                saveOrdersToFile();
                dispose();
            }
        });

        setVisible(true);
    }


    private void initializeButtons() { // buttons can do stuff now
        btnOrderCoffee = new JButton("Order Coffee");
        btnOrderCoffee.addActionListener(e -> orderCoffee(e));
        btnViewOrders = new JButton("View Orders");
        btnViewOrders.addActionListener(e -> viewOrders(e));
        btnPrepare = new JButton("Prepare");
        btnPrepare.addActionListener(e -> prepareOrder(e));
        btnPayment = new JButton("Payment");
        btnPayment.addActionListener(e -> processPayment(e));
        btnExit = new JButton("Exit");
        btnExit.addActionListener(e -> exitApplication());
    }

    private void orderCoffee(ActionEvent e) { // the long process of ordering coffee and getting customer info
        new CoffeeOrderDialog(this, activeOrders).setVisible(true);
    }
    private void displayOrders(String title, boolean allowPrepare) {
        DefaultTableModel model = new DefaultTableModel(new String[]{"Name", "Order Details", "Customer Type", "Status"}, 0);
        for (Order order : activeOrders) {
            if (!allowPrepare || order.status.equals("Active")) {
                model.addRow(new Object[]{order.customerName, order.orderDetails, order.customerType, order.status});
            }
        }

        JTable table = new JTable(model);
        int result = JOptionPane.showConfirmDialog(null, new JScrollPane(table), title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (allowPrepare && result == JOptionPane.OK_OPTION) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                Order selectedOrder = activeOrders.get(selectedRow);
                selectedOrder.status = "Passive"; // Change status to passive
            }
        }
    }

    private void viewOrders(ActionEvent e) { // view orders made from orderCoffee
        displayOrders("Active Orders", false);
    }

    private void prepareOrder(ActionEvent e) { // uses table to interact and show the orders and change them
        displayOrders("Prepare Orders", true);
    }

    private void processPayment(ActionEvent e) { // uses table from before to process payments
        DefaultTableModel model = new DefaultTableModel(new String[]{"Name", "Order Details", "Customer Type", "Status"}, 0);
        for (Order order : activeOrders) {
            if (order.status.equals("Passive")) {
                model.addRow(order.toTableRow());
            }
        } // onyl will be using passive since active orders arent ready

        JTable table = new JTable(model); 
        int result = JOptionPane.showConfirmDialog(null, new JScrollPane(table), "Select an Order to Pay", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION && table.getSelectedRow() != -1) {
            int selectedRow = table.getSelectedRow();
            Order order = activeOrders.get(selectedRow);
            processOrderPayment(order);
            activeOrders.remove(selectedRow);
            JOptionPane.showMessageDialog(this, "Order payment completed.");
        } else {
            JOptionPane.showMessageDialog(this, "No order selected or available for payment.");
        }
    }

    private void processOrderPayment(Order order) { // payment process
        double price = calculatePrice(order);
        String[] paymentMethods = {"Credit Card", "Cash", "Bitcoin"};
        if (order.customerType.equals("Premium")) {
            price *= 0.9;  // 10% discount if premium
        }
        String paymentMethod = (String) JOptionPane.showInputDialog(null, "Total due: $" + String.format("%.2f", price) + "\nSelect payment method:", "Payment", JOptionPane.QUESTION_MESSAGE, null, paymentMethods, paymentMethods[0]);
        
    }

    private double calculatePrice(Order order) {

        double price = 0;
        // prices from the shop
        if (order.orderDetails.contains("Espresso")) {
            price += 2.50;
        } else if (order.orderDetails.contains("Filtered Coffee")) {
            price += 1.80;
        } else if (order.orderDetails.contains("Cappuccino")) {
            price += 3.00;
        } else if (order.orderDetails.contains("Latte")) {
            price += 3.50;
        } else if (order.orderDetails.contains("Mocha")) {
            price += 3.75;
        }
        return price;
    }

    private void saveOrdersToFile() {
        System.out.println("Attempting to save " + activeOrders.size() + " orders.");
        if (activeOrders.isEmpty()) {
            System.out.println("No orders to save.");
            return;
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, false))) {  // Ensure not appending but overwriting
            for (Order order : activeOrders) {
                String csvLine = order.toCSVFormat();
                System.out.println("Writing: " + csvLine);
                bw.write(csvLine);
                bw.newLine();
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error saving orders to file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void loadOrdersFromFile() {
        File file = new File(filePath);
        System.out.println("Loading from file: " + file.getAbsolutePath());  // Debugging output
        if (!file.exists()) {
            System.out.println("No file to read from.");
            return; // No file to read from
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println("Reading line: " + line);  // Debugging output
                Order order = Order.fromCSVFormat(line);
                if (order != null) activeOrders.add(order);
                else System.out.println("Failed to parse order from line.");
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error loading orders from file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exitApplication() { // simply exits 
        JOptionPane.showMessageDialog(this, "Exiting application, all data saved.");
        System.exit(0);
    }
    private void setupDecorativeDisplay() {
        JPanel displayPanel = new JPanel(new BorderLayout());
        displayPanel.setBackground(COLOR_BLACK);

        // title
        JLabel titleLabel = new JLabel("Trane Coffee Shop", SwingConstants.CENTER);
        titleLabel.setForeground(COLOR_ORANGE);
        titleLabel.setFont(TITLE_FONT);
        displayPanel.add(titleLabel, BorderLayout.NORTH);

        // DISPLAYING MY BEAUTY
        String[] coffees = {"Espresso - $2.50", "Filtered Coffee - $1.80", "Cappuccino - $3.00", "Latte - $3.50", "Mocha - $3.75"};
        JPanel coffeePanel = new JPanel();
        coffeePanel.setLayout(new GridLayout(coffees.length, 1));
        coffeePanel.setBackground(COLOR_BLACK);
        for (String coffee : coffees) {
            JLabel coffeeLabel = new JLabel(coffee, SwingConstants.CENTER);
            coffeeLabel.setForeground(COLOR_WHITE);
            coffeeLabel.setFont(COFFEE_FONT);
            coffeePanel.add(coffeeLabel);
        }

        // ASCII art I sourced the link in the explanation doc
        String coffeeCupArt = "<html><pre style='font-size: 10px; color: #FFA500;'>" +
            "                                       %                                        \n" +
            "                                       %%                                       \n" +
            "                                      ,%%#                                      \n" +
            "                                     %%%%  %                                    \n" +
            "                                    %%%   %%                                    \n" +
            "                         #%%%%%%%%%%%# %%%% %%%%%#                              \n" +
            "                    %%%%#          #%   (#        (%%%%.                        \n" +
            "                  %%%%              %                %%%%%%%%%%,                \n" +
            "                  %%%%%(                           (%%%%%%%%%%%%%               \n" +
            "                  /%%%%%%    %%%%#,     ,#%%%%%%%%%%%%%%(  %%%%%%%              \n" +
            "                   %%%%%%    %%%%%%%%%%%%%%%%%%%%%%%%%%%    %%%%%               \n" +
            "                    %%%%%%   %%%%%%%%%%%%%%%%%%%%%%%%%%     %%%%                \n" +
            "                     %%%%%%   %%%%%%%%%%%%%%%%%%%%%%%%    %%%%                  \n" +
            "                      #%%%%%%   %%%%%%%%%%%%%%%%%%%%% (%%%%,                    \n" +
            "                        %%%%%%%%% %%%%%%%%%%%%%%%%%%%%*                         \n" +
            "                 %%%%%/    /%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%                      \n" +
            "               %%%%%%%%%%%(       ,(###(, %%%%%%%%%%%%%%%%%%                    \n" +
            "                .%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%,                     \n" +
            "                      #%%%%%%%%%%%%%%%%%%%%%%%%%%%%%#                           \n" +
            "</pre></html>";

        JLabel leftCoffeeLabel = new JLabel(coffeeCupArt);
        JLabel rightCoffeeLabel = new JLabel(coffeeCupArt);

        // elements for display panel
        displayPanel.add(leftCoffeeLabel, BorderLayout.WEST);
        displayPanel.add(rightCoffeeLabel, BorderLayout.EAST);
        displayPanel.add(coffeePanel, BorderLayout.CENTER);

        //main display panel for frame
        getContentPane().add(displayPanel, BorderLayout.CENTER);
    }


    public static void main(String[] args) {
        new CoffeeShopGUI();
    }
}
