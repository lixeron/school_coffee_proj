package hw_04_trane;

class Order {
    String customerName;
    String orderDetails;
    String customerType;
    String status;  // "Active" or "Passive"

    public Order(String customerName, String orderDetails, String customerType, String status) {
        this.customerName = customerName;
        this.orderDetails = orderDetails;
        this.customerType = customerType;
        this.status = status;
    }
    public String toCSVFormat() {
        return customerName + "," + orderDetails + "," + customerType + "," + status;
    }

    public static Order fromCSVFormat(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length < 4) return null;
        return new Order(parts[0], parts[1], parts[2], parts[3]);  // Ensure constructor matches this usage
    }


    public String[] toTableRow() {
        return new String[]{customerName, orderDetails, customerType, status};
    }
}
