package com.hash.core;

import java.util.ArrayList;

public class CalculatedMark {
	
	String name;
	ArrayList<String> marks;
	ArrayList<Integer> weights;
	
	public CalculatedMark()
	{
		name = "";
		weights = new ArrayList<Integer>();
		marks = new ArrayList<String>();
	}
	
	public int size()
	{
		return marks.size();
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<String> getMarks() {
		return marks;
	}

	public void setMarks(ArrayList<String> marks) {
		this.marks = marks;
	}

	public ArrayList<Integer> getWeights() {
		return weights;
	}

	public void setWeights(ArrayList<Integer> weights) {
		this.weights = weights;
	}

	public CalculatedMark(String n, ArrayList<String> a, ArrayList<Integer> w)
	{
		name = n;
		marks = a;
		weights = w;
	}

}
