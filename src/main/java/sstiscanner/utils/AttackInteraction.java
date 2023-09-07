package sstiscanner.utils;

import burp.api.montoya.collaborator.Interaction;

public record AttackInteraction(ExecutedAttack attack, Interaction interaction) {

}
