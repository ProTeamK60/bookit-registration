package se.knowit.bookitregistration.service;

import se.knowit.bookitregistration.model.Registration;

import java.util.function.Predicate;

public class Predicates {

    static final Predicate<Registration> matchAll = r -> true;

}
