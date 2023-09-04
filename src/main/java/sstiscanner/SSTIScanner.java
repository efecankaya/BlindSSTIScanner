package sstiscanner;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;
import sstiscanner.core.Attacker;
import sstiscanner.core.ScanChecks;
import sstiscanner.engines.Engines;
import sstiscanner.utils.MyExtensionUnloadingHandler;
import sstiscanner.view.ConfigView;
import sstiscanner.utils.Config;

public class SSTIScanner implements BurpExtension {

    MontoyaApi api;
    Attacker attacker;
    Engines engines;
    Config config;
    ConfigView configView;

    @Override
    public void initialize(MontoyaApi api) {
        String name = "Blind SSTI Scanner";

        this.api = api;
        this.api.extension().setName(name);

        this.engines = new Engines();
        this.config = new Config(engines);
        this.configView = new ConfigView(this.api, this.config);
        this.attacker = new Attacker(this.api, this.engines, this.config);

        this.api.userInterface().registerSuiteTab("SSTI Scanner", configView.$$$getRootComponent$$$());
        this.api.scanner().registerScanCheck(new ScanChecks(this.api, this.attacker));
        this.api.extension().registerUnloadingHandler(new MyExtensionUnloadingHandler(api));

        this.api.logging().logToOutput(name + " has been loaded.");
    }
}
