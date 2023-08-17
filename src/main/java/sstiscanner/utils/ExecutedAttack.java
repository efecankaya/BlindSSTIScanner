package sstiscanner.utils;

import burp.api.montoya.http.message.HttpRequestResponse;

public record ExecutedAttack(String id, String payload, HttpRequestResponse baseRequestResponse, HttpRequestResponse attackRequestResponse) {

}
