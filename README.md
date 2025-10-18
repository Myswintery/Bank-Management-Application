# 🏦 JavaBankApp — Banking Management System  

## 📘 Overview  
**JavaBankApp** is a desktop-based banking management application developed in **Java**.  
It provides a secure and efficient platform for handling common banking operations such as **account creation, deposit, withdrawal, transaction history, and balance inquiry**.  

The application connects to a **MySQL database** for data storage and management, ensuring reliability and scalability.  

---

## ✨ Key Features  
- 👤 **Customer Account Management** – Create, update, and delete bank accounts.  
- 💰 **Transaction Handling** – Deposit and withdraw funds securely.  
- 📄 **Transaction Records** – Maintain and view detailed transaction history.  
- 🔐 **Database Integration** – Connects with MySQL for persistent data storage.  
- 🪟 **User-Friendly GUI** – Built using Java Swing for a clean and intuitive interface.  

---

## 🧰 Tech Stack  
| Category          | Technology               |
|-------------------|--------------------------|
| **Language**      | Java (JDK 8+)            |
| **IDE**           | NetBeans                 |
| **Database**      | MySQL                    |
| **Driver**        | MySQL Connector/J 8.0.33 |
| **GUI Framework** | Java Swing               |
| **Build Tool**    | Apache Ant (build.xml)   |

---

## ⚙️ Setup & Installation  

### Prerequisites  
- **Java JDK 8 or later**  
- **NetBeans IDE** (or any IDE supporting Ant builds)  
- **MySQL Server**

## 🧩 Database Schema
## Database Name: bankapp
**Table: users**
| Column | Type | Attributes | Description|
|---------|----------|---------|-----------------|
| id | INT | AUTO_INCREMENT, PRIMARY KEY | Unique user ID | 
| username | VARCHAR(100)	| NOT NULL | User’s login name|
| password | VARCHAR(100) | NOT NULL | Encrypted or plain password|
| current_balance | DECIMAL(10,2) | DEFAULT 0.00 | Current account balance|

**💳 Table: transactions**
| Column | Type | Attributes | Description|
|---------|----------|---------|-----------------|
|id | INT |	AUTO_INCREMENT, PRIMARY KEY |	Unique transaction ID|
|transaction_amount |	DECIMAL(10,2) |	NOT NULL |	Amount involved in transaction| 
|transaction_type |	VARCHAR(50) |	NOT NULL |	Type: Deposit / Withdrawal |
|user_id |	INT |	FOREIGN KEY (user_id) REFERENCES users(id) |	Associated user ID|

---

## 🚀Future Enhancements
- 🔔 Email/SMS notifications for transactions.
- 📊 Spending and savings analytics dashboard.
- 🧠 Machine learning-based insights and recommendations.

## 💬 Author  
**MysWintery**  
🎓 BSc. Information Technology | 💡 Data Scientist Enthusiast  
📧 [Contact Here](mailto:myswgamex@gmail.com)
