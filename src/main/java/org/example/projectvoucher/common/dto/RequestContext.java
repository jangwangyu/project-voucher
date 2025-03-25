package org.example.projectvoucher.common.dto;

import org.example.projectvoucher.common.type.RequesterType;

public record RequestContext(
    RequesterType requesterType,
    String requesterId
) {

}
