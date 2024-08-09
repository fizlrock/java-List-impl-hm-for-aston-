package dev.fizlrock.configurations;

import org.apache.catalina.startup.Tomcat;
import org.apache.coyote.http2.Http2Protocol;

/**
 * TomcatConfiguration
 */
public class TomcatConfiguration {

  public static Tomcat getTomcat() {

    Tomcat tomcat = new Tomcat();
    tomcat.setBaseDir("build/tomcat_trashcan");
    tomcat.setPort(8080);
    var connector = tomcat.getConnector();
    connector.addUpgradeProtocol(new Http2Protocol());

    return tomcat;
  }

}
