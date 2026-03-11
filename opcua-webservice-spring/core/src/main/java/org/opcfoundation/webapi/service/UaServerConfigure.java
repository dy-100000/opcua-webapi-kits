package org.opcfoundation.webapi.service;

import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;

public class UaServerConfigure {
    private String applicationUri;
    private LocalizedText applicationName;
    private String productUri;
    private ApplicationType applicationType;

    private boolean supportServerUriPath;

    private int browseRequestMaxSize;
    private int readRequestMaxSize;
    private int writeRequestMaxSize;
    private int callRequestMaxSize;
    private int historyReadRequestMaxSize;
    private int translateRequestMaxSize;

    public UaServerConfigure()
    {
        applicationUri = "Unknown";
        applicationName = new LocalizedText("Unknown");
        productUri = "Unknown";
        applicationType = ApplicationType.Server;

        supportServerUriPath = true;

        browseRequestMaxSize = 0;
        readRequestMaxSize = 0;
        writeRequestMaxSize = 0;
        callRequestMaxSize = 0;
        historyReadRequestMaxSize = 0;
        translateRequestMaxSize = 0;
    }

    public ApplicationType getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(ApplicationType applicationType) {
        this.applicationType = applicationType;
    }

    public String getApplicationUri() {
        return applicationUri;
    }

    public void setApplicationUri(String applicationUri)
    {
        this.applicationUri = applicationUri;
    }

    public LocalizedText getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(LocalizedText applicationName)
    {
        this.applicationName = applicationName;
    }

    public String getProductUri()
    {
        return productUri;
    }

    public void setProductUri(String productUri)
    {
        this.productUri = productUri;
    }

    public boolean isServerUriPathSupported() {
        return supportServerUriPath;
    }

    public void setSupportServerUriPath(boolean isSupported) {
        supportServerUriPath = isSupported;
    }

    public int getBrowseRequestMaxSize() {
        return browseRequestMaxSize;
    }

    public void setBrowseRequestMaxSize(int browseRequestMaxSize) {
        this.browseRequestMaxSize = Math.max(browseRequestMaxSize, 0);;
    }

    public int getReadRequestMaxSize() {
        return readRequestMaxSize;
    }

    public void setReadRequestMaxSize(int readRequestMaxSize) {
        this.readRequestMaxSize = Math.max(readRequestMaxSize, 0);
    }

    public int getWriteRequestMaxSize() {
        return writeRequestMaxSize;
    }

    public void setWriteRequestMaxSize(int writeRequestMaxSize) {
        this.writeRequestMaxSize = Math.max(writeRequestMaxSize, 0);
    }

    public int getCallRequestMaxSize() {
        return callRequestMaxSize;
    }

    public void setCallRequestMaxSize(int callRequestMaxSize) {
        this.callRequestMaxSize = Math.max(callRequestMaxSize, 0);
    }

    public int getHistoryReadRequestMaxSize() {
        return historyReadRequestMaxSize;
    }

    public void setHistoryReadRequestMaxSize(int historyReadRequestMaxSize) {
        this.historyReadRequestMaxSize = Math.max(historyReadRequestMaxSize,0);
    }

    public int getTranslateRequestMaxSize() {
        return translateRequestMaxSize;
    }

    public void setTranslateRequestMaxSize(int translateRequestMaxSize) {
        this.translateRequestMaxSize = Math.max(translateRequestMaxSize, 0);
    }
}
