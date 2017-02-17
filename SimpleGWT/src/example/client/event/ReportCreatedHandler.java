package example.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface ReportCreatedHandler extends EventHandler {
	
	void onReportCreated(ReportCreatedEvent event);

}
