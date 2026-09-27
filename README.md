# OOP Coursework: Board Game Store System

Console-based Java app for running a board game and accessory store. There are two roles, Admin and Customer, each with their own CLI. Users and stock are stored in plain text files.

## Requirements

- JDK 8+

## Project layout

```
.
├── .gitignore
├── src/              Java source files
├── Stock.txt
├── UserAccounts.txt
└── README.md
```

Source files are in `src/`, in the default package. `Stock.txt` and `UserAccounts.txt` need to stay at the project root since both are read using relative paths from wherever the program is run.

## Running

```bash
javac -d bin src/*.java
java -cp bin Main
```

Run the `java` command from the project root, not from inside `bin`, so `UserAccounts.txt` and `Stock.txt` resolve correctly. `Stock.txt` gets created automatically the first time a product is added if it doesn't already exist. `UserAccounts.txt` needs to exist beforehand though, otherwise no users load and the login menu is empty.

To work on this in Eclipse, import `src` into a new project rather than as an existing one, the project's own `.classpath`/`.project` files are no longer committed.

## Data files

`UserAccounts.txt`, one user per line:
```
ID; Name; HouseNumber; Postcode; City; Role
```
`Role` is `admin` or `customer`.

`Stock.txt`, one product per line:
```
ID; Category; Type; Name; Price; Stock; PurchaseCost; Extra
```
`Category` is `board game` or `accessory`. `Extra` holds max players for board games, or compatibility for accessories.

## Classes

| File | Role |
|---|---|
| `Main.java` | Entry point. Loads users, shows the login menu, routes to `AdminCLI` or `CustomerCLI` |
| `User.java` | Abstract base for account holders (id, name, address) |
| `Admin.java` | `User` subclass routed to the admin CLI |
| `Customer.java` | `User` subclass holding a `ShoppingCart` |
| `AdminCLI.java` | Admin menu: view stock with purchase cost, add products, adjust stock levels |
| `CustomerCLI.java` | Customer menu: browse, search, manage basket, checkout |
| `Product.java` | Abstract base for store items (id, category, name, cost, stock, price) |
| `BoardGame.java` | `Product` subclass, adds type and max players |
| `Accessory.java` | `Product` subclass, adds type and compatibility |
| `ProductCategory.java` | Enum: `BOARDGAME`, `ACCESSORY` |
| `Stock.java` | Loads and persists `Stock.txt`, adds, updates, and searches products |
| `ShoppingCart.java` | Per-customer basket. Add/remove items, calculate total, two-phase checkout |
| `PaymentMethod.java` | Interface: `processPayment(total, address) -> Receipt` |
| `Paypal.java` | `PaymentMethod` implementation using an email address |
| `CreditCard.java` | `PaymentMethod` implementation using card number and security code |
| `Receipt.java` | Wraps the formatted text of a completed transaction |
| `ServiceResult.java` | Enum of outcome codes returned by cart and stock operations |
| `Address.java` | Postcode, city, house number, formats a billing address |

## Design notes

`Product` and `User` are abstract classes extended by the concrete types (`BoardGame`/`Accessory`, `Admin`/`Customer`), so shared fields and behaviour live in one place.

`PaymentMethod` is an interface, so `ShoppingCart.pay()` doesn't need to know which payment type it's handling.

`ServiceResult` gives cart and stock operations a shared set of outcome codes instead of relying on booleans or exceptions.

Checkout in `ShoppingCart.pay()` happens in two steps. First it checks that every item in the basket still has enough stock, then it deducts everything. This avoids partial updates if stock changed after items were added to the basket.
