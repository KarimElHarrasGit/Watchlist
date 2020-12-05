package com.openclassrooms.watchlist.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import java.lang.annotation.RetentionPolicy;

@Target ({ElementType.FIELD})
@Retention (RetentionPolicy.RUNTIME)
@Constraint (validatedBy= PriorityValidator.class)
public @interface Priority {
	
	String message() default "Please enter M,L or H for priority";
	
	Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}