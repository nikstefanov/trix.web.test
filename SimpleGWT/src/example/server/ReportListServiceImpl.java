package example.server;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class ReportListServiceImpl extends RemoteServiceServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String fetchReportList() {
		Tree tree = new Tree();
		
//		TreeItem item = tree.addItem("Statistical Reports");
//		addSubItem(item, "subreport1", "subreport1.rptdesign");
//		item = tree.addItem("WMS Reports");
//		addSubItem(item, "subreport2", "subreport2.rptdesign");
//		return tree.toString();
		return "";
	}
	
	/**
	   * Add a new section of music created by a specific composer.
	   * 
	   * @param parent the parent {@link TreeItem} where the section will be added
	   * @param label the label of the new section of music
	   * @param composerWorks an array of works created by the composer
	   */
	  private void addSubItem(TreeItem parent, String label, String fileName) {
	    TreeItem section = parent.addItem(label);
	    section.setText(fileName);
	  }
	
}
