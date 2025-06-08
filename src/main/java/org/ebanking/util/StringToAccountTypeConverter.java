package org.ebanking.util;

import org.ebanking.model.Account;
import org.ebanking.model.enums.AccountType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToAccountTypeConverter implements Converter<String, AccountType> {
    @Override
    public AccountType convert(String source) {
        return AccountType.valueOf(source.toUpperCase());
    }
}
