package example.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Tree;

public interface ReportListServiceAsync {
	public void fetchReportList(AsyncCallback<String> callback);
}
