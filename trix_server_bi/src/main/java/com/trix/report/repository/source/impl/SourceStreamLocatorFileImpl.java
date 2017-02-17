package com.trix.report.repository.source.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.trix.report.repository.source.SourceStreamLocator;

public class SourceStreamLocatorFileImpl implements SourceStreamLocator {
  private String baseDirectory;
  
  public SourceStreamLocatorFileImpl(String baseDirectory) {
    if (baseDirectory.charAt(baseDirectory.length()-1) != File.separatorChar) {
      baseDirectory = baseDirectory + File.separatorChar;
    }
    this.baseDirectory = baseDirectory;    
  }

  @Override
  public InputStream findSource(String sourceName) throws FileNotFoundException {    
    return new FileInputStream(baseDirectory + sourceName);    
  }

}
