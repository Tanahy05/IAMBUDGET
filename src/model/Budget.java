package model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Budget class represents a budget category with spending limit
 * Based on the class diagram in the provided images
 */
public class Budget implements Serializable {

    private static final long serialVersionUID = 1L;

    private String budgetId;
    private String category;
    private BigDecimal limit;
    private String period; // Monthly, Weekly, etc.

    /**
     * Constructor for creating a new budget
     *
     * @param budgetId Unique identifier for this budget
     * @param category Category of expenses this budget tracks
     * @param limit Maximum spending limit for this budget
     * @param period The time period this budget covers (e.g., "Monthly")
     */
    public Budget(String budgetId, String category, BigDecimal limit, String period) {
        this.budgetId = budgetId;
        this.category = category;
        this.limit = limit;
        this.period = period;
    }

    /**
     * Gets the budget ID
     *
     * @return Budget ID
     */
    public String getBudgetId() {
        return budgetId;
    }

    /**
     * Gets the category for this budget
     *
     * @return Category name
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the category for this budget
     *
     * @param category New category name
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Gets the spending limit
     *
     * @return Budget limit
     */
    public BigDecimal getLimit() {
        return limit;
    }

    /**
     * Sets the spending limit
     *
     * @param limit New budget limit
     */
    public void setLimit(BigDecimal limit) {
        this.limit = limit;
    }

    /**
     * Gets the time period
     *
     * @return Time period (e.g., "Monthly")
     */
    public String getPeriod() {
        return period;
    }

    /**
     * Sets the time period
     *
     * @param period New time period
     */
    public void setPeriod(String period) {
        this.period = period;
    }

    /**
     * Compares budget with actual spending to determine if over budget
     *
     * @param actualSpending The actual amount spent in this category
     * @return true if over budget, false otherwise
     */
    public boolean compareWithActualSpending(BigDecimal actualSpending) {
        return actualSpending.compareTo(limit) > 0;
    }

    /**
     * Updates the budget limit for a specific category
     *
     * @param category Category to update
     * @param amount New limit amount
     */
    public void updateLimit(String category, BigDecimal amount) {
        if (this.category.equals(category)) {
            this.limit = amount;
        }
    }

    /**
     * Generates budget recommendations based on spending patterns
     *
     * @return Array of recommendation strings
     */
    public String[] generateRecommendations() {
        // In a real implementation, this would analyze spending patterns
        // and generate intelligent recommendations
        return new String[] {
                "Consider reducing " + category + " spending by 10%",
                "Set aside 5% of your " + category + " budget for savings"
        };
    }

    @Override
    public String toString() {
        return category + " budget: " + limit + " (" + period + ")";
    }
}