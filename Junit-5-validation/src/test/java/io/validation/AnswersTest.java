package io.validation;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.platform.engine.discovery.PackageNameFilter;
import org.reflections.Reflections;

class AnswersTest  {

	static Class parent = Answer.class;
	static String logFile = "log.txt";
	static FileOutputStream log;
	static String PackageNamePrefix = "io.";
	static String[] AnswersLogs;
	static String nameField = "dbiwvwnwpsuvwmwperirv009_j";
	static int N;
	static ArrayList<Class<? extends Answer>> classes = new ArrayList<>();

	@BeforeAll
	static void initiate() throws FileNotFoundException, InstantiationException, IllegalAccessException {
		FileOutputStream log = new FileOutputStream(logFile);
		System.setOut(new PrintStream(log));
		Package[] pkgs = Package.getPackages();
		for(Package pkg: pkgs) {
			if(!pkg.getName().startsWith(PackageNamePrefix)) continue;
			Reflections reflections = new Reflections(pkg.getName());
			Set<Class<? extends Answer>> clss = reflections.getSubTypesOf(parent);
			for(Class cls: clss) classes.add(cls);
		}
		N = classes.size();
		AnswersLogs = new String[N];
		for(int i = 0; i < N; i++) AnswersLogs[i] = "";
		for(int i = 0; i < N; i++) {
			Answer ans = classes.get(i).newInstance();
			Method[] methods = ans.getClass().getDeclaredMethods();
			boolean add=false, subtract=false, multiply=false, divide=false;
			for(Method m : methods) {
				add = add || m.getName().equals("add");
				subtract = subtract || m.getName().equals("subtract");
				multiply = multiply || m.getName().equals("multiply");
				divide = divide || m.getName().equals("divide");
			}
			if(add && subtract && multiply && divide)
				AnswersLogs[i] += "-- No problems with methods\n";
			if(!add) AnswersLogs[i] += "** No addition method\n";
			if(!subtract) AnswersLogs[i] += "** No subtraction method\n";
			if(!multiply) AnswersLogs[i] += "** No multiplication method\n";
			if(!divide) AnswersLogs[i] += "** No division method\n";
		}
	}
	
	@DisplayName("Null Exception")
	@Tag("Exception")
	@Test
	void testNull() throws InstantiationException, IllegalAccessException {
		for(int i = 0; i < N; i++) {
			Answer ans = classes.get(i).newInstance();
			Integer a = null, b = null;
			boolean done1 = false, done2 = false, done3 = false, done4 = false;
			try {
				ans.add(a, b);
			} catch(NullPointerException e) {
				done1 = true;
			} catch(Exception e) {}
			try {
				ans.subtract(a, b);
			} catch(NullPointerException e) {
				done2 = true;
			} catch(Exception e) {}
			try {
				ans.multiply(a, b);
			} catch(NullPointerException e) {
				done3 = true;
			} catch(Exception e) {}
			try {
				ans.divide(a, b);
			} catch(NullPointerException e) {
				done4 = true;
			} catch(Exception e) {}
			if(done1 && done2 && done3 && done4)
				AnswersLogs[i] += "-- No problem with NullPointerException\n";
			if(!done1) AnswersLogs[i] += "** Hasn't caught NullPointerException in addition\n";
			if(!done2) AnswersLogs[i] += "** Hasn't caught NullPointerException in subtraction\n";
			if(!done3) AnswersLogs[i] += "** Hasn't caught NullPointerException in multiplication\n";
			if(!done4) AnswersLogs[i] += "** Hasn't caught NullPointerException in division\n";
		}
	}

	@DisplayName("IllegalArgument Exception")
	@Tag("Exception")
	@Test
	void testIllegal() throws InstantiationException, IllegalAccessException {
		for(int i = 0; i < N; i++) {
			Answer ans = classes.get(i).newInstance();
			String a = "Hey", b = "Man";
			boolean done1 = false, done2 = false, done3 = false, done4 = false;
			try {
				ans.add(a, b);
			} catch(IllegalArgumentException e) {
				done1 = true;
			} catch(Exception e) {}
			try {
				ans.subtract(a, b);
			} catch(IllegalArgumentException e) {
				done2 = true;
			} catch(Exception e) {}
			try {
				ans.multiply(a, b);
			} catch(IllegalArgumentException e) {
				done3 = true;
			} catch(Exception e) {}
			try {
				ans.divide(a, b);
			} catch(IllegalArgumentException e) {
				done4 = true;
			} catch(Exception e) {}
			if(done1 && done2 && done3 && done4)
				AnswersLogs[i] += "-- No problem with IllegalArgumentException\n";
			if(!done1) AnswersLogs[i] += "** Hasn't caught IllegalArgumentException in addition\n";
			if(!done2) AnswersLogs[i] += "** Hasn't caught IllegalArgumentException in subtraction\n";
			if(!done3) AnswersLogs[i] += "** Hasn't caught IllegalArgumentException in multiplication\n";
			if(!done4) AnswersLogs[i] += "** Hasn't caught IllegalArgumentException in division\n";
		}
	}

	@DisplayName("Division by zero")
	@Tag("Exception")
	@Test
	void testZeroDivision() throws InstantiationException, IllegalAccessException {
		for(int i = 0; i < N; i++) {			
			Answer ans = classes.get(i).newInstance();
			Integer a1 = 5, b1 = 0;
			boolean done = false;
			try {
				ans.divide(a1, b1);
			} catch(IllegalArgumentException e) {
				done = true;
			} catch(Exception e) {}
			if(done)
				AnswersLogs[i] += "-- No problem with Zero Division\n";
			if(!done) AnswersLogs[i] += "** Hasn't caught A Zero Division Exception\n";
		}
	}
	
	@DisplayName("Validate Addition")
	@Tag("Answer")
	@Test
	void testAddition() throws InstantiationException, IllegalAccessException {
		for(int i = 0; i < N; i++) {
			Answer ans = classes.get(i).newInstance();
			Random rnd = new Random();
			Integer a1 = rnd.nextInt(Integer.MAX_VALUE/2), b1 = rnd.nextInt(Integer.MAX_VALUE/2), expected1 = a1 + b1;
			Double a2 = rnd.nextDouble(), b2 = rnd.nextDouble(), expected2 = a2 + b2;
			Float a3 = rnd.nextFloat(), b3 = rnd.nextFloat(), expected3 = a3 + b3;
			boolean done = false;
			done = (ans.add(a1, b1).equals(expected1) && ans.add(a2, b2).equals(expected2) && ans.add(a3, b3).equals(expected3));
			if(done)
				AnswersLogs[i] += "-- Addition is implemented correctly\n";
			if(!done) AnswersLogs[i] += "** Addition is not implemented correctly\n";
		}
	}
	
	@DisplayName("Validate Subtraction")
	@Tag("Answer")
	@Test
	void testSubtraction() throws InstantiationException, IllegalAccessException {
		for(int i = 0; i < N; i++) {
			Answer ans = classes.get(i).newInstance();
			Random rnd = new Random();
			Integer a1 = rnd.nextInt(Integer.MAX_VALUE/2), b1 = rnd.nextInt(Integer.MAX_VALUE/2), expected1 = a1 - b1;
			Double a2 = rnd.nextDouble(), b2 = rnd.nextDouble(), expected2 = a2 - b2;
			Float a3 = rnd.nextFloat(), b3 = rnd.nextFloat(), expected3 = a3 - b3;
			boolean done = false;
			done = (ans.subtract(a1, b1).equals(expected1) && ans.subtract(a2, b2).equals(expected2) && ans.subtract(a3, b3).equals(expected3));
			if(done)
				AnswersLogs[i] += "-- Subtraction is implemented correctly\n";
			if(!done) AnswersLogs[i] += "** Subtraction is not implemented correctly\n";
		}
	}
	
	@DisplayName("Validate Multiplication")
	@Tag("Answer")
	@Test
	void testMultiplication() throws InstantiationException, IllegalAccessException {
		for(int i = 0; i < N; i++) {
			Answer ans = classes.get(i).newInstance();
			Random rnd = new Random();
			Integer a1 = rnd.nextInt(Integer.MAX_VALUE), b1 = rnd.nextInt(Integer.MAX_VALUE/a1), expected1 = a1 * b1;
			Double a2 = rnd.nextDouble(), b2 = rnd.nextDouble(), expected2 = a2 * b2;
			Float a3 = rnd.nextFloat(), b3 = rnd.nextFloat(), expected3 = a3 * b3;
			boolean done = false;
			done = (ans.multiply(a1, b1).equals(expected1) && ans.multiply(a2, b2).equals(expected2) && ans.multiply(a3, b3).equals(expected3));
			if(done)
				AnswersLogs[i] += "-- Multiplication is implemented correctly\n";
			if(!done) AnswersLogs[i] += "** Multiplication is not implemented correctly\n";
		}
	}

	@DisplayName("Validate Division")
	@Tag("Answer")
	@Test
	void testDivision() throws InstantiationException, IllegalAccessException {
		for(int i = 0; i < N; i++) {
			Answer ans = classes.get(i).newInstance();
			Random rnd = new Random();
			Integer a1 = rnd.nextInt(Integer.MAX_VALUE), b1 = rnd.nextInt(Integer.MAX_VALUE), expected1 = a1 / b1;
			Double a2 = rnd.nextDouble(), b2 = rnd.nextDouble(), expected2 = a2 / b2;
			Float a3 = rnd.nextFloat(), b3 = rnd.nextFloat(), expected3 = a3 / b3;
			boolean done = false;
			done = (ans.divide(a1, b1).equals(expected1) && ans.divide(a2, b2).equals(expected2) && ans.divide(a3, b3).equals(expected3));
			if(done)
				AnswersLogs[i] += "-- Division is implemented correctly\n";
			if(!done) AnswersLogs[i] += "** Division is not implemented correctly\n";
		}
	}
	
	@AfterAll
	static void closeMe() throws InstantiationException, IllegalAccessException, NoSuchFieldException, SecurityException {
		for(int i = 0; i < N; i++) {
			Answer ans = classes.get(i).newInstance();
			String fullClassName = classes.get(i).getName();
			String[] tmp = fullClassName.split("\\.");
			String name = tmp[tmp.length-1];
			System.out.println(classes.get(i).getField(nameField).get(null)+" log: "+fullClassName);
			System.out.println(AnswersLogs[i]);
		}
	}

}
