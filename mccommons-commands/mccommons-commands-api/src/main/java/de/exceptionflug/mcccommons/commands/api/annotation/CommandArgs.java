package de.exceptionflug.mcccommons.commands.api.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandArgs {
    int maxArgsLength() default -1;

    int minArgsLength() default -1;
}
