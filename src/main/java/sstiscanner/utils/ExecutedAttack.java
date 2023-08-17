package sstiscanner.utils;

import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.scanner.audit.insertionpoint.AuditInsertionPoint;
import sstiscanner.engines.Engine;

public record ExecutedAttack(String id, String payload, Engine engine, AuditInsertionPoint insertionPoint, HttpRequestResponse baseRequestResponse, HttpRequestResponse attackRequestResponse) {

}
