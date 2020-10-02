# Nickel Test

## Requirements (Tested with OSX Catalina)

* **Scala version:** `2.13.3`
* **JDK:** 1.8
* **OS:** MacOS Catalina v10.15.6
* **sbt:** `sbt version in this project: 1.3.13` and `
             sbt script version: 1.3.13`
             
## CLI Usage
run: `cd nickel-test && sbt run`

## How does it work?
The goal is to determine the price of a cart full of books with some reduction prices available.
The reductions are constrained by a fixed number of different books. Higher the number higher the reduction.
The problem is: Given a list of reduction (containing a reduction price and a number of different books), a cart, 
determine the smallest price for the client.

Eg: 
* Articles: (book 1, 10$) (book 2, 20$) (book 3, 5$)
* Cart: 2 * book-1 + 2 * book-2 + 1 * book-3
* Reductions: 5% for 2 differents books, 10% for three different books
**What is the smallest price; reformulated: what is the best composition of reductions offering the best price for the client?**