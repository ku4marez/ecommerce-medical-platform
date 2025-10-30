package com.github.ku4marez.ecom.starters.security;

import org.springframework.security.core.GrantedAuthority;
import java.util.Objects;

public final class SimpleGrantedAuthority implements GrantedAuthority {
    private final String authority;

    public SimpleGrantedAuthority(String authority) {
        this.authority = Objects.requireNonNull(authority, "authority cannot be null");
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    @Override
    public String toString() {
        return this.authority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SimpleGrantedAuthority that)) return false;
        return this.authority.equals(that.authority);
    }

    @Override
    public int hashCode() {
        return this.authority.hashCode();
    }
}
