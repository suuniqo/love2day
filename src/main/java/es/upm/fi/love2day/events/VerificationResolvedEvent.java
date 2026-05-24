package es.upm.fi.love2day.events;

import es.upm.fi.love2day.model.VerificationStatus;

public record VerificationResolvedEvent(Long userId, VerificationStatus status) {}
