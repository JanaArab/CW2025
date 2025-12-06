package com.comp2042.tetris.utils;

/**
 * Annotation indicating that a method or class is functionally pure.
 * Pure functions have no side effects and always return the same output
 * for the same input.
 *
 * <p>This is a documentation annotation only and does not enforce purity.</p>
 *
 * @see MatrixOperations
 */
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface Pure {
}
