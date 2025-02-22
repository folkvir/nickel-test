# Nickel Test

## Requirements (Tested with OSX Catalina)

* **Scala version:** `2.13.3`
* **JDK:** 1.8
* **OS:** MacOS Catalina v10.15.6
* **sbt:** `sbt version in this project: 1.3.13` and `
             sbt script version: 1.3.13`

## Usage

`java -jar nickel.jar --help`

## Build and CLI Usage
```
cd nickel-test 
sbt clean assembly
# navigate to the fat jar in target/scala-{YOUR-version}/nickel.jar
# run with scala (for scala 2.13)
scala target/scala-2.13/nickel.jar --example
# run with java
java -jar target/scala-2.13/nickel.jar --example
```

## The problem

The goal is to determine the price of a cart full of books with some reduction prices available.
The reductions are constrained by a fixed number of different books. Higher the number higher the reduction.
The problem is: Given a list of reduction, a cart, determine the smallest price for the client.

Eg: 
* Articles: (book 1, 8$) (book 2, 8$) (book 3, 8$)
* calculator.Cart: 2 * book-1 + 2 * book-2 + 1 * book-3
* Reductions: 5% for 2 differents books, 10% for three different books
**What is the smallest price; reformulated: what is the best composition of reductions offering the best price for the client?**

##  Solutions
* **\[Implemented\]** For the sake of simplicity, all books have the same price, then the idea is: for a specified cart generate all possible reduction combination until 
the cart cannot apply anymore reduction. Then, select the smallest price given by all combinations of reduction for a given cart.

* For different book prices, then, it is the same idea 
    * Add a new Reduction(0, 1) and reimplement applyReduction with a sort by count and price, and return the smallest 
    price because it will be always efficient for other reductions to be applied on books with higher price

* For reductions with different constraints, eg: book with id=0 is at 50% today, then add a Reduction(0.5, 1) and reimplement the canBeApply() with the condition that the book is in the cart. Dont forget to reimplement the applyReduction accordingly...