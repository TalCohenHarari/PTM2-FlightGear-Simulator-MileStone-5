package model.interpreter.Expression;

public class Number implements Expression{

	private double value;
	
	public Number() {
		this.value=0;
	}
	
	public Number(double value) {
		this.value=value;
	}
	
	public void setValue(double value){
		this.value=value;
	}
	
	public String getCalculateString() {
		return this.value + "";
	}

	@Override
	public double calculate() {
		return value;
	}
}
