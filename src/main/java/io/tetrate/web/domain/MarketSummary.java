package io.tetrate.web.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class MarketSummary {

	private boolean initialised;

	private List<Quote> topLosers;

	private List<Quote> topGainers;
	
	private BigDecimal tradeStockIndexAverage = BigDecimal.ZERO;
	
	private BigDecimal tradeStockIndexVolume = BigDecimal.ZERO;
	
	private BigDecimal tradeStockIndexOpenAverage = BigDecimal.ZERO;
	
	private BigDecimal change = BigDecimal.ZERO;
	
	private BigDecimal percentGain = BigDecimal.ZERO;
	
	private Date summaryDate = new Date();

	public List<Quote> getTopLosers() {
		return topLosers;
	}

	public void setTopLosers(List<Quote> topLosers) {
		this.topLosers = topLosers;
		this.initialised = true;
	}

	public List<Quote> getTopGainers() {
		return topGainers;
	}

	public void setTopGainers(List<Quote> topGainers) {
		this.topGainers = topGainers;
	}

	public BigDecimal getTradeStockIndexAverage() {
		return tradeStockIndexAverage;
	}

	public void setTradeStockIndexAverage(BigDecimal tradeStockIndexAverage) {
		this.tradeStockIndexAverage = tradeStockIndexAverage;
	}

	public BigDecimal getTradeStockIndexVolume() {
		return tradeStockIndexVolume;
	}

	public void setTradeStockIndexVolume(BigDecimal tradeStockIndexVolume) {
		this.tradeStockIndexVolume = tradeStockIndexVolume;
	}

	public BigDecimal getTradeStockIndexOpenAverage() {
		return tradeStockIndexOpenAverage;
	}

	public void setTradeStockIndexOpenAverage(BigDecimal tradeStockIndexOpenAverage) {
		this.tradeStockIndexOpenAverage = tradeStockIndexOpenAverage;
	}

	public BigDecimal getChange() {
		return change;
	}

	public void setChange(BigDecimal change) {
		this.change = change;
	}

	public BigDecimal getPercentGain() {
		return percentGain;
	}

	public void setPercentGain(BigDecimal percentGain) {
		this.percentGain = percentGain;
	}

	public Date getSummaryDate() {
		return summaryDate;
	}

	public void setSummaryDate(Date summaryDate) {
		this.summaryDate = summaryDate;
	}

	public boolean isInitialised() {
		return initialised;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MarketSummary [topLosers=").append(topLosers)
				.append(", topGainers=").append(topGainers)
				.append(", tradeStockIndexAverage=")
				.append(tradeStockIndexAverage)
				.append(", tradeStockIndexVolume=")
				.append(tradeStockIndexVolume)
				.append(", tradeStockIndexOpenAverage=")
				.append(tradeStockIndexOpenAverage).append(", change=")
				.append(change).append(", percentGain=").append(percentGain)
				.append(", summaryDate=").append(summaryDate).append("]");
		return builder.toString();
	}
	
	
}