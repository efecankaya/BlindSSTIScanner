package sstiscanner.utils;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.extension.ExtensionUnloadingHandler;

public class UnloadHandler implements ExtensionUnloadingHandler
{
    private final MontoyaApi api;

    public UnloadHandler(MontoyaApi api) {
        this.api = api;
    }

    @Override
    public void extensionUnloaded() {
        this.api.logging().logToOutput("Extension has been unloaded.");
    }
}
