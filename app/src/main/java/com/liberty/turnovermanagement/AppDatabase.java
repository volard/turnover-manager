package com.liberty.turnovermanagement;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.liberty.turnovermanagement.customers.data.Customer;
import com.liberty.turnovermanagement.customers.data.CustomerDao;
import com.liberty.turnovermanagement.customers.data.CustomerHistory;
import com.liberty.turnovermanagement.orders.data.Order;
import com.liberty.turnovermanagement.orders.data.OrderDao;
import com.liberty.turnovermanagement.products.data.Product;
import com.liberty.turnovermanagement.products.data.ProductDao;
import com.liberty.turnovermanagement.products.data.ProductHistory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * AppDatabase class represents the main database for the application.
 * It uses Room persistence library and defines the database configuration and serves as the app's main access point to the persisted data.
 */
@Database(
        entities = {
            Product.class,
            Customer.class,
            Order.class,
            ProductHistory.class,
            CustomerHistory.class
        },
        version = 8,
        exportSchema = false)
@TypeConverters({DateTimeStringConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    /**
     * Abstract method to get ProductDao
     * @return ProductDao instance
     */
    public abstract ProductDao productDao();

    /**
     * Abstract method to get CustomerDao
     * @return CustomerDao instance
     */
    public abstract CustomerDao customerDao();

    /**
     * Abstract method to get OrderDao
     * @return OrderDao instance
     */
    public abstract OrderDao orderDao();

    // Singleton instance of the database
    private static volatile AppDatabase INSTANCE;

    // Thread pool for database operations
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    /**
     * Gets the singleton instance of the AppDatabase.
     * @param context The application context
     * @return The singleton instance of AppDatabase
     */
    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "main_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Generates test data for the database.
     * This method clears existing data and populates the database with sample products, customers, and orders.
     */
    public void generateTestData() {
        databaseWriteExecutor.execute(() -> {
            ProductDao productDao   = productDao();
            CustomerDao customerDao = customerDao();
            OrderDao orderDao       = orderDao();

            // Clear existing data
            orderDao.deleteAll();
            productDao.deleteAll();
            customerDao.deleteAll();

            // Generate test products
            List<Product> products = getProducts();
            List<Long> productIds = productDao.insertAllAndGetIds(products);
            for (int i = 0; i < productIds.size(); i++) {
                products.get(i).setId(productIds.get(i));
            }

            // Generate test customers
            List<Customer> customers = getCustomers();
            List<Long> customerIds = customerDao.insertAllAndGetIds(customers);
            for (int i = 0; i < customerIds.size(); i++) {
                customers.get(i).setId(customerIds.get(i));
            }


            // Generate test orders
            List<Order> orders = getOrders(customers, products);
            orderDao.insertAll(orders);
        });
    }

    private static @NonNull List<Order> getOrders(List<Customer> customers, List<Product> products) {
        List<Order> orders = new ArrayList<>();
        Random random = new Random();
        String[] cities = {"New York", "Los Angeles", "Chicago", "Houston", "Phoenix", "Philadelphia", "San Antonio", "San Diego", "Dallas", "San Jose"};

        for (int i = 0; i < 50; i++) {
            Order order = new Order();

            Product selectedProduct = products.get(random.nextInt(products.size()));
            Customer selectedCustomer = customers.get(random.nextInt(customers.size()));

            order.setProductId(selectedProduct.getId());
            order.setCustomerId(selectedCustomer.getId());
            order.setProductVersion(selectedProduct.getVersion());
            order.setCustomerVersion(selectedCustomer.getVersion());
            order.setAmount(random.nextInt(10) + 1); // 1 to 10 items

            // Generate a random date within the last 90 days
            LocalDateTime orderDate = LocalDateTime.now().minusDays(random.nextInt(90));
            order.setDatetime(orderDate);

            String city = cities[random.nextInt(cities.length)];
            order.setCity(city);
            order.setStreet(generateStreetName());
            order.setHome(generateHomeNumber());

            orders.add(order);
        }
        return orders;
    }


    /**
     * Generates a random street name.
     * @return A randomly generated street name
     */
    private static String generateStreetName() {
        String[] streetTypes = {"St", "Ave", "Blvd", "Ln", "Rd", "Way", "Pl"};
        String[] streetNames = {"Main", "Oak", "Pine", "Maple", "Cedar", "Elm", "Washington", "Park", "Lake", "Hill"};
        return streetNames[new Random().nextInt(streetNames.length)] + " " + streetTypes[new Random().nextInt(streetTypes.length)];
    }

    /**
     * Generates a random home number.
     * @return A randomly generated home number as a String
     */
    private static String generateHomeNumber() {
        return String.valueOf(new Random().nextInt(9999) + 1);
    }

    /**
     * Generates a list of sample products.
     * @return List of generated Product objects
     */
    private static @NonNull List<Product> getProducts() {
        List<Product> products = new ArrayList<>();
        String[] productNames = {
                "Laptop", "Smartphone", "Tablet", "Desktop PC", "Wireless Earbuds",
                "Smart Watch", "Gaming Console", "4K TV", "Digital Camera", "Bluetooth Speaker",
                "Wireless Router", "External Hard Drive", "Printer", "Graphic Card", "Keyboard",
                "Mouse", "Monitor", "Headphones", "Webcam", "Microphone",
                "Power Bank", "USB Flash Drive", "SSD", "RAM", "CPU Cooler",
                "Smartwatch", "Fitness Tracker", "VR Headset", "Drone", "E-reader"
        };

        Random random = new Random();

        for (String name : productNames) {
            Product product = new Product();
            product.setName(name);
            product.setAmount(random.nextInt(100) + 1); // Random amount between 1 and 100
            product.setPrice(50 + (1950 * random.nextDouble())); // Random price between 50 and 2000
            product.setPrice(Math.round(product.getPrice() * 100.0) / 100.0); // Round to 2 decimal places
            product.setDeleted(false);
            products.add(product);
        }
        return products;
    }

    /**
     * Generates a list of sample customers.
     * @return List of generated Customer objects
     */
    @SuppressLint("DefaultLocale")
    private static @NonNull List<Customer> getCustomers() {
        List<Customer> customers = new ArrayList<>();
        String[] firstNames = {"John", "Jane", "Mike", "Emily", "David", "Sarah", "Chris", "Laura", "Alex", "Emma",
                "Daniel", "Olivia", "James", "Sophia", "William", "Ava", "Michael", "Isabella", "Robert", "Mia"};
        String[] lastNames = {"Doe", "Smith", "Johnson", "Brown", "Wilson", "Taylor", "Anderson", "Thomas", "Jackson", "White",
                "Harris", "Martin", "Thompson", "Garcia", "Martinez", "Robinson", "Clark", "Rodriguez", "Lewis", "Lee"};
        String[] middleNames = {"A.", "B.", "C.", "D.", "E.", "F.", "G.", "H.", "I.", "J."};
        String[] domains = {"gmail.com", "yahoo.com", "hotmail.com", "outlook.com", "example.com"};

        Random random = new Random();

        for (int i = 0; i < 50; i++) {
            Customer customer = new Customer();
            String firstName = firstNames[random.nextInt(firstNames.length)];
            String lastName = lastNames[random.nextInt(lastNames.length)];

            customer.setName(firstName);
            customer.setSurname(lastName);
            customer.setMiddleName(middleNames[random.nextInt(middleNames.length)]);
            customer.setPhone(String.format("%03d-%03d-%04d", random.nextInt(1000), random.nextInt(1000), random.nextInt(10000)));
            customer.setEmail(firstName.toLowerCase() + "." + lastName.toLowerCase() + "@" + domains[random.nextInt(domains.length)]);
            customer.setDeleted(false);
            customers.add(customer);
        }
        return customers;
    }
}

