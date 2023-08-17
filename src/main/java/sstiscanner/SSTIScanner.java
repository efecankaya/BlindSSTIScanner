package sstiscanner;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;
import burp.api.montoya.logging.Logging;

public class SSTIScanner implements BurpExtension {

    MontoyaApi api;
    Logging logging;

    @Override
    public void initialize(MontoyaApi api) {
        this.api = api;
        this.logging = api.logging();
        this.api.extension().setName("SSTI Scanner");
        this.logging.logToOutput("SSTI Scanner loaded");
    }
}
