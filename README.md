I AM BUDGET
A personal finance and budget management application built with JavaFX that helps users track expenses, manage budgets, and achieve financial goals.

Table of Contents

Features
Screenshots
Installation
Usage
Project Structure
Technologies Used
Contributors

Features

User Authentication: Secure login and signup functionality
Budget Creation and Tracking: Create and monitor customized budgets for different spending categories
Expense Tracking: Log and categorize daily expenses
Income Tracking: Record and manage various income sources
Dashboard Analytics: Visualize spending patterns and financial health
Budgeting Analysis: Get insights into your spending habits and budget performance
Savings Goals: Set and track financial goals
Reminders: Set notifications for bills and important financial deadlines
Navigation System: Easy-to-use interface with a dedicated navigation bar
Data Persistence: All user data is securely stored between sessions



Usage
Getting Started

Create an Account

Launch the application
Click on "Sign Up" on the login screen
Fill in your details to create an account


Login

Enter your username and password
Click "Login" to access your dashboard


Set Up Your Budget

Navigate to the budget section
Create budget categories (e.g., Housing, Food, Entertainment)
Allocate monthly amounts to each category


Track Expenses

Add new expenses as you make purchases
Categorize each expense
Add notes or receipts if needed


Monitor Your Progress

Check the dashboard for spending summaries
View reports to analyze spending patterns
Track progress toward your financial goals



Project Structure
Based on the actual project organization:
I AM BUDGET/
├── controller/
│   ├── Auth.java
│   ├── BudgetingAnalysis.java
│   ├── BudgetTracker.java
│   ├── Dashboard.java
│   ├── ExpenseTracker.java
│   ├── ExpenseTrackerController.java
│   ├── HomePage.java
│   ├── IncomeTrackingController.java
│   ├── Login.java
│   ├── NavBar.java
│   ├── Reminders.java
│   ├── ReminderTracker.java
│   ├── SavingsGoals.java
│   └── SignUp.java
├── interfaces/
│   └── Tracker.java
├── model/
│   ├── Budget.java
│   ├── Expense.java
│   ├── Income.java
│   ├── IncomeTracker.java
│   ├── Reminder.java
│   ├── SystemManager.java
│   └── User.java
├── service/
│   └── [Service classes]
├── storage/
│   └── UserDatabase.java
├── ui/
│   ├── BudgetingAnalysis.fxml
│   ├── dashboard.fxml
│   ├── ExpenseTracker.fxml
│   ├── HomePage.fxml
│   ├── Login.fxml
│   ├── NavBar.fxml
│   ├── Reminders.fxml
│   ├── SignUp.fxml
│   └── TrackingIncome.fxml
├── Main.java
├── .gitignore
├── IAMBUDGET.iml
└── users.dat
Technologies Used

JavaFX: For the graphical user interface
FXML: For UI layout design
CSS: For styling the application
Java: Core programming language
Maven: Dependency management and build automation
File-based Storage: Using custom data files (users.dat) for data persistence


Contributors

Mohamed Eltanahy - Lead Developer
khaled hawash
Yahia diaa 



"I AM BUDGET" - Take control of your financial future!
