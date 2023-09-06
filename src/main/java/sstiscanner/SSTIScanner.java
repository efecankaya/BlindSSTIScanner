package sstiscanner;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;
import burp.api.montoya.collaborator.CollaboratorClient;
import sstiscanner.core.Attacker;
import sstiscanner.core.ScanChecks;
import sstiscanner.engines.Engines;
import sstiscanner.core.Poller;
import sstiscanner.utils.MyExtensionUnloadingHandler;
import sstiscanner.view.ConfigView;
import sstiscanner.utils.Config;

public class SSTIScanner implements BurpExtension {

    MontoyaApi api;
    Attacker attacker;
    Engines engines;
    Config config;
    ConfigView configView;
    Poller poller;
    CollaboratorClient collaboratorClient;

    @Override
    public void initialize(MontoyaApi api) {
        String name = "Blind SSTI Scanner";

        this.api = api;
        this.api.extension().setName(name);

        this.collaboratorClient = this.api.collaborator().createClient();
        this.poller = new Poller(this.collaboratorClient, this.api);
        this.engines = new Engines();
        this.config = new Config(this.engines, this.poller, this.api);
        this.configView = new ConfigView(this.config);
        this.attacker = new Attacker(this.api, this.engines, this.config, this.collaboratorClient);

        this.api.userInterface().registerSuiteTab("SSTI Scanner", this.configView.$$$getRootComponent$$$());
        this.api.scanner().registerScanCheck(new ScanChecks(this.api, this.attacker));
        this.api.extension().registerUnloadingHandler(new MyExtensionUnloadingHandler(this.api));

        this.api.logging().logToOutput(name + " has been loaded.");
    }
}
