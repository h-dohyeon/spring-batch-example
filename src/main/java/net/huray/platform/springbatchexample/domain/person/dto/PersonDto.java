package net.huray.platform.springbatchexample.domain.person.dto;

import lombok.*;

@Getter @Setter
@Builder @AllArgsConstructor
@NoArgsConstructor
public class PersonDto {

    private String lastName;
    private String firstName;

    public String getName() {
        return firstName + " "+ lastName;
    }

}
