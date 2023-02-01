package com.serverless;

import org.xml.sax.helpers.DefaultHandler;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.security.KeyStore;

public class OtherClass {
  public char[] unknownCharArray;

  public void saxParserBad(DefaultHandler dh) throws Exception {
    SAXParserFactory spf = SAXParserFactory.newInstance();
    SAXParser parser = spf.newSAXParser();
    parser.parse("uri", dh);
  }

  public void incorrectUsage2() throws Exception {
    KeyStore keystore = KeyStore.getInstance("PKCS12", "BC");
    keystore.load(null, unknownCharArray);
    keystore.setEntry(null, null, null);
  }

  public static SSLSocket test2(String host, int port) throws IOException {

    final String[] protocols = new String[] {"SSLv2", // Bad
      "TLSv1.3"}; // OK

    SSLSocket socket = (SSLSocket) SSLSocketFactory.getDefault()
      .createSocket(host, port);
    socket.setEnabledProtocols(protocols);
    return socket;
  }
}
