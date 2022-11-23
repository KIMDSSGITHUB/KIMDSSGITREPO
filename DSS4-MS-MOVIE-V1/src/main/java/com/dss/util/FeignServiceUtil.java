package com.dss.util;

import com.dss.entity.Actor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name="ms-actor-service")
public interface FeignServiceUtil {

    @GetMapping("actors/{id}")
    public Actor findActor(@PathVariable("id") UUID id);
}
