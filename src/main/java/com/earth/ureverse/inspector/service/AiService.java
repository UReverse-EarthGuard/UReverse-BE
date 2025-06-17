package com.earth.ureverse.inspector.service;

import org.springframework.scheduling.annotation.Async;

import java.io.IOException;
import java.util.List;

public interface AiService {

    @Async
    void aiInspect(List<String> imgUrls, String category, String brandName, Long productId, String senderName, Long userId);
}
