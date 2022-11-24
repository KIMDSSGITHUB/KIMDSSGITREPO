package com.dss.util;

import com.dss.entity.Movie;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name="ms-movie-service")
public interface FeignServiceUtil {
    @GetMapping("movies/{id}")
    public Movie findMovie(@PathVariable("id") UUID id);
}
