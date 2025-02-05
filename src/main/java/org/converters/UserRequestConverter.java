package org.converters;

import org.models.User;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class UserRequestConverter {

    public User convert(String name, String age, String birthDate) {
        return convert(null, name, age, birthDate);
    }

    public User convert(String id, String name, String age, String birthDate) {
        User user = new User();
        if (id != null) {
            user.setId(Integer.parseInt(id));
        }
        user.setName(name);
        user.setAge(Integer.parseInt(age));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(birthDate, formatter);
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        user.setBirthDate(date);
        return user;
    }
}
