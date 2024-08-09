package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import dev.fizlrock.myspring.web.FrontController;

/**
 * FrontControllerTests
 */
public class FrontControllerTests {

  @ParameterizedTest
  @CsvSource({ "/,/", "/user,/user" })
  void successMatchPathWithoutParams(String template, String path) {
    var map = FrontController.matchPathAndExtractVariable(template, path);
    assertNotNull(map);
    assertTrue(map.isEmpty());

  }

  @ParameterizedTest
  @CsvSource({ "/a, /", "/b,/a", "/user/{someV},/user" })
  void failedMatchPathWithoutParams(String template, String path) {
    var map = FrontController.matchPathAndExtractVariable(template, path);
    assertNull(map);
  }

  @Test
  void successMatchPathWithParams() {
    var map = FrontController.matchPathAndExtractVariable("/user/{userId}/home/{sityName}", "/user/1/home/lenks");
    assertNotNull(map);
    var expectedMap = Map.of("userId", "1", "sityName", "lenks");
    assertEquals(expectedMap, map);
  }


  

}
