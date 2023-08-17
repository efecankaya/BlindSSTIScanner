package sstiscanner;

import burp.api.montoya.http.message.HttpRequestResponse;

public record ExecutedAttack(String payload, HttpRequestResponse originalRequestResponse, HttpRequestResponse attackRequestResponse) {

}
