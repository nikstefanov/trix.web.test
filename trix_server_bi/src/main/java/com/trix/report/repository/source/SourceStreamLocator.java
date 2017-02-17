package com.trix.report.repository.source;

import java.io.IOException;
import java.io.InputStream;

public interface SourceStreamLocator {
  public InputStream findSource(String sourceName) throws IOException;
}
