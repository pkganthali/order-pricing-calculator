package com.tek.interview.question;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PricingCalculatorTest {

	@Test
	public void testCalculateTaxForLocalGoods() {
		float price = 500f;
		Item localMade = new Item("Armani Crew Neck Tee", price);
		double taxOfArmaniTee = calculator.calculateTax(localMade);
		// Making sure that a 10% tax is levied.
		assertTrue(50 == taxOfArmaniTee);
	}

	@Test
	public void testCalculateTaxForImportedGoods() {
		float price = 500f;
		Item localMade = new Item("Imported Elephant Tusk", price);
		double taxOfArmaniTee = calculator.calculateTax(localMade);
		// Making sure that a 15% tax is levied.
		assertTrue(75 == taxOfArmaniTee);
	}

	@Test
	public void testRounding() {
		assertTrue(45.67 == calculator.rounding(45.67899943));
	}

}
