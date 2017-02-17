package com.trix.report.repository;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.Collections;
import java.util.Map;

import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.ModuleHandle;

import com.trix.report.repository.source.SourceStreamLocator;

public class ResourceLocator implements IResourceLocator {
  private SourceStreamLocator sourceStreamLocator;
  
  public ResourceLocator(SourceStreamLocator sourceStreamLocator) {
    this.sourceStreamLocator = sourceStreamLocator;
  }
  @Override
  public URL findResource(
      ModuleHandle moduleHandle, String resourcePath, int type) {    
    return findResource(moduleHandle, resourcePath, type, Collections.emptyMap());
  }

  @Override
  public URL findResource(final ModuleHandle moduleHandle,
      final String fileName, final int type,
      @SuppressWarnings("rawtypes") final Map appContext) {    
    try {
      // The actual URL is not important, it just needs to be valid.
      return new URL(
          null, "resource://localsystem" + fileName, new URLStreamHandler() {
        @Override
        protected URLConnection openConnection(URL url)
            throws IOException {
          return new URLConnection(url) {

            @Override
            public void connect() throws IOException {
              // Connecting may not be needed.
            }

            @Override
            public InputStream getInputStream() throws IOException {
              return sourceStreamLocator.findSource(fileName);
            }
          };
        }
      });
    } catch (MalformedURLException e) {
      // Since it is our own URL, this should not happen.
      throw new IllegalStateException(
          "Hardcoded URL malformed. Please revisit this class.");
    }
  }

}
