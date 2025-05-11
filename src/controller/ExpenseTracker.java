package controller;
import model.Expense;
import interfaces.Tracker;
import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExpenseTracker implements Tracker<Expense> {
    private static ExpenseTracker instance;
    private ArrayList<Expense> expenses;
    private static final String DATA_DIR = "data";

    private ExpenseTracker() {
        expenses = new ArrayList<>();
    }

    public static ExpenseTracker getInstance() {
        if (instance == null) {
            instance = new ExpenseTracker();
        }
        return instance;
    }

    public ArrayList<Expense> getExpenses() {
        return expenses;
    }

    @Override
    public void add(Expense expense) {
        expenses.add(expense);
    }

    @Override
    public void edit(Expense expense) {
        for (int i = 0; i < expenses.size(); i++) {
            if (expenses.get(i).getId().equals(expense.getId())) {
                expenses.set(i, expense);
                return;
            }
        }
    }

    @Override
    public void delete(String expenseId) {
        expenses.removeIf(expense -> expense.getId().equals(expenseId));
    }

    @Override
    public boolean validate(Expense expense) {
        // Basic validation for expense object
        return expense != null &&
                expense.getId() != null &&
                !expense.getId().isEmpty() &&
                expense.getAmount() > 0 &&
                expense.getDate() != null &&
                expense.getCategory() != null &&
                !expense.getCategory().isEmpty();
    }

    @Override
    public BigDecimal calculateAmount() {
        double total = expenses.stream().mapToDouble(Expense::getAmount).sum();
        return BigDecimal.valueOf(total);
    }

    // Legacy methods that can be kept for backward compatibility

    public void addExpense(Expense expense) {
        add(expense);
    }

    public void updateExpense(Expense expense) {
        edit(expense);
    }

    public void removeExpense(String expenseId) {
        delete(expenseId);
    }

    public double getTotalExpenses() {
        return calculateAmount().doubleValue();
    }

    public List<Expense> getExpensesByDateRange(LocalDate startDate, LocalDate endDate) {
        return expenses.stream()
                .filter(expense -> !expense.getDate().isBefore(startDate) && !expense.getDate().isAfter(endDate))
                .collect(Collectors.toList());
    }

    public List<Expense> getExpensesForMonth(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        return getExpensesByDateRange(startDate, endDate);
    }

    public Map<String, Double> getCategoryTotals() {
        Map<String, Double> categoryTotals = new HashMap<>();

        for (Expense expense : expenses) {
            String category = expense.getCategory();
            categoryTotals.put(category, categoryTotals.getOrDefault(category, 0.0) + expense.getAmount());
        }

        return categoryTotals;
    }

    public void clearData() {
        expenses.clear();
    }

    public boolean saveData(int userId) {
        String filePath = getExpenseFilePath(userId);
        createDataDirIfNeeded();

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(expenses);
            System.out.println("Expense data saved successfully for user ID: " + userId);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error saving expense data: " + e.getMessage());
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    public boolean loadData(int userId) {
        String filePath = getExpenseFilePath(userId);
        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println("No expense data file found for user ID: " + userId);
            expenses = new ArrayList<>();
            return true;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            expenses = (ArrayList<Expense>) ois.readObject();
            System.out.println("Expense data loaded successfully for user ID: " + userId);
            return true;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Error loading expense data: " + e.getMessage());
            expenses = new ArrayList<>();
            return false;
        }
    }

    private String getExpenseFilePath(int userId) {
        return DATA_DIR + "/" + userId + "_expenses.dat";
    }

    private void createDataDirIfNeeded() {
        File dataDir = new File(DATA_DIR);
        if (!dataDir.exists()) {
            dataDir.mkdir();
        }
    }
}