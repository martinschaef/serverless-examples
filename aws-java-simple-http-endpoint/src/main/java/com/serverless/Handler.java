package com.serverless;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.apache.log4j.Logger;

import java.io.IOException;

public class Handler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

	private static final Logger LOG = Logger.getLogger(Handler.class);

	@Override
	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
		LOG.info("received: " + input);

    createProcessNoncompliant(input.getBody());

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
}
