package com.chenxin.authority.util;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

import java.util.Locale;

/**
 * Hibernate {@link PhysicalNamingStrategy} that follows Spring recommended naming
 * conventions.
 *
 * @author Phillip Webb
 * @since 1.4.0
 */
public class SpringPhysicalNamingStrategy implements PhysicalNamingStrategy {

    @Override
    public Identifier toPhysicalCatalogName(Identifier name,
                                            JdbcEnvironment jdbcEnvironment) {
        return apply(name);
    }

    @Override
    public Identifier toPhysicalSchemaName(Identifier name,
                                           JdbcEnvironment jdbcEnvironment) {
        return apply(name);
    }

    @Override
    public Identifier toPhysicalTableName(Identifier name,
                                          JdbcEnvironment jdbcEnvironment) {
        return apply(name);
    }

    @Override
    public Identifier toPhysicalSequenceName(Identifier name,
                                             JdbcEnvironment jdbcEnvironment) {
        return apply(name);
    }

    @Override
    public Identifier toPhysicalColumnName(Identifier name,
                                           JdbcEnvironment jdbcEnvironment) {
        return apply(name);
    }

    private Identifier apply(Identifier name) {
        if (name == null) {
            return null;
        }
        StringBuilder text = new StringBuilder(name.getText().replace('.', '_'));
        for (int i = 1; i < text.length() - 1; i++) {
            if (isUnderscoreRequired(text.charAt(i - 1), text.charAt(i), text.charAt(i + 1))) {
                text.insert(i++, '_');
            }
        }
        return new Identifier(text.toString().toLowerCase(Locale.ROOT), name.isQuoted());
    }

    private boolean isUnderscoreRequired(char before, char current, char after) {
        return Character.isLowerCase(before) && Character.isUpperCase(current)
                && Character.isLowerCase(after);
    }

}
