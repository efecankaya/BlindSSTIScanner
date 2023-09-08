package sstiscanner;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;
import burp.api.montoya.collaborator.CollaboratorClient;
import sstiscanner.core.*;
import sstiscanner.engines.Engines;
import sstiscanner.utils.UnloadHandler;
import sstiscanner.view.ConfigView;

public class SSTIScanner implements BurpExtension {

    MontoyaApi api;
    Attacker attacker;
    Engines engines;
    Config config;
    ConfigView configView;
    Poller poller;
    Attacks attacks;
    CollaboratorClient collaboratorClient;
    InteractionHandler interactionHandler;
    @Override
    public void initialize(MontoyaApi api) {
        this.api = api;
        this.api.extension().setName("Blind SSTI Scanner");

        this.engines = new Engines();
        this.attacks = new Attacks();
        this.interactionHandler = new InteractionHandler(this.api);
        this.collaboratorClient = this.api.collaborator().createClient();
        this.poller = new Poller(this.collaboratorClient, this.attacks, this.api, this.interactionHandler);
        this.config = new Config(this.api, this.engines, this.poller, this.attacks);
        this.configView = new ConfigView(this.config);
        this.attacker = new Attacker(this.api, this.engines, this.config, this.collaboratorClient, this.attacks, this.interactionHandler);

        this.api.userInterface().registerSuiteTab("SSTI Scanner", this.configView.$$$getRootComponent$$$());
        this.api.scanner().registerScanCheck(new ScanChecks(this.api, this.attacker));
        this.api.extension().registerUnloadingHandler(new UnloadHandler(this.api));

        this.api.logging().logToOutput("Blind SSTI Scanner has been loaded");
    }
}
