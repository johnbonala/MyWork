package com.lifelens.automation.tabulardata;

public class Value {

	private boolean acceptAny = false;
	private String value;

	private static final Value ACCEPT_ANY = new Value();

	private Value() {
		value = "??";
		this.acceptAny = true;
	}

	public Value(String value) {
		this.value = value;
	}

	public String toString() {
		return this.value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {

		if (acceptAny) {
			return true;
		}

		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Value other = (Value) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	public static Value acceptAny() {
		return ACCEPT_ANY;
	}

}
