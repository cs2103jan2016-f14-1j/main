//@@author A0125347H

package shared;

public class IntegerPair {
	private int s1;
	private int s2;
	
	public IntegerPair(int s1, int s2) {
		this.s1 = s1;
		this.s2 = s2;
	}
	
	public void setInt1(int s) {
		s1 = s;
	}
	
	public void setInt2(int s) {
		s2 = s;
	}
	
	public int getInt1() {
		return s1;
	}
	
	public int getInt2() {
		return s2;
	}
	
	public boolean inBetween(int s) {
		return (s >= s1) && (s <= s2);
	}
}
