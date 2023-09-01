package sstiscanner;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;
import burp.api.montoya.logging.Logging;
import sstiscanner.core.Attacker;
import sstiscanner.core.ScanChecks;
import sstiscanner.engines.Engines;
import sstiscanner.utils.ConfigWindow;

public class SSTIScanner implements BurpExtension {

    MontoyaApi api;
    Logging logger;
    Attacker attacker;

    ConfigWindow configWindow;
    Engines engines;

    @Override
    public void initialize(MontoyaApi api) {
        this.api = api;
        this.api.extension().setName("SSTI Scanner");

        this.logger = api.logging();

        this.engines = new Engines();

        this.configWindow = new ConfigWindow(this.api, this.attacker, this.engines);
        this.api.userInterface().registerSuiteTab("SSTI Scanner", configWindow.getUIComponent());

        this.attacker = new Attacker(this.api, this.engines);

        this.api.scanner().registerScanCheck(new ScanChecks(this.api, this.attacker));


        this.logger.logToOutput("SSTI Scanner loaded");

    }
}
