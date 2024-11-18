package com.liberty.turnovermanagement;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.liberty.turnovermanagement.customers.data.Customer;
import com.liberty.turnovermanagement.customers.data.CustomerDao;
import com.liberty.turnovermanagement.customers.data.CustomerHistory;
import com.liberty.turnovermanagement.customers.data.CustomerRepository;
import com.liberty.turnovermanagement.orders.data.Order;
import com.liberty.turnovermanagement.orders.data.OrderDao;
import com.liberty.turnovermanagement.products.data.Product;
import com.liberty.turnovermanagement.products.data.ProductDao;
import com.liberty.turnovermanagement.products.data.ProductHistory;
import com.liberty.turnovermanagement.products.data.ProductRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
        version = 9,
        exportSchema = false)
@TypeConverters({DateTimeStringConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract ProductDao productDao();
    public abstract CustomerDao customerDao();
    public abstract OrderDao orderDao();

    private static final String DATABASE_NAME = "main_database";

    private static volatile AppDatabase INSTANCE;

    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }


    public void clearAllData() {
        ProductRepository productRepository = new ProductRepository(productDao());
        CustomerRepository customerRepository = new CustomerRepository(customerDao());

        databaseWriteExecutor.execute(() -> {
            OrderDao orderDao = orderDao();

            // Clear existing data
            orderDao.deleteAll();
            productRepository.deleteAll();
            customerRepository.deleteAll();
        });
    }

    /**
     * Generates test data for the database.
     * This method clears existing data and populates the database with sample products, customers, and orders.
     */
    public void generateTestData() {
        databaseWriteExecutor.execute(() -> {
            ProductDao productDao = productDao();
            CustomerDao customerDao = customerDao();
            OrderDao orderDao = orderDao();

            // Clear existing data
            orderDao.deleteAll();
            productDao.deleteAll();
            customerDao.deleteAll();

            // Generate test data
            List<Product> products = generateProducts();
            List<Customer> customers = generateCustomers();
            List<Long> productIds = productDao.insertAllAndGetIds(products);
            List<Long> customerIds = customerDao.insertAllAndGetIds(customers);
            for (Customer customer :
                    customers) {
                customer.setId(customerIds.get(customers.indexOf(customer)));
            }
            for (Product product :
                    products) {
                product.setId(productIds.get(products.indexOf(product)));
            }


            List<Order> orders = generateOrders(products, customers);

            // Insert test data
            orderDao.insertAll(orders);

            // Generate and insert history data
            generateAndInsertHistory(productDao, customerDao, products, customers);
        });
    }

    private String transliterate(String message) {
        char[] abcCyr = {' ', 'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'ю', 'я'};
        String[] abcLat = {" ", "a", "b", "v", "g", "d", "e", "e", "zh", "z", "i", "y", "k", "l", "m", "n", "o", "p", "r", "s", "t", "u", "f", "h", "ts", "ch", "sh", "sch", "", "y", "", "e", "yu", "ya"};
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            for (int x = 0; x < abcCyr.length; x++) {
                if (message.charAt(i) == abcCyr[x]) {
                    builder.append(abcLat[x]);
                }
            }
        }
        return builder.toString();
    }


    private List<Product> generateProducts() {
        // Generate test products
        List<Product> products = new ArrayList<>();
        String[] productNames = {"Ноутбук", "Смартфон", "Планшет", "Наушники", "Умные часы"};
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            Product product = new Product();
            product.setName(productNames[i]);
            product.setAmount(random.nextInt(50) + 1);
            BigDecimal price = BigDecimal.valueOf(random.nextDouble() * 50000 + 5000);
            price = price.setScale(2, RoundingMode.HALF_UP); // Round to 2 decimal places
            product.setPrice(price.doubleValue());
            product.setDeleted(false);
            products.add(product);
        }

        return products;
    }

    private List<Customer> generateCustomers() {
        List<Customer> customers = new ArrayList<>();
        String[] firstNames = {"Иван", "Мария", "Алексей", "Екатерина", "Дмитрий"};
        String[] lastNames = {"Иванов", "Петрова", "Сидоров", "Смирнова", "Кузнецов"};
        String[] middleNames = {"Александрович", "Сергеевна", "Николаевич", "Андреевна", "Владимирович"};

        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            Customer customer = new Customer();
            customer.setSurname(lastNames[i]);
            customer.setName(firstNames[i]);
            customer.setMiddleName(middleNames[i]);
            customer.setPhone("+7" + (900 + random.nextInt(100)) + random.nextInt(99999999));
            customer.setEmail(transliterate(firstNames[i].toLowerCase()) + "." + transliterate(lastNames[i].toLowerCase()) + "@mail.ru");
            customer.setDeleted(false);
            customers.add(customer);
        }

        return customers;
    }

    private List<Order> generateOrders(List<Product> products, List<Customer> customers) {
        // Generate test orders
        List<Order> orders = new ArrayList<>();
        String[] cities = {"Москва", "Санкт-Петербург", "Новосибирск", "Екатеринбург", "Казань"};
        String[] streets = {"Ленина", "Пушкина", "Гагарина", "Советская", "Мира"};

        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            Order order = new Order();
            Product product = products.get(random.nextInt(products.size() - 1));
            Customer customer = customers.get(random.nextInt(customers.size() - 1));
            order.setProductId(product.getId());
            order.setCustomerId(customer.getId());
            order.setProductVersion(product.getVersion());
            order.setCustomerVersion(customer.getVersion());
            order.setAmount(random.nextInt(5) + 1);
            order.setCreatedAt(LocalDateTime.now().minusDays(random.nextInt(30)));
            order.setCity(cities[random.nextInt(cities.length)]);
            order.setStreet("ул. " + streets[random.nextInt(streets.length)]);
            order.setHome("д. " + (random.nextInt(100) + 1));
            orders.add(order);
        }

        return orders;
    }

    private void generateAndInsertHistory(ProductDao productDao, CustomerDao customerDao,
                                          List<Product> products, List<Customer> customers) {
        Random random = new Random();

        // Generate product history
        for (Product product : products) {
            if (random.nextBoolean()) {
                ProductHistory history = new ProductHistory();
                history.setProductId(product.getId());
                history.setName(product.getName() + " (Old)");
                history.setAmount(product.getAmount() - random.nextInt(10));
                history.setPrice(product.getPrice() * 0.9);
                history.setVersion(product.getVersion());
                history.setCreatedAt(product.getLastUpdated().minusDays(random.nextInt(30)));

                productDao.insertHistory(
                        history.getProductId(),
                        history.getName(),
                        history.getAmount(),
                        history.getPrice(),
                        history.getVersion(),
                        history.getCreatedAt()
                );

                productDao.incrementVersion(product.getId(), LocalDateTime.now());
            }
        }

        // Generate customer history
        for (Customer customer : customers) {
            if (random.nextBoolean()) {
                CustomerHistory history = new CustomerHistory();
                history.setCustomerId(customer.getId());
                history.setSurname(customer.getSurname());
                history.setName(customer.getName());
                history.setMiddleName(customer.getMiddleName());
                history.setPhone("000-000-" + random.nextInt(10000));
                history.setEmail("old." + customer.getEmail());
                history.setVersion(customer.getVersion());
                history.setCreatedAt(customer.getLastUpdated().minusDays(random.nextInt(30)));

                customerDao.insertHistory(
                        history.getCustomerId(),
                        history.getSurname(),
                        history.getName(),
                        history.getMiddleName(),
                        history.getPhone(),
                        history.getEmail(),
                        history.getVersion(),
                        history.getCreatedAt()
                );

                customerDao.incrementVersion(customer.getId(), LocalDateTime.now());
            }
        }
    }

}

