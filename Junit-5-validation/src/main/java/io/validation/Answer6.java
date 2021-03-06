package io.validation;

class Answer6 extends Answer {
	public static String dbiwvwnwpsuvwmwperirv009_j = "StudentF";
	public <T> T add(T a, T b) {
		if(a == null || b == null)
			throw new NullPointerException("One of the arguments is null");
		if(a instanceof Integer && b instanceof Integer)
			return (T) new Integer(((Integer)a).intValue() + ((Integer)b).intValue());
		if(a instanceof Double && b instanceof Double)
			return (T) new Double(((Double)a).doubleValue() + ((Double)b).doubleValue());
		if(a instanceof Float && b instanceof Float)
			return (T) new Float(((Float)a).floatValue() + ((Float)b).floatValue());
		else
			throw new IllegalArgumentException("Datatypes have no addition operator");
	}
	public <T> T subtract(T a, T b) {
		if(a == null || b == null)
			throw new NullPointerException("One of the arguments is null");
		if(a instanceof Integer && b instanceof Integer)
			return (T) new Integer(((Integer)a).intValue() - ((Integer)b).intValue());
		if(a instanceof Double && b instanceof Double)
			return (T) new Double(((Double)a).doubleValue() - ((Double)b).doubleValue());
		if(a instanceof Float && b instanceof Float)
			return (T) new Float(((Float)a).floatValue() - ((Float)b).floatValue());
		else
			throw new IllegalArgumentException("Datatypes have no addition operator");
	}
	public <T> T multiply(T a, T b) {
		if(a == null || b == null)
			throw new NullPointerException("One of the arguments is null");
		if(a instanceof Integer && b instanceof Integer)
			return (T) new Integer(((Integer)a).intValue() * ((Integer)b).intValue());
		if(a instanceof Double && b instanceof Double)
			return (T) new Double(((Double)a).doubleValue() * ((Double)b).doubleValue());
		if(a instanceof Float && b instanceof Float)
			return (T) new Float(((Float)a).floatValue() * ((Float)b).floatValue());
		else
			throw new IllegalArgumentException("Datatypes have no addition operator");
	}
	public <T> T divide(T a, T b) {
		if(a == null || b == null)
			throw new NullPointerException("One of the arguments is null");
		if((b instanceof Integer || b instanceof Double || b instanceof Float) && ((Number)b).doubleValue() == 0.0)
			throw new IllegalArgumentException("Division by zero is not allowed");
		if(a instanceof Integer && b instanceof Integer)
			return (T) new Integer(((Integer)a).intValue() / ((Integer)b).intValue());
		if(a instanceof Double && b instanceof Double)
			return (T) new Double(((Double)a).doubleValue() / ((Double)b).doubleValue());
		if(a instanceof Float && b instanceof Float)
			return (T) new Float(((Float)a).floatValue() / ((Float)b).floatValue());
		else
			throw new IllegalArgumentException("Datatypes have no addition operator");
	}
}
