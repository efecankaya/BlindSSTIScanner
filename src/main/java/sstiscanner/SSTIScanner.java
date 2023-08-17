package sstiscanner;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;
import burp.api.montoya.collaborator.CollaboratorClient;
import burp.api.montoya.logging.Logging;
import sstiscanner.core.Attacker;

public class SSTIScanner implements BurpExtension {

    MontoyaApi api;
    Logging logger;
    Attacker attacker;
    Collaborator collaborator;

    @Override
    public void initialize(MontoyaApi api) {
        this.api = api;
        this.api.extension().setName("SSTI Scanner");

        this.logger = api.logging();
        this.collaborator = new Collaborator(this.api);
        this.attacker = new Attacker(this.api, this.collaborator);

        this.api.scanner().registerScanCheck(new ScanChecks(this.api, this.attacker));

        this.logger.logToOutput("SSTI Scanner loaded");

    }
}
