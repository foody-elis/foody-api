package com.example.foody.model.user;

import com.example.foody.utils.Role;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue(Role.Constants.MODERATOR_VALUE)
public class ModeratorUser extends User {
}
