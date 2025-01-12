package io.tetrate.web.domain;

import java.math.BigDecimal;
import java.util.*;

public class Holding {

	private Integer id;
	private String symbol;
	private Integer quantity = 0;
	private String currency;
	private BigDecimal purchaseValue = BigDecimal.ZERO;
	private BigDecimal sellValue = BigDecimal.ZERO;
	private List<Order> orders = new ArrayList<>();
	private BigDecimal currentValue = BigDecimal.ZERO;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public BigDecimal getPurchaseValue() {
		return purchaseValue;
	}
	public void setPurchaseValue(BigDecimal purchaseValue) {
		this.purchaseValue = purchaseValue;
	}
	public List<Order> getOrders() {
		return orders;
	}
	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}
	public BigDecimal getCurrentValue() {
		return currentValue;
	}
	public void setCurrentValue(BigDecimal currentValue) {
		this.currentValue = currentValue;
	}
	
	public void addOrder(Order order) {
		//check order is not already in.
		if (orders.contains(order)) {
			//TODO: throw RuntimeException?? and do nothing;
		} else {
			orders.add(order);
			//update stats
			if (order.getOrderType().equals(OrderType.BUY)) {
				setQuantity(getQuantity()+order.getQuantity());
				setPurchaseValue(getPurchaseValue().add(order.getPrice().multiply(new BigDecimal(order.getQuantity()))));
			} else if (order.getOrderType().equals(OrderType.SELL)) {
				setQuantity(getQuantity()-order.getQuantity());
				setSellValue(getSellValue().add(order.getPrice().multiply(new BigDecimal(order.getQuantity()))));
			}
		}
	}

	public BigDecimal getSellValue() {
		return sellValue;
	}
	public void setSellValue(BigDecimal sellPrice) {
		this.sellValue = sellPrice;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Holding [id=").append(id).append(", symbol=")
				.append(symbol).append(", quantity=").append(quantity)
				.append(", currency=").append(currency)
				.append(", purchaseValue=").append(purchaseValue)
				.append(", sellValue=").append(sellValue).append(", orders=")
				.append(orders).append(", currentValue=").append(currentValue)
				.append("]");
		return builder.toString();
	}

}