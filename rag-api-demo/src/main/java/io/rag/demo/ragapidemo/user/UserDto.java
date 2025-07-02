package io.rag.demo.ragapidemo.user;

import java.io.Serializable;
import lombok.Value;

/**
 * DTO for {@link User}
 */
@Value
public class UserDto implements Serializable {

  String name;
  String email;
  Integer age;
  String address;
}