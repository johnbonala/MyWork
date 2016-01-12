package com.lifelens.automation.tabulardata;

public class Header implements Comparable<Header> {

	public String headerName;
	public int xssColumnNo;

	public Header(String name, int colNo) {
		this.xssColumnNo = colNo;
		this.headerName = name;
	}

	@Override
	public int compareTo(Header obj) {
		if (!(obj instanceof Header)) {
			return -1;
		}
		Header other = (Header) obj;
		return xssColumnNo - other.xssColumnNo;
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
		result = prime * result + ((headerName == null) ? 0 : headerName.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Header other = (Header) obj;
		if (headerName == null) {
			if (other.headerName != null)
				return false;
		} else if (!headerName.equals(other.headerName))
			return false;
		return true;
	}

	public String toString() {
		return headerName + " (" + xssColumnNo + ")";
	}
}