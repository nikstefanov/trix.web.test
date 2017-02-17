package example.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class SelectReportEvent extends GwtEvent<SelectReportHandler> {
	
	private static Type<SelectReportHandler> TYPE;
	
	private String reportName;
	
	public SelectReportEvent(String reportName) {
		this.reportName = reportName;
	}

	public static Type<SelectReportHandler> getType() {
		return TYPE != null ? TYPE : (TYPE = new Type<SelectReportHandler>());
	}
	
	@Override
	public Type<SelectReportHandler> getAssociatedType() {
		return getType();
	}

	@Override
	protected void dispatch(SelectReportHandler handler) {
		handler.onSelectReport(this);
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

}
