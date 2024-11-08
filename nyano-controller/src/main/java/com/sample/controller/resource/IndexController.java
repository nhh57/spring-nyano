package com.sample.controller.resource;

import com.sample.application.service.event.EventAppService;
import com.sample.application.service.event.EventAppServiceRedis;
import com.sample.domain.model.HiDomainEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/index")
public class IndexController {
    private final EventAppService eventAppService;

    private final EventAppServiceRedis eventAppServiceRedis;

    public IndexController(EventAppService eventAppService, EventAppServiceRedis eventAppServiceRedis) {
        this.eventAppService = eventAppService;
        this.eventAppServiceRedis = eventAppServiceRedis;
    }

    @GetMapping("")
    public String index() {
        Object key = "hainh";
        String data = "";
        data = (String) eventAppServiceRedis.getSayHiRedis(key);

        if (!(data == null)) {
            return data;
        }
        String datas = eventAppService.SayHi("hainh");
        System.out.println("data::" + datas);
        eventAppServiceRedis.saveSayHi("chain");
        return datas;
    }

    @GetMapping("/get-all")
    public List<HiDomainEntity> getAll() {
        return eventAppService.getAllHiEntities();
    }
}
