package com.mayab.quality.doubles;

public class Dependency {
	private final SubDependency subDependency;
	public double suma(double operando1, double operando2) {
		return operando1 + operando2;
	}
	public Dependency(SubDependency subDependency) {
		super();
		this.subDependency = subDependency;
	}
	
	public String getClassName() {
		return this.getClass().getSimpleName();
	}
	
	public String getSubdependencyClassName() {
		return subDependency.getClassName();
	}
	
	public int addTwo(int i) {
		return i + 2;
	}
	public String getClassNameUpperCase() {
		return getClassName().toUpperCase();
	}
	
}