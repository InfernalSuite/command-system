package net.endrealm.libraries.api.commands.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface IntSettings {
    boolean hasInnerBound() default false;
    boolean hasOuterBound() default false;
    int innerBound() default 0;
    int outerBound() default 0;
}
