package io.validation;

abstract class Answer {
	<T> T add(T a, T b) {return a;}
	<T> T subtract(T a, T b) {return b;}
	<T> T multiply(T a, T b) {return a;}
	<T> T divide(T a, T b) {return b;}
}
