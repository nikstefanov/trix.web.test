package com.trix.web.birt.server;

import java.util.List;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabExtendedItemFactory;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.HierarchyHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.birt.report.model.api.olap.TabularHierarchyHandle;

public class CrossTabTest extends Test {
  private CrosstabReportItemHandle xtabHandle;
  private StringBuilder groupClauseBuilder;
  
  static{
    DESIGN_FILE =
        "D:/Users/User/Development/Workspaces/trix.birt/"
            + "test_birt_report/HillClinic/CrossTabExample.rptdesign";  
    OUTPUT_FILE =
        "D:/Users/User/Development/Projects/HillClinicReports/"
        + "Generated Reports/CrossTabExample.html";
  }

  public CrossTabTest() throws BirtException {
    super();
    xtabHandle = null;
    groupClauseBuilder = new StringBuilder();
  }
  
  public void RunAndRender() throws  Exception {
    renderOptions.setOutputFormat("HTML");
    renderOptions.setOutputFileName(OUTPUT_FILE);
    IReportRunnable design = reportEngine.openReportDesign(DESIGN_FILE);
    
    addCrossTab((ReportDesignHandle) design.getDesignHandle( ));
    dimensions((ReportDesignHandle) design.getDesignHandle( ));
    measures((ReportDesignHandle) design.getDesignHandle( ));
    headers((ReportDesignHandle) design.getDesignHandle( ));
    setGroupClause((ReportDesignHandle) design.getDesignHandle( ),
        groupClauseBuilder.toString());
    
    IRunAndRenderTask task = reportEngine.createRunAndRenderTask(design);    
    task.setRenderOption(renderOptions);
    task.run();
    task.close();  
  }
  
  private void addCrossTab(ReportDesignHandle designHandle) throws Exception {
    ExtendedItemHandle xtab =
        CrosstabExtendedItemFactory.createCrosstabReportItem(
            designHandle,
            (TabularCubeHandle) designHandle.getCubes( ).get(0),
            "MyCrosstab" );
    // check box for avoiding preaggregation of the cube.
    ((TabularCubeHandle) designHandle.getCubes( ).get(0)).setAutoPrimaryKey(true);
    designHandle.getBody().add(xtab);
    IReportItem reportItem = xtab.getReportItem( );
    xtabHandle = (CrosstabReportItemHandle) reportItem;
  }
  
  private void dimensions(ReportDesignHandle designHandle)
      throws SemanticException {
    CubeHandle cubeHandle = (CubeHandle) designHandle.getCubes().get(0);
    insertDimension(
        designHandle, cubeHandle.getDimension("ClinicGroup"),
        ICrosstabConstants.ROW_AXIS_TYPE, -1);
    insertDimension(
        designHandle, cubeHandle.getDimension("YearGroup"),
        ICrosstabConstants.ROW_AXIS_TYPE, -1);
    insertDimension(
        designHandle, cubeHandle.getDimension("SexNameGroup"),
        ICrosstabConstants.COLUMN_AXIS_TYPE, -1);
    groupClauseBuilder.append("ar.clinicId");
    groupClauseBuilder.append(", fromDateTimeYear");
    groupClauseBuilder.append(", pe.sex");
  }
  
  private void measures(ReportDesignHandle designHandle)
      throws SemanticException {
    CubeHandle cubeHandle = (CubeHandle) designHandle.getCubes().get(0);
    insertMeasure(designHandle, cubeHandle.getMeasure("patientsCount"), -1);
  }
  
  private void insertDimension(ReportDesignHandle designHandle,
      DimensionHandle dimension, int axisType, int index)
          throws SemanticException {
    ElementFactory designFactory = designHandle.getElementFactory( );
    DimensionViewHandle dvh =
        xtabHandle.insertDimension(dimension, axisType, index);
    HierarchyHandle hierarchy =
//        (TabularHierarchyHandle) dimension.getContent( TabularDimensionHandle.HIERARCHIES_PROP, 0 );
        dimension.getDefaultHierarchy();
    @SuppressWarnings("rawtypes")
    List levelList =
        hierarchy.getPropertyHandle(TabularHierarchyHandle.LEVELS_PROP).getContents();
    for(Object obj:levelList) {
      LevelHandle levelHandle = (LevelHandle) obj;
//      System.out.println("QN:"+levelHandle.getQualifiedName());
//      System.out.println("N:"+levelHandle.getName());
      LevelViewHandle levelViewHandle = dvh.insertLevel(levelHandle, -1);      
      CrosstabCellHandle cellHandle = levelViewHandle.getCell( );

      DesignElementHandle eii = xtabHandle.getModelHandle( );
//      CrosstabModelUtil.updateHeaderCell(xtabHandle, index, axisType);

      //level computed column
      ComputedColumn bindingColumn =
          StructureFactory.newComputedColumn( eii, levelHandle.getName( ) );
      ComputedColumnHandle bindingHandle =
          ((ReportItemHandle) eii).addColumnBinding( bindingColumn,false );
      bindingColumn.setDataType( levelHandle.getDataType( ) );
      String exp = "dimension[\"" + dimension.getName()+"\"][\""+levelHandle.getName()+"\"]";
      bindingColumn.setExpression( exp);

      //add data element for level
      DataItemHandle dataHandle = designFactory.newDataItem( levelHandle.getName( ) );
      dataHandle.setResultSetColumn( bindingHandle.getName( ) );
      cellHandle.addContent( dataHandle );
    }
  }
  
  private void insertMeasure(ReportDesignHandle designHandle,
      MeasureHandle measure, int index)
          throws SemanticException {
//    System.out.println("Measure: " +measure.getName());
//    System.out.println(measure.getFunction());
//    System.out.println(measure.getMeasureExpression());
//    System.out.println(measure.getDisplayName());
//    System.out.println(measure.getDataType());
//    System.out.println(measure.getDisplayLabel());
//    System.out.println(measure.getQualifiedName());
//    System.out.println(measure.getFullName());
    ElementFactory designFactory = designHandle.getElementFactory( );
    MeasureViewHandle mvh =
        xtabHandle.insertMeasure(measure, index);
    mvh.addHeader( );
    LabelHandle labelHandle = designFactory.newLabel( null );
    labelHandle.setText( "Брой пациенти" );
    mvh.getHeader( ).addContent( labelHandle );
    
    AggregationCellHandle cellHandle = mvh.getCell( );
    ExtendedItemHandle xtModelHandle =
        (ExtendedItemHandle) xtabHandle.getModelHandle( );
    
  //level computed column
    ComputedColumn bindingColumn =
        StructureFactory.newComputedColumn( xtModelHandle, measure.getName( )+"cc" );
    ComputedColumnHandle bindingHandle =
        ((ReportItemHandle) xtModelHandle).addColumnBinding( bindingColumn,false );
    bindingColumn.setDataType( measure.getDataType( ) );
    bindingColumn.setAggregateFunction(measure.getFunction());
    String exp = "measure[\"" + measure.getName()+"\"]";
    bindingColumn.setExpression( exp);
    bindingColumn.addAggregateOn("YearGroup/fromDateTimeYear");
    bindingColumn.addAggregateOn("SexNameGroup/sexName");
//    bindingColumn.setAggregateOn("YearGroup/fromDateTimeYear,SexNameGroup/sexName");
    
  //add data element for level
    DataItemHandle dataHandle = designFactory.newDataItem( measure.getName( )+"data" );
    dataHandle.setResultSetColumn( bindingHandle.getName( ) );
    cellHandle.addContent( dataHandle );
  }
  
  private void headers(ReportDesignHandle designHandle)
      throws SemanticException {
    ElementFactory designFactory = designHandle.getElementFactory( );
    LabelHandle labelHandle0 = designFactory.newLabel( "Label0" );
    labelHandle0.setText( "Label0" );
    xtabHandle.getHeader(0).addContent(labelHandle0);
    
    LabelHandle labelHandle1 = designFactory.newLabel( "Label1" );
    labelHandle1.setText( "Label1" );
    xtabHandle.getHeader(1).addContent(labelHandle1);
    
    LabelHandle labelHandle2 = designFactory.newLabel( "Label2" );
    labelHandle2.setText( "Label2" );
    xtabHandle.getHeader(2).addContent(labelHandle2);
  }
  
  private void setGroupClause(ReportDesignHandle designHandle,
      String groupClauseString) throws SemanticException {
    OdaDataSetHandle odaDataSetHandle =
        (OdaDataSetHandle)designHandle.getDataSets().get(0);
    String query = odaDataSetHandle.getQueryText();
    query = query.replace("#groupClause", "GROUP BY " + groupClauseString);
    odaDataSetHandle.setQueryText(query);    
  }
  
  public static void main(String[] args) {
    try{
      CrossTabTest xtTest = new CrossTabTest();
      xtTest.RunAndRender();
      xtTest.exit();     
    }catch(Exception ex){
      ex.printStackTrace();
    }
  }
  
}
