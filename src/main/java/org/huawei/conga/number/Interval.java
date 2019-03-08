package org.huawei.conga.number;
public class Interval extends Number {

	private Double ub;
	private Double lb;

	public Interval(Double lb, Double ub) {
		this.ub = ub;
		this.lb = lb;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -8581848488683691343L;

	@Override
	public int intValue() {
		return ub.intValue();
	}

	@Override
	public long longValue() {
		return ub.longValue();
	}

	@Override
	public float floatValue() {
		return ub.floatValue();
	}

	@Override
	public double doubleValue() {
		return ub.floatValue();
	}

	public Double getLb() {
		return lb;
	}

	public Double getUb() {
		return ub;
	}
	
	public void setLb(Double lb) {
		this.lb = lb;
	}
	
	public void setUb(Double ub) {
		this.ub = ub;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Interval)) {
			return false;
		}
		Interval objI = (Interval) obj;
		return this.getLb().equals(objI.getLb()) && this.getUb().equals(objI.getUb());
	}
	
	@Override
	public String toString() {
		return "["+lb+","+ ub+"]";
	}

}
