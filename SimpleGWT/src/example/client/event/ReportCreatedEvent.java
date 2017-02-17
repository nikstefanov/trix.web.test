package example.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class ReportCreatedEvent extends GwtEvent<ReportCreatedHandler> {
	
	private static Type<ReportCreatedHandler> TYPE;
	
	private String reportName;
	
	public ReportCreatedEvent(String reportName) {
		this.reportName = reportName;
	}

	public static Type<ReportCreatedHandler> getType() {
		return TYPE != null ? TYPE : (TYPE = new Type<ReportCreatedHandler>());
	}
	
	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	@Override
	protected void dispatch(ReportCreatedHandler handler) {
		handler.onReportCreated(this);
		
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<ReportCreatedHandler> getAssociatedType() {
		return getType();
	}

}
