package example.client;

import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;

public interface BirtGeneratorService extends RemoteService {
	String generateReport(String name, Map params) throws IllegalArgumentException; 

}
