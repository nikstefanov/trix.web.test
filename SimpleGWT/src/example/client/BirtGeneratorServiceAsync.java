package example.client;

import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface BirtGeneratorServiceAsync {

	void generateReport(String name, Map params, AsyncCallback<String> callback);

}
