package example.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.ui.Tree;

public interface ReportListService extends RemoteService{
	public String fetchReportList();
}
