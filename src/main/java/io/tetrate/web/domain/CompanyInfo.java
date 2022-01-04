package io.tetrate.web.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Represents company information.
 *  
 * {
 *	"Symbol":"NFLX",
 *	"Name":"Netflix Inc",
 *	"Exchange":"NASDAQ"
 * }
 * 
 * @author Adam Zwickey
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompanyInfo implements Comparable<CompanyInfo> {

	private String symbol;
	private String companyName;
	private String exchange;
	
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getExchange() {
		return exchange;
	}
	public void setExchange(String exchange) {
		this.exchange = exchange;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CompanyInfo [symbol=").append(symbol).append(", name=")
				.append(companyName).append(", exchange=").append(exchange)
				.append("]");
		return builder.toString();
	}
	@Override
	public int compareTo(CompanyInfo o) {
		if(o == null){
			return -1;
		}
		return this.getSymbol().compareTo(o.getSymbol());
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((exchange == null) ? 0 : exchange.hashCode());
		result = prime * result + ((companyName == null) ? 0 : companyName.hashCode());
		result = prime * result + ((symbol == null) ? 0 : symbol.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CompanyInfo other = (CompanyInfo) obj;
		if (exchange == null) {
			if (other.exchange != null)
				return false;
		} else if (!exchange.equals(other.exchange))
			return false;
		if (companyName == null) {
			if (other.companyName != null)
				return false;
		} else if (!companyName.equals(other.companyName))
			return false;
		if (symbol == null) {
			if (other.symbol != null)
				return false;
		} else if (!symbol.equals(other.symbol))
			return false;
		return true;
	}
}
