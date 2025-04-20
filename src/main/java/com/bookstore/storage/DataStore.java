package com.bookstore.storage;

import com.bookstore.model.*;
import com.bookstore.exception.BookNotFoundException;
import com.bookstore.exception.CartNotFoundException;
import com.bookstore.exception.OutOfStockException;

import java.util.*;

public class DataStore {
    private static final Map<Integer, Author> authors = new HashMap<>();
    private static final Map<Integer, Book> books = new HashMap<>();
    private static final Map<Integer, Customer> customers = new HashMap<>();
    private static final Map<Integer, Cart> carts = new HashMap<>();
    private static final Map<Integer, List<Order>> orders = new HashMap<>();

    private static int authorIdCounter = 1;
    private static int bookIdCounter = 1;
    private static int customerIdCounter = 1;
    private static int orderIdCounter = 1;

    // ---------- Authors ----------
    public static Author addAuthor(Author author) {
        author.setId(authorIdCounter++);
        authors.put(author.getId(), author);
        return author;
    }

    public static Author getAuthor(int id) {
        return authors.get(id);
    }

    public static List<Author> getAllAuthors() {
        return new ArrayList<>(authors.values());
    }

    public static Author updateAuthor(int id, Author updatedAuthor) {
        if (!authors.containsKey(id)) return null;
        updatedAuthor.setId(id);
        authors.put(id, updatedAuthor);
        return updatedAuthor;
    }

    public static boolean deleteAuthor(int id) {
        return authors.remove(id) != null;
    }

    public static List<Book> getBooksByAuthor(int authorId) {
        List<Book> result = new ArrayList<>();
        for (Book book : books.values()) {
            if (book.getAuthorId() == authorId) {
                result.add(book);
            }
        }
        return result;
    }

    // ---------- Books ----------
    public static Book addBook(Book book) {
        book.setId(bookIdCounter++);
        books.put(book.getId(), book);
        return book;
    }

    public static Book getBook(int id) {
        return books.get(id);
    }

    public static List<Book> getAllBooks() {
        return new ArrayList<>(books.values());
    }

    public static Book updateBook(int id, Book updatedBook) {
        if (!books.containsKey(id)) return null;
        updatedBook.setId(id);
        books.put(id, updatedBook);
        return updatedBook;
    }

    public static boolean deleteBook(int id) {
        return books.remove(id) != null;
    }

    // ---------- Customers ----------
    public static Customer addCustomer(Customer customer) {
        customer.setId(customerIdCounter++);
        customers.put(customer.getId(), customer);
        carts.put(customer.getId(), new Cart(customer.getId(), new ArrayList<>()));
        orders.put(customer.getId(), new ArrayList<>());
        return customer;
    }

    public static Customer getCustomer(int id) {
        return customers.get(id);
    }

    public static Customer updateCustomer(int id, Customer updatedCustomer) {
        if (!customers.containsKey(id)) return null;
        updatedCustomer.setId(id);
        customers.put(id, updatedCustomer);
        return updatedCustomer;
    }

    public static List<Customer> getAllCustomers() {
        return new ArrayList<>(customers.values());
    }

    public static boolean deleteCustomer(int id) {
        customers.remove(id);
        carts.remove(id);
        orders.remove(id);
        return true;
    }

    // ---------- Cart ----------
    public static Cart getCart(int customerId) {
        return carts.get(customerId);
    }

    public static Cart addToCart(int customerId, Cart.Item item) {
        Cart cart = carts.get(customerId);
        if (cart == null) {
            cart = new Cart(customerId, new ArrayList<>());
            carts.put(customerId, cart);
        }

        Optional<Cart.Item> existingItem = cart.getItems().stream()
                .filter(i -> i.getBookId() == item.getBookId())
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + item.getQuantity());
        } else {
            cart.getItems().add(item);
        }

        return cart;
    }

    public static Cart removeFromCart(int customerId, int bookId) {
        Cart cart = carts.get(customerId);
        if (cart == null) return null; // If the cart does not exist, return null

        // Try to remove the item from the cart and check if an item was removed
        boolean removed = cart.getItems().removeIf(item -> item.getBookId() == bookId);
        if (removed) {
            return cart; // Return the updated cart if the item was removed
        } else {
            return null; // Return null if the item wasn't found
        }
    }


    public static Cart updateCartItem(int customerId, Cart.Item updatedItem) {
        Cart cart = carts.get(customerId);
        if (cart == null) return null;

        for (Cart.Item item : cart.getItems()) {
            if (item.getBookId() == updatedItem.getBookId()) {
                item.setQuantity(updatedItem.getQuantity());
                return cart;
            }
        }

        // If the item is not found, add it to the cart
        cart.getItems().add(updatedItem);
        return cart;
    }

    public static Cart clearCart(int customerId) {
        Cart cart = carts.get(customerId);
        if (cart != null) {
            cart.setItems(new ArrayList<>());
        }
        return cart;
    }

    // ---------- Orders ----------
    public static Order placeOrder(int customerId) {
        Cart cart = carts.get(customerId);
        if (cart == null || cart.getItems().isEmpty()) {
            throw new CartNotFoundException(customerId);
        }

        List<OrderItem> orderItems = new ArrayList<>();
        double total = 0;

        for (Cart.Item cartItem : cart.getItems()) {
            Book book = books.get(cartItem.getBookId());
            if (book == null) {
                throw new BookNotFoundException(cartItem.getBookId());
            }

            if (book.getStock() < cartItem.getQuantity()) {
                throw new OutOfStockException(book.getId(), cartItem.getQuantity(), book.getStock());
            }

            book.setStock(book.getStock() - cartItem.getQuantity());

            orderItems.add(new OrderItem(
                    book.getId(),
                    book.getTitle(),
                    cartItem.getQuantity(),
                    book.getPrice()
            ));

            total += cartItem.getQuantity() * book.getPrice();
        }

        Order order = new Order();
        order.setId(orderIdCounter++);
        order.setCustomerId(customerId);
        order.setItems(orderItems);
        order.setTotal(total);
        order.setTimestamp(new Date());

        orders.get(customerId).add(order);
        clearCart(customerId);

        return order;
    }

    public static Order createOrderFromCart(int customerId) {
        return placeOrder(customerId);
    }

    public static List<Order> getOrdersByCustomer(int customerId) {
        return orders.getOrDefault(customerId, new ArrayList<>());
    }


    public static List<Order> getAllOrders() {
        List<Order> allOrders = new ArrayList<>();
        for (List<Order> customerOrders : orders.values()) {
            allOrders.addAll(customerOrders);
        }
        return allOrders;
    }

    public static Order getOrder(int customerId, int orderId) {
        return orders.getOrDefault(customerId, new ArrayList<>()).stream()
                .filter(order -> order.getId() == orderId)
                .findFirst()
                .orElse(null);
    }


}


