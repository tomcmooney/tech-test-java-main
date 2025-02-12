package com.systemloco.techtest.JavaTechTest.api.serialisers;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonSerialize(using = Truncated.TruncatedSerializer.class)
public @interface Truncated {
    int decimalPlaces() default 2;

    class TruncatedSerializer extends StdSerializer<Double> implements ContextualSerializer {
        private int decimalPlaces;

        public TruncatedSerializer() {
            super(Double.class);
        }

        public TruncatedSerializer(
                final int decimalPlaces
        ) {
            super(Double.class);
            this.decimalPlaces = decimalPlaces;
        }

        @Override
        public void serialize(
                @Nullable final Double target,
                @NotNull final JsonGenerator generator,
                @NotNull final SerializerProvider provider
        ) throws IOException {
            if (target == null) {
                generator.writeObject(null);
                return;
            }
            final var exp = Math.pow(10.0, decimalPlaces);
            generator.writeObject(
                    Math.round(target * exp) / exp
            );
        }

        @Override
        public JsonSerializer<?> createContextual(
                @NotNull final SerializerProvider provider,
                @Nullable final BeanProperty property
        ) {
            if (property == null) {
                return null;
            }
            final var target = property.getAnnotation(Truncated.class);
            if (target == null) {
                return null;
            }
            return new TruncatedSerializer(target.decimalPlaces());
        }
    }
}
