package com.tek.interview.question;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/* ****************************************************************************************
 
Please remove all bugs from the code below to get the following output:

<pre>

*******Order 1*******
1 book: 13.74
1 music CD: 16.49
1 chocolate bar: 0.94
Sales Tax: 2.84
Total: 28.33
*******Order 2*******
1 imported box of chocolate: 11.5
1 imported bottle of perfume: 54.62
Sales Tax: 8.62
Total: 57.5
*******Order 3*******
1 Imported bottle of perfume: 32.19
1 bottle of perfume: 20.89
1 packet of headache pills: 10.73
1 box of imported chocolates: 12.94
Sales Tax: 8.77
Total: 67.98
Sum of orders: 153.81
 
</pre>
 
******************************************************************************************** */

/*
 * represents an item, contains a price and a description.
 *
 */
class Item {

	private String description;
	private float price;

	public Item(String description, float price) {
		super();
		this.description = description;
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public float getPrice() {
		return price;
	}
}

/*
 * represents an order line which contains the @link Item and the quantity.
 *
 */
class OrderLine {

	private int quantity;
	private Item item;

	/*
	 * @param item Item of the order
	 * 
	 * @param quantity Quantity of the item
	 */
	public OrderLine(Item item, int quantity) throws Exception {
		if (item == null) {
			System.err.println("ERROR - Item is NULL");
			throw new Exception("Item is NULL");
		}
		assert quantity > 0;
		this.item = item;
		this.quantity = quantity;
	}

	public Item getItem() {
		return item;
	}

	public int getQuantity() {
		return quantity;
	}
}

class Order {

	// Bug Fix: 1 (NullPointerException was thrown as it wasn't instantiated.)
	private List<OrderLine> orderLines = new ArrayList<OrderLine>();

	public void add(OrderLine o) throws Exception {
		if (o == null) {
			System.err.println("ERROR - Order is NULL");
			throw new IllegalArgumentException("Order is NULL");
		}
		orderLines.add(o);
	}

	public int size() {
		return orderLines.size();
	}

	public OrderLine get(int i) {
		return orderLines.get(i);
	}

	public void clear() {
		this.orderLines.clear();
	}
}

class calculator {

	public static double rounding(double value) {
		// Bug Fix: 2 (The casting to (int) makes everything an integer even after the divide by 100. 
		// Hence to make the result a double, 100 is changed to 100.0)
		return ((int)(value * 100)) / 100.0;
	}

	/**
	 * receives a collection of orders. For each order, iterates on the order lines and calculate the total price which
	 * is the item's price * quantity * taxes.
	 * 
	 * For each order, print the total Sales Tax paid and Total price without taxes for this order
	 */
	public double calculate(Map<String, Order> orders) {
		
		// Bug Fix: 3 (Since orders is not checked for NULL, it would lead to a NullPointerException when orders.entrySet() is fired.)
		if(orders == null) {
			throw new IllegalArgumentException("orders is Null.");
		}

		double grandTotal = 0;

		// Iterate through the orders
		for (Map.Entry<String, Order> entry : orders.entrySet()) {
			System.out.println("*******" + entry.getKey() + "*******");
			
			// Bug Fix: 4 (grandTotal should not be reset to 0 here as it is updated after iterating through each order.)
			//grandtotal = 0;

			Order order = entry.getValue();

			double totalTax = 0;
			double total = 0;

			// Iterate through the items in the order
			// Bug Fix: 5 (Fence Post Error. If index starts with 0, the condition should not be <=.)
			for (int i = 0; i < order.size(); i++) {

				// Calculate the taxes
				double tax = 0;

				Item item = order.get(i).getItem();
				
				tax = calculateTax(item);

				// Calculate the total price
				double totalPrice = item.getPrice() + tax;

				// Print out the item's total price
				// Bug Fix: 6 (Wrong way of rounding. To meet the requirements, we should be using BigDecimal.)
				System.out.println(item.getDescription() + ": " + roundToTwoDecimal(totalPrice));

				// Keep a running total
				totalTax += tax;
				total += item.getPrice();
			}

			// Print out the total taxes
			// Bug Fix: 7 (Wrong way of rounding. To meet the requirements, we should be using BigDecimal.)
			System.out.println("Sales Tax: " + roundToTwoDecimal(totalTax) /*Math.floor(totalTax)*/);

			// Bug Fix : 8 (Total should not have tax component.)
			//total = total + totalTax;

			// Print out the total amount
			// Bug Fix: 9 (Wrong way of rounding. To meet the requirements, we should be using BigDecimal.)
			System.out.println("Total: " + roundToTwoDecimal(total) /*Math.floor(total * 100) / 100*/);
			grandTotal += total;
		}

		//grandtotal = Math.floor(grandtotal * 100) / 100;
		// Bug Fix: 10 (Wrong way of rounding. To meet the requirements, we should be using BigDecimal.)
		System.out.println("Sum of orders: " + roundToTwoDecimal(grandTotal));
		
		return grandTotal;
	}

	public static double roundToTwoDecimal(Double value) {
		return BigDecimal.valueOf(value).setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
	}

	public static double calculateTax(Item item) {
		double tax;
		// Bug Fix: 11 (When the description has the word <imported> in upper or Mixed case, earlier it failed to detect it as an Imported one.)
		if (item.getDescription().toLowerCase().contains("imported")) {
			// imported items
			tax = roundToTwoDecimal(item.getPrice() * 0.15); // Extra 5% tax on
		} else {
			tax = roundToTwoDecimal(item.getPrice() * 0.10);
		}
		return tax;
	}
}

public class Foo {

	public static void main(String[] args) throws Exception {

		Map<String, Order> orders = new LinkedHashMap<String, Order>();

		Order order = new Order();

		//double grandTotal = 0;

		order.add(new OrderLine(new Item("book", (float) 12.49), 1));
		order.add(new OrderLine(new Item("music CD", (float) 14.99), 1));
		order.add(new OrderLine(new Item("chocolate bar", (float) 0.85), 1));

		orders.put("Order 1", order);

		// Reuse cart for an other order
		// order.clear();
		order = new Order();
		order.add(new OrderLine(new Item("imported box of chocolate", 10), 1));
		order.add(new OrderLine(new Item("imported bottle of perfume", (float) 47.50), 1));

		orders.put("Order 2", order);

		// Reuse cart for an other order
		//order.clear();
		
		order = new Order();
		order.add(new OrderLine(new Item("Imported bottle of perfume", (float) 27.99), 1));
		order.add(new OrderLine(new Item("bottle of perfume", (float) 18.99), 1));
		order.add(new OrderLine(new Item("packet of headache pills", (float) 9.75), 1));
		// Bug Fix: 12 (Import Misspelled)
		order.add(new OrderLine(new Item("box of imported chocolates", (float) 11.25), 1));

		orders.put("Order 3", order);

		new calculator().calculate(orders);

	}
}
