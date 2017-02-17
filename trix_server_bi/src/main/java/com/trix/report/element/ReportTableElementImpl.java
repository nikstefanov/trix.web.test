package com.trix.report.element;

import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.TableHandle;

import com.trix.report.ReportElement;
import com.trix.report.ReportElementConfiguration;
import com.trix.report.element.utilities.FilterUtilities;

public class ReportTableElementImpl extends ReportElement {

  @Override
  public void configure(ReportElementConfiguration reportElementConfiguration) 
      throws Exception {
    FilterUtilities filterUtilities = new FilterUtilities(
        (OdaDataSetHandle)((TableHandle)elementHandle).getDataSet(),
        reportElementConfiguration.getCriteriaJSON());
    filterUtilities.applyJSONFilterConditions();
  }

}
