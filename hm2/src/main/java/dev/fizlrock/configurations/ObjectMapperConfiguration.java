package dev.fizlrock.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * ObjectMapperConfiguration
 */
public class ObjectMapperConfiguration {

  public static ObjectMapper getObjectMapper() {
    return new ObjectMapper();
  }

}
