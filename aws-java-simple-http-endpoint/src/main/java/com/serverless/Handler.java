package com.serverless;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.apache.log4j.Logger;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXTransformerFactory;
import java.io.IOException;

public class Handler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

	private static final Logger LOG = Logger.getLogger(Handler.class);
  protected java.sql.Connection connection;

	@Override
	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
		LOG.info("received: " + input);

    createProcessNoncompliant(input.getBody());
    sqlInjection(input.getBody());

		return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody("Hi");
	}

  public void createProcessNoncompliant(String taintedInput) {
    // Noncompliant: user-supplied parameter is passed to an OS command and could be malicious.
    ProcessBuilder pb = new ProcessBuilder("/usr/local/bin/program", "--color", taintedInput);
    try {
      pb.start();
    } catch (IOException e) {
      System.out.println(e);
    }
  }

  private void sqlInjection(String taintedInput) {
    try {
      // Noncompliant: user-given input might contain malicious special characters.
      String sql = "SELECT * FROM people WHERE favorite_color='" + taintedInput + "'";
      java.sql.Statement statement = connection.createStatement();
      statement.execute(sql);
    } catch (java.sql.SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void saxTransformBad(Source source) throws Exception {
    SAXTransformerFactory sf = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
    sf.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
    sf.newXMLFilter(source);
  }
}
