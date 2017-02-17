package example.server;

import java.util.Map;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import example.client.BirtGeneratorService;

/**
 * The server side implementation of the BirtGeneratorService RPC service.
 */
@SuppressWarnings("serial")
public class BirtGeneratorServiceImpl extends RemoteServiceServlet implements
		BirtGeneratorService {

	@Override
	public String generateReport(String name, Map params) throws IllegalArgumentException {
		ReportHandler rh = new ReportHandler();
		//TODO: Parameteruebergabe implementieren
		String htmlReport = rh.executeHTMLReport(name, params);
		return htmlReport;

	}
}
